package org.apache.supersaiyan.Configs;

import org.apache.supersaiyan.SSJ;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

public class SSJPlayerConfigManager {

    private final File folder;

    private final SSJ ssj;

    public SSJPlayerConfigManager(SSJ ssj, File folder) {

        this.folder = folder;

        this.ssj = ssj;

    }

    public File getFile(Player player) {

        return new File(folder, player.getUniqueId() + ".yml");

    }

    public FileConfiguration getPlayerConfig(Player player) {

        File file = getFile(player);

        return YamlConfiguration.loadConfiguration(file);

    }

    public void savePlayerConfig(Player player, FileConfiguration config) {

        File file = getFile(player);

        try {

            config.save(file);

        } catch (IOException e) {

            e.printStackTrace();

        }

    }

    public Object getPlayerConfigValue(Player player, String key) {

        FileConfiguration config = getPlayerConfig(player);

        return config.get(key);

    }

    public void setPlayerConfigValue(Player player, String key, Object value) {

        FileConfiguration config = getPlayerConfig(player);

        config.set(key, value);

        savePlayerConfig(player, config);

    }

    public void createUserCheck(final Player p) {

        if (!(getFile(p).exists())) {

            onFirstSet(p);

        }

    }

    private void onFirstSet(Player p) {

        ssj.getLogger().warning(p.getName() + "'s.yml Doesn't exist! Creating one...");

        getPlayerConfig(p);

        setPlayerConfigValue(p, "Player_Name", p.getName());

        setPlayerConfigValue(p,"Start", false);

        setPlayerConfigValue(p,"Level", 0);

        setPlayerConfigValue(p,"Battle_Power", 0);

        setPlayerConfigValue(p,"Energy", 0);

        setPlayerConfigValue(p,"Form", "Base");

        setPlayerConfigValue(p,"Action_Points", ssj.getSSJConfigs().getSAP());

        setPlayerConfigValue(p,"Base.Health", ssj.getSSJConfigs().getSAP());

        setPlayerConfigValue(p,"Base.Power", ssj.getSSJConfigs().getSAP());

        setPlayerConfigValue(p,"Base.Strength", ssj.getSSJConfigs().getSAP());

        setPlayerConfigValue(p,"Base.Speed", ssj.getSSJConfigs().getSAP());

        setPlayerConfigValue(p,"Base.Stamina", ssj.getSSJConfigs().getSAP());

        setPlayerConfigValue(p,"Base.Defence", ssj.getSSJConfigs().getSAP());

        setPlayerConfigValue(p,"Transformations_Unlocked", "");

        savePlayerConfig(p, getPlayerConfig(p));

        ssj.getLogger().warning(p.getName() + "'s.yml has been created!");

    }

    public String getName(Player p) { // Relates to the player's name in their config.

        return ((String) getPlayerConfigValue(p, "Player_Name"));

    }

    public int getLimit(Player p) { // Multiplies the player's Power stat with the default energy multiplier limit.

        return ((int) getPlayerConfigValue(p, "Base.Power")) * ssj.getSSJConfigs().getEML();

    }

    public int getHealth(Player p) { //Relates to how much health the player has.

        return ((int) getPlayerConfigValue(p,"Base.Health"));

    }

    public int getPower(Player p) { //Relates to how much energy the player can store.

        return ((int) getPlayerConfigValue(p,"Base.Power"));

    }

    public int getStrength(Player p) { //Relates to player's damage out-put.

        return ((int) getPlayerConfigValue(p,"Base.Strength"));

    }

    public int getSpeed(Player p) { //Relates to player's speed.

        return ((int) getPlayerConfigValue(p,"Base.Speed"));

    }

    public int getStamina(Player p) { //Relates to how long the player can hold a form or how long the player takes to transform.

        return ((int) getPlayerConfigValue(p,"Base.Stamina"));

    }

    public int getDefence(Player p) { //Relates to mow much damage is mitigated to the player.

        return ((int) getPlayerConfigValue(p,"Base.Defence"));

    }

    public int getEnergy(Player p) { //Gets how much energy the player has. (energy is set by

        return ((int) getPlayerConfigValue(p,"Energy"));

    }

    public int getBP(Player p) { // Gets the player's current Battle power.

        return ((int) getPlayerConfigValue(p,"Battle_Power"));

    }

    public int getAP(Player p) { //Relates to how many "action points" has. The player can spend action points on the stats below.

        return ((int) getPlayerConfigValue(p,"Action_Points"));

    }

    public int getLevel(Player p) { //Gets the player's level.

        return ((int) getPlayerConfigValue(p,"Level"));

    }

    public String getForm(Player p) { //Gets the player's current form they are in.

        return ((String) getPlayerConfigValue(p,"Form"));

    }

    public String getTransformations(Player p) { //Gets the current transformations the player has unlocked.

        return ((String) getPlayerConfigValue(p,"Transformations_Unlocked"));
    }
}