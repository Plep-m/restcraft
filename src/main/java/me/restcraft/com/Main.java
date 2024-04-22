package me.restcraft.com;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;

import me.restcraft.com.commands.TimeCommand;
import me.restcraft.com.managers.*;
import org.reflections.Reflections;

import me.restcraft.com.interfaces.Manager;

import net.minestom.server.MinecraftServer;
import net.minestom.server.instance.InstanceContainer;

public class Main {
    @SuppressWarnings("CallToPrintStackTrace")
    public static void main(String[] args) {
        MinecraftServer minecraftServer = MinecraftServer.init();
        InstanceContainer instanceContainer = InstanceCreator.createInstance();

        Reflections reflections = new Reflections("me.restcraft.com.managers");
        Set<Class<? extends Manager>> managerClasses = reflections.getSubTypesOf(Manager.class);

        Map<Class<? extends Manager>, Manager> managerInstances = new HashMap<>();
        for (Class<? extends Manager> managerClass : managerClasses) {
            try {
                Manager managerInstance = managerClass.getDeclaredConstructor(InstanceContainer.class).newInstance(instanceContainer);
                managerInstances.put(managerClass, managerInstance);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        HttpRoutes httpRoutes = new HttpRoutes(managerInstances);
        httpRoutes.setupRoutes();

        CommandsManager commandManager = new CommandsManager(instanceContainer);
        commandManager.addCommand(new TimeCommand(instanceContainer));
        commandManager.registerCommands();

        // Step 5: Start the Minecraft server
        minecraftServer.start("0.0.0.0", 25565);
    }
}
