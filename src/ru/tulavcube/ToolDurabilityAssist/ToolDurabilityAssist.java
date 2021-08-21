package ru.tulavcube.ToolDurabilityAssist;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.plugin.java.JavaPlugin;
import ru.tulavcube.ToolDurabilityAssist.commands.tooldurabilityassist;

public class ToolDurabilityAssist extends JavaPlugin implements Listener {

    private int _percent;

    @Override
    public void onEnable() {
        super.onEnable();
        getLogger().info("ToolDurabilityAssist plugin started");
        saveDefaultConfig();
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

        if (checkDurability(damagedItem, _percent)) return;

        String customName = damagedItem.getItemMeta().getDisplayName();
        String itemName;
        if (customName.isBlank()) {
            itemName = damagedItem.getType().toString().replace('_', ' ').toLowerCase();
            itemName = itemName.substring(0, 1).toUpperCase() + itemName.substring(1);
        } else itemName = customName;

        player.sendTitle(" ", itemName + " durability is low", 10, 60, 10);
        player.playSound(player.getLocation(), "block.chest.open", 1, 2F);

    }

    public void updateSettings() {
        _percent = getConfig().getInt("percentage");
    }

    private boolean checkDurability(ItemStack item, float percentDamage) {
        if (item != null && item.getItemMeta() instanceof Damageable) {
            int maxDurability = item.getType().getMaxDurability();
            int damage = ((Damageable) item.getItemMeta()).getDamage();
            return !((float) (maxDurability - damage) / maxDurability * 100 < percentDamage);
        }
        return true;
    }

}
