/*
 * Copyright 2024 Diego Silva <diego.silva at apuntesdejava.com>.
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

import com.camucode.gen.type.ClassType;
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
import static org.apache.commons.lang3.StringUtils.SPACE;

/**
 * @author Diego Silva <diego.silva at apuntesdejava.com>
 */
public class MethodDefinitionBuilder {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodDefinitionBuilder.class);

    private String name;

    private ClassType returnType;
    private final Set<Modifier> modifiers = new LinkedHashSet<>();
    private final Map<String, ClassType> parameters = new LinkedHashMap<>();

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

    public MethodDefinitionBuilder isAbstract(boolean isAbstract) {
        this.isAbstract = isAbstract;
        return this;
    }

    public MethodDefinitionBuilder returnType(ClassType returnType) {
        this.returnType = returnType;
        return this;
    }

    public MethodDefinitionBuilder addModifier(Modifier modifier) {
        this.modifiers.add(modifier);
        return this;
    }

    public MethodDefinitionBuilder addParameter(String name, ClassType typeDefinition) {
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
        methodDefinition.sourceLines = createSourceLines();

        return methodDefinition;
    }

    private List<String> createSourceLines() {
        StringBuilder sourceString = new StringBuilder();
        sourceString.append(Modifier.currentMethodAccessModifier(modifiers));
        if (sourceString.length() > 0) {
            sourceString.append(SPACE);
        }
        Optional.ofNullable(returnType)
            .ifPresentOrElse(type -> sourceString.append(type.getClassNameWithGeneric()), () -> sourceString.append(
                "void"));

        sourceString.append(SPACE).append(name);
        sourceString.append("(");

        insertParameters(sourceString);

        sourceString.append(")");
        if (isAbstract) {
            sourceString.append(";");
        } else {
            sourceString.append("{\n");
            sourceString.append("}");
        }

        return sourceString.toString().lines().collect(Collectors.toList());
    }

    private void insertParameters(StringBuilder sourceString) {
        if (parameters.isEmpty()) return;
        sourceString.append(parameters.entrySet().stream().map(parameter -> String.format("%s %s",
            parameter.getValue().getClassNameWithGeneric(),
            parameter.getKey())).collect(Collectors.joining(COMMA)));
    }

    public static class MethodDefinition {

        private String name;
        private ClassType returnType;
        private Set<Modifier> modifiers;
        private Map<String, ClassType> parameters;
        private List<String> sourceLines;

        private MethodDefinition() {

        }

        public ClassType getReturnType() {
            return returnType;
        }

        public Set<Modifier> getModifiers() {
            return modifiers;
        }

        public Map<String, ClassType> getParameters() {
            return parameters;
        }

        public List<String> getSourceLines() {
            return sourceLines;
        }

        public String getName() {
            return name;
        }
    }
}
