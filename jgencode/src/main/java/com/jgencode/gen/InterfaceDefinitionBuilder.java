/*
 * Copyright 2024 Diego Silva diego.silva at apuntesdejava.com.
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
import static com.jgencode.gen.util.Constants.COMMA;
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

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * Interface definition constructor class
 *
 * @author Diego Silva diego.silva at apuntesdejava.com
 */
public class InterfaceDefinitionBuilder extends DefinitionBuilder implements DefinitionBuilderWithMethods {

    private static final Logger LOGGER = LoggerFactory.getLogger(InterfaceDefinitionBuilder.class);

    private final List<ClassType> interfacesExtends = new LinkedList<>();
    private Collection<MethodDefinitionBuilder.MethodDefinition> methods;

    InterfaceDefinitionBuilder(String packageDefinition, String className) {
        super(packageDefinition, className);
    }

    /**
     *
     * @param interfaceType
     * @return
     */
    public DefinitionBuilder addInterfaceExtend(ClassType interfaceType) {
        interfacesExtends.add(interfaceType);
        return this;
    }

    @Override
    protected void importClasses() {
        LOGGER.debug("import classes from interface definition {}", className);

        //from extended
        interfacesExtends.forEach(interfaceExtend -> {
            classesToImport.add(interfaceExtend.getFullClassName());
            Optional.ofNullable(interfaceExtend.getGenerics()).ifPresent(generics -> {
                classesToImport.addAll(
                        generics.values().stream().filter(value -> value instanceof ClassType)
                                .map(ClassType.class::cast)
                                .map(ClassType::getFullClassName)
                                .collect(toList()));
            });
        });

        super.importClasses();
        LOGGER.debug("Classes to import: {}", classesToImport);
    }

    @Override
    protected void doBuildCode() {
        codeLines = new ArrayList<>();
        codeLines.add(getPackageDeclaration());

        var classDeclaration = new StringBuilder();
        annotationTypes.forEach(annotationType -> {
            for (String s : annotationType.createSourceLines()) {
                classDeclaration.append(s).append(System.lineSeparator());
            }
        });
        classDeclaration.append(Modifier.currentAccessModifier(modifiers));
        classDeclaration.append(StringUtils.SPACE).append("interface").append(StringUtils.SPACE);
        classDeclaration.append(className);
        addInterfacesExtendsCode(classDeclaration);
        classDeclaration.append('{');

        MethodUtil.importClassesFromMethods(methods, classesToImport);

        importClasses();
        codeLines.add(classDeclaration.toString());
        codeLines.addAll(createFields());

        if (methods != null) {
            methods.forEach(method -> codeLines.addAll(method.getSourceLines().stream().map(
                    line -> String.format("%s%s", getIndentation(1), line)).collect(toList()))
            );
        }

        codeLines.add("}");
    }

    private void addInterfacesExtendsCode(StringBuilder classDeclaration) {
        var interfacesExtendsCode = interfacesExtends.stream().map(interfaceExtend -> {
            var declaration = new StringBuilder(interfaceExtend.getClassName());
            if (interfaceExtend.getGenerics() != null) {
                declaration.append(LESS_THAN);
                var params
                        = interfaceExtend.getGenerics().values().stream().map(genericType -> {
                            if (genericType instanceof ClassType genericTypeParam) {
                                String className = genericTypeParam.getClassName();
                                String packageName = null;
                                if (StringUtils.contains(className, PERIOD)) {
                                    className = StringUtils.substringAfterLast(genericTypeParam.getClassName(), PERIOD);
                                    packageName = StringUtils.substringBeforeLast(genericTypeParam.getClassName(),
                                            PERIOD);
                                } else {
                                    className = genericTypeParam.getFullClassName();
                                }
                                var typeParam = ClassTypeBuilder.newBuilder()
                                        .className(className)
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
            return declaration.toString();
        }).collect(Collectors.joining(COMMA));
        if (StringUtils.isNotBlank(interfacesExtendsCode)) {
            classDeclaration.append(" extends ").append(interfacesExtendsCode);
        }
    }

    @Override
    public DefinitionBuilderWithMethods addMethods(
            Collection<MethodDefinitionBuilder.MethodDefinition> methodDefinitions) {
        Optional.ofNullable(this.methods).orElseGet(() -> this.methods = new LinkedHashSet<>()).
                addAll(methodDefinitions);
        return this;
    }

    /**
     *
     * @param methodDefinition
     * @return
     */
    @Override
    public DefinitionBuilderWithMethods addMethod(MethodDefinitionBuilder.MethodDefinition methodDefinition) {
        Optional.ofNullable(this.methods).orElseGet(() -> this.methods = new LinkedHashSet<>()).add(methodDefinition);
        return this;
    }

}
