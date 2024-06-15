package io.github.itzispyder.guns.firearms;

import io.github.itzispyder.guns.firearms.scopes.Scope;
import io.github.itzispyder.pdk.utils.misc.Randomizer;
import io.github.itzispyder.pdk.utils.misc.SoundPlayer;
import io.github.itzispyder.pdk.utils.raytracers.BlockDisplayRaytracer;
import io.github.itzispyder.pdk.utils.raytracers.CustomDisplayRaytracer;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ShootingManager {

    public static final Map<UUID, Scope> scopeViewers = new HashMap<>();

    public static void onTick() {

    }

    public static CustomDisplayRaytracer.Point shoot(LivingEntity shooter, double distance, double damage) {
        return shoot(shooter, 0.0, distance, damage);
    }

    public static CustomDisplayRaytracer.Point shoot(LivingEntity shooter, double maxUncertainty, double distance, double damage) {
        Vector dir = shooter.getLocation().getDirection();
        Location eye = CustomDisplayRaytracer.blocksInFrontOf(shooter.getEyeLocation(), dir, 0.1, true).getLoc();
        Randomizer r = new Randomizer();

        double x = r.getRandomDouble(-maxUncertainty, maxUncertainty);
        double y = r.getRandomDouble(-maxUncertainty, maxUncertainty);
        double z = r.getRandomDouble(-maxUncertainty, maxUncertainty);
        dir.add(new Vector(x, y, z));

        var hit = CustomDisplayRaytracer.trace(eye, dir, distance, 0.3, point -> {
            point.getNearbyEntities(shooter, 5, true, 0.2, ent -> {
                return ent instanceof LivingEntity && !ent.isDead();
            }).forEach(ent -> {
                damage((LivingEntity) ent, shooter, damage);
            });
            return CustomDisplayRaytracer.hitAnythingExclude(shooter).test(point);
        });
        BlockDisplayRaytracer.trace(Material.WHITE_CONCRETE, eye, hit.getLoc(), 0.0069, 3);
        return hit;
    }

    public static void playSound(LivingEntity shooter, Location... hitResults) {
        playSound(shooter, Sound.BLOCK_BEACON_DEACTIVATE, 10F, 2F, hitResults);
    }

    public static void playSound(LivingEntity shooter, Sound shootSound, float shootVolume, float shootPitch, Location... hitResults) {
        SoundPlayer sp = new SoundPlayer(shooter.getEyeLocation(), shootSound, shootVolume, shootPitch);
        sp.playWithin(15);

        sp.setSound(Sound.ITEM_HOE_TILL);
        sp.setVolume(10F);
        sp.setPitch(2F);
        for (Location hitResult : hitResults) {
            if (hitResult == null) {
                continue;
            }
            sp.setLocation(hitResult);
            sp.playWithin(5);
        }
    }

    public static synchronized void damage(LivingEntity target, LivingEntity attacker, double amount) {
        if (target == null || attacker == null) {
            return;
        }

        int tick = target.getNoDamageTicks();
        int maxTick = target.getMaximumNoDamageTicks();

        target.setNoDamageTicks(0);
        target.setMaximumNoDamageTicks(0);
        target.damage(amount, attacker);

        target.setNoDamageTicks(tick);
        target.setMaximumNoDamageTicks(maxTick);
    }
}
