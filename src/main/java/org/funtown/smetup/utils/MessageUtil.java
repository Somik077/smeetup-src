package org.funtown.smetup.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.funtown.smetup.Smeetup;

public class MessageUtil {

    public static String colorize(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static void send(Player player, String path, String... replacements) {
        FileConfiguration messages = Smeetup.getInstance().getConfigManager().getConfig("messages");
        String prefix = messages.getString("prefix", "&8[&cSmeetup&8] &7");
        String message = messages.getString(path, "&cMessage not found: " + path);

        for (int i = 0; i < replacements.length; i += 2) {
            if (i + 1 < replacements.length) {
                message = message.replace(replacements[i], replacements[i + 1]);
            }
        }

        player.sendMessage(colorize(prefix + message));
    }

    public static void broadcast(String path, String... replacements) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            send(player, path, replacements);
        }
    }

    public static void playSound(Player player, String soundName) {
        try {
            Sound sound = Sound.valueOf(soundName);
            player.playSound(player.getLocation(), sound, 1.0f, 1.0f);
        } catch (IllegalArgumentException e) {
            Smeetup.getInstance().getLogger().warning("Invalid sound: " + soundName);
        }
    }

    public static void broadcastSound(String soundName) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            playSound(player, soundName);
        }
    }
}