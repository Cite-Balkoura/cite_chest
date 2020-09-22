package fr.milekat.cite_box.utils;

import fr.milekat.cite_box.MainBox;
import fr.milekat.cite_box.obj.Crate;
import fr.milekat.cite_core.MainCore;
import fr.milekat.cite_libs.utils_tools.ItemParser;
import fr.mrmicky.fastinv.ItemBuilder;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;

public class CrateRegister {
    public CrateRegister() {
        Connection connection = MainCore.getSQL().getConnection();
        try {
            PreparedStatement q = connection.prepareStatement("SELECT * FROM `" + MainCore.SQLPREFIX + "crates`;");
            q.execute();
            while (q.getResultSet().next()) {
                PreparedStatement q2 = connection.prepareStatement("SELECT * FROM `" + MainCore.SQLPREFIX +
                        "crates_loots` NATURAL JOIN `balkoura_material_liste` WHERE `crate_id` = ?;");
                q2.setInt(1,q.getResultSet().getInt("crate_id"));
                q2.execute();
                float totalLuck = 0L;
                HashMap<ItemStack, Float> itemsLucks = new HashMap<>();
                while (q2.getResultSet().next()) {
                    if (q2.getResultSet().getString("material")==null) continue;
                    itemsLucks.put(ItemParser.setItem(
                            q2.getResultSet().getString("material"),
                            q2.getResultSet().getString("name"),
                            q2.getResultSet().getString("enchantment"),
                            "Chance: " + q2.getResultSet().getFloat("luck") * 100 + "%",
                            q2.getResultSet().getInt("item_amount")),
                            q2.getResultSet().getFloat("luck"));
                }
                q2.close();
                Material material = Material.getMaterial(q.getResultSet().getString("crate_display_item"));
                if (itemsLucks.isEmpty() || material==null) continue;
                MainBox.crates.put(
                        q.getResultSet().getInt("crate_id"),
                        new Crate(
                                q.getResultSet().getInt("crate_id"),
                                q.getResultSet().getString("crate_name"),
                                q.getResultSet().getString("crate_description"),
                                totalLuck,
                                itemsLucks,
                                q.getResultSet().getInt("min_items"),
                                q.getResultSet().getInt("max_items"),
                                setDisplayItem(
                                        material,
                                        q.getResultSet().getString("crate_name"),
                                        q.getResultSet().getString("crate_description"),
                                        q.getResultSet().getInt("min_items"),
                                        q.getResultSet().getInt("max_items")),
                                q.getResultSet().getInt("crate_display_slot")
                        ));
            }
            q.close();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    private ItemStack setDisplayItem(Material material, String name, String desc, int min, int max) {
        ItemBuilder itemStack = new ItemBuilder(material).name("§7Box: §6" + name);
        itemStack.addLore("§7Description:");
        for (String line: desc.split("(?<=\\G.{25,}\\s)")) {
            itemStack.addLore(ChatColor.translateAlternateColorCodes('&', line));
        }
        itemStack.addLore("§7Items mini: §e" + min)
                .addLore("§7Items maxi: §a" + max);
        return itemStack.build();
    }
}
