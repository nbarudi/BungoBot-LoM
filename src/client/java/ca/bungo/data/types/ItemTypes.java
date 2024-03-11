package ca.bungo.data.types;

import ca.bungo.BotBungo;
import net.minecraft.item.ItemStack;

import java.util.List;

public enum ItemTypes {

    JUNK_SELL_ITEM("Junk Sell Items", "jigsaw", 206, 26),
    FISH_SELL_ITEM("Fish Item", "rabbit_foot", 9, 0),
    SHELL_SELL_ITEM("Shell Item", "rabbit_foot", 16, 1),
    BONE_SELL_ITEM("Bone Item", "bone", 0, 3),
    SILVER_CONVERT_ITEM("Silver Item", "rabbit_foot", 17, 7),
    GOLD_CONVERT_ITEM("Gold Item", "rabbit_foot", 10, 10),
    COPPER_CONVERT_ITEM("Copper Item", "rabbit_foot", 8, 6);

    private final String simpleName;
    private final String itemType;
    private final int customModelData;
    public final int sellSlot;
    ItemTypes(String simpleName, String itemType, int customModelData, int sellSlot){
        this.simpleName = simpleName;
        this.itemType = itemType;
        this.customModelData = customModelData;
        this.sellSlot = sellSlot;
    }

    public static ItemStack getSellItem(List<ItemStack> items){
        for(ItemStack item : items){
            for(ItemTypes type : ItemTypes.values()){
                if(item.toString().split(" ")[1].equalsIgnoreCase(type.itemType)){
                    if(item.getNbt() == null) continue;
                    if(item.getNbt().getInt("CustomModelData") == type.customModelData) {
                        return item;
                    }
                }
            }
        }
        return null;
    }

    public static ItemStack getSellItem(List<ItemStack> items, ItemTypes expected){
        for(ItemStack item : items){
            if(item.toString().split(" ")[1].equalsIgnoreCase(expected.itemType)){
                if(item.getNbt() == null) continue;
                if(item.getNbt().getInt("CustomModelData") == expected.customModelData) {
                    return item;
                }
            }
        }
        return null;
    }

    public int getItemCount(List<ItemStack> items){
        int count = 0;
        for(ItemStack item : items){
            if(item.toString().split(" ")[1].equalsIgnoreCase(this.itemType)){
                if(item.getNbt() == null) continue;
                if(item.getNbt().getInt("CustomModelData") == this.customModelData) {
                    count += item.getCount();
                }
            }
        }
        return count;
    }

}
