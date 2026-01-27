package org.funtown.smetup.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.funtown.smetup.Smeetup;

public class PlayerQuitListener implements Listener {
    private final Smeetup plugin;

    public PlayerQuitListener(Smeetup plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        plugin.getGameManager().onPlayerQuit(event.getPlayer());
    }
}