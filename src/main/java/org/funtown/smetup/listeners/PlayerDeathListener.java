package org.funtown.smetup.listeners;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.funtown.smetup.Smeetup;

public class PlayerDeathListener implements Listener {
    private final Smeetup plugin;

    public PlayerDeathListener(Smeetup plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        if (!plugin.getGameManager().isGameRunning()) return;

        FileConfiguration config = plugin.getConfigManager().getConfig("config");
        boolean keepInventory = config.getBoolean("rules.keep-inventory");

        if (!keepInventory) {
            event.setKeepInventory(false);
            event.getDrops().clear();
        } else {
            event.setKeepInventory(true);
        }

        plugin.getGameManager().onPlayerDeath(event.getEntity());
    }
}