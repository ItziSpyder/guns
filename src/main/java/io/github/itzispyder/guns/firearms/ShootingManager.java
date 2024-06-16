package io.github.itzispyder.guns.firearms;

import io.github.itzispyder.guns.Guns;
import io.github.itzispyder.guns.commands.BlockCollisionsCommand;
import io.github.itzispyder.guns.firearms.scopes.Scope;
import io.github.itzispyder.pdk.utils.misc.Randomizer;
import io.github.itzispyder.pdk.utils.misc.SoundPlayer;
import io.github.itzispyder.pdk.utils.raytracers.BlockDisplayRaytracer;
import io.github.itzispyder.pdk.utils.raytracers.CustomDisplayRaytracer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

public class ShootingManager {

    public static final Map<UUID, Scope> scopeViewers = new HashMap<>();

    public static void onTick() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            UUID id = player.getUniqueId();
            boolean viewing = scopeViewers.containsKey(id);

            if (!player.isSneaking()) {
                if (viewing) {
                    scopeViewers.get(id).kill();
                    scopeViewers.remove(id);
                }
                continue;
            }

            ItemStack item = player.getInventory().getItemInMainHand();
            if (item.getType().isAir() || item.getType().isEmpty())
                continue;

            GunNBT gun = Guns.getGun(item);
            if (gun == null || !gun.hasScope() || (viewing && scopeViewers.get(id).getType() != gun.scopeType)) {
                if (viewing) {
                    scopeViewers.get(id).kill();
                    scopeViewers.remove(id);
                }
                continue;
            }
            if (!viewing) {
                Scope scope = gun.scopeType.createScope(player);
                scopeViewers.put(id, scope);
            }

            Scope scope = scopeViewers.get(id);
            if (scope.getParts().isEmpty())
                scope.spawn();
            else
                scope.tick();

            PotionEffect potion = new PotionEffect(PotionEffectType.SLOW, 2, gun.scopeSlownessAmplifier, false, false, false);
            player.addPotionEffect(potion);
        }
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
            AtomicBoolean target = new AtomicBoolean(false);
            point.getNearbyEntities(shooter, 5, true, 0.2, ent -> {
                return ent instanceof LivingEntity && !ent.isDead() && !(ent instanceof ArmorStand);
            }).forEach(ent -> {
                target.set(true);
                damage((LivingEntity) ent, shooter, damage);
            });
            return target.get() || BlockCollisionsCommand.collidesWidth(point.getBlock(), point.getLoc());
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
