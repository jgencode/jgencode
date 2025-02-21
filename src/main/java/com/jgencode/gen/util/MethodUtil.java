package com.jgencode.gen.util;

import com.jgencode.gen.MethodDefinitionBuilder;
import com.jgencode.gen.ParameterDefinition;
import com.jgencode.gen.type.AnnotationType;
import com.jgencode.gen.type.ClassType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import static java.util.stream.Collectors.toList;

/**
 *
 * @author Diego Silva (diego.silva at apuntesdejava.com)
 */
public class MethodUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodUtil.class);

    /**
     *
     * @param methods
     * @param classesToImport
     */
    public static void importClassesFromMethods(Collection<MethodDefinitionBuilder.MethodDefinition> methods,
            Set<String> classesToImport) {
        LOGGER.debug("getting the classes that are used in the methods");
        if (methods == null) {
            return;
        }
        methods.forEach(method -> {
            if ((method.getReturnType() != null) && (method.getReturnType() instanceof ClassType)) {
                ClassType returnClassType = (ClassType) method.getReturnType();
                classesToImport.add(returnClassType.getFullClassName());
                if (returnClassType.getGenerics() != null) {
                    Optional.ofNullable(returnClassType.getGenerics()).map(Map::values)
                            .get().stream()
                            .filter(generic -> generic instanceof ClassType)
                            .map(ClassType.class::cast)
                            .forEach(generic -> classesToImport.add(generic.getFullClassName()));
                }
            }
            method.getAnnotationTypes().stream().map(AnnotationType::getClassType)
                    .forEach(classType -> classesToImport.add(classType.getFullClassName()));

            var classesParameters = method.getParameters()
                    .stream()
                    .map(ParameterDefinition::getParameterType)
                    .filter(Objects::nonNull)
                    .map(item -> (item instanceof ClassType) ? ((ClassType) item).getFullClassName() : item.
                    getFullName())
                    .filter(ClassUtil::isNotNative)
                    .collect(toList());
            if (!classesParameters.isEmpty()) {
                classesToImport.addAll(classesParameters);
            }

            //from parameters annotations
            var annotationsParameters = method.getParameters()
                    .stream()
                    .flatMap(param -> param.getAnnotationTypes().stream())
                    .map(AnnotationType::getClassType)
                    .map(ClassType::getFullClassName).collect(toList());

            if (!annotationsParameters.isEmpty()) {
                classesToImport.addAll(annotationsParameters);
            }

        });
    }
}
