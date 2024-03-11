package ca.bungo.bungobot.commands;

import baritone.api.BaritoneAPI;
import ca.bungo.bungobot.Command;

public class StopCommand extends Command {
    public StopCommand(String commandName) {
        super(commandName);
    }

    @Override
    public boolean execute(String sender, String[] args) {
        BaritoneAPI.getProvider().getPrimaryBaritone().getCommandManager().execute("stop");
        sendChatMessage("I will now attempt to stop what I am doing!");
        return true;
    }
}
