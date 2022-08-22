package org.apache.maven.supersaiyan;

import org.apache.maven.supersaiyan.Configs.Configs;
import org.apache.maven.supersaiyan.Configs.PlayerConfig;
import org.apache.maven.supersaiyan.Listeners.SSJListeners;
import org.apache.maven.supersaiyan.SSJCommands.Commands;
import org.apache.maven.supersaiyan.Listeners.SSJgui;
import org.bukkit.plugin.java.JavaPlugin;


public class SSJ extends JavaPlugin {

    private Configs configs;

    private SSJListeners ssjlistener;

    private PlayerConfig pPc;

    private SSJgui ssjgui;

    @Override
    public void onEnable() {

        regClass();

        regListeners();

        regCommands();

        configUICall();

    }

    @Override
    public void onDisable() {

        configs.saveConfig();

    }

    private void regListeners(){

        SSJListeners ssjlistener = new SSJListeners(this);

        SSJgui ssjgui = new SSJgui(this);

        super.getServer().getPluginManager().registerEvents(ssjgui, this);

        super.getServer().getPluginManager().registerEvents(ssjlistener, this);


    }

    private void regClass(){

        configs = new Configs(this);

        pPc = new PlayerConfig(this);

        ssjgui = new SSJgui(this);

    }

    private void regCommands(){

        getCommand("ssj").setExecutor(new Commands(this));

    }

    private void configUICall(){

        configs.createConfig();

        configs.createTConfig();

        configs.updateConfig();
    }

    public SSJgui getssjgui(){

        return ssjgui;
    }

    public PlayerConfig getpPc(){

        return pPc;
    }

}
