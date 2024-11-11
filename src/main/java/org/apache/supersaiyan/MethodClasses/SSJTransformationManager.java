package org.apache.supersaiyan.MethodClasses;

import org.apache.supersaiyan.SSJ;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;

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
        
        // Get boss bar progress
        SSJBossBar bossBar = ssj.getSSJActionListeners().getBossBars().get(player.getUniqueId());
        if (bossBar != null) {
            int currentProgress = bossBar.getPlayerStats().getOrDefault(player.getUniqueId(), 0);
            return currentProgress >= 100;
        }
        
        return false;
    }
    
    @SuppressWarnings("deprecation")
    public void transform(Player player, String transformationId) {
        // Get transformation config section
        String path = getTransformationPath(transformationId);
        if (!ssj.getSSJConfigs().getTCFile().contains(path)) {
            return;
        }
        
        // Apply transformation
        ConfigurationSection transform = ssj.getSSJConfigs().getTCFile().getConfigurationSection(path);
        
        // Update player form
        ssj.getSSJPCM().setPlayerConfigValue(player, "Form", transform.getString("Desc"));
        
        // Apply traits using attribute modifiers
        ConfigurationSection traits = transform.getConfigurationSection("Traits");
        for (String trait : traits.getKeys(false)) {
            int level = traits.getInt(trait);
            applyAttributeModifier(player, trait, level);
        }
        
        // Apply particles
        String particleType = transform.getString("Particle.Type");
        int particleCount = transform.getInt("Particle.Count");
        if (!particleType.isEmpty() && particleCount > 0) {
            new SSJParticles(ssj, player, org.bukkit.Particle.valueOf(particleType.toUpperCase()), 
                           particleCount, 3).createParticles();
        }
        
        // Apply effects based on config
        if (ssj.getSSJConfigs().getLF()) {
            Location loc = player.getLocation();
            player.getWorld().spigot().strikeLightningEffect(loc, false);
        }
        if (ssj.getSSJConfigs().getEE()) {
            int radius = ssj.getSSJConfigs().getER();
            if (radius > 0) {
                player.getWorld().createExplosion(
                    player.getLocation(), 
                    radius,
                    false,
                    false,
                    player
                );
            }
        }
        if (ssj.getSSJConfigs().getSE()) {
            player.getWorld().playSound(player.getLocation(), org.bukkit.Sound.ENTITY_GHAST_SHOOT, 1.0f, 1.0f);
        }
        
        // Reset boss bar progress after transformation
        SSJBossBar bossBar = ssj.getSSJActionListeners().getBossBars().get(player.getUniqueId());
        if (bossBar != null) {
            bossBar.getPlayerStats().put(player.getUniqueId(), 0);
            bossBar.update(player);
        }
    }
    
    @SuppressWarnings("deprecation")
    public void detransform(Player player) {
        // Reset all attributes to default
        ssj.getSSJRpgSys().resetAllStatBoosts(player);
        
        // Reset form to base
        ssj.getSSJPCM().setPlayerConfigValue(player, "Form", "Base");
        
        // Reapply base stat boosts based on player's actual stats
        ssj.getSSJRpgSys().updateAllStatBoosts(player);
        
        // Apply effects based on config
        if (ssj.getSSJConfigs().getLF()) {
            Location loc = player.getLocation();
            player.getWorld().spigot().strikeLightningEffect(loc, false);
        }
        if (ssj.getSSJConfigs().getEE()) {
            int radius = ssj.getSSJConfigs().getER();
            if (radius > 0) {
                player.getWorld().createExplosion(
                    player.getLocation(), 
                    radius,
                    false,
                    false,
                    player
                );
            }
        }
        if (ssj.getSSJConfigs().getSE()) {
            player.getWorld().playSound(player.getLocation(), org.bukkit.Sound.ENTITY_GHAST_SHOOT, 1.0f, 1.0f);
        }
        
        // Update charging particles if player is charging
        if (ssj.getSSJChargeSystem().isCharging()) {
            ssj.getSSJChargeSystem().updateParticles(player);
        }
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
}
