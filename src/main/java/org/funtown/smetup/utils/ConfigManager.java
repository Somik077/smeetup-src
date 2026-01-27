package org.funtown.smetup.utils;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.funtown.smetup.Smeetup;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ConfigManager {
    private final Smeetup plugin;
    private final Map<String, FileConfiguration> configs;
    private final Map<String, File> configFiles;

    public ConfigManager(Smeetup plugin) {
        this.plugin = plugin;
        this.configs = new HashMap<>();
        this.configFiles = new HashMap<>();

        loadConfigs();
    }

    private void loadConfigs() {
        plugin.saveDefaultConfig();
        configs.put("config", plugin.getConfig());

        createConfig("colbs");
        createConfig("kits");
        createConfig("maps");
        createConfig("messages");

        setDefaults();
    }

    private void createConfig(String name) {
        File file = new File(plugin.getDataFolder(), name + ".yml");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().severe("Could not create " + name + ".yml");
            }
        }
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        configs.put(name, config);
        configFiles.put(name, file);
    }

    private void setDefaults() {
        FileConfiguration config = getConfig("config");
        config.addDefault("game.min-players", 2);
        config.addDefault("game.phases.preparation", 90);
        config.addDefault("game.phases.kit-distribution", 90);
        config.addDefault("game.join-timeout", 90);

        config.addDefault("border.initial-size", 500);
        config.addDefault("border.final-size", 10);
        config.addDefault("border.shrink-duration", 600);
        config.addDefault("border.shrink-delay", 0);
        config.addDefault("border.damage", 0.2);

        config.addDefault("colbs.size", 3);
        config.addDefault("colbs.height", 4);
        config.addDefault("colbs.material", "BEDROCK");

        config.addDefault("rules.clear-inventory", true);
        config.addDefault("rules.spectator-mode", true);
        config.addDefault("rules.keep-inventory", false);
        config.addDefault("rules.natural-regeneration", true);
        config.addDefault("rules.hunger", true);

        config.addDefault("rewards.enabled", true);
        config.addDefault("rewards.commands", new String[]{
                "give %player% diamond 64",
                "broadcast &6Игрок &e%player% &6победил в Meetup!"
        });

        config.addDefault("sounds.phase-change", "ENTITY_PLAYER_LEVELUP");
        config.addDefault("sounds.game-start", "ENTITY_ENDER_DRAGON_GROWL");
        config.addDefault("sounds.game-end", "UI_TOAST_CHALLENGE_COMPLETE");

        config.options().copyDefaults(true);
        saveConfig("config");

        FileConfiguration messages = getConfig("messages");
        messages.addDefault("prefix", "&8[&cSmeetup&8] &7");
        messages.addDefault("game.started", "&aИгра началась!");
        messages.addDefault("game.not-enough-players", "&cНедостаточно игроков! Нужно: %min%");
        messages.addDefault("game.already-running", "&cИгра уже идет!");
        messages.addDefault("game.force-stopped", "&cИгра принудительно остановлена!");

        messages.addDefault("phases.preparation", "&eФаза подготовки! Ожидайте...");
        messages.addDefault("phases.kit-distribution", "&aВыдача китов! Подготовьтесь!");
        messages.addDefault("phases.game", "&cИгра началась! Удачи!");

        messages.addDefault("player.teleported-to-colb", "&aВы в колбе!");
        messages.addDefault("player.no-colb-available", "&cУвы, вам не досталось колбы!");
        messages.addDefault("player.late-join", "&cВы не успели на игру!");
        messages.addDefault("player.kit-received", "&aВы получили кит: &e%kit%");
        messages.addDefault("player.victory", "&6&lПОБЕДА! &eВы выиграли!");

        messages.addDefault("border.shrinking", "&cБордер начинает сужаться!");

        messages.options().copyDefaults(true);
        saveConfig("messages");

        FileConfiguration kits = getConfig("kits");
        if (!kits.contains("kits.basic")) {
            kits.set("kits.basic.chance", 100);
            kits.set("kits.basic.receivers", -1);
            saveConfig("kits");
        }
    }

    public FileConfiguration getConfig(String name) {
        return configs.get(name);
    }

    public void saveConfig(String name) {
        try {
            if (name.equals("config")) {
                plugin.saveConfig();
            } else {
                FileConfiguration config = configs.get(name);
                File file = configFiles.get(name);
                if (config != null && file != null) {
                    config.save(file);
                }
            }
        } catch (IOException e) {
            plugin.getLogger().severe("Could not save " + name + ".yml");
        }
    }

    public void reloadConfigs() {
        plugin.reloadConfig();
        configs.put("config", plugin.getConfig());

        for (String name : configFiles.keySet()) {
            File file = configFiles.get(name);
            configs.put(name, YamlConfiguration.loadConfiguration(file));
        }
    }
}