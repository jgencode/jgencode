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
import java.util.Objects;
import java.util.Set;
import static org.apache.commons.lang3.StringUtils.SPACE;

/**
 *
 * @author Diego Silva diego.silva at apuntesdejava.com
 */
public class ParameterDefinitionBuilder {

    private String parameterName;

    private JavaType parameterType;

    private final Set<AnnotationType> annotationTypes = new LinkedHashSet<>();

    private ParameterDefinitionBuilder() {

    }

    /**
     *
     * @param parameterName
     * @return
     */
    public ParameterDefinitionBuilder parameterName(String parameterName) {
        this.parameterName = parameterName;
        return this;
    }

    /**
     *
     * @param parameterType
     * @return
     */
    public ParameterDefinitionBuilder parameterType(JavaType parameterType) {
        this.parameterType = parameterType;
        return this;
    }

    /**
     *
     * @param annotationType
     * @return
     */
    public ParameterDefinitionBuilder addAnnotation(AnnotationType annotationType) {
        this.annotationTypes.add(annotationType);
        return this;
    }

    /**
     *
     * @return
     */
    public static ParameterDefinitionBuilder newBuilder() {
        return new ParameterDefinitionBuilder();
    }

    /**
     *
     * @return
     */
    public ParameterDefinition build() {
        ParameterDefinition definition = new ParameterDefinition();
        definition.annotationTypes = annotationTypes;
        definition.parameterName = parameterName;
        definition.parameterType = parameterType;
        definition.sourceCode = generateSourceCode();

        return definition;

    }

    private String generateSourceCode() {
        StringBuilder sourceCode = new StringBuilder();
        annotationTypes.forEach(annotationType -> {
            sourceCode.append(String.join(SPACE, annotationType.createSourceLines())).append(SPACE);
        });
        if (Objects.nonNull(parameterType)) {
            sourceCode.append(parameterType.getFullName()).append(SPACE).append(parameterName);
        }
        return sourceCode.toString();
    }

}
