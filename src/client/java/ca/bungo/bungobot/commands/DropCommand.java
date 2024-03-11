package ca.bungo.bungobot.commands;

import ca.bungo.BotBungo;
import ca.bungo.bungobot.Command;
import ca.bungo.mixin.client.ClientPlayNetworkMixin;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.SlotActionType;

public class DropCommand extends Command {
    public DropCommand(String commandName) {
        super(commandName);
    }

    @Override
    public boolean execute(String sender, String[] args) {
        if(args.length <= 1) return false;
        String mode = args[1];

        MinecraftClient client = MinecraftClient.getInstance();
        ClientPlayerEntity player = client.player;

        if(mode.equalsIgnoreCase("hand")){
            if(player.dropSelectedItem(true)){
                sendChatMessage("Dropping held item!");
            }
            else {
                sendChatMessage("I am not holding any items!");
            }
        }
        else if(mode.equalsIgnoreCase("all")){
            for(int i = 0; i < player.getInventory().size(); i++){
                ItemStack stack = player.getInventory().getStack(i);
                if(!stack.isEmpty()){
                    client.interactionManager.clickSlot(player.playerScreenHandler.syncId, i, player.getInventory().selectedSlot, SlotActionType.SWAP, player);
                    player.dropSelectedItem(true);
                }
            }
            sendChatMessage("I have dropped all inventory items!");
        }

        return true;
    }
}
