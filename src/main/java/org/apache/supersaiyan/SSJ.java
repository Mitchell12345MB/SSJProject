package org.apache.supersaiyan;

import org.apache.supersaiyan.Configs.SSJConfigs;
import org.apache.supersaiyan.Configs.SSJPlayerConfigManager;
import org.apache.supersaiyan.Listeners.SSJListeners;
import org.apache.supersaiyan.MethodClasses.SSJParticles;
import org.apache.supersaiyan.MethodClasses.*;
import org.apache.supersaiyan.SSJCommands.SSJCommands;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class SSJ extends JavaPlugin {

    private Player player;

    private Particle particleType;

    private int particleCount;

    private Double particleRange;

    private SSJConfigs ssjconfigs;

    private SSJGui ssjgui;

    private SSJMethods ssjmethods;

    private SSJTimers ssjtimers;

    private SSJMethodChecks ssjmethodchecks;

    private SSJPlayerConfigManager ssjplayerconfigmanager;

   // private SSJHologram ssjhologram;

    private SSJScoreBoards ssjscoreboards;

    private SSJParticles ssjparticles;

    @Override
    public void onEnable() {

        //ssjhologram = new SSJHologram(this);

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

        File playerConfigsFolder = new File(getDataFolder(), "PlayerConfigs");

        if (!playerConfigsFolder.exists()) {

            playerConfigsFolder.mkdirs();

        }

        ssjscoreboards = new SSJScoreBoards(this);

        ssjconfigs = new SSJConfigs(this);

        ssjgui = new SSJGui(this);

        ssjmethods = new SSJMethods(this);

        ssjtimers = new SSJTimers(this);

        new SSJXPBar(this);

        ssjplayerconfigmanager = new SSJPlayerConfigManager(this, playerConfigsFolder, this);

        ssjmethodchecks = new SSJMethodChecks(this);

        ssjparticles = new SSJParticles(this, player, particleType, particleCount, particleRange);

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

   //public SSJHologram getSSJHologram(){

     //   return ssjhologram;

    //}

    public SSJScoreBoards getSSJSB(){

        return ssjscoreboards;

    }
}
