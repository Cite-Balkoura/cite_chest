package fr.milekat.cite_box;

import fr.milekat.cite_box.chests.LootBoxCmd;
import fr.milekat.cite_box.chests.LootCrates;
import fr.milekat.cite_box.chests.LootCratesTAB;
import fr.milekat.cite_box.engines.NewDayUpdate;
import fr.milekat.cite_box.events.LootBoxEvents;
import fr.milekat.cite_box.events.OpenCrateEvent;
import fr.milekat.cite_box.obj.Crate;
import fr.milekat.cite_box.utils.CrateRegister;
import fr.milekat.cite_box.utils.GUI.CrateGUI;
import fr.milekat.cite_box.utils.Load;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;

public class MainBox extends JavaPlugin {
    public static World WORLD;
    public static Location DAYCHEST;
    public static Location CRATECHEST;
    public static String prefixConsole = "[Balkoura-Chest-Crates]";
    public static HashMap<String, Inventory> lootbox = new HashMap<>();
    public static HashMap<Integer, Crate> crates = new HashMap<>();
    private BukkitTask newDayUpdate;
    private static MainBox mainChest;

    @Override
    public void onEnable() {
        mainChest = this;
        WORLD = Bukkit.getWorld("world");
        DAYCHEST = new Location(WORLD,-9,99,15);
        CRATECHEST = new Location(WORLD,-14,155,-8);
        // Events
        getServer().getPluginManager().registerEvents(new LootBoxEvents(),this);
        getServer().getPluginManager().registerEvents(new OpenCrateEvent(),this);
        getServer().getPluginManager().registerEvents(new CrateGUI(),this);
        // Commandes
        getCommand("box").setExecutor(new LootBoxCmd());
        new CrateRegister();
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