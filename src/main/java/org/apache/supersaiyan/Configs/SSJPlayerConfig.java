package org.apache.supersaiyan.Configs;

import org.apache.supersaiyan.SSJ;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class SSJPlayerConfig {

    private final SSJ ssj;

    public SSJPlayerConfig(SSJ ssj) {
        this.ssj = ssj;
    }

    UUID u;

    File userFile;

    FileConfiguration userConfig;

    public SSJPlayerConfig(SSJ ssj, UUID u) {

        this.ssj = ssj;

        this.u = u;

        userFile = new File(ssj.getDataFolder(), File.separator + "PlayerConfigs" + File.separator + u + ".yml");

        userConfig = YamlConfiguration.loadConfiguration(userFile);

    }

    public void createUserCheck(final Player p) {

        if (!(getUserFile().exists())) {

            onFirstSet(p);

        }

    }

    public FileConfiguration getUserConfig() {

        return userConfig;

    }

    public File getUserFile() {

        return userFile;

    }

    public void loadUserFile(){

        try {

            getUserConfig().load(getUserFile());

        } catch (IOException | InvalidConfigurationException xp) {

            xp.printStackTrace();

        }

    }

    public void saveUserFile() {

        try {

            getUserConfig().save(getUserFile());

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    private void onFirstSet(Player p) {

        try {

            ssj.getLogger().warning(p.getName() + "'s.yml Doesn't exist! Creating one...");

            YamlConfiguration UserConfig = YamlConfiguration.loadConfiguration(getUserFile());

            UserConfig.set("Player_Name", p.getName());

            UserConfig.set("Start", false);

            UserConfig.set("Level", 0);

            UserConfig.set("Battle_Power", 0);

            UserConfig.set("Energy", 0);

            UserConfig.set("Form", "Base");

            UserConfig.set("Action_Points", ssj.getSSJConfigs().getSAP());

            UserConfig.set("Base.Health", ssj.getSSJConfigs().getSAP());

            UserConfig.set("Base.Power", ssj.getSSJConfigs().getSAP());

            UserConfig.set("Base.Strength", ssj.getSSJConfigs().getSAP());

            UserConfig.set("Base.Speed", ssj.getSSJConfigs().getSAP());

            UserConfig.set("Base.Stamina", ssj.getSSJConfigs().getSAP());

            UserConfig.set("Base.Defence", ssj.getSSJConfigs().getSAP());

            UserConfig.set("Transformations_Unlocked", "");

            UserConfig.save(getUserFile());

            ssj.getLogger().warning(p.getName() + "'s.yml has been created!");

        } catch (IOException ex) {

            ex.printStackTrace();
        }
    }

    public int getLimit() {

        return getUserConfig().getInt("Base.Power") * ssj.getSSJConfigs().getCFile().getInt("Limit_Energy_Multiplier");

    }

    public int getHealth() {

        return getUserConfig().getInt("Base.Health");

    }

    public int getPower() {

        return getUserConfig().getInt("Base.Power");

    }

    public int getStrength() {

        return getUserConfig().getInt("Base.Strength");

    }

    public int getSpeed() {

        return getUserConfig().getInt("Base.Speed");

    }

    public int getStamina() {

        return getUserConfig().getInt("Base.Stamina");

    }

    public int getDefence() {

        return getUserConfig().getInt("Base.Defence");

    }

    public int getEnergy() {

        return getUserConfig().getInt("Energy");

    }

    public int getBaseBP() {

        return getHealth() * getPower() + getStrength() * getSpeed() + getStamina() * getDefence();

    }

    public int getBP() {

        return getUserConfig().getInt("Battle_Power");

    }

    public int getAP() {

        return getUserConfig().getInt("Action_Points");

    }

    public int getLevel() {

        return getUserConfig().getInt("Level");

    }

    public String getForm() {

        return getUserConfig().getString("Form");

    }

    public String getTransformations() {

        return getUserConfig().getString("Transformations_Unlocked");
    }
}