package fr.milekat.cite_box.utils.GUI;

import fr.milekat.cite_box.MainBox;
import fr.milekat.cite_box.obj.Crate;
import fr.milekat.cite_core.MainCore;
import fr.milekat.cite_core.core.obj.Profil;
import fr.mrmicky.fastinv.FastInv;
import fr.mrmicky.fastinv.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.TimeUnit;

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
            setItem(13, itemBuilder.build().clone(), e -> onClickDelay(crate, (Player) e.getWhoClicked()));
        } else setItem(13, itemBuilder.build().clone());
        int loop = 27;
        for (ItemStack loopItem: crate.getItemsLucks().values()) {
            setItem(loop,loopItem);
            loop++;
        }
    }

    /**
     *      Attente de validation pour ouvrir la box
     */
    private void onClickDelay(Crate crate, Player player) {
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    setItem(13, new ItemBuilder(Material.BLUE_TERRACOTTA).name("§c§lAttente:3").build());
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT,1, (float) 0.5);
                    TimeUnit.MILLISECONDS.sleep(500);
                    setItem(13, new ItemBuilder(Material.LIGHT_BLUE_TERRACOTTA).name("§e§lAttente:2").build());
                    TimeUnit.MILLISECONDS.sleep(500);
                    setItem(13, new ItemBuilder(Material.LIGHT_BLUE_TERRACOTTA).name("§a§lAttente:1").build());
                    TimeUnit.MILLISECONDS.sleep(500);
                    player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP,1,1);
                    setItem(13, new ItemBuilder(Material.LIME_TERRACOTTA).name("§a§l[OUVRIR LA BOX]").build(), e ->
                            new CrateGUI().openCrate(crate, player));
                } catch (InterruptedException ignore) {}
            }
        }.runTaskAsynchronously(MainBox.getInstance());
    }

    @Override
    protected void onClick(InventoryClickEvent event) {
        event.setCancelled(true);
    }
}
