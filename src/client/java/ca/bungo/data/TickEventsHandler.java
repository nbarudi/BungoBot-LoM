package ca.bungo.data;

import baritone.api.BaritoneAPI;
import ca.bungo.BotBungo;
import ca.bungo.BotBungoClient;
import ca.bungo.bungobot.commands.SniffCommand;
import ca.bungo.bungobot.commands.WatchCommand;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.DeathScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.command.argument.EntityAnchorArgumentType;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.ai.goal.ChaseBoatGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.ClientStatusC2SPacket;
import net.minecraft.util.math.Vec3d;

import java.util.concurrent.TimeUnit;

public class TickEventsHandler {

    private static boolean pausedPathing = false;
    private static int debounce = 1;
    public static void loadTickEvents(){

        ClientTickEvents.START_CLIENT_TICK.register((client) ->{
            if(!BotBungoSettings.careSprintStat) return;
            if(client.player == null) return;

            if(!BaritoneAPI.getProvider().getPrimaryBaritone().getPathingBehavior().isPathing() && pausedPathing) {
                if(client.player.getHealth() >= 20){
                    BotBungo.LOGGER.info("Health full! Resuming pathing!");
                    sendCommand(client, "sit");
                    BaritoneAPI.getProvider().getPrimaryBaritone().getCommandManager().execute("resume");
                    pausedPathing = false;
                }
                else if (!client.player.hasVehicle() && client.player.getHealth() <= 6) {
                    if(debounce % 40 == 0){
                        BotBungo.LOGGER.info("Health unchanged! Not sitting! Fixing");
                        sendCommand(client, "sit");
                    }
                    debounce++;
                }
            }
            if(!pausedPathing && BaritoneAPI.getProvider().getPrimaryBaritone().getPathingBehavior().isPathing()){
                if(client.player.getHealth() <= 6){
                    BotBungo.LOGGER.info("Health low! Pausing pathing!");
                    BaritoneAPI.getProvider().getPrimaryBaritone().getCommandManager().execute("pause");
                    pausedPathing = true;
                    sendCommand(client, "sit");
                }
            }

        });

        ClientTickEvents.END_CLIENT_TICK.register((client) ->{
            if(!BotBungoSettings.autoLookMode) return;
            if(client.player == null) return;
            lookAtClosestPlayer();
        });

        ClientTickEvents.END_CLIENT_TICK.register((client) ->{
            if(!BotBungoSettings.autoRebuild) return;
            if(client.player == null) return;

            if(client.player.isDead() && client.currentScreen instanceof DeathScreen){
                client.getNetworkHandler().sendPacket(new ClientStatusC2SPacket(ClientStatusC2SPacket.Mode.PERFORM_RESPAWN));
                if(BotBungoSettings.autoPathToDestruct){
                    BotBungoClient.schedule(()-> {
                        BaritoneAPI.getProvider().getPrimaryBaritone().getCommandManager().execute("wp goto death");
                        BaritoneAPI.getProvider().getPrimaryBaritone().getCommandManager().execute("wp d death");
                        sendChatMessage("/skin revert");
                        sendChatMessage("/nick BotBungo");
                    }, 2000, TimeUnit.MILLISECONDS);
                }
            }
        });

    }

    public static void sendChatMessage(String message){
        MinecraftClient.getInstance().execute(()->{
            ChatScreen screen = new ChatScreen("");
            Screen prevScreen = MinecraftClient.getInstance().currentScreen;
            MinecraftClient.getInstance().setScreen(screen);
            screen.sendMessage(message, true);
            MinecraftClient.getInstance().setScreen(prevScreen);
        });
    }

    public static void sendCommand(MinecraftClient client, String command){
        ChatScreen screen = new ChatScreen("");
        Screen prevScreen = client.currentScreen;
        client.setScreen(screen);
        screen.sendMessage("/" + command, true);
        client.setScreen(prevScreen);
    }

    private static void lookAtClosestPlayer() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.world == null || client.player == null) {
            return;
        }

        PlayerEntity closestPlayer = findClosestPlayer();

        if (closestPlayer != null) {
            //BotBungo.LOGGER.info("" + closestPlayer.getPos());
            client.player.lookAt(EntityAnchorArgumentType.EntityAnchor.FEET, closestPlayer.getPos());
        }
    }

    private static PlayerEntity findClosestPlayer() {
        MinecraftClient client = MinecraftClient.getInstance();
        PlayerEntity closestPlayer = null;
        double closestDistance = Double.MAX_VALUE;
        boolean watchCommandOverride = false;

        for (PlayerEntity player : client.world.getPlayers()) {
            if (player == client.player) continue; // Skip the client's player
            if(watchCommandOverride) continue;

            if(player.getName().getString().equals(WatchCommand.watching)){
                closestPlayer = player;
                watchCommandOverride = true;
                continue;
            }

            double distance = client.player.squaredDistanceTo(player);
            if (distance < closestDistance) {
                closestPlayer = player;
                closestDistance = distance;
            }
        }

        return closestPlayer;
    }

}
