package org.apache.maven.supersaiyan.Configs;

import org.apache.maven.supersaiyan.SSJ;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.io.File;
import java.io.IOException;

public class PlayerConfig implements Listener {

    private SSJ ssj;

    public PlayerConfig(SSJ ssj) {
        this.ssj = ssj;
    }

    private File pConfigFile;

    private FileConfiguration pConfig;

    @EventHandler
    private void createPConfig(AsyncPlayerPreLoginEvent e) {

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

    @EventHandler
    public void savePCOnLeave(PlayerQuitEvent e) {

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

        }

    }

    @EventHandler
    private void updatePlayerName(AsyncPlayerPreLoginEvent e) {

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
}
