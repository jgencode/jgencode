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
package com.camucode.gen.type;

import static com.camucode.gen.util.Constants.LESS_THAN;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author Diego Silva <diego.silva at apuntesdejava.com>
 */
public class ClassType {

    private final String packageName;
    private final String className;
    private final String classNameNoGeneric;
    Map<String, Object> generics;

    ClassType(String packageName, String className) {
        this.packageName = packageName;
        this.className = className;
        this.classNameNoGeneric = StringUtils.substringBefore(className, LESS_THAN);
    }

    public static ClassType createClassTypeWithPackageAndName(String packageName, String className) {
        return new ClassType(packageName, className);
    }

    public String getPackageName() {
        return packageName;
    }

    public String getFullClassName() {
        if (StringUtils.isBlank(packageName)) {
            return classNameNoGeneric;
        }
        return String.format("%s.%s", packageName, classNameNoGeneric);
    }

    public String getClassName() {
        return className;
    }

    public Map<String, Object> getGenerics() {
        return generics;
    }

}
