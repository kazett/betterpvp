// Better PvP - Copyright (c) 2023 kazet, MIT License (see LICENSE)
package id.betterpvp;

import id.betterpvp.config.BetterPvPConfig;
import id.betterpvp.util.CleanCutUtil;
import id.betterpvp.util.WeaponFilter;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.item.BowItem;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BetterPvP implements ClientModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("betterpvp");

    private int fullChargeTicks = 0;

    @Override
    public void onInitializeClient() {
        AutoCrit.register();
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            autoMeleeTick(client);
            autoBowTick(client);
        });
    }

    private void autoMeleeTick(MinecraftClient client) {
        if (client.player == null || client.world == null || client.interactionManager == null) {
            fullChargeTicks = 0;
            return;
        }

        if (client.player.getAttackCooldownProgress(0.0f) >= 1.0f) {
            fullChargeTicks++;
        } else {
            fullChargeTicks = 0;
        }

        if (!BetterPvPConfig.autoAttack) {
            return;
        }
        if (!(client.options.attackKey.isPressed() || BetterPvPConfig.afkAttack)) {
            return;
        }
        if (!WeaponFilter.allowed(client.player.getMainHandStack())) {
            return;
        }
        if (client.player.isUsingItem()) {
            return;
        }
        if (fullChargeTicks <= BetterPvPConfig.extraDelayTicks) {
            return;
        }

        HitResult target = client.crosshairTarget;

        if (target == null || target.getType() == HitResult.Type.MISS) {
            if (BetterPvPConfig.alwaysAttack && !client.player.handSwinging) {
                client.player.swingHand(Hand.MAIN_HAND);
            }
            return;
        }

        if (target.getType() == HitResult.Type.BLOCK && BetterPvPConfig.cleanCut) {
            EntityHitResult behind = CleanCutUtil.findEntityBehindSoftBlock(client);
            if (behind != null) {
                attack(client, behind.getEntity());
            }
        } else if (target.getType() == HitResult.Type.ENTITY) {
            Entity entity = ((EntityHitResult) target).getEntity();
            if (entity.isAlive() && entity.isAttackable()) {
                attack(client, entity);
            }
        }
    }

    private static void attack(MinecraftClient client, Entity entity) {
        client.interactionManager.attackEntity(client.player, entity);
        client.player.swingHand(Hand.MAIN_HAND);
    }

    private void autoBowTick(MinecraftClient client) {
        if (!client.options.useKey.isPressed() || client.player == null || client.interactionManager == null) {
            return;
        }

        ClientPlayerEntity player = client.player;
        ItemStack stack = player.getMainHandStack();
        Item item = stack.getItem();

        if (item == Items.BOW && BetterPvPConfig.autoBow) {
            float pull = BowItem.getPullProgress(player.getItemUseTime() - 1);
            if (pull >= 1.0f) {
                client.interactionManager.stopUsingItem(player);
            }
        }

        if (item == Items.CROSSBOW && BetterPvPConfig.autoCrossBow) {
            float pullTime = CrossbowItem.getPullTime(stack, player);
            float timeLeft = player.getItemUseTimeLeft();
            float progress = (pullTime - timeLeft) / pullTime;
            if (progress >= 1.3f) {
                client.interactionManager.stopUsingItem(player);
                client.interactionManager.interactItem(player, Hand.MAIN_HAND);
            }
        }

        if (item == Items.TRIDENT && BetterPvPConfig.autoTrident) {
            float progress = player.getItemUseTime() / 10.0f;
            if (progress >= 1.3f) {
                client.interactionManager.stopUsingItem(player);
            }
        }
    }
}
