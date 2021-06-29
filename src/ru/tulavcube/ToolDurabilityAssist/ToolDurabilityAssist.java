package ru.tulavcube.ToolDurabilityAssist;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import ru.tulavcube.ToolDurabilityAssist.commands.tooldurabilityassist;

import java.util.ArrayList;

public class ToolDurabilityAssist extends JavaPlugin {

    private int _percent;
    private final ArrayList<String> playerList = new ArrayList<>();
    private BukkitTask _runnable;

    @Override
    public void onEnable() {
        getLogger().info("ToolDurabilityAssist plugin started");
        saveDefaultConfig();
        updateSettings();

        getCommand("tooldurabilityassist").setExecutor(new tooldurabilityassist(this));
        getCommand("tooldurabilityassist").setTabCompleter(new tooldurabilityassist(this));

        _runnable = new BukkitRunnable(){
            @Override
            public void run() {
                for(Player player : getServer().getOnlinePlayers()){
                    ItemStack item = player.getInventory().getItemInMainHand();
                    int maxDurability= item.getType().getMaxDurability();
                    if(item.getItemMeta() instanceof Damageable) {
                        float durability = ((Damageable) item.getItemMeta()).getDamage();
                        if (((maxDurability - durability) / maxDurability * 100) < _percent) {
                            if(!playerList.contains(player.getName())) {
                                player.sendTitle("Tool durability low", null, 10, 60, 10);
                                player.playSound(player.getLocation(), "block.chest.open", 1, 2F);
                                playerList.add(player.getName());
                            }
                        }
                        else playerList.remove(player.getName());
                    }
                    else playerList.remove(player.getName());
                }
            }
        }.runTaskTimer(this, 0, 40);
    }

    @Override
    public void onDisable() {
        _runnable.cancel();
        getLogger().info("ToolDurabilityAssist plugin stopped");
    }

    public void updateSettings(){
        _percent = getConfig().getInt("percentage");
    }

}
