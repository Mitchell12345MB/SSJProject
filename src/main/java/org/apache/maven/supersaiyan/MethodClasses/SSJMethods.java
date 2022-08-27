package org.apache.maven.supersaiyan.MethodClasses;

import org.apache.maven.supersaiyan.SSJ;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;

import java.io.IOException;

public class SSJMethods {

    private final SSJ ssj;

    public SSJMethods(SSJ ssj) {
        this.ssj = ssj;
    }

    public void checkStartCommandMethod(Player p) {

        if (ssj.getpPc().getpConfigFile(p).exists() && ssj.getpPc().getpConfig(p).getBoolean("Start")) {

            p.sendMessage(ChatColor.RED + "You've already started your Saiyan journey!");

        }

        if (!ssj.getpPc().getpConfigFile(p).exists()) {

            p.sendMessage(ChatColor.RED + "Your player file doesn't exist!");

            p.sendMessage(ChatColor.RED + "Please re-log or tell a server Admin/Owner.");

        }

        if (ssj.getpPc().getpConfigFile(p).exists() && !ssj.getpPc().getpConfig(p).getBoolean("Start")) {

            try {

                ssj.getpPc().getpConfig(p).load(ssj.getpPc().getpConfigFile(p));

                ssj.getpPc().getpConfig(p).set("Start", true);

                ssj.getpPc().getpConfig(p).save(ssj.getpPc().getpConfigFile(p));

                ssj.getssjgui().openInventory(p);

                p.sendMessage(ChatColor.RED + "Your Saiyan journey has started!");

                ssj.getLogger().warning(p.getName() + "'s.yml Has been updated!");

            } catch (IOException | InvalidConfigurationException ex) {

                ex.printStackTrace();

            }
        }
    }



}
