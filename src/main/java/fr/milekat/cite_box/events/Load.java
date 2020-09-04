package fr.milekat.cite_box.events;

import fr.milekat.cite_box.MainBox;
import fr.milekat.cite_core.MainCore;
import fr.milekat.cite_core.utils_tools.ItemSerial;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Load {
    /**
     *      Chargement des LootBox Template & Crates
     */
    public Load() {
        try {
            Connection connection = MainCore.sql.getConnection();
            PreparedStatement q = connection.prepareStatement("SELECT * FROM `balkoura_box`");
            q.execute();
            while (q.getResultSet().next()) {
                String date = q.getResultSet().getString("box_date");
                Inventory box = Bukkit.createInventory(null,27,"Box du " + date);
                box.setContents(ItemSerial.invFromBase64(q.getResultSet().getString("box_content")));
                MainBox.lootbox.put(date,box);
            }
            q.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
