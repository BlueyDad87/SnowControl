package net.snowcontrol.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.permission.Permissions;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.snowcontrol.SnowControlMod;
import net.snowcontrol.config.SnowZone;

import java.util.List;

/**
 * Registers the /snowcontrol command tree.
 *
 * Usage:
 *   /snowcontrol add <name> <minX> <minZ> <maxX> <maxZ>
 *   /snowcontrol remove <name>
 *   /snowcontrol list
 *   /snowcontrol reload
 */
public class SnowControlCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
            CommandManager.literal("snowcontrol")
                    // --- ADD ---
                .then(CommandManager.literal("add")
                    .then(CommandManager.argument("name", StringArgumentType.word())
                        .then(CommandManager.argument("minX", IntegerArgumentType.integer())
                            .then(CommandManager.argument("minZ", IntegerArgumentType.integer())
                                .then(CommandManager.argument("maxX", IntegerArgumentType.integer())
                                    .then(CommandManager.argument("maxZ", IntegerArgumentType.integer())
                                        .executes(SnowControlCommand::addZone)))))))

                // --- REMOVE ---
                .then(CommandManager.literal("remove")
                    .then(CommandManager.argument("name", StringArgumentType.word())
                        .executes(SnowControlCommand::removeZone)))

                // --- LIST ---
                .then(CommandManager.literal("list")
                    .executes(SnowControlCommand::listZones))

                // --- RELOAD ---
                .then(CommandManager.literal("reload")
                    .executes(SnowControlCommand::reload))
        );
    }

    // ---------------------------------------------------------------

    private static int addZone(CommandContext<ServerCommandSource> ctx) {
        String name = StringArgumentType.getString(ctx, "name");
        int minX = IntegerArgumentType.getInteger(ctx, "minX");
        int minZ = IntegerArgumentType.getInteger(ctx, "minZ");
        int maxX = IntegerArgumentType.getInteger(ctx, "maxX");
        int maxZ = IntegerArgumentType.getInteger(ctx, "maxZ");

        SnowZone zone = new SnowZone(name, minX, minZ, maxX, maxZ);
        boolean added = SnowControlMod.CONFIG.addZone(zone);

        if (added) {
            ctx.getSource().sendFeedback(
                () -> Text.literal("§a[SnowControl] Zone added: " + zone),
                true
            );
        } else {
            ctx.getSource().sendError(
                Text.literal("§c[SnowControl] A zone named '" + name + "' already exists.")
            );
        }
        return added ? 1 : 0;
    }

    private static int removeZone(CommandContext<ServerCommandSource> ctx) {
        String name = StringArgumentType.getString(ctx, "name");
        boolean removed = SnowControlMod.CONFIG.removeZone(name);

        if (removed) {
            ctx.getSource().sendFeedback(
                () -> Text.literal("§a[SnowControl] Zone '" + name + "' removed."),
                true
            );
        } else {
            ctx.getSource().sendError(
                Text.literal("§c[SnowControl] No zone found with name '" + name + "'.")
            );
        }
        return removed ? 1 : 0;
    }

    private static int listZones(CommandContext<ServerCommandSource> ctx) {
        List<SnowZone> zones = SnowControlMod.CONFIG.getZones();
        if (zones.isEmpty()) {
            ctx.getSource().sendFeedback(
                () -> Text.literal("§e[SnowControl] No snow exclusion zones defined."),
                false
            );
        } else {
            StringBuilder sb = new StringBuilder("§b[SnowControl] Active zones (" + zones.size() + "):\n");
            for (SnowZone z : zones) {
                sb.append("  §f- ").append(z).append("\n");
            }
            final String msg = sb.toString().trim();
            ctx.getSource().sendFeedback(() -> Text.literal(msg), false);
        }
        return zones.size();
    }

    private static int reload(CommandContext<ServerCommandSource> ctx) {
        SnowControlMod.CONFIG.load();
        ctx.getSource().sendFeedback(
            () -> Text.literal("§a[SnowControl] Config reloaded. " +
                SnowControlMod.CONFIG.getZones().size() + " zone(s) loaded."),
            true
        );
        return 1;
    }
}
