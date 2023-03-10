package org.apache.supersaiyan;

import org.apache.supersaiyan.Configs.SSJConfigs;
import org.apache.supersaiyan.Configs.SSJPlayerConfigManager;
import org.apache.supersaiyan.Listeners.SSJListeners;
import org.apache.supersaiyan.MethodClasses.*;
import org.apache.supersaiyan.SSJCommands.SSJCommands;
import org.bukkit.Bukkit;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class SSJ extends JavaPlugin {

    private SSJConfigs ssjconfigs;

    private SSJGui ssjgui;

    private SSJMethods ssjmethods;

    private SSJTimers ssjtimers;

    private SSJMethodChecks ssjmethodchecks;

    private SSJPlayerConfigManager ssjplayerconfigmanager;

    private SSJHologram ssjhologram;

    private SSJHologramUpdater ssjhologramupdater;

    private SSJScoreBoards ssjscoreboards;

    @Override
    public void onEnable() {

        regClass();

        regListeners();

        regCommands();

        configUICall();

        ssjmethodchecks.onEnableChecks();

    }

    @Override
    public void onDisable() {

        ssjmethodchecks.onDisableChecks();

    }

    private void regListeners(){

        SSJListeners ssjlistener = new SSJListeners(this);

        super.getServer().getPluginManager().registerEvents(ssjlistener, this);

    }

    private void regClass() {

        ssjscoreboards = new SSJScoreBoards(this);

        File playerConfigsFolder = new File(getDataFolder(), "PlayerConfigs");

        if (!playerConfigsFolder.exists()) {

            playerConfigsFolder.mkdirs();

        }

        ssjplayerconfigmanager = new SSJPlayerConfigManager(this, playerConfigsFolder, this);

        for (Player player : Bukkit.getOnlinePlayers()) {

            ArmorStand armorStand = ssjhologram.getArmorStandPName(player);

            Player player1 = ssjhologram.getPlayer(armorStand);

            this.ssjhologram = new SSJHologram(this, player);

            int ticks = 20;

            ssjhologramupdater = new SSJHologramUpdater(this, armorStand, player1);

            this.getServer().getScheduler().runTaskTimer(this, ssjhologramupdater, 0, ticks);

        }

        ssjconfigs = new SSJConfigs(this);

        ssjgui = new SSJGui(this);

        ssjmethods = new SSJMethods(this);

        ssjtimers = new SSJTimers(this);

        new SSJXPBar(this);

        ssjmethodchecks = new SSJMethodChecks(this);
    }

    private void regCommands(){

        getCommand("ssj").setExecutor(new SSJCommands(this));

    }

    private void configUICall(){

        ssjconfigs.createConfig();

        ssjconfigs.createTConfig();

        ssjconfigs.updateConfig();

    }

    public SSJGui getSSJGui(){

        return ssjgui;

    }

    public SSJConfigs getSSJConfigs(){

        return ssjconfigs;

    }

    public SSJMethods getSSJMethods(){

        return ssjmethods;

    }

    public SSJTimers getSSJTimers(){

        return ssjtimers;

    }

    public SSJPlayerConfigManager getSSJPCM(){

        return ssjplayerconfigmanager;

    }

    public SSJMethodChecks getSSJMethodChecks(){

        return ssjmethodchecks;

    }

    public SSJHologram getSSJHologram(){

        return ssjhologram;

    }

    public SSJScoreBoards getSSJSB(){

        return ssjscoreboards;

    }

}
