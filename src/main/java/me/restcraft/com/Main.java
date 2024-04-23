package me.restcraft.com;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.logging.Logger;

import me.restcraft.com.managers.*;
import org.reflections.Reflections;

import me.restcraft.com.interfaces.Manager;

import net.minestom.server.MinecraftServer;
import net.minestom.server.instance.InstanceContainer;

public class Main {
    static Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        MinecraftServer minecraftServer = MinecraftServer.init();
        InstanceContainer instanceContainer = InstanceCreator.createInstance();

        Reflections reflections = new Reflections("me.restcraft.com.managers");
        Set<Class<? extends Manager>> managerClasses = reflections.getSubTypesOf(Manager.class);

        Map<Class<? extends Manager>, Manager> managerInstances = new HashMap<>();

        // Create an instance of RedstoneManager
        RedstoneManager redstoneManager = new RedstoneManager(new BlockManager(instanceContainer, null));
        // Create an instance of BlockManager
        BlockManager blockManager = new BlockManager(instanceContainer, redstoneManager);

        managerInstances.put(RedstoneManager.class, redstoneManager);
        managerInstances.put(BlockManager.class, blockManager);

        for (Class<? extends Manager> managerClass : managerClasses) {
            try {
                if (managerClass.equals(RedstoneManager.class) || managerClass.equals(BlockManager.class)) {
                    continue;
                }

                Manager managerInstance;
                if (managerClass.equals(PlayerManager.class)) {
                    managerInstance = managerClass.getDeclaredConstructor(InstanceContainer.class, RedstoneManager.class).newInstance(instanceContainer, redstoneManager);
                } else {
                    managerInstance = managerClass.getDeclaredConstructor(InstanceContainer.class).newInstance(instanceContainer);
                }
                managerInstances.put(managerClass, managerInstance);
            } catch (Exception e) {
                logger.severe("Could not create manager instance: " + e.getMessage());
            }
        }

        HttpRoutes httpRoutes = new HttpRoutes(managerInstances);
        httpRoutes.setupRoutes();

        CommandsManager commandManager = new CommandsManager(instanceContainer);
        commandManager.registerCommands();

        // Step 5: Start the Minecraft server
        minecraftServer.start("0.0.0.0", 25565);
    }
}