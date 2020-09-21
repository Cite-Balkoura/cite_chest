package fr.milekat.cite_box.events;

import fr.milekat.cite_box.MainBox;
import fr.milekat.cite_core.MainCore;
import fr.milekat.cite_libs.utils_tools.DateMilekat;
import fr.milekat.cite_libs.utils_tools.ItemSerial;
import fr.milekat.cite_libs.utils_tools.Tools;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class LootBoxEvents implements Listener {

    @EventHandler (priority = EventPriority.LOWEST)
    public void onOpenBox(PlayerInteractEvent event) {
        if (event.getClickedBlock()==null || !event.getClickedBlock().getLocation().equals(MainBox.DAYCHEST)) return;
        event.setCancelled(true);
        Inventory inventory = Bukkit.createInventory(null, 27,
                "Box du " + DateMilekat.setDateNow().substring(0,10));
        try {
            Connection connection = MainCore.getSQL().getConnection();
            PreparedStatement q = connection.prepareStatement("SELECT `daily_box` FROM `" + MainCore.SQLPREFIX +
                    "player` WHERE `uuid` = ?;");
            q.setString(1, event.getPlayer().getUniqueId().toString());
            q.execute();
            while (q.getResultSet().next()) {
                inventory.setContents(ItemSerial.invFromBase64(q.getResultSet().getString("daily_box")));
            }
            q.close();
            event.getPlayer().openInventory(inventory);
        } catch (SQLException throwables) {
            event.getPlayer().sendMessage(MainCore.prefixCmd + "§cErreur SQL");
            throwables.printStackTrace();
        }
    }

    /**
     *      Event de récupération des items de la box du joueur
     */
    @EventHandler
    private void onClickBoxEvent(InventoryClickEvent event){
        if (event.getView().getTitle().equalsIgnoreCase("Box du " + DateMilekat.setDateNow().substring(0,10))) {
            if (event.getClickedInventory().getType().equals(InventoryType.PLAYER)) return;
            event.setCancelled(true);
            if (event.getCurrentItem()==null) return;
            if (!event.getCursor().getType().equals(Material.AIR)) return;
            if (Tools.canStore(event.getWhoClicked().getInventory(),36,event.getCurrentItem(),1)){
                try {
                    Connection connection = MainCore.getSQL().getConnection();
                    ItemStack item = event.getCurrentItem();
                    event.getView().setItem(event.getSlot(),null);
                    PreparedStatement q = connection.prepareStatement("UPDATE `" + MainCore.SQLPREFIX + "player` " +
                            "SET `daily_box` = ? WHERE `uuid` = ?;");
                    q.setString(1, ItemSerial.invToBase64(event.getView().getTopInventory().getContents()));
                    q.setString(2, event.getWhoClicked().getUniqueId().toString());
                    q.execute();
                    q.close();
                    event.getWhoClicked().getInventory().addItem(item);
                } catch (SQLException throwables) {
                    event.getWhoClicked().sendMessage(MainCore.prefixCmd + "§cNous rencontrons un soucis avec les Box.");
                    event.getWhoClicked().closeInventory();
                    throwables.printStackTrace();
                }

            } else {
                event.getWhoClicked().sendMessage(MainCore.prefixCmd + "§cInventaire full.");
            }
        }
    }

    /**
     *      Save SQL du content de l'inv
     */
    @EventHandler
    private void onCloseEditBox(InventoryCloseEvent event) {
        if (!event.getPlayer().hasPermission("modo.chest.command.box.edit")) return;
        if (event.getView().getTitle().length() < 28 || !event.getView().getTitle().startsWith("§c[EDIT] §rbox du ")) return;
        try {
            Connection connection = MainCore.getSQL().getConnection();
            PreparedStatement q = connection.prepareStatement("INSERT INTO `balkoura_box`(`box_date`, `box_content`) " +
                    "VALUES (?,?) ON DUPLICATE KEY UPDATE `box_content` = ?;");
            String content = ItemSerial.invToBase64(event.getView().getTopInventory().getContents());
            String date = event.getView().getTitle().substring(event.getView().getTitle().length() - 10);
            q.setString(1,date);
            q.setString(2,content);
            q.setString(3,content);
            q.execute();
            q.close();
            Inventory box = Bukkit.createInventory(null,27,"Box du " + date);
            box.setContents(event.getView().getTopInventory().getContents());
            MainBox.lootbox.put(date, box);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
