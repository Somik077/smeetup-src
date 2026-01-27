package org.funtown.smetup.managers;

import org.funtown.smetup.Smeetup;
import org.funtown.smetup.models.Kit;
import org.funtown.smetup.utils.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class KitManager {
    private final Smeetup plugin;
    private final Map<String, Kit> kits;

    public KitManager(Smeetup plugin) {
        this.plugin = plugin;
        this.kits = new HashMap<>();
        loadKits();
    }

    public void loadKits() {
        kits.clear();
        FileConfiguration config = plugin.getConfigManager().getConfig("kits");

        if (config.contains("kits")) {
            ConfigurationSection kitsSection = config.getConfigurationSection("kits");
            if (kitsSection != null) {
                for (String kitName : kitsSection.getKeys(false)) {
                    Kit kit = new Kit(kitName);
                    String path = "kits." + kitName;

                    
                    kit.setChance(config.getInt(path + ".chance", 100));
                    kit.setReceivers(config.getInt(path + ".receivers", -1));

                    
                    if (config.contains(path + ".items")) {
                        List<String> items = config.getStringList(path + ".items");
                        for (String itemString : items) {
                            ItemStack item = parseItem(itemString);
                            if (item != null) {
                                kit.addItem(item);
                            }
                        }
                    }

                    
                    if (config.contains(path + ".armor.helmet")) {
                        kit.setHelmet(parseItem(config.getString(path + ".armor.helmet")));
                    }
                    if (config.contains(path + ".armor.chestplate")) {
                        kit.setChestplate(parseItem(config.getString(path + ".armor.chestplate")));
                    }
                    if (config.contains(path + ".armor.leggings")) {
                        kit.setLeggings(parseItem(config.getString(path + ".armor.leggings")));
                    }
                    if (config.contains(path + ".armor.boots")) {
                        kit.setBoots(parseItem(config.getString(path + ".armor.boots")));
                    }

                    kits.put(kitName.toLowerCase(), kit);
                }
            }
        }
    }

    public void saveKits() {
        FileConfiguration config = plugin.getConfigManager().getConfig("kits");
        config.set("kits", null);

        for (Kit kit : kits.values()) {
            String path = "kits." + kit.getName();
            config.set(path + ".chance", kit.getChance());
            config.set(path + ".receivers", kit.getReceivers());

            
            List<String> items = new ArrayList<>();
            for (ItemStack item : kit.getItems()) {
                items.add(itemToString(item));
            }
            config.set(path + ".items", items);

            
            if (kit.getHelmet() != null) {
                config.set(path + ".armor.helmet", itemToString(kit.getHelmet()));
            }
            if (kit.getChestplate() != null) {
                config.set(path + ".armor.chestplate", itemToString(kit.getChestplate()));
            }
            if (kit.getLeggings() != null) {
                config.set(path + ".armor.leggings", itemToString(kit.getLeggings()));
            }
            if (kit.getBoots() != null) {
                config.set(path + ".armor.boots", itemToString(kit.getBoots()));
            }
        }

        plugin.getConfigManager().saveConfig("kits");
    }

    public Kit createKit(String name, Player player) {
        Kit kit = new Kit(name);

        
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null && !item.getType().isAir()) {
                kit.addItem(item);
            }
        }

        
        kit.setHelmet(player.getInventory().getHelmet());
        kit.setChestplate(player.getInventory().getChestplate());
        kit.setLeggings(player.getInventory().getLeggings());
        kit.setBoots(player.getInventory().getBoots());

        kits.put(name.toLowerCase(), kit);
        saveKits();
        return kit;
    }

    public boolean deleteKit(String name) {
        Kit removed = kits.remove(name.toLowerCase());
        if (removed != null) {
            saveKits();
            return true;
        }
        return false;
    }

    public Kit getKit(String name) {
        return kits.get(name.toLowerCase());
    }

    public Collection<Kit> getAllKits() {
        return kits.values();
    }

    public void giveKit(Player player, Kit kit) {
        
        for (ItemStack item : kit.getItems()) {
            player.getInventory().addItem(item);
        }

        
        if (kit.getHelmet() != null) {
            player.getInventory().setHelmet(kit.getHelmet());
        }
        if (kit.getChestplate() != null) {
            player.getInventory().setChestplate(kit.getChestplate());
        }
        if (kit.getLeggings() != null) {
            player.getInventory().setLeggings(kit.getLeggings());
        }
        if (kit.getBoots() != null) {
            player.getInventory().setBoots(kit.getBoots());
        }

        MessageUtil.send(player, "player.kit-received", "%kit%", kit.getName());
    }

    public void distributeKits(List<UUID> playerUUIDs) {
        List<Player> players = new ArrayList<>();
        for (UUID uuid : playerUUIDs) {
            Player player = Bukkit.getPlayer(uuid);
            if (player != null) {
                players.add(player);
            }
        }

        
        Kit basicKit = getKit("basic");
        if (basicKit != null) {
            for (Player player : players) {
                giveKit(player, basicKit);
            }
        }

        
        List<Kit> additionalKits = new ArrayList<>();
        for (Kit kit : kits.values()) {
            if (!kit.isBasic()) {
                additionalKits.add(kit);
            }
        }

        for (Kit kit : additionalKits) {
            
            Random random = new Random();
            if (random.nextInt(100) >= kit.getChance()) {
                continue;
            }

            
            int receivers = kit.getReceivers();
            if (receivers == -1) {
                receivers = players.size();
            }
            receivers = Math.min(receivers, players.size());

            
            List<Player> shuffled = new ArrayList<>(players);
            Collections.shuffle(shuffled);

            for (int i = 0; i < receivers; i++) {
                giveKit(shuffled.get(i), kit);
            }
        }
    }

    private ItemStack parseItem(String itemString) {
        if (itemString == null || itemString.isEmpty()) return null;

        String[] parts = itemString.split(" ");
        if (parts.length < 1) return null;

        try {
            Material material = Material.valueOf(parts[0].toUpperCase());
            int amount = parts.length > 1 ? Integer.parseInt(parts[1]) : 1;
            ItemStack item = new ItemStack(material, amount);

            
            if (parts.length > 2) {
                String[] enchants = parts[2].split(",");
                for (String enchant : enchants) {
                    String[] enchantParts = enchant.split(":");
                    if (enchantParts.length == 2) {
                        Enchantment ench = Enchantment.getByName(enchantParts[0].toUpperCase());
                        int level = Integer.parseInt(enchantParts[1]);
                        if (ench != null) {
                            item.addUnsafeEnchantment(ench, level);
                        }
                    }
                }
            }

            return item;
        } catch (Exception e) {
            plugin.getLogger().warning("Failed to parse item: " + itemString);
            return null;
        }
    }

    private String itemToString(ItemStack item) {
        if (item == null || item.getType().isAir()) return "";

        StringBuilder sb = new StringBuilder();
        sb.append(item.getType().name()).append(" ").append(item.getAmount());

        if (item.hasItemMeta() && item.getItemMeta().hasEnchants()) {
            sb.append(" ");
            List<String> enchants = new ArrayList<>();
            for (Map.Entry<Enchantment, Integer> entry : item.getItemMeta().getEnchants().entrySet()) {
                enchants.add(entry.getKey().getKey().getKey() + ":" + entry.getValue());
            }
            sb.append(String.join(",", enchants));
        }

        return sb.toString();
    }
}