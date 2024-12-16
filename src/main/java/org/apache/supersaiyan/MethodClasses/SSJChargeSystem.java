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
    private Map<UUID, SSJBossBar> energyBars = new HashMap<>();
    private Map<UUID, Boolean> chargingPlayers = new HashMap<>();
    private Map<UUID, Boolean> scoreboardEnabled = new HashMap<>();  // Track scoreboard state
    private String particleType1;
    private String particleType2;
    private int particleCount1;
    private int particleCount2;
    private Map<UUID, BukkitTask> energyDrainTasks = new HashMap<>();
    private int energyDrainRate = 10; // Energy points to drain per second
    
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
        SSJBossBar energyBar;
        if (!energyBars.containsKey(playerId)) {
            energyBar = new SSJBossBar(ssj, ChatColor.GOLD + "Energy: ", new HashMap<>(), false);
            energyBars.put(playerId, energyBar);
        } else {
            energyBar = energyBars.get(playerId);
        }
        energyBar.show(player);
        energyBar.update(player);

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
                    stopCharging(player);
                    this.cancel();
                    return;
                }

                // Play charging particles every 10 ticks (2 times per second)
                if (tickCounter % 10 == 0) {
                    if (particleType1 != null && !particleType1.isEmpty() && particleCount1 > 0) {
                        new SSJParticles(
                            ssj, player, Particle.valueOf(particleType1), Math.min(particleCount1, 20), 2
                        ).createParticles();
                    }
                    if (particleType2 != null && !particleType2.isEmpty() && particleCount2 > 0) {
                        new SSJParticles(
                            ssj, player, Particle.valueOf(particleType2), Math.min(particleCount2, 20), 2
                        ).createParticles();
                    }
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
                            ssj.getSSJEnergyManager().modifyEnergy(player, passiveEnergyGain * transformationEnergyGainMultiplier);
                        } else {
                            int passiveEnergyGain = ssj.getSSJConfigs().getPEMG();
                            ssj.getSSJEnergyManager().modifyEnergy(player, passiveEnergyGain);
                        }
                    }

                    if (!currentForm.equals("Base")) {
                        int nonPassiveEnergyGain = ssj.getSSJConfigs().getNPEMG();
                        ssj.getSSJEnergyManager().modifyEnergy(player, nonPassiveEnergyGain * transformationEnergyGainMultiplier);
                    } else {
                        int nonPassiveEnergyGain = ssj.getSSJConfigs().getNPEMG();
                        ssj.getSSJEnergyManager().modifyEnergy(player, nonPassiveEnergyGain);
                    }

                    // Update energy bar
                    if (energyBars.containsKey(playerId)) {
                        energyBars.get(playerId).update(player);
                    }
                }

                tickCounter++;
            }
        };
        chargeTask = chargeRunnable.runTaskTimer(ssj, 0L, 1L);
    }
    
    public void stopCharging(Player player) {
        UUID playerId = player.getUniqueId();
        
        if (chargeTask != null) {
            chargeTask.cancel();
            chargeTask = null;
        }
        if (chargeRunnable != null) {
            chargeRunnable.cancel();
            chargeRunnable = null;
        }
        if (energyBars.containsKey(playerId)) {
            energyBars.get(playerId).hide(player);
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
    
    @SuppressWarnings("deprecation")
    public void handleEnergyDrain(Player player) {
        boolean isFlying = player.isFlying();
        boolean staffFlightEnabled = ssj.getSSJPCM().isStaffFlightEnabled(player);

        if (isFlying && !staffFlightEnabled) {
            startEnergyDrain(player);
        } else {
            cancelEnergyDrain(player);
        }

        if (player.isOnGround()) {
            cancelEnergyDrain(player);
        }
    }

    private void startEnergyDrain(Player player) {
        UUID playerId = player.getUniqueId();
        
        // Don't start a new task if one is already running
        if (energyDrainTasks.containsKey(playerId)) {
            return;
        }

        BukkitTask task = new BukkitRunnable() {
            @SuppressWarnings("deprecation")
            @Override
            public void run() {
                if (!player.isOnline() || player.isOnGround() || !player.isFlying()) {
                    cancelEnergyDrain(player);
                    return;
                }

                int currentEnergy = ssj.getSSJPCM().getEnergy(player);
                if (currentEnergy <= 0) {
                    player.setFlying(false);
                    player.setAllowFlight(false);
                    player.sendMessage(ChatColor.RED + "You've run out of energy!");
                    cancelEnergyDrain(player);
                    return;
                }

                ssj.getSSJEnergyManager().modifyEnergy(player, -energyDrainRate);
                
                // Update energy bar if it exists
                if (energyBars.containsKey(playerId)) {
                    energyBars.get(playerId).update(player);
                }
            }
        }.runTaskTimer(ssj, 0L, 20L); // Run every second (20 ticks)

        energyDrainTasks.put(playerId, task);
    }

    private void cancelEnergyDrain(Player player) {
        UUID playerId = player.getUniqueId();
        BukkitTask task = energyDrainTasks.remove(playerId);
        if (task != null) {
            task.cancel();
        }
    }

    public void toggleScoreboard(Player player) {
        UUID playerId = player.getUniqueId();
        boolean currentState = scoreboardEnabled.getOrDefault(playerId, false);
        
        if (currentState) {
            player.setScoreboard(ssj.getServer().getScoreboardManager().getNewScoreboard());  // Reset to empty scoreboard
            scoreboardEnabled.put(playerId, false);
            player.sendMessage(ChatColor.GREEN + "Scoreboard removed!");
        } else {
            ssj.getSSJMethods().callScoreboard(player);
            scoreboardEnabled.put(playerId, true);
            player.sendMessage(ChatColor.GREEN + "Scoreboard added!");
        }
    }

    public void cleanup() {
        // Cancel all energy drain tasks
        for (BukkitTask task : energyDrainTasks.values()) {
            task.cancel();
        }
        energyDrainTasks.clear();

        // Clean up all energy bars
        for (Map.Entry<UUID, SSJBossBar> entry : energyBars.entrySet()) {
            Player player = ssj.getServer().getPlayer(entry.getKey());
            if (player != null) {
                entry.getValue().hide(player);
            }
        }
        energyBars.clear();
        
        // Clean up scoreboards
        for (Map.Entry<UUID, Boolean> entry : scoreboardEnabled.entrySet()) {
            if (entry.getValue()) {
                Player player = ssj.getServer().getPlayer(entry.getKey());
                if (player != null) {
                    player.setScoreboard(ssj.getServer().getScoreboardManager().getNewScoreboard());  // Reset to empty scoreboard
                }
            }
        }
        scoreboardEnabled.clear();
        
        // Clear charging players
        chargingPlayers.clear();
    }
}
