package io.github.itzispyder.guns.firearms.scopes;

import org.bukkit.entity.Player;

import java.util.function.Function;

public enum ScopeType {

    NONE(player -> null),
    SNIPER(SniperScope::new);

    private final Function<Player, Scope> scopeSupplier;

    ScopeType(Function<Player, Scope> scopeSupplier) {
        this.scopeSupplier = scopeSupplier;
    }

    public Scope createScope(Player player) {
        return scopeSupplier.apply(player);
    }
}
