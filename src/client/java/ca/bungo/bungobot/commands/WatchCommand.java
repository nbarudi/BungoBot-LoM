package ca.bungo.bungobot.commands;

import ca.bungo.bungobot.Command;

public class WatchCommand extends Command {
    public static String watching = "";
    public WatchCommand(String commandName) {
        super(commandName);
    }

    @Override
    public boolean execute(String sender, String[] args) {
        if(args.length <= 1) {
            watching = "";
            sendChatMessage("Now watching no one!");
            return true;
        }
        watching = args[1];
        sendChatMessage("Now watching " + args[1]);
        return true;
    }
}
