package org.apache.supersaiyan.MethodClasses;

import org.apache.supersaiyan.SSJ;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class SSJParticles {

    private final Player player;

    private final Particle particleType;

    private final int particleCount;

    private final double particleRange;

    private final SSJ ssj;

    public SSJParticles(SSJ ssj, Player player, Particle particleType, int particleCount, double particleRange) {

        this.ssj = ssj;

        this.player = player;

        this.particleType = particleType;

        this.particleCount = particleCount;

        this.particleRange = particleRange;

    }

    public void createParticles() {

        new BukkitRunnable() {

            double t = 0;

            final int particleCounts = SSJParticles.this.particleCount / 2;

            final int particleCounto = SSJParticles.this.particleCount;

            @Override
            public void run() {

                t += Math.PI / 8;

                for (Entity entity : player.getWorld().getNearbyEntities(player.getLocation(), particleRange, particleRange, particleRange)) {

                    if (entity instanceof Player && !entity.equals(player)) {

                        Player p = (Player) entity;

                        for (int i = 0; i < particleCounto; i++) {

                            double x = particleRange * Math.cos(t + 2 * Math.PI * i / particleCounto);

                            double y = 2 * Math.exp(-0.1 * t) * Math.sin(t) + 1.5;

                            double z = particleRange * Math.sin(t + 2 * Math.PI * i / particleCounto);

                            Location loc = player.getLocation().add(x, y, z);

                            p.spawnParticle(particleType, loc, 0);

                        }

                    } else {

                        for (int i = 0; i < particleCounts; i++) {

                            double x = particleRange * Math.cos(t + 2 * Math.PI * i / particleCounts);

                            double y = 2 * Math.exp(-0.1 * t) * Math.sin(t) + 1.5;

                            double z = particleRange * Math.sin(t + 2 * Math.PI * i / particleCounts);

                            Location loc = player.getLocation().add(x, y, z);

                            player.spawnParticle(particleType, loc, 0);

                        }

                    }

                }

                if (t > Math.PI * 2) {

                    this.cancel();
                }
            }
        }.runTaskTimer(ssj, 0L, 1L);
    }
}
