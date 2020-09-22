package fr.milekat.cite_box.obj;

import org.bukkit.inventory.ItemStack;

import java.util.TreeMap;

public class Crate {
    private final int id;
    private final String name;
    private final String description;
    private final long totalLuck;
    private final TreeMap<Integer, ItemStack> itemsLucks;
    private final int minItems;
    private final int maxItems;
    private final ItemStack displayItem;
    private final int displaySlot;

    public Crate(int id, String name, String description, long totalLuck, TreeMap<Integer, ItemStack> itemsLucks,
                 int minItems, int maxItems, ItemStack displayItem, int displaySlot) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.totalLuck = totalLuck;
        this.itemsLucks = itemsLucks;
        this.minItems = minItems;
        this.maxItems = maxItems;
        this.displayItem = displayItem;
        this.displaySlot = displaySlot;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public long getTotalLuck() {
        return totalLuck;
    }

    public TreeMap<Integer, ItemStack> getItemsLucks() {
        return itemsLucks;
    }

    public int getMinItems() {
        return minItems;
    }

    public int getMaxItems() {
        return maxItems;
    }

    public ItemStack getDisplayItem() {
        return displayItem;
    }

    public int getDisplaySlot() {
        return displaySlot;
    }
}
