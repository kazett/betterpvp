// Better PvP - Copyright (c) 2023 kazet, MIT License (see LICENSE)
package id.betterpvp.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.util.hit.EntityHitResult;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import id.betterpvp.util.CleanCutUtil;

@Mixin(GameRenderer.class)
public class GameRenderMixin {
    @Shadow
    @Final
    private MinecraftClient client;

    @Inject(method = "updateCrosshairTarget", at = @At("TAIL"), require = 0)
    private void autoattack$updateCrosshairTarget(float tickProgress, CallbackInfo ci) {
        EntityHitResult behind = CleanCutUtil.findEntityBehindSoftBlock(this.client);
        if (behind != null) {
            this.client.targetedEntity = behind.getEntity();
        }
    }
}
