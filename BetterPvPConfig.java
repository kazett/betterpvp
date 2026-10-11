// Better PvP - Copyright (c) 2023 kazet, MIT License (see LICENSE)
package id.betterpvp.config;

import eu.midnightdust.lib.config.MidnightConfig;

public class BetterPvPConfig extends MidnightConfig {

    @Comment
    public static Comment headerAttack;

    @Entry
    public static boolean autoAttack = true;

    @Entry(min = 0, max = 10)
    public static int extraDelayTicks = 1;

    @Entry
    public static boolean attackSword = true;
    @Entry
    public static boolean attackAxe = true;
    @Entry
    public static boolean attackMace = true;
    @Entry
    public static boolean attackTrident = true;
    @Entry
    public static boolean attackSpear = true;

    @Entry
    public static boolean attackOther = true;

    @Comment
    public static Comment headerCrit;

    @Entry
    public static boolean autoCrit = true;

    @Entry
    public static boolean critOnlyFullCharge = true;

    @Comment
    public static Comment headerMisc;

    @Entry
    public static boolean preventsHittingBlocks = false;
    @Entry
    public static boolean preventsHittingBlocksSwords = true;
    @Entry
    public static boolean cleanCut = true;
    @Entry
    public static boolean autoBow = true;
    @Entry
    public static boolean autoCrossBow = true;
    @Entry
    public static boolean autoTrident = true;
    @Entry
    public static boolean afkAttack = false;
    @Entry
    public static boolean alwaysAttack = false;
}
