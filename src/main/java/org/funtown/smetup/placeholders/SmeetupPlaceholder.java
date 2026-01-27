package org.funtown.smetup.placeholders;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.funtown.smetup.Smeetup;
import org.jetbrains.annotations.NotNull;

public class SmeetupPlaceholder extends PlaceholderExpansion {
    private final Smeetup plugin;

    public SmeetupPlaceholder(Smeetup plugin) {
        this.plugin = plugin;
    }

    @Override
    @NotNull
    public String getIdentifier() {
        return "meetup";
    }

    @Override
    @NotNull
    public String getAuthor() {
        return "Funtown";
    }

    @Override
    @NotNull
    public String getVersion() {
        return "1.0.0";
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String params) {
        if (params.equals("phase")) {
            return plugin.getGameManager().getCurrentPhase().getDisplayName();
        }

        if (params.equals("next_phase")) {
            return plugin.getGameManager().getCurrentPhase().getNext().getDisplayName();
        }

        if (params.equals("phasetime")) {
            int timeLeft = plugin.getGameManager().getPhaseTimeLeft();
            if (timeLeft < 0) return "∞";

            int minutes = timeLeft / 60;
            int seconds = timeLeft % 60;
            return String.format("%d:%02d", minutes, seconds);
        }

        if (params.equals("players")) {
            return String.valueOf(plugin.getGameManager().getAlivePlayers());
        }

        if (params.equals("players_total")) {
            return String.valueOf(plugin.getGameManager().getAlivePlayers());
        }

        if (params.equals("border_size")) {
            double size = plugin.getBorderManager().getCurrentSize();
            return String.format("%.1f", size);
        }

        if (params.equals("winner")) {
            return plugin.getGameManager().getLastWinner();
        }

        if (params.equals("status")) {
            return plugin.getGameManager().isGameRunning() ? "Активна" : "Неактивна";
        }

        return null;
    }
}