package net.snowcontrol.config;

import com.google.gson.*;
import net.fabricmc.loader.api.FabricLoader;
import net.snowcontrol.SnowControlMod;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Manages persistence of snow exclusion zones in
 * config/snowcontrol/zones.json
 */
public class SnowControlConfig {

    private static final Path CONFIG_DIR = FabricLoader.getInstance()
            .getConfigDir()
            .resolve("snowcontrol");
    private static final Path CONFIG_FILE = CONFIG_DIR.resolve("zones.json");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    /** Thread-safe list of active zones */
    private final List<SnowZone> zones = new CopyOnWriteArrayList<>();

    // ---------------------------------------------------------------
    // Public API
    // ---------------------------------------------------------------

    public List<SnowZone> getZones() {
        return Collections.unmodifiableList(zones);
    }

    public boolean addZone(SnowZone zone) {
        // Prevent duplicate names
        for (SnowZone z : zones) {
            if (z.getName().equalsIgnoreCase(zone.getName())) return false;
        }
        zones.add(zone);
        save();
        return true;
    }

    public boolean removeZone(String name) {
        boolean removed = zones.removeIf(z -> z.getName().equalsIgnoreCase(name));
        if (removed) save();
        return removed;
    }

    /**
     * Returns true if ANY active zone covers the given XZ position.
     */
    public boolean isBlocked(int x, int z) {
        for (SnowZone zone : zones) {
            if (zone.contains(x, z)) return true;
        }
        return false;
    }

    // ---------------------------------------------------------------
    // Serialization
    // ---------------------------------------------------------------

    public void load() {
        zones.clear();
        try {
            Files.createDirectories(CONFIG_DIR);
            if (!Files.exists(CONFIG_FILE)) {
                SnowControlMod.LOGGER.info("[SnowControl] No config found; starting with empty zone list.");
                return;
            }
            try (Reader reader = Files.newBufferedReader(CONFIG_FILE)) {
                JsonArray arr = GSON.fromJson(reader, JsonArray.class);
                if (arr != null) {
                    for (JsonElement el : arr) {
                        zones.add(SnowZone.fromJson(el.getAsJsonObject()));
                    }
                }
            }
            SnowControlMod.LOGGER.info("[SnowControl] Loaded {} snow exclusion zone(s).", zones.size());
        } catch (Exception e) {
            SnowControlMod.LOGGER.error("[SnowControl] Failed to load config: {}", e.getMessage());
        }
    }

    public void save() {
        try {
            Files.createDirectories(CONFIG_DIR);
            JsonArray arr = new JsonArray();
            for (SnowZone z : zones) arr.add(z.toJson());
            try (Writer writer = Files.newBufferedWriter(CONFIG_FILE)) {
                GSON.toJson(arr, writer);
            }
        } catch (Exception e) {
            SnowControlMod.LOGGER.error("[SnowControl] Failed to save config: {}", e.getMessage());
        }
    }
}
