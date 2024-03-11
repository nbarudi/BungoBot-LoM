package ca.bungo.data;

import ca.bungo.BotBungo;
import ca.bungo.BotBungoClient;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.MinecraftClient;
import net.minecraft.registry.Registries;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class InventoryInstances {

    public static <T extends ScreenHandler> void respondData(ScreenHandlerType<T> type, MinecraftClient client, int id, Text title, CallbackInfo info){
//        BotBungo.LOGGER.info(title.getString() + " | " + id + " | " + Registries.SCREEN_HANDLER.getId(type));
        if(!(client.player.currentScreenHandler instanceof GenericContainerScreenHandler handler)) return;
//        BotBungo.LOGGER.info("Current Handler: " + handler.getInventory());
        ActionResult result = GenericInventoryOpenCallback.EVENT.invoker().inventoryOpened(client, title, handler);

        if(result == ActionResult.FAIL){
            info.cancel();
        }
    }

    public interface GenericInventoryOpenCallback {
        Event<GenericInventoryOpenCallback> EVENT = EventFactory.createArrayBacked(GenericInventoryOpenCallback.class, (listeners) ->(client, title, handler) -> {
            for(GenericInventoryOpenCallback listener : listeners){
                ActionResult result = listener.inventoryOpened(client, title, handler);

                if(result != ActionResult.PASS){
                    return result;
                }
            }
            return ActionResult.PASS;
        });

        ActionResult inventoryOpened(MinecraftClient client, Text title, GenericContainerScreenHandler handler);
    }

}
