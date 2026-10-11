// Better PvP - Copyright (c) 2023 kazet, MIT License (see LICENSE)
package id.betterpvp;

import id.betterpvp.config.BetterPvPConfig;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.network.ClientPlayerEntity;

public final class JumpReset {
    private static int lastHurt = 0;
    private static boolean pressedByMod = false;
    private static boolean previousState = false;

    private JumpReset() {
    }

    public static void register() {
        ClientTickEvents.START_CLIENT_TICK.register(client -> {
            // Kembalikan HANYA jumpKey ke state sebelumnya.
            // (updatePressedStates() sebelumnya me-reset semua tombol, termasuk
            // tombol dari touch control / attack / sneak -> bikin stuck.)
            if (pressedByMod) {
                client.options.jumpKey.setPressed(previousState);
                pressedByMod = false;
            }

            ClientPlayerEntity p = client.player;
            if (p == null) {
                lastHurt = 0;
                return;
            }

            int hurt = p.hurtTime;
            if (BetterPvPConfig.jumpReset
                    && hurt > lastHurt
                    && p.isOnGround()
                    && !p.isTouchingWater()
                    && !p.isInLava()
                    && client.currentScreen == null
                    && !client.options.jumpKey.isPressed()) {
                previousState = false;
                client.options.jumpKey.setPressed(true);
                pressedByMod = true;
            }
            lastHurt = hurt;
        });
    }
}
