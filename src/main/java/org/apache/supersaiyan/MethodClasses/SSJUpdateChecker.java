package org.apache.supersaiyan.MethodClasses;

import org.apache.supersaiyan.SSJ;
import org.bukkit.Bukkit;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;
import java.util.function.Consumer;

public class SSJUpdateChecker {

    private final SSJ ssj;

    private final int resourceId;

    public SSJUpdateChecker(SSJ ssj, int resourceId) {

        this.ssj = ssj;

        this.resourceId = resourceId;

    }

    public void getVersion(final Consumer<String> consumer) {

        Bukkit.getScheduler().runTaskAsynchronously(this.ssj, () -> {

            try (InputStream inputStream = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + this.resourceId).openStream(); Scanner scanner = new Scanner(inputStream)) {

                if (scanner.hasNext()) {

                    consumer.accept(scanner.next());

                }

            } catch (IOException exception) {

                ssj.getLogger().info("Unable to check for updates: " + exception.getMessage());

            }
        });
    }
}
