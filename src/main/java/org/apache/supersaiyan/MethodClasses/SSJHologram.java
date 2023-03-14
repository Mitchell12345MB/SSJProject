package org.apache.supersaiyan.MethodClasses;

import org.apache.supersaiyan.SSJ;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SSJHologram {

    private final SSJ ssj;

    private final Map<UUID, SSJHologram> players = new HashMap<>();

    public SSJHologram(SSJ ssj) {
        this.ssj = ssj;
    }

    public void removeHolosForPlayer(Player player) {
        if (players.containsKey(player.getUniqueId())) {

            ArmorStand armorNameStand = getArmorStandPName(player);
            ArmorStand armorPowerStand = getArmorStandPPower(player);
            ArmorStand armorFormStand = getArmorStandPForm(player);

            armorNameStand.remove();
            armorPowerStand.remove();
            armorFormStand.remove();

            players.remove(player.getUniqueId());
        }
    }

    public void createHolosForPlayer(Player player) {
        if (player == null) {
            return;
        }

        removeHolosForPlayer(player);


        ArmorStand armorNameStand = createArmorStand(player, EntityType.ARMOR_STAND, player.getLocation().subtract(0, 2, 0));
        ArmorStand armorPowerStand = createArmorStand(player, EntityType.ARMOR_STAND, player.getLocation().subtract(0, 3, 0));
        ArmorStand armorFormStand = createArmorStand(player, EntityType.ARMOR_STAND, player.getLocation().subtract(0, 4, 0));

        armorNameStand.setCustomName(player.getName());
        armorPowerStand.setCustomName(String.valueOf(ssj.getSSJPCM().getBP(player)));
        armorFormStand.setCustomName(ssj.getSSJPCM().getForm(player));

        armorNameStand.setCustomNameVisible(true);
        armorPowerStand.setCustomNameVisible(true);
        armorFormStand.setCustomNameVisible(true);

        players.put(player.getUniqueId(), this);
    }

    private ArmorStand createArmorStand(Player player, EntityType entityType, org.bukkit.Location location) {
        ArmorStand armorStand = (ArmorStand) player.getWorld().spawnEntity(location, entityType);
        armorStand.setGravity(false);
        armorStand.setVisible(false);
        armorStand.setCanPickupItems(false);
        armorStand.setCustomNameVisible(false);
        armorStand.setRemoveWhenFarAway(false);
        armorStand.setSmall(true);
        armorStand.setMarker(true);
        return armorStand;
    }

    private ArmorStand getArmorStandPName(Player player) {
        return (ArmorStand) getNearbyEntity(player, EntityType.ARMOR_STAND, 2, 2, 2, true);
    }

    private ArmorStand getArmorStandPPower(Player player) {
        return (ArmorStand) getNearbyEntity(player, EntityType.ARMOR_STAND, 2, 3, 2, true);
    }

    private ArmorStand getArmorStandPForm(Player player) {
        return (ArmorStand) getNearbyEntity(player, EntityType.ARMOR_STAND, 2, 4, 2, true);
    }

    private Entity getNearbyEntity(Player player, EntityType entityType, int rangeXZ, int rangeY, int rangeXZBuff, boolean playerOnly) {
        Entity nearbyEntity = null;
        for (Entity entity : player.getNearbyEntities(rangeXZ, rangeY, rangeXZ)) {
            if (entity.getType() == entityType && (playerOnly ? entity instanceof Player : true)) {
                if (rangeXZBuff > 0 || rangeY > 0) {
                    if (entity.getLocation().getBlockY() >= player.getLocation().getBlockY() - rangeY
                            && entity.getLocation().getBlockY() <= player.getLocation().getBlockY() + rangeY) {
                        double xDiff = Math.abs(entity.getLocation().getX() - player.getLocation().getX());
                        double zDiff = Math.abs(entity.getLocation().getZ() - player.getLocation().getZ());

                        double xzDistance = Math.sqrt(Math.pow(xDiff, 2) + Math.pow(zDiff, 2));

                        if (xzDistance <= rangeXZ + rangeXZBuff) {
                            nearbyEntity = entity;
                            rangeXZBuff = (int) xzDistance;
                        }
                    }
                } else {
                    nearbyEntity = entity;
                }
            }
        }

        return nearbyEntity;
    }

}
