package ca.bungo.bungobot.commands;

import ca.bungo.bungobot.Command;

public class RunCommand extends Command {
    public RunCommand(String commandName) {
        super(commandName);
    }

    @Override
    public boolean execute(String sender, String[] args) {
        if(args.length <= 1) return false;

        StringBuilder builder = new StringBuilder();
        builder.append("/");
        for(int i = 1; i < args.length; i++){
            builder.append(args[i]).append(" ");
        }

        sendChatMessage(builder.toString().substring(0,builder.length()-1));
        return true;
    }
}
