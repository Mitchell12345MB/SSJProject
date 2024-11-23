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
    private SSJBossBar energyBar;
    private Map<UUID, Boolean> chargingPlayers = new HashMap<>();
    private String particleType1;
    private String particleType2;
    private int particleCount1;
    private int particleCount2;
    
    public SSJChargeSystem(SSJ ssj) {
        this.ssj = ssj;
    }
    
    public void startCharging(Player player) {
        UUID playerId = player.getUniqueId();

        if (isCharging(player)) {
            player.sendMessage(ChatColor.RED + "You are already charging!");
            return;
        }

        // Show energy bar if not already visible
        if (energyBar == null) {
            energyBar = new SSJBossBar(ssj, ChatColor.GOLD + "Energy: ", new HashMap<>(), false);
        }

        // Show energy bar if config is enabled
        if (!ssj.getSSJConfigs().getEnergyBarVisible()) {
            energyBar.show(player);
        }

        // Update energy bar if it exists
        if (energyBar != null) {
            energyBar.update(player);
        }

        // Add player to charging list
        chargingPlayers.put(playerId, true);

        // Determine particle types and counts based on current transformation
        String currentForm = ssj.getSSJPCM().getForm(player);
        particleType1 = "FLAME"; // Default particle
        particleCount1 = 10;     // Default count
        particleType2 = null;   // Default second particle
        particleCount2 = 0;     // Default second particle count

        // Fetch particle settings from the configuration
        if (!currentForm.equals("Base")) {
            // Fetch particle settings from the configuration
            String[] categories = {
                "Base_Forms", "Kaioken_Forms", "Saiyan_Forms",
                "Legendary_Saiyan_Forms", "Saiyan_God_Forms"
            };

            for (String category : categories) {
                ConfigurationSection section = ssj.getSSJConfigs().getTCFile().getConfigurationSection(category);
                if (section != null) {
                    for (String key : section.getKeys(false)) {
                        String desc = section.getString(key + ".Desc");
                        if (currentForm.equals(desc)) {
                            String particlePath1 = category + "." + key + ".Particle.Type";
                            String particleCountPath1 = category + "." + key + ".Particle.Count";

                            String particlePath2 = category + "." + key + ".Particle2.Type";
                            String particleCountPath2 = category + "." + key + ".Particle2.Count";

                            if (ssj.getSSJConfigs().getTCFile().contains(particlePath1)) {
                                particleType1 = ssj.getSSJConfigs().getTCFile()
                                    .getString(particlePath1).toUpperCase();
                            }
                            if (ssj.getSSJConfigs().getTCFile().contains(particleCountPath1)) {
                                particleCount1 = ssj.getSSJConfigs().getTCFile()
                                    .getInt(particleCountPath1);
                            }

                            if (ssj.getSSJConfigs().getTCFile().contains(particlePath2)) {
                                particleType2 = ssj.getSSJConfigs().getTCFile()
                                    .getString(particlePath2).toUpperCase();
                            }
                            if (ssj.getSSJConfigs().getTCFile().contains(particleCountPath2)) {
                                particleCount2 = ssj.getSSJConfigs().getTCFile()
                                    .getInt(particleCountPath2);
                            }
                            break;
                        }
                    }
                }
            }
        }

        // Start charging task
        chargeRunnable = new BukkitRunnable() {
            int tickCounter = 0;

            @Override
            public void run() {
                if (!player.isOnline() || !isCharging(player)) {
                    this.cancel();
                    return;
                }

                // Play charging particles
                if (particleType1 != null && !particleType1.isEmpty() && particleCount1 > 0) {
                    new SSJParticles(
                        ssj, player, Particle.valueOf(particleType1), particleCount1, 4
                    ).createParticles();
                }
                if (particleType2 != null && !particleType2.isEmpty() && particleCount2 > 0) {
                    new SSJParticles(
                        ssj, player, Particle.valueOf(particleType2), particleCount2, 5
                    ).createParticles();
                }

                // Every 20 ticks (1 second)
                if (tickCounter % 20 == 0) {
                    int maxEnergy = ssj.getSSJEnergyManager().getEnergyLimit(player);
                    int currentEnergy = ssj.getSSJPCM().getEnergy(player);
                    int transformationEnergyGainMultiplier = ssj.getSSJConfigs().getTCFile().getInt(
                        currentForm + ".Traits.ENERGY_GAIN_MULTIPLIER"
                    );

                    if (currentEnergy >= maxEnergy) {
                        player.sendMessage(ChatColor.RED + "You've reached your energy limit!");
                        stopCharging(player);
                        return;
                    }

                    // Modify energy
                    if (ssj.getSSJConfigs().getPassiveEnergyGain()) {
                        if (!currentForm.equals("Base")) {
                            int passiveEnergyGain = ssj.getSSJConfigs().getPEMG();
                            ssj.getSSJEnergyManager().modifyEnergy(player, passiveEnergyGain * transformationEnergyGainMultiplier * (currentEnergy / 2));
                        } else {
                            int passiveEnergyGain = ssj.getSSJConfigs().getPEMG();
                            ssj.getSSJEnergyManager().modifyEnergy(player, passiveEnergyGain * (currentEnergy / 2));
                        }
                    }

                    if (!currentForm.equals("Base")) {
                        int nonPassiveEnergyGain = ssj.getSSJConfigs().getNPEMG();
                        ssj.getSSJEnergyManager().modifyEnergy(player, nonPassiveEnergyGain * transformationEnergyGainMultiplier + (currentEnergy / 2));
                    } else {
                        int nonPassiveEnergyGain = ssj.getSSJConfigs().getNPEMG();
                        ssj.getSSJEnergyManager().modifyEnergy(player, nonPassiveEnergyGain * (currentEnergy / 2));
                    }

                    // Recalculate BP
                    ssj.getSSJRpgSys().multBP(player);

                    // Update the scoreboard
                    ssj.getSSJMethodChecks().scoreBoardCheck();
                    ssj.getSSJMethods().callScoreboard(player);

                    // Update energy bar
                    if (energyBar != null) {
                        energyBar.update(player);
                    }
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
        particleType1 = "FLAME"; // Default particle
        particleType2 = null;
        
        if (!currentForm.equals("Base")) {
            String[] categories = {"Base_Forms", "Kaioken_Forms", "Saiyan_Forms", 
                                 "Legendary_Saiyan_Forms", "Saiyan_God_Forms"};
            
            for (String category : categories) {
                ConfigurationSection section = ssj.getSSJConfigs().getTCFile().getConfigurationSection(category);
                if (section != null) {
                    for (String key : section.getKeys(false)) {
                        String desc = section.getString(key + ".Desc");
                        if (currentForm.equals(desc)) {
                            String particlePath1 = category + "." + key + ".Particle.Type";
                            String particlePath2 = category + "." + key + ".Particle2.Type";
                            if (ssj.getSSJConfigs().getTCFile().contains(particlePath1)) {
                                particleType1 = ssj.getSSJConfigs().getTCFile().getString(particlePath1);
                                break;
                            }
                            if (ssj.getSSJConfigs().getTCFile().contains(particlePath2)) {
                                particleType2 = ssj.getSSJConfigs().getTCFile().getString(particlePath2);
                                break;
                            }
                        }
                    }
                }
            }
        }
        
        // Play charging particles
        if (particleType1 != null && !particleType1.isEmpty() && particleCount1 > 0) {
            new SSJParticles(
                ssj, player, Particle.valueOf(particleType1), particleCount1, 4
            ).createParticles();
        }
        if (particleType2 != null && !particleType2.isEmpty() && particleCount2 > 0) {
            new SSJParticles(
                ssj, player, Particle.valueOf(particleType2), particleCount2, 5
            ).createParticles();
        }
    }
}
