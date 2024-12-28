package org.apache.supersaiyan.MethodClasses;

import org.apache.supersaiyan.SSJ;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.Particle;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.PacketType;
import org.bukkit.entity.EntityType;
import java.util.UUID;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.ChatColor;

public class SSJTransformationManager {
    private final SSJ ssj;
    
    public SSJTransformationManager(SSJ ssj) {
        this.ssj = ssj;
    }
    
    public boolean canTransform(Player player, String transformationId) {
        String unlockedTransforms = ssj.getSSJPCM().getTransformations(player);
        String currentForm = ssj.getSSJPCM().getForm(player);
        
        // First check if transformation is unlocked
        if (!unlockedTransforms.contains(transformationId)) {
            return false;
        }
        
        // If player is in base form, allow first transformation
        if (currentForm.equals("Base")) {
            return true;
        }
        
        // Get transformation config section
        String path = getTransformationPath(transformationId);
        if (!ssj.getSSJConfigs().getTCFile().contains(path)) {
            return false;
        }
        
        ConfigurationSection transform = ssj.getSSJConfigs().getTCFile().getConfigurationSection(path);
        
        // **Check if required abilities/skills are enabled**

        // **1. Saiyan Ability**
        if (transform.contains("Saiyan_Ability_Lock")) {
            int saiyanAbilityLock = transform.getInt("Saiyan_Ability_Lock");
            int playerSaiyanAbilityLevel = ssj.getSSJPCM().getSaiyanAbility(player);

            if (playerSaiyanAbilityLevel < saiyanAbilityLock) {
                player.sendMessage("§cYour Saiyan Ability level is too low to use this transformation!");
                return false;
            }

            // Check if Saiyan Ability is enabled in settings
            if (!ssj.getSSJPCM().isSaiyanAbilityEnabled(player)) {
                player.sendMessage("§cSaiyan Ability is disabled in your settings!");
                return false;
            }
        }

        // **2. Potential Skill**
        if (transform.contains("Potential_Skill_Lock")) {
            int potentialSkillLock = transform.getInt("Potential_Skill_Lock");
            int playerPotentialSkillLevel = ssj.getSSJSkillManager().getSkillLevel(player, "Potential");

            if (playerPotentialSkillLevel < potentialSkillLock) {
                player.sendMessage("§cYour Potential skill level is too low to use this transformation!");
                return false;
            }

            // Check if Potential skill is enabled in settings
            if (!ssj.getSSJPCM().isSkillEnabled(player, "Potential")) {
                player.sendMessage("§cPotential skill is disabled in your settings!");
                return false;
            }
        }

        // **3. Kaioken Ability**
        if (transform.contains("Kaioken_Ability_Lock")) {
            int kaiokenAbilityLock = transform.getInt("Kaioken_Ability_Lock");
            int playerKaiokenLevel = ssj.getSSJSkillManager().getSkillLevel(player, "Kaioken");

            if (playerKaiokenLevel < kaiokenAbilityLock) {
                player.sendMessage("§cYour Kaioken ability level is too low to use this transformation!");
                return false;
            }

            // Check if Kaioken skill is enabled in settings
            if (!ssj.getSSJPCM().isSkillEnabled(player, "Kaioken")) {
                player.sendMessage("§cKaioken skill is disabled in your settings!");
                return false;
            }
        }

        // **4. God Ability**
        if (transform.contains("God_Ability_Lock")) {
            int godAbilityLock = transform.getInt("God_Ability_Lock");
            int playerGodAbilityLevel = ssj.getSSJSkillManager().getSkillLevel(player, "God");

            if (playerGodAbilityLevel < godAbilityLock) {
                player.sendMessage("§cYour God ability level is too low to use this transformation!");
                return false;
            }

            // Check if God skill is enabled in settings
            if (!ssj.getSSJPCM().isSkillEnabled(player, "God")) {
                player.sendMessage("§cGod skill is disabled in your settings!");
                return false;
            }
        }
        
        // Existing Level Lock check
        int playerLevel = ssj.getSSJPCM().getLevel(player);
        int levelLock = transform.getInt("Level_Lock", 0);
        if (playerLevel < levelLock) {
            player.sendMessage("§cYou need to be level " + levelLock + " to use this transformation!");
            return false;
        }
        
        // All checks passed
        return true;
    }
    
    public void transform(Player player, String transformationId) {
        String path = getTransformationPath(transformationId);
        if (!ssj.getSSJConfigs().getTCFile().contains(path)) {
            return;
        }
        
        ConfigurationSection transform = ssj.getSSJConfigs().getTCFile().getConfigurationSection(path);
        if (transform == null) {
            return;
        }
        
        // Add after path check, before applying effects
        ConfigurationSection traits = transform.getConfigurationSection("Traits");
        if (traits != null) {
            int transformationCost = traits.getInt("TRANSFORMATION_ENERGY_COST", 100);
            int currentEnergy = ssj.getSSJPCM().getEnergy(player);
            
            if (currentEnergy < transformationCost) {
                player.sendMessage("§cYou need " + transformationCost + " energy to transform!");
                return;
            }
            
            // Deduct energy cost
            ssj.getSSJEnergyManager().modifyEnergy(player, -transformationCost);
        }
        
        // Apply base transformation effects
        String formName = transform.getString("Desc", "Base");
        ssj.getSSJPCM().setPlayerConfigValue(player, "Form", formName);
        
        // Apply multipliers from traits
        if (traits != null) {
            double bpMultiplier = traits.getDouble("BP_MULTIPLIER", 1.0);
            double energyGainMultiplier = traits.getDouble("ENERGY_GAIN_MULTIPLIER", 1.0);
            double energyLimitMultiplier = traits.getDouble("ENERGY_LIMIT_MULTIPLIER", 1.0);
            double energyDrainMultiplier = traits.getDouble("ENERGY_DRAIN_MULTIPLIER", 1.0);
            int transformationEnergyCost = traits.getInt("TRANSFORMATION_ENERGY_COST", 100);
            
            ssj.getSSJEnergyManager().setMultipliers(
                player,
                bpMultiplier,
                energyGainMultiplier,
                energyLimitMultiplier,
                energyDrainMultiplier,
                transformationEnergyCost
            );
            
            // Update BP immediately after setting multipliers
            ssj.getSSJRpgSys().multBP(player);
            
            // Apply traits using attribute modifiers
            for (String trait : traits.getKeys(false)) {
                if (!trait.equals("BP_MULTIPLIER") && 
                    !trait.equals("ENERGY_GAIN_MULTIPLIER") && 
                    !trait.equals("ENERGY_LIMIT_MULTIPLIER") && 
                    !trait.equals("ENERGY_DRAIN_MULTIPLIER") && 
                    !trait.equals("TRANSFORMATION_ENERGY_COST")) {
                        
                    int level = traits.getInt(trait);
                    applyAttributeModifier(player, trait, level);
                }
            }
        }
        
        // Add lightning effects for specific forms
        if (formName.equals("Super Saiyan 2") || 
            formName.equals("Super Saiyan 3") || 
            formName.equals("Super Saiyan 4") || 
            formName.equals("Super Saiyan 5") || 
            formName.equals("Super Saiyan God") || 
            formName.equals("Super Saiyan Blue") || 
            formName.equals("Super Saiyan Rose") || 
            formName.equals("Super Saiyan Rage") || 
            formName.equals("Super Saiyan Blue Evolution") || 
            (formName.equals("Kaioken Transformation") && 
             (transformationId.equals("x50") || transformationId.equals("x100")))) {
            new SSJParticles(ssj, player, Particle.WAX_OFF, 10, 3).createLightningEffect();
        }
        
        // Apply particles
        String particleType1 = transform.getString("Particle.Type");
        int particleCount1 = transform.getInt("Particle.Count");

        String particleType2 = transform.getString("Particle2.Type");
        int particleCount2 = transform.getInt("Particle2.Count");

        // Spawn the first particle type if specified
        if (particleType1 != null && !particleType1.isEmpty() && particleCount1 > 0) {
            new SSJParticles(ssj, player, Particle.valueOf(particleType1.toUpperCase()), particleCount1, 4).createParticles();
        }

        // Spawn the second particle type if specified
        if (particleType2 != null && !particleType2.isEmpty() && particleCount2 > 0) {
            new SSJParticles(ssj, player, Particle.valueOf(particleType2.toUpperCase()), particleCount2, 5).createParticles();
        }
        
        // Apply effects based on player's settings
        if (ssj.getSSJPCM().getLightningEffects(player)) {
            // Apply lightning effect
            sendLightningEffect(player);
        }
        if (ssj.getSSJPCM().getExplosionEffects(player)) {
            // Apply explosion effect
            createExplosionEffect(player);
        }
        if (ssj.getSSJPCM().getSoundEffects(player)) {
            // Apply sound effect
            player.playSound(
                player.getLocation(),
                org.bukkit.Sound.ENTITY_GHAST_SHOOT,
                1.0f,
                1.0f
            );
        }
        
        // Reset boss bar progress after transformation
        SSJBossBar bossBar = ssj.getSSJActionListeners().getBossBars().get(player.getUniqueId());
        if (bossBar != null) {
            bossBar.getPlayerStats().put(player.getUniqueId(), 0);
            bossBar.update(player);
        }
        
        // Update charging particles if player is charging
        if (ssj.getSSJChargeSystem().isCharging(player)) {
            ssj.getSSJChargeSystem().updateParticles(player);
        }
        
        // After setting multipliers
        ssj.getSSJEnergyManager().startEnergyDrain(player, "transformation");

        // Recalculate BP after applying transformation multipliers
        ssj.getSSJRpgSys().multBP(player);

        // Update the scoreboard
        ssj.getSSJMethodChecks().checkScoreboard();
        ssj.getSSJMethods().callScoreboard(player);
    }
    
    public void detransform(Player player) {
        String currentForm = ssj.getSSJPCM().getForm(player);
        if (currentForm.equals("Base")) {
            return;
        }

        // Get current energy percentage before resetting multipliers
        double currentPercentage = ssj.getSSJEnergyManager().getCurrentEnergyPercentage(player);
        
        // Reset all multipliers
        ssj.getSSJEnergyManager().resetMultipliers(player);
        
        // Set form back to base
        ssj.getSSJPCM().setPlayerConfigValue(player, "Form", "Base");
        
        // Scale energy to maintain the same percentage in base form
        int newLimit = ssj.getSSJEnergyManager().getEnergyLimit(player);
        int newEnergy = (int) (newLimit * currentPercentage);
        ssj.getSSJPCM().setPlayerConfigValue(player, "Energy", newEnergy);
        
        // Reset stats and stop energy drain
        ssj.getSSJRpgSys().resetAllStatBoosts(player);
        ssj.getSSJEnergyManager().stopAllEnergyDrains(player);

        // Reapply base stats
        ssj.getSSJRpgSys().updateAllStatBoosts(player);

        // Apply effects based on player's settings
        if (ssj.getSSJPCM().getLightningEffects(player)) {
            // Apply lightning effect
            sendLightningEffect(player);
        }
        if (ssj.getSSJPCM().getExplosionEffects(player)) {
            // Apply explosion effect
            createExplosionEffect(player);
        }
        if (ssj.getSSJPCM().getSoundEffects(player)) {
            // Apply sound effect
            player.playSound(
                player.getLocation(),
                org.bukkit.Sound.ENTITY_GHAST_SHOOT,
                1.0f,
                1.0f
            );
        }
        
        // Update charging particles if player is charging
        if (ssj.getSSJChargeSystem().isCharging(player)) {
            ssj.getSSJChargeSystem().updateParticles(player);
        }

        // Update BP
        ssj.getSSJRpgSys().multBP(player);
    }
    
    private void applyAttributeModifier(Player player, String trait, int level) {
        // Get base stats first
        int baseHealth = ssj.getSSJPCM().getHealth(player);
        int baseStrength = ssj.getSSJPCM().getStrength(player);
        int baseSpeed = ssj.getSSJPCM().getSpeed(player);
        int baseDefense = ssj.getSSJPCM().getDefence(player);
        
        // Apply max stats limit if enabled
        if (ssj.getSSJConfigs().getMaxStatsLimit()) {
            int maxStats = ssj.getSSJConfigs().getMaxStats();
            baseHealth = Math.min(baseHealth, maxStats);
            baseStrength = Math.min(baseStrength, maxStats);
            baseSpeed = Math.min(baseSpeed, maxStats);
            baseDefense = Math.min(baseDefense, maxStats);
        }

        switch(trait.toUpperCase()) {
            case "SPEED":
                AttributeInstance speed = player.getAttribute(Attribute.MOVEMENT_SPEED);
                if (speed != null) {
                    double baseSpeedBoost = 0.1 + (baseSpeed * 0.01);
                    double transformSpeedBoost = level * 0.01;
                    speed.setBaseValue(Math.min(baseSpeedBoost + transformSpeedBoost, 0.5));
                }
                break;
            case "INCREASE_DAMAGE":
                AttributeInstance damage = player.getAttribute(Attribute.ATTACK_DAMAGE);
                if (damage != null) {
                    double baseDamageBoost = 2 + (baseStrength * 0.5);
                    double transformDamageBoost = level * 0.5;
                    damage.setBaseValue(Math.min(baseDamageBoost + transformDamageBoost, 20));
                }
                break;
            case "DAMAGE_RESISTANCE":
                AttributeInstance armor = player.getAttribute(Attribute.ARMOR);
                AttributeInstance toughness = player.getAttribute(Attribute.ARMOR_TOUGHNESS);
                if (armor != null && toughness != null) {
                    double baseArmorBoost = baseDefense * 2;
                    double baseToughnessBoost = baseDefense;
                    double transformArmorBoost = level * 2;
                    double transformToughnessBoost = level;
                    armor.setBaseValue(Math.min(baseArmorBoost + transformArmorBoost, 30));
                    toughness.setBaseValue(Math.min(baseToughnessBoost + transformToughnessBoost, 20));
                }
                break;
            case "HEALTH_BOOST":
                AttributeInstance health = player.getAttribute(Attribute.MAX_HEALTH);
                if (health != null) {
                    double baseHealthBoost = 20 + (baseHealth * 2);
                    double transformHealthBoost = level * 2;
                    health.setBaseValue(Math.min(baseHealthBoost + transformHealthBoost, 100));
                }
                break;
        }
    }
    
    public String getTransformationPath(String transformId) {
        String[] categories = {"Base_Forms", "Kaioken_Forms", "Saiyan_Forms", 
                             "Legendary_Saiyan_Forms", "Saiyan_God_Forms"};
        
        for (String category : categories) {
            for (String key : ssj.getSSJConfigs().getTCFile().getConfigurationSection(category).getKeys(false)) {
                if (ssj.getSSJConfigs().getTCFile().getString(category + "." + key + ".TransformationID").equals(transformId)) {
                    return category + "." + key;
                }
            }
        }
        return null;
    }
    
    // Check if the player can use a specific transformation based on enabled abilities/skills
    private boolean canUseTransformation(Player player, ConfigurationSection transform) {
        // Check if Saiyan Ability is required and enabled
        if (transform.contains("Saiyan_Ability_Lock") && !ssj.getSSJPCM().isSaiyanAbilityEnabled(player)) {
            return false;
        }
        // Check if Kaioken Skill is required and enabled
        if (transform.contains("Kaioken_Ability_Lock") && !ssj.getSSJPCM().isSkillEnabled(player, "Kaioken")) {
            return false;
        }
        // Check if Potential Skill is required and enabled
        if (transform.contains("Potential_Skill_Lock") && !ssj.getSSJPCM().isSkillEnabled(player, "Potential")) {
            return false;
        }
        // Check if God Skill is required and enabled
        if (transform.contains("God_Ability_Lock") && !ssj.getSSJPCM().isSkillEnabled(player, "God")) {
            return false;
        }
        return true;
    }

    public String getNextTransformation(Player player, String currentForm) {
        String[] categories = {"Base_Forms", "Kaioken_Forms", "Saiyan_Forms", 
                               "Legendary_Saiyan_Forms", "Saiyan_God_Forms"};

        String unlockedTransforms = ssj.getSSJPCM().getTransformations(player);

        // For base form or no form, find first unlocked transformation
        if (currentForm.equals("Base") || currentForm.isEmpty()) {
            return findFirstUnlockedTransform(categories, unlockedTransforms, player);
        }

        boolean foundCurrent = false;

        // Iterate through categories
        for (String category : categories) {
            ConfigurationSection section = ssj.getSSJConfigs().getTCFile().getConfigurationSection(category);
            if (section != null) {
                List<String> keys = new ArrayList<>(section.getKeys(false));
                for (String key : keys) {
                    String desc = section.getString(key + ".Desc");
                    ConfigurationSection transform = section.getConfigurationSection(key);

                    // Skip transformations that are not unlocked
                    String transformId = transform.getString("TransformationID");
                    if (!unlockedTransforms.contains(transformId)) {
                        continue;
                    }

                    // Check if required abilities/skills are enabled
                    if (!canUseTransformation(player, transform)) {
                        continue;
                    }

                    // If we've found the current form, set the flag to true
                    if (!foundCurrent && desc != null && desc.equals(currentForm)) {
                        foundCurrent = true;
                        continue;
                    }

                    // Once the current form is found, the next valid transformation is returned
                    if (foundCurrent) {
                        return transformId;
                    }
                }
            }
        }

        // No further transformation available
        return null;
    }

    private String findFirstUnlockedTransform(String[] categories, String unlockedTransforms, Player player) {
        for (String category : categories) {
            ConfigurationSection section = ssj.getSSJConfigs().getTCFile().getConfigurationSection(category);
            if (section != null) {
                for (String key : section.getKeys(false)) {
                    ConfigurationSection transform = section.getConfigurationSection(key);
                    String transformId = transform.getString("TransformationID");

                    if (!unlockedTransforms.contains(transformId)) {
                        continue;
                    }

                    if (!canUseTransformation(player, transform)) {
                        continue;
                    }

                    return transformId;
                }
            }
        }
        return null;
    }

    public void reapplyCurrentForm(Player player) {
        String currentForm = ssj.getSSJPCM().getForm(player);
        if (currentForm.equals("Base")) {
            return;
        }

        // Find the current transformation's config section
        String path = null;
        String[] categories = {"Base_Forms", "Kaioken_Forms", "Saiyan_Forms", 
                              "Legendary_Saiyan_Forms", "Saiyan_God_Forms"};
        
        for (String category : categories) {
            ConfigurationSection section = ssj.getSSJConfigs().getTCFile().getConfigurationSection(category);
            if (section != null) {
                for (String key : section.getKeys(false)) {
                    String desc = section.getString(key + ".Desc");
                    if (currentForm.equals(desc)) {
                        path = category + "." + key;
                        break;
                    }
                }
            }
            if (path != null) break;
        }

        if (path != null) {
            ConfigurationSection transform = ssj.getSSJConfigs().getTCFile().getConfigurationSection(path);
            ConfigurationSection traits = transform.getConfigurationSection("Traits");
            
            if (traits != null) {
                double bpMultiplier = traits.getDouble("BP_MULTIPLIER", 1.0);
                double energyGainMultiplier = traits.getDouble("ENERGY_GAIN_MULTIPLIER", 1.0);
                double energyLimitMultiplier = traits.getDouble("ENERGY_LIMIT_MULTIPLIER", 1.0);
                double energyDrainMultiplier = traits.getDouble("ENERGY_DRAIN_MULTIPLIER", 1.0);
                int transformationEnergyCost = traits.getInt("TRANSFORMATION_ENERGY_COST", 100);
                
                ssj.getSSJEnergyManager().setMultipliers(
                    player,
                    bpMultiplier,
                    energyGainMultiplier,
                    energyLimitMultiplier,
                    energyDrainMultiplier,
                    transformationEnergyCost
                );
                
                // Reapply attribute modifiers
                ssj.getSSJRpgSys().resetAllStatBoosts(player);
                ssj.getSSJRpgSys().updateAllStatBoosts(player);
                
                // Update BP
                ssj.getSSJRpgSys().multBP(player);
            }
        }
    }

    public void revertToBase(Player player) {
        // Reset all multipliers
        ssj.getSSJEnergyManager().resetMultipliers(player);

        // Set form back to base
        ssj.getSSJPCM().setPlayerConfigValue(player, "Form", "Base");

        // Reset stats
        ssj.getSSJRpgSys().resetAllStatBoosts(player);
        ssj.getSSJRpgSys().updateAllStatBoosts(player);

        // Recalculate BP after resetting stats
        ssj.getSSJRpgSys().multBP(player);

        // Update the scoreboard
        ssj.getSSJMethodChecks().checkScoreboard();
        ssj.getSSJMethods().callScoreboard(player);

        // Update charging particles if player is charging
        if (ssj.getSSJChargeSystem().isCharging(player)) {
            ssj.getSSJChargeSystem().updateParticles(player);
        }

        // Stop all energy drains since the player is back to base form
        ssj.getSSJEnergyManager().stopEnergyDrain(player, "transformation");
        ssj.getSSJEnergyManager().stopAllEnergyDrains(player);
    }

    public void applyTransformationEffects(Player player) {
        // Existing code...

        // Replace global lightning effect with per-player effect
        if (ssj.getSSJPCM().getLightningEffects(player)) {
            sendLightningEffect(player);
        }

        // Existing code...
    }

    public void sendLightningEffect(Player player) {
        Location loc = player.getLocation();
        
        // Create lightning packet
        PacketContainer lightningPacket = ProtocolLibrary.getProtocolManager()
            .createPacket(PacketType.Play.Server.SPAWN_ENTITY);
        
        int entityId = (int) Math.floor(Math.random() * Integer.MAX_VALUE);
        lightningPacket.getIntegers().write(0, entityId);
        lightningPacket.getUUIDs().write(0, UUID.randomUUID());
        lightningPacket.getDoubles()
            .write(0, loc.getX())
            .write(1, loc.getY())
            .write(2, loc.getZ());
        lightningPacket.getEntityTypeModifier().write(0, EntityType.LIGHTNING_BOLT);
        lightningPacket.getBytes()
            .write(0, (byte) 0)
            .write(1, (byte) 0);

        // Send to all players in range who have effects enabled
        for (Player nearbyPlayer : loc.getWorld().getPlayers()) {
            if (nearbyPlayer.getLocation().distance(loc) <= 50) { // 50 block radius
                if (ssj.getSSJPCM().getLightningEffects(nearbyPlayer)) {
                    try {
                        ProtocolLibrary.getProtocolManager().sendServerPacket(nearbyPlayer, lightningPacket);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public void createExplosionEffect(Player player) {
        Location loc = player.getLocation();
        
        // Show explosion effect to nearby players who have effects enabled
        for (Player nearbyPlayer : loc.getWorld().getPlayers()) {
            if (nearbyPlayer.getLocation().distance(loc) <= 50) { // 50 block radius
                if (ssj.getSSJPCM().getExplosionEffects(nearbyPlayer)) {
                    nearbyPlayer.spawnParticle(Particle.FLASH, loc, 1);
                    if (ssj.getSSJPCM().getSoundEffects(nearbyPlayer)) {
                        nearbyPlayer.playSound(loc, org.bukkit.Sound.ENTITY_GENERIC_EXPLODE, 1.0f, 1.0f);
                    }
                }
            }
        }
    }

    public boolean canUnlockTransformation(Player player, String transformationId) {
        String path = getTransformationPath(transformationId);
        if (!ssj.getSSJConfigs().getTCFile().contains(path)) {
            return false;
        }
        
        ConfigurationSection transform = ssj.getSSJConfigs().getTCFile().getConfigurationSection(path);
        if (transform == null) {
            return false;
        }

        // Check if already unlocked
        String unlockedTransforms = ssj.getSSJPCM().getTransformations(player);
        if (unlockedTransforms.contains(transformationId)) {
            player.sendMessage(ChatColor.RED + "You have already unlocked this transformation!");
            return false;
        }

        // Check level requirement
        int playerLevel = ssj.getSSJPCM().getLevel(player);
        int levelLock = transform.getInt("Level_Lock", 0);
        if (playerLevel < levelLock) {
            player.sendMessage(ChatColor.RED + "You need to be level " + levelLock + " to unlock this transformation!");
            return false;
        }

        // Check Saiyan Ability requirement
        if (transform.contains("Saiyan_Ability_Lock")) {
            int saiyanAbilityLock = transform.getInt("Saiyan_Ability_Lock");
            int playerSaiyanAbilityLevel = ssj.getSSJPCM().getSaiyanAbility(player);
            if (playerSaiyanAbilityLevel < saiyanAbilityLock) {
                player.sendMessage(ChatColor.RED + "Your Saiyan Ability level is too low! You need level " + saiyanAbilityLock + "!");
                return false;
            }
        }

        // Check Kaioken Ability requirement
        if (transform.contains("Kaioken_Ability_Lock")) {
            int kaiokenAbilityLock = transform.getInt("Kaioken_Ability_Lock");
            int playerKaiokenLevel = ssj.getSSJSkillManager().getSkillLevel(player, "Kaioken");
            if (playerKaiokenLevel < kaiokenAbilityLock) {
                player.sendMessage(ChatColor.RED + "Your Kaioken ability level is too low! You need level " + kaiokenAbilityLock + "!");
                return false;
            }
        }

        // Check God Ability requirement
        if (transform.contains("God_Ability_Lock")) {
            int godAbilityLock = transform.getInt("God_Ability_Lock");
            int playerGodAbilityLevel = ssj.getSSJSkillManager().getSkillLevel(player, "God");
            if (playerGodAbilityLevel < godAbilityLock) {
                player.sendMessage(ChatColor.RED + "Your God ability level is too low! You need level " + godAbilityLock + "!");
                return false;
            }
        }

        // Check Action Points requirement
        if (transform.contains("Acion_Points_Cost")) {
            int actionPointsCost = transform.getInt("Acion_Points_Cost");
            int playerActionPoints = ssj.getSSJPCM().getActionPoints(player);
            if (playerActionPoints < actionPointsCost) {
                player.sendMessage(ChatColor.RED + "You need " + actionPointsCost + " Action Points to unlock this transformation!");
                return false;
            }
        }

        return true;
    }

    public void unlockTransformation(Player player, String transformationId) {
        if (!canUnlockTransformation(player, transformationId)) {
            return;
        }

        String path = getTransformationPath(transformationId);
        ConfigurationSection transform = ssj.getSSJConfigs().getTCFile().getConfigurationSection(path);
        
        // Deduct Action Points if required
        if (transform.contains("Acion_Points_Cost")) {
            int actionPointsCost = transform.getInt("Acion_Points_Cost");
            int currentPoints = ssj.getSSJPCM().getActionPoints(player);
            ssj.getSSJPCM().setActionPoints(player, currentPoints - actionPointsCost);
        }

        // Add transformation to player's unlocked list using the correct method
        String currentTransforms = ssj.getSSJPCM().getTransformations(player);
        String newTransforms = currentTransforms.isEmpty() ? transformationId : currentTransforms + "," + transformationId;
        ssj.getSSJPCM().setTransformations(player, newTransforms);

        // Send success message
        String transformName = transform.getString("Desc", "Unknown Transformation");
        player.sendMessage(ChatColor.GREEN + "You have unlocked the " + transformName + " transformation!");
        
        // Play unlock effects
        player.playSound(player.getLocation(), org.bukkit.Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
        player.spawnParticle(Particle.WAX_ON, player.getLocation().add(0, 1, 0), 50, 0.5, 1, 0.5, 0.1);
    }

    public void handleTransformationUnlockClick(Player player, String transformationId) {
        if (canUnlockTransformation(player, transformationId)) {
            unlockTransformation(player, transformationId);
        }
    }

    public void removeTransformation(Player player, String transformId) {
        // Get current transformations
        String currentTransforms = ssj.getSSJPCM().getTransformations(player);
        
        // Remove the transformation
        String newTransforms = currentTransforms.replace(transformId, "").trim();
        
        // Update player's transformations
        ssj.getSSJPCM().setTransformations(player, newTransforms);
        
        // If player is currently in this form, revert to base
        if (ssj.getSSJPCM().getForm(player).contains(transformId)) {
            revertToBase(player);
        }
        
        // Update scoreboard
        ssj.getSSJMethodChecks().checkScoreboard();
        ssj.getSSJMethods().callScoreboard(player);
        
        player.sendMessage("§cTransformation " + transformId + " has been removed!");
    }
}
