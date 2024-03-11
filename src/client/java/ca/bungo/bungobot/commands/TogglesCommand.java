package ca.bungo.bungobot.commands;

import ca.bungo.bungobot.Command;
import ca.bungo.data.BotBungoSettings;

import java.lang.reflect.Field;

public class TogglesCommand extends Command {
    public TogglesCommand(String commandName) {
        super(commandName);
    }

    @Override
    public boolean execute(String sender, String[] args) {

        StringBuilder builder = new StringBuilder();
        builder.append("Here are the fields I can see: ");
        for(Field f : BotBungoSettings.class.getFields()){
            builder.append(f.getName()).append(", ");
        }
        sendChatMessage(builder.substring(0, builder.length()-2));
        return true;
    }
}
