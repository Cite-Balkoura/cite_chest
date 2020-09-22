package fr.milekat.cite_box.utils.GUI;

import fr.milekat.cite_box.obj.Crate;
import fr.milekat.cite_core.MainCore;
import fr.milekat.cite_core.core.obj.Profil;
import fr.mrmicky.fastinv.FastInv;
import fr.mrmicky.fastinv.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class DetailsGui extends FastInv {

    /**
     *      Affichage des détails de la crate
     */
    public DetailsGui(Crate crate, Player player) {
        super(54, MainCore.prefixCmd + "§8Box: " + crate.getName());
        for (int i=0; i<9; i++) {
            setItem(i, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).name(" ").build());
        }
        for (int i=18; i<54; i++) {
            setItem(i, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).name(" ").build());
        }
        ItemBuilder itemBuilder = new ItemBuilder(crate.getDisplayItem().clone());
        Profil profil = MainCore.profilHashMap.get(player.getUniqueId());
        itemBuilder.addLore("§7Disponibles: §b" + profil.getCrates().getOrDefault(crate.getId(),0));
        if (profil.getCrates().getOrDefault(crate.getId(),0)>0) {
            itemBuilder.addLore("§a§l[Click pour ouvrir]");
        }
        setItem(13, itemBuilder.build().clone());
        int loop = 27;
        for (ItemStack loopItem: crate.getItemsLucks().keySet()) {
            setItem(loop,loopItem);
            loop++;
        }
    }

    @Override
    protected void onClick(InventoryClickEvent event) {
        event.setCancelled(true);
    }
}
