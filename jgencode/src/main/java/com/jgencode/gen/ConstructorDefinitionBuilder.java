/*
 * Copyright 2023 diego.silva.
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
import com.jgencode.gen.values.Modifier;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

import static com.jgencode.gen.DefinitionBuilder.getIndentation;
import static com.jgencode.gen.util.Constants.CLASSNAME_PARAMETER;
import static com.jgencode.gen.util.Constants.CLOSE_BRACE;
import static com.jgencode.gen.util.Constants.COMMA;
import static com.jgencode.gen.util.Constants.OPEN_BRACE;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.SPACE;

/**
 * Builder class for defining a constructor
 *
 * @author Diego Silva (diego.silva at apuntesdejava.com)
 */
public class ConstructorDefinitionBuilder {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConstructorDefinitionBuilder.class);

    private final Set<Modifier> modifiers;
    private final Set<ParameterDefinition> parameters;
    private final Set<AnnotationType> annotationTypes;

    private String body;

    private ConstructorDefinitionBuilder() {
        this.modifiers = new LinkedHashSet<>();
        this.parameters = new LinkedHashSet<>();
        this.annotationTypes = new LinkedHashSet<>();
    }

    /**
     * Creates a builder object from a constructor definition
     *
     * @return a new object builder
     */
    public static ConstructorDefinitionBuilder createBuilder() {
        return new ConstructorDefinitionBuilder();
    }

    /**
     * defines the body of the constructor
     *
     * @param body the body of the constructor
     * @return the same builder object
     */
    public ConstructorDefinitionBuilder body(String body) {
        this.body = body;
        return this;
    }

    /**
     *
     * @param modifier
     * @return
     */
    public ConstructorDefinitionBuilder addModifier(Modifier modifier) {
        this.modifiers.add(modifier);
        return this;
    }

    /**
     *
     * @param parameterDefinition
     * @return
     */
    public ConstructorDefinitionBuilder addParameter(ParameterDefinition parameterDefinition) {
        parameters.add(parameterDefinition);
        return this;
    }

    /**
     *
     * @param parameterDefinitions
     * @return
     */
    public ConstructorDefinitionBuilder parameters(Collection<ParameterDefinition> parameterDefinitions) {
        this.parameters.addAll(parameterDefinitions);
        return this;
    }

    /**
     *
     * @return
     */
    public ConstructorDefinitionBuilder.ConstructorDefinition build() {
        LOGGER.debug("new method definition build");
        var methodDefinition = new ConstructorDefinitionBuilder.ConstructorDefinition();

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

        sourceString.append(SPACE).append(CLASSNAME_PARAMETER);
        sourceString.append("(");

        insertParameters(sourceString);

        sourceString.append(")");

        sourceString.append(OPEN_BRACE).append(System.lineSeparator());
        if (StringUtils.isNotBlank(body)) {
            String[] newBody = StringUtils.split(body, System.lineSeparator());
            for (String newLine : newBody) {
                sourceString.append(String.format("%s%s%n", getIndentation(1), newLine));
            }
        }
        sourceString.append(CLOSE_BRACE).append(System.lineSeparator()).append(System.lineSeparator());

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
    public ConstructorDefinitionBuilder addAnnotationType(AnnotationType annotationType) {
        this.annotationTypes.add(annotationType);
        return this;
    }

    /**
     * Definition constructor builder
     */
    public static class ConstructorDefinition {

        private Set<AnnotationType> annotationTypes;

        private Set<Modifier> modifiers;
        private Set<ParameterDefinition> parameters;
        private List<String> sourceLines;
        private String body;

        private ConstructorDefinition() {

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

    }
}
