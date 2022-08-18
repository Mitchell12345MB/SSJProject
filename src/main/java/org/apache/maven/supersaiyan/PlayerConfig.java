package org.apache.maven.supersaiyan;

import lombok.Getter;
import org.apache.logging.log4j.Level;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

import static org.apache.logging.log4j.LogManager.getLogger;

@Getter
public class PlayerConfig implements Listener {

    private Main main = Main.getInstance();

    File userFile;

    private HashMap<UUID, YamlConfiguration> usersConfig = new HashMap<>();

    @EventHandler
    public void onPlayerPreLogin(AsyncPlayerPreLoginEvent e) {

        File folder = new File("plugins/SuperSaiyan", "PlayerConfigs");

        userFile = new File(main.getDataFolder().toString()+ File.separatorChar + "PlayerConfigs" + File.separatorChar + e.getUniqueId().toString() + ".yml");

        if(!folder.exists()) {

            folder.mkdirs();
        }

        if (!userFile.exists()) {

            try {

                userFile.createNewFile();

            } catch (IOException ex) {

                getLogger().log(Level.WARN, "Can't create " + e.getName() + " user file");

                ex.printStackTrace();

            }

        }

        usersConfig.put(e.getUniqueId(), YamlConfiguration.loadConfiguration(userFile));
    }
}
