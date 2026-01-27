package org.funtown.smetup.listeners;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.funtown.smetup.Smeetup;
import org.funtown.smetup.utils.MessageUtil;

public class WandListener implements Listener {
    private final Smeetup plugin;

    public WandListener(Smeetup plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        
        if (item == null || item.getType() != Material.GOLDEN_AXE) return;
        if (!item.hasItemMeta() || !item.getItemMeta().hasDisplayName()) return;
        if (!item.getItemMeta().getDisplayName().contains("Smeetup Wand")) return;

        Block block = event.getClickedBlock();
        if (block == null) return;

        event.setCancelled(true);

        if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
            plugin.getMapManager().setPos1(block.getLocation());
            player.sendMessage(MessageUtil.colorize("&aТочка 1 установлена: " +
                    block.getX() + ", " + block.getY() + ", " + block.getZ()));
        } else if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            plugin.getMapManager().setPos2(block.getLocation());
            player.sendMessage(MessageUtil.colorize("&aТочка 2 установлена: " +
                    block.getX() + ", " + block.getY() + ", " + block.getZ()));
        }
    }
}