package org.apache.supersaiyan.MethodClasses;

import org.apache.supersaiyan.SSJ;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import java.util.Random;

public class SSJParticles {

    private final Player player;

    private final Particle particleType;

    private final int particleCount;

    private final double particleRange;

    private final SSJ ssj;

    private final Random random;

    public SSJParticles(SSJ ssj, Player player, Particle particleType, int particleCount, double particleRange) {

        this.ssj = ssj;

        this.player = player;

        this.particleType = particleType;

        this.particleCount = Math.min(particleCount, 20);

        this.particleRange = particleRange;

        this.random = new Random();

    }

    public void createParticles() {

        new BukkitRunnable() {

            double angle = 0;

            int tickCount = 0;

            @Override
            public void run() {

                tickCount++;

                if (tickCount % 2 != 0) return;

                Location playerLoc = player.getLocation();

                // Create base aura with reduced particle count
                for (int i = 0; i < particleCount; i++) {

                    double height = random.nextDouble() * 2.0;
                    double radius = particleRange * (1 + 0.2 * Math.sin(angle + i));

                    double x = radius * Math.cos(angle + (2 * Math.PI * i / particleCount));
                    double z = radius * Math.sin(angle + (2 * Math.PI * i / particleCount));

                    Location particleLoc = playerLoc.clone().add(x, height, z);

                    Vector offset = new Vector(
                        (random.nextDouble() - 0.5) * 0.2,
                        (random.nextDouble() - 0.5) * 0.2,
                        (random.nextDouble() - 0.5) * 0.2
                    );

                    player.getWorld().spawnParticle(
                        particleType,
                        particleLoc,
                        0,
                        offset.getX(),
                        0.1 + random.nextDouble() * 0.2,
                        offset.getZ()
                    );

                    // Reduce ground particle frequency (1 in 6 chance instead of 1 in 3)
                    if (random.nextInt(6) == 0) {

                        Location groundLoc = playerLoc.clone().add(
                            (random.nextDouble() - 0.5) * particleRange * 2,
                            0.1,
                            (random.nextDouble() - 0.5) * particleRange * 2
                        );

                        player.getWorld().spawnParticle(
                            particleType,
                            groundLoc,
                            0,
                            0,
                            0.05 + random.nextDouble() * 0.1,
                            0
                        );

                    }

                }

                angle += Math.PI / 16;

                if (angle >= Math.PI * 4) {

                    this.cancel();

                }

            }

        }.runTaskTimer(ssj, 0L, 1L);

    }

}
