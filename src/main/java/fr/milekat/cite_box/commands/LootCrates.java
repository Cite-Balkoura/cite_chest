package fr.milekat.cite_box.commands;

import fr.milekat.cite_box.MainBox;
import fr.milekat.cite_core.MainCore;
import fr.milekat.cite_core.core.obj.Profil;
import fr.milekat.cite_libs.MainLibs;
import fr.milekat.cite_libs.utils_tools.MojangNames;
import fr.milekat.cite_libs.utils_tools.Tools;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class LootCrates implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 4 && args[0].equalsIgnoreCase("add")) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    try {
                        Profil profil = MainCore.profilHashMap.get(UUID.fromString(MojangNames.getUuid(args[1])));
                        HashMap<Integer, Integer> crates = profil.getCrates();
                        crates.put(Integer.parseInt(args[2]), profil.getCrates()
                                .getOrDefault(Integer.parseInt(args[2]), 0) + Integer.parseInt(args[3]));
                        profil.setCrates(crates);
                        updateSQLCratesofPlayer(profil);
                        sender.sendMessage(MainCore.prefixCmd + "§6Box ajoutée(s).");
                    } catch (NullPointerException | FileNotFoundException exception) {
                        sender.sendMessage(MainCore.prefixCmd +
                                "Erreur lors de l'ajout de(s) crate(s) pour: " + args[1] + " raison: " + exception);
                        Bukkit.getLogger().warning(MainBox.prefixConsole + "");
                    }
                }
            }.runTaskAsynchronously(MainBox.getInstance());
        } else if (args.length == 4 && args[0].equalsIgnoreCase("set")) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    try {
                        Profil profil = MainCore.profilHashMap.get(UUID.fromString(MojangNames.getUuid(args[1])));
                        HashMap<Integer, Integer> crates = profil.getCrates();
                        crates.put(Integer.parseInt(args[2]), Integer.parseInt(args[3]));
                        profil.setCrates(crates);
                        updateSQLCratesofPlayer(profil);
                        sender.sendMessage(MainCore.prefixCmd + "§6Box mises à jour.");
                    } catch (NullPointerException | FileNotFoundException exception) {
                        sender.sendMessage(MainCore.prefixCmd +
                                "Erreur de set crate pour: " + args[1] + " raison: " + exception);
                        Bukkit.getLogger().warning(MainBox.prefixConsole + "");
                    }
                }
            }.runTaskAsynchronously(MainBox.getInstance());
        } else if (!(sender instanceof Player)) {
            sender.sendMessage(MainLibs.prefixConsole + "Commande à éffectuer en jeux.");
            return true;
        }
        return true;
    }

    /**
     *      Update côté SQL d'un joueur après modification de sa HashMap
     */
    private void updateSQLCratesofPlayer(Profil profil) {
        try {
            Connection connection = MainCore.getSQL().getConnection();
            PreparedStatement q = connection.prepareStatement("UPDATE `balkoura_player` SET `crates`=? WHERE `uuid` = ?;");
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
}
