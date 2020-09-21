package fr.milekat.cite_box;

import fr.milekat.cite_box.commands.LootCrates;
import fr.milekat.cite_box.commands.LootBoxCmd;
import fr.milekat.cite_box.engines.NewDayUpdate;
import fr.milekat.cite_box.events.LootBoxEvents;
import fr.milekat.cite_box.obj.Crate;
import fr.milekat.cite_box.utils.Load;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;

public class MainBox extends JavaPlugin {
    public static Location DAYCHEST;
    public static Location CRATECHEST;
    public static HashMap<String, Inventory> lootbox = new HashMap<>();
    public static HashMap<Integer, Crate> crates = new HashMap<>();
    private BukkitTask newDayUpdate;
    private static MainBox mainChest;

    @Override
    public void onEnable() {
        mainChest = this;
        DAYCHEST = new Location(Bukkit.getWorld("world"),26,99,18);
        CRATECHEST = new Location(Bukkit.getWorld("world"),-14,155,-8);
        getServer().getPluginManager().registerEvents(new LootBoxEvents(),this);
        getCommand("crate").setExecutor(new LootCrates());
        getCommand("box").setExecutor(new LootBoxCmd());
        new Load();
        newDayUpdate = new NewDayUpdate().runTask();
    }

    @Override
    public void onDisable() {
        newDayUpdate.cancel();
    }

    public static MainBox getInstance(){
        return mainChest;
    }
}