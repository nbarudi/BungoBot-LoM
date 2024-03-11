package ca.bungo.data.types;

import net.minecraft.item.ItemStack;

import java.util.List;

public class InventoryTypeStorage {

    public int syncId;
    public int revision;
    public List<ItemStack> items;

    public InventoryTypeStorage(int syncId, int revision, List<ItemStack> items){
        this.syncId = syncId;
        this.revision = revision;
        this.items = items;
    }

}
