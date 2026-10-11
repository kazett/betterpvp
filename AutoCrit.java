// Better PvP - Copyright (c) 2023 kazet, MIT License (see LICENSE)
package id.betterpvp;

import id.betterpvp.config.BetterPvPConfig;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.ActionResult;

public final class AutoCrit {
    private AutoCrit() {
    }

    public static void register() {
        AttackEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            if (!world.isClient() || !BetterPvPConfig.autoCrit) return ActionResult.PASS;

            MinecraftClient mc = MinecraftClient.getInstance();
            ClientPlayerEntity self = mc.player;
            if (self == null || player != self || mc.getNetworkHandler() == null) {
                return ActionResult.PASS;
            }
            if (entity instanceof LivingEntity && canCrit(self)) {
                sendCritPackets(self);
            }
            return ActionResult.PASS;
        });
    }

    private static boolean canCrit(ClientPlayerEntity p) {
        if (BetterPvPConfig.critOnlyFullCharge && p.getAttackCooldownProgress(0.5f) <= 0.9f) {
            return false;
        }
        return p.isOnGround()
                && !p.isTouchingWater()
                && !p.isInLava()
                && !p.isClimbing()
                && !p.hasVehicle()
                && !p.hasStatusEffect(StatusEffects.BLINDNESS);
    }

    private static void sendCritPackets(ClientPlayerEntity p) {
        double x = p.getX();
        double y = p.getY();
        double z = p.getZ();
        boolean hc = p.horizontalCollision;
        var net = p.networkHandler;

        net.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(x, y + 0.0625, z, false, hc));
        net.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(x, y, z, false, hc));
    }
}
