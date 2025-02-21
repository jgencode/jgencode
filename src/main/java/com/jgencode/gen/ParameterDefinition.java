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

import com.jgencode.gen.type.AnnotationType;
import com.jgencode.gen.type.JavaType;

import java.util.LinkedHashSet;
import java.util.Set;

import static org.apache.commons.lang3.StringUtils.SPACE;

/**
 * @author Diego Silva diego.silva at apuntesdejava.com
 */
public class ParameterDefinition {

    String parameterName;

    JavaType parameterType;
    Set<AnnotationType> annotationTypes = new LinkedHashSet<>();

    String sourceCode;

    ParameterDefinition() {

    }

    /**
     *
     * @return
     */
    public String getParameterName() {
        return parameterName;
    }

    /**
     *
     * @return
     */
    public JavaType getParameterType() {
        return parameterType;
    }

    /**
     *
     * @return
     */
    public Set<AnnotationType> getAnnotationTypes() {
        return annotationTypes;
    }

    /**
     *
     * @return
     */
    public String getSourceCode() {
        return sourceCode;
    }

    /**
     *
     * @return
     */
    public String getAnnotationSource() {
        StringBuilder source = new StringBuilder();
        annotationTypes.forEach(annotationType -> source.append(String.join(SPACE,
                annotationType.createSourceLines())));
        return source.toString();
    }
}
