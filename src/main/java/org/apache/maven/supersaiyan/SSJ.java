package org.apache.maven.supersaiyan;

import org.apache.maven.supersaiyan.Configs.SSJConfigs;
import org.apache.maven.supersaiyan.Configs.SSJPlayerConfig;
import org.apache.maven.supersaiyan.Listeners.SSJListeners;
import org.apache.maven.supersaiyan.MethodClasses.*;
import org.apache.maven.supersaiyan.SSJCommands.SSJCommands;
import org.bukkit.plugin.java.JavaPlugin;


public class SSJ extends JavaPlugin {

    private SSJConfigs ssjconfigs;

    private SSJPlayerConfig ssjppc;

    private SSJGui ssjgui;

    private SSJMethods ssjmethods;

    private SSJScoreboard ssjscoreboard;

    private SSJTimers ssjtimers;

    private ScoreHelper scorehelper;

    @Override
    public void onEnable() {

        regClass();

        regListeners();

        regCommands();

        configUICall();

        ssjmethods.onEnableChecks();

    }

    @Override
    public void onDisable() {

        ssjmethods.onDisableChecks();

        ssjconfigs.saveConfig();

    }

    private void regListeners(){

        SSJListeners ssjlistener = new SSJListeners(this);

        super.getServer().getPluginManager().registerEvents(ssjlistener, this);

    }

    private void regClass(){

        ssjconfigs= new SSJConfigs(this);

        ssjppc = new SSJPlayerConfig(this);

        ssjgui = new SSJGui(this);

        ssjmethods = new SSJMethods(this);

        ssjscoreboard = new SSJScoreboard(this);

        ssjtimers = new SSJTimers(this);

        scorehelper = new ScoreHelper(this);

    }

    private void regCommands(){

        getCommand("ssj").setExecutor(new SSJCommands(this));

    }

    private void configUICall(){

        ssjconfigs.createConfig();

        ssjconfigs.createTConfig();

        ssjconfigs.updateConfig();

    }

    public SSJGui getssjgui(){

        return ssjgui;

    }

    public SSJPlayerConfig getSSJpPc(){

        return ssjppc;

    }

    public SSJConfigs getSSJCConfigs(){

        return ssjconfigs;

    }

    public SSJMethods getSSJmethods(){

        return ssjmethods;

    }

    public SSJScoreboard getSSJscoreboard(){

        return ssjscoreboard;

    }

    public SSJTimers getSSJTimers(){

        return ssjtimers;
    }

    public ScoreHelper getSSJSH(){

        return scorehelper;
    }
}
