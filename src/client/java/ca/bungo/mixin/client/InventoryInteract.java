package ca.bungo.mixin.client;

import ca.bungo.BotBungo;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.ClickSlotC2SPacket;
import net.minecraft.screen.slot.SlotActionType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClickSlotC2SPacket.class)
public class InventoryInteract {


    @Inject(method = "<init>(IIIILnet/minecraft/screen/slot/SlotActionType;Lnet/minecraft/item/ItemStack;Lit/unimi/dsi/fastutil/ints/Int2ObjectMap;)V", at = @At("TAIL"))
    public void constructor(int syncId, int revision, int slot, int button, SlotActionType actionType,
                            ItemStack stack, Int2ObjectMap<ItemStack> modifiedStacks, CallbackInfo ci){
        BotBungo.LOGGER.info(String.format("Sync: %s | Revision: %s | Slot: %s | Button: %s", syncId, revision, slot, button));
    }

}
