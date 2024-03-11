package ca.bungo.bungobot.commands;

import ca.bungo.bungobot.Command;

public class SleepCommand extends Command {
    public SleepCommand(String commandName) {
        super(commandName);
    }

    @Override
    public boolean execute(String sender, String[] args) {
        sendChatMessage("Goodnight!");
        sendChatMessage("/me sleeps");
        sendChatMessage("/lay");
        return true;
    }
}
