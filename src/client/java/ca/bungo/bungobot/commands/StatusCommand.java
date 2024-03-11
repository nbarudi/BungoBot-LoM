package ca.bungo.bungobot.commands;

import ca.bungo.bungobot.Command;
import net.minecraft.client.MinecraftClient;

public class StatusCommand extends Command {
    public StatusCommand(String commandName) {
        super(commandName);
    }

    @Override
    public boolean execute(String sender, String[] args) {

        assert MinecraftClient.getInstance().player != null;
        sendChatMessage(String.format(
                "I am currently %.0f percent rested!",
                (MinecraftClient.getInstance().player.getHealth() / MinecraftClient.getInstance().player.getMaxHealth())*100
        ));
        return true;
    }
}
