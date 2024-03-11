package ca.bungo.data.farmers;

import baritone.api.BaritoneAPI;
import ca.bungo.BotBungo;
import ca.bungo.BotBungoClient;
import ca.bungo.data.InventoryInstances;
import ca.bungo.data.WaypointInfo;
import ca.bungo.data.types.InventoryTypeStorage;
import ca.bungo.data.types.ItemTypes;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.ClickSlotC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.network.packet.s2c.play.InventoryS2CPacket;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Box;
import org.lwjgl.glfw.GLFW;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class AutoSellFish {

    private static boolean registered = false;
    private static boolean active = false;

    public static void startBot(){
        if(!registered){
            registered = true;
            registerEvents();
        }

        active = true;
        BotBungo.LOGGER.info("Starting Auto Sell Fish Bot!");
        BotBungo.LOGGER.info("Starting Pathing!");
        WaypointInfo.JUNK_SELLER.pathToWaypoint();

        BotBungo.LOGGER.info("Waiting for pathing...");
        while(true) {
            if(BaritoneAPI.getProvider().getPrimaryBaritone().getCustomGoalProcess().isActive()) continue;
            break;
        }


        MinecraftClient client = MinecraftClient.getInstance();
        if(client.player == null) return;
        if(client.world == null){
            BotBungo.LOGGER.info("World is null!");
            return;
        }

        Box box = new Box(client.player.getBlockPos()).expand(7);

        List<VillagerEntity> entities = client.world.getEntitiesByClass(VillagerEntity.class, box, LivingEntity::isAlive);
        if(entities.isEmpty()){
            BotBungo.LOGGER.info("Could not find villager! List is 0!");
            return;
        }
        VillagerEntity villagerEntity = entities.get(0);
        if(villagerEntity == null) {
            BotBungo.LOGGER.info("Could not find villager!");
            return;
        }

        BotBungo.LOGGER.info("Sending entity interact packet...");
        PlayerInteractEntityC2SPacket playerInteractEntityC2SPacket = PlayerInteractEntityC2SPacket.interactAt(villagerEntity,
                false, Hand.MAIN_HAND, villagerEntity.getBlockPos().toCenterPos());
        client.player.networkHandler.sendPacket(playerInteractEntityC2SPacket);
        BotBungo.LOGGER.info("Sent!");
    }

    private static boolean isStorageMode = false;
    private static void startConvertingStatus(){
        active = true;
        WaypointInfo.CURRENCY_CONVERT.pathToWaypoint();

        BotBungo.LOGGER.info("Waiting for pathing...");
        while(true) {
            if(BaritoneAPI.getProvider().getPrimaryBaritone().getCustomGoalProcess().isActive()) continue;
            break;
        }

        MinecraftClient client = MinecraftClient.getInstance();
        if(client.player == null) return;
        if(client.world == null){
            BotBungo.LOGGER.info("World is null!");
            return;
        }

        Box box = new Box(client.player.getBlockPos()).expand(7);

        List<VillagerEntity> entities = client.world.getEntitiesByClass(VillagerEntity.class, box, LivingEntity::isAlive);
        if(entities.isEmpty()){
            BotBungo.LOGGER.info("Could not find villager! List is 0!");
            return;
        }
        VillagerEntity villagerEntity = entities.get(0);
        if(villagerEntity == null) {
            BotBungo.LOGGER.info("Could not find villager!");
            return;
        }

        BotBungo.LOGGER.info("Sending entity interact packet...");
        PlayerInteractEntityC2SPacket playerInteractEntityC2SPacket = PlayerInteractEntityC2SPacket.interactAt(villagerEntity,
                false, Hand.MAIN_HAND, villagerEntity.getBlockPos().toCenterPos());
        client.player.networkHandler.sendPacket(playerInteractEntityC2SPacket);
        BotBungo.LOGGER.info("Sent!");
    }



    private static void registerEvents(){
        InventoryInstances.GenericInventoryOpenCallback.EVENT.register((client, title, handler) ->{

            if(!active) return ActionResult.PASS;

            if(client.interactionManager == null) return ActionResult.FAIL;

            if(title.getString().contains("Jimothy Worrywart")) {
                //Junk Seller NPC
                client.interactionManager.clickSlot(handler.syncId, 26, 0, SlotActionType.PICKUP, client.player);
                return ActionResult.PASS;
            }
            else if(title.getString().contains("Jamie Franklin")){
                if(isStorageMode){
                    client.interactionManager.clickSlot(handler.syncId, 26, 0, SlotActionType.PICKUP, client.player);
                }else{
                    client.interactionManager.clickSlot(handler.syncId, 17, 0, SlotActionType.PICKUP, client.player);
                }
            }
            else if(title.getString().contains("Sell Junk")){
                //Sell Junk Screen
                if(!sellItem(ItemTypes.FISH_SELL_ITEM, client, handler)){
                    BotBungo.LOGGER.info("Stopped Selling Fish!");
                    if(!sellItem(ItemTypes.SHELL_SELL_ITEM, client, handler)){
                        BotBungo.LOGGER.info("Stopped Selling Shells!");
                        if(!sellItem(ItemTypes.BONE_SELL_ITEM, client, handler)){
                            BotBungo.LOGGER.info("Stopped Selling Bones!");
                            active = false;
                            BotBungoClient.schedule(AutoSellFish::startConvertingStatus, 1, TimeUnit.SECONDS);
                            //return ActionResult.PASS;
                        }
                        //return ActionResult.PASS;
                    }
                    //return ActionResult.PASS;
                }
            }
            else if(title.getString().contains("Tool Store")){
                String result = convertCurrency(client, handler);
                if(result.equals("No Currency")){
                    BotBungo.LOGGER.info("Stopping Converting Currency!");
                    active = false;
                    if(ItemTypes.getSellItem(client.player.getInventory().main, ItemTypes.FISH_SELL_ITEM) != null ||
                            ItemTypes.getSellItem(client.player.getInventory().main, ItemTypes.SHELL_SELL_ITEM) != null ||
                            ItemTypes.getSellItem(client.player.getInventory().main, ItemTypes.BONE_SELL_ITEM) != null){
                        BotBungoClient.schedule(AutoSellFish::startBot, 1, TimeUnit.SECONDS);
                    }else{
                        BaritoneAPI.getProvider().getPrimaryBaritone().getCommandManager().execute("wp goto dummylocation");
                        BaritoneAPI.getProvider().getPrimaryBaritone().getCommandManager().execute("wp d dummylocation");

                        client.options.inventoryKey.setPressed(true);
                        client.options.inventoryKey.setPressed(false);

                    }
                }
            }
            else{
                BotBungo.LOGGER.info(title.getString() + " | " + handler.getClass().getName());
            }
            return ActionResult.PASS;
        });
    }

    private static boolean sellItem(ItemTypes type, MinecraftClient client, GenericContainerScreenHandler handler){
        if(ItemTypes.getSellItem(client.player.getInventory().main, type) != null){
            if(client.player.getInventory().getEmptySlot() == -1) {
                return false;
            }
            client.interactionManager.clickSlot(handler.syncId, type.sellSlot, 0, SlotActionType.PICKUP, client.player);
            return true;
        }
        return false;
    }

    private static String convertCurrency(MinecraftClient client, GenericContainerScreenHandler handler){
        if(ItemTypes.getSellItem(client.player.getInventory().main, ItemTypes.COPPER_CONVERT_ITEM) == null
                || ItemTypes.COPPER_CONVERT_ITEM.getItemCount(client.player.getInventory().main) < 50){
            if (ItemTypes.getSellItem(client.player.getInventory().main, ItemTypes.SILVER_CONVERT_ITEM) != null) {
                if (ItemTypes.SILVER_CONVERT_ITEM.getItemCount(client.player.getInventory().main) >= 50) {
                    client.interactionManager.clickSlot(handler.syncId, 7, 0, SlotActionType.PICKUP, client.player);
                    return "Sold Silver";
                }
            }
            return "No Currency";
        }else{
            if(ItemTypes.COPPER_CONVERT_ITEM.getItemCount(client.player.getInventory().main) >= 50){
                client.interactionManager.clickSlot(handler.syncId, 6, 0, SlotActionType.PICKUP, client.player);
                return "Sold Copper";
            }
            return "No Currency";
        }
    }
}
