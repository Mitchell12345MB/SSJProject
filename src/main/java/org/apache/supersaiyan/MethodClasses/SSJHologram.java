package org.apache.supersaiyan.MethodClasses;


import org.apache.supersaiyan.SSJ;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class SSJHologram {

    private final SSJ ssj;

    private final HashMap<UUID, SSJHologram> players = new HashMap<UUID, SSJHologram>();

    public SSJHologram(SSJ ssj) {

        this.ssj = ssj;

    }

    public SSJHologram(SSJ ssj, Player player) {

        this.ssj = ssj;

        playerNameHolo(player);

        players.put(player.getUniqueId(), this);

    }

    private ArmorStand as;

    public ArmorStand playerNameHolo(Player player) {

        as = (ArmorStand) player.getLocation().getWorld().spawnEntity(player.getLocation(), EntityType.ARMOR_STAND);

        as.setCustomName(player.getName());

        as.setMarker(true);

        as.setInvulnerable(true);

        as.setSmall(true);

        as.setVisible(false);

        as.setCustomNameVisible(true);

        as.setBasePlate(false);

        as.setArms(false);

        as.setGravity(false);

        return as;

    }

    public void removeHolos(Player player) {

        playerNameHolo(player).remove();

        players.remove(player.getUniqueId());

    }

    public SSJHologram creatHolos(Player player) {

        return new SSJHologram(ssj, player);

    }

}
