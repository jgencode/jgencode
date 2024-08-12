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

import java.util.Collection;

/**
 * Interface that specifies that a definition has methods
 *
 * @author Diego Silva diego.silva at apuntesdejava.com
 */
public interface DefinitionBuilderWithMethods {

    /**
     * Adds a method definition collection to the current class builder
     *
     * @param methods A collection of method definitions. See
     * #{@link com.jgencode.gen.MethodDefinitionBuilder.MethodDefinition}
     * @return The current builder instance
     */
    DefinitionBuilderWithMethods addMethods(Collection<MethodDefinitionBuilder.MethodDefinition> methods);

    /**
     *
     * @param methodDefinition
     * @return
     */
    DefinitionBuilderWithMethods addMethod(MethodDefinitionBuilder.MethodDefinition methodDefinition);
}
