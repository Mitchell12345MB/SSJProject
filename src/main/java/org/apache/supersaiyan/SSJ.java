package org.apache.supersaiyan;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import org.apache.supersaiyan.Configs.SSJConfigs;
import org.apache.supersaiyan.Configs.SSJPlayerConfig;
import org.apache.supersaiyan.Listeners.SSJListeners;
import org.apache.supersaiyan.MethodClasses.*;
import org.apache.supersaiyan.SSJCommands.SSJCommands;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class SSJ extends JavaPlugin {

    private SSJConfigs ssjconfigs;

    private SSJGui ssjgui;

    private SSJMethods ssjmethods;

    private SSJTimers ssjtimers;

    private SSJScoreBoards ssjscoreboards;

    private SSJXPBar ssjxpbar;

    private SSJPlayerConfig ssjppc;

    private SSJHologram ssjhologram;

    private ProtocolManager protocolManager;

    private SSJMethodChecks ssjmethodchecks;

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

    private void regClass(){

        protocolManager = ProtocolLibrary.getProtocolManager();

        ssjconfigs = new SSJConfigs(this);

        ssjppc = new SSJPlayerConfig(this);

        ssjgui = new SSJGui(this);

        ssjmethods = new SSJMethods(this);

        ssjscoreboards = new SSJScoreBoards(this);

        ssjtimers = new SSJTimers(this);

        ssjxpbar = new SSJXPBar(this);

        ssjhologram = new SSJHologram(this);

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

    public SSJScoreBoards getSSJSB(){

        return ssjscoreboards;

    }

    public SSJXPBar getSSJXPB(){

        return ssjxpbar;

    }
    public SSJPlayerConfig getSSJPPC(SSJ ssj, Player p) {

        return new SSJPlayerConfig(ssj, p.getUniqueId());

    }

    public SSJHologram getSSJHologram(){

        return ssjhologram;

    }

    public ProtocolManager getPL(){

        return protocolManager;
    }

    public SSJMethodChecks getSSJMethodChecks(){

        return ssjmethodchecks;
    }
}
