package io.github.itzispyder.guns.events;

import io.github.itzispyder.guns.Guns;
import io.github.itzispyder.guns.firearms.nbt.GunNBT;
import io.github.itzispyder.pdk.events.CustomListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerEventListener implements CustomListener {

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        switch (e.getAction()) {
            case LEFT_CLICK_AIR, LEFT_CLICK_BLOCK -> onLeftClick(e);
            case RIGHT_CLICK_AIR, RIGHT_CLICK_BLOCK -> onRightClick(e);
        }
    }

    public void onRightClick(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        ItemStack item = e.getItem();
        GunNBT gun = Guns.getGun(item);

        if (gun == null)
            return;

        e.setCancelled(true);
        gun.onShoot(p, item);
    }

    public void onLeftClick(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        ItemStack item = e.getItem();
        GunNBT gun = Guns.getGun(item);

        if (gun == null)
            return;

        e.setCancelled(true);
        gun.reload(p, item);
    }
}
