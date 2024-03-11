package ca.bungo.bungobot.commands;

import ca.bungo.bungobot.Command;
import ca.bungo.bungobot.CommandManager;

public class WhitelistCommand extends Command {
    public WhitelistCommand(String commandName) {
        super(commandName);
    }

    @Override
    public boolean execute(String sender, String[] args) {
        if(args.length <= 1){
            sendChatMessage("Here are my whitelisted users:");
            for(String name : CommandManager.whitelistedPlayers){
                sendChatMessage("- " + name);
            }
            return true;
        }
        String name = args[1];
        if(CommandManager.whitelistedPlayers.contains(name)){
            sendChatMessage("Removing " + name + " from the whitelist!");
            CommandManager.whitelistedPlayers.remove(name);
        }else{
            sendChatMessage("Adding " + name + " to the whitelist!");
            CommandManager.whitelistedPlayers.add(name);
        }
        return true;
    }
}
