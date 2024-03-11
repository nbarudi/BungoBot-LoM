package ca.bungo.bungobot.commands;

import ca.bungo.bungobot.Command;
import ca.bungo.bungobot.CommandManager;

import java.util.ArrayList;
import java.util.List;

public class CommandsCommand extends Command {
    public CommandsCommand(String commandName) {
        super(commandName);
    }

    @Override
    public boolean execute(String sender, String[] args) {

        sendChatMessage("Here are my commands:");
        List<StringBuilder> builders = new ArrayList<>();
        builders.add(new StringBuilder());
        for(Command command : CommandManager.commandList.values()){
            StringBuilder builder = builders.get(builders.size()-1);
            if(builder.length() + command.getCommandName().length() + 2 >= 256) {
                builders.add(new StringBuilder());
                builder = builders.get(builders.size()-1);
            }
            builder.append(command.getCommandName()).append(", ");
        }
        for(StringBuilder builder : builders){
            sendChatMessage(builder.toString());
        }
        return true;
    }
}
