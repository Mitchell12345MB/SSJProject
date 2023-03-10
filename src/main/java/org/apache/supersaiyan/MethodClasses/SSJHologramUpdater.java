package org.apache.supersaiyan.MethodClasses;

import org.apache.supersaiyan.SSJ;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class SSJHologramUpdater extends BukkitRunnable {

    public final ArmorStand armorStand;

    private final Player player;

    private final SSJ ssj;

    public SSJHologramUpdater(SSJ ssj, ArmorStand armorStand, Player player) {

        this.armorStand = armorStand;

        this.player = player;

        this.ssj = ssj;

    }

    @Override

    public void run() {

        armorStand.teleport(player.getLocation().add(0, 2, 0));

    }

}
