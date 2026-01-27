package org.funtown.smetup.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.funtown.smetup.Smeetup;
import org.funtown.smetup.models.GamePhase;

public class GameProtectionListener implements Listener {
    private final Smeetup plugin;

    public GameProtectionListener(Smeetup plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (!plugin.getGameManager().isGameRunning()) return;

        Player player = event.getPlayer();
        GamePhase phase = plugin.getGameManager().getCurrentPhase();

        
        if (phase == GamePhase.PREPARATION &&
                plugin.getGameManager().isPlayerAlive(player.getUniqueId())) {

            if (event.getFrom().getX() != event.getTo().getX() ||
                    event.getFrom().getZ() != event.getTo().getZ()) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (!plugin.getGameManager().isGameRunning()) return;

        GamePhase phase = plugin.getGameManager().getCurrentPhase();

        
        if (phase == GamePhase.KIT_DISTRIBUTION || phase == GamePhase.GAME) {
            return; 
        }

        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (!plugin.getGameManager().isGameRunning()) return;

        GamePhase phase = plugin.getGameManager().getCurrentPhase();

        
        if (phase == GamePhase.KIT_DISTRIBUTION || phase == GamePhase.GAME) {
            return; 
        }

        event.setCancelled(true);
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event) {
        if (!plugin.getGameManager().isGameRunning()) return;

        GamePhase phase = plugin.getGameManager().getCurrentPhase();

        
        if (phase == GamePhase.KIT_DISTRIBUTION || phase == GamePhase.GAME) {
            return; 
        }

        event.setCancelled(true);
    }

    
    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (!plugin.getGameManager().isGameRunning()) return;

        GamePhase phase = plugin.getGameManager().getCurrentPhase();

        
        if (phase == GamePhase.KIT_DISTRIBUTION || phase == GamePhase.GAME) {
            return; 
        }
    }
}