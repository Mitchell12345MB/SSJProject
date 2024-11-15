package org.apache.supersaiyan.MethodClasses;

import org.apache.supersaiyan.SSJ;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
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
    private Map<UUID, Long> lastUpdateTime = new HashMap<>();
    private static final long UPDATE_COOLDOWN = 50L; // 50ms cooldown between updates
    private final Map<UUID, Integer> transformationProgress = new HashMap<>();
    private BukkitTask energyBarTask;

    public SSJBossBar(SSJ ssj, String title, Map<UUID, Integer> playerStats, boolean isPersistent) {
        this.ssj = ssj;
        this.title = title != null ? title : "Energy: ";
        this.playerStats = playerStats != null ? playerStats : new HashMap<>();
        this.isPersistent = isPersistent || ssj.getSSJConfigs().getEnergyBarVisible();
        this.isTransformBar = title != null && title.contains("Transformation");
        
        if (isTransformBar) {
            this.bossBar = Bukkit.createBossBar(this.title, BarColor.PURPLE, BarStyle.SOLID);
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
        if (isTransformBar) {
            bossBar.addPlayer(player);
        } else if (isPersistent) {
            // Start persistent energy bar updates
            if (energyBarTask == null) {
                energyBarTask = new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (player.isOnline()) {
                            update(player);
                        } else {
                            hide(player);
                        }
                    }
                }.runTaskTimer(ssj, 0L, 20L); // Update every second
            }
        }
        update(player);
    }

    public void hide(Player player) {
        if (isTransformBar && bossBar != null) {
            bossBar.removePlayer(player);
        }
        if (!isPersistent) {
            playerStats.remove(player.getUniqueId());
            if (energyBarTask != null) {
                energyBarTask.cancel();
                energyBarTask = null;
            }
        }
    }

    public void update(Player player) {
        long currentTime = System.currentTimeMillis();
        long lastUpdate = lastUpdateTime.getOrDefault(player.getUniqueId(), 0L);
        
        if (currentTime - lastUpdate < UPDATE_COOLDOWN) {
            return;
        }
        
        lastUpdateTime.put(player.getUniqueId(), currentTime);
        
        if (isTransformBar) {
            // Use stored transformation progress instead of energy percentage
            int progress = transformationProgress.getOrDefault(player.getUniqueId(), 0);
            float progressPercent = progress / 100f;
            bossBar.setProgress(Math.min(1.0, progressPercent));
            bossBar.setTitle(ChatColor.LIGHT_PURPLE + "Transformation Progress: " + 
                            ChatColor.YELLOW + progress + "%");
        } else {
            // Energy bar logic remains the same
            int currentEnergy = ssj.getSSJPCM().getEnergy(player);
            int maxEnergy = ssj.getSSJEnergyManager().getEnergyLimit(player);
            int realTimePercent = Math.round(((float) currentEnergy / maxEnergy) * 100);
            
            StringBuilder energyBar = new StringBuilder();
            int energyBarLength = 20;
            int energyFilledBars = Math.round((realTimePercent / 100f) * energyBarLength);
            
            energyBar.append(ChatColor.GOLD).append("Energy: ");
            energyBar.append(ChatColor.GREEN);
            for (int i = 0; i < energyFilledBars; i++) {
                energyBar.append("│");
            }
            energyBar.append(ChatColor.RED);
            for (int i = energyFilledBars; i < energyBarLength; i++) {
                energyBar.append("│");
            }
            energyBar.append(" ").append(ChatColor.YELLOW).append(realTimePercent).append("%");

            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(energyBar.toString()));
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
