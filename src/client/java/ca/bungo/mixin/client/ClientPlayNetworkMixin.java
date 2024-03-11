package ca.bungo.mixin.client;

import ca.bungo.BotBungo;
import ca.bungo.bungobot.CommandManager;
import ca.bungo.data.farmers.AutoSellFish;
import ca.bungo.data.types.InventoryTypeStorage;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.ChatMessageS2CPacket;
import net.minecraft.network.packet.s2c.play.InventoryS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkMixin {

    @Inject(method = "onInventory", at = @At("HEAD"))
    public void onInventory(InventoryS2CPacket packet, CallbackInfo ci){
        //AutoSellFish.storage = new InventoryTypeStorage(packet.getSyncId(), packet.getRevision(), packet.getContents());
    }

}
