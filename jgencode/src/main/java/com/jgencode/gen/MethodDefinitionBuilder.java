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
import com.jgencode.gen.values.Modifier;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.jgencode.gen.DefinitionBuilder.getIndentation;
import static com.jgencode.gen.util.Constants.CLOSE_BRACE;
import static com.jgencode.gen.util.Constants.COMMA;
import static com.jgencode.gen.util.Constants.OPEN_BRACE;
import static com.jgencode.gen.util.Constants.SEMI_COLON;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.SPACE;

/**
 * Constructor class to define a method
 *
 * @author Diego Silva diego.silva at apuntesdejava.com
 */
public class MethodDefinitionBuilder {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodDefinitionBuilder.class);

    private String name;

    private JavaType returnType;
    private final Set<Modifier> modifiers = new LinkedHashSet<>();
    private final Set<ParameterDefinition> parameters = new LinkedHashSet<>();
    private final Set<AnnotationType> annotationTypes;

    private String body;

    private boolean isAbstract;

    private MethodDefinitionBuilder() {
        this.annotationTypes = new LinkedHashSet<>();

    }

    /**
     *
     * @return
     */
    public static MethodDefinitionBuilder createBuilder() {
        return new MethodDefinitionBuilder();
    }

    /**
     *
     * @param name
     * @return
     */
    public MethodDefinitionBuilder name(String name) {
        this.name = name;
        return this;
    }

    /**
     *
     * @param body
     * @return
     */
    public MethodDefinitionBuilder body(String body) {
        this.body = body;
        return this;
    }

    /**
     *
     * @param isAbstract
     * @return
     */
    public MethodDefinitionBuilder isAbstract(boolean isAbstract) {
        this.isAbstract = isAbstract;
        return this;
    }

    /**
     *
     * @param returnType
     * @return
     */
    public MethodDefinitionBuilder returnClassType(JavaType returnType) {
        this.returnType = returnType;
        return this;
    }

    /**
     *
     * @param modifier
     * @return
     */
    public MethodDefinitionBuilder addModifier(Modifier modifier) {
        this.modifiers.add(modifier);
        return this;
    }

    /**
     *
     * @param parameterDefinition
     * @return
     */
    public MethodDefinitionBuilder addParameter(ParameterDefinition parameterDefinition) {
        parameters.add(parameterDefinition);
        return this;
    }

    /**
     *
     * @param parameterDefinitions
     * @return
     */
    public MethodDefinitionBuilder parameters(Collection<ParameterDefinition> parameterDefinitions) {
        this.parameters.addAll(parameterDefinitions);
        return this;
    }

    /**
     *
     * @return
     */
    public MethodDefinition build() {
        LOGGER.debug("new method definition build");
        MethodDefinition methodDefinition = new MethodDefinition();
        methodDefinition.name = name;
        methodDefinition.returnType = returnType;
        methodDefinition.modifiers = modifiers;
        methodDefinition.parameters = parameters;
        methodDefinition.body = body;
        methodDefinition.annotationTypes = annotationTypes;
        methodDefinition.sourceLines = createSourceLines();

        return methodDefinition;
    }

    private List<String> createSourceLines() {
        List<String> lines = new ArrayList<>();

        annotationTypes.forEach(annotationType -> lines.addAll(annotationType.createSourceLines()));

        StringBuilder sourceString = new StringBuilder();
        sourceString.append(Modifier.currentMethodAccessModifier(modifiers));
        if (sourceString.length() > 0) {
            sourceString.append(SPACE);
        }
        Optional.ofNullable(returnType).ifPresentOrElse(type -> sourceString.append(type.getFullName()),
                () -> sourceString.append("void"));

        sourceString.append(SPACE).append(name);
        sourceString.append("(");

        insertParameters(sourceString);

        sourceString.append(")");
        if (isAbstract) {
            sourceString.append(SEMI_COLON);
        } else {
            sourceString.append(OPEN_BRACE).append(System.lineSeparator());
            if (StringUtils.isNotBlank(body)) {
                String[] newBody = StringUtils.split(body, System.lineSeparator());
                for (String newLine : newBody) {
                    sourceString.append(String.format("%s%s%n", getIndentation(1), newLine));
                }
            }
            sourceString.append(CLOSE_BRACE).append(System.lineSeparator()).append(System.lineSeparator());
        }

        lines.addAll(sourceString.toString().lines().collect(Collectors.toList()));

        return lines;
    }

    private void insertParameters(StringBuilder sourceString) {
        if (parameters.isEmpty()) {
            return;
        }
        var paramsToInsert = parameters.stream()
                .filter(parameterDefinition -> Objects.nonNull(parameterDefinition.parameterName))
                .map(parameter -> String.format("%s %s %s",
                parameter.getAnnotationSource(),
                parameter.getParameterType() == null ? EMPTY : parameter.getParameterType().getFullName(),
                parameter.getParameterName()
        )).collect(Collectors.joining(COMMA));
        if (StringUtils.isNotBlank(paramsToInsert)) {
            sourceString.append(paramsToInsert);
        }
    }

    /**
     *
     * @param annotationType
     * @return
     */
    public MethodDefinitionBuilder addAnnotationType(AnnotationType annotationType) {
        this.annotationTypes.add(annotationType);
        return this;
    }

    /**
     * Definition of method
     */
    public static class MethodDefinition {

        private Set<AnnotationType> annotationTypes;
        private String name;
        private JavaType returnType;
        private Set<Modifier> modifiers;
        private Set<ParameterDefinition> parameters;
        private List<String> sourceLines;
        private String body;

        private MethodDefinition() {

        }

        public Set<AnnotationType> getAnnotationTypes() {
            return annotationTypes;
        }

        /**
         *
         * @return
         */
        public JavaType getReturnType() {
            return returnType;
        }

        /**
         *
         * @return
         */
        public Set<Modifier> getModifiers() {
            return modifiers;
        }

        /**
         *
         * @return
         */
        public Set<ParameterDefinition> getParameters() {
            return parameters;
        }

        /**
         *
         * @return
         */
        public List<String> getSourceLines() {
            return sourceLines;
        }

        /**
         *
         * @return
         */
        public String getBody() {
            return body;
        }

        /**
         *
         * @return
         */
        public String getName() {
            return name;
        }
    }
}
