package fr.milekat.cite_box.chests;

import fr.milekat.cite_core.MainCore;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LootCratesTAB implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (command.getName().equalsIgnoreCase("crate")) {
            if (args.length<2) {
                return MainCore.getTabArgs(args[0],new ArrayList<>(Arrays.asList("add", "set")));
            } else if (args.length<3) {
                return MainCore.getPlayersList(args[1]);
            }
        }
        return null;
    }
}
