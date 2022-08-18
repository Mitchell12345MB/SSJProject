package org.apache.maven.supersaiyan;

import lombok.Getter;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

@Getter
public class Configs{

    private Main main = Main.getInstance();

    private File ConfigFile;

    private FileConfiguration Config;

    private File TConfigFile;

    private FileConfiguration TConfig;

    File userFolder;

    public FileConfiguration getCFile() {

        return this.Config;

    }

    public FileConfiguration getTCFile() {

        return this.TConfig;

    }

    public void createConfig() {

        userFolder = new File(main.getDataFolder(), "PlayerConfigs");

        userFolder.mkdirs();

        ConfigFile = new File(main.getDataFolder(), "config.yml");

        Config = new YamlConfiguration();

        if (!ConfigFile.exists()) {

            ConfigFile.getParentFile().mkdirs();

            main.saveResource("config.yml", false);

        }

        try {

            Config.load(ConfigFile);

        } catch (IOException | InvalidConfigurationException e) {

            e.printStackTrace();

        }

    }

    public void createTConfig() {

        TConfigFile = new File(main.getDataFolder(), "transformations.yml");

        if (!TConfigFile.exists()) {

            TConfigFile.getParentFile().mkdirs();

            main.saveResource("transformations.yml", false);

        }

        TConfig = new YamlConfiguration();

        try {

            TConfig.load(TConfigFile);

        } catch (IOException | InvalidConfigurationException e) {

            e.printStackTrace();

        }

    }

    public void updateConfig(){

        if (getCFile().getDouble("Version") < Double.parseDouble(Main.getInstance().getDescription().getVersion())) {

            File configFile = new File(main.getDataFolder(), "config.yml");

            File tConfigFile = new File(main.getDataFolder(), "transformations.yml");

            configFile.delete();

            tConfigFile.delete();

            createConfig();

            createTConfig();

            main.saveDefaultConfig();

            main.reloadConfig();

            main.getLogger().warning("Config.yml has been updated!");

            main.getLogger().warning("Transformations.yml has been updated!");

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