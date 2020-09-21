package fr.milekat.cite_box.events;

import fr.milekat.cite_box.MainBox;
import fr.milekat.cite_box.utils.MainCratesGUI;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class OpenCrateEvent implements Listener {
    @EventHandler(priority = EventPriority.LOWEST)
    public void onOpenCrate(PlayerInteractEvent event) {
        if (event.getClickedBlock()==null || !event.getClickedBlock().getLocation().equals(MainBox.CRATECHEST)) return;
        event.setCancelled(true);
        new MainCratesGUI().open(event.getPlayer());
    }
}
