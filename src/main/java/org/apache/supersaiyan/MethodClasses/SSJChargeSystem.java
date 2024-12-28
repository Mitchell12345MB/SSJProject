package org.apache.supersaiyan.MethodClasses;

import org.apache.supersaiyan.SSJ;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
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
    private String particleType1 = "FLAME";  // Default particle type
    private String particleType2 = "CRIT";   // Secondary particle type
    private int particleCount1 = 5;          // Default particle count
    private int particleCount2 = 3;          // Secondary particle count
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

        // Check if hold charge is enabled and player is holding the charge item
        if (ssj.getSSJConfigs().getHoldChargeItem()) {
            ItemStack heldItem = player.getInventory().getItemInMainHand();
            if (!isChargeItem(heldItem)) {
                return;
            }
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

        // Start charging task
        startChargingTask(player);
    }

    private void startChargingTask(Player player) {
        UUID playerId = player.getUniqueId();
        
        // Cancel any existing task for this player
        if (energyDrainTasks.containsKey(playerId)) {
            energyDrainTasks.get(playerId).cancel();
            energyDrainTasks.remove(playerId);
        }
        
        chargeRunnable = new BukkitRunnable() {
            int tickCounter = 0;

            @Override
            public void run() {
                // Stop charging if player is offline or not charging
                if (!player.isOnline() || !isCharging(player)) {
                    stopCharging(player);
                    this.cancel();
                    return;
                }

                // Check if hold charge is enabled and player is still holding the charge item
                if (ssj.getSSJConfigs().getHoldChargeItem()) {
                    ItemStack heldItem = player.getInventory().getItemInMainHand();
                    if (!isChargeItem(heldItem)) {
                        stopCharging(player);
                        this.cancel();
                        return;
                    }
                }

                // Handle particles and energy gain
                if (tickCounter % 10 == 0) {
                    updateParticles(player);
                }

                if (tickCounter % 20 == 0) {
                    handleEnergyGain(player);
                }

                tickCounter++;
            }
        };
        chargeTask = chargeRunnable.runTaskTimer(ssj, 0L, 1L);
    }

    private boolean isChargeItem(ItemStack item) {
        return item != null && item.getType() == Material.MAGMA_CREAM;  // Changed from TNT to MAGMA_CREAM
    }

    private void handleEnergyGain(Player player) {
        String currentForm = ssj.getSSJPCM().getForm(player);
        int maxEnergy = ssj.getSSJEnergyManager().getEnergyLimit(player);
        int currentEnergy = ssj.getSSJPCM().getEnergy(player);

        if (currentEnergy >= maxEnergy) {
            player.sendMessage(ChatColor.RED + "You've reached your energy limit!");
            stopCharging(player);
            return;
        }

        // Calculate energy gain with potential boost
        if (!currentForm.equals("Base")) {
            int energyGain = ssj.getSSJConfigs().getNPEMG();
            ssj.getSSJEnergyManager().modifyEnergy(player, energyGain);
            ssj.getSSJEnergyManager().handlePotentialCharge(player);
            
            // Create charging particles based on form
            if (particleType1 != null && !particleType1.isEmpty()) {
                try {
                    Particle particle1 = Particle.valueOf(particleType1);
                    new SSJParticles(ssj, player, particle1, particleCount1, 2).createParticles();
                } catch (IllegalArgumentException e) {
                    ssj.getLogger().warning("Invalid particle type: " + particleType1);
                }
            }
            
            if (particleType2 != null && !particleType2.isEmpty()) {
                try {
                    Particle particle2 = Particle.valueOf(particleType2);
                    new SSJParticles(ssj, player, particle2, particleCount2, 2).createParticles();
                } catch (IllegalArgumentException e) {
                    ssj.getLogger().warning("Invalid particle type: " + particleType2);
                }
            }
        } else {
            int energyGain = ssj.getSSJConfigs().getNPEMG();
            ssj.getSSJEnergyManager().modifyEnergy(player, energyGain);
            
            // Create base form charging particles
            try {
                Particle baseParticle = Particle.valueOf(particleType1);
                new SSJParticles(ssj, player, baseParticle, particleCount1, 2).createParticles();
            } catch (IllegalArgumentException e) {
                ssj.getLogger().warning("Invalid particle type: " + particleType1);
            }
        }

        // Update energy bar
        if (energyBars.containsKey(player.getUniqueId())) {
            energyBars.get(player.getUniqueId()).update(player);
        }
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
        
        // Default particles for base form
        String particleType1 = "FLAME";
        String particleType2 = null;
        int particleCount1 = 5;  // Reduced from 10
        int particleCount2 = 0;
        
        if (!currentForm.equals("Base")) {
            String[] categories = {"Base_Forms", "Kaioken_Forms", "Saiyan_Forms", 
                                 "Legendary_Saiyan_Forms", "Saiyan_God_Forms"};
            
            for (String category : categories) {
                ConfigurationSection section = ssj.getSSJConfigs().getTCFile().getConfigurationSection(category);
                if (section != null) {
                    for (String key : section.getKeys(false)) {
                        String desc = section.getString(key + ".Desc");
                        if (currentForm.equals(desc)) {
                            // Get particle settings
                            if (section.contains(key + ".Particle.Type")) {
                                particleType1 = section.getString(key + ".Particle.Type").toUpperCase();
                                particleCount1 = Math.min(section.getInt(key + ".Particle.Count", 5), 10);  // Cap at 10
                            }
                            
                            if (section.contains(key + ".Particle2.Type")) {
                                String type2 = section.getString(key + ".Particle2.Type");
                                if (type2 != null && !type2.isEmpty()) {
                                    particleType2 = type2.toUpperCase();
                                    particleCount2 = Math.min(section.getInt(key + ".Particle2.Count", 5), 10);  // Cap at 10
                                }
                            }
                            break;
                        }
                    }
                }
            }
        }
        
        // Spawn primary particles
        if (particleType1 != null && !particleType1.isEmpty()) {
            try {
                Particle particle = Particle.valueOf(particleType1);
                new SSJParticles(ssj, player, particle, particleCount1, 2).createParticles();
            } catch (IllegalArgumentException e) {
                ssj.getLogger().warning("Invalid particle type: " + particleType1);
            }
        }
        
        // Spawn secondary particles if defined
        if (particleType2 != null && !particleType2.isEmpty()) {
            try {
                Particle particle = Particle.valueOf(particleType2);
                new SSJParticles(ssj, player, particle, particleCount2, 2).createParticles();
            } catch (IllegalArgumentException e) {
                ssj.getLogger().warning("Invalid particle type: " + particleType2);
            }
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
        boolean currentState = scoreboardEnabled.getOrDefault(playerId, true);  // Default to true
        
        if (currentState) {
            // Disable scoreboard
            player.setScoreboard(ssj.getServer().getScoreboardManager().getNewScoreboard());
            scoreboardEnabled.put(playerId, false);
            player.sendMessage(ChatColor.GREEN + "Scoreboard removed!");
        } else {
            // Enable scoreboard
            ssj.getSSJMethods().callScoreboard(player);
            scoreboardEnabled.put(playerId, true);
            player.sendMessage(ChatColor.GREEN + "Scoreboard added!");
        }
        
        // Save the scoreboard state to player config
        ssj.getSSJPCM().setPlayerConfigValue(player, "Scoreboard_Enabled", !currentState);
    }

    // Add this method to initialize scoreboard state on join
    public void initializeScoreboardState(Player player) {
        boolean enabled = ssj.getSSJPCM().getPlayerConfig(player).getBoolean("Scoreboard_Enabled", true);
        scoreboardEnabled.put(player.getUniqueId(), enabled);
        
        if (enabled) {
            ssj.getSSJMethods().callScoreboard(player);
        } else {
            player.setScoreboard(ssj.getServer().getScoreboardManager().getNewScoreboard());
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
