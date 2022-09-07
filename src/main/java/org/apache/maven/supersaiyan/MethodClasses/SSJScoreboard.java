package org.apache.maven.supersaiyan.MethodClasses;

import org.apache.maven.supersaiyan.SSJ;
import org.bukkit.entity.Player;

public class SSJScoreboard {

    private final SSJ ssj;

    public SSJScoreboard(SSJ ssj) {
        this.ssj = ssj;
    }

    public void callScoreboard(Player p) {

        ScoreHelper helper = ssj.getSSJSH().createScore(p);

        helper.setTitle("&aTutorial &eA");
        helper.setSlot(3, "&7&m--------------------------------");
        helper.setSlot(2, "&aPlayer&f: " + p.getName());
        helper.setSlot(1, "&7&m--------------------------------");

    }

    public void callBelowName() {


    }
}
