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
package com.jgencode.gen.type;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Constructor class to define an annotation
 */
public class AnnotationTypeBuilder {

    private ClassType classType;

    private final Map<String, Object> attributes = new LinkedHashMap<>();

    private AnnotationTypeBuilder() {

    }
/**
 * 
 * @return 
 */
    public static AnnotationTypeBuilder newBuilder() {
        return new AnnotationTypeBuilder();
    }
/**
 * 
 * @param classType
 * @return 
 */
    public AnnotationTypeBuilder classType(ClassType classType) {
        this.classType = classType;
        return this;
    }

    /**
     * Add an annotation attribute
     *
     * @param name  attribute name
     * @param value attribute value
     * @return the same object
     */
    public AnnotationTypeBuilder addAttribute(String name, Object value) {
        attributes.put(name, value);
        return this;
    }

    /**
     * Build the annotation type
     *
     * @return the annotation type #AnnotationType
     */
    public AnnotationType build() {
        AnnotationType annotationType = new AnnotationType();
        annotationType.classType = classType;
        annotationType.attributes = new LinkedHashMap<>(attributes);
        return annotationType;
    }
}
