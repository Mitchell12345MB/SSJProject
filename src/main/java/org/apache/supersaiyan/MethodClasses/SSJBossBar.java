package org.apache.supersaiyan.MethodClasses;

import org.apache.supersaiyan.SSJ;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import java.util.Map;
import java.util.UUID;
import java.util.HashMap;
import java.util.HashSet;

public class SSJBossBar {
    private final SSJ ssj;
    private final String title;
    private final Map<UUID, Integer> playerStats;
    private final boolean isPersistent;
    private BukkitTask updateTask;
    private final boolean isTransformBar;
    private BossBar bossBar;
    private final Map<UUID, Integer> transformationProgress = new HashMap<>();
    private BukkitTask energyBarTask;

    public SSJBossBar(SSJ ssj, String title, Map<UUID, Integer> playerStats, boolean isPersistent) {
        this.ssj = ssj;
        this.title = title != null ? title : "Energy: ";
        this.playerStats = playerStats != null ? playerStats : new HashMap<>();
        this.isTransformBar = title != null && title.contains("Transformation");
        this.isPersistent = !isTransformBar && (isPersistent || ssj.getSSJConfigs().getEnergyBarVisible());
        
        if (isTransformBar) {
            this.bossBar = Bukkit.createBossBar(this.title, BarColor.PURPLE, BarStyle.SOLID);
        } else {
            this.bossBar = Bukkit.createBossBar(this.title, BarColor.YELLOW, BarStyle.SEGMENTED_10);
        }
        
        if (this.isPersistent) {
            startPersistentUpdates();
        }
    }

    private void startPersistentUpdates() {
        if (updateTask != null) {
            updateTask.cancel();
        }
        
        updateTask = new BukkitRunnable() {
            @Override
            public void run() {
                if (playerStats == null || playerStats.isEmpty()) {
                    return;
                }
                
                for (UUID playerId : new HashSet<>(playerStats.keySet())) {
                    Player player = Bukkit.getPlayer(playerId);
                    if (player != null && player.isOnline()) {
                        update(player);
                    } else {
                        playerStats.remove(playerId);
                    }
                }
            }
        }.runTaskTimer(ssj, 0L, 20L); // Update every second
    }

    public void show(Player player) {
        if (bossBar == null) return;
        
        bossBar.addPlayer(player);
        playerStats.putIfAbsent(player.getUniqueId(), 0);
        
        if (isTransformBar) {
            transformationProgress.putIfAbsent(player.getUniqueId(), 0);
        } else if (isPersistent) {
            startPersistentUpdates();
        }
        update(player);
    }

    public void hide(Player player) {
        if (bossBar != null) {
            if (!isPersistent) {
                bossBar.removePlayer(player);
                playerStats.remove(player.getUniqueId());
                if (energyBarTask != null) {
                    energyBarTask.cancel();
                    energyBarTask = null;
                }
            }
        }
    }

    public void update(Player player) {
        if (bossBar == null) return;
        
        if (isTransformBar) {
            int progress = transformationProgress.getOrDefault(player.getUniqueId(), 0);
            bossBar.setProgress(progress / 100.0);
            bossBar.setTitle("§5Transformation Progress: §d" + progress + "%");
        } else {
            double currentEnergy = ssj.getSSJPCM().getEnergy(player);
            double maxEnergy = ssj.getSSJEnergyManager().getEnergyLimit(player);
            double progress = Math.min(1.0, currentEnergy / maxEnergy);
            
            bossBar.setProgress(progress);
            bossBar.setTitle("§6Energy: §e" + (int)currentEnergy + "§6/§e" + (int)maxEnergy);
        }
    }

    public void addProgress(Player player, int amount) {
        int currentProgress = transformationProgress.getOrDefault(player.getUniqueId(), 0);
        transformationProgress.put(player.getUniqueId(), Math.min(100, currentProgress + amount));
        update(player);
    }

    public void removeProgress(Player player, int amount) {
        int currentProgress = transformationProgress.getOrDefault(player.getUniqueId(), 0);
        transformationProgress.put(player.getUniqueId(), Math.max(0, currentProgress - amount));
        update(player);
    }

    public void resetProgress(Player player) {
        transformationProgress.remove(player.getUniqueId());
    }

    public int getProgress(Player player) {
        return transformationProgress.getOrDefault(player.getUniqueId(), 0);
    }

    public Map<UUID, Integer> getPlayerStats() {
        return playerStats;
    }

    public void cleanup() {
        if (energyBarTask != null) {
            energyBarTask.cancel();
            energyBarTask = null;
        }
        if (updateTask != null) {
            updateTask.cancel();
            updateTask = null;
        }
        if (bossBar != null) {
            bossBar.removeAll();
        }
    }
}
