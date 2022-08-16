package org.apache.maven.supersaiyan;

import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.UUID;

@Getter
public class PlayerConfig {

    private Main main = Main.getInstance();

    Commands c;

    UUID u;

    File userFile;

    FileConfiguration userConfig;


    public void createUser(final Player player){

        if ( !(userFile.exists()) ) {

            try {

                YamlConfiguration userConfig = YamlConfiguration.loadConfiguration(userFile);

                userFile.createNewFile();

                userConfig.set("User.Info.PreviousName", player.getName());

                userConfig.set("User.Info.UniqueID", player.getUniqueId().toString());

                userConfig.set("User.Info.ipAddress", player.getAddress().getAddress().getHostAddress());

                userConfig.save(userFile);

            } catch (Exception e) {

                e.printStackTrace();

            }
        }
    }

    public void userDataHandler(UUID u){

        this.u = u;

        userFile = new File(main.getDataFolder(), u + ".yml");

        userConfig = YamlConfiguration.loadConfiguration(userFile);
    }

    public FileConfiguration getUserFile(){

        return userConfig;

    }

    public void saveUserFile() {

        try {

            getUserFile().save(userFile);

        } catch (Exception e) {

            e.printStackTrace();

        }
    }

}
