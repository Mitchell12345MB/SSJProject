package org.apache.maven.supersaiyan;

import org.bukkit.plugin.java.JavaPlugin;


public class SSJ extends JavaPlugin {

    private Configs configs = new Configs(this);

    private Listeners listener = new Listeners(this);

    private PlayerConfig pPc = new PlayerConfig(this);

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
}
