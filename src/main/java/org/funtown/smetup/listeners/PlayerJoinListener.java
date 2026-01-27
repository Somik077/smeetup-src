package org.funtown.smetup.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.funtown.smetup.Smeetup;

public class PlayerJoinListener implements Listener {
    private final Smeetup plugin;

    public PlayerJoinListener(Smeetup plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        plugin.getGameManager().onPlayerJoin(event.getPlayer());
    }
}