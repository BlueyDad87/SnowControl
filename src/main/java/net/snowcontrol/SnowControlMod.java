package net.snowcontrol;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.snowcontrol.command.SnowControlCommand;
import net.snowcontrol.config.SnowControlConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * SnowControl — server-side Fabric mod.
 *
 * Prevents snow and ice placement within operator-defined XZ regions.
 * Zones are persisted to config/snowcontrol/zones.json and manageable
 * in-game via /snowcontrol add|remove|list|reload (requires op level 2).
 */
public class SnowControlMod implements ModInitializer {

    public static final String MOD_ID = "snowcontrol";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    /** Global config instance shared with the mixin and commands. */
    public static final SnowControlConfig CONFIG = new SnowControlConfig();

    @Override
    public void onInitialize() {
        LOGGER.info("[SnowControl] Initializing Snow Control mod...");

        // Load persisted zones
        CONFIG.load();

        // Register commands
        CommandRegistrationCallback.EVENT.register(
            (dispatcher, registryAccess, environment) ->
                SnowControlCommand.register(dispatcher)
        );

        LOGGER.info("[SnowControl] Ready. {} zone(s) loaded.", CONFIG.getZones().size());
    }
}
