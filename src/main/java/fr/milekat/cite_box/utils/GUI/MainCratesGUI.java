package fr.milekat.cite_box.utils.GUI;

import fr.milekat.cite_box.MainBox;
import fr.milekat.cite_box.obj.Crate;
import fr.milekat.cite_core.MainCore;
import fr.milekat.cite_core.core.obj.Profil;
import fr.mrmicky.fastinv.FastInv;
import fr.mrmicky.fastinv.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.Arrays;

public class MainCratesGUI extends FastInv {

    /**
     *      Menu principal avec toutes les crates affichées
     */
    public MainCratesGUI(Player player) {
        super(45, MainCore.prefixCmd + "§8Crates !");
        // Ajout des glass panes vertes.
        for (int i=0; i<45; i++) {
            setItem(i, new ItemBuilder(Material.LIME_STAINED_GLASS_PANE).name(" ").build());
        }
        // Ajout des glass panes grises, sur les côtés.
        for (int slot: Arrays.asList(0, 8, 9, 17, 18, 26, 27, 35, 36, 44)) {
            setItem(slot, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).name(" ").build());
        }
        for (Crate crate: MainBox.crates.values()) {
            ItemBuilder itemBuilder = new ItemBuilder(crate.getDisplayItem().clone());
            Profil profil = MainCore.profilHashMap.get(player.getUniqueId());
            itemBuilder.addLore("§7Disponibles: §b" + profil.getCrates().getOrDefault(crate.getId(),0));
            if (profil.getCrates().getOrDefault(crate.getId(),0)>0) {
                itemBuilder.addLore("§a§l[Box disponibles]");
            }
            setItem(crate.getDisplaySlot(), itemBuilder.build().clone()
                    , e -> new DetailsGui(crate, (Player) e.getWhoClicked()).open((Player) e.getWhoClicked()));
        }
    }

    @Override
    protected void onClick(InventoryClickEvent event) {
        event.setCancelled(true);
    }
}
