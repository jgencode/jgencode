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
package com.jgencode.gen.util;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author Diego Silva diego.silva at apuntesdejava.com
 */
public class ClassUtil {

    /**
     *
     */
    public static final String[] NATIVE_TYPES = {"byte", "Byte", "short", "Short", "int", "Integer", "long", "Long", "float", "Float", "double", "Double", "boolean", "Boolean", "char", "Character", "String"};

    /**
     *
     * @param packageName
     * @param className
     * @return
     */
    public static String removeClassFromPackage(String packageName, String className) {
        return StringUtils.substringBefore(packageName, "." + className);
    }

    /**
     *
     * @param className
     * @return
     */
    public static boolean isNative(String className) {
        return (ArrayUtils.contains(NATIVE_TYPES, className));
    }

    /**
     *
     * @param className
     * @return
     */
    public static boolean isNotNative(String className) {
        return !isNative(className);
    }

}
