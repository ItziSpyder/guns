package io.github.itzispyder.guns.firearms.scopes;

import io.github.itzispyder.guns.firearms.ShootingManager;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public abstract class Scope {

    protected final List<Entity> parts = new ArrayList<>();
    protected final Player owner;
    protected final ScopeType type;

    public Scope(ScopeType type, Player owner) {
        this.type = type;
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

    public ScopeType getType() {
        return type;
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
        float x = (float)Math.toRadians(pitch);
        float y = (float)Math.toRadians(yaw);
        Quaternionf rotationPitch = new Quaternionf().rotationX(x);
        Quaternionf rotationYaw = new Quaternionf().rotationY(y);
        Quaternionf rotation = rotationYaw.mul(rotationPitch);
        Vector3f trans = vec.subtract(origin).toVector3f();

        trans = rotation.transform(trans).add(origin.toVector3f());
        return new Vector(trans.x, trans.y, trans.z);
    }
}
