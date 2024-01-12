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

import com.camucode.gen.type.ClassType;
import com.camucode.gen.util.Constants;
import com.camucode.gen.values.Modifier;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Diego Silva <diego.silva at apuntesdejava.com>
 */
public class InterfaceDefinitionBuilder extends DefinitionBuilder {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(InterfaceDefinitionBuilder.class);
    
    private final List<ClassType> interfacesExtends = new LinkedList<>();
    
    InterfaceDefinitionBuilder(String packageDefinition, String className) {
        super(packageDefinition, className);
    }
    
    public DefinitionBuilder addInterfaceExtend(ClassType interfaceType) {
        interfacesExtends.add(interfaceType);
        return this;
    }
    
    @Override
    protected void doBuildCode() {
        codeLines = new ArrayList<>();
        codeLines.add(getPackageDeclaration());
        codeLines.add(System.lineSeparator());
        
        var classDeclaration = new StringBuilder();
        classDeclaration.append(Modifier.currentAccessModifier(modifiers));
        classDeclaration.append(StringUtils.SPACE).append("interface").append(StringUtils.SPACE);
        classDeclaration.append(className);
        addInterfacesExtendsCode(classDeclaration);
        classDeclaration.append('{');
        
        importClasses();
        codeLines.add(classDeclaration.toString());
        
        codeLines.add("}");
    }
    
    private void addInterfacesExtendsCode(StringBuilder classDeclaration) {
        var interfacesExtendsCode = interfacesExtends.stream().map(interfaceExtend -> {
            var declaration = new StringBuilder(interfaceExtend.getClassName());
            if (interfaceExtend.getGenerics() != null) {
                declaration.append(Constants.LESS_THAN);
                var params
                    = interfaceExtend.getGenerics().values().stream().map(genericType -> {
                        if (genericType instanceof ClassType) {
                            var genericTypeParam = (ClassType) genericType;
                            var classToImport = genericTypeParam.getFullClassName();
                            classesToImport.add(classToImport);
                            return genericTypeParam.getClassName();
                        }
                        return (String) genericType;
                        
                    }).collect(Collectors.joining(","));
                declaration.append(params);
                declaration.append(Constants.MORE_THAN);
            }
            return declaration.toString();
        }).collect(Collectors.joining(","));
        if (StringUtils.isNotBlank(interfacesExtendsCode)) {
            classDeclaration.append(" extends ").append(interfacesExtendsCode);
        }
    }
    
}
