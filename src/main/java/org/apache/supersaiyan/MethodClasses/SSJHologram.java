package org.apache.supersaiyan.MethodClasses;

import org.apache.supersaiyan.SSJ;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SSJHologram {

    private final SSJ ssj;

    private final Map<UUID, SSJHologram> players = new HashMap<>();

    public SSJHologram(SSJ ssj, Player player) {

        this.ssj = ssj;

        createHolosForPlayer(player);

        players.put(player.getUniqueId(), this);

    }

    public void removeHolosForPlayer(Player player) {

        ArmorStand armorStand = createPlayerNameHolo(player);

        armorStand.remove();

        players.remove(player.getUniqueId());

    }

    public void createHolosForPlayer(Player player) {

        if (player == null) {

            return;

        }

        this.createPlayerNameHolo(player);

        this.players.put(player.getUniqueId(), this);

    }

    private ArmorStand createPlayerNameHolo(Player player) {

        if (player == null) {

            return null;

        }

        ArmorStand armorStand = null;

        try {

            armorStand = (ArmorStand) player.getLocation().getWorld().spawnEntity(player.getLocation(), EntityType.ARMOR_STAND);

            armorStand.setCustomName(player.getName());

            armorStand.setMarker(true);

            armorStand.setInvulnerable(true);

            armorStand.setSmall(true);

            armorStand.setVisible(false);

            armorStand.setCustomNameVisible(true);

            armorStand.setBasePlate(false);

            armorStand.setArms(false);

            armorStand.setGravity(false);

        } catch (Exception e) {

            e.printStackTrace();

        }

        return armorStand;

    }

}
