package org.apache.supersaiyan.MethodClasses;

import org.apache.supersaiyan.SSJ;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SSJChargeSystem {
    private final SSJ ssj;
    private BukkitRunnable chargeTask;
    private SSJParticles particleEffect;
    private SSJBossBar energyBar;
    private Map<UUID, Boolean> chargingPlayers = new HashMap<>();
    
    public SSJChargeSystem(SSJ ssj) {
        this.ssj = ssj;
    }
    
    public void startCharging(Player player) {
        if (isCharging(player)) {
            stopCharging(player);
            return;
        }
        
        @SuppressWarnings("unused")
        String currentForm = ssj.getSSJPCM().getForm(player);
        String particleType = "FLAME";
        
        particleEffect = new SSJParticles(ssj, player, Particle.valueOf(particleType.toUpperCase()), 50, 3);
        energyBar = new SSJBossBar(ssj, ChatColor.GOLD + "Energy: ", new HashMap<>(), ssj.getSSJConfigs().getEnergyBarVisible());
        energyBar.show(player);
        
        int currentEnergy = ssj.getSSJPCM().getEnergy(player);
        int maxEnergy = ssj.getSSJPCM().getLimit(player);
        float energyPercent = (float) currentEnergy / maxEnergy;
        
        energyBar.getPlayerStats().put(player.getUniqueId(), (int)(energyPercent * 100));
        energyBar.update(player);
        chargingPlayers.put(player.getUniqueId(), true);
        
        chargeTask = new BukkitRunnable() {
            private int tickCounter = 0;
            
            @Override
            public void run() {
                if (!player.isOnline()) {
                    stopCharging(player);
                    return;
                }
                
                // For hold-to-charge, check if player is still holding right-click
                if (ssj.getSSJConfigs().getHoldChargeItem()) {
                    ItemStack heldItem = player.getInventory().getItemInMainHand();
                    if (heldItem.getType() != Material.MAGMA_CREAM || !player.isHandRaised()) {
                        stopCharging(player);
                        return;
                    }
                }
                
                // Create particles every tick
                particleEffect.createParticles();
                
                // Process energy gain every second (20 ticks)
                if (tickCounter % 20 == 0) {
                    int maxEnergy = ssj.getSSJPCM().getLimit(player);
                    int currentEnergy = ssj.getSSJPCM().getEnergy(player);
                    
                    if (currentEnergy >= maxEnergy) {
                        player.sendMessage(ChatColor.RED + "You've reached your energy limit!");
                        stopCharging(player);
                        return;
                    }
                    
                    int energyGain = ssj.getSSJConfigs().getNPEMG();
                    int newEnergy = Math.min(maxEnergy, currentEnergy + energyGain);
                    ssj.getSSJPCM().setPlayerConfigValue(player, "Energy", newEnergy);
                    
                    float newPercent = (float) newEnergy / maxEnergy;
                    energyBar.getPlayerStats().put(player.getUniqueId(), (int)(newPercent * 100));
                    energyBar.update(player);
                    
                    ssj.getSSJRpgSys().multBP(player);
                    ssj.getSSJMethodChecks().scoreBoardCheck();
                    ssj.getSSJMethods().callScoreboard(player);
                }
                
                tickCounter++;
            }
        };
        
        chargeTask.runTaskTimer(ssj, 0L, 1L);
    }
    
    public void stopCharging(Player player) {
        if (energyBar != null) {
            energyBar.hide(player);
            energyBar = null;
        }
        if (chargeTask != null) {
            chargeTask.cancel();
            chargeTask = null;
        }
        chargingPlayers.remove(player.getUniqueId());
    }
    
    public boolean isCharging(Player player) {
        return chargingPlayers.getOrDefault(player.getUniqueId(), false);
    }
    
    public boolean isCharging() {
        return !chargingPlayers.isEmpty();
    }
    
    public void updateParticles(Player player) {
        String currentForm = ssj.getSSJPCM().getForm(player);
        String particleType = "FLAME"; // Default particle
        
        if (!currentForm.equals("Base")) {
            String[] categories = {"Base_Forms", "Kaioken_Forms", "Saiyan_Forms", 
                                 "Legendary_Saiyan_Forms", "Saiyan_God_Forms"};
            
            for (String category : categories) {
                ConfigurationSection section = ssj.getSSJConfigs().getTCFile().getConfigurationSection(category);
                if (section != null) {
                    for (String key : section.getKeys(false)) {
                        String desc = section.getString(key + ".Desc");
                        if (currentForm.equals(desc)) {
                            String particlePath = category + "." + key + ".Particle.Type";
                            if (ssj.getSSJConfigs().getTCFile().contains(particlePath)) {
                                particleType = ssj.getSSJConfigs().getTCFile().getString(particlePath);
                            }
                            break;
                        }
                    }
                }
            }
        }
        
        if (isCharging(player)) {
            particleEffect = new SSJParticles(ssj, player, Particle.valueOf(particleType.toUpperCase()), 50, 3);
        }
    }
}
