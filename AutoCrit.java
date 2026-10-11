// Better PvP - Copyright (c) 2023 kazet, MIT License (see LICENSE)
package id.betterpvp;

import id.betterpvp.config.BetterPvPConfig;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.ActionResult;

public final class AutoCrit {
    private static boolean resprint = false;

    private AutoCrit() {
    }

    public static void register() {
        // Setelah hit, kasih tau server kalau kita masih sprint (biar ga desync)
        net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (!resprint) return;
            resprint = false;
            ClientPlayerEntity p = client.player;
            if (p != null && p.networkHandler != null && p.isSprinting()) {
                p.networkHandler.sendPacket(new ClientCommandC2SPacket(p, ClientCommandC2SPacket.Mode.START_SPRINTING));
            }
        });

        AttackEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            if (!world.isClient() || !BetterPvPConfig.autoCrit) return ActionResult.PASS;

            MinecraftClient mc = MinecraftClient.getInstance();
            ClientPlayerEntity self = mc.player;
            if (self == null || player != self || mc.getNetworkHandler() == null) {
                return ActionResult.PASS;
            }
            if (entity instanceof LivingEntity && eligible(self)) {
                apply(self);
            }
            return ActionResult.PASS;
        });
    }

    private static boolean eligible(ClientPlayerEntity p) {
        if (BetterPvPConfig.critOnlyFullCharge && p.getAttackCooldownProgress(0.5f) <= 0.9f) {
            return false;
        }
        return !p.isTouchingWater()
                && !p.isInLava()
                && !p.isClimbing()
                && !p.hasVehicle()
                && !p.hasStatusEffect(StatusEffects.BLINDNESS);
    }

    private static void apply(ClientPlayerEntity p) {
        var net = p.networkHandler;

        if (BetterPvPConfig.critWhileSprinting && p.isSprinting()) {
            net.sendPacket(new ClientCommandC2SPacket(p, ClientCommandC2SPacket.Mode.STOP_SPRINTING));
            resprint = true;
        }

        if (p.isOnGround() || p.fallDistance <= 0) {
            double x = p.getX();
            double y = p.getY();
            double z = p.getZ();
            boolean hc = p.horizontalCollision;
            net.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(x, y + 0.0625, z, false, hc));
            net.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(x, y, z, false, hc));
        }
    }
}
