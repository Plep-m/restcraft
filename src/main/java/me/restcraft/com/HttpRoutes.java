package me.restcraft.com;

import org.reflections.Reflections;
import com.google.gson.Gson;
import spark.Spark;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.logging.Logger;

import me.restcraft.com.interfaces.*;
import me.restcraft.com.annotations.*;

public class HttpRoutes {
    private final List<SetupRoutes> routeClasses;
    private final Logger logger = Logger.getLogger(HttpRoutes.class.getName());

    public HttpRoutes(Map<Class<? extends Manager>, Manager> managerInstances) {
        Map<Class<? extends Annotation>, Object> annotationToInstance = prepareAnnotations(managerInstances);
        routeClasses = createRouteClasses(annotationToInstance);
    }

    private Map<Class<? extends Annotation>, Object> prepareAnnotations(Map<Class<? extends Manager>, Manager> managerInstances) {
        Gson gson = new Gson();
        Reflections reflections = new Reflections("me.restcraft.com.annotations");

        Set<Class<? extends Annotation>> useAnnotations = getUseAnnotations(reflections);
        Map<Class<? extends Annotation>, Object> annotationToInstance = new HashMap<>();

        for (Class<? extends Annotation> annotation : useAnnotations) {
            String managerName = annotation.getSimpleName().substring(3, annotation.getSimpleName().length() - 7); // Strip 'Use' and 'Manager'
            addManagerInstance(annotation, managerName, managerInstances, annotationToInstance);
        }

        annotationToInstance.put(UseGson.class, gson);
        annotationToInstance.put(UseDatabase.class, new Database());

        return annotationToInstance;
    }

    private Set<Class<? extends Annotation>> getUseAnnotations(Reflections reflections) {
        Set<Class<? extends Annotation>> annotations = reflections.getSubTypesOf(Annotation.class);
        annotations.removeIf(annotation -> !annotation.getSimpleName().startsWith("Use") || !annotation.getSimpleName().endsWith("Manager"));
        return annotations;
    }

    @SuppressWarnings("unchecked")
    private void addManagerInstance(Class<? extends Annotation> annotation, String managerName,
                                    Map<Class<? extends Manager>, Manager> managerInstances,
                                    Map<Class<? extends Annotation>, Object> annotationToInstance) {
        try {
            Class<? extends Manager> managerClass = (Class<? extends Manager>) Class.forName("me.restcraft.com.managers." + managerName + "Manager");
            annotationToInstance.put(annotation, managerInstances.get(managerClass));
        } catch (ClassNotFoundException e) {
            logger.severe("Could not find manager class for annotation: " + annotation.getSimpleName());
        }
    }

    private List<SetupRoutes> createRouteClasses(Map<Class<? extends Annotation>, Object> annotationToInstance) {
        List<SetupRoutes> localRouteClasses = new ArrayList<>();
        Reflections routeReflections = new Reflections("me.restcraft.com.routes");

        Set<Class<?>> annotatedClasses = routeReflections.getTypesAnnotatedWith(Route.class);

        for (Class<?> clazz : annotatedClasses) {
            Constructor<?> matchingConstructor = findMatchingConstructor(clazz, annotationToInstance);

            if (matchingConstructor != null) {
                SetupRoutes routeSetup = instantiateRouteSetup(matchingConstructor, annotationToInstance);
                if (routeSetup != null) {
                    localRouteClasses.add(routeSetup);
                }
            }
        }

        return localRouteClasses;
    }

    private Constructor<?> findMatchingConstructor(Class<?> clazz, Map<Class<? extends Annotation>, Object> annotationToInstance) {
        return Arrays.stream(clazz.getDeclaredConstructors())
                .filter(constructor -> constructorHasAllRequiredAnnotations(constructor, annotationToInstance))
                .findFirst()
                .orElse(null);
    }

    private boolean constructorHasAllRequiredAnnotations(Constructor<?> constructor,
                                                         Map<Class<? extends Annotation>, Object> annotationToInstance) {
        return Arrays.stream(constructor.getParameters())
                .allMatch(param -> getAnnotationType(param, annotationToInstance) != null);
    }

    private SetupRoutes instantiateRouteSetup(Constructor<?> constructor, Map<Class<? extends Annotation>, Object> annotationToInstance) {
        try {
            List<Object> args = new ArrayList<>();
            for (Parameter param : constructor.getParameters()) {
                Class<? extends Annotation> annotationType = getAnnotationType(param, annotationToInstance);
                args.add(annotationToInstance.get(annotationType));
            }

            return (SetupRoutes) constructor.newInstance(args.toArray());
        } catch (Exception e) {
            logger.severe("Could not instantiate route setup class: " + constructor.getDeclaringClass().getSimpleName());
            return null;
        }
    }

    private Class<? extends Annotation> getAnnotationType(Parameter param,
                                                          Map<Class<? extends Annotation>, Object> annotationToInstance) {
        for (Class<? extends Annotation> annotationType : annotationToInstance.keySet()) {
            if (param.isAnnotationPresent(annotationType)) {
                return annotationType;
            }
        }
        return null;
    }

    public void setupRoutes() {
        Spark.port(4567); // Set the port
        Spark.init(); // Initialize Spark framework

        for (SetupRoutes routeSetup : routeClasses) {
            routeSetup.setupRoutes();
        }
    }
}
