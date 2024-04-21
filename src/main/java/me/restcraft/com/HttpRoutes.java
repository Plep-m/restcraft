package me.restcraft.com;

import me.restcraft.com.interfaces.SetupRoutes;
import org.reflections.Reflections;
import spark.Spark;
import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import me.restcraft.com.annotations.Route;
import me.restcraft.com.annotations.UseBlockManager;
import me.restcraft.com.annotations.UseChunkManager;
import me.restcraft.com.annotations.UseGson;
import me.restcraft.com.annotations.UsePlayerManager;
import me.restcraft.com.managers.PlayerManager;
import me.restcraft.com.managers.BlockManager;
import me.restcraft.com.managers.ChunkManager;
import com.google.gson.Gson;

public class HttpRoutes {
    private final List<SetupRoutes> routeClasses;

    public HttpRoutes(PlayerManager playerManager, BlockManager blockManager, ChunkManager chunkManager) {
        Gson gson = new Gson();

        routeClasses = new ArrayList<>();

        Reflections reflections = new Reflections("me.restcraft.com.routes");
        Set<Class<?>> annotatedClasses = reflections.getTypesAnnotatedWith(Route.class);

        for (Class<?> clazz : annotatedClasses) {
            try {
                Constructor<?> matchingConstructor = null;

                for (Constructor<?> constructor : clazz.getDeclaredConstructors()) {
                    boolean hasPlayerManager = Arrays.stream(constructor.getParameters())
                            .anyMatch(p -> p.isAnnotationPresent(UsePlayerManager.class));
                    boolean hasBlockManager = Arrays.stream(constructor.getParameters())
                            .anyMatch(p -> p.isAnnotationPresent(UseBlockManager.class));
                    boolean hasChunkManager = Arrays.stream(constructor.getParameters())
                            .anyMatch(p -> p.isAnnotationPresent(UseChunkManager.class));
                    boolean hasGson = Arrays.stream(constructor.getParameters())
                            .anyMatch(p -> p.isAnnotationPresent(UseGson.class));

                    if (hasBlockManager && hasGson) {
                        matchingConstructor = constructor;
                        break;
                    }
                }

                if (matchingConstructor != null) {

                    List<Object> args = new ArrayList<>();
                    for (Parameter param : matchingConstructor.getParameters()) {
                        if (param.isAnnotationPresent(UsePlayerManager.class)) {
                            args.add(playerManager);
                        }
                        if (param.isAnnotationPresent(UseBlockManager.class)) {
                            args.add(blockManager);
                        }
                        if (param.isAnnotationPresent(UseChunkManager.class)) {
                            args.add(chunkManager);
                        }
                        if (param.isAnnotationPresent(UseGson.class)) {
                            args.add(gson);
                        }
                    }

                    SetupRoutes routeSetup = (SetupRoutes) matchingConstructor.newInstance(args.toArray());
                    routeClasses.add(routeSetup);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void setupRoutes() {
        Spark.port(4567); // Set the port
        Spark.init(); // Initialize Spark framework

        for (SetupRoutes routeSetup : routeClasses) {
            routeSetup.setupRoutes();
        }
    }
}
