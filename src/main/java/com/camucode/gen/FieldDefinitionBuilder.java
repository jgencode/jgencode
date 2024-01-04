/*
 * Copyright 2023 Diego Silva <diego.silva at apuntesdejava.com>.
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
import org.apache.commons.lang3.StringUtils;

import java.util.LinkedHashSet;
import java.util.Set;

import static com.camucode.gen.util.Constants.SEMI_COLON;
import java.util.Optional;

/**
 * @author Diego Silva <diego.silva at apuntesdejava.com>
 */
public class FieldDefinitionBuilder {

    public static FieldDefinitionBuilder createBuilder() {
        return new FieldDefinitionBuilder();
    }

    private String fieldName;
    private String nativeType;
    private ClassType classType;
    private final Set<Modifier> modifiers = new LinkedHashSet<>();
    private boolean setter;
    private boolean getter;

    private FieldDefinitionBuilder() {

    }

    public FieldDefinitionBuilder fieldName(String fieldName) {
        this.fieldName = fieldName;
        return this;
    }

    public FieldDefinitionBuilder nativeType(String nativeType) {
        this.nativeType = nativeType;
        return this;
    }

    public FieldDefinitionBuilder classType(ClassType classType) {
        this.classType = classType;
        return this;
    }

    public FieldDefinitionBuilder addModifier(Modifier modifier) {
        this.modifiers.add(modifier);
        return this;
    }

    public FieldDefinitionBuilder setter(boolean setter) {
        this.setter = setter;
        return this;
    }

    public FieldDefinitionBuilder getter(boolean getter) {
        this.getter = getter;
        return this;
    }

    public FieldDefinition build() {
        FieldDefinition fieldDefinition = new FieldDefinition();
        fieldDefinition.classType = classType;
        fieldDefinition.fieldName = fieldName;
        fieldDefinition.getter = getter;
        fieldDefinition.modifiers = modifiers;
        fieldDefinition.nativeType = nativeType;
        fieldDefinition.setter = setter;
        fieldDefinition.sourceLine = createSourceLine();
        return fieldDefinition;
    }


    private String createSourceLine() {
        var sourceLine = new StringBuilder();
        sourceLine.append(Modifier.currentAccessModifier(modifiers))
            .append(StringUtils.SPACE);
        if (StringUtils.isBlank(nativeType)) {
            sourceLine.append(classType.getClassName());
        } else {
            sourceLine.append(nativeType);
        }
        sourceLine.append(StringUtils.SPACE);
        sourceLine.append(fieldName);
        sourceLine.append(SEMI_COLON);
        return sourceLine.toString();
    }

    public static class FieldDefinition {

        private String fieldName;
        private String nativeType;
        private ClassType classType;
        private Set<Modifier> modifiers = new LinkedHashSet<>();
        private boolean setter;
        private boolean getter;
        String sourceLine;

        public String getFieldName() {
            return fieldName;
        }
        
        public String getFieldType(){
            return Optional.ofNullable(classType).map(ClassType::getClassName).orElse(nativeType);
        }

        public String getNativeType() {
            return nativeType;
        }

        public ClassType getClassType() {
            return classType;
        }

        public Set<Modifier> getModifiers() {
            return modifiers;
        }

        public boolean isSetter() {
            return setter;
        }

        public boolean isGetter() {
            return getter;
        }

        public String getSourceLine() {
            return sourceLine;
        }

        private FieldDefinition() {

        }
    }
}
