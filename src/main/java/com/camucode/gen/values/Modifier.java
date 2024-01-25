/*
 * Copyright 2023 Diego Silva <diego.silva at apuntesdejava.com>.
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
package com.camucode.gen.values;

import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.Set;

/**
 * @author Diego Silva <diego.silva at apuntesdejava.com>
 */
public enum Modifier {
    PUBLIC, PRIVATE, PROTECTED, FINAL, ABSTRACT, STATIC;
    public static final Set<Modifier> ACCESS_MODIFIERS = Set.of(Modifier.PUBLIC, Modifier.PRIVATE, Modifier.PROTECTED);
    public static final Set<Modifier> METHOD_ACCESS_MODIFIERS = Set.of(Modifier.PUBLIC,
        Modifier.PRIVATE,
        Modifier.PROTECTED, ABSTRACT, STATIC);

    public static String currentAccessModifier(Collection<Modifier> modifiers) {
        for (Modifier modifier : Modifier.ACCESS_MODIFIERS) {
            if (modifiers.contains(modifier)) {
                return StringUtils.lowerCase(modifier.name());
            }
        }
        return StringUtils.EMPTY;
    }

    public static String currentMethodAccessModifier(Collection<Modifier> modifiers) {
        for (Modifier modifier : Modifier.METHOD_ACCESS_MODIFIERS) {
            if (modifiers.contains(modifier)) {
                return StringUtils.lowerCase(modifier.name());
            }
        }
        return StringUtils.EMPTY;
    }
}
