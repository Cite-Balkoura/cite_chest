package fr.milekat.cite_chest.commands;

import fr.milekat.cite_chest.MainChest;
import fr.milekat.cite_core.MainCore;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class LootBoxCmd implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(MainCore.prefixConsole + "Commande à éffectuer en jeux.");
            return true;
        }
        if (args.length!=2) {
            sendHelp((Player) sender);
            return true;
        }
        if (args[1].length()<10) {
            sender.sendMessage(MainCore.prefixCmd + "§cMerci d'utiliser le format dd/mm/aaaa");
            return true;
        }
        if (!MainChest.lootbox.containsKey(args[1])) {
            Inventory box = Bukkit.createInventory(null,27,"Box du " + args[1]);
            MainChest.lootbox.put(args[1], box);
        }
        Inventory box = Bukkit.createInventory(null,27,"§c[EDIT] §rbox du " + args[1]);
        box.setContents(MainChest.lootbox.get(args[1]).getContents());
        ((Player) sender).openInventory(box);
        return true;
    }

    /**
     *      Envoie la liste d'help pour la commande /event au joueur qui exécute /event (Custom pour les modos)
     * @param player joueur qui exécute /event
     */
    private void sendHelp(Player player){
        player.sendMessage(MainCore.prefixCmd + "§6Loot Box help !");
        player.sendMessage("§6/box help: §rAffiche ces informations.");
        player.sendMessage("§6/box edit <Date>: §rÉdit/Ajoute la box de la date.");
    }
}
