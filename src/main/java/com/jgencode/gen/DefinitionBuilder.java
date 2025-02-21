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

import com.jgencode.gen.type.AnnotationType;
import com.jgencode.gen.type.ClassType;
import com.jgencode.gen.values.Modifier;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import static com.jgencode.gen.util.Constants.PERIOD;
import static com.jgencode.gen.util.Constants.SEARCH_DOT;
import static java.util.stream.Collectors.toList;

/**
 * Abstract class for class definition constructors
 *
 * @author diego.silva@apuntesdejava.com
 */
public abstract class DefinitionBuilder {

    /**
     *
     */
    protected final String packageDefinition;
    /**
     *
     */
    protected final String className;
    /**
     *
     */
    protected final Set<Modifier> modifiers = new LinkedHashSet<>();
    /**
     *
     */

    protected List<String> codeLines;
    /**
     *
     */
    protected Set<String> classesToImport = new TreeSet<>();
    /**
     *
     */
    protected Set<ClassType> classesTypeToImport = new LinkedHashSet<>();
    /**
     *
     */
    protected Collection<FieldDefinitionBuilder.FieldDefinition> fields;
    /**
     *
     */
    protected Set<AnnotationType> annotationTypes = new LinkedHashSet<>();
    static int spaceIndent = 4;

    /**
     *
     * @param level
     * @return
     */
    public static String getIndentation(int level) {
        return StringUtils.repeat(StringUtils.SPACE, spaceIndent * level);
    }

    /**
     *
     * @param packageDefinition
     * @param className
     */
    DefinitionBuilder(String packageDefinition, String className) {
        this.packageDefinition = packageDefinition;
        this.className = className;
    }

    /**
     *
     * @return
     */
    protected String getPackageDeclaration() {
        return String.format("package %s;%n", packageDefinition);
    }

    /**
     * Create a builder for the definition of a class.
     *
     * @param packageDefinition The definition of the package to which the class belongs. It should be separated by
     * points, just like a package.
     * @param className The name of the class to create
     * @return {@link ClassDefinitionBuilder} itself
     */
    public static ClassDefinitionBuilder createClassBuilder(String packageDefinition, String className) {
        return new ClassDefinitionBuilder(packageDefinition, className);
    }

    /**
     *
     * @param packageDefinition
     * @param className
     * @return
     */
    public static RecordDefinitionBuilder createRecordBuilder(String packageDefinition, String className) {
        return new RecordDefinitionBuilder(packageDefinition, className);
    }

    /**
     *
     * @param packageDefinition
     * @param interfaceName
     * @return
     */
    public static InterfaceDefinitionBuilder createInterfaceBuilder(String packageDefinition, String interfaceName) {
        return new InterfaceDefinitionBuilder(packageDefinition, interfaceName);
    }

    /**
     *
     * @param classType
     * @return
     */
    public DefinitionBuilder addClassToImport(ClassType classType) {
        classesTypeToImport.add(classType);
        return this;
    }

    /**
     *
     * @param annotationType
     * @return
     */
    public DefinitionBuilder addAnnotationType(AnnotationType annotationType) {
        this.annotationTypes.add(annotationType);
        return this;
    }

    /**
     *
     * @param fieldDefinition
     * @return
     */
    public DefinitionBuilder addField(FieldDefinitionBuilder.FieldDefinition fieldDefinition) {
        Optional.ofNullable(fields).orElseGet(() -> this.fields = new LinkedHashSet<>()).add(fieldDefinition);
        return this;
    }

    /**
     *
     * @return
     */
    public String getPackageDefinition() {
        return packageDefinition;
    }

    /**
     *
     * @return
     */
    public String getClassName() {
        return className;
    }

    /**
     *
     * @param modifier
     * @return
     */
    public DefinitionBuilder addModifier(Modifier modifier) {
        this.modifiers.add(modifier);
        return this;
    }

    /**
     *
     * @return
     */
    public Definition build() {
        doBuildCode();
        var definition = new Definition();
        definition.className = className;
        definition.modifiers = modifiers;
        definition.packageDefinition = packageDefinition;
        definition.packagePath = createPackagePath();
        definition.codeLines = codeLines;
        return definition;
    }

    /**
     * do build code
     */
    abstract protected void doBuildCode();

    private Path createPackagePath() {
        var packageDefinitionArray = packageDefinition.split(SEARCH_DOT);
        return Paths.get(packageDefinitionArray[0], ArrayUtils.subarray(packageDefinitionArray, 1,
                packageDefinitionArray.length));
    }

    /**
     *
     * @param fieldsDefinition
     * @return
     */
    public DefinitionBuilder addFields(Collection<FieldDefinitionBuilder.FieldDefinition> fieldsDefinition) {
        Optional.ofNullable(fields).orElseGet(() -> this.fields = new LinkedHashSet<>()).addAll(fieldsDefinition);
        return this;
    }

    /**
     *
     * @return
     */
    protected List<String> createFields() {
        if (fields == null || fields.isEmpty()) {
            return Collections.emptyList();
        }
        return fields.stream().flatMap(field -> field.sourceLines.stream())
                .map(line -> String.format("%s%s", getIndentation(1), line))
                .collect(toList());
    }

    /**
     * create import from classes
     */
    protected void importClasses() {
        classesToImport.addAll(
                classesTypeToImport.stream().map(ClassType::getFullClassName).collect(Collectors.toSet())
        );
        //from fields
        if (fields != null) {
            classesToImport.addAll(fields.stream().filter(
                    field -> field.getClassType() != null && StringUtils.isNotBlank(
                    field.getClassType().getPackageName())).map(field -> field.getClassType().getFullClassName())
                    .filter(
                            StringUtils::isNotBlank).collect(Collectors.toSet()));
            //from annotation
            classesToImport.addAll(
                    fields.stream().flatMap(field -> field.getAnnotationType().stream())
                            .map(annotationType -> annotationType.getClassType().getFullClassName())
                            .collect(toList()));
        }
        //from class annotations
        annotationTypes.forEach(annotationType -> classesToImport.add(annotationType.getClassType()
                .getFullClassName()));
        annotationTypes.stream().flatMap(annotationType -> annotationType.getAttributes().values().stream())
                .filter(value -> value instanceof ClassType)
                .map(ClassType.class::cast)
                .forEach(classType -> classesToImport.add(classType.getFullClassName()));

        classesToImport
                .stream()
                .filter(clazz -> !clazz.equals("Override"))
                .filter(clazz -> {
                    var packageClassName = StringUtils.substringBeforeLast(clazz, PERIOD);
                    return !StringUtils.equals(packageClassName, packageDefinition);
                })
                .forEach(classToImport -> codeLines.add(String.format("import %s;", classToImport)));
    }

    /**
     * A definition class
     */
    public static class Definition {

        private String className;
        private Set<Modifier> modifiers;
        private String packageDefinition;
        private Path packagePath;
        private List<String> codeLines;

        /**
         *
         * @return
         */
        public List<String> getCodeLines() {
            return codeLines;
        }

        /**
         *
         * @return
         */
        public String getClassName() {
            return className;
        }

        /**
         *
         * @return
         */
        public Set<Modifier> getModifiers() {
            return modifiers;
        }

        /**
         *
         * @return
         */
        public String getPackageDefinition() {
            return packageDefinition;
        }

        /**
         *
         * @return
         */
        public Path getPackagePath() {
            return packagePath;
        }

    }
}
