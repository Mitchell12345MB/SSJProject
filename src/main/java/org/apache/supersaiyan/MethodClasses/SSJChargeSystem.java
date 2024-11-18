package org.apache.supersaiyan.MethodClasses;

import org.apache.supersaiyan.SSJ;
import org.bukkit.ChatColor;
import org.bukkit.Particle;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SSJChargeSystem {
    private final SSJ ssj;
    private BukkitTask chargeTask;
    private BukkitRunnable chargeRunnable;
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
        
        UUID playerId = player.getUniqueId();
        
        // Use persistent bar if available
        if (ssj.getSSJConfigs().getEnergyBarVisible()) {
            energyBar = ssj.getPersistentEnergyBar();
        } else {
            energyBar = new SSJBossBar(ssj, ChatColor.GOLD + "Energy: ", new HashMap<>(), false);
        }
        
        energyBar.show(player);
        chargingPlayers.put(playerId, true);
        
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
                                break;
                            }
                        }
                    }
                }
            }
        }
        
        particleEffect = new SSJParticles(ssj, player, Particle.valueOf(particleType.toUpperCase()), 10, 3);
        
        chargeRunnable = new BukkitRunnable() {
            int tickCounter = 0;
            @Override
            public void run() {
                if (!player.isOnline() || !isCharging(player)) {
                    cancel();
                    return;
                }
                
                particleEffect.createParticles();
                
                if (tickCounter % 20 == 0) {
                    String form = ssj.getSSJPCM().getForm(player);
                    if (form.equals("Super Saiyan 2") || 
                        form.equals("Super Saiyan 3") || 
                        form.equals("Super Saiyan 4") || 
                        form.equals("Super Saiyan 5") || 
                        form.equals("Super Saiyan God") || 
                        form.equals("Super Saiyan Blue") || 
                        form.equals("Super Saiyan Rose") || 
                        form.equals("Super Saiyan Rage") || 
                        form.equals("Super Saiyan Blue Evolution") || 
                        (form.equals("Kaioken Transformation") && 
                         (form.contains("x50") || form.contains("x100")))) {
                        new SSJParticles(ssj, player, Particle.WAX_OFF, 10, 3).createLightningEffect();
                    }
                    
                    int maxEnergy = ssj.getSSJEnergyManager().getEnergyLimit(player);
                    int currentEnergy = ssj.getSSJPCM().getEnergy(player);
                    
                    if (currentEnergy >= maxEnergy) {
                        player.sendMessage(ChatColor.RED + "You've reached your energy limit!");
                        stopCharging(player);
                        return;
                    }
                    
                    int energyGain = ssj.getSSJConfigs().getNPEMG();
                    ssj.getSSJEnergyManager().modifyEnergy(player, energyGain);
                    ssj.getSSJRpgSys().multBP(player);
                    energyBar.update(player);
                }
                
                tickCounter++;
            }
        };
        chargeTask = chargeRunnable.runTaskTimer(ssj, 0L, 1L);
    }
    
    public void stopCharging(Player player) {
        UUID playerId = player.getUniqueId();
        
        if (energyBar != null) {
            if (!ssj.getSSJConfigs().getEnergyBarVisible()) {
                energyBar.hide(player);
                energyBar = null;
            }
        }
        
        if (chargeTask != null) {
            chargeTask.cancel();
            chargeTask = null;
        }
        if (chargeRunnable != null) {
            chargeRunnable.cancel();
            chargeRunnable = null;
        }
        chargingPlayers.remove(playerId);
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
                                break;
                            }
                        }
                    }
                }
            }
        }
        
        if (particleEffect != null) {
            particleEffect = new SSJParticles(ssj, player, Particle.valueOf(particleType.toUpperCase()), 10, 3);
        }
    }
}
