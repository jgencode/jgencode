/*
 * Copyright 2023 Diego Silva diego.silva at apuntesdejava.com.
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
import com.jgencode.gen.type.ClassType;
import com.jgencode.gen.values.Modifier;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.jgencode.gen.util.Constants.SEMI_COLON;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.SPACE;

/**
 * Field definition constructor class
 *
 * @author Diego Silva diego.silva at apuntesdejava.com
 */
public class FieldDefinitionBuilder {

    /**
     *
     * @return
     */
    public static FieldDefinitionBuilder createBuilder() {
        return new FieldDefinitionBuilder();
    }

    private String fieldName;
    private String nativeType;

    private final Set<AnnotationType> annotationTypes = new LinkedHashSet<>();
    private ClassType classType;
    private final Set<Modifier> modifiers = new LinkedHashSet<>();
    private boolean setter;
    private boolean getter;

    private String defaultValue;

    private FieldDefinitionBuilder() {

    }

    /**
     *
     * @param fieldName
     * @return
     */
    public FieldDefinitionBuilder fieldName(String fieldName) {
        this.fieldName = fieldName;
        return this;
    }

    /**
     *
     * @param nativeType
     * @return
     */
    public FieldDefinitionBuilder nativeType(String nativeType) {
        this.nativeType = nativeType;
        return this;
    }

    /**
     *
     * @param classType
     * @return
     */
    public FieldDefinitionBuilder classType(ClassType classType) {
        this.classType = classType;
        return this;
    }

    /**
     *
     * @param modifier
     * @return
     */
    public FieldDefinitionBuilder addModifier(Modifier modifier) {
        this.modifiers.add(modifier);
        return this;
    }

    /**
     *
     * @param annotationType
     * @return
     */
    public FieldDefinitionBuilder addAnnotationType(AnnotationType annotationType) {
        this.annotationTypes.add(annotationType);
        return this;
    }

    /**
     *
     * @param setter
     * @return
     */
    public FieldDefinitionBuilder setter(boolean setter) {
        this.setter = setter;
        return this;
    }

    /**
     *
     * @param getter
     * @return
     */
    public FieldDefinitionBuilder getter(boolean getter) {
        this.getter = getter;
        return this;
    }

    /**
     *
     * @param defaultValue
     * @return
     */
    public FieldDefinitionBuilder defaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }

    /**
     *
     * @return
     */
    public FieldDefinition build() {
        FieldDefinition fieldDefinition = new FieldDefinition();
        fieldDefinition.classType = classType;
        fieldDefinition.fieldName = fieldName;
        fieldDefinition.getter = getter;
        fieldDefinition.modifiers = modifiers;
        fieldDefinition.nativeType = nativeType;
        fieldDefinition.setter = setter;
        fieldDefinition.annotationType = annotationTypes;
        fieldDefinition.defaultValue = defaultValue;
        fieldDefinition.sourceLines = createSourceLine();
        return fieldDefinition;
    }

    private List<String> createSourceLine() {
        List<String> lines = new ArrayList<>();
        annotationTypes.forEach(annotationType -> lines.addAll(annotationType.createSourceLines()));

        var sourceLine = new StringBuilder();
        sourceLine.append(Modifier.currentAccessModifier(modifiers))
                .append(SPACE);
        if (StringUtils.isBlank(nativeType)) {
            sourceLine.append(classType.getClassName());
        } else {
            sourceLine.append(nativeType);
        }
        sourceLine.append(SPACE);
        sourceLine.append(fieldName);
        if (StringUtils.isNotBlank(defaultValue)) {
            sourceLine.append(" = ");
            sourceLine.append(defaultValue);
        }
        sourceLine.append(SEMI_COLON);
        lines.add(sourceLine.toString());
        lines.add(EMPTY);
        return lines;
    }

    /**
     * Field definition class
     */
    public static class FieldDefinition {

        private Set<AnnotationType> annotationType;
        private String defaultValue;
        private String fieldName;
        private String nativeType;
        private ClassType classType;
        private Set<Modifier> modifiers = new LinkedHashSet<>();
        private boolean setter;
        private boolean getter;
        List<String> sourceLines;

        /**
         *
         * @return
         */
        public String getFieldName() {
            return fieldName;
        }

        /**
         *
         * @return
         */
        public String getFieldType() {
            return Optional.ofNullable(classType).map(ClassType::getClassName).orElse(nativeType);
        }

        /**
         *
         * @return
         */
        public Set<AnnotationType> getAnnotationType() {
            return annotationType;
        }

        /**
         *
         * @return
         */
        public String getNativeType() {
            return nativeType;
        }

        /**
         *
         * @return
         */
        public ClassType getClassType() {
            return classType;
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
        public boolean isSetter() {
            return setter;
        }

        /**
         *
         * @return
         */
        public boolean isGetter() {
            return getter;
        }

        /**
         *
         * @return
         */
        public List<String> getSourceLines() {
            return sourceLines;
        }

        private FieldDefinition() {

        }
    }
}
