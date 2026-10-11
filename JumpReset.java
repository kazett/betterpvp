// Better PvP - Copyright (c) 2023 kazet, MIT License (see LICENSE)
package id.betterpvp;

import id.betterpvp.config.BetterPvPConfig;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.KeyBinding;

public final class JumpReset {
    private static int lastHurt = 0;
    private static boolean pressed = false;

    private JumpReset() {
    }

    public static void register() {
        ClientTickEvents.START_CLIENT_TICK.register(client -> {
            if (pressed) {
                KeyBinding.updatePressedStates();
                pressed = false;
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
                    && client.currentScreen == null) {
                client.options.jumpKey.setPressed(true);
                pressed = true;
            }
            lastHurt = hurt;
        });
    }
}
