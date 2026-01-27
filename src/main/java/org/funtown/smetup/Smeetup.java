package org.funtown.smetup;

import org.funtown.smetup.commands.SmeetupCommand;
import org.funtown.smetup.listeners.*;
import org.funtown.smetup.managers.*;
import org.funtown.smetup.placeholders.SmeetupPlaceholder;
import org.funtown.smetup.utils.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Smeetup extends JavaPlugin {

    private static Smeetup instance;
    private ConfigManager configManager;
    private GameManager gameManager;
    private ColbManager colbManager;
    private KitManager kitManager;
    private BorderManager borderManager;
    private MapManager mapManager;

    @Override
    public void onEnable() {
        instance = this;

        
        configManager = new ConfigManager(this);
        colbManager = new ColbManager(this);
        kitManager = new KitManager(this);
        mapManager = new MapManager(this);
        borderManager = new BorderManager(this);
        gameManager = new GameManager(this);

        
        getCommand("smeetup").setExecutor(new SmeetupCommand(this));

        
        registerListeners();

        
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new SmeetupPlaceholder(this).register();
            getLogger().info("PlaceholderAPI hooked!");
        }

        getLogger().info("Smeetup plugin enabled!");
    }

    @Override
    public void onDisable() {
        if (gameManager != null && gameManager.isGameRunning()) {
            gameManager.forceStop();
        }
        getLogger().info("Smeetup plugin disabled!");
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerQuitListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerDeathListener(this), this);
        getServer().getPluginManager().registerEvents(new GameProtectionListener(this), this);
        getServer().getPluginManager().registerEvents(new WandListener(this), this);
    }

    public static Smeetup getInstance() {
        return instance;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public GameManager getGameManager() {
        return gameManager;
    }

    public ColbManager getColbManager() {
        return colbManager;
    }

    public KitManager getKitManager() {
        return kitManager;
    }

    public BorderManager getBorderManager() {
        return borderManager;
    }

    public MapManager getMapManager() {
        return mapManager;
    }
}