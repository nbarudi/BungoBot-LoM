package ca.bungo.bungobot.commands;

import ca.bungo.BotBungoClient;
import ca.bungo.bungobot.Command;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.EntityPose;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;

import java.util.concurrent.TimeUnit;

public class SniffCommand extends Command {
    public SniffCommand(String commandName) {
        super(commandName);


    }

    @Override
    public boolean execute(String sender, String[] args) {
        sendChatMessage("/me sniffs");
        ClientCommandC2SPacket startPacket = new ClientCommandC2SPacket(MinecraftClient.getInstance().player, ClientCommandC2SPacket.Mode.PRESS_SHIFT_KEY);
        MinecraftClient.getInstance().player.networkHandler.sendPacket(startPacket);
        BotBungoClient.schedule(() -> {
            ClientCommandC2SPacket endPacket = new ClientCommandC2SPacket(MinecraftClient.getInstance().player, ClientCommandC2SPacket.Mode.RELEASE_SHIFT_KEY);
            MinecraftClient.getInstance().player.networkHandler.sendPacket(endPacket);
        }, 6, TimeUnit.SECONDS);
        return true;
    }
}
