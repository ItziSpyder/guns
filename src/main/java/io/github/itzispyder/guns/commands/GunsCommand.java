package io.github.itzispyder.guns.commands;

import io.github.itzispyder.guns.Guns;
import io.github.itzispyder.guns.firearms.nbt.*;
import io.github.itzispyder.guns.firearms.scopes.ScopeType;
import io.github.itzispyder.pdk.commands.Args;
import io.github.itzispyder.pdk.commands.CommandRegistry;
import io.github.itzispyder.pdk.commands.CustomCommand;
import io.github.itzispyder.pdk.commands.completions.CompletionBuilder;
import io.github.itzispyder.pdk.utils.StringUtils;
import io.github.itzispyder.pdk.utils.misc.config.JsonSerializable;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

@CommandRegistry(value = "guns", printStackTrace = true, playersOnly = true, usage = "/guns <action>")
public class GunsCommand implements CustomCommand {

    @Override
    @SuppressWarnings("deprecation")
    public void dispatchCommand(CommandSender sender, Command command, String s, Args args) {
        Player p = (Player) sender;
        ItemStack item = p.getInventory().getItemInMainHand();

        if (args.isEmpty()) {
            error(sender, "Incomplete command");
            return;
        }

        if (args.match(0, "makegun")) {
            if (item.getType().isAir()) {
                error(sender, "Cannot make Air a gun");
                return;
            }
            if (args.getSize() == 1) {
                error(sender, "Please provide a display name");
                return;
            }

            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(color(args.getAll(1).toString()));
            item.setItemMeta(meta);
            Guns.makeGun(item);
            info(sender, "Made &7%s&r a gun".formatted(StringUtils.capitalizeWords(item.getType().name())));
            return;
        }

        if (args.match(0, "save")) {
            if (!Guns.isGun(item)) {
                error(sender, "This item is NOT a gun, please do /guns makegun");
                return;
            }
            if (args.getSize() == 1) {
                error(sender, "Please provide a name");
                return;
            }

            String name = args.get(1).toString().replaceAll("[^0-9A-Za-z_-]", "");
            if (Guns.gunPresets.presets.containsKey(name)) {
                error(sender, "Preset '%s' already exists".formatted(name));
                return;
            }
            Guns.gunPresets.presets.put(name, Guns.getGun(item));
            Guns.gunPresets.save();
            info(sender, "Saved NBT to preset &7%s".formatted(name));
        }

        if (args.match(0, "load")) {
            if (item.getType().isAir()) {
                error(sender, "Cannot make Air a gun");
                return;
            }
            if (args.getSize() == 1) {
                error(sender, "Please provide a name");
                return;
            }

            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(color("&r" + StringUtils.capitalizeWords(args.getAll(1).toString())));
            item.setItemMeta(meta);

            if (!Guns.isGun(item))
                Guns.makeGun(item);

            GunNBT gun = Guns.gunPresets.presets.get(args.get(1).toString());
            if (gun == null) {
                error(sender, "Preset '%s' not found".formatted(args.get(1)));
                return;
            }
            gun.updateItemMeta(item);
            info(sender, "Loaded preset &7%s".formatted(args.get(1)));
        }

        if (args.match(0, "delete")) {
            if (args.getSize() == 1) {
                error(sender, "Please provide a name");
                return;
            }

            String name = args.get(1).toString();

            if (!Guns.gunPresets.presets.containsKey(name)) {
                error(sender, "Preset '%s' not found".formatted(args.get(1)));
                return;
            }
            Guns.gunPresets.presets.remove(name);
            Guns.gunPresets.save();
            info(sender, "Deleted preset &7%s".formatted(args.get(1)));
        }

        if (args.match(0, "info")) {
            if (!Guns.isGun(item)) {
                error(sender, "This item is NOT a gun, please do /guns makegun");
                return;
            }
            GunNBT gun = Guns.getGun(item);
            String json = JsonSerializable.gson.toJson(gun);
            info(sender, "This gun has the following NBT data: &7%s".formatted(json));
            return;
        }

        if (args.match(0, "edit")) {
            if (args.getSize() == 1) {
                error(sender, "Please tell us what to edit");
                return;
            }
            if (!Guns.isGun(item)) {
                error(sender, "This item is NOT a gun, please do /guns makegun");
                return;
            }
            if (args.getSize() == 2) {
                error(sender, "Please give us a value to replace with");
                return;
            }

            GunNBT gun = Guns.getGun(item);
            editGun(sender, gun, args);
            gun.updateItemMeta(item);
        }
    }
    
    private void editGun(CommandSender sender, GunNBT gun, Args args) {
        switch (args.get(1).toString()) {
            case "onShoot" -> editEvent(sender, gun.fire, args);
            case "onHit" -> editEvent(sender, gun.impact, args);

            case "raycastMode" -> {
                gun.raycastMode = args.get(2).toEnum(RaycastMode.class, gun.raycastMode);
                info(sender, "Set gun's &7%s&r to &7%s&r".formatted(args.get(1), args.get(2)));
            }

            case "mode" -> {
                switch (args.get(2).toString()) {
                    case "hitscan" -> {
                        switch (args.get(3).toString()) {
                            case "distance" -> {
                                gun.hitscan.distance = args.get(4).toDouble();
                                info(sender, "Set hitscan &7%s&r to &7%s&r".formatted(args.get(3), args.get(4)));
                            }
                            default -> error(sender, "Unknown property");
                        }
                    }
                    case "ballistics" -> {
                        switch (args.get(3).toString()) {
                            case "bulletDropPerTick" -> {
                                gun.ballistics.bulletDropPerTick = args.get(4).toDouble();
                                info(sender, "Set ballistics &7%s&r to &7%s&r".formatted(args.get(3), args.get(4)));
                            }
                            case "blocksPerTick" -> {
                                gun.ballistics.blocksPerTick = args.get(4).toDouble();
                                info(sender, "Set ballistics &7%s&r to &7%s&r".formatted(args.get(3), args.get(4)));
                            }
                            case "glowColor" -> {
                                int hex = Integer.parseUnsignedInt(args.get(4).toString().substring(1), 16);
                                int r = hex >> 24 & 0xFF;
                                int g = hex >> 16 & 0xFF;
                                int b = hex >> 8 & 0xFF;
                                gun.ballistics.glowColor = Color.fromRGB(r, g, b);
                                info(sender, "Set ballistics &7%s&r to &7%s&r".formatted(args.get(3), args.get(4)));
                            }
                            case "glowing" -> {
                                gun.ballistics.glowing = args.get(4).toBool();
                                info(sender, "Set ballistics &7%s&r to &7%s&r".formatted(args.get(3), args.get(4)));
                            }
                            default -> error(sender, "Unknown property");
                        }
                    }
                    default -> error(sender, "Unknown raycast mode");
                }
            }

            case "scopeType" -> {
                gun.scopeType = args.get(2).toEnum(ScopeType.class, gun.scopeType);
                info(sender, "Set gun's &7%s&r to &7%s&r".formatted(args.get(1), args.get(2)));
            }
            case "scopeSlownessAmplifier" -> {
                gun.scopeSlownessAmplifier = args.get(2).toInt();
                info(sender, "Set gun's &7%s&r to &7%s&r".formatted(args.get(1), args.get(2)));
            }

            case "maxUncertainty" -> {
                gun.maxUncertainty = args.get(2).toDouble();
                info(sender, "Set gun's &7%s&r to &7%s&r".formatted(args.get(1), args.get(2)));
            }
            case "damage" -> {
                gun.damage = args.get(2).toDouble();
                info(sender, "Set gun's &7%s&r to &7%s&r".formatted(args.get(1), args.get(2)));
            }
            case "sneakUncertaintyMultiplier" -> {
                gun.sneakUncertaintyMultiplier = args.get(2).toDouble();
                info(sender, "Set gun's &7%s&r to &7%s&r".formatted(args.get(1), args.get(2)));
            }

            case "ammo" -> {
                gun.ammo = args.get(2).toInt();
                info(sender, "Set gun's &7%s&r to &7%s&r".formatted(args.get(1), args.get(2)));
            }
            case "maxAmmo" -> {
                gun.maxAmmo = args.get(2).toInt();
                info(sender, "Set gun's &7%s&r to &7%s&r".formatted(args.get(1), args.get(2)));
            }
            case "reloadTicks" -> {
                gun.reloadTicks = args.get(2).toInt();
                info(sender, "Set gun's &7%s&r to &7%s&r".formatted(args.get(1), args.get(2)));
            }
            case "roundsPerShot" -> {
                gun.roundsPerShot = args.get(2).toInt();
                info(sender, "Set gun's &7%s&r to &7%s&r".formatted(args.get(1), args.get(2)));
            }
            case "cooldownTicks" -> {
                gun.cooldownTicks = args.get(2).toInt();
                info(sender, "Set gun's &7%s&r to &7%s&r".formatted(args.get(1), args.get(2)));
            }
            case "repetitionIterations" -> {
                gun.repetitionIterations = args.get(2).toInt();
                info(sender, "Set gun's &7%s&r to &7%s&r".formatted(args.get(1), args.get(2)));
            }
            case "repetitionPeriod" -> {
                gun.repetitionPeriod = args.get(2).toInt();
                info(sender, "Set gun's &7%s&r to &7%s&r".formatted(args.get(1), args.get(2)));
            }

            default -> error(sender, "Unknown gun property '%s'".formatted(args.get(1)));
        }
    }

    public void editEvent(CommandSender sender, Event event, Args args) {
        switch (args.get(2).toString()) {
            case "particles" -> {
                switch (args.get(3).toString()) {
                    case "remove" -> {
                        int index = args.get(4).toInt();
                        ParticleInstance particle = event.particles.get(index);
                        event.particles.remove(index);
                        info(sender, "Removed particle from event at index %s: &7%s".formatted(index, JsonSerializable.gson.toJson(particle)));
                    }
                    case "add" -> {
                        Particle type = args.get(4).toEnum(Particle.class);
                        int count = args.get(5).toInt();
                        double deltaX = args.get(6).toDouble();
                        double deltaY = args.get(7).toDouble();
                        double deltaZ = args.get(8).toDouble();
                        double extra = args.get(9).toDouble();
                        ParticleInstance particle = new ParticleInstance(type, count, deltaX, deltaY, deltaZ, extra);
                        event.particles.add(particle);
                        info(sender, "Added new particle to event: &7%s".formatted(JsonSerializable.gson.toJson(particle)));
                    }
                    case "edit" -> {
                        ParticleInstance particle = event.particles.get(args.get(4).toInt());
                        switch (args.get(5).toString()) {
                            case "particle" -> {
                                particle.particle = args.get(6).toEnum(Particle.class, particle.particle);
                                info(sender, "Set particle's &7%s&r to &7%s&r".formatted(args.get(5), args.get(6)));
                            }
                            case "count" -> {
                                particle.count = args.get(6).toInt();
                                info(sender, "Set particle's &7%s&r to &7%s&r".formatted(args.get(5), args.get(6)));
                            }
                            case "deltaX" -> {
                                particle.deltaX = args.get(6).toDouble();
                                info(sender, "Set particle's &7%s&r to &7%s&r".formatted(args.get(5), args.get(6)));
                            }
                            case "deltaY" -> {
                                particle.deltaY = args.get(6).toDouble();
                                info(sender, "Set particle's &7%s&r to &7%s&r".formatted(args.get(5), args.get(6)));
                            }
                            case "deltaZ" -> {
                                particle.deltaZ = args.get(6).toDouble();
                                info(sender, "Set particle's &7%s&r to &7%s&r".formatted(args.get(5), args.get(6)));
                            }
                            case "extra" -> {
                                particle.extra = args.get(6).toDouble();
                                info(sender, "Set particle's &7%s&r to &7%s&r".formatted(args.get(5), args.get(6)));
                            }
                            case "dust" -> {
                                int hex = Integer.parseUnsignedInt(args.get(6).toString().substring(1), 16);
                                int r = hex >> 24 & 0xFF;
                                int g = hex >> 16 & 0xFF;
                                int b = hex >> 8 & 0xFF;
                                Color color = Color.fromRGB(r, g, b);
                                float size = args.get(7).toFloat();
                                particle.dust = new Particle.DustOptions(color, size);
                                info(sender, "Set particle's &7%s&r to &7%s, %s&r".formatted(args.get(5), args.get(6), args.get(7)));
                            }
                            default -> error(sender, "Unknown property");
                        }
                    }
                    default -> error(sender, "Unknown action for event");
                }
            }
            case "sounds" -> {
                switch (args.get(3).toString()) {
                    case "remove" -> {
                        int index = args.get(4).toInt();
                        SoundInstance soundInstance = event.sounds.get(index);
                        event.sounds.remove(index);
                        info(sender, "Removed sound from event at index %s: &7%s".formatted(index, JsonSerializable.gson.toJson(soundInstance)));
                    }
                    case "add" -> {
                        Sound sound = args.get(4).toEnum(Sound.class);
                        float volume = args.get(5).toFloat();
                        float pitch = args.get(6).toFloat();
                        double range = args.get(7).toDouble();
                        SoundInstance soundInstance = new SoundInstance(sound, volume, pitch, range);
                        event.sounds.add(soundInstance);
                        info(sender, "Added new sound to event: &7%s".formatted(JsonSerializable.gson.toJson(soundInstance)));
                    }
                    case "edit" -> {
                        SoundInstance sound = event.sounds.get(args.get(4).toInt());
                        switch (args.get(5).toString()) {
                            case "sound" -> {
                                sound.sound = args.get(6).toEnum(Sound.class, sound.sound);
                                info(sender, "Set sound's &7%s&r to &7%s&r".formatted(args.get(5), args.get(6)));
                            }
                            case "volume" -> {
                                sound.volume = args.get(6).toFloat();
                                info(sender, "Set sound's &7%s&r to &7%s&r".formatted(args.get(5), args.get(6)));
                            }
                            case "pitch" -> {
                                sound.pitch = args.get(6).toFloat();
                                info(sender, "Set sound's &7%s&r to &7%s&r".formatted(args.get(5), args.get(6)));
                            }
                            case "range" -> {
                                sound.range = args.get(6).toDouble();
                                info(sender, "Set sound's &7%s&r to &7%s&r".formatted(args.get(5), args.get(6)));
                            }
                            default -> error(sender, "Unknown property");
                        }
                    }
                    default -> error(sender, "Unknown action for event");
                }
            }
            default -> error(sender, "Property not found");
        }
    }

    @Override
    public void dispatchCompletions(CommandSender sender, Command command, String s, CompletionBuilder b) {
        b.then(b.arg("makegun")
                .then(b.arg(color("&8displayName"))));

        b.then(b.arg("info"));

        b.then(b.arg("save")
                .then(b.arg(color("&8presetName"))));
        b.then(b.arg("load", "delete")
                .then(b.arg(new ArrayList<>(Guns.gunPresets.presets.keySet()))));

        b.then(b.arg("edit")
                .then(b.arg("maxUncertainty")
                        .then(b.argPosDecimal("uncertainty")))
                .then(b.arg("damage")
                        .then(b.argPosDecimal("health")))
                .then(b.arg("ammo")
                        .then(b.argPosInt("amount")))
                .then(b.arg("maxAmmo")
                        .then(b.argPosInt("amount")))
                .then(b.arg("reloadTicks")
                        .then(b.argPosInt("ticks")))
                .then(b.arg("cooldownTicks")
                        .then(b.argPosInt("ticks")))
                .then(b.arg("repetitionIterations")
                        .then(b.argPosInt("count")))
                .then(b.arg("repetitionPeriod")
                        .then(b.argPosInt("ticks")))
                .then(b.arg("scopeType")
                        .then(b.argEnum(ScopeType.class)))
                .then(b.arg("scopeSlownessAmplifier")
                        .then(b.argPosInt("amplifier")))
                .then(b.arg("sneakUncertaintyMultiplier")
                        .then(b.argPosDecimal("multiplier")))
                .then(b.arg("roundsPerShot")
                        .then(b.argPosInt("count")))
                .then(b.arg("raycastMode")
                        .then(b.argEnum(RaycastMode.class)))
                .then(b.arg("mode")
                        .then(b.arg("hitscan")
                                .then(b.arg("distance")
                                        .then(b.argPosDecimal("blocks"))))
                        .then(b.arg("ballistics")
                                .then(b.arg("bulletDropPerTick")
                                        .then(b.argPosDecimal("degrees")))
                                .then(b.arg("blocksPerTick")
                                        .then(b.argPosDecimal("blocks")))
                                .then(b.arg("glowColor")
                                        .then(b.argRegex("#[a-fA-Z0-9]+", "hex value")))
                                .then(b.arg("glowing")
                                        .then(b.argBool()))))
                .then(b.arg("onShoot", "onHit")
                        .then(b.arg("particles")
                                .then(b.arg("remove")
                                        .then(b.argPosInt("index")))
                                .then(b.arg("add")
                                        .then(b.argEnum(Particle.class)
                                                .then(b.argPosInt("count")
                                                        .then(b.argPosDecimal("deltaX")
                                                                .then(b.argPosDecimal("deltaY")
                                                                        .then(b.argPosDecimal("deltaZ")
                                                                                .then(b.argPosDecimal("extra"))))))))
                                .then(b.arg("edit")
                                        .then(b.argPosInt("index")
                                                .then(b.arg("particle")
                                                        .then(b.argEnum(Particle.class)))
                                                .then(b.arg("count")
                                                        .then(b.argPosInt("count")))
                                                .then(b.arg("deltaX")
                                                        .then(b.argPosDecimal("value")))
                                                .then(b.arg("deltaY")
                                                        .then(b.argPosDecimal("value")))
                                                .then(b.arg("deltaZ")
                                                        .then(b.argPosDecimal("value")))
                                                .then(b.arg("extra")
                                                        .then(b.argPosDecimal("value"))))))
                        .then(b.arg("sounds")
                                .then(b.arg("remove")
                                        .then(b.argPosInt("index")))
                                .then(b.arg("add")
                                        .then(b.argEnum(Sound.class)
                                                .then(b.argPosDecimal("volume")
                                                        .then(b.argPosDecimal("pitch")
                                                                .then(b.argPosDecimal("range"))))))
                                .then(b.arg("edit")
                                        .then(b.argPosInt("index")
                                                .then(b.arg("sound")
                                                        .then(b.argEnum(Sound.class)))
                                                .then(b.arg("volume")
                                                        .then(b.argPosDecimal("value")))
                                                .then(b.arg("pitch")
                                                        .then(b.argPosDecimal("value")))
                                                .then(b.arg("range")
                                                        .then(b.argPosDecimal("blocks"))))))));
    }
}
