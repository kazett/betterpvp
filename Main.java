// Better PvP - Copyright (c) 2023 kazet, MIT License (see LICENSE)
package id.betterpvp;

import eu.midnightdust.lib.config.MidnightConfig;
import id.betterpvp.config.BetterPvPConfig;
import net.fabricmc.api.ModInitializer;

public class Main implements ModInitializer {
    @Override
    public void onInitialize() {
        MidnightConfig.init("betterpvp", BetterPvPConfig.class);
    }
}
