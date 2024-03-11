package ca.bungo.bungobot.commands;

import ca.bungo.bungobot.Command;
import ca.bungo.data.BotBungoSettings;

import java.lang.reflect.Field;

public class ToggleCommand extends Command {
    public ToggleCommand(String commandName) {
        super(commandName);
    }

    @Override
    public boolean execute(String sender, String[] args) {

        if(args.length <= 1) return false;

        String toggle = args[1];

        try {
            Field field = BotBungoSettings.class.getField(toggle);
            field.set(null, !(boolean)field.get(null));
            sendChatMessage("Understood! I have now set " + toggle + "'s value to: " + ((boolean)field.get(null) ? "On" : "Off"));
        } catch(NoSuchFieldException e){
            sendChatMessage("Failed to find toggleable variable " + toggle);
            return true;
        } catch (IllegalAccessException e) {
            sendChatMessage("I do not have the ability to access " + toggle);
            return true;
        }

        return true;
    }
}
