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
package com.camucode.gen.type;

import com.camucode.gen.util.ClassUtil;
import com.camucode.gen.util.Constants;
import static com.camucode.gen.util.Constants.GENERAL_CLASSES;
import static com.camucode.gen.util.Constants.LESS_THAN;
import static com.camucode.gen.util.Constants.MORE_THAN;
import java.util.LinkedHashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author Diego Silva diego.silva at apuntesdejava.com
 */
public class ClassTypeBuilder {

    private String packageName;
    private String className;
    private final Map<String, Object> generics = new LinkedHashMap<>();

    public static ClassTypeBuilder newBuilder() {
        return new ClassTypeBuilder();
    }

    public ClassTypeBuilder packageName(String packageName) {
        this.packageName = packageName;
        return this;
    }

    public ClassTypeBuilder className(String className) {
        if (StringUtils.contains(className, LESS_THAN) && StringUtils.endsWith(className, MORE_THAN)) {
            String generic = StringUtils.substringBetween(className, LESS_THAN, MORE_THAN);
            addGeneric("T", ClassTypeBuilder.newBuilder()
                .className(generic)
                .packageName(packageName)
                .build());
            this.className = StringUtils.substringBefore(className, LESS_THAN);
            if (Constants.GENERAL_CLASSES.containsKey(this.className)) {
                packageName = ClassUtil.removeClassFromPackage(GENERAL_CLASSES.get(this.className), this.className);
            }
        } else {
            this.className = className;
        }
        return this;
    }


    public ClassTypeBuilder addGeneric(String key, Object genericClass) {
        generics.put(key, genericClass);
        return this;
    }

    public ClassType build() {
        var classType = new ClassType(packageName, className);
        if (!generics.isEmpty()) {
            classType.generics = generics;
        }
        return classType;
    }
}
