package io.github.itzispyder.guns.firearms.nbt;

import io.github.itzispyder.pdk.utils.misc.SoundPlayer;
import org.bukkit.Location;
import org.bukkit.Sound;

public class SoundInstance {

    public Sound sound;
    public float pitch, volume;
    public double range;

    public SoundInstance(Sound sound, float volume, float pitch, double range) {
        this.sound = sound;
        this.pitch = pitch;
        this.volume = volume;
        this.range = range;
    }

    public void play(SoundPlayer player) {
        player.changePlayer(sound, volume, pitch);
        player.playWithin(range);
    }

    public void play(Location loc) {
        SoundPlayer player = new SoundPlayer(loc, sound, volume, pitch);
        player.playWithin(range);
    }
}
