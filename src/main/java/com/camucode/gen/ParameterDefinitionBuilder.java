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
package com.camucode.gen;

import com.camucode.gen.type.AnnotationType;
import com.camucode.gen.type.JavaType;
import java.util.LinkedHashSet;
import java.util.Set;

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

    public ParameterDefinitionBuilder parameterName(String parameterName) {
        this.parameterName = parameterName;
        return this;
    }

    public ParameterDefinitionBuilder parameterType(JavaType parameterType) {
        this.parameterType = this.parameterType;
        return this;
    }

    public ParameterDefinitionBuilder addAnnotation(AnnotationType annotationType) {
        this.annotationTypes.add(annotationType);
        return this;
    }

    public static ParameterDefinitionBuilder newBuilder() {
        return new ParameterDefinitionBuilder();
    }

    public ParameterDefinition build() {
        ParameterDefinition definition = new ParameterDefinition();
        definition.annotationTypes = annotationTypes;
        definition.parameterName = parameterName;
        definition.parameterType = parameterType;
        definition.sourceCode = generateSourceCode();

        return definition;

    }

    private String generateSourceCode() {
       // throw new UnsupportedOperationException("Not supported yet."); // Generated from
        // nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        return "";
    }

}
