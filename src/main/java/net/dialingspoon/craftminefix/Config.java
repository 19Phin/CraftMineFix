package net.dialingspoon.craftminefix;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;

public class Config {
    private static final Path CONFIG_PATH = Paths.get("config", "craftminefix.config");

    public static boolean DRY_LAND_ENABLED;
    public static boolean SHIELDS_ENABLED;
    public static boolean EXPLOSION_RESIST_ENABLED;
    public static boolean WITHER_SKELETON_EGG;
    public static boolean BUNDLED_REWARDS_ENABLED;
    public static boolean ARMOR_ENABLED;
    public static boolean BATTLES_FIX_ENABLED;

    private static final Map<String, Boolean> DEFAULTS = new LinkedHashMap<>();
    static {
        DEFAULTS.put("dry_land", true);
        DEFAULTS.put("shields", true);
        DEFAULTS.put("explosion_resistance", true);
        DEFAULTS.put("wither_skeleton_egg", true);
        DEFAULTS.put("bundled_rewards", true);
        DEFAULTS.put("armor", false);
        DEFAULTS.put("battles", false);
    }

    private static final Map<String, Boolean> values = new LinkedHashMap<>();

    public static void load() {
        try {
            Files.createDirectories(CONFIG_PATH.getParent());
        } catch (IOException e) {
            CraftMineFix.LOGGER.warn("Failed to create config directory: " + e);
        }
        if (!Files.exists(CONFIG_PATH)) {
            createDefaultConfigFile();
        }

        values.putAll(DEFAULTS);

        try (BufferedReader reader = Files.newBufferedReader(CONFIG_PATH, StandardCharsets.UTF_8)) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();

                int eqIndex = line.indexOf('=');
                if (eqIndex <= 0) {
                    continue;
                }

                String key = line.substring(0, eqIndex).trim();
                String val = line.substring(eqIndex + 1).trim();

                if (!values.containsKey(key)) {
                    continue;
                }

                if (val.startsWith("true")) {
                    values.put(key, true);
                } else if (val.startsWith("false")) {
                    values.put(key, false);
                } else {
                    CraftMineFix.LOGGER.warn("Invalid boolean for key '" + key + "': " + val + " (using default: " + values.get(key) + ")");
                }
            }
        } catch (IOException e) {
            CraftMineFix.LOGGER.warn("Failed reading config file " + CONFIG_PATH + ": " + e);
        }

        DRY_LAND_ENABLED         = values.get("dry_land");
        SHIELDS_ENABLED          = values.get("shields");
        EXPLOSION_RESIST_ENABLED = values.get("explosion_resistance");
        WITHER_SKELETON_EGG      = values.get("wither_skeleton_egg");
        BUNDLED_REWARDS_ENABLED  = values.get("bundled_rewards");
        ARMOR_ENABLED            = values.get("armor");
        BATTLES_FIX_ENABLED      = values.get("battles");
    }

    private static void createDefaultConfigFile() {
        try (BufferedWriter writer = Files.newBufferedWriter(CONFIG_PATH, StandardCharsets.UTF_8)) {
            for (Map.Entry<String, Boolean> entry : DEFAULTS.entrySet()) {
                writer.write(entry.getKey() + " = " + entry.getValue());
                writer.newLine();
            }
            CraftMineFix.LOGGER.info("Created default config at " + CONFIG_PATH.toAbsolutePath());
        } catch (IOException e) {
            CraftMineFix.LOGGER.warn("Failed to create default config file: " + e);
        }
    }
}

