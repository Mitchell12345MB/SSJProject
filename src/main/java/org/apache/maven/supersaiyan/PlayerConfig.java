package org.apache.maven.supersaiyan;

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

    private final SSJ ssj;
    private File pConfigFile;
    private FileConfiguration pConfig;

    public PlayerConfig(SSJ ssj) {

        this.ssj = ssj;

    }

    public FileConfiguration getPConfig(Player e) {

        return (FileConfiguration) this.pConfig.get(File.separator + "PlayerConfigs" + File.separator + e.getUniqueId() + ".yml");

    }

    @EventHandler
    private void createPConfig(AsyncPlayerPreLoginEvent e) {

        pConfigFile = new File(ssj.getDataFolder(), File.separator + "PlayerConfigs" + File.separator + e.getUniqueId() + ".yml");

        if (!pConfigFile.exists()) {

            pConfig = new YamlConfiguration();

            try {

                pConfigFile.createNewFile();

                pConfig.load(pConfigFile);

                pConfig.set("Player_Name", e.getName());

                pConfig.set("Level", 0);

                pConfig.set("Base.Health", 0);

                pConfig.set("Base.Power", 0);

                pConfig.set("Base.Strength", 0);

                pConfig.set("Base.Speed", 0);

                pConfig.set("Base.Stamina", 0);

                pConfig.set("Transformations_Unlocked", "");

                pConfig.save(pConfigFile);

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

            try {

                pConfigFile.createNewFile();

                pConfig.load(pConfigFile);

                pConfig.set("Player_Name", e.getPlayer().getName());

                pConfig.set("Level", 0);

                pConfig.set("Base.Health", 0);

                pConfig.set("Base.Power", 0);

                pConfig.set("Base.Strength", 0);

                pConfig.set("Base.Speed", 0);

                pConfig.set("Base.Stamina", 0);

                pConfig.set("Transformations_Unlocked", "");

                pConfig.save(pConfigFile);

            } catch (IOException | InvalidConfigurationException ex) {

                ex.printStackTrace();
            }
        }
    }
}
