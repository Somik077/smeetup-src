package org.funtown.smetup.managers;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.funtown.smetup.Smeetup;
import org.funtown.smetup.models.GameMap;
import java.util.HashMap;
import java.util.Map;

public class MapManager {
    private final Smeetup plugin;
    private final Map<String, GameMap> maps;
    private GameMap currentMap;
    private Location pos1;
    private Location pos2;

    public MapManager(Smeetup plugin) {
        this.plugin = plugin;
        this.maps = new HashMap<>();
        loadMaps();
    }

    public void loadMaps() {
        maps.clear();
        FileConfiguration config = plugin.getConfigManager().getConfig("maps");

        if (config.contains("maps")) {
            ConfigurationSection mapsSection = config.getConfigurationSection("maps");
            if (mapsSection != null) {
                for (String mapName : mapsSection.getKeys(false)) {
                    String path = "maps." + mapName;
                    String worldName = config.getString(path + ".world");

                    GameMap map = new GameMap(mapName, worldName);

                    if (config.contains(path + ".center")) {
                        double x = config.getDouble(path + ".center.x");
                        double y = config.getDouble(path + ".center.y");
                        double z = config.getDouble(path + ".center.z");
                        World world = Bukkit.getWorld(worldName);
                        if (world != null) {
                            map.setCenter(new Location(world, x, y, z));
                        }
                    }

                    if (config.contains(path + ".region.pos1")) {
                        double x1 = config.getDouble(path + ".region.pos1.x");
                        double y1 = config.getDouble(path + ".region.pos1.y");
                        double z1 = config.getDouble(path + ".region.pos1.z");
                        World world = Bukkit.getWorld(worldName);
                        if (world != null) {
                            map.setPos1(new Location(world, x1, y1, z1));
                        }
                    }

                    if (config.contains(path + ".region.pos2")) {
                        double x2 = config.getDouble(path + ".region.pos2.x");
                        double y2 = config.getDouble(path + ".region.pos2.y");
                        double z2 = config.getDouble(path + ".region.pos2.z");
                        World world = Bukkit.getWorld(worldName);
                        if (world != null) {
                            map.setPos2(new Location(world, x2, y2, z2));
                        }
                    }

                    if (config.contains(path + ".lobby")) {
                        String lobbyWorld = config.getString(path + ".lobby.world");
                        double lx = config.getDouble(path + ".lobby.x");
                        double ly = config.getDouble(path + ".lobby.y");
                        double lz = config.getDouble(path + ".lobby.z");
                        World world = Bukkit.getWorld(lobbyWorld);
                        if (world != null) {
                            map.setLobby(new Location(world, lx, ly, lz));
                        }
                    }

                    maps.put(mapName.toLowerCase(), map);
                }
            }
        }

        if (!maps.isEmpty() && currentMap == null) {
            currentMap = maps.values().iterator().next();
        }
    }

    public void saveMaps() {
        FileConfiguration config = plugin.getConfigManager().getConfig("maps");
        config.set("maps", null);

        for (GameMap map : maps.values()) {
            String path = "maps." + map.getName();
            config.set(path + ".world", map.getWorldName());

            if (map.getCenter() != null) {
                Location center = map.getCenter();
                config.set(path + ".center.x", center.getX());
                config.set(path + ".center.y", center.getY());
                config.set(path + ".center.z", center.getZ());
            }

            if (map.getPos1() != null) {
                Location pos1 = map.getPos1();
                config.set(path + ".region.pos1.x", pos1.getX());
                config.set(path + ".region.pos1.y", pos1.getY());
                config.set(path + ".region.pos1.z", pos1.getZ());
            }

            if (map.getPos2() != null) {
                Location pos2 = map.getPos2();
                config.set(path + ".region.pos2.x", pos2.getX());
                config.set(path + ".region.pos2.y", pos2.getY());
                config.set(path + ".region.pos2.z", pos2.getZ());
            }

            if (map.getLobby() != null) {
                Location lobby = map.getLobby();
                config.set(path + ".lobby.world", lobby.getWorld().getName());
                config.set(path + ".lobby.x", lobby.getX());
                config.set(path + ".lobby.y", lobby.getY());
                config.set(path + ".lobby.z", lobby.getZ());
            }
        }

        plugin.getConfigManager().saveConfig("maps");
    }

    public GameMap createMap(String name) {
        if (pos1 == null || pos2 == null) return null;

        GameMap map = new GameMap(name, pos1.getWorld().getName());
        map.setPos1(pos1);
        map.setPos2(pos2);

        maps.put(name.toLowerCase(), map);

        
        currentMap = map;

        saveMaps();

        pos1 = null;
        pos2 = null;

        return map;
    }

    public void setPos1(Location location) {
        this.pos1 = location;
    }

    public void setPos2(Location location) {
        this.pos2 = location;
    }

    public Location getPos1() {
        return pos1;
    }

    public Location getPos2() {
        return pos2;
    }

    public GameMap getCurrentMap() {
        return currentMap;
    }

    public void setCurrentMap(String name) {
        GameMap map = maps.get(name.toLowerCase());
        if (map != null) {
            currentMap = map;
        }
    }

    public GameMap getMap(String name) {
        return maps.get(name.toLowerCase());
    }

    public Map<String, GameMap> getAllMaps() {
        return new HashMap<>(maps);
    }

    public void restoreMap(GameMap map) {
        plugin.getLogger().info("Map restoration is not implemented yet");
    }
}