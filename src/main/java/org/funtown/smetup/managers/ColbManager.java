package org.funtown.smetup.managers;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.funtown.smetup.Smeetup;
import org.funtown.smetup.models.Colb;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ColbManager {
    private final Smeetup plugin;
    private final Map<Integer, Colb> colbs;
    private int nextColbId;

    public ColbManager(Smeetup plugin) {
        this.plugin = plugin;
        this.colbs = new HashMap<>();
        this.nextColbId = 1;
        loadColbs();
    }

    public void loadColbs() {
        colbs.clear();
        FileConfiguration config = plugin.getConfigManager().getConfig("colbs");

        if (config.contains("colbs")) {
            ConfigurationSection colbsSection = config.getConfigurationSection("colbs");
            if (colbsSection != null) {
                for (String key : colbsSection.getKeys(false)) {
                    int id = Integer.parseInt(key);
                    String worldName = config.getString("colbs." + key + ".world");
                    double x = config.getDouble("colbs." + key + ".x");
                    double y = config.getDouble("colbs." + key + ".y");
                    double z = config.getDouble("colbs." + key + ".z");

                    World world = Bukkit.getWorld(worldName);
                    if (world != null) {
                        Location location = new Location(world, x, y, z);
                        colbs.put(id, new Colb(id, location));
                        if (id >= nextColbId) {
                            nextColbId = id + 1;
                        }
                    }
                }
            }
        }
    }

    public void saveColbs() {
        FileConfiguration config = plugin.getConfigManager().getConfig("colbs");
        config.set("colbs", null);

        for (Colb colb : colbs.values()) {
            Location loc = colb.getLocation();
            String path = "colbs." + colb.getId();
            config.set(path + ".world", loc.getWorld().getName());
            config.set(path + ".x", loc.getBlockX());
            config.set(path + ".y", loc.getBlockY());
            config.set(path + ".z", loc.getBlockZ());
        }

        plugin.getConfigManager().saveConfig("colbs");
    }

    public Colb addColb(Location location) {
        Colb colb = new Colb(nextColbId++, location);
        colbs.put(colb.getId(), colb);
        saveColbs();
        return colb;
    }

    public boolean removeColb(int id) {
        Colb colb = colbs.remove(id);
        if (colb != null) {
            saveColbs();
            return true;
        }
        return false;
    }

    public Colb getColb(int id) {
        return colbs.get(id);
    }

    public List<Colb> getColbs() {
        return new ArrayList<>(colbs.values());
    }

    public List<Colb> getFreeColbs() {
        return colbs.values().stream()
                .filter(colb -> !colb.isOccupied())
                .collect(Collectors.toList());
    }

    public void buildAllColbs() {
        Material material = Material.valueOf(
                plugin.getConfigManager().getConfig("config").getString("colbs.material", "BEDROCK")
        );
        int size = plugin.getConfigManager().getConfig("config").getInt("colbs.size", 3);
        int height = plugin.getConfigManager().getConfig("config").getInt("colbs.height", 4);

        for (Colb colb : colbs.values()) {
            colb.build(material, size, height);
        }
    }

    
    public void removeAllColbs() {
        int size = plugin.getConfigManager().getConfig("config").getInt("colbs.size", 3);
        int height = plugin.getConfigManager().getConfig("config").getInt("colbs.height", 4);

        for (Colb colb : colbs.values()) {
            colb.remove(size, height);
            colb.setOccupied(false);
        }
    }

    public int getColbCount() {
        return colbs.size();
    }
}