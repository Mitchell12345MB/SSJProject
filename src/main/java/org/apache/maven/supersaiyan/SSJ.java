package org.apache.maven.supersaiyan;

import org.apache.maven.supersaiyan.Configs.*;
import org.apache.maven.supersaiyan.Listeners.*;
import org.apache.maven.supersaiyan.MethodClasses.*;
import org.apache.maven.supersaiyan.SSJCommands.*;
import org.bukkit.plugin.java.JavaPlugin;


public class SSJ extends JavaPlugin {

    private SSJConfigs ssjconfigs;

    private SSJGui ssjgui;

    private SSJMethods ssjmethods;

    private SSJTimers ssjtimers;

    private SSJScoreBoards ssjscoreboards;

    private SSJXPBar ssjxpbar;

    private SSJBelowName ssjbelowname;

    SSJPlayerConfig ssjppc;

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

        ssjconfigs = new SSJConfigs(this);

        ssjppc = new SSJPlayerConfig(this);

        ssjgui = new SSJGui(this);

        ssjmethods = new SSJMethods(this);

        ssjscoreboards = new SSJScoreBoards(this);

        ssjtimers = new SSJTimers(this);

        ssjxpbar = new SSJXPBar(this);

        ssjbelowname = new SSJBelowName(this);

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

    public SSJConfigs getSSJCConfigs(){

        return ssjconfigs;

    }

    public SSJMethods getSSJmethods(){

        return ssjmethods;

    }

    public SSJTimers getSSJTimers(){

        return ssjtimers;
    }

    public SSJScoreBoards getSSJSB(){

        return ssjscoreboards;
    }

    public SSJXPBar getSSJXPB(){

        return ssjxpbar;
    }

    public SSJBelowName getSSJBN() {

        return ssjbelowname;
    }
}
