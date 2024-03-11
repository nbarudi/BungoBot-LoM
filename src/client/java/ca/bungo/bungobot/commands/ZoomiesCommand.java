package ca.bungo.bungobot.commands;

import baritone.api.BaritoneAPI;
import ca.bungo.bungobot.Command;

public class ZoomiesCommand extends Command {

    public ZoomiesCommand(String commandName) {
        super(commandName);
    }

    @Override
    public boolean execute(String sender, String[] args) {
        sendChatMessage("Zooooooooooooom");
        BaritoneAPI.getProvider().getPrimaryBaritone()
                .getCommandManager().execute("explore ~3 ~3");
        return true;
    }
}
