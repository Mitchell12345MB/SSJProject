package org.apache.maven.supersaiyan;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
@Setter
public class Main extends JavaPlugin {

    @Getter
    private static Main instance;

    private Configs configs;

    private PlayerConfig pconfigs;

    @Override
    public void onEnable() {

        instance = this;

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

        getServer().getPluginManager().registerEvents(new Listeners(), this);

        getServer().getPluginManager().registerEvents(new PlayerConfig(), this);

    }

    private void regClass(){

        configs = new Configs();

    }

    private void regCommands(){

        getCommand("ssj").setExecutor(new Commands());

    }

    private void configUICall(){

        configs.createConfig();

        configs.createTConfig();

        configs.updateConfig();
    }
}
