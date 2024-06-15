package io.github.itzispyder.guns.firearms.scopes;

import io.github.itzispyder.guns.firearms.ShootingManager;
import io.github.itzispyder.guns.utils.Rotations;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public abstract class Scope {

    protected final List<Entity> parts = new ArrayList<>();
    protected final Player owner;

    public Scope(Player owner) {
        this.owner = owner;
        ShootingManager.scopeViewers.put(owner.getUniqueId(), this);
    }

    public abstract void spawn();
    public abstract void tick();

    public void kill() {
        parts.forEach(Entity::remove);
        parts.clear();
        ShootingManager.scopeViewers.remove(owner.getUniqueId());
    }

    public Player getOwner() {
        return owner;
    }

    public List<Entity> getParts() {
        return parts;
    }

    public <T extends Entity> T addPart(T part) {
        if (part != null)
            parts.add(part);
        return part;
    }

    public void removePart(Entity part) {
        if (part != null)
            parts.add(part);
    }

    protected ArmorStand newPartBase(Location loc) {
        return addPart(loc.getWorld().spawn(loc, ArmorStand.class, stand -> {
            stand.setSmall(true);
            stand.setInvisible(true);
            stand.setInvulnerable(true);
            stand.setBasePlate(false);
            stand.setArms(false);
            stand.setMarker(true);
        }));
    }

    protected Location toLoc(Vector vec, World world) {
        return new Location(world, vec.getX(), vec.getY(), vec.getZ());
    }

    protected Vector rotVec(Vector vec, float pitch, float yaw, Vector origin) {
        return Rotations.Vectors.XYZ.rotateVectorDegrees(
                (float)vec.getX(), (float)vec.getY(), (float)vec.getZ(),
                pitch, yaw, 0,
                (float)origin.getX(), (float)origin.getY(), (float)origin.getZ()
        );
    }
}
