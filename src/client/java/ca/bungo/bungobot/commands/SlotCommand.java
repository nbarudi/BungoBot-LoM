package ca.bungo.bungobot.commands;

import ca.bungo.bungobot.Command;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.slot.SlotActionType;

public class SlotCommand extends Command {


    public SlotCommand(String commandName) {
        super(commandName);
    }

    @Override
    public boolean execute(String sender, String[] args) {
        if(args.length <= 1) return false;

        try {
            int slot = Integer.parseInt(args[1]);
            if(slot > 9) {
                sendChatMessage("I only have 9 slots to select!");
                return true;
            }
            MinecraftClient client = MinecraftClient.getInstance();
            PlayerEntity player = client.player;
            player.getInventory().selectedSlot = slot-1;
            sendChatMessage("Switching to slot " + slot);
        } catch(NumberFormatException e){
            sendChatMessage("I was not able to translate a slot number!");
        }

        return true;
    }
}
