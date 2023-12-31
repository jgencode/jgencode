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
package com.camucode.gen;

import com.camucode.gen.DefinitionBuilder.Definition;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @author diego.silva
 */
public class JavaFileBuilder {

    private final Definition definition;
    private final Path destinationPath;

    private JavaFileBuilder(Definition definition, Path destinationPath) {
        this.definition = definition;
        this.destinationPath = destinationPath;
    }

    public static class JavaFile {

        private final Definition definition;
        private final Path destinationPath;

        private JavaFile(Definition definition, Path destinationPath) {
            this.definition = definition;
            this.destinationPath = destinationPath;
        }

        public Path writeFile() throws IOException {
            Path javaFilePath = destinationPath.resolve(definition.getPackagePath())
                .resolve(Path.of(String.format(
                    "%s.java", definition.getClassName())));
            Files.createDirectories(javaFilePath.getParent());
            return Files.write(javaFilePath, definition.getCodeLines());
        }

    }

    public static JavaFileBuilder createBuilder(Definition definition, Path destinationPath) {
        JavaFileBuilder builder = new JavaFileBuilder(definition, destinationPath);
        return builder;
    }

    public JavaFile build() {
        JavaFile javaFile = new JavaFile(definition, destinationPath);
        return javaFile;
    }
}
