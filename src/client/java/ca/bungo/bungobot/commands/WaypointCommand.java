package ca.bungo.bungobot.commands;

import baritone.api.BaritoneAPI;
import ca.bungo.bungobot.Command;

public class WaypointCommand extends Command {

    public WaypointCommand(String commandName) {
        super(commandName);
    }

    @Override
    public boolean execute(String sender, String[] args) {

        if(args.length <= 1) return false;

        String waypointName = args[1];

        if(BaritoneAPI.getProvider().getPrimaryBaritone().getCommandManager().execute("wp save user " + waypointName)){
            sendChatMessage("I have saved the waypoint " + waypointName + "!");
            return true;
        }else{
            sendChatMessage("I was unable to save the waypoint " + waypointName + "!");
            return false;
        }
    }
}
