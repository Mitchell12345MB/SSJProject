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
        
        // Check Level Lock
        int playerLevel = ssj.getSSJPCM().getLevel(player);
        int levelLock = transform.getInt("Level_Lock", 0);
        if (playerLevel < levelLock) {
            player.sendMessage("§cYou need to be level " + levelLock + " to use this transformation!");
            return false;
        }
        
        // Check Saiyan Ability Lock
        int playerSaiyanAbility = ssj.getSSJPCM().getSaiyanAbility(player);
        int saiyanAbilityLock = transform.getInt("Saiyan_Ability_Lock", 0);
        if (playerSaiyanAbility < saiyanAbilityLock) {
            player.sendMessage("§cYou need a Saiyan Ability level of " + saiyanAbilityLock + " to use this transformation!");
            return false;
        }
        
        // Kaioken Skill Lock
        if (transform.contains("Kaioken_Ability_Lock")) {
            if (!ssj.getSSJPCM().hasSkill(player, "Kaioken")) {
                player.sendMessage("§cYou need to unlock the Kaioken skill to use this transformation!");
                return false;
            }
        }
        
        // God Skill Lock
        if (transform.contains("God_Ability_Lock")) {
            if (!ssj.getSSJPCM().hasSkill(player, "God")) {
                player.sendMessage("§cYou need to unlock the God skill to use this transformation!");
                return false;
            }
        }
        
        // Potential Skill Lock
        if (transform.contains("Potential_Skill_Lock")) {
            if (!ssj.getSSJPCM().hasSkill(player, "Potential")) {
                player.sendMessage("§cYou need to unlock the Potential skill to use this transformation!");
                return false;
            }
        }
        
        // Get boss bar progress using the correct method
        SSJBossBar bossBar = ssj.getSSJActionListeners().getBossBars().get(player.getUniqueId());
        if (bossBar != null) {
            int currentProgress = bossBar.getProgress(player);
            return currentProgress >= 100;
        }
        
        return false;
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
        String particleType = transform.getString("Particle.Type");
        int particleCount = transform.getInt("Particle.Count");
        if (!particleType.isEmpty() && particleCount > 0) {
            new SSJParticles(ssj, player, org.bukkit.Particle.valueOf(particleType.toUpperCase()), 
                           particleCount, 3).createParticles();
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
        ssj.getSSJMethodChecks().scoreBoardCheck();
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

        switch(trait.toUpperCase()) {
            case "SPEED":
                AttributeInstance speed = player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED);
                if (speed != null) {
                    double baseSpeedBoost = 0.1 + (baseSpeed * 0.01);
                    double transformSpeedBoost = level * 0.01;
                    speed.setBaseValue(Math.min(baseSpeedBoost + transformSpeedBoost, 0.5));
                }
                break;
            case "INCREASE_DAMAGE":
                AttributeInstance damage = player.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE);
                if (damage != null) {
                    double baseDamageBoost = 2 + (baseStrength * 0.5);
                    double transformDamageBoost = level * 0.5;
                    damage.setBaseValue(Math.min(baseDamageBoost + transformDamageBoost, 20));
                }
                break;
            case "DAMAGE_RESISTANCE":
                AttributeInstance armor = player.getAttribute(Attribute.GENERIC_ARMOR);
                AttributeInstance toughness = player.getAttribute(Attribute.GENERIC_ARMOR_TOUGHNESS);
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
                AttributeInstance health = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
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
    
    public String getNextTransformation(Player player, String currentForm) {
        String[] categories = {"Base_Forms", "Kaioken_Forms", "Saiyan_Forms", 
                             "Legendary_Saiyan_Forms", "Saiyan_God_Forms"};
        
        String unlockedTransforms = ssj.getSSJPCM().getTransformations(player);
        
        // For base form or no form, find first unlocked transformation
        if (currentForm.equals("Base") || currentForm.isEmpty()) {
            return findFirstUnlockedTransform(categories, unlockedTransforms);
        }
        
        // Find current form in categories
        for (String category : categories) {
            ConfigurationSection section = ssj.getSSJConfigs().getTCFile().getConfigurationSection(category);
            if (section != null) {
                for (String key : section.getKeys(false)) {
                    String desc = section.getString(key + ".Desc");
                    if (desc != null && desc.equals(currentForm)) {
                        // Found current form, get next form in sequence
                        String nextId = findNextFormInCategory(section, key, unlockedTransforms);
                        if (nextId != null) {
                            return nextId;
                        }
                        // If no next form in current category, check next category
                        return findNextCategoryTransform(categories, category, unlockedTransforms);
                    }
                }
            }
        }
        return null;
    }

    private String findFirstUnlockedTransform(String[] categories, String unlockedTransforms) {
        for (String category : categories) {
            ConfigurationSection section = ssj.getSSJConfigs().getTCFile().getConfigurationSection(category);
            if (section != null) {
                for (String key : section.getKeys(false)) {
                    String transformId = section.getString(key + ".TransformationID");
                    if (transformId != null && unlockedTransforms.contains(transformId)) {
                        return transformId;
                    }
                }
            }
        }
        return null;
    }

    private String findNextCategoryTransform(String[] categories, String currentCategory, String unlockedTransforms) {
        boolean foundCurrentCategory = false;
        for (String category : categories) {
            if (foundCurrentCategory) {
                ConfigurationSection section = ssj.getSSJConfigs().getTCFile().getConfigurationSection(category);
                if (section != null) {
                    for (String key : section.getKeys(false)) {
                        String transformId = section.getString(key + ".TransformationID");
                        if (transformId != null && unlockedTransforms.contains(transformId)) {
                            return transformId;
                        }
                    }
                }
            }
            if (category.equals(currentCategory)) {
                foundCurrentCategory = true;
            }
        }
        return null;
    }

    private String findNextFormInCategory(ConfigurationSection section, String currentKey, String unlockedTransforms) {
        boolean foundCurrent = false;
        for (String key : section.getKeys(false)) {
            if (foundCurrent) {
                String nextId = section.getString(key + ".TransformationID");
                if (nextId != null && unlockedTransforms.contains(nextId)) {
                    return nextId;
                }
            }
            if (key.equals(currentKey)) {
                foundCurrent = true;
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
        ssj.getSSJMethodChecks().scoreBoardCheck();
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

        PacketContainer lightningPacket = ProtocolLibrary.getProtocolManager()
            .createPacket(PacketType.Play.Server.SPAWN_ENTITY);

        // Set a unique entity ID
        int entityId = (int) Math.floor(Math.random() * Integer.MAX_VALUE);
        lightningPacket.getIntegers().write(0, entityId); // Entity ID

        // Set UUID for the entity
        lightningPacket.getUUIDs().write(0, UUID.randomUUID());

        // Set the position of the entity
        lightningPacket.getDoubles()
            .write(0, loc.getX())
            .write(1, loc.getY())
            .write(2, loc.getZ());

        // Set the entity type to Lightning Bolt
        lightningPacket.getEntityTypeModifier().write(0, EntityType.LIGHTNING_BOLT);

        // Set pitch and yaw (if required)
        lightningPacket.getBytes()
            .write(0, (byte) 0) // Pitch
            .write(1, (byte) 0); // Yaw

        try {
            ProtocolLibrary.getProtocolManager().sendServerPacket(player, lightningPacket);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createExplosionEffect(Player player) {
        Location loc = player.getLocation();
        player.spawnParticle(Particle.FLASH, loc, 1);
        player.playSound(loc, org.bukkit.Sound.ENTITY_GENERIC_EXPLODE, 1.0f, 1.0f);
    }
}
