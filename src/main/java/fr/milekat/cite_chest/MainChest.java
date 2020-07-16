package fr.milekat.cite_chest;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

public class MainChest extends JavaPlugin {
    public static Location DAYCHEST = new Location(Bukkit.getWorld("world"),0,0,0);
    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new Events(),this);
    }
}