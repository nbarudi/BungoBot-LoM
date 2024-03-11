package ca.bungo.bungobot.commands;

import ca.bungo.bungobot.Command;
import net.minecraft.client.MinecraftClient;

public class SitCommand extends Command {

    public SitCommand(String commandName) {
        super(commandName);
    }

    @Override
    public boolean execute(String sender, String[] args) {

        assert MinecraftClient.getInstance().player != null;
        if(MinecraftClient.getInstance().player.hasVehicle()){
            sendChatMessage("Sorry! I am already sitting");
        }else{
            sendChatMessage("Understood! I will rest my servos!");
            sendChatMessage("/sit");
        }
        return true;

    }
}
