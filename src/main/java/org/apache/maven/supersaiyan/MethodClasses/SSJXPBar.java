package org.apache.maven.supersaiyan.MethodClasses;

import org.apache.maven.supersaiyan.Configs.SSJPlayerConfig;
import org.apache.maven.supersaiyan.SSJ;
import org.bukkit.entity.Player;

public class SSJXPBar {

    private final SSJ ssj;

    public SSJXPBar(SSJ ssj) {
        this.ssj = ssj;
    }

    public void addEnergy(Player p) {

        SSJPlayerConfig user = new SSJPlayerConfig(ssj, p.getUniqueId());

        int energy = user.getUserConfig().getInt("Energy");

        int power = user.getUserConfig().getInt("Base.Power");

        int bp = user.getUserConfig().getInt("Battle_Power");

        int adde = energy + power;

        int limit = power * 5;

        int addbp = bp * energy;

        if (energy < limit) {

            user.loadUserFile();

            user.getUserConfig().set("Energy", adde);

            user.getUserConfig().set("Battle_Power", addbp);

            user.saveUserFile();

            ssj.getSSJmethods().callScoreboard(p);
        }
    }
}
