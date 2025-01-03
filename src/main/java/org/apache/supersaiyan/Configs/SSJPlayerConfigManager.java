package org.apache.supersaiyan.Configs;

import org.apache.supersaiyan.SSJ;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class SSJPlayerConfigManager {

    private final File folder;
    private final SSJ ssj;
    private final Map<String, Object> defaultValues = new HashMap<>();
    public Map<UUID, Double> bpMultipliers = new HashMap<>();
    public Map<UUID, Double> energyGainMultipliers = new HashMap<>();
    public Map<UUID, Double> energyLimitMultipliers = new HashMap<>();
    public Map<UUID, Double> energyDrainMultipliers = new HashMap<>();
    public Map<UUID, Integer> transformationEnergyCosts = new HashMap<>();

    public SSJPlayerConfigManager(SSJ ssj, File folder) {
        this.folder = folder;
        this.ssj = ssj;
    }

    private void initializeDefaultValues(Player player) {
        defaultValues.clear(); // Clear any existing values
        defaultValues.put("Start", false);
        defaultValues.put("Player_Name", player.getName());
        defaultValues.put("See_Lightning_Effects", true);
        defaultValues.put("See_Explosion_Effects", true);
        defaultValues.put("Hear_Sound_Effects", true);
        defaultValues.put("Level", 0);
        defaultValues.put("Battle_Power", 0);
        defaultValues.put("Energy", 0);
        defaultValues.put("Saiyan_Ability.Level", 0);
        defaultValues.put("Form", "Base");
        defaultValues.put("Action_Points", ssj.getSSJConfigs().getSAP());
        int startingStatPoints = ssj.getSSJConfigs().getSSP();
        defaultValues.put("Base.Health", startingStatPoints);
        defaultValues.put("Base.Power", startingStatPoints);
        defaultValues.put("Base.Strength", startingStatPoints);
        defaultValues.put("Base.Speed", startingStatPoints);
        defaultValues.put("Base.Stamina", startingStatPoints);
        defaultValues.put("Base.Defence", startingStatPoints);
        defaultValues.put("Transformations", "");
        defaultValues.put("Staff_Flight", false);
        defaultValues.put("Saiyan_Ability.Enabled", true);

        // Debug log
        ssj.getLogger().info("Initializing default values for " + player.getName());
        ssj.getLogger().info("Starting Action Points: " + ssj.getSSJConfigs().getSAP());
        ssj.getLogger().info("Starting Stat Points: " + startingStatPoints);
    }

    public File getFile(Player player) {
        return new File(folder, player.getUniqueId() + ".yml");
    }

    public FileConfiguration getPlayerConfig(Player player) {
        var file = getFile(player);
        var config = YamlConfiguration.loadConfiguration(file);
        if (!file.exists()) {
            // Reset player stats to vanilla values first
            ssj.getSSJRpgSys().resetAllStatBoosts(player);
            
            // Initialize default values with the player
            initializeDefaultValues(player);
            defaultValues.forEach(config::set);  // Apply the default values to config
            savePlayerConfig(player, config);
        }
        return config;
    }

    public void savePlayerConfig(Player player, FileConfiguration config) {
        var file = getFile(player);
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Optional<Object> getPlayerConfigValue(Player player, String key) {
        var config = getPlayerConfig(player);
        return Optional.ofNullable(config.get(key)); // Ensure the method exists
    }

    public void setPlayerConfigValue(Player player, String path, Object value) {
        FileConfiguration config = getPlayerConfig(player);
        config.set(path, value);
        savePlayerConfig(player, config);
    }

    public void createUserCheck(final Player p) {
        if (!getFile(p).exists()) {
            onFirstSet(p);
        }
    }
    
    private void onFirstSet(Player p) {
        ssj.getLogger().warning(p.getName() + "'s.yml Doesn't exist! Creating one...");
        var config = getPlayerConfig(p);
        defaultValues.forEach(config::set);  // Apply the default values to config
        savePlayerConfig(p, config);
        ssj.getLogger().warning(p.getName() + "'s.yml has been created!");
    }

    public String getName(Player p) {
        return (String) getPlayerConfigValue(p, "Player_Name").orElse("");
    }

    public boolean getStart(Player p) {
        boolean started = (boolean) getPlayerConfigValue(p, "Start").orElse(false);
        if (!started) {
            ssj.getSSJRpgSys().resetAllStatBoosts(p);
        }
        return started;
    }

    public int getLevel(Player p) {
        return (int) getPlayerConfigValue(p, "Level").orElse(0);
    }

    public int getBattlePower(Player player) {
        return getPlayerConfig(player).getInt("Battle_Power", 0);
    }

    public int getBP(Player player) {
        return getBattlePower(player);
    }

    public int getEnergy(Player p) {
        return (int) getPlayerConfigValue(p, "Energy").orElse(0);
    }

    public int getSaiyanAbility(Player p) {
        return getPlayerConfig(p).getInt("Saiyan_Ability.Level", 0);
    }

    public String getForm(Player p) {
        return (String) getPlayerConfigValue(p, "Form").orElse("Base");
    }

    public int getActionPoints(Player p) {
        return (int) getPlayerConfigValue(p, "Action_Points").orElse(0);
    }

    public int getHealth(Player p) {
        return (int) getPlayerConfigValue(p, "Base.Health").orElse(0);
    }

    public int getPower(Player p) {
        return (int) getPlayerConfigValue(p, "Base.Power").orElse(0);
    }

    public int getStrength(Player p) {
        return (int) getPlayerConfigValue(p, "Base.Strength").orElse(0);
    }

    public int getSpeed(Player p) {
        return (int) getPlayerConfigValue(p, "Base.Speed").orElse(0);
    }

    public int getStamina(Player p) {
        return (int) getPlayerConfigValue(p, "Base.Stamina").orElse(0);
    }

    public int getDefence(Player p) {
        return (int) getPlayerConfigValue(p, "Base.Defence").orElse(0);
    }

    public int getLimit(Player p) {
        return getPower(p) * ssj.getSSJConfigs().getEML();
    }

    public String getTransformations(Player p) {
        return (String) getPlayerConfigValue(p, "Transformations").orElse("");
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
    
    public int getTransformationEnergyCost(Player player) {
        return transformationEnergyCosts.getOrDefault(player.getUniqueId(), 100);
    }

    public void setActionPoints(Player p, int points) {
        setPlayerConfigValue(p, "Action_Points", points);
    }

    public void addActionPoints(Player p, int points) {
        setPlayerConfigValue(p, "Action_Points", getActionPoints(p) + points);
    }

    public void setSaiyanAbility(Player p, int level) {
        setPlayerConfigValue(p, "Saiyan_Ability.Level", level);
    }

    public void setTransformations(Player p, String transforms) {
        setPlayerConfigValue(p, "Transformations", transforms);
    }

    public boolean hasSkill(Player player, String skillName) {
        return getPlayerConfig(player).getStringList("Unlocked_Skills").contains(skillName);
    }

    public void unlockSkill(Player player, String skillName) {
        FileConfiguration config = getPlayerConfig(player);
        List<String> unlockedSkills = config.getStringList("Unlocked_Skills");
        
        if (!unlockedSkills.contains(skillName)) {
            unlockedSkills.add(skillName);
            config.set("Unlocked_Skills", unlockedSkills);
            config.set("Skills." + skillName + ".Level", 1);
            config.set("Skills." + skillName + ".Enabled", true);
            
            try {
                savePlayerConfig(player, config);

                // Apply initial skill boosts if any
                ssj.getSSJSkillManager().applySkillBoosts(player, skillName);
            } catch (Exception e) {
                e.printStackTrace();
                player.sendMessage("§cError saving skill data!");
            }
        }
    }

    public int getSkillLevel(Player player, String skillName) {
        return getPlayerConfig(player).getInt("Skills." + skillName + ".Level", 0);
    }

    public void setSkillLevel(Player player, String skillName, int level) {
        FileConfiguration config = getPlayerConfig(player);
        config.set("Skills." + skillName + ".Level", level);
        savePlayerConfig(player, config);
    }

    public int getAbilityLevel(Player player, String abilityName) {
        return getPlayerConfig(player).getInt("Abilities." + abilityName + ".Level", 0);
    }
    
    public void setAbilityLevel(Player player, String abilityName, int level) {
        setPlayerConfigValue(player, "Abilities." + abilityName + ".Level", level);
    }

    public boolean isStaffFlightEnabled(Player p) {
        return (boolean) getPlayerConfigValue(p, "Staff_Flight").orElse(false);
    }

    public void setStaffFlightEnabled(Player p, boolean enabled) {
        setPlayerConfigValue(p, "Staff_Flight", enabled);
    }

    public boolean isSaiyanAbilityEnabled(Player p) {
        return getPlayerConfig(p).getBoolean("Saiyan_Ability.Enabled", true);
    }

    public void setSaiyanAbilityEnabled(Player p, boolean enabled) {
        setPlayerConfigValue(p, "Saiyan_Ability.Enabled", enabled);
    }

    public boolean isSkillEnabled(Player p, String skillName) {
        return getPlayerConfig(p).getBoolean("Skills." + skillName + ".Enabled", true);
    }

    public void setSkillEnabled(Player p, String skillName, boolean enabled) {
        setPlayerConfigValue(p, "Skills." + skillName + ".Enabled", enabled);

        // If the skill is being disabled, handle necessary actions
        if (!enabled) {
            ssj.getSSJSkillManager().handleSkillDisabled(p, skillName);
        }
    }

    public boolean getExplosionEffects(Player player) {
        return getPlayerConfig(player).getBoolean("See_Explosion_Effects", true);
    }

    public void setExplosionEffects(Player player, boolean enabled) {
        setPlayerConfigValue(player, "See_Explosion_Effects", enabled);
    }

    public boolean getLightningEffects(Player player) {
        return getPlayerConfig(player).getBoolean("See_Lightning_Effects", true);
    }

    public void setLightningEffects(Player player, boolean enabled) {
        setPlayerConfigValue(player, "See_Lightning_Effects", enabled);
    }

    public boolean getSoundEffects(Player player) {
        return getPlayerConfig(player).getBoolean("Hear_Sound_Effects", true);
    }

    public void setSoundEffects(Player player, boolean enabled) {
        setPlayerConfigValue(player, "Hear_Sound_Effects", enabled);
    }

    public boolean hasTransformation(Player player, String transformId) {
        String transformations = getTransformations(player);
        return transformations != null && transformations.contains(transformId);
    }
}