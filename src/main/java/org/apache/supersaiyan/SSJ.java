package org.apache.supersaiyan;

import org.apache.supersaiyan.Configs.SSJConfigs;
import org.apache.supersaiyan.Configs.SSJPlayerConfigManager;
import org.apache.supersaiyan.Listeners.SSJActionListeners;
import org.apache.supersaiyan.Listeners.SSJListeners;
import org.apache.supersaiyan.MethodClasses.*;
import org.apache.supersaiyan.SSJCommands.SSJCommands;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashMap;
import java.util.Objects;

public class SSJ extends JavaPlugin {

    private static SSJ instance;

    private SSJConfigs ssjconfigs;
    private SSJGui ssjgui;
    private SSJMethods ssjmethods;
    private SSJTimers ssjtimers;
    private SSJMethodChecks ssjmethodchecks;
    private SSJPlayerConfigManager ssjplayerconfigmanager;
    private SSJScoreBoards ssjscoreboards;
    private SSJParticles ssjparticles;
    private SSJBossBar ssjbossbar;
    private SSJListeners ssjlisteners;
    private SSJRpgSys ssjrpgsys;
    private SSJActionListeners ssjactionlisteners;
    private SSJTransformationManager ssjtransformationmanager;
    private SSJChargeSystem ssjchargesystem;
    private SSJSkillManager ssjskillmanager;
    private SSJEnergyManager ssjenergymanager;
    private SSJSaiyanAbilityManager ssjsaiyanabilitymanager;
    private SSJBossBar persistentEnergyBar;
    private SSJPotentialSystem ssjPotentialSystem;
    private SSJAuraSystem ssjAuraSystem;

    @Override
    public void onEnable() {
        // Initialize plugin instance
        instance = this;
        
        // Initialize configs first
        this.ssjconfigs = new SSJConfigs(this);
        this.ssjconfigs.createConfig();
        this.ssjconfigs.createTConfig();
        this.ssjconfigs.createSConfig();
        this.ssjconfigs.loadConfigs();
        this.ssjconfigs.updateConfigs();
        
        File userFolder = new File(getDataFolder(), "PlayerConfigs");
        userFolder.mkdirs();
        this.ssjplayerconfigmanager = new SSJPlayerConfigManager(this, userFolder);
        
        // Register all classes
        regClass();
        
        // Register event listeners
        regListeners();
        
        // Register commands
        Objects.requireNonNull(getCommand("ssj")).setExecutor(new SSJCommands(this));
        
        // Start timers and systems
        this.ssjtimers.saveTimer();
        
        // Only start passive energy gain if enabled in config
        if (this.ssjconfigs.getPassiveEnergyGain()) {
            this.ssjrpgsys.startPassiveEnergyGain();
        }
        
        ssjPotentialSystem = new SSJPotentialSystem(this);
        ssjAuraSystem = new SSJAuraSystem(this);
        
        getLogger().info("SSJ Plugin has been enabled!");
    }

    @Override
    public void onDisable() {
        // Clean up boss bars
        if (ssjbossbar != null) {
            ssjbossbar.cleanup();
        }

        if (persistentEnergyBar != null) {
            persistentEnergyBar.cleanup();
            persistentEnergyBar = null;
        }
        
        // Clean up charge system bars
        if (ssjchargesystem != null) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                ssjchargesystem.stopCharging(player);
            }
        }

        // Clean up potential system
        if (ssjPotentialSystem != null) {
            ssjPotentialSystem.cleanup();
        }

        if (ssjmethodchecks != null) {
            ssjmethodchecks.checkOnDisable();
        }

        if (ssjrpgsys != null) {
            ssjrpgsys.stopPassiveEnergyGain();
        }

        getLogger().info("SSJ Plugin has been disabled!");
    }

    private void regListeners() {
        ssjlisteners = new SSJListeners(this);
        getServer().getPluginManager().registerEvents(ssjlisteners, this);

        ssjactionlisteners = new SSJActionListeners(this);
        getServer().getPluginManager().registerEvents(ssjactionlisteners, this);
    }

    private void regClass() {
        this.ssjmethods = new SSJMethods(this);
        this.ssjtimers = new SSJTimers(this);
        this.ssjmethodchecks = new SSJMethodChecks(this);
        this.ssjscoreboards = new SSJScoreBoards(this);
        this.ssjparticles = new SSJParticles(this, null, Particle.FLAME, 5, 2.0);
        this.ssjbossbar = new SSJBossBar(this, "Energy: ", new HashMap<>(), true);
        this.ssjrpgsys = new SSJRpgSys(this);
        this.ssjtransformationmanager = new SSJTransformationManager(this);
        this.ssjchargesystem = new SSJChargeSystem(this);
        this.ssjskillmanager = new SSJSkillManager(this);
        this.ssjenergymanager = new SSJEnergyManager(this);
        this.ssjsaiyanabilitymanager = new SSJSaiyanAbilityManager(this);
        this.ssjgui = new SSJGui(this);
    }

    // Getter methods
    public static SSJ getInstance() {
        return instance;
    }

    public SSJConfigs getSSJConfigs() {
        return ssjconfigs;
    }

    public SSJGui getSSJGui() {
        return ssjgui;
    }

    public SSJMethods getSSJMethods() {
        return ssjmethods;
    }

    public SSJTimers getSSJTimers() {
        return ssjtimers;
    }

    public SSJMethodChecks getSSJMethodChecks() {
        return ssjmethodchecks;
    }

    public SSJPlayerConfigManager getSSJPCM() {
        return ssjplayerconfigmanager;
    }

    public SSJScoreBoards getSSJScoreBoards() {
        return ssjscoreboards;
    }

    public SSJParticles getSSJParticles() {
        return ssjparticles;
    }

    public SSJBossBar getSSJBossBar() {
        return ssjbossbar;
    }

    public SSJRpgSys getSSJRpgSys() {
        return ssjrpgsys;
    }

    public SSJTransformationManager getSSJTransformationManager() {
        return ssjtransformationmanager;
    }

    public SSJChargeSystem getSSJChargeSystem() {
        return ssjchargesystem;
    }

    public SSJSkillManager getSSJSkillManager() {
        return ssjskillmanager;
    }

    public SSJEnergyManager getSSJEnergyManager() {
        return ssjenergymanager;
    }

    public SSJSaiyanAbilityManager getSSJSaiyanAbilityManager() {
        return ssjsaiyanabilitymanager;
    }

    public SSJActionListeners getSSJActionListeners() {
        return ssjactionlisteners;
    }

    public SSJPotentialSystem getSSJPotentialSystem() {
        return ssjPotentialSystem;
    }

    public SSJAuraSystem getSSJAuraSystem() {
        return ssjAuraSystem;
    }
}