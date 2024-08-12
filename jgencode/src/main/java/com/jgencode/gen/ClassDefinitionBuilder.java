/*
 * Copyright 2023 diego.silva.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jgencode.gen;

import com.jgencode.gen.type.ClassType;
import com.jgencode.gen.type.ClassTypeBuilder;
import com.jgencode.gen.util.Constants;
import static com.jgencode.gen.util.Constants.CLASSNAME_PARAMETER;
import static com.jgencode.gen.util.Constants.COMMA_SPACE;
import static com.jgencode.gen.util.Constants.GENERAL_CLASSES;
import static com.jgencode.gen.util.Constants.LESS_THAN;
import static com.jgencode.gen.util.Constants.MORE_THAN;
import static com.jgencode.gen.util.Constants.PERIOD;
import com.jgencode.gen.util.MethodUtil;
import com.jgencode.gen.values.Modifier;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.SPACE;

/**
 * Class definition constructor class
 *
 * @author diego.silva
 */
public class ClassDefinitionBuilder extends DefinitionBuilder implements DefinitionBuilderWithMethods {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClassDefinitionBuilder.class);

    private Collection<MethodDefinitionBuilder.MethodDefinition> methods;

    private Collection<ConstructorDefinitionBuilder.ConstructorDefinition> constructors;

    private final Collection<ClassType> interfacesImplements = new LinkedHashSet<>();

    private ClassType classExtended;

    ClassDefinitionBuilder(String packageDefinition, String className) {
        super(packageDefinition, className);
    }

    /**
     * Creates import statements based on class definitions
     */
    @Override
    protected void importClasses() {
        LOGGER.debug("import classes from class definition {}", className);
        //from interfaces
        interfacesImplements.forEach(interfaceImplement -> classesToImport.add(interfaceImplement.getFullClassName()));
        //from extended
        if (classExtended != null) {
            classesToImport.add(classExtended.getFullClassName());
        }

        super.importClasses();
        LOGGER.debug("Classes to import: {}", classesToImport);
    }

    /**
     * Builds the code according to the values assigned in its properties. The result is saved in {@link #codeLines}
     * internal property
     */
    @Override
    protected void doBuildCode() {
        LOGGER.debug("building code {}", className);
        codeLines = new ArrayList<>();
        codeLines.add(getPackageDeclaration());
        MethodUtil.importClassesFromMethods(methods, classesToImport);
        importClasses();
        var classDeclaration = new StringBuilder(System.lineSeparator());
        annotationTypes.forEach(annotationType -> {
            for (String s : annotationType.createSourceLines()) {
                classDeclaration.append(s).append(System.lineSeparator());
            }
        });

        classDeclaration.append(Modifier.currentAccessModifier(modifiers));
        classDeclaration.append(SPACE).append("class").append(SPACE);
        classDeclaration.append(className);
        addClassExtendedToDeclaration(classDeclaration);
        addInterfaceImplementsToDeclaration(classDeclaration);
        classDeclaration.append('{');

        codeLines.add(classDeclaration.toString());

        codeLines.addAll(createFields());

        if (fields != null && !fields.isEmpty()) {
            codeLines.addAll(createAccessors());
        }

        if (constructors != null) {
            constructors.forEach(constructor -> codeLines.addAll(constructor.getSourceLines().stream()
                    .map(line -> StringUtils.replace(line, CLASSNAME_PARAMETER, className))
                    .map(
                            line -> String.format("%s%s", getIndentation(1), line)).collect(toList()))
            );
        }

        if (methods != null) {
            methods.forEach(method -> codeLines.addAll(method.getSourceLines().stream().map(
                    line -> String.format("%s%s", getIndentation(1), line)).collect(toList()))
            );
        }

        codeLines.add("}");
    }

    private void addClassExtendedToDeclaration(StringBuilder classDeclaration) {
        if (classExtended == null) {
            return;
        }

        var declaration = new StringBuilder(classExtended.getClassName());
        if (classExtended.getGenerics() != null) {
            declaration.append(LESS_THAN);
            var params
                    = classExtended.getGenerics().values().stream().map(genericType -> {
                        if (genericType instanceof ClassType genericTypeParam) {
                            String typeParamClassName = genericTypeParam.getClassName();
                            String packageName = null;
                            if (StringUtils.contains(typeParamClassName, PERIOD)) {
                                typeParamClassName = StringUtils.substringAfterLast(genericTypeParam.getClassName(),
                                        PERIOD);
                                packageName = StringUtils.substringBeforeLast(genericTypeParam.getClassName(), PERIOD);
                            }
                            var typeParam = ClassTypeBuilder.newBuilder()
                                    .className(typeParamClassName)
                                    .packageName(packageName)
                                    .build();

                            var classToImport = typeParam.getFullClassName();
                            classesToImport.add(classToImport);
                            return typeParam.getClassName();
                        }
                        if (GENERAL_CLASSES.containsKey((String) genericType)) {
                            classesToImport.add(GENERAL_CLASSES.get((String) genericType));
                        }
                        return (String) genericType;

                    }).collect(Collectors.joining(COMMA_SPACE));
            declaration.append(params);
            declaration.append(MORE_THAN);
        }

        classDeclaration.append(" extends ").append(declaration);

    }

    private void addInterfaceImplementsToDeclaration(StringBuilder classDeclaration) {
        if (interfacesImplements.isEmpty()) {
            return;
        }
        classDeclaration.append(" implements ");

        var interfaces
                = interfacesImplements.stream().map(ClassType::getClassName)
                        .collect(Collectors.joining(Constants.COMMA_SPACE));
        classDeclaration.append(interfaces);
        classDeclaration.append(SPACE);
    }

    /**
     * Add the implements declarations of the interfaces
     *
     * @param interfaceType Interface type
     * @return this same object
     */
    public ClassDefinitionBuilder addInterfaceImplements(ClassType interfaceType) {
        interfacesImplements.add(interfaceType);
        return this;
    }

    /**
     * Add the definition of the class to extend
     *
     * @param classExtended Definition class to extend
     * @return this same builder object
     */
    public ClassDefinitionBuilder classExtended(ClassType classExtended) {
        this.classExtended = classExtended;
        return this;
    }

    private Collection<? extends String> createAccessors() {

        List<String> lines = new ArrayList<>();
        fields.forEach(field -> {
            if (field.isSetter()) {
                var fieldCapitalized = StringUtils.capitalize(field.getFieldName());
                var fieldType = field.getFieldType();
                lines.add(String.format("%spublic void set%s(%s %s){", getIndentation(1), fieldCapitalized, fieldType,
                        field.getFieldName()));
                lines.add(String.format("%1$sthis.%2$s = %2$s;", getIndentation(2), field.getFieldName()));
                lines.add(String.format("%s}%n", getIndentation(1)));
            }
            if (field.isGetter()) {
                var fieldCapitalized = StringUtils.capitalize(field.getFieldName());
                var fieldType = field.getFieldType();
                lines.add(String.format("%spublic %s get%s(){", getIndentation(1), fieldType, fieldCapitalized));
                lines.add(String.format("%1$sreturn %2$s;", getIndentation(2), field.getFieldName()));
                lines.add(String.format("%s}%n", getIndentation(1)));
            }
        });
        return lines;
    }

    @Override
    public ClassDefinitionBuilder addMethods(Collection<MethodDefinitionBuilder.MethodDefinition> methodDefinitions) {
        Optional.ofNullable(this.methods).orElseGet(() -> this.methods = new LinkedHashSet<>())
                .addAll(methodDefinitions);
        return this;
    }

    /**
     * Add the constructor definition
     *
     * @param constructorDefinition constructor definition
     * @return the same object builder
     */
    public ClassDefinitionBuilder addConstructor(
            ConstructorDefinitionBuilder.ConstructorDefinition constructorDefinition) {
        Optional.ofNullable(this.constructors).orElseGet(() -> this.constructors = new LinkedHashSet<>()).add(
                constructorDefinition);
        return this;
    }

    /**
     * Add the definition of a method
     *
     * @param methodDefinition a method definition
     * @return the same object builder
     */
    @Override
    public DefinitionBuilderWithMethods addMethod(MethodDefinitionBuilder.MethodDefinition methodDefinition) {
        Optional.ofNullable(this.methods).orElseGet(() -> this.methods = new LinkedHashSet<>()).add(methodDefinition);
        return this;
    }

}
