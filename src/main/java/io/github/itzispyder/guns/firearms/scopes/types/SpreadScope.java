package io.github.itzispyder.guns.firearms.scopes.types;

import io.github.itzispyder.guns.firearms.scopes.Scope;
import io.github.itzispyder.pdk.utils.raytracers.BlockDisplayRaytracer;
import io.github.itzispyder.pdk.utils.raytracers.CustomDisplayRaytracer;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class SpreadScope extends Scope {

    public SpreadScope(Player owner) {
        super(owner);
    }

    @Override
    public void spawn() {
        float pitch = owner.getLocation().getPitch();
        float yaw = owner.getLocation().getYaw();
        float radius = 0.12F;
        int stay = 20 * 60 * 5; // 5 minutes

        var loc = owner.getLocation().add(0, 1.27, 0);
        var dir = owner.getLocation().getDirection();
        var w = owner.getWorld();
        var center = CustomDisplayRaytracer.blocksInFrontOf(loc, dir, 0.25, true).getLoc();
        ArmorStand base;
        BlockDisplay part;

        var v1 = rotVec(center.clone().add(radius, 0, 0).toVector(), 0, -yaw, center.toVector());
        var v2 = rotVec(center.clone().add(-radius, 0, 0).toVector(), 0, -yaw, center.toVector());
        var v3 = rotVec(center.clone().add(0, radius / 2, 0).toVector(), 0, -yaw, center.toVector());
        var v4 = rotVec(center.clone().add(0, -radius / 2, 0).toVector(), 0, -yaw, center.toVector());

        base = newPartBase(toLoc(v1, w));
        part = addPart(BlockDisplayRaytracer.trace(Material.RED_CONCRETE, toLoc(v1, w), toLoc(v2, w), 0.0005, stay));
        part.setGlowColorOverride(Color.RED);
        part.setGlowing(true);
        base.addPassenger(part);

        base = newPartBase(toLoc(v3, w));
        part = addPart(BlockDisplayRaytracer.trace(Material.RED_CONCRETE, toLoc(v3, w), toLoc(v4, w), 0.0005, stay));
        part.setGlowColorOverride(Color.RED);
        part.setGlowing(true);
        base.addPassenger(part);


        v1 = rotVec(center.clone().add(radius / 2, -radius / 2, 0).toVector(), 0, -yaw, center.toVector());
        v2 = rotVec(center.clone().add(-radius / 2, -radius / 2, 0).toVector(), 0, -yaw, center.toVector());
        v3 = rotVec(center.clone().add(radius / 2, radius / 2, 0).toVector(), 0, -yaw, center.toVector());
        v4 = rotVec(center.clone().add(-radius / 2, radius / 2, 0).toVector(), 0, -yaw, center.toVector());
        base = newPartBase(toLoc(v1, w));
        part = addPart(BlockDisplayRaytracer.trace(Material.GRAY_CONCRETE, toLoc(v1, w), toLoc(v2, w), 0.006, stay));
        base.addPassenger(part);
        base = newPartBase(toLoc(v3, w));
        part = addPart(BlockDisplayRaytracer.trace(Material.GRAY_CONCRETE, toLoc(v3, w), toLoc(v4, w), 0.006, stay));
        base.addPassenger(part);

        v1 = rotVec(center.clone().add(-radius, radius / 4, 0).toVector(), 0, -yaw, center.toVector());
        v2 = rotVec(center.clone().add(-radius, -radius / 4, 0).toVector(), 0, -yaw, center.toVector());
        v3 = rotVec(center.clone().add(radius, radius / 4, 0).toVector(), 0, -yaw, center.toVector());
        v4 = rotVec(center.clone().add(radius, -radius / 4, 0).toVector(), 0, -yaw, center.toVector());
        base = newPartBase(toLoc(v1, w));
        part = addPart(BlockDisplayRaytracer.trace(Material.GRAY_CONCRETE, toLoc(v1, w), toLoc(v2, w), 0.006, stay));
        base.addPassenger(part);
        base = newPartBase(toLoc(v3, w));
        part = addPart(BlockDisplayRaytracer.trace(Material.GRAY_CONCRETE, toLoc(v3, w), toLoc(v4, w), 0.006, stay));
        base.addPassenger(part);


        v1 = rotVec(center.clone().add(-radius * 0.5, -radius * 0.5, 0).toVector(), 0, -yaw, center.toVector());
        v2 = rotVec(center.clone().add(-radius, -radius * 0.2, 0).toVector(), 0, -yaw, center.toVector());
        base = newPartBase(toLoc(v1, w));
        part = addPart(BlockDisplayRaytracer.trace(Material.GRAY_CONCRETE, toLoc(v1, w), toLoc(v2, w), 0.006, stay));
        base.addPassenger(part);

        v1 = rotVec(center.clone().add(radius * 0.5, -radius * 0.5, 0).toVector(), 0, -yaw, center.toVector());
        v2 = rotVec(center.clone().add(radius, -radius * 0.2, 0).toVector(), 0, -yaw, center.toVector());
        base = newPartBase(toLoc(v1, w));
        part = addPart(BlockDisplayRaytracer.trace(Material.GRAY_CONCRETE, toLoc(v1, w), toLoc(v2, w), 0.006, stay));
        base.addPassenger(part);

        v1 = rotVec(center.clone().add(-radius * 0.5, radius * 0.5, 0).toVector(), 0, -yaw, center.toVector());
        v2 = rotVec(center.clone().add(-radius, radius * 0.25, 0).toVector(), 0, -yaw, center.toVector());
        base = newPartBase(toLoc(v1, w));
        part = addPart(BlockDisplayRaytracer.trace(Material.GRAY_CONCRETE, toLoc(v1, w), toLoc(v2, w), 0.006, stay));
        base.addPassenger(part);

        v1 = rotVec(center.clone().add(radius * 0.5, radius * 0.5, 0).toVector(), 0, -yaw, center.toVector());
        v2 = rotVec(center.clone().add(radius, radius * 0.25, 0).toVector(), 0, -yaw, center.toVector());
        base = newPartBase(toLoc(v1, w));
        part = addPart(BlockDisplayRaytracer.trace(Material.GRAY_CONCRETE, toLoc(v1, w), toLoc(v2, w), 0.006, stay));
        base.addPassenger(part);


        for (int i = 1; i <= 5; i++) {
            v1 = rotVec(center.clone().add(0.02, -(i * 0.01), 0).toVector(), 0, -yaw, center.toVector());
            v2 = rotVec(center.clone().add(-0.02, -(i * 0.01), 0).toVector(), 0, -yaw, center.toVector());
            base = newPartBase(toLoc(v1, w));
            part = addPart(BlockDisplayRaytracer.trace(Material.RED_CONCRETE, toLoc(v1, w), toLoc(v2, w), 0.0005, stay));
            part.setGlowColorOverride(Color.RED);
            part.setGlowing(true);
            base.addPassenger(part);
        }
        for (int i = 0; i <= 360; i += 10) {
            if ((i < 25 || i > 55) && (i < 115 || i > 145) && (i < 205 || i > 235) && (i < 295 || i > 325))
                continue;

            float angle1 = (float)Math.toRadians(i);
            float angle2 = (float)Math.toRadians(i + 10);
            float x1 = (float)(center.getX() + Math.cos(angle1) * radius * 0.35);
            float y1 = (float)(center.getY() + Math.sin(angle1) * radius * 0.35);
            float x2 = (float)(center.getX() + Math.cos(angle2) * radius * 0.35);
            float y2 = (float)(center.getY() + Math.sin(angle2) * radius * 0.35);
            float z = (float)center.getZ();

            v1 = rotVec(new Vector(x1, y1, z), 0, -yaw, center.toVector());
            v2 = rotVec(new Vector(x2, y2, z), 0, -yaw, center.toVector());
            base = newPartBase(toLoc(v1, w));
            part = addPart(BlockDisplayRaytracer.trace(Material.RED_CONCRETE, toLoc(v1, w), toLoc(v2, w), 0.0005, stay));
            part.setGlowColorOverride(Color.RED);
            part.setGlowing(true);
            base.addPassenger(part);
        }
        for (int i = 0; i <= 360; i += 10) {
            if ((i < 25 || i > 55) && (i < 115 || i > 145) && (i < 205 || i > 235) && (i < 295 || i > 325))
                continue;

            float angle1 = (float)Math.toRadians(i);
            float angle2 = (float)Math.toRadians(i + 10);
            float x1 = (float)(center.getX() + Math.cos(angle1) * radius * 0.4);
            float y1 = (float)(center.getY() + Math.sin(angle1) * radius * 0.4);
            float x2 = (float)(center.getX() + Math.cos(angle2) * radius * 0.4);
            float y2 = (float)(center.getY() + Math.sin(angle2) * radius * 0.4);
            float z = (float)center.getZ();

            v1 = rotVec(new Vector(x1, y1, z), 0, -yaw, center.toVector());
            v2 = rotVec(new Vector(x2, y2, z), 0, -yaw, center.toVector());
            base = newPartBase(toLoc(v1, w));
            part = addPart(BlockDisplayRaytracer.trace(Material.RED_CONCRETE, toLoc(v1, w), toLoc(v2, w), 0.0005, stay));
            part.setGlowColorOverride(Color.RED);
            part.setGlowing(true);
            base.addPassenger(part);
        }
    }

    @Override
    public void tick() {
        int vectorIndex = 0;

        float pitch = owner.getLocation().getPitch();
        float yaw = owner.getLocation().getYaw();
        float radius = 0.12F;
        int stay = 20 * 60 * 5; // 5 minutes

        var loc = owner.getLocation().add(0, 1.27, 0);
        var dir = owner.getLocation().getDirection();
        var w = owner.getWorld();
        var center = CustomDisplayRaytracer.blocksInFrontOf(loc, dir, 0.25, true).getLoc();
        Entity base, part;

        var v1 = rotVec(center.clone().add(radius, 0, 0).toVector(), 0, -yaw, center.toVector());
        var v2 = rotVec(center.clone().add(-radius, 0, 0).toVector(), 0, -yaw, center.toVector());
        var v3 = rotVec(center.clone().add(0, radius / 2, 0).toVector(), 0, -yaw, center.toVector());
        var v4 = rotVec(center.clone().add(0, -radius / 2, 0).toVector(), 0, -yaw, center.toVector());

        base = parts.get(vectorIndex++);
        base.getPassengers().forEach(base::removePassenger);
        base.teleport(toLoc(v1, w));
        part = parts.get(vectorIndex++);
        part.teleport(part.getLocation().setDirection(v2.subtract(v1)));
        base.addPassenger(part);

        base = parts.get(vectorIndex++);
        base.getPassengers().forEach(base::removePassenger);
        base.teleport(toLoc(v3, w));
        part = parts.get(vectorIndex++);
        part.teleport(part.getLocation().setDirection(v4.subtract(v3)));
        base.addPassenger(part);


        v1 = rotVec(center.clone().add(radius / 2, -radius / 2, 0).toVector(), 0, -yaw, center.toVector());
        v2 = rotVec(center.clone().add(-radius / 2, -radius / 2, 0).toVector(), 0, -yaw, center.toVector());
        v3 = rotVec(center.clone().add(radius / 2, radius / 2, 0).toVector(), 0, -yaw, center.toVector());
        v4 = rotVec(center.clone().add(-radius / 2, radius / 2, 0).toVector(), 0, -yaw, center.toVector());
        base = parts.get(vectorIndex++);
        base.getPassengers().forEach(base::removePassenger);
        base.teleport(toLoc(v1, w));
        part = parts.get(vectorIndex++);
        part.teleport(part.getLocation().setDirection(v2.subtract(v1)));
        base.addPassenger(part);
        base = parts.get(vectorIndex++);
        base.getPassengers().forEach(base::removePassenger);
        base.teleport(toLoc(v3, w));
        part = parts.get(vectorIndex++);
        part.teleport(part.getLocation().setDirection(v4.subtract(v3)));
        base.addPassenger(part);

        v1 = rotVec(center.clone().add(-radius, radius / 4, 0).toVector(), 0, -yaw, center.toVector());
        v2 = rotVec(center.clone().add(-radius, -radius / 4, 0).toVector(), 0, -yaw, center.toVector());
        v3 = rotVec(center.clone().add(radius, radius / 4, 0).toVector(), 0, -yaw, center.toVector());
        v4 = rotVec(center.clone().add(radius, -radius / 4, 0).toVector(), 0, -yaw, center.toVector());
        base = parts.get(vectorIndex++);
        base.getPassengers().forEach(base::removePassenger);
        base.teleport(toLoc(v1, w));
        part = parts.get(vectorIndex++);
        part.teleport(part.getLocation().setDirection(v2.subtract(v1)));
        base.addPassenger(part);
        base = parts.get(vectorIndex++);
        base.getPassengers().forEach(base::removePassenger);
        base.teleport(toLoc(v3, w));
        part = parts.get(vectorIndex++);
        part.teleport(part.getLocation().setDirection(v4.subtract(v3)));
        base.addPassenger(part);


        v1 = rotVec(center.clone().add(-radius * 0.5, -radius * 0.5, 0).toVector(), 0, -yaw, center.toVector());
        v2 = rotVec(center.clone().add(-radius, -radius * 0.2, 0).toVector(), 0, -yaw, center.toVector());
        base = parts.get(vectorIndex++);
        base.getPassengers().forEach(base::removePassenger);
        base.teleport(toLoc(v1, w));
        part = parts.get(vectorIndex++);
        part.teleport(part.getLocation().setDirection(v2.subtract(v1)));
        base.addPassenger(part);

        v1 = rotVec(center.clone().add(radius * 0.5, -radius * 0.5, 0).toVector(), 0, -yaw, center.toVector());
        v2 = rotVec(center.clone().add(radius, -radius * 0.2, 0).toVector(), 0, -yaw, center.toVector());
        base = parts.get(vectorIndex++);
        base.getPassengers().forEach(base::removePassenger);
        base.teleport(toLoc(v1, w));
        part = parts.get(vectorIndex++);
        part.teleport(part.getLocation().setDirection(v2.subtract(v1)));
        base.addPassenger(part);

        v1 = rotVec(center.clone().add(-radius * 0.5, radius * 0.5, 0).toVector(), 0, -yaw, center.toVector());
        v2 = rotVec(center.clone().add(-radius, radius * 0.25, 0).toVector(), 0, -yaw, center.toVector());
        base = parts.get(vectorIndex++);
        base.getPassengers().forEach(base::removePassenger);
        base.teleport(toLoc(v1, w));
        part = parts.get(vectorIndex++);
        part.teleport(part.getLocation().setDirection(v2.subtract(v1)));
        base.addPassenger(part);

        v1 = rotVec(center.clone().add(radius * 0.5, radius * 0.5, 0).toVector(), 0, -yaw, center.toVector());
        v2 = rotVec(center.clone().add(radius, radius * 0.25, 0).toVector(), 0, -yaw, center.toVector());
        base = parts.get(vectorIndex++);
        base.getPassengers().forEach(base::removePassenger);
        base.teleport(toLoc(v1, w));
        part = parts.get(vectorIndex++);
        part.teleport(part.getLocation().setDirection(v2.subtract(v1)));
        base.addPassenger(part);


        for (int i = 1; i <= 5; i++) {
            v1 = rotVec(center.clone().add(0.02, -(i * 0.01), 0).toVector(), 0, -yaw, center.toVector());
            v2 = rotVec(center.clone().add(-0.02, -(i * 0.01), 0).toVector(), 0, -yaw, center.toVector());
            base = parts.get(vectorIndex++);
            base.getPassengers().forEach(base::removePassenger);
            base.teleport(toLoc(v1, w));
            part = parts.get(vectorIndex++);
            part.teleport(part.getLocation().setDirection(v2.subtract(v1)));
            base.addPassenger(part);
        }
        for (int i = 0; i <= 360; i += 10) {
            if ((i < 25 || i > 55) && (i < 115 || i > 145) && (i < 205 || i > 235) && (i < 295 || i > 325))
                continue;

            float angle1 = (float)Math.toRadians(i);
            float angle2 = (float)Math.toRadians(i + 10);
            float x1 = (float)(center.getX() + Math.cos(angle1) * radius * 0.35);
            float y1 = (float)(center.getY() + Math.sin(angle1) * radius * 0.35);
            float x2 = (float)(center.getX() + Math.cos(angle2) * radius * 0.35);
            float y2 = (float)(center.getY() + Math.sin(angle2) * radius * 0.35);
            float z = (float)center.getZ();

            v1 = rotVec(new Vector(x1, y1, z), 0, -yaw, center.toVector());
            v2 = rotVec(new Vector(x2, y2, z), 0, -yaw, center.toVector());
            base = parts.get(vectorIndex++);
            base.getPassengers().forEach(base::removePassenger);
            base.teleport(toLoc(v1, w));
            part = parts.get(vectorIndex++);
            part.teleport(part.getLocation().setDirection(v2.subtract(v1)));
            base.addPassenger(part);
        }
        for (int i = 0; i <= 360; i += 10) {
            if ((i < 25 || i > 55) && (i < 115 || i > 145) && (i < 205 || i > 235) && (i < 295 || i > 325))
                continue;

            float angle1 = (float)Math.toRadians(i);
            float angle2 = (float)Math.toRadians(i + 10);
            float x1 = (float)(center.getX() + Math.cos(angle1) * radius * 0.4);
            float y1 = (float)(center.getY() + Math.sin(angle1) * radius * 0.4);
            float x2 = (float)(center.getX() + Math.cos(angle2) * radius * 0.4);
            float y2 = (float)(center.getY() + Math.sin(angle2) * radius * 0.4);
            float z = (float)center.getZ();

            v1 = rotVec(new Vector(x1, y1, z), 0, -yaw, center.toVector());
            v2 = rotVec(new Vector(x2, y2, z), 0, -yaw, center.toVector());
            base = parts.get(vectorIndex++);
            base.getPassengers().forEach(base::removePassenger);
            base.teleport(toLoc(v1, w));
            part = parts.get(vectorIndex++);
            part.teleport(part.getLocation().setDirection(v2.subtract(v1)));
            base.addPassenger(part);
        }
    }
}
