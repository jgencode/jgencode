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
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author diego.silva
 */
public class ClassDefinitionBuilder extends DefinitionBuilder {

    ClassDefinitionBuilder(String packageDefinition, String className) {
        super(packageDefinition, className);
    }

    /**
     * Builds the code according to the values assigned in its properties. The result is saved in {@link #codeLines}
     * internal property
     */
    @Override
    protected void doBuildCode() {
        codeLines = new ArrayList<>();
        codeLines.add(getPackageDeclaration());
        codeLines.add(System.lineSeparator());
        importClasses();
        var classDeclaration = new StringBuilder();
        classDeclaration.append(Modifier.currentAccessModifier(modifiers));
        classDeclaration.append(StringUtils.SPACE).append("class").append(StringUtils.SPACE);
        classDeclaration.append(className);
        classDeclaration.append('{');

        codeLines.add(classDeclaration.toString());
        codeLines.add(System.lineSeparator());

        codeLines.addAll(createFields());

        codeLines.addAll(createAccessors());

        codeLines.add("}");
    }

    private Collection<? extends String> createAccessors() {
        if (fields.isEmpty()) {
            return Collections.emptyList();
        }
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
        lines.add(System.lineSeparator());
        return lines;
    }

}
