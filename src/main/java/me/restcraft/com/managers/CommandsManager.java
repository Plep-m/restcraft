package me.restcraft.com.managers;

import me.restcraft.com.annotations.IsCommand;
import me.restcraft.com.annotations.UseInstanceContainer;
import me.restcraft.com.interfaces.SetupCommand;
import net.minestom.server.MinecraftServer;
import net.minestom.server.instance.Instance;
import org.reflections.Reflections;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

public class CommandsManager {
    private final List<SetupCommand> commandList = new ArrayList<>();
    Logger logger = Logger.getLogger(CommandsManager.class.getName());

    public CommandsManager(@UseInstanceContainer Instance instance) {
        Reflections reflections = new Reflections("me.restcraft.com.commands");
        Set<Class<?>> annotated = reflections.getTypesAnnotatedWith(IsCommand.class);

        logger.info("Found " + annotated.size() + " classes annotated with @IsCommand");

        for (Class<?> clazz : annotated) {
            if (SetupCommand.class.isAssignableFrom(clazz)) {
                try {
                    SetupCommand commandInstance = (SetupCommand) clazz.getDeclaredConstructor(Instance.class).newInstance(instance);
                    commandList.add(commandInstance);
                    logger.info("Successfully created an instance of " + clazz.getName());
                } catch (Exception e) {
                    logger.severe("Could not create command instance: " + e.getMessage());
                }
            } else {
                logger.severe(clazz.getName() + " is annotated with @IsCommand but does not implement SetupCommand");
            }
        }
    }

    public void registerCommands() {
        for (SetupCommand command : commandList) {
            MinecraftServer.getCommandManager().register((net.minestom.server.command.builder.Command) command);
            logger.info("Registered command: " + command.getClass().getName());
        }
    }
}