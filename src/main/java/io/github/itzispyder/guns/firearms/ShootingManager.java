package io.github.itzispyder.guns.firearms;

import io.github.itzispyder.guns.Guns;
import io.github.itzispyder.guns.commands.BlockCollisionsCommand;
import io.github.itzispyder.guns.firearms.nbt.Event;
import io.github.itzispyder.guns.firearms.nbt.GunNBT;
import io.github.itzispyder.guns.firearms.scopes.Scope;
import io.github.itzispyder.pdk.utils.SchedulerUtils;
import io.github.itzispyder.pdk.utils.misc.Randomizer;
import io.github.itzispyder.pdk.utils.misc.SoundPlayer;
import io.github.itzispyder.pdk.utils.raytracers.BlockDisplayRaytracer;
import io.github.itzispyder.pdk.utils.raytracers.CustomDisplayRaytracer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.joml.AxisAngle4f;
import org.joml.Vector3f;

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

    public static CustomDisplayRaytracer.Point shootHitscan(LivingEntity shooter, GunNBT gun) {
        Vector dir = shooter.getLocation().getDirection();
        Location eye = CustomDisplayRaytracer.blocksInFrontOf(shooter.getEyeLocation(), dir, 0.1, true).getLoc();
        Randomizer r = new Randomizer();
        double uncertainty = !shooter.isSneaking() ? gun.maxUncertainty : gun.maxUncertainty * gun.sneakUncertaintyMultiplier;

        double x = r.getRandomDouble(-uncertainty, uncertainty);
        double y = r.getRandomDouble(-uncertainty, uncertainty);
        double z = r.getRandomDouble(-uncertainty, uncertainty);
        dir.add(new Vector(x, y, z));

        var hit = CustomDisplayRaytracer.trace(eye, dir, gun.hitscan.distance, 0.1, point -> {
            AtomicBoolean target = new AtomicBoolean(false);
            point.getNearbyEntities(shooter, 5, true, 0.2, ent -> {
                return ent instanceof LivingEntity && !ent.isDead() && !(ent instanceof ArmorStand);
            }).forEach(ent -> {
                target.set(true);
                damage((LivingEntity) ent, shooter, gun.damage);
            });
            return target.get() || BlockCollisionsCommand.collidesWidth(point.getBlock(), point.getLoc());
        });
        BlockDisplayRaytracer.trace(gun.getDisplay(), eye, hit.getLoc(), 0.0069, 3);
        return hit;
    }

    public static void shootBallistics(LivingEntity shooter, GunNBT gun) {
        World world = shooter.getWorld();
        Vector dir = shooter.getLocation().getDirection();
        Location eye = shooter.getEyeLocation();
        Randomizer r = new Randomizer();
        double uncertainty = !shooter.isSneaking() ? gun.maxUncertainty : gun.maxUncertainty * gun.sneakUncertaintyMultiplier;

        double x = r.getRandomDouble(-uncertainty, uncertainty);
        double y = r.getRandomDouble(-uncertainty, uncertainty);
        double z = r.getRandomDouble(-uncertainty, uncertainty);
        dir.add(new Vector(x, y, z));

        float thickness = 0.0625F; // one block pixel
        int maxDistance = 32;

        // laser bolt
        BlockDisplay block = world.spawn(eye, BlockDisplay.class);
        AxisAngle4f angle = new AxisAngle4f(0, 0, 0, 1);
        Vector3f translation = new Vector3f(-thickness / 2);
        Vector3f scale = new Vector3f(0, 0, 0);

        Location teleport = block.getLocation();
        teleport.setDirection(dir);
        block.teleport(teleport);
        block.setBlock(gun.getDisplay().createBlockData());
        block.setGlowColorOverride(gun.ballistics.glowColor);
        block.setGlowing(gun.ballistics.glowing);
        block.setInterpolationDelay(0);
        block.setTransformation(new Transformation(translation, angle, scale, angle));

        // laser travel passenger
        ArmorStand stand = world.spawn(eye, ArmorStand.class, initStand ->  {
            initStand.setMarker(true);
            initStand.setArms(false);
            initStand.setSmall(true);
            initStand.setGravity(false);
            initStand.setBasePlate(false);
            initStand.setInvulnerable(true);
            initStand.setInvisible(true);
        });

        stand.addPassenger(block);

        SchedulerUtils.later(3, () -> {
            Vector3f rescale = new Vector3f(thickness, thickness, 1);
            block.setInterpolationDelay(0);
            block.setInterpolationDuration(1);
            block.setTransformation(new Transformation(translation, angle, rescale, angle));
        });

        AtomicBoolean canTravel = new AtomicBoolean(true);
        SchedulerUtils.whileLoop(1, canTravel::get, i -> {
            if (i >= maxDistance) {
                canTravel.set(false);
                boltTravel(shooter, stand, block, gun);
                stand.remove();
                block.remove();
                return;
            }
            canTravel.set(boltTravel(shooter, stand, block, gun));
        });
    }

    public static void playSound(LivingEntity shooter, Event fireEvent, Event impactEvent, Location... hitResults) {
        Location loc = CustomDisplayRaytracer.blocksInFrontOf(shooter.getEyeLocation(), shooter.getLocation().getDirection(), 0.1, false).getLoc();
        SoundPlayer sp = new SoundPlayer(loc, null, 0, 0);
        fireEvent.trigger(sp);

        for (Location hitResult : hitResults)
            if (hitResult != null)
                impactEvent.trigger(hitResult);
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

    private static boolean boltTravel(LivingEntity shooter, ArmorStand base, BlockDisplay bolt, GunNBT gun) {
        double delta = gun.ballistics.blocksPerTick;

        Location loc = bolt.getLocation();
        loc.setPitch((float)(loc.getPitch() + gun.ballistics.bulletDropPerTick));
        Vector dir = loc.getDirection().normalize();
        Location teleport = loc.clone().add(dir.multiply(delta));

        base.getPassengers().forEach(base::removePassenger);
        base.teleport(teleport);
        bolt.teleport(teleport);
        base.addPassenger(bolt);

        for (double travel = 0; travel < delta; travel += 0.0625) {
            CustomDisplayRaytracer.Point point = CustomDisplayRaytracer.blocksInFrontOf(loc, dir, travel, true);
            AtomicBoolean target = new AtomicBoolean(false);

            point.getNearbyEntities(shooter, 5, true, 0.2, ent -> {
                return ent instanceof LivingEntity && !ent.isDead() && !(ent instanceof ArmorStand);
            }).forEach(ent -> {
                target.set(true);
                damage((LivingEntity) ent, shooter, gun.damage);
            });

            if (target.get() || CustomDisplayRaytracer.HIT_BLOCK.test(point)) {
                gun.impact.trigger(point.getLoc());
                bolt.remove();
                base.remove();
                return false;
            }
        }
        return true;
    }
}
