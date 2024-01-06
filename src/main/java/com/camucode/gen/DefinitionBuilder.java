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
package com.camucode.gen;

import com.camucode.gen.values.Modifier;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang3.ArrayUtils;
import static com.camucode.gen.util.Constants.SEARCH_DOT;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.toList;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author diego.silva@apuntesdejava.com
 */
public abstract class DefinitionBuilder {

    protected final String packageDefinition;
    protected final String className;
    protected final Set<Modifier> modifiers = new LinkedHashSet<>();

    protected List<String> codeLines;
    protected Collection<FieldDefinitionBuilder.FieldDefinition> fields;

    protected int spaceIndent = 2;

    String getIndentation(int level) {
        return StringUtils.repeat(StringUtils.SPACE, spaceIndent * level);
    }

    DefinitionBuilder(String packageDefinition, String className) {
        this.packageDefinition = packageDefinition;
        this.className = className;
    }

    protected String getPackageDeclaration() {
        return String.format("package %s;", packageDefinition);
    }

    /**
     * Create a builder for the definition of a class.
     *
     * @param packageDefinition The definition of the package to which the class belongs. It should be separated by
     * points, just like a package.
     * @param className The name of the class to create
     * @return
     */
    public static ClassDefinitionBuilder createClassBuilder(String packageDefinition, String className) {
        return new ClassDefinitionBuilder(packageDefinition, className);
    }

    public static InterfaceDefinitionBuilder createInterfaceBuilder(String packageDefinition, String interfaceName) {
        return new InterfaceDefinitionBuilder(packageDefinition, interfaceName);
    }

    public String getPackageDefinition() {
        return packageDefinition;
    }

    public String getClassName() {
        return className;
    }

    public DefinitionBuilder addModifier(Modifier modifier) {
        this.modifiers.add(modifier);
        return this;
    }

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

    abstract protected void doBuildCode();

    private Path createPackagePath() {
        var packageDefinitionArray = packageDefinition.split(SEARCH_DOT);
        return Paths.get(packageDefinitionArray[0], ArrayUtils.subarray(packageDefinitionArray, 1,
            packageDefinitionArray.length));
    }

    public DefinitionBuilder addFields(Collection<FieldDefinitionBuilder.FieldDefinition> fieldsDefinition) {
        this.fields = fieldsDefinition;
        return this;
    }

    protected List<String> createFields() {
        if (fields == null || fields.isEmpty()) {
            return Collections.emptyList();
        }
        List<String> lines = fields.stream().map(field -> String.format("%s%s%n", getIndentation(1), field.sourceLine)).collect(
            toList());
        lines.add(System.lineSeparator());
        return lines;
    }

    protected Set<String> importClasses() {
        if (fields == null || fields.isEmpty()) {
            return Collections.emptySet();
        }
        //from fields
        Set<String> classesToImport = fields.stream().filter(
            field -> field.getClassType() != null && StringUtils.isNotBlank(
            field.getClassType().getPackageName())).map(field -> field.getClassType().getFullClassName()).filter(
            StringUtils::isNotBlank).collect(Collectors.toSet());
        classesToImport.forEach(classToImport -> codeLines.add(String.format("import %s;", classToImport)));

        return classesToImport;
    }

    public static class Definition {

        private String className;
        private Set<Modifier> modifiers;
        private String packageDefinition;
        private Path packagePath;
        private List<String> codeLines;

        public List<String> getCodeLines() {
            return codeLines;
        }

        public String getClassName() {
            return className;
        }

        public Set<Modifier> getModifiers() {
            return modifiers;
        }

        public String getPackageDefinition() {
            return packageDefinition;
        }

        public Path getPackagePath() {
            return packagePath;
        }

    }
}
