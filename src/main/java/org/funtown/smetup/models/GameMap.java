package org.funtown.smetup.models;

import org.bukkit.Location;

public class GameMap {
    private final String name;
    private final String worldName;
    private Location center;
    private Location pos1;
    private Location pos2;
    private Location lobby;

    public GameMap(String name, String worldName) {
        this.name = name;
        this.worldName = worldName;
    }

    public String getName() {
        return name;
    }

    public String getWorldName() {
        return worldName;
    }

    public Location getCenter() {
        return center != null ? center.clone() : null;
    }

    public void setCenter(Location center) {
        this.center = center;
    }

    public Location getPos1() {
        return pos1 != null ? pos1.clone() : null;
    }

    public void setPos1(Location pos1) {
        this.pos1 = pos1;
    }

    public Location getPos2() {
        return pos2 != null ? pos2.clone() : null;
    }

    public void setPos2(Location pos2) {
        this.pos2 = pos2;
    }

    public Location getLobby() {
        return lobby != null ? lobby.clone() : null;
    }

    public void setLobby(Location lobby) {
        this.lobby = lobby;
    }

    public boolean hasRegion() {
        return pos1 != null && pos2 != null;
    }
}