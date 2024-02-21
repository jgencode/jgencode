package com.camucode.gen;

import com.camucode.gen.type.ClassType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static java.util.stream.Collectors.toList;

public class MethodUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodUtil.class);

    static void importClassesFromMethods(Collection<MethodDefinitionBuilder.MethodDefinition> methods,
                                         Set<String> classesToImport) {
     LOGGER.debug("getting the classes that are used in the methods");
        if (methods == null) {
            return;
        }
        methods.forEach(method -> {
            if ((method.getReturnType() != null) && (method.getReturnType() instanceof ClassType)) {
                ClassType returnClassType = (ClassType) method.getReturnType();
                classesToImport.add(returnClassType.getFullClassName());
                Optional.ofNullable(returnClassType.getGenerics()).map(Map::values)
                    .get().stream()
                    .filter(generic -> generic instanceof ClassType)
                    .map(ClassType.class::cast)
                    .forEach(generic -> {
                        classesToImport.add(generic.getFullClassName());
                    });
            }
            var classesParameters = method.getParameters().values()
                .stream()
                .filter(item -> item instanceof ClassType)
                .map(ClassType.class::cast)
                .map(ClassType::getFullClassName).collect(
                    toList());
            if (!classesParameters.isEmpty()) {
                classesToImport.addAll(classesParameters);
            }

        });
    }
}
