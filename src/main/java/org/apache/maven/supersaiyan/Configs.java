package org.apache.maven.supersaiyan;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class Configs{

    private SSJ ssj;

    public Configs(SSJ ssj) {
        this.ssj = ssj;
    }

    private File ConfigFile;

    private FileConfiguration Config;

    private File TConfigFile;

    private FileConfiguration TConfig;

    private File userFolder;

    public FileConfiguration getCFile() {

        return this.Config;

    }

    public FileConfiguration getTCFile() {

        return this.TConfig;

    }

    public void createConfig() {

        userFolder = new File(ssj.getDataFolder(), "PlayerConfigs");

        userFolder.mkdirs();

        ConfigFile = new File(ssj.getDataFolder(), "config.yml");

        Config = new YamlConfiguration();

        if (!ConfigFile.exists()) {

            ConfigFile.getParentFile().mkdirs();

            ssj.saveResource("config.yml", false);

        }

        try {

            Config.load(ConfigFile);

        } catch (IOException | InvalidConfigurationException e) {

            e.printStackTrace();

        }

    }

    public void createTConfig() {

        TConfigFile = new File(ssj.getDataFolder(), "transformations.yml");

        if (!TConfigFile.exists()) {

            TConfigFile.getParentFile().mkdirs();

            ssj.saveResource("transformations.yml", false);

        }

        TConfig = new YamlConfiguration();

        try {

            TConfig.load(TConfigFile);

        } catch (IOException | InvalidConfigurationException e) {

            e.printStackTrace();

        }

    }

    public void updateConfig(){

        if (getCFile().getDouble("Version") < Double.parseDouble(ssj.getDescription().getVersion())) {

            File configFile = new File(ssj.getDataFolder(), "config.yml");

            File tConfigFile = new File(ssj.getDataFolder(), "transformations.yml");

            configFile.delete();

            tConfigFile.delete();

            createConfig();

            createTConfig();

            ssj.saveDefaultConfig();

            ssj.reloadConfig();

            ssj.getLogger().warning("Config.yml has been updated!");

            ssj.getLogger().warning("Transformations.yml has been updated!");

        }

    }

    public void saveConfig() {

        try {

            Config.load(ConfigFile);

            TConfig.load(TConfigFile);

            Config.save(ConfigFile);

            TConfig.save(TConfigFile);

        } catch (IOException | InvalidConfigurationException e) {

            e.printStackTrace();
        }
    }
}