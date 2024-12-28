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
    private final Map<UUID, Integer> energyDrainSources = new HashMap<>();
    
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
        
        // Get potential multiplier if applicable
        double potentialMultiplier = 1.0;
        if (ssj.getSSJPCM().isSkillEnabled(player, "Potential")) {
            int potentialLevel = ssj.getSSJPCM().getSkillLevel(player, "Potential");
            potentialMultiplier = 1.0 + (potentialLevel * 0.1); // 10% increase per level
        }
        
        return (int) (baseLimit * multiplier * potentialMultiplier);
    }
    
    public void modifyEnergy(Player player, int amount) {
        int currentEnergy = ssj.getSSJPCM().getEnergy(player);
        double gainMultiplier = getEnergyGainMultiplier(player);
        
        // Apply gain multiplier only for positive energy changes
        int modifiedAmount = amount > 0 ? (int)(amount * gainMultiplier) : amount;
        
        int newEnergy = Math.max(0, Math.min(getEnergyLimit(player), currentEnergy + modifiedAmount));
        ssj.getSSJPCM().setPlayerConfigValue(player, "Energy", newEnergy);

        // Update potential system
        ssj.getSSJPotentialSystem().updatePotential(player);

        // Recalculate BP since energy changed
        ssj.getSSJRpgSys().multBP(player);

        // Update the scoreboard
        ssj.getSSJMethodChecks().checkScoreboard();
        ssj.getSSJMethods().callScoreboard(player);

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
    
    public void startEnergyDrain(Player player, String source) {
        UUID playerId = player.getUniqueId();
        energyDrainSources.put(playerId, energyDrainSources.getOrDefault(playerId, 0) + 1);

        if (energyDrainTasks.containsKey(playerId)) {
            // Task already running
            return;
        }

        BukkitRunnable drainTask = new BukkitRunnable() {
            @Override
            public void run() {
                if (!player.isOnline()) {
                    cancel();
                    energyDrainTasks.remove(playerId);
                    energyDrainSources.remove(playerId);
                    return;
                }

                int baseEnergyDrain = ssj.getSSJConfigs().getBaseEnergyDrain();
                double drainMultiplier = getEnergyDrainMultiplier(player);
                int drainAmount = (int) Math.round(baseEnergyDrain * drainMultiplier);

                modifyEnergy(player, -drainAmount);
                ssj.getSSJRpgSys().multBP(player);

                if (ssj.getSSJPCM().getEnergy(player) <= 0) {
                    cancel();
                    energyDrainTasks.remove(playerId);
                    energyDrainSources.remove(playerId);
                    // Handle what happens when energy depletes
                    if (ssj.getSSJPCM().getForm(player).equals("Base")) {
                        // Stop flying if in flight
                        player.setFlying(false);
                        player.setAllowFlight(false);
                    } else {
                        // Revert to base form
                        ssj.getSSJTransformationManager().revertToBase(player);
                    }
                }
            }
        };

        drainTask.runTaskTimer(ssj, 20L, 20L); // Runs every second
        energyDrainTasks.put(playerId, drainTask);
    }
    
    public void stopEnergyDrain(Player player, String source) {
        UUID playerId = player.getUniqueId();
        int sources = energyDrainSources.getOrDefault(playerId, 0) - 1;

        if (sources <= 0) {
            // No more sources requiring energy drain
            energyDrainSources.remove(playerId);
            BukkitRunnable task = energyDrainTasks.remove(playerId);
            if (task != null) {
                task.cancel();
            }
        } else {
            // Update the number of sources
            energyDrainSources.put(playerId, sources);
        }
    }
    
    public int getTransformationEnergyCost(Player player) {
        return transformationEnergyCosts.getOrDefault(player.getUniqueId(), 100);
    }
    
    public void stopAllEnergyDrains(Player player) {
        UUID playerId = player.getUniqueId();

        // Remove all energy drain sources for the player
        energyDrainSources.remove(playerId);

        // Cancel and remove the energy drain task if it exists
        BukkitRunnable task = energyDrainTasks.remove(playerId);
        if (task != null) {
            task.cancel();
        }
    }
    
    public void handlePotentialCharge(Player player) {
        if (!ssj.getSSJPCM().isSkillEnabled(player, "Potential")) {
            return;
        }
        
        int potentialLevel = ssj.getSSJPCM().getSkillLevel(player, "Potential");
        double chargeMultiplier = 1.0 + (potentialLevel * 0.05); // 5% faster charging per level
        
        // Apply potential charge multiplier
        int baseCharge = ssj.getSSJConfigs().getNPEMG();
        int modifiedCharge = (int)(baseCharge * chargeMultiplier);
        
        modifyEnergy(player, modifiedCharge);
    }
}