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
package com.jgencode.gen.type;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.jgencode.gen.DefinitionBuilder.getIndentation;
import static com.jgencode.gen.util.Constants.COMMA;

/**
 * @author Diego Silva diego.silva at apuntesdejava.com
 */
public class AnnotationType {

    ClassType classType;
    Map<String, Object> attributes;

    AnnotationType() {
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
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    /**
     *
     * @return
     */
    public List<String> createSourceLines() {
        List<String> lines = new ArrayList<>();
        String annotation = "@" + classType.getClassName();
        if (attributes.isEmpty()) {
            lines.add(annotation);
        } else {
            lines.add(annotation + "(");
            attributes.forEach((key, value) -> {
                Object toValue;
                if (value instanceof ClassType) {
                    toValue = ((ClassType) value).getClassName() + ".class";
                } else if ((value instanceof Number) || (value instanceof Boolean)) {
                    toValue = (String.format("%s", value));
                } else {
                    toValue = (String.format("\"%s\"", value));
                }
                lines.add(String.format("%s%s = %s,", getIndentation(1), key, toValue));
            });
            String lastLine = lines.get(lines.size() - 1);
            lastLine = StringUtils.substringBeforeLast(lastLine, COMMA);
            lines.set(lines.size() - 1, lastLine);
            lines.add(")");
        }
        return lines;
    }

}
