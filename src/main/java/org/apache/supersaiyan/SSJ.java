package org.apache.supersaiyan;

import org.apache.supersaiyan.Configs.SSJConfigs;
import org.apache.supersaiyan.Configs.SSJPlayerConfigManager;
import org.apache.supersaiyan.Listeners.SSJActionListeners;
import org.apache.supersaiyan.Listeners.SSJListeners;
import org.apache.supersaiyan.MethodClasses.*;
import org.apache.supersaiyan.SSJCommands.SSJCommands;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class SSJ extends JavaPlugin {

    private Player player;

    private Particle particleType;

    private String string;

    private int integer;

    private Map<UUID, Integer> mapui;

    private SSJConfigs ssjconfigs;

    private SSJGui ssjgui;

    private SSJMethods ssjmethods;

    private SSJTimers ssjtimers;

    private SSJMethodChecks ssjmethodchecks;

    private SSJPlayerConfigManager ssjplayerconfigmanager;

    private SSJScoreBoards ssjscoreboards;

    private SSJParticles ssjparticles;

    private SSJXPBar ssjxpbar;

    private SSJBossBar ssjbossbar;

    private Metrics metrics;

    private SSJListeners ssjlisteners;

    private SSJRpgSys ssjrpgsys;

    private SSJActionListeners ssjactionlisteners;

    @Override
    public void onEnable() {

        int pluginId = 18024;

        metrics = new Metrics(this, pluginId);

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

        ssjlisteners = new SSJListeners(this);

        super.getServer().getPluginManager().registerEvents(ssjlisteners, this);

        ssjactionlisteners = new SSJActionListeners(this);

        super.getServer().getPluginManager().registerEvents(ssjactionlisteners, this);

    }

    private void regClass() {

        File playerConfigsFolder = new File(getDataFolder(), "PlayerConfigs");

        if (!playerConfigsFolder.exists()) {

            playerConfigsFolder.mkdirs();

        }

        new SSJUpdateChecker(this, 12345).getVersion(version -> {

            if (!this.getDescription().getVersion().equals(version)) {

                getLogger().info("There is a new update available.");

            }

        });

        ssjplayerconfigmanager = new SSJPlayerConfigManager(this, playerConfigsFolder);

        ssjscoreboards = new SSJScoreBoards(this);

        ssjconfigs = new SSJConfigs(this);

        ssjgui = new SSJGui(this);

        ssjmethods = new SSJMethods(this);

        ssjtimers = new SSJTimers(this);

        ssjxpbar = new SSJXPBar(this, player);

        ssjmethodchecks = new SSJMethodChecks(this);

        ssjparticles = new SSJParticles(this, player, particleType, integer, 0);

        ssjbossbar = new SSJBossBar(this, string, mapui);

        ssjrpgsys = new SSJRpgSys(this);

    }

    private void regCommands(){

        Objects.requireNonNull(getCommand("ssj")).setExecutor(new SSJCommands(this));

    }

    private void configUICall(){

        ssjconfigs.createConfig();

        ssjconfigs.createTConfig();

        ssjconfigs.updateConfigs();

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

    public SSJScoreBoards getSSJSB(){

        return ssjscoreboards;

    }

    public SSJXPBar getSSJXPB(){

        return ssjxpbar;

    }

    public SSJBossBar getSSJBB(){

        return ssjbossbar;

    }

    public SSJParticles getSSJParticles(){

        return ssjparticles;

    }

    public SSJRpgSys getSSJRpgSys() {

        return ssjrpgsys;

    }

    public SSJActionListeners getSSJAL() {

        return ssjactionlisteners;

    }

    public SSJListeners getSSJListeners() {

        return ssjlisteners;

    }
}
