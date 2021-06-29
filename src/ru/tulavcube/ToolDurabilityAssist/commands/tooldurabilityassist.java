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
        } else {
            if (args.length == 3 && args[0].equalsIgnoreCase("percentage") && args[1].equalsIgnoreCase("set")) {
                _plg.getConfig().set("percentage", Integer.parseInt(args[2]));
                _plg.saveConfig();
                _plg.updateSettings();
                sender.sendMessage(ChatColor.DARK_GREEN + "Saved!");
            } else if (args.length == 2 && args[0].equalsIgnoreCase("percentage") && args[1].equalsIgnoreCase("get")) {
                sender.sendMessage(ChatColor.DARK_GREEN + "Current durability threshold is set to "
                        + _plg.getConfig().getInt("percentage") + "%");
            } else {
                _plg.getLogger().info(Integer.toString(args.length));
                return false;
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player && !sender.isOp()) {
            return new ArrayList<>();
        } else {
            ArrayList<String> arr = new ArrayList<>();

            if (args.length == 1) {
                arr.add("percentage");
            } else if (args.length == 2) {
                arr.add("get");
                arr.add("set");
            }
            return arr;
        }
    }
}
