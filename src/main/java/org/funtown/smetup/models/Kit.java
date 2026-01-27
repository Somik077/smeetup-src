package org.funtown.smetup.models;

import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class Kit {
    private final String name;
    private final List<ItemStack> items;
    private ItemStack helmet;
    private ItemStack chestplate;
    private ItemStack leggings;
    private ItemStack boots;
    private int chance;
    private int receivers;

    public Kit(String name) {
        this.name = name;
        this.items = new ArrayList<>();
        this.chance = 100;
        this.receivers = -1;
    }

    public String getName() {
        return name;
    }

    public List<ItemStack> getItems() {
        return new ArrayList<>(items);
    }

    public void addItem(ItemStack item) {
        if (item != null && !item.getType().isAir()) {
            items.add(item.clone());
        }
    }

    public void setItems(List<ItemStack> items) {
        this.items.clear();
        items.forEach(this::addItem);
    }

    public ItemStack getHelmet() {
        return helmet != null ? helmet.clone() : null;
    }

    public void setHelmet(ItemStack helmet) {
        this.helmet = helmet != null ? helmet.clone() : null;
    }

    public ItemStack getChestplate() {
        return chestplate != null ? chestplate.clone() : null;
    }

    public void setChestplate(ItemStack chestplate) {
        this.chestplate = chestplate != null ? chestplate.clone() : null;
    }

    public ItemStack getLeggings() {
        return leggings != null ? leggings.clone() : null;
    }

    public void setLeggings(ItemStack leggings) {
        this.leggings = leggings != null ? leggings.clone() : null;
    }

    public ItemStack getBoots() {
        return boots != null ? boots.clone() : null;
    }

    public void setBoots(ItemStack boots) {
        this.boots = boots != null ? boots.clone() : null;
    }

    public int getChance() {
        return chance;
    }

    public void setChance(int chance) {
        this.chance = Math.max(0, Math.min(100, chance));
    }

    public int getReceivers() {
        return receivers;
    }

    public void setReceivers(int receivers) {
        this.receivers = receivers;
    }

    public boolean isBasic() {
        return name.equalsIgnoreCase("basic");
    }
}