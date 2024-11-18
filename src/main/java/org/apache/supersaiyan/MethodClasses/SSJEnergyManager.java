package org.apache.supersaiyan.MethodClasses;

import org.apache.supersaiyan.SSJ;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SSJEnergyManager {
    private final SSJ ssj;
    private final Map<UUID, Double> bpMultipliers = new HashMap<>();
    private final Map<UUID, Double> energyGainMultipliers = new HashMap<>();
    private final Map<UUID, Double> energyLimitMultipliers = new HashMap<>();
    private final Map<UUID, Double> energyDrainMultipliers = new HashMap<>();
    private final Map<UUID, Integer> transformationEnergyCosts = new HashMap<>();
    private final Map<UUID, BukkitRunnable> energyDrainTasks = new HashMap<>();
    
    public SSJEnergyManager(SSJ ssj) {
        this.ssj = ssj;
    }
    
    public void setBPMultiplier(Player player, double multiplier) {
        UUID playerId = player.getUniqueId();
        bpMultipliers.put(playerId, multiplier);
    }
    
    public void setEnergyGainMultiplier(Player player, double multiplier) {
        UUID playerId = player.getUniqueId();
        energyGainMultipliers.put(playerId, multiplier);
    }
    
    public void setEnergyLimitMultiplier(Player player, double multiplier) {
        UUID playerId = player.getUniqueId();
        energyLimitMultipliers.put(playerId, multiplier);
    }
    
    public void setEnergyDrainMultiplier(Player player, double multiplier) {
        UUID playerId = player.getUniqueId();
        energyDrainMultipliers.put(playerId, multiplier);
    }
    
    public double getBPMultiplier(Player player) {
        return bpMultipliers.getOrDefault(player.getUniqueId(), 1.0);
    }
    
    public double getEnergyGainMultiplier(Player player) {
        return energyGainMultipliers.getOrDefault(player.getUniqueId(), 1.0);
    }
    
    public double getEnergyLimitMultiplier(Player player) {
        return energyLimitMultipliers.getOrDefault(player.getUniqueId(), 1.0);
    }
    
    public double getEnergyDrainMultiplier(Player player) {
        return energyDrainMultipliers.getOrDefault(player.getUniqueId(), 1.0);
    }
    
    public void setMultipliers(Player player, double bpMultiplier, double gainMultiplier, 
                              double limitMultiplier, double drainMultiplier, int energyCost) {
        UUID playerId = player.getUniqueId();
        bpMultipliers.put(playerId, bpMultiplier);
        energyGainMultipliers.put(playerId, gainMultiplier);
        energyLimitMultipliers.put(playerId, limitMultiplier);
        energyDrainMultipliers.put(playerId, drainMultiplier);
        transformationEnergyCosts.put(playerId, energyCost);
    }
    
    public void resetMultipliers(Player player) {
        UUID playerId = player.getUniqueId();
        bpMultipliers.remove(playerId);
        energyGainMultipliers.remove(playerId);
        energyLimitMultipliers.remove(playerId);
        energyDrainMultipliers.remove(playerId);
        transformationEnergyCosts.remove(playerId);
    }
    
    public int getEnergyLimit(Player player) {
        double baseLimit = ssj.getSSJPCM().getLimit(player);
        double multiplier = getEnergyLimitMultiplier(player);
        return (int) (baseLimit * multiplier);
    }
    
    public void modifyEnergy(Player player, int amount) {
        int currentEnergy = ssj.getSSJPCM().getEnergy(player);
        int newEnergy = Math.max(0, Math.min(getEnergyLimit(player), currentEnergy + amount));
        ssj.getSSJPCM().setPlayerConfigValue(player, "Energy", newEnergy);
        
        // Check if energy is below transformation requirement
        String currentForm = ssj.getSSJPCM().getForm(player);
        if (!currentForm.equals("Base")) {
            int requiredEnergy = getTransformationEnergyCost(player);
            if (newEnergy < requiredEnergy) {
                // Force detransform
                ssj.getSSJTransformationManager().revertToBase(player);
                player.sendMessage("§cNot enough energy to maintain transformation!");
            }
        }
    }
    
    public double getCurrentEnergyPercentage(Player player) {
        int currentEnergy = ssj.getSSJPCM().getEnergy(player);
        int maxEnergy = getEnergyLimit(player);
        return (double) currentEnergy / maxEnergy * 100;
    }
    
    public void startEnergyDrain(Player player) {
        UUID playerId = player.getUniqueId();
        if (energyDrainTasks.containsKey(playerId)) {
            return;
        }

        BukkitRunnable drainTask = new BukkitRunnable() {
            @Override
            public void run() {
                if (!player.isOnline()) {
                    cancel();
                    energyDrainTasks.remove(playerId);
                    return;
                }

                int baseEnergyDrain = ssj.getSSJConfigs().getBaseEnergyDrain();
                int drainAmount = (int) Math.round(baseEnergyDrain * getEnergyDrainMultiplier(player));
                modifyEnergy(player, -drainAmount);

                if (ssj.getSSJPCM().getEnergy(player) <= 0) {
                    cancel();
                    energyDrainTasks.remove(playerId);
                }
            }
        };

        drainTask.runTaskTimer(ssj, 20L, 20L);
        energyDrainTasks.put(playerId, drainTask);
    }
    
    public void stopEnergyDrain(Player player) {
        UUID playerId = player.getUniqueId();
        BukkitRunnable task = energyDrainTasks.remove(playerId);
        if (task != null) {
            task.cancel();
        }
    }
    
    public int getTransformationEnergyCost(Player player) {
        return transformationEnergyCosts.getOrDefault(player.getUniqueId(), 100);
    }
}