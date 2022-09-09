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

        if (!(userFile.exists())) {

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

            getUserConfig().load(userFile);

        } catch (IOException | InvalidConfigurationException xp) {

            xp.printStackTrace();

        }

    }

    public void saveUserFile() {

        try {

            getUserConfig().save(userFile);

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    private void onFirstSet(Player p) {

        try {

            int sap = ssj.getSSJCConfigs().getCFile().getInt("Starting_Action_Points");

            ssj.getLogger().warning(p.getName() + "'s.yml Doesn't exist! Creating one...");

            YamlConfiguration UserConfig = YamlConfiguration.loadConfiguration(userFile);

            UserConfig.set("Player_Name", p.getName());

            UserConfig.set("Start", false);

            UserConfig.set("Level", 0);

            UserConfig.set("Battle_Power", 0);

            UserConfig.set("Energy", 0);

            UserConfig.set("Form", "Base");

            UserConfig.set("Action_Points", sap);

            UserConfig.set("Base.Health", sap);

            UserConfig.set("Base.Power", sap);

            UserConfig.set("Base.Strength", sap);

            UserConfig.set("Base.Speed", sap);

            UserConfig.set("Base.Stamina", sap);

            UserConfig.set("Base.Defence", sap);

            UserConfig.set("Transformations_Unlocked", "");

            UserConfig.save(userFile);

            ssj.getLogger().warning(p.getName() + "'s.yml has been created!");

        } catch (IOException ex) {

            ex.printStackTrace();
        }
    }
}