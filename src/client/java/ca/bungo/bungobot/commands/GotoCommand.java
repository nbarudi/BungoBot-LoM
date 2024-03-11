package ca.bungo.bungobot.commands;

import baritone.api.BaritoneAPI;
import ca.bungo.bungobot.Command;

public class GotoCommand extends Command {

    public GotoCommand(String commandName) {
        super(commandName);
    }

    @Override
    public boolean execute(String sender, String[] args) {

        if(args.length <= 1) return false;

        String waypoint = args[1];

        if(BaritoneAPI.getProvider().getPrimaryBaritone().getCommandManager().execute("wp goto " + waypoint)){
            sendChatMessage("Understood! I will start making my way there!");
            return true;
        } else{
            sendChatMessage("Failed to find waypoint! Not moving.");
            return false;
        }
    }
}
