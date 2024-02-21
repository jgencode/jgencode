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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * @author diego.silva
 */
public class ClassDefinitionBuilder extends DefinitionBuilder implements DefinitionBuilderWithMethods {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClassDefinitionBuilder.class);

    private Collection<MethodDefinitionBuilder.MethodDefinition> methods;

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
        MethodUtil.importClassesFromMethods(methods, classesToImport);
        importClasses();
        var classDeclaration = new StringBuilder(System.lineSeparator());
        classDeclaration.append(Modifier.currentAccessModifier(modifiers));
        classDeclaration.append(StringUtils.SPACE).append("class").append(StringUtils.SPACE);
        classDeclaration.append(className);
        classDeclaration.append('{');

        codeLines.add(classDeclaration.toString());

        codeLines.addAll(createFields());

        if (fields != null && !fields.isEmpty()) {
            codeLines.addAll(createAccessors());
        }

        if (methods != null) {
            methods.forEach(method -> codeLines.addAll(method.getSourceLines().stream().map(
                line -> String.format("%s%s", getIndentation(1), line)).collect(toList()))
            );
        }

        codeLines.add("}");
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
    public ClassDefinitionBuilder addMethods(Collection<MethodDefinitionBuilder.MethodDefinition> methods) {
        this.methods = methods;
        return this;
    }

}
