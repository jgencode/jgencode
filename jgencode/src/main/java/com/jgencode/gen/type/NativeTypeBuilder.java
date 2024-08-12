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
package com.jgencode.gen.type;

/**
 *
 * @author Diego Silva diego.silva at apuntesdejava.com
 */
public class NativeTypeBuilder {

    private String name;

    private NativeTypeBuilder() {

    }

    /**
     *
     * @return
     */
    public static NativeTypeBuilder newBuilder() {
        return new NativeTypeBuilder();
    }

    /**
     *
     * @param name
     * @return
     */
    public NativeTypeBuilder name(String name) {
        this.name = name;
        return this;
    }

    /**
     *
     * @return
     */
    public NativeType build() {
        return new NativeType(name);
    }

}
