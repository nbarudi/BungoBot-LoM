package ca.bungo.bungobot.commands;

import baritone.api.BaritoneAPI;
import ca.bungo.bungobot.Command;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;

public class ComeCommand extends Command {
    public ComeCommand(String commandName) {
        super(commandName);
    }

    @Override
    public boolean execute(String sender, String[] args) {
        sendChatMessage("On my way!");
        PlayerEntity player = findPlayer(sender);
        if(player == null) return true;
        double x = player.getPos().x;
        double z = player.getPos().z;
        BaritoneAPI.getProvider().getPrimaryBaritone().getCommandManager().execute("goto " + x + " " + z);
        return true;
    }

    private PlayerEntity findPlayer(String name){
        for(PlayerEntity player : MinecraftClient.getInstance().player.clientWorld.getPlayers()){
            if(player.getName().getString().equals(name))
                return player;
        }
        return null;
    }
}
