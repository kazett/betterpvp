// Better PvP - Copyright (c) 2023 kazet, MIT License (see LICENSE)
package id.betterpvp.util;

import id.betterpvp.config.BetterPvPConfig;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;

public final class WeaponFilter {
    private WeaponFilter() {
    }

    public static boolean allowed(ItemStack stack) {
        String id = Registries.ITEM.getId(stack.getItem()).getPath();

        if (id.endsWith("_sword")) return BetterPvPConfig.attackSword;
        if (id.endsWith("_axe")) return BetterPvPConfig.attackAxe;
        if (id.equals("mace")) return BetterPvPConfig.attackMace;
        if (id.equals("trident")) return BetterPvPConfig.attackTrident;
        if (id.endsWith("_spear")) return BetterPvPConfig.attackSpear;
        return BetterPvPConfig.attackOther;
    }
}
