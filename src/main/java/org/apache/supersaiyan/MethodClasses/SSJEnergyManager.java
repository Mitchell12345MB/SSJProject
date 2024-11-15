package org.apache.supersaiyan.MethodClasses;

import org.apache.supersaiyan.SSJ;
import org.bukkit.entity.Player;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class SSJEnergyManager {
    private final SSJ ssj;
    private Map<UUID, Double> energyGainMultipliers = new HashMap<>();
    private Map<UUID, Double> energyLimitMultipliers = new HashMap<>();
    private Map<UUID, Double> energyDrainMultipliers = new HashMap<>();
    private Map<UUID, Integer> transformationEnergyCosts = new HashMap<>();
    private Map<UUID, Double> bpMultipliers = new HashMap<>();
    private Map<UUID, Float> baseEnergyPercentages = new HashMap<>();
    private Map<UUID, BukkitTask> drainTasks = new HashMap<>();

    public SSJEnergyManager(SSJ ssj) {
        this.ssj = ssj;
    }

    public void setMultipliers(Player player, double bpMultiplier, double gainMultiplier, double limitMultiplier, double drainMultiplier, int energyCost) {
        UUID playerId = player.getUniqueId();
        
        float currentPercentage = getCurrentEnergyPercentage(player);
        baseEnergyPercentages.put(playerId, currentPercentage);
        
        bpMultipliers.put(playerId, bpMultiplier);
        energyGainMultipliers.put(playerId, gainMultiplier);
        energyLimitMultipliers.put(playerId, limitMultiplier);
        energyDrainMultipliers.put(playerId, drainMultiplier);
        transformationEnergyCosts.put(playerId, energyCost);
        
        scaleEnergy(player, currentPercentage);
    }
    
    public float getCurrentEnergyPercentage(Player player) {
        int currentEnergy = ssj.getSSJPCM().getEnergy(player);
        int currentLimit = getEnergyLimit(player);
        return currentLimit > 0 ? (float) currentEnergy / currentLimit : 0f;
    }
    
    private void scaleEnergy(Player player, float percentage) {
        int newLimit = getEnergyLimit(player);
        int newEnergy = (int) (newLimit * percentage);
        ssj.getSSJPCM().setPlayerConfigValue(player, "Energy", newEnergy);
    }
    
    public void resetMultipliers(Player player) {
        UUID playerId = player.getUniqueId();
        float currentPercentage = baseEnergyPercentages.getOrDefault(playerId, 0f);
        
        bpMultipliers.remove(playerId);
        energyGainMultipliers.remove(playerId);
        energyLimitMultipliers.remove(playerId);
        energyDrainMultipliers.remove(playerId);
        transformationEnergyCosts.remove(playerId);
        baseEnergyPercentages.remove(playerId);
        
        scaleEnergy(player, currentPercentage);
    }

    public double getBPMultiplier(Player player) {
        return bpMultipliers.getOrDefault(player.getUniqueId(), 1.0);
    }

    public int getEnergyLimit(Player player) {
        double baseLimit = ssj.getSSJConfigs().getEML() * 100;
        double multiplier = energyLimitMultipliers.getOrDefault(player.getUniqueId(), 1.0);
        return (int) (baseLimit * multiplier);
    }

    public void modifyEnergy(Player player, int amount) {
        UUID playerId = player.getUniqueId();
        int currentEnergy = ssj.getSSJPCM().getEnergy(player);
        double multiplier = amount > 0 
            ? energyGainMultipliers.getOrDefault(playerId, 1.0)
            : energyDrainMultipliers.getOrDefault(playerId, 1.0);
        
        int modifiedAmount = (int) (amount * multiplier);
        int energyLimit = getEnergyLimit(player);
        int newEnergy = Math.max(0, Math.min(currentEnergy + modifiedAmount, energyLimit));
        
        if (newEnergy != currentEnergy) {
            ssj.getSSJPCM().setPlayerConfigValue(player, "Energy", newEnergy);
            ssj.getSSJRpgSys().multBP(player);
            
            if (newEnergy == 0) {
                String currentForm = ssj.getSSJPCM().getForm(player);
                if (!currentForm.equals("Base")) {
                    player.sendMessage("§cYou've run out of energy! Reverting to base form!");
                    ssj.getSSJTransformationManager().detransform(player);
                }
            }
        }
    }

    public int getTransformationCost(Player player) {
        return transformationEnergyCosts.getOrDefault(player.getUniqueId(), 100);
    }

    public void startEnergyDrain(Player player) {
        UUID playerId = player.getUniqueId();
        if (drainTasks.containsKey(playerId)) {
            return;
        }
        
        BukkitTask task = new BukkitRunnable() {
            @Override
            public void run() {
                if (!player.isOnline()) {
                    stopEnergyDrain(player);
                    return;
                }
                
                double drainMultiplier = energyDrainMultipliers.getOrDefault(playerId, 1.0);
                int drainAmount = (int)(-5 * drainMultiplier); // Base drain of 5 energy per tick
                modifyEnergy(player, drainAmount);
            }
        }.runTaskTimer(ssj, 20L, 20L); // Run every second
        
        drainTasks.put(playerId, task);
    }

    public void stopEnergyDrain(Player player) {
        UUID playerId = player.getUniqueId();
        BukkitTask task = drainTasks.remove(playerId);
        if (task != null) {
            task.cancel();
        }
    }
}