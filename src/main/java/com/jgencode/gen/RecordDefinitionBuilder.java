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

import com.jgencode.gen.values.Modifier;
import java.util.ArrayList;
import java.util.stream.Collectors;

import static com.jgencode.gen.util.Constants.COMMA_SPACE;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.SPACE;

/**
 *
 * @author Diego Silva diego.silva at apuntesdejava.com
 */
public class RecordDefinitionBuilder extends DefinitionBuilder {

    /**
     *
     * @param packageDefinition
     * @param className
     */
    public RecordDefinitionBuilder(String packageDefinition, String className) {
        super(packageDefinition, className);
    }

    @Override
    protected void doBuildCode() {
        codeLines = new ArrayList<>();
        codeLines.add(getPackageDeclaration());
        importClasses();
        codeLines.add(EMPTY);
        var recordDeclaration = new StringBuilder(Modifier.currentAccessModifier(modifiers));
        recordDeclaration.append(SPACE).append("record ").append(className).append('(');
        if (fields != null) {
            recordDeclaration.append(
                    fields.stream().map(field -> field.getFieldType() + SPACE + field.getFieldName()).collect(
                            Collectors.joining(COMMA_SPACE))
            );
        }
        recordDeclaration.append(')');
        recordDeclaration.append(" {");
        codeLines.add(recordDeclaration.toString());
        codeLines.add("}");

    }

}
