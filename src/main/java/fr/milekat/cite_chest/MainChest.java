package fr.milekat.cite_chest;

import fr.milekat.cite_chest.commands.LootCrates;
import fr.milekat.cite_chest.commands.LootBoxCmd;
import fr.milekat.cite_chest.events.Load;
import fr.milekat.cite_chest.events.LootBoxEvents;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public class MainChest extends JavaPlugin {
    public static Location DAYCHEST = new Location(Bukkit.getWorld("world"),26,99,18);
    public static HashMap<String, Inventory> lootbox = new HashMap<>();
    @Override
    public void onEnable() {
        new Load();
        getServer().getPluginManager().registerEvents(new LootBoxEvents(),this);
        getCommand("crate").setExecutor(new LootCrates());
        getCommand("box").setExecutor(new LootBoxCmd());
    }
}