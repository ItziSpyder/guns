package io.github.itzispyder.guns.commands;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.itzispyder.guns.Guns;
import io.github.itzispyder.guns.firearms.GunNBT;
import io.github.itzispyder.guns.firearms.scopes.ScopeType;
import io.github.itzispyder.pdk.commands.Args;
import io.github.itzispyder.pdk.commands.CommandRegistry;
import io.github.itzispyder.pdk.commands.CustomCommand;
import io.github.itzispyder.pdk.commands.completions.CompletionBuilder;
import io.github.itzispyder.pdk.utils.StringUtils;
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
            Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
            String json = gson.toJson(gun);
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
            boolean failed = true;

            switch (args.get(1).toString()) {
                case "shootSound" -> {
                    gun.shootSound = args.get(2).toEnum(Sound.class, gun.shootSound);
                    failed = false;
                }
                case "shootSoundPitch" -> {
                    gun.shootSoundPitch = args.get(2).toFloat();
                    failed = false;
                }
                case "shootSoundVolume" -> {
                    gun.shootSoundVolume = args.get(2).toFloat();
                    failed = false;
                }

                case "scopeType" -> {
                    gun.scopeType = args.get(2).toEnum(ScopeType.class, gun.scopeType);
                    failed = false;
                }
                case "scopeSlownessAmplifier" -> {
                    gun.scopeSlownessAmplifier = args.get(2).toInt();
                    failed = false;
                }

                case "maxUncertainty" -> {
                    gun.maxUncertainty = args.get(2).toDouble();
                    failed = false;
                }
                case "distance" -> {
                    gun.distance = args.get(2).toDouble();
                    failed = false;
                }
                case "damage" -> {
                    gun.damage = args.get(2).toDouble();
                    failed = false;
                }
                case "sneakUncertaintyMultiplier" -> {
                    gun.sneakUncertaintyMultiplier = args.get(2).toDouble();
                    failed = false;
                }

                case "ammo" -> {
                    gun.ammo = args.get(2).toInt();
                    failed = false;
                }
                case "maxAmmo" -> {
                    gun.maxAmmo = args.get(2).toInt();
                    failed = false;
                }
                case "reloadTicks" -> {
                    gun.reloadTicks = args.get(2).toInt();
                    failed = false;
                }
                case "roundsPerShot" -> {
                    gun.roundsPerShot = args.get(2).toInt();
                    failed = false;
                }
                case "cooldownTicks" -> {
                    gun.cooldownTicks = args.get(2).toInt();
                    failed = false;
                }
                case "repetitionIterations" -> {
                    gun.repetitionIterations = args.get(2).toInt();
                    failed = false;
                }
                case "repetitionPeriod" -> {
                    gun.repetitionPeriod = args.get(2).toInt();
                    failed = false;
                }

                default -> error(sender, "Unknown gun property '%s'".formatted(args.get(1)));
            }

            if (!failed) {
                gun.updateItemMeta(item);
                info(sender, "Set gun's &7%s&r to &7%s&r".formatted(args.get(1), args.get(2)));
            }
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
                .then(b.arg("shootSound")
                        .then(b.argEnum(Sound.class)))
                .then(b.arg("shootSoundPitch")
                        .then(b.argPosDecimal("pitch")))
                .then(b.arg("shootSoundVolume")
                        .then(b.argPosDecimal("volume")))
                .then(b.arg("maxUncertainty")
                        .then(b.argPosDecimal("uncertainty")))
                .then(b.arg("distance")
                        .then(b.argPosDecimal("blocks")))
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
                        .then(b.argPosInt("count"))));
    }
}
