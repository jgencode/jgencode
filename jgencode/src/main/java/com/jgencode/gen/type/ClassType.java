/*
 * Copyright 2023 Diego Silva diego.silva at apuntesdejava.com.
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
package com.jgencode.gen.type;

import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.stream.Collectors;

import static com.jgencode.gen.util.Constants.COMMA;
import static com.jgencode.gen.util.Constants.LESS_THAN;
import static com.jgencode.gen.util.Constants.MORE_THAN;

/**
 *
 * @author Diego Silva diego.silva at apuntesdejava.com
 */
public class ClassType extends JavaType {

    private final String packageName;
    private final String classNameNoGeneric;
    Map<String, Object> generics;

    ClassType(String packageName, String className) {
        super(className);
        this.packageName = packageName;
        this.classNameNoGeneric = StringUtils.substringBefore(className, LESS_THAN);
    }

    /**
     *
     * @param packageName
     * @param className
     * @return
     */
    public static ClassType createClassTypeWithPackageAndName(String packageName, String className) {
        return new ClassType(packageName, className);
    }

    /**
     *
     * @return
     */
    public String getPackageName() {
        return packageName;
    }

    /**
     *
     * @return
     */
    public String getFullClassName() {
        if (StringUtils.isBlank(packageName)) {
            return classNameNoGeneric;
        }
        return String.format("%s.%s", packageName, classNameNoGeneric);
    }

    /**
     *
     * @return
     */
    public String getClassName() {
        return super.getName();
    }

    /**
     *
     * @return
     */
    public String getClassNameWithGeneric() {
        if (generics == null) {
            return super.getName();
        }
        return String.format("%s%s%s%s", super.getName(), LESS_THAN, generics.values().stream().map(
                type -> type instanceof ClassType ? ((ClassType) type).getClassNameWithGeneric() : (String) type).
                collect(
                        Collectors.joining(COMMA)),
                MORE_THAN);
    }

    /**
     *
     * @return
     */
    public Map<String, Object> getGenerics() {
        return generics;
    }

    /**
     *
     * @return
     */
    @Override
    public String getFullName() {
        return getClassNameWithGeneric();
    }

}
