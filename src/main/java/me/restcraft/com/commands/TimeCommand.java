package me.restcraft.com.commands;

import me.restcraft.com.interfaces.SetupCommand;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.instance.Instance;
import me.restcraft.com.annotations.IsCommand;

@IsCommand
public class TimeCommand extends Command implements SetupCommand {

    public TimeCommand(Instance instance) {
        super("time");

        setDefaultExecutor((sender, context) -> sender.sendMessage("Usage: /time set <number> (Sets the world time)"));

        var timeArgument = ArgumentType.Integer("time");

        addSyntax((sender, context) -> {
            final int time = context.get(timeArgument);

            instance.setTime(time);

            sender.sendMessage("World time set to: " + time);
        }, ArgumentType.Word("action").from("set"), timeArgument);
    }
}
