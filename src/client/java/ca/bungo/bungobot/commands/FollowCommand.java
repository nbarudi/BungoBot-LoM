package ca.bungo.bungobot.commands;

import baritone.api.BaritoneAPI;
import ca.bungo.bungobot.Command;

public class FollowCommand extends Command {
    public FollowCommand(String commandName) {
        super(commandName);
    }

    @Override
    public boolean execute(String sender, String[] args) {
        if(args.length <= 1) return false;

        String playerName = args[1];

        if(playerName.equalsIgnoreCase("me"))
            playerName = sender;

        if(BaritoneAPI.getProvider().getPrimaryBaritone().getCommandManager().execute("follow player " + playerName)){
            sendChatMessage("I will attempt to follow " + playerName + "!");
            return true;
        }else{
            sendChatMessage("I am unsure who " + playerName + " is!");
        }

        return false;
    }
}
