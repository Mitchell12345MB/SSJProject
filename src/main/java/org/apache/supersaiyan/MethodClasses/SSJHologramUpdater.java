package org.apache.supersaiyan.MethodClasses;

import org.apache.supersaiyan.SSJ;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class SSJHologramUpdater extends BukkitRunnable {

    private final SSJ ssj;
    private final ArmorStand armorStand;
    private final Player player;

    public SSJHologramUpdater(SSJ ssj, ArmorStand armorStand, Player player) {
        this.ssj = ssj;
        this.armorStand = armorStand;
        this.player = player;
    }

    @Override
    public void run() {
        if (armorStand.isDead()) {
            cancel();
            return;
        }

        Location location = player.getLocation().clone().add(0, 2.2, 0); // adjust the Y value to move the holograms higher or lower

        armorStand.teleport(location);

        if (armorStand.getCustomName() != null) {
            if (armorStand.getCustomName().contains("Power:")) {
                armorStand.setCustomName("Power: " + ssj.getSSJPCM().getPower(player));
            } else if (armorStand.getCustomName().contains("Form:")) {
                armorStand.setCustomName("Form: " + ssj.getSSJPCM().getForm(player));
            } else if (armorStand.getCustomName().contains(player.getName())) {
                armorStand.setCustomName(player.getName());
            }
        }
    }
}
