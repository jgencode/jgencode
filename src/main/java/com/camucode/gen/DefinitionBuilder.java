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

import java.lang.reflect.AccessFlag;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 *
 * @author diego.silva@apuntesdejava.com
 */
public class DefinitionBuilder {

    private final String packageDefinition;
    private final String className;
    private Set<AccessFlag> accessFlags = new LinkedHashSet<>();

    DefinitionBuilder(String packageDefinition, String className) {
        this.packageDefinition = packageDefinition;
        this.className = className;
    }

    /**
     * Create a builder for the definition of a class.
     *
     * @param packageDefinition The definition of the package to which the class
     * belongs. It should be separated by points, just like a package.
     * @param className The name of the class to create
     * @return
     */
    public static ClassDefinitionBuilder createClassBuilder(String packageDefinition, String className) {
        return new ClassDefinitionBuilder(packageDefinition, className);
    }

    public String getPackageDefinition() {
        return packageDefinition;
    }

    public String getClassName() {
        return className;
    }

    public DefinitionBuilder addAccessFlag(AccessFlag accessFlag) {
        this.accessFlags.add(accessFlag);
        return this;
    }
    public Definition build(){
        return new Definition();
    }
    
    public static class Definition{
        
    }
}
