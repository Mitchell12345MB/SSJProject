package org.apache.supersaiyan.Configs;

import org.apache.supersaiyan.SSJ;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class SSJConfigs {

    private final SSJ ssj;

    private File ConfigFile;

    private FileConfiguration Config;

    private File TConfigFile;

    private FileConfiguration TConfig;

    private File SConfigFile;

    private FileConfiguration SConfig;

    public SSJConfigs(SSJ ssj) {

        this.ssj = ssj;

    }

    public void createConfig() {

        File userFolder = new File(ssj.getDataFolder(), "PlayerConfigs");

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

    public void createSConfig() {

        SConfigFile = new File(ssj.getDataFolder(), "skills.yml");

        if (!SConfigFile.exists()) {

            SConfigFile.getParentFile().mkdirs();

            ssj.saveResource("skills.yml", false);

        }

        SConfig = new YamlConfiguration();

        try {

            SConfig.load(SConfigFile);

        } catch (IOException | InvalidConfigurationException e) {

            e.printStackTrace();

        }

    }

    public void updateConfigs(){

        if (getDVr() < Double.parseDouble(ssj.getDescription().getVersion())) {

            File configFile = new File(ssj.getDataFolder(), "config.yml");

            File tConfigFile = new File(ssj.getDataFolder(), "transformations.yml");

            File sConfigFile = new File(ssj.getDataFolder(), "skills.yml");

            configFile.delete();

            tConfigFile.delete();

            sConfigFile.delete();

            createConfig();

            createTConfig();

            createSConfig();

            ssj.saveDefaultConfig();

            ssj.reloadConfig();

            ssj.getLogger().warning("Config.yml has been updated!");

            ssj.getLogger().warning("Transformations.yml has been updated!");

            ssj.getLogger().warning("skills.yml has been updated!");

        }

    }

    public void loadConfigs() {

        loadConfig();

        loadTConfig();

        loadSConfig();

    }

    public void loadTConfig() {

        try {

            TConfig.load(TConfigFile);

        } catch (IOException | InvalidConfigurationException xp) {

            xp.printStackTrace();

        }

    }

    public void loadSConfig() {

        try {

            SConfig.load(SConfigFile);

        } catch (IOException | InvalidConfigurationException xp) {

            xp.printStackTrace();

        }

    }

    public void loadConfig() {

        try {

            Config.load(ConfigFile);

        } catch (IOException | InvalidConfigurationException xp) {

            xp.printStackTrace();
        }
    }

    public void saveConfigs() {

        saveConfig();

        saveTConfig();

        saveSConfig();

    }

    public void saveTConfig() {

        try {

            loadTConfig();

            TConfig.save(TConfigFile);

        } catch (IOException xp) {

            xp.printStackTrace();

        }

    }

    public void saveSConfig() {

        try {

            loadTConfig();

            SConfig.save(SConfigFile);

        } catch (IOException xp) {

            xp.printStackTrace();

        }

    }

    public void saveConfig() {

        try {

            loadConfig();

            Config.save(ConfigFile);

        } catch (IOException xp) {

            xp.printStackTrace();
        }
    }

    public FileConfiguration getCFile() {

        return this.Config;

    }

    public FileConfiguration getTCFile() {

        return this.TConfig;

    }

    public FileConfiguration getSCFile() {

        return this.SConfig;

    }

    private double getDVr() {

        return ssj.getSSJConfigs().getCFile().getDouble("Version");

    }

    //Config.yml content

    public int getVr() { //Gets the plugin's current version from the config.

        return ssj.getSSJConfigs().getCFile().getInt("Version");

    }

    public boolean getTR() { //Gets if the server admin wants to enable or disable the removal of the player's transformation when the player teleports via command or plugin.

        return ssj.getSSJConfigs().getCFile().getBoolean("Teleportation_Removal");

    }

    public boolean getLF() { //Gets if the server admin wants to enable or disable the lightning effect when the player(s) transform.

        return ssj.getSSJConfigs().getCFile().getBoolean("Explosion_Effect");

    }

    public boolean getEE() { //Gets if the server admin wants to enable or disable the explosion effect when the player(s) transform.

        return ssj.getSSJConfigs().getCFile().getBoolean("Explosion_Effect");

    }

    public int getER() { //Gets the default explosion radius from the config.

        return ssj.getSSJConfigs().getCFile().getInt("Explosion_Radius");

    }

    public boolean getSE() { //Gets if the server admin wants to enable or disable the sound effect when the player(s) transform.

        return ssj.getSSJConfigs().getCFile().getBoolean("Sound_Effect");

    }

    public int getSAP() { //Gets the default starting actions points from the config.

        return ssj.getSSJConfigs().getCFile().getInt("Starting_Action_Points");

    }

    public int getSSP() { //Gets the default starting stat points from the config.

        return ssj.getSSJConfigs().getCFile().getInt("Starting_Stat_Points");

    }

    public int getBPM() { //Gets the default base battle power multiplier from the config.

        return ssj.getSSJConfigs().getCFile().getInt("Base_Battle_Power_Multiplier");

    }

    public int getNPEMG() { //Gets the default non-passive (action) energy multiplier gain from the config.

        return ssj.getSSJConfigs().getCFile().getInt("NGain_Energy_Multiplier");

    }

    public int getPEMG() { //Gets the default passive energy multiplier gain from the config.

        return ssj.getSSJConfigs().getCFile().getInt("PGain_Energy_Multiplier");

    }

    public int getEML() { //Gets the default energy multiplier limit from the config.

        return ssj.getSSJConfigs().getCFile().getInt("Energy_Multiplier_Limit");

    }

    //transformations.yml stuff

    public List getBaseForms() {

        return ssj.getSSJConfigs().getTCFile().getList("Base_Forms");

    }

    public List getKaiokenForms() {

        return ssj.getSSJConfigs().getTCFile().getList("Kaioken_Forms");

    }

    public List getSaiyanForms() {

        return ssj.getSSJConfigs().getTCFile().getList("Saiyan_Forms");

    }

    public List getSaiyanGodForms() {

        return ssj.getSSJConfigs().getTCFile().getList("Saiyan_God_Forms");

    }

    public List getSaiyanGodKaiokenForms() {

        return ssj.getSSJConfigs().getTCFile().getList("Kaioken_and_Saiyan_God_Forms");

    }

}