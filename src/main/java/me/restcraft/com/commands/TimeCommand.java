package me.restcraft.com.commands;

import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.instance.Instance;

public class TimeCommand extends Command {

    public TimeCommand(Instance instance) {
        super("time");

        // Default message for command usage
        setDefaultExecutor((sender, context) -> {
            sender.sendMessage("Usage: /time set <number> (Sets the world time)");
        });

        // Argument for setting the time
        var timeArgument = ArgumentType.Integer("time");

        // Syntax to set the world time
        addSyntax((sender, context) -> {
            final int time = context.get(timeArgument);

            // Set the time in the world instance
            instance.setTime(time);

            sender.sendMessage("World time set to: " + time);
        }, ArgumentType.Word("action").from("set"), timeArgument);
    }
}
