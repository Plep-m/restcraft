package me.restcraft.com.managers;

import me.restcraft.com.interfaces.Manager;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.builder.Command;
import net.minestom.server.instance.InstanceContainer;

import java.util.ArrayList;
import java.util.List;

public class CommandsManager implements Manager {
    private final List<Command> commandList = new ArrayList<>();
    private final InstanceContainer instanceContainer;

    public CommandsManager(InstanceContainer instanceContainer) {
        this.instanceContainer = instanceContainer;
    }

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
