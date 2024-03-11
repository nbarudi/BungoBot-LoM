package ca.bungo.bungobot.commands;

import ca.bungo.bungobot.Command;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;

public class StandCommand extends Command {

    public StandCommand(String commandName) {
        super(commandName);
    }

    @Override
    public boolean execute(String sender, String[] args) {

        assert MinecraftClient.getInstance().player != null;
        if(!MinecraftClient.getInstance().player.hasVehicle()){
            sendChatMessage("Understood!");
            ClientCommandC2SPacket packet = new ClientCommandC2SPacket(MinecraftClient.getInstance().player, ClientCommandC2SPacket.Mode.RELEASE_SHIFT_KEY);
            MinecraftClient.getInstance().player.networkHandler.sendPacket(packet);
        }else{
            sendChatMessage("Understood! I will stand up!");
            sendChatMessage("/sit");
        }
        return true;

    }
}
