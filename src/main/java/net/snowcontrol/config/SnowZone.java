package net.snowcontrol.config;

import com.google.gson.JsonObject;

/**
 * Represents a rectangular XZ region where snow accumulation is blocked.
 */
public class SnowZone {

    private final String name;
    private final int minX;
    private final int minZ;
    private final int maxX;
    private final int maxZ;

    public SnowZone(String name, int minX, int minZ, int maxX, int maxZ) {
        this.name = name;
        // Normalize so min <= max
        this.minX = Math.min(minX, maxX);
        this.minZ = Math.min(minZ, maxZ);
        this.maxX = Math.max(minX, maxX);
        this.maxZ = Math.max(minZ, maxZ);
    }

    public String getName() {
        return name;
    }

    public int getMinX() { return minX; }
    public int getMinZ() { return minZ; }
    public int getMaxX() { return maxX; }
    public int getMaxZ() { return maxZ; }

    /**
     * Returns true if the given block X/Z coordinate falls within this zone.
     */
    public boolean contains(int x, int z) {
        return x >= minX && x <= maxX && z >= minZ && z <= maxZ;
    }

    public JsonObject toJson() {
        JsonObject obj = new JsonObject();
        obj.addProperty("name", name);
        obj.addProperty("minX", minX);
        obj.addProperty("minZ", minZ);
        obj.addProperty("maxX", maxX);
        obj.addProperty("maxZ", maxZ);
        return obj;
    }

    public static SnowZone fromJson(JsonObject obj) {
        String name = obj.get("name").getAsString();
        int minX = obj.get("minX").getAsInt();
        int minZ = obj.get("minZ").getAsInt();
        int maxX = obj.get("maxX").getAsInt();
        int maxZ = obj.get("maxZ").getAsInt();
        return new SnowZone(name, minX, minZ, maxX, maxZ);
    }

    @Override
    public String toString() {
        return String.format("%s [(%d,%d) -> (%d,%d)]", name, minX, minZ, maxX, maxZ);
    }
}
