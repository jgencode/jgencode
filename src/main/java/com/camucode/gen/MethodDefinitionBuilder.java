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

import static com.camucode.gen.DefinitionBuilder.getIndentation;
import com.camucode.gen.type.JavaType;
import static com.camucode.gen.util.Constants.CLOSE_BRACE;
import com.camucode.gen.values.Modifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.camucode.gen.util.Constants.COMMA;
import static com.camucode.gen.util.Constants.OPEN_BRACE;
import static com.camucode.gen.util.Constants.SEMI_COLON;
import org.apache.commons.lang3.StringUtils;
import static org.apache.commons.lang3.StringUtils.SPACE;

/**
 * @author Diego Silva diego.silva at apuntesdejava.com
 */
public class MethodDefinitionBuilder {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodDefinitionBuilder.class);

    private String name;

    private JavaType returnType;
    private final Set<Modifier> modifiers = new LinkedHashSet<>();
    private final Map<String, JavaType> parameters = new LinkedHashMap<>();

    private String body;

    private boolean isAbstract;

    private MethodDefinitionBuilder() {

    }

    public static MethodDefinitionBuilder createBuilder() {
        return new MethodDefinitionBuilder();
    }

    public MethodDefinitionBuilder name(String name) {
        this.name = name;
        return this;
    }

    public MethodDefinitionBuilder body(String body) {
        this.body = body;
        return this;
    }

    public MethodDefinitionBuilder isAbstract(boolean isAbstract) {
        this.isAbstract = isAbstract;
        return this;
    }

    public MethodDefinitionBuilder returnClassType(JavaType returnType) {
        this.returnType = returnType;
        return this;
    }

    public MethodDefinitionBuilder addModifier(Modifier modifier) {
        this.modifiers.add(modifier);
        return this;
    }

    public MethodDefinitionBuilder addParameter(String name, JavaType typeDefinition) {
        parameters.put(name, typeDefinition);
        return this;
    }

    public MethodDefinition build() {
        LOGGER.debug("new method definition build");
        MethodDefinition methodDefinition = new MethodDefinition();
        methodDefinition.name = name;
        methodDefinition.returnType = returnType;
        methodDefinition.modifiers = modifiers;
        methodDefinition.parameters = parameters;
        methodDefinition.body = body;
        methodDefinition.sourceLines = createSourceLines();

        return methodDefinition;
    }

    private List<String> createSourceLines() {
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

        return sourceString.toString().lines().collect(Collectors.toList());
    }

    private void insertParameters(StringBuilder sourceString) {
        if (parameters.isEmpty()) {
            return;
        }
        sourceString.append(parameters.entrySet().stream().map(parameter -> String.format("%s %s",
            parameter.getValue().getFullName(),
            parameter.getKey())).collect(Collectors.joining(COMMA)));
    }

    public static class MethodDefinition {

        private String name;
        private JavaType returnType;
        private Set<Modifier> modifiers;
        private Map<String, JavaType> parameters;
        private List<String> sourceLines;
        private String body;

        private MethodDefinition() {

        }

        public JavaType getReturnType() {
            return returnType;
        }

        public Set<Modifier> getModifiers() {
            return modifiers;
        }

        public Map<String, JavaType> getParameters() {
            return parameters;
        }

        public List<String> getSourceLines() {
            return sourceLines;
        }

        public String getBody() {
            return body;
        }

        public String getName() {
            return name;
        }
    }
}
