package io.github.itzispyder.guns;

import io.github.itzispyder.guns.commands.BlockCollisionsCommand;
import io.github.itzispyder.guns.commands.GunsCommand;
import io.github.itzispyder.guns.data.PersistentData;
import io.github.itzispyder.guns.events.PlayerEventListener;
import io.github.itzispyder.guns.events.ServerEventListener;
import io.github.itzispyder.guns.firearms.GunPresets;
import io.github.itzispyder.guns.firearms.ShootingManager;
import io.github.itzispyder.guns.firearms.nbt.GunNBT;
import io.github.itzispyder.guns.firearms.nbt.presets.*;
import io.github.itzispyder.pdk.PDK;
import io.github.itzispyder.pdk.utils.misc.config.JsonSerializable;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public final class Guns extends JavaPlugin {

    public static final GunPresets gunPresets = JsonSerializable.load(GunPresets.PATH, GunPresets.class, new GunPresets());

    @Override
    public void onEnable() {
        PDK.init(this);
        gunPresets.presets.put("pistol", new PistolNBT());
        gunPresets.presets.put("shotgun", new ShotgunNBT());
        gunPresets.presets.put("sniper", new SniperNBT());
        gunPresets.presets.put("sub-machine-gun", new SmgNBT());
        gunPresets.presets.put("assault-rifle", new AssaultRifleNBT());
        gunPresets.presets.put("double-barrel", new DoubleBarrelNBT());
        gunPresets.presets.put("desert-eagle", new DesertEagleNBT());
        gunPresets.presets.put("rifle", new RifleNBT());
        gunPresets.presets.put("blaster", new BlasterNBT());
        gunPresets.presets.put("blaster-rifle", new BlasterRifleNBT());
        gunPresets.presets.put("rpg", new RpgNBT());
        gunPresets.save();

        this.getConfig().options().copyDefaults(true);
        this.saveDefaultConfig();

        new GunsCommand().register();
        new BlockCollisionsCommand().register();

        new PlayerEventListener().register();
        new ServerEventListener().register();

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, ShootingManager::onTick, 0, 1);
    }

    @Override
    public void onDisable() {
        gunPresets.save();
    }

    public static boolean isGun(ItemStack item) {
        if (item == null || !item.hasItemMeta())
            return false;

        ItemMeta meta = item.getItemMeta();
        PersistentData data = new PersistentData(meta.getPersistentDataContainer());
        return data.has("gun_nbt");
    }

    public static GunNBT getGun(ItemStack item) {
        if (item == null || !item.hasItemMeta())
            return null;

        ItemMeta meta = item.getItemMeta();
        PersistentData data = new PersistentData(meta.getPersistentDataContainer());
        return data.read("gun_nbt", GunNBT.class);
    }

    public static boolean makeGun(ItemStack item) {
        if (item == null || !item.hasItemMeta())
            return false;

        GunNBT nbt = new GunNBT();
        nbt.updateItemMeta(item);
        return true;
    }
}
