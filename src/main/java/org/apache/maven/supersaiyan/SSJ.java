package org.apache.maven.supersaiyan;

import org.bukkit.plugin.java.JavaPlugin;

public class SSJ extends JavaPlugin {

    private Configs configs;

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

        getServer().getPluginManager().registerEvents(new Listeners(this), this);

        getServer().getPluginManager().registerEvents(new PlayerConfig(this), this);

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
