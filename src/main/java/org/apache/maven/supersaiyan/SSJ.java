package org.apache.maven.supersaiyan;

import org.apache.maven.supersaiyan.Configs.Configs;
import org.apache.maven.supersaiyan.Configs.PlayerConfig;
import org.apache.maven.supersaiyan.Listeners.Listeners;
import org.apache.maven.supersaiyan.SSJCommands.Commands;
import org.apache.maven.supersaiyan.SSJGUI.SSJgui;
import org.bukkit.plugin.java.JavaPlugin;


public class SSJ extends JavaPlugin {

    private Configs configs = new Configs(this);

    private Listeners listener = new Listeners(this);

    private PlayerConfig pPc = new PlayerConfig(this);

    private SSJgui ssjgui = new SSJgui(this);

    @Override
    public void onEnable() {

        regListeners();

        regClass();

        regCommands();

        configUICall();

    }

    @Override
    public void onDisable() {

        configs.saveConfig();

    }

    private void regListeners(){

        super.getServer().getPluginManager().registerEvents(listener, this);

        super.getServer().getPluginManager().registerEvents(pPc, this);

        super.getServer().getPluginManager().registerEvents(ssjgui, this);

    }

    private void regClass(){

        configs = new Configs(this);

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
}
