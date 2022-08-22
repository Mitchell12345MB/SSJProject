package org.apache.maven.supersaiyan.Configs;

import org.apache.maven.supersaiyan.SSJ;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

public class PlayerConfig {

    private final SSJ ssj;

    public PlayerConfig(SSJ ssj) {
        this.ssj = ssj;
    }

    public void callCreatePLayerConfig(Player e) {

        File pConfigFile;

        FileConfiguration pConfig;

        pConfigFile = new File(ssj.getDataFolder(), File.separator + "PlayerConfigs" + File.separator + e.getUniqueId() + ".yml");

        pConfig = new YamlConfiguration();

        if (!pConfigFile.exists()) {

            ssj.getLogger().warning(e.getName() + "'s.yml Doesn't exist! Creating one...");

            try {

                pConfigFile.createNewFile();

                pConfig.load(pConfigFile);

                pConfig.set("Player_Name", e.getName());

                pConfig.set("Start", false);

                pConfig.set("Level", 0);

                pConfig.set("Base.Health", 0);

                pConfig.set("Base.Power", 0);

                pConfig.set("Base.Strength", 0);

                pConfig.set("Base.Speed", 0);

                pConfig.set("Base.Stamina", 0);

                pConfig.set("Transformations_Unlocked", "");

                pConfig.save(pConfigFile);

                ssj.getLogger().warning(e.getName() + "'s.yml has been created!");

            } catch (IOException | InvalidConfigurationException ex) {

                ex.printStackTrace();

            }

        }

    }

    public void callSavePlayerConfig(Player e) {

        File pConfigFile;

        FileConfiguration pConfig;

        pConfigFile = new File(ssj.getDataFolder(), File.separator + "PlayerConfigs" + File.separator + e.getPlayer().getUniqueId() + ".yml");

        pConfig = new YamlConfiguration();

        if (!pConfigFile.exists()) {

            ssj.getLogger().warning(e.getPlayer().getName() + "'s.yml Doesn't exist! Creating one...");

            try {

                pConfigFile.createNewFile();

                pConfig.load(pConfigFile);

                pConfig.set("Player_Name", e.getPlayer().getName());

                pConfig.set("Start", false);

                pConfig.set("Level", 0);

                pConfig.set("Base.Health", 0);

                pConfig.set("Base.Power", 0);

                pConfig.set("Base.Strength", 0);

                pConfig.set("Base.Speed", 0);

                pConfig.set("Base.Stamina", 0);

                pConfig.set("Transformations_Unlocked", "");

                pConfig.save(pConfigFile);

                ssj.getLogger().warning(e.getPlayer().getName() + "'s.yml has been created!");

            } catch (IOException | InvalidConfigurationException ex) {

                ex.printStackTrace();

            }

        } else {

            try {

                pConfig.load(pConfigFile);

                pConfig.save(pConfigFile);

            } catch (IOException | InvalidConfigurationException ex) {

                ex.printStackTrace();

            }

        }

    }

    public void callUpdatePlayerName(Player e) {

        File pConfigFile;

        FileConfiguration pConfig;

        pConfigFile = new File(ssj.getDataFolder(), File.separator + "PlayerConfigs" + File.separator + e.getUniqueId() + ".yml");

        pConfig = new YamlConfiguration();

        if (pConfigFile.exists()) {

            try {

                pConfig.load(pConfigFile);

                if (!(e.getName().equals(pConfig.getString("Player_Name")))) {

                    pConfig.set("Player_Name", e.getName());

                    ssj.getLogger().warning(e.getName() + "'s.yml has been updated!");

                }

            } catch (IOException | InvalidConfigurationException ex) {

                ex.printStackTrace();

            }

        } else {

            ssj.getLogger().warning(e.getName() + "'s.yml Doesn't exist! Creating one...");

        }

    }

    public File getpConfigFile(final Player p) {

        File pConfigFile;

        pConfigFile = new File(ssj.getDataFolder(), File.separator + "PlayerConfigs" + File.separator + p.getUniqueId() + ".yml");

        return pConfigFile;

    }

    public FileConfiguration getpConfig(final Player p) {

        FileConfiguration pConfigFile;

        pConfigFile = new FileConfiguration() {
            @Override
            public String saveToString() {

                return File.separator + "PlayerConfigs" + File.separator + p.getUniqueId() + ".yml";

            }

            @Override
            public void loadFromString(String s){

                try {

                    this.load(File.separator + "PlayerConfigs" + File.separator + p.getUniqueId() + ".yml");

                } catch (IOException | InvalidConfigurationException xe) {

                    throw new RuntimeException(xe);

                }

            }

        };

        return pConfigFile;

    }

}
