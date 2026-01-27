package org.funtown.smetup.managers;

import org.funtown.smetup.Smeetup;
import org.funtown.smetup.models.Colb;
import org.funtown.smetup.models.GamePhase;
import org.funtown.smetup.utils.MessageUtil;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class GameManager {
    private final Smeetup plugin;
    private GamePhase currentPhase;
    private boolean gameRunning;
    private int phaseTimeLeft;
    private BukkitTask phaseTask;
    private final Set<UUID> alivePlayers;
    private final Set<UUID> spectators;
    private String lastWinner;

    public GameManager(Smeetup plugin) {
        this.plugin = plugin;
        this.currentPhase = GamePhase.WAITING;
        this.gameRunning = false;
        this.alivePlayers = new HashSet<>();
        this.spectators = new HashSet<>();
    }

    public boolean startGame() {
        if (gameRunning) {
            return false;
        }

        int minPlayers = plugin.getConfigManager().getConfig("config").getInt("game.min-players");
        if (Bukkit.getOnlinePlayers().size() < minPlayers) {
            return false;
        }

        gameRunning = true;
        alivePlayers.clear();
        spectators.clear();

        
        teleportPlayersToColbs();

        
        setPhase(GamePhase.PREPARATION);

        return true;
    }

    public void forceStop() {
        if (!gameRunning) return;

        gameRunning = false;
        if (phaseTask != null) {
            phaseTask.cancel();
            phaseTask = null;
        }

        
        Location lobby = plugin.getMapManager().getCurrentMap().getLobby();
        if (lobby != null) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.teleport(lobby);
                player.setGameMode(GameMode.SURVIVAL);
            }
        }

        
        plugin.getColbManager().removeAllColbs();

        
        plugin.getBorderManager().resetBorder();

        currentPhase = GamePhase.WAITING;
        alivePlayers.clear();
        spectators.clear();

        MessageUtil.broadcast("game.force-stopped");
    }

    private void teleportPlayersToColbs() {
        List<Colb> availableColbs = new ArrayList<>(plugin.getColbManager().getColbs());
        List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());

        
        if (plugin.getConfigManager().getConfig("config").getBoolean("rules.clear-inventory")) {
            for (Player player : players) {
                player.getInventory().clear();
                player.setHealth(20);
                player.setFoodLevel(20);
                player.setGameMode(GameMode.SURVIVAL);
            }
        }

        int colbIndex = 0;
        for (Player player : players) {
            if (colbIndex >= availableColbs.size()) {
                MessageUtil.send(player, "player.no-colb-available");
                continue;
            }

            Colb colb = availableColbs.get(colbIndex);
            player.teleport(colb.getCenterLocation());
            colb.setOccupied(true);
            alivePlayers.add(player.getUniqueId());

            MessageUtil.send(player, "player.teleported-to-colb");
            colbIndex++;
        }

        
        plugin.getColbManager().buildAllColbs();
    }

    public void setPhase(GamePhase phase) {
        currentPhase = phase;

        if (phaseTask != null) {
            phaseTask.cancel();
        }

        switch (phase) {
            case PREPARATION:
                handlePreparationPhase();
                break;
            case KIT_DISTRIBUTION:
                handleKitDistributionPhase();
                break;
            case GAME:
                handleGamePhase();
                break;
            case ENDING:
                handleEndingPhase();
                break;
        }
    }

    private void handlePreparationPhase() {
        phaseTimeLeft = plugin.getConfigManager().getConfig("config").getInt("game.phases.preparation");
        MessageUtil.broadcast("phases.preparation");

        
        for (UUID uuid : alivePlayers) {
            Player player = Bukkit.getPlayer(uuid);
            if (player != null) {
                player.setWalkSpeed(0);
                player.setFlySpeed(0);
            }
        }

        startPhaseTimer(() -> setPhase(GamePhase.KIT_DISTRIBUTION));
    }

    private void handleKitDistributionPhase() {
        phaseTimeLeft = plugin.getConfigManager().getConfig("config").getInt("game.phases.kit-distribution");
        MessageUtil.broadcast("phases.kit-distribution");

        
        for (UUID uuid : alivePlayers) {
            Player player = Bukkit.getPlayer(uuid);
            if (player != null) {
                player.setWalkSpeed(0.2f);
                player.setFlySpeed(0.1f);
            }
        }

        
        plugin.getKitManager().distributeKits(new ArrayList<>(alivePlayers));

        startPhaseTimer(() -> setPhase(GamePhase.GAME));
    }

    private void handleGamePhase() {
        MessageUtil.broadcast("phases.game");

        
        plugin.getColbManager().removeAllColbs();

        
        Location center = plugin.getMapManager().getCurrentMap().getCenter();
        if (center != null) {
            for (UUID uuid : alivePlayers) {
                Player player = Bukkit.getPlayer(uuid);
                if (player != null) {
                    player.teleport(center);
                }
            }
        }

        
        int delay = plugin.getConfigManager().getConfig("config").getInt("border.shrink-delay");
        new BukkitRunnable() {
            @Override
            public void run() {
                if (gameRunning) {
                    plugin.getBorderManager().startShrinking();
                    MessageUtil.broadcast("border.shrinking");
                }
            }
        }.runTaskLater(plugin, delay * 20L);

        phaseTimeLeft = -1; 
    }

    private void handleEndingPhase() {
        if (phaseTask != null) {
            phaseTask.cancel();
        }

        
        if (alivePlayers.size() == 1) {
            UUID winnerUUID = alivePlayers.iterator().next();
            Player winner = Bukkit.getPlayer(winnerUUID);
            if (winner != null) {
                lastWinner = winner.getName();
                MessageUtil.send(winner, "player.victory");

                
                if (plugin.getConfigManager().getConfig("config").getBoolean("rewards.enabled")) {
                    List<String> commands = plugin.getConfigManager().getConfig("config").getStringList("rewards.commands");
                    for (String cmd : commands) {
                        String finalCmd = cmd.replace("%player%", winner.getName());
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), finalCmd);
                    }
                }
            }
        }

        
        new BukkitRunnable() {
            @Override
            public void run() {
                forceStop();
            }
        }.runTaskLater(plugin, 200L);
    }

    private void startPhaseTimer(Runnable onComplete) {
        phaseTask = new BukkitRunnable() {
            @Override
            public void run() {
                if (phaseTimeLeft <= 0) {
                    cancel();
                    onComplete.run();
                    return;
                }
                phaseTimeLeft--;
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }

    public void onPlayerDeath(Player player) {
        if (!gameRunning || !alivePlayers.contains(player.getUniqueId())) return;

        alivePlayers.remove(player.getUniqueId());

        if (plugin.getConfigManager().getConfig("config").getBoolean("rules.spectator-mode")) {
            spectators.add(player.getUniqueId());
            player.setGameMode(GameMode.SPECTATOR);
        }

        
        if (alivePlayers.size() <= 1) {
            setPhase(GamePhase.ENDING);
        }
    }

    public void onPlayerJoin(Player player) {
        if (!gameRunning) return;

        if (currentPhase == GamePhase.PREPARATION) {
            int timeout = plugin.getConfigManager().getConfig("config").getInt("game.join-timeout");
            if (phaseTimeLeft > timeout - phaseTimeLeft) {
                
                List<Colb> freeColbs = plugin.getColbManager().getFreeColbs();
                if (!freeColbs.isEmpty()) {
                    Colb colb = freeColbs.get(0);
                    player.teleport(colb.getCenterLocation());
                    colb.setOccupied(true);
                    alivePlayers.add(player.getUniqueId());
                    player.setWalkSpeed(0);
                    MessageUtil.send(player, "player.teleported-to-colb");
                } else {
                    MessageUtil.send(player, "player.no-colb-available");
                }
            } else {
                MessageUtil.send(player, "player.late-join");
            }
        } else {
            MessageUtil.send(player, "player.late-join");
        }
    }

    public void onPlayerQuit(Player player) {
        if (!gameRunning) return;

        if (alivePlayers.contains(player.getUniqueId())) {
            onPlayerDeath(player);
        }
    }

    public boolean isGameRunning() {
        return gameRunning;
    }

    public GamePhase getCurrentPhase() {
        return currentPhase;
    }

    public int getPhaseTimeLeft() {
        return phaseTimeLeft;
    }

    public int getAlivePlayers() {
        return alivePlayers.size();
    }

    public String getLastWinner() {
        return lastWinner != null ? lastWinner : "Нет";
    }

    public boolean isPlayerAlive(UUID uuid) {
        return alivePlayers.contains(uuid);
    }

    public boolean isPlayerSpectator(UUID uuid) {
        return spectators.contains(uuid);
    }
}