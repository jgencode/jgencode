/*
 * Copyright 2024 Diego Silva <diego.silva at apuntesdejava.com>.
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
import java.util.ArrayList;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author Diego Silva <diego.silva at apuntesdejava.com>
 */
public class InterfaceDefinitionBuilder extends DefinitionBuilder {

    InterfaceDefinitionBuilder(String packageDefinition, String className) {
        super(packageDefinition, className);
    }

    @Override
    protected void doBuildCode() {
        codeLines = new ArrayList<>();
        codeLines.add(getPackageDeclaration());
        codeLines.add(System.lineSeparator());
        var classesToImport = importClasses();

        var classDeclaration = new StringBuilder();
        classDeclaration.append(Modifier.currentAccessModifier(modifiers));
        classDeclaration.append(StringUtils.SPACE).append("interface").append(StringUtils.SPACE);
        classDeclaration.append(className);
        classDeclaration.append('{');

        codeLines.add(classDeclaration.toString());

        codeLines.add("}");
    }

}
