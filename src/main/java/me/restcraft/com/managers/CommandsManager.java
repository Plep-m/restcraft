package me.restcraft.com.managers;

import net.minestom.server.MinecraftServer;
import net.minestom.server.command.builder.Command;
import java.util.ArrayList;
import java.util.List;

public class CommandsManager {
    private final List<Command> commandList = new ArrayList<>();

    // Add a command to the manager
    public void addCommand(Command command) {
        commandList.add(command);
    }

    // Register all commands to the server
    public void registerCommands() {
        for (Command command : commandList) {
            MinecraftServer.getCommandManager().register(command);
        }
    }
}
