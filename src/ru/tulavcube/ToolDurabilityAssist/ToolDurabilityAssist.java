package ru.tulavcube.ToolDurabilityAssist;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.plugin.java.JavaPlugin;
import ru.tulavcube.ToolDurabilityAssist.commands.tooldurabilityassist;

import java.io.File;

public class ToolDurabilityAssist extends JavaPlugin implements Listener {

    private int _percentWarning;
    private int _damageCritical;
    private int configVersion = 2;

    @Override
    public void onEnable() {
        super.onEnable();
        getLogger().info("ToolDurabilityAssist plugin started");

        checkConfigFile();
        updateSettings();

        getCommand("tooldurabilityassist").setExecutor(new tooldurabilityassist(this));
        getCommand("tooldurabilityassist").setTabCompleter(new tooldurabilityassist(this));

        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        getLogger().info("ToolDurabilityAssist plugin stopped");
    }

    @EventHandler
    private void onItemDamage(PlayerItemDamageEvent e) {
        Player player = e.getPlayer();
        ItemStack damagedItem = e.getItem();

        if (e.getDamage() == 0) return;
        if (!checkDurabilityAbs(damagedItem, _damageCritical)) {
            String customName = damagedItem.getItemMeta().getDisplayName();
            String itemName;
            if (customName.isBlank()) {
                itemName = damagedItem.getType().toString().replace('_', ' ').toLowerCase();
                itemName = itemName.substring(0, 1).toUpperCase() + itemName.substring(1);
            } else itemName = customName;

            player.sendTitle(" ", itemName + " durability is low", 10, 60, 10);
            player.playSound(player.getLocation(), "block.chest.open", 1, 2F);

            return;
        }
        if (checkDurabilityPercent(damagedItem, _percentWarning)) return;

        String customName = damagedItem.getItemMeta().getDisplayName();
        String itemName;
        if (customName.isBlank()) {
            itemName = damagedItem.getType().toString().replace('_', ' ').toLowerCase();
            itemName = itemName.substring(0, 1).toUpperCase() + itemName.substring(1);
        } else itemName = customName;

        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.
                fromLegacyText(itemName + " durability is low"));
    }

    private void checkConfigFile(){
        FileConfiguration config = getConfig();
        if(config == null) {
            saveDefaultConfig();
            return;
        }
        if(configVersion == config.getInt("config-version")){
            File configFile = new File(getDataFolder(), "config.yml");
            configFile.delete();
            saveDefaultConfig();
        }
    }

    public void updateSettings() {
        _percentWarning = getConfig().getInt("warning");
        _damageCritical = getConfig().getInt("critical");
    }

    private boolean checkDurabilityPercent(ItemStack item, float percentLeft) {
        if (item != null && item.getItemMeta() instanceof Damageable) {
            int maxDurability = item.getType().getMaxDurability();
            int damage = ((Damageable) item.getItemMeta()).getDamage();
            return !((float) (maxDurability - damage) / maxDurability * 100 < percentLeft);
        }
        return true;
    }

    private boolean checkDurabilityAbs(ItemStack item, int durabilityLeft) {
        if (item != null && item.getItemMeta() instanceof Damageable) {
            int maxDurability = item.getType().getMaxDurability();
            int damage = ((Damageable) item.getItemMeta()).getDamage();
            return !(maxDurability - damage < durabilityLeft);
        }
        return true;
    }

}
