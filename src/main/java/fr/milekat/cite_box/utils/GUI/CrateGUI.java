package fr.milekat.cite_box.utils.GUI;

import fr.milekat.cite_box.MainBox;
import fr.milekat.cite_box.obj.Crate;
import fr.milekat.cite_core.MainCore;
import fr.milekat.cite_core.core.obj.Profil;
import fr.milekat.cite_libs.utils_tools.Tools;
import fr.mrmicky.fastinv.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class CrateGUI implements Listener {

    /**
     *      Lancement d'un tirage de crate
     */
    public void openCrate(Crate crate, Player player) {
        Inventory crateInv = Bukkit.createInventory(null, 36, "§8Ouverture de la box " + crate.getName());
        for (int i=0; i<9; i++) {
            crateInv.setItem(i, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).name(" ").build());
        }
        for (int i=27; i<36; i++) {
            crateInv.setItem(i, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).name(" ").build());
        }
        for (int slot: Arrays.asList(9, 17, 18, 26)) {
            crateInv.setItem(slot, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).name(" ").build());
        }
        int givedItems = new Random().nextInt(crate.getMaxItems() - crate.getMinItems()) + crate.getMinItems();
        for (int loop = 1; loop<=givedItems; loop++) {
            for (int slots=0; slots<35; slots++) {
                if (crateInv.getItem(slots)!=null) continue;
                crateInv.setItem(
                        slots,crate.getItemsLucks().ceilingEntry(new Random().nextInt((int) crate.getTotalLuck())).getValue());
                break;
            }
        }
        player.openInventory(crateInv);
        updatePlayerCrates(player, crate.getId());
    }

    /**
     *      Empêcher de déposer des items dans l'inventaire, ou de récupérer les glass panes
     */
    @EventHandler
    private void onCrateClick(InventoryClickEvent event) {
        if (!event.getView().getTitle().startsWith("§8Ouverture de la box ")) return;
        if (!(event.getClickedInventory() ==null) && !event.getClickedInventory().getType().equals(InventoryType.PLAYER)) {
            if (!event.getAction().equals(InventoryAction.PICKUP_ALL)) {
                event.setCancelled(true);
            } else if (event.getCurrentItem() == null) {
                event.setCancelled(true);
            } else if (event.getCursor() != null && !event.getCursor().getType().isAir()) {
                event.setCancelled(true);
            } else if (event.getCurrentItem().getType().equals(Material.BLACK_STAINED_GLASS_PANE)) {
                event.setCancelled(true);
            } else if (event.getCurrentItem()!=null) {
                ItemMeta meta = event.getCurrentItem().getItemMeta();
                if (meta!=null && meta.getLore()!=null && meta.getLore().get(0).startsWith("Chance:")) {
                    meta.setLore(null);
                    event.getCurrentItem().setItemMeta(meta);
                }
            }
        }
    }

    /**
     *      Ejection des items non récoltés, récupérable par tous désormais.
     */
    @EventHandler
    protected void onCrateClose(InventoryCloseEvent event) {
        if (!event.getView().getTitle().startsWith("§8Ouverture de la box ")) return;
        for (ItemStack itemStack: event.getInventory().getContents()) {
            if (itemStack==null || itemStack.getType().equals(Material.BLACK_STAINED_GLASS_PANE)) continue;
            ItemMeta meta = itemStack.getItemMeta();
            if (meta!=null) {
                meta.setLore(null);
                itemStack.setItemMeta(meta);
            }
            for (ItemStack todrop: event.getPlayer().getInventory().addItem(itemStack).values())
                MainBox.WORLD.dropItemNaturally(event.getPlayer().getLocation(),todrop);
        }
    }

    private void updatePlayerCrates(Player player, int crateid) {
        new BukkitRunnable() {
            @Override
            public void run() {
                Profil profil = MainCore.profilHashMap.get(player.getUniqueId());
                HashMap<Integer, Integer> crates = profil.getCrates();
                crates.put(crateid, profil.getCrates().getOrDefault(crateid, 0) - 1);
                profil.setCrates(crates);
                try {
                    Connection connection = MainCore.getSQL().getConnection();
                    PreparedStatement q = connection.prepareStatement(
                            "UPDATE `balkoura_player` SET `crates`=? WHERE `uuid` = ?;");
                    StringBuilder stringBuilder = new StringBuilder();
                    for (Map.Entry<Integer, Integer> cratesloop: profil.getCrates().entrySet()) {
                        stringBuilder.append(cratesloop.getKey()).append(":").append(cratesloop.getValue()).append(";");
                    }
                    q.setString(1, Tools.remLastChar(stringBuilder.toString()));
                    q.setString(2, profil.getUuid().toString());
                    q.execute();
                    q.close();
                } catch (SQLException exception) {
                    exception.printStackTrace();
                }
            }
        }.runTaskAsynchronously(MainBox.getInstance());
    }
}
