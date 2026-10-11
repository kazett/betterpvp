// Better PvP - Copyright (c) 2023 kazet, MIT License (see LICENSE)
package id.betterpvp.util;

import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import id.betterpvp.config.BetterPvPConfig;

public final class CleanCutUtil {
    private CleanCutUtil() {
    }

    public static EntityHitResult findEntityBehindSoftBlock(MinecraftClient client) {
        HitResult target = client.crosshairTarget;
        ClientPlayerEntity player = client.player;
        ClientWorld world = client.world;

        if (target == null || player == null || world == null) {
            return null;
        }
        if (target.getType() != HitResult.Type.BLOCK || !BetterPvPConfig.cleanCut) {
            return null;
        }

        BlockPos pos = ((BlockHitResult) target).getBlockPos();
        BlockState state = world.getBlockState(pos);
        boolean soft = state.getCollisionShape(world, pos).isEmpty()
                || state.getHardness(world, pos) == 0.0f;
        if (!soft) {
            return null;
        }

        double reach = player.getEntityInteractionRange();
        Vec3d start = player.getCameraPosVec(1.0f);
        Vec3d look = player.getRotationVec(1.0f);
        Vec3d end = start.add(look.x * reach, look.y * reach, look.z * reach);

        EntityHitResult hit = ProjectileUtil.raycast(
                player, start, end, player.getBoundingBox().stretch(look.multiply(reach)).expand(1.0),
                entity -> !entity.isSpectator() && entity.isAttackable(),
                reach * reach);

        if (hit == null || !hit.getEntity().isAlive()) {
            return null;
        }
        return hit;
    }
}
