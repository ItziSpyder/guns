package io.github.itzispyder.guns.firearms;

import io.github.itzispyder.guns.firearms.nbt.GunNBT;
import io.github.itzispyder.pdk.utils.misc.config.JsonSerializable;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class GunPresets implements JsonSerializable<GunPresets> {

    public static final String PATH = "plugins/Guns/presets.json";
    public final Map<String, GunNBT> presets = new HashMap<>();

    @Override
    public File getFile() {
        return new File(PATH);
    }
}
