package org.funtown.smetup.models;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

public class Colb {
    private final int id;
    private final Location location;
    private boolean occupied;

    public Colb(int id, Location location) {
        this.id = id;
        this.location = location;
        this.occupied = false;
    }

    public int getId() {
        return id;
    }

    public Location getLocation() {
        return location.clone();
    }

    
    public Location getCenterLocation() {
        Location center = location.clone();
        center.setX(location.getBlockX() + 1.5);
        center.setY(location.getBlockY() + 2.0);  
        center.setZ(location.getBlockZ() + 1.5);
        center.setPitch(0);
        center.setYaw(0);
        return center;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }

    public void build(Material material, int size, int height) {
        World world = location.getWorld();
        if (world == null) return;

        int startX = location.getBlockX();
        int startY = location.getBlockY();
        int startZ = location.getBlockZ();

        
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < height; y++) {
                for (int z = 0; z < size; z++) {
                    world.getBlockAt(startX + x, startY + y, startZ + z).setType(Material.AIR);
                }
            }
        }

        
        for (int x = 0; x < size; x++) {
            for (int z = 0; z < size; z++) {
                world.getBlockAt(startX + x, startY, startZ + z).setType(material);
            }
        }

        
        for (int x = 0; x < size; x++) {
            for (int z = 0; z < size; z++) {
                world.getBlockAt(startX + x, startY + height - 1, startZ + z).setType(material);
            }
        }

        
        for (int y = 1; y < height - 1; y++) {
            for (int x = 0; x < size; x++) {
                world.getBlockAt(startX + x, startY + y, startZ).setType(material);
                world.getBlockAt(startX + x, startY + y, startZ + size - 1).setType(material);
            }
            for (int z = 0; z < size; z++) {
                world.getBlockAt(startX, startY + y, startZ + z).setType(material);
                world.getBlockAt(startX + size - 1, startY + y, startZ + z).setType(material);
            }
        }
    }

    
    public void remove() {
        World world = location.getWorld();
        if (world == null) return;

        int startX = location.getBlockX();
        int startY = location.getBlockY();
        int startZ = location.getBlockZ();

        
        int size = 3;
        int height = 4;

        
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < height; y++) {
                for (int z = 0; z < size; z++) {
                    world.getBlockAt(startX + x, startY + y, startZ + z).setType(Material.AIR);
                }
            }
        }
    }

    
    public void remove(int size, int height) {
        World world = location.getWorld();
        if (world == null) return;

        int startX = location.getBlockX();
        int startY = location.getBlockY();
        int startZ = location.getBlockZ();

        for (int x = 0; x < size; x++) {
            for (int y = 0; y < height; y++) {
                for (int z = 0; z < size; z++) {
                    world.getBlockAt(startX + x, startY + y, startZ + z).setType(Material.AIR);
                }
            }
        }
    }
}