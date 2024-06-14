package io.github.itzispyder.guns.firearms;

import io.github.itzispyder.guns.data.PersistentData;
import io.github.itzispyder.guns.data.PersistentDataSerializable;
import io.github.itzispyder.pdk.utils.MathUtils;
import io.github.itzispyder.pdk.utils.SchedulerUtils;
import io.github.itzispyder.pdk.utils.ServerUtils;
import io.github.itzispyder.pdk.utils.misc.SoundPlayer;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

// §
public class GunNBT implements PersistentDataSerializable {

    public Sound shootSound;
    public float shootSoundPitch, shootSoundVolume;

    public double maxUncertainty, distance, damage, sneakUncertaintyMultiplier;
    public int ammo, maxAmmo, reloadTicks, roundsPerShot, cooldownTicks;

    public GunNBT() {
        shootSound = Sound.ENTITY_FIREWORK_ROCKET_BLAST;
        shootSoundVolume = 10F;
        shootSoundPitch = 0.1F;

        maxUncertainty = 0.069420;
        distance = 32;
        damage = 4;
        sneakUncertaintyMultiplier = 0.5;

        ammo = 10;
        maxAmmo = 10;
        reloadTicks = 20;
        roundsPerShot = 1;
        cooldownTicks = 1;
    }

    @SuppressWarnings("deprecation")
    public void updateItemMeta(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        List<String> lore = new ArrayList<>();

        lore.add("§7§oRight click to shoot");
        lore.add("§7§oLeft click to reload");
        lore.add("");
        lore.add("§7Ammo: §f%s/%s".formatted(ammo, maxAmmo));
        lore.add("§7Range: §f%s".formatted(MathUtils.round(distance, 10)));
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
        player.setCooldown(item.getType(), cooldownTicks + 1);
        ammo--;
        updateItemMeta(item);

        Location[] results = new Location[roundsPerShot];
        double uncertainty = !player.isSneaking() ? maxUncertainty : maxUncertainty * sneakUncertaintyMultiplier;
        for (int i = 0; i < roundsPerShot; i++) {
            results[i] = ShootingManager.shoot(player, uncertainty, distance, damage).getLoc();
        }
        ShootingManager.playSound(player, shootSound, shootSoundVolume, shootSoundPitch, results);
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

    public void sendActionBar(Player player) {
        ServerUtils.sendActionBar(player, "§8<< §e%s§7/%s §8>>".formatted(ammo, maxAmmo));
    }
}
