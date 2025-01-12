package de.blazemcworld.fireflow.space;

import net.minestom.server.item.Material;

import java.util.Set;
import java.util.UUID;

public class SpaceInfo {

    public final int id;
    public String name;
    public Material icon;
    public UUID owner;
    public Set<UUID> developers;
    public Set<UUID> builders;

    public SpaceInfo(int id) {
        this.id = id;
    }

}
