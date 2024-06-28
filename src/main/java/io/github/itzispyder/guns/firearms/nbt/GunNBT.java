package io.github.itzispyder.guns.firearms.nbt;

import io.github.itzispyder.guns.data.PersistentData;
import io.github.itzispyder.guns.data.PersistentDataSerializable;
import io.github.itzispyder.guns.firearms.ShootingManager;
import io.github.itzispyder.guns.firearms.scopes.ScopeType;
import io.github.itzispyder.pdk.utils.MathUtils;
import io.github.itzispyder.pdk.utils.SchedulerUtils;
import io.github.itzispyder.pdk.utils.ServerUtils;
import io.github.itzispyder.pdk.utils.misc.SoundPlayer;
import io.github.itzispyder.pdk.utils.raytracers.CustomDisplayRaytracer;
import org.bukkit.*;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

// §
public class GunNBT implements PersistentDataSerializable {

    public Event impact, fire;

    public HitscanMode hitscan;
    public BallisticsMode ballistics;
    public RaycastMode raycastMode;

    public ScopeType scopeType;
    public int scopeSlownessAmplifier;

    public double maxUncertainty, damage, sneakUncertaintyMultiplier;
    public int ammo, maxAmmo, reloadTicks, roundsPerShot, cooldownTicks, repetitionPeriod, repetitionIterations;

    public GunNBT() {
        fire = new Event();
        fire.sounds.add(new SoundInstance(Sound.ENTITY_FIREWORK_ROCKET_BLAST, 10F, 0.1F, 15));
        impact = new Event();
        impact.sounds.add(new SoundInstance(Sound.ITEM_HOE_TILL, 10F, 2F, 5));
        impact.particles.add(new ParticleInstance(Particle.ELECTRIC_SPARK, 10, 0.0625, 0.0625, 0.0625, 1));

        hitscan = new HitscanMode(32);
        ballistics = new BallisticsMode(0, 2, true, Color.AQUA);
        raycastMode = RaycastMode.HITSCAN;

        maxUncertainty = 0.069420;
        damage = 4;
        sneakUncertaintyMultiplier = 0.5;

        scopeType = ScopeType.NONE;
        scopeSlownessAmplifier = 5;

        ammo = 10;
        maxAmmo = 10;
        reloadTicks = 20;
        roundsPerShot = 1;
        cooldownTicks = 1;
        repetitionPeriod = 3;
        repetitionIterations = 1;
    }

    @SuppressWarnings("deprecation")
    public void updateItemMeta(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        List<String> lore = new ArrayList<>();

        lore.add("§7§oRight click to shoot");
        lore.add("§7§oLeft click to reload");
        lore.add("");
        lore.add("§7Ammo: §f%s/%s".formatted(ammo, maxAmmo));

        switch (raycastMode) {
            case HITSCAN -> lore.add("§7Range: §f%s".formatted(MathUtils.round(hitscan.distance, 10)));
            case BALLISTICS -> lore.add("§7Ammo Weight: §f%s".formatted(MathUtils.round(ballistics.bulletDropPerTick, 10)));
        }

        lore.add("§7Damage: §f%s".formatted(MathUtils.round(damage, 10)));
        lore.add("§7Uncertainty: §f%s".formatted(MathUtils.round(maxUncertainty, 10)));

        meta.setLore(lore);
        meta.addItemFlags(ItemFlag.HIDE_ITEM_SPECIFICS, ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);

        PersistentData nbt = new PersistentData(meta.getPersistentDataContainer());
        nbt.write("gun_nbt", this);

        item.setItemMeta(meta);
    }

    public void onShoot(Player player, ItemStack item) {
        if (player.hasCooldown(item.getType()))
            return;
        if (ammo <= 0) {
            reload(player, item);
            return;
        }

        SchedulerUtils.loop(repetitionPeriod, repetitionIterations, iteration -> fire(player, item));
    }

    private void fire(Player player, ItemStack item) {
        if (ammo <= 0 || raycastMode == null)
            return;

        player.setCooldown(item.getType(), cooldownTicks + 1);
        ammo--;
        updateItemMeta(item);

        Location[] results = new Location[roundsPerShot];
        double uncertainty = !player.isSneaking() ? maxUncertainty : maxUncertainty * sneakUncertaintyMultiplier;
        boolean instant = raycastMode.isInstant();

        if (!instant)
            fire.trigger(CustomDisplayRaytracer.blocksInFrontOf(player.getEyeLocation(), player.getLocation().getDirection(), 0.1, false).getLoc());
        for (int i = 0; i < roundsPerShot; i++) {
            switch (raycastMode) {
                case HITSCAN -> results[i] = ShootingManager.shootHitscan(player, uncertainty, hitscan, damage).getLoc();
                case BALLISTICS -> ShootingManager.shootBallistics(player, uncertainty, ballistics, damage, impact);
            }
        }
        if (instant)
            ShootingManager.playSound(player, fire, impact, results);
        sendActionBar(player);

        player.getWorld().spawn(player.getLocation().add(0, 1, 0), Item.class, ent -> {
            ent.setItemStack(new ItemStack(Material.IRON_NUGGET));
            ent.setCanMobPickup(false);
            ent.setCanPlayerPickup(false);
            SchedulerUtils.later(15, () -> {
                SoundPlayer sound = new SoundPlayer(ent.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.5F, 2F);
                sound.playWithin(2);
                ent.remove();
            });
        });
    }

    public void reload(Player player, ItemStack item) {
        if (ammo >= maxAmmo)
            return;
        if (player.hasCooldown(item.getType()))
            return;

        player.setCooldown(item.getType(), reloadTicks + 1);

        SoundPlayer sound = new SoundPlayer(player.getLocation(), Sound.ITEM_FLINTANDSTEEL_USE, 1F, 0.1F);
        sound.playWithin(10);

        SchedulerUtils.later(reloadTicks, () ->  {
            ammo = maxAmmo;
            updateItemMeta(item);
            sendActionBar(player);

            sound.changePlayer(player.getLocation(), Sound.BLOCK_IRON_TRAPDOOR_OPEN, 1F, 1.5F);
            sound.playWithin(5);
            SchedulerUtils.later(5, () -> sound.playWithin(5));
        });
    }

    public boolean hasScope() {
        return scopeType != null && scopeType != ScopeType.NONE;
    }

    public void sendActionBar(Player player) {
        ServerUtils.sendActionBar(player, "§8<< §e%s§7/%s §8>>".formatted(ammo, maxAmmo));
    }
}
