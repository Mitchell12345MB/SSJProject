package org.apache.supersaiyan.MethodClasses;

import org.apache.supersaiyan.SSJ;
import org.bukkit.entity.Player;

public class SSJRpgSys {

    private final SSJ ssj;

    public SSJRpgSys(SSJ ssj) {

        this.ssj = ssj;

    }

    public void addEnergy(Player p) {

        int adde = ssj.getSSJPCM().getEnergy(p) * ssj.getSSJPCM().getPower(p);

        if (ssj.getSSJPCM().getEnergy(p) == 0) {

            ssj.getSSJPCM().setPlayerConfigValue(p, "Energy", 5);

            ssj.getSSJMethodChecks().scoreBoardCheck();

            ssj.getSSJMethods().callScoreboard(p);

        } else if (ssj.getSSJPCM().getEnergy(p) < ssj.getSSJPCM().getLimit(p)) {

            ssj.getSSJPCM().setPlayerConfigValue(p, "Energy", adde);

            ssj.getSSJMethodChecks().scoreBoardCheck();

            ssj.getSSJMethods().callScoreboard(p);

        }

    }

    public void multBP(Player p) {

        int multbp = ((ssj.getSSJPCM().getEnergy(p) + getBaseBP(p)) * ssj.getSSJConfigs().getBPM());

        if (ssj.getSSJPCM().getBattlePower(p) == 0) {

            ssj.getSSJPCM().setPlayerConfigValue(p,"Battle_Power", addBaseBP(p));

            ssj.getSSJMethodChecks().scoreBoardCheck();

            ssj.getSSJMethods().callScoreboard(p);

        } else if (ssj.getSSJPCM().getEnergy(p) == 0) {

            ssj.getSSJPCM().setPlayerConfigValue(p,"Battle_Power", addBaseBP(p));

            ssj.getSSJMethodChecks().scoreBoardCheck();

            ssj.getSSJMethods().callScoreboard(p);

        } else if (ssj.getSSJPCM().getEnergy(p) < ssj.getSSJPCM().getLimit(p)) {

            ssj.getSSJPCM().setPlayerConfigValue(p,"Battle_Power", multbp);

            ssj.getSSJMethodChecks().scoreBoardCheck();

            ssj.getSSJMethods().callScoreboard(p);

        }

    }

    public int addBaseBP(Player p) {

        return getBaseBP(p) * ssj.getSSJConfigs().getBPM();

    }

    public int addLevel(Player p) {

        return getBaseBP(p) / 150;

    }

    public int getBaseBP(Player p) { // Multiplies the player's Health, Power, Strength, Speed, and defence and output's the player's base battle power.

        return ssj.getSSJPCM().getHealth(p) * ssj.getSSJPCM().getPower(p) + ssj.getSSJPCM().getStrength(p) *

                ssj.getSSJPCM().getSpeed(p) + ssj.getSSJPCM().getStamina(p) * ssj.getSSJPCM().getDefence(p);

    }
}
