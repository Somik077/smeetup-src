package org.funtown.smetup.managers;

import org.funtown.smetup.Smeetup;
import org.funtown.smetup.models.GameMap;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldBorder;

public class BorderManager {
    private final Smeetup plugin;
    private WorldBorder worldBorder;

    public BorderManager(Smeetup plugin) {
        this.plugin = plugin;
    }

    public void startShrinking() {
        GameMap map = plugin.getMapManager().getCurrentMap();
        if (map == null || map.getCenter() == null) return;

        Location center = map.getCenter();
        World world = center.getWorld();
        if (world == null) return;

        worldBorder = world.getWorldBorder();

        
        int initialSize = plugin.getConfigManager().getConfig("config").getInt("border.initial-size");
        int finalSize = plugin.getConfigManager().getConfig("config").getInt("border.final-size");
        int duration = plugin.getConfigManager().getConfig("config").getInt("border.shrink-duration");
        double damage = plugin.getConfigManager().getConfig("config").getDouble("border.damage");

        
        worldBorder.setCenter(center);
        worldBorder.setSize(initialSize);
        worldBorder.setDamageAmount(damage);
        worldBorder.setDamageBuffer(0);
        worldBorder.setWarningDistance(10);

        
        worldBorder.setSize(finalSize, duration);
    }

    public void resetBorder() {
        if (worldBorder != null) {
            worldBorder.setSize(59999968);
            worldBorder.setCenter(0, 0);
            worldBorder = null;
        }
    }

    public double getCurrentSize() {
        return worldBorder != null ? worldBorder.getSize() : 0;
    }

    public boolean isShrinking() {
        return worldBorder != null;
    }
}