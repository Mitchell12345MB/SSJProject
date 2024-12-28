package org.apache.supersaiyan.MethodClasses;

import org.apache.supersaiyan.SSJ;
import org.bukkit.entity.Player;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SSJPotentialSystem {
    private final SSJ ssj;
    private final Map<UUID, SSJBossBar> potentialBars;

    public SSJPotentialSystem(SSJ ssj) {
        this.ssj = ssj;
        this.potentialBars = new HashMap<>();
    }

    public void updatePotential(Player player) {
        // Get current energy percentage
        double energyPercentage = ssj.getSSJEnergyManager().getCurrentEnergyPercentage(player);
        
        // Calculate potential multiplier (maxes out at 50% energy)
        double potentialMultiplier = Math.min(energyPercentage / 50.0, 1.0);
        
        // Apply potential boost to stats
        applyPotentialBoosts(player, potentialMultiplier);
        
        // Update battle power
        ssj.getSSJRpgSys().multBP(player);

        // Update potential bar
        updatePotentialBar(player, potentialMultiplier);
    }

    private void updatePotentialBar(Player player, double potentialMultiplier) {
        UUID playerId = player.getUniqueId();
        
        // Create or get the potential bar
        SSJBossBar potentialBar = potentialBars.computeIfAbsent(playerId, 
            k -> new SSJBossBar(ssj, "§6Potential: 0%", new HashMap<>(), true));
        
        // Convert potential multiplier to percentage (0-100)
        int potentialPercentage = (int)(potentialMultiplier * 100.0);
        
        // Reset progress to 0 and add new progress
        potentialBar.removeProgress(player, 100); // Remove any existing progress
        potentialBar.addProgress(player, potentialPercentage);
        
        // Show the bar
        potentialBar.show(player);
    }

    private void applyPotentialBoosts(Player player, double potentialMultiplier) {
        // Base potential boost is 20% at max potential (50% energy)
        double maxBoost = 0.2;
        double currentBoost = maxBoost * potentialMultiplier;
        
        // Apply boost to all stats
        ssj.getSSJEnergyManager().setBPMultiplier(player, 1.0 + currentBoost);
        
        // Update player's stats
        ssj.getSSJRpgSys().updateAllStatBoosts(player);
    }

    public void hidePotentialBar(Player player) {
        UUID playerId = player.getUniqueId();
        if (potentialBars.containsKey(playerId)) {
            potentialBars.get(playerId).hide(player);
        }
    }

    public void cleanup() {
        // Clean up all boss bars when plugin disables
        for (SSJBossBar bar : potentialBars.values()) {
            bar.cleanup();
        }
        potentialBars.clear();
    }
} 