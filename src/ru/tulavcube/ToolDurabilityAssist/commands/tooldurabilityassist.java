package ru.tulavcube.ToolDurabilityAssist.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import ru.tulavcube.ToolDurabilityAssist.ToolDurabilityAssist;

import java.util.ArrayList;
import java.util.List;

public class tooldurabilityassist implements TabExecutor {

    private final ToolDurabilityAssist _plg;

    public tooldurabilityassist(ToolDurabilityAssist plg) {
        _plg = plg;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player && !sender.isOp()) {
            sender.sendMessage(ChatColor.RED + "You need to be an operator to run this command");
        } else if (args.length == 3) {
            if (args[1].equalsIgnoreCase("set")) {
                if (args[0].equalsIgnoreCase("warningPercentage")) {
                    _plg.getConfig().set("warning", Integer.parseInt(args[2]));
                    _plg.saveConfig();
                    _plg.updateSettings();
                    sender.sendMessage(ChatColor.DARK_GREEN + "Saved!");
                    return true;
                } else if (args[0].equalsIgnoreCase("criticalLevel")) {
                    _plg.getConfig().set("critical", Integer.parseInt(args[2]));
                    _plg.saveConfig();
                    _plg.updateSettings();
                    sender.sendMessage(ChatColor.DARK_GREEN + "Saved!");
                    return true;
                } else return false;
            }
        } else if (args.length == 2) {
            if (args[1].equalsIgnoreCase("get")) {
                if (args[0].equalsIgnoreCase("warningPercentage")) {
                    sender.sendMessage(ChatColor.DARK_GREEN + "Warning is set to " + _plg.getConfig().getInt("warning") + "%");
                    return true;
                } else if (args[0].equalsIgnoreCase("criticalLevel")) {
                    sender.sendMessage(ChatColor.DARK_GREEN + "Critical level is set to " + _plg.getConfig().getInt("critical"));
                    return true;
                } else return false;
            } else return false;
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player && !sender.isOp()) {
            return new ArrayList<>();
        } else {
            ArrayList<String> arr = new ArrayList<>();

            if (args.length == 1) {
                arr.add("warningPercentage");
                arr.add("criticalLevel");
            } else if (args.length == 2) {
                arr.add("get");
                arr.add("set");
            }
            return arr;
        }
    }
}
