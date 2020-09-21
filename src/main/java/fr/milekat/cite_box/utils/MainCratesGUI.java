package fr.milekat.cite_box.utils;

import fr.milekat.cite_box.MainBox;
import fr.milekat.cite_box.obj.Crate;
import fr.milekat.cite_core.MainCore;
import fr.mrmicky.fastinv.FastInv;
import fr.mrmicky.fastinv.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class MainCratesGUI extends FastInv {

    /**
     *      Menu principal avec toutes les crates affichées
     */
    public MainCratesGUI() {
        super(63, MainCore.prefixCmd + "§7Crates !");
        // Ajout des glass panes vertes.
        for (int i=0; i<62; i++) {
            setItem(i, new ItemBuilder(Material.LIME_STAINED_GLASS_PANE).name("").build());
        }
        for (Crate crate: MainBox.crates.values()) {
            setItem(crate.getDisplaySlot(), new ItemBuilder(crate.getDisplayItem()).name("§7Box: §6" + crate.getName())
                    .build(), e -> new DetailsGui(crate).open((Player) e.getWhoClicked()));
        }
    }
}
