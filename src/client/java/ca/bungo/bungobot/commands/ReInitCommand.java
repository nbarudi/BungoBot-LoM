package ca.bungo.bungobot.commands;

import ca.bungo.bungobot.Command;
import ca.bungo.bungobot.CommandManager;

public class ReInitCommand extends Command {
    public ReInitCommand(String commandName) {
        super(commandName);
    }


    @Override
    public boolean execute(String sender, String[] args) {
        sendChatMessage("Reloading Commands!");
        CommandManager.initCommands();
        return true;
    }
}
