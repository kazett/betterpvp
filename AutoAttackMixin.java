// Better PvP - Copyright (c) 2023 kazet, MIT License (see LICENSE)
package id.betterpvp.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.registry.Registries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import id.betterpvp.config.BetterPvPConfig;

@Mixin(MinecraftClient.class)
public abstract class AutoAttackMixin {
    @Shadow
    public ClientPlayerInteractionManager interactionManager;

    @Shadow
    public ClientPlayerEntity player;

    @Inject(method = "handleBlockBreaking", at = @At("HEAD"), cancellable = true)
    private void autoattack$onHandleBlockBreaking(boolean breaking, CallbackInfo ci) {
        if (!breaking || this.player == null || this.interactionManager == null) {
            return;
        }

        String id = Registries.ITEM.getId(this.player.getMainHandStack().getItem()).getPath();
        boolean holdingSword = id.contains("sword");

        if ((holdingSword && BetterPvPConfig.preventsHittingBlocksSwords)
                || BetterPvPConfig.preventsHittingBlocks) {
            this.interactionManager.cancelBlockBreaking();
            ci.cancel();
        }
    }
}
