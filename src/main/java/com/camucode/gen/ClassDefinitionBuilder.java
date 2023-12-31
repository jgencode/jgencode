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
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author diego.silva
 */
public class ClassDefinitionBuilder extends DefinitionBuilder {

    ClassDefinitionBuilder(String packageDefinition, String className) {
        super(packageDefinition, className);
    }

    @Override
    protected void doBuildCode() {
        codeLines = new ArrayList<>();
        codeLines.add(getPackageDeclaration());
        codeLines.add(System.lineSeparator());
        var classesToImport = importClasses();
        classesToImport.forEach(classToImport -> codeLines.add(String.format("import %s;", classToImport)));
        var classDeclaration = new StringBuilder();
        classDeclaration.append(Modifier.currentAccessModifier(modifiers));
        classDeclaration.append(StringUtils.SPACE).append("class").append(StringUtils.SPACE);
        classDeclaration.append(className);
        classDeclaration.append('{');

        codeLines.add(classDeclaration.toString());
        codeLines.add(System.lineSeparator());

        codeLines.addAll(createFields());
        codeLines.add(System.lineSeparator());

        codeLines.add("}");
    }

    private List<String> createFields() {
        List<String> lines = new ArrayList<>();
        fields.forEach(field -> lines.add(String.format("  %s%n", field.sourceLine)));
        return lines;
    }

    private Set<String> importClasses() {
        //from fields
        return fields.stream()
            .filter(field -> field.getClassType() != null)
            .map(field -> field.getClassType().getFullClassName())
            .filter(StringUtils::isNotBlank)
            .collect(Collectors.toSet());
    }

}
