package ca.bungo.mixin.client;

import ca.bungo.BotBungo;
import ca.bungo.data.InventoryInstances;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.registry.Registries;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HandledScreens.class)
public class HandledScreensMixin {

    @Inject(method = "open", at = @At("HEAD"), cancellable = true)
    private static <T extends ScreenHandler>void open(ScreenHandlerType<T> type, MinecraftClient client, int id, Text title, CallbackInfo ci){
        InventoryInstances.respondData(type, client, id, title, ci);
    }

}
