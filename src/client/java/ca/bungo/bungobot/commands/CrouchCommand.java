package ca.bungo.bungobot.commands;

import ca.bungo.bungobot.Command;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;

public class CrouchCommand extends Command {
    public CrouchCommand(String commandName) {
        super(commandName);
    }

    @Override
    public boolean execute(String sender, String[] args) {
        sendChatMessage("Crouching!");
        ClientCommandC2SPacket startPacket = new ClientCommandC2SPacket(MinecraftClient.getInstance().player, ClientCommandC2SPacket.Mode.PRESS_SHIFT_KEY);
        MinecraftClient.getInstance().player.networkHandler.sendPacket(startPacket);
        return true;
    }
}
