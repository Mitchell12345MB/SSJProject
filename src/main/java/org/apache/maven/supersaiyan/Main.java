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

        getServer().getPluginManager().registerEvents(new Listeners(), this);

        configs = new Configs();

        pconfigs = new PlayerConfig();

        getCommand("ssj").setExecutor(new Commands());

        configs.createConfig();

        configs.createTConfig();

        configs.updateConfig();

    }

    @Override
    public void onDisable() {

        configs.saveConfig();


    }
}
