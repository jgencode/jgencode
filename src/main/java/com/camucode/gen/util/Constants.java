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
package com.camucode.gen.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

/**
 * @author Diego Silva diego.silva at apuntesdejava.com
 */
public class Constants {

    /**
     * "{@code .}" caracter
     */
    public static final String SEARCH_DOT = "\\.";
    /**
     * "{@code ;}" caracter
     */
    public static final String SEMI_COLON = ";";

    public static final String OPEN_BRACE = "{";
    public static final String CLOSE_BRACE = "}";

    public static final String PERIOD = ".";

    public static final String CLASSNAME_PARAMETER = "{_CLASSNAME_}";
    /**
     * "{@code \u003c}" caracter
     */
    public static final String LESS_THAN = "<";
    /**
     * "{@code \u003e}" caracter
     */
    public static final String MORE_THAN = ">";
    /**
     * "{@code ,}" caracter
     */
    public static final String COMMA = ",";
    /**
     * "{@code ,}" caracter
     */
    public static final String COMMA_SPACE = ", ";
    /**
     * Class map with their respective complete package declaration
     */
    public static final Map<String, String> GENERAL_CLASSES = Map.of("UUID", UUID.class.getName(), "LocalDate",
        LocalDate.class.getName(), "LocalDateTime", LocalDateTime.class.getName(), "Optional", Optional.class.getName(),
        "Stream", Stream.class.getName());
}
