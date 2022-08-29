package org.apache.maven.supersaiyan.Configs;

import org.apache.maven.supersaiyan.SSJ;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

public class PlayerConfig {

    private final SSJ ssj;

    public PlayerConfig(SSJ ssj) {
        this.ssj = ssj;
    }

    File pConfigFile;

    YamlConfiguration pConfig;

    public void callCreatePLayerConfig(Player p) {

        pConfigFile = new File(ssj.getDataFolder(), File.separator + "PlayerConfigs" + File.separator + p.getUniqueId() + ".yml");

        pConfig = new YamlConfiguration();

        if (!pConfigFile.exists()) {

            onFirstSet(p);

        }

    }

    public void callSavePlayerConfig(Player p) {

        pConfigFile = new File(ssj.getDataFolder(), File.separator + "PlayerConfigs" + File.separator + p.getUniqueId() + ".yml");

        pConfig = new YamlConfiguration();

        if (!pConfigFile.exists()) {

            onFirstSet(p);

        } else {

            try {

                pConfig.load(pConfigFile);

                pConfig.save(pConfigFile);

            } catch (IOException | InvalidConfigurationException ex) {

                ex.printStackTrace();

            }

        }

    }

    public void callUpdatePlayerName(Player p) {

        pConfigFile = new File(ssj.getDataFolder(), File.separator + "PlayerConfigs" + File.separator + p.getUniqueId() + ".yml");

        pConfig = new YamlConfiguration();

        if (pConfigFile.exists()) {

            try {

                pConfig.load(pConfigFile);

                if (!(p.getName().equals(pConfig.getString("Player_Name")))) {

                    pConfig.set("Player_Name", p.getName());

                    pConfig.save(pConfigFile);

                    ssj.getLogger().warning(p.getName() + "'s.yml has been updated!");

                }

            } catch (IOException | InvalidConfigurationException ex) {

                ex.printStackTrace();

            }

        } else {

            onFirstSet(p);

        }

    }

    public File getpConfigFile(final Player p) {

        pConfigFile = new File(ssj.getDataFolder(), File.separator + "PlayerConfigs" + File.separator + p.getUniqueId() + ".yml");

        return pConfigFile;

    }

    public YamlConfiguration getpConfig(final Player p) {

        return pConfig;
    }

    private void onFirstSet(Player p) {

        pConfigFile = new File(ssj.getDataFolder(), File.separator + "PlayerConfigs" + File.separator + p.getUniqueId() + ".yml");

        pConfig = new YamlConfiguration();

        try {

            ssj.getLogger().warning(p.getName() + "'s.yml Doesn't exist! Creating one...");

            pConfigFile.createNewFile();

            pConfig.load(pConfigFile);

            pConfig.set("Player_Name", p.getName());

            pConfig.set("Start", false);

            pConfig.set("Level", 0);

            pConfig.set("Battle_Power", 0);

            pConfig.set("Action_Points", ssj.getCConfigs().getCFile().getInt("Starting_Action_Points"));

            pConfig.set("Base.Health", ssj.getCConfigs().getCFile().getInt("Starting_Attribute_Points"));

            pConfig.set("Base.Power", ssj.getCConfigs().getCFile().getInt("Starting_Attribute_Points"));

            pConfig.set("Base.Strength", ssj.getCConfigs().getCFile().getInt("Starting_Attribute_Points"));

            pConfig.set("Base.Speed", ssj.getCConfigs().getCFile().getInt("Starting_Attribute_Points"));

            pConfig.set("Base.Stamina", ssj.getCConfigs().getCFile().getInt("Starting_Attribute_Points"));

            pConfig.set("Base.Defence", ssj.getCConfigs().getCFile().getInt("Starting_Attribute_Points"));

            pConfig.set("Transformations_Unlocked", "");

            pConfig.save(pConfigFile);

            ssj.getLogger().warning(p.getName() + "'s.yml has been created!");

        } catch (IOException | InvalidConfigurationException ex) {

            ex.printStackTrace();
        }
    }
}