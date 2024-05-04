package com.camucode.gen;

import com.camucode.gen.type.AnnotationType;
import com.camucode.gen.values.Modifier;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

import static com.camucode.gen.DefinitionBuilder.getIndentation;
import static com.camucode.gen.util.Constants.*;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.SPACE;

public class ConstructorDefinitionBuilder {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConstructorDefinitionBuilder.class);

    private final Set<Modifier> modifiers = new LinkedHashSet<>();
    private final Set<ParameterDefinition> parameters = new LinkedHashSet<>();
    public Set<AnnotationType> annotationTypes = new LinkedHashSet<>();

    private String body;

    private ConstructorDefinitionBuilder() {
    }


    public static ConstructorDefinitionBuilder createBuilder() {
        return new ConstructorDefinitionBuilder();
    }


    public ConstructorDefinitionBuilder body(String body) {
        this.body = body;
        return this;
    }


    public ConstructorDefinitionBuilder addModifier(Modifier modifier) {
        this.modifiers.add(modifier);
        return this;
    }

    public ConstructorDefinitionBuilder addParameter(ParameterDefinition parameterDefinition) {
        parameters.add(parameterDefinition);
        return this;
    }

    public ConstructorDefinitionBuilder parameters(Collection<ParameterDefinition> parameterDefinitions) {
        this.parameters.addAll(parameterDefinitions);
        return this;
    }

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

    public ConstructorDefinitionBuilder addAnnotationType(AnnotationType annotationType) {
        this.annotationTypes.add(annotationType);
        return this;
    }

    public static class ConstructorDefinition {

        public Set<AnnotationType> annotationTypes;

        private Set<Modifier> modifiers;
        private Set<ParameterDefinition> parameters;
        private List<String> sourceLines;
        private String body;

        private ConstructorDefinition() {

        }


        public Set<Modifier> getModifiers() {
            return modifiers;
        }

        public Set<ParameterDefinition> getParameters() {
            return parameters;
        }

        public List<String> getSourceLines() {
            return sourceLines;
        }

        public String getBody() {
            return body;
        }

    }
}
