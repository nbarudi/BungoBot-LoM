package ca.bungo.bungobot.commands;

import ca.bungo.bungobot.Command;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.util.Hand;

public class PunchCommand extends Command {
    public PunchCommand(String commandName) {
        super(commandName);
    }

    @Override
    public boolean execute(String sender, String[] args) {
        sendChatMessage("Rawr!");
        PlayerEntity player = findClosestPlayer();
        if(player != null){
            MinecraftClient.getInstance().getNetworkHandler().sendPacket(PlayerInteractEntityC2SPacket.attack(player, false));
            MinecraftClient.getInstance().player.networkHandler.sendPacket(new HandSwingC2SPacket(Hand.MAIN_HAND));
        }
        return true;
    }

    private PlayerEntity findClosestPlayer() {
        MinecraftClient client = MinecraftClient.getInstance();
        PlayerEntity closestPlayer = null;
        double closestDistance = Double.MAX_VALUE;

        for (PlayerEntity player : client.world.getPlayers()) {
            if (player == client.player) continue; // Skip the client's player

            double distance = client.player.squaredDistanceTo(player);
            if (distance < closestDistance) {
                closestPlayer = player;
                closestDistance = distance;
            }
        }

        return closestPlayer;
    }
}
