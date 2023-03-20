package org.apache.supersaiyan.Configs;

import org.apache.supersaiyan.SSJ;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

public class SSJPlayerConfigManager {

    private final File folder;

    private final SSJ ssj;

    public SSJPlayerConfigManager(SSJ ssj, File folder) {

        this.folder = folder;

        this.ssj = ssj;

    }

    public File getFile(Player player) {

        return new File(folder, player.getUniqueId() + ".yml");

    }

    public FileConfiguration getPlayerConfig(Player player) {

        File file = getFile(player);

        return YamlConfiguration.loadConfiguration(file);

    }

    public void savePlayerConfig(Player player, FileConfiguration config) {

        File file = getFile(player);

        try {

            config.save(file);

        } catch (IOException e) {

            e.printStackTrace();

        }

    }

    public Object getPlayerConfigValue(Player player, String key) {

        FileConfiguration config = getPlayerConfig(player);

        return config.get(key);

    }

    public void setPlayerConfigValue(Player player, String key, Object value) {

        FileConfiguration config = getPlayerConfig(player);

        config.set(key, value);

        savePlayerConfig(player, config);

    }

    public void createUserCheck(final Player p) {

        if (!(getFile(p).exists())) {

            onFirstSet(p);

        }

    }

    private void onFirstSet(Player p) {

        ssj.getLogger().warning(p.getName() + "'s.yml Doesn't exist! Creating one...");

        getPlayerConfig(p);

        setPlayerConfigValue(p, "Player_Name", p.getName());

        setPlayerConfigValue(p,"Start", false);

        setPlayerConfigValue(p,"Level", 0);

        setPlayerConfigValue(p,"Battle_Power", 0);

        setPlayerConfigValue(p,"Energy", 0);

        setPlayerConfigValue(p,"Form", "Base");

        setPlayerConfigValue(p,"Action_Points", ssj.getSSJConfigs().getSAP());

        setPlayerConfigValue(p,"Base.Health", ssj.getSSJConfigs().getSAP());

        setPlayerConfigValue(p,"Base.Power", ssj.getSSJConfigs().getSAP());

        setPlayerConfigValue(p,"Base.Strength", ssj.getSSJConfigs().getSAP());

        setPlayerConfigValue(p,"Base.Speed", ssj.getSSJConfigs().getSAP());

        setPlayerConfigValue(p,"Base.Stamina", ssj.getSSJConfigs().getSAP());

        setPlayerConfigValue(p,"Base.Defence", ssj.getSSJConfigs().getSAP());

        setPlayerConfigValue(p,"Transformations_Unlocked", "");

        savePlayerConfig(p, getPlayerConfig(p));

        ssj.getLogger().warning(p.getName() + "'s.yml has been created!");

    }

    public String getName(Player p) {

        return ((String) getPlayerConfigValue(p, "Player_Name"));

    }

    public int getLimit(Player p) {

        return ((int) getPlayerConfigValue(p, "Base.Power")) * ssj.getSSJConfigs().getCFile().getInt("Limit_Energy_Multiplier");

    }

    public int getHealth(Player p) {

        return ((int) getPlayerConfigValue(p,"Base.Health"));

    }

    public int getPower(Player p) {

        return ((int) getPlayerConfigValue(p,"Base.Power"));

    }

    public int getStrength(Player p) {

        return ((int) getPlayerConfigValue(p,"Base.Strength"));

    }

    public int getSpeed(Player p) {

        return ((int) getPlayerConfigValue(p,"Base.Speed"));

    }

    public int getStamina(Player p) {

        return ((int) getPlayerConfigValue(p,"Base.Stamina"));

    }

    public int getDefence(Player p) {

        return ((int) getPlayerConfigValue(p,"Base.Defence"));

    }

    public int getEnergy(Player p) {

        return ((int) getPlayerConfigValue(p,"Energy"));

    }

    public int getBaseBP(Player p) {

        return getHealth(p) * getPower(p) + getStrength(p) * getSpeed(p) + getStamina(p) * getDefence(p);

    }

    public int getBP(Player p) {

        return ((int) getPlayerConfigValue(p,"Battle_Power"));

    }

    public int getAP(Player p) {

        return ((int) getPlayerConfigValue(p,"Action_Points"));

    }

    public int getLevel(Player p) {

        return ((int) getPlayerConfigValue(p,"Level"));

    }

    public String getForm(Player p) {

        return ((String) getPlayerConfigValue(p,"Form"));

    }

    public String getTransformations(Player p) {

        return ((String) getPlayerConfigValue(p,"Transformations_Unlocked"));
    }
}