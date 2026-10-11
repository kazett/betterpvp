// Better PvP - Copyright (c) 2023 kazet, MIT License (see LICENSE)
package id.betterpvp.config;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

public class KeyBindingConfig implements ClientModInitializer {
    private static final KeyBinding.Category CATEGORY =
            KeyBinding.Category.create(Identifier.of("betterpvp", "main"));

    private static KeyBinding autoCritKeyBinding;
    private static KeyBinding autoAttackKeyBinding;
    private static KeyBinding preventsHittingBlocksKeyBinding;
    private static KeyBinding afkAttackKeyBinding;
    private static KeyBinding autoBowKeyBinding;

    @Override
    public void onInitializeClient() {
        autoCritKeyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.betterpvp.autoCrit", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_R, CATEGORY));
        autoAttackKeyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.betterpvp.autoAttack", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_G, CATEGORY));
        preventsHittingBlocksKeyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.betterpvp.preventsHittingBlocks", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_H, CATEGORY));
        afkAttackKeyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.betterpvp.afkAttack", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_K, CATEGORY));
        autoBowKeyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.betterpvp.autoRanged", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_J, CATEGORY));

        ClientTickEvents.END_CLIENT_TICK.register(KeyBindingConfig::onEndTick);
    }

    private static void onEndTick(MinecraftClient client) {
        while (autoCritKeyBinding.wasPressed()) {
            BetterPvPConfig.autoCrit = !BetterPvPConfig.autoCrit;
            if (client.player != null) {
                client.player.sendMessage(Text.translatable(BetterPvPConfig.autoCrit
                        ? "text.KeyBindingConfig.autoCrit.ON" : "text.KeyBindingConfig.autoCrit.OFF"), true);
            }
        }

        while (autoAttackKeyBinding.wasPressed()) {
            BetterPvPConfig.autoAttack = !BetterPvPConfig.autoAttack;
            if (client.player != null) {
                client.player.sendMessage(Text.translatable(BetterPvPConfig.autoAttack
                        ? "text.KeyBindingConfig.autoAttack.ON" : "text.KeyBindingConfig.autoAttack.OFF"), true);
            }
        }

        if (preventsHittingBlocksKeyBinding.wasPressed() && client.player != null) {
            if (BetterPvPConfig.preventsHittingBlocks) {
                client.player.sendMessage(Text.translatable("text.KeyBindingConfig.preventsHittingBlocks.OFF"), false);
                BetterPvPConfig.preventsHittingBlocks = false;
            } else {
                client.player.sendMessage(Text.translatable("text.KeyBindingConfig.preventsHittingBlocks.ON"), false);
                BetterPvPConfig.preventsHittingBlocks = true;
            }
        }

        if (afkAttackKeyBinding.wasPressed() && client.player != null) {
            if (BetterPvPConfig.afkAttack) {
                client.player.sendMessage(Text.translatable("text.KeyBindingConfig.afkAttack.OFF"), false);
                BetterPvPConfig.afkAttack = false;
            } else {
                client.player.sendMessage(Text.translatable("text.KeyBindingConfig.afkAttack.ON"), false);
                BetterPvPConfig.afkAttack = true;
            }
        }

        if (autoBowKeyBinding.wasPressed() && client.player != null) {
            if (BetterPvPConfig.autoBow || BetterPvPConfig.autoCrossBow || BetterPvPConfig.autoTrident) {
                client.player.sendMessage(Text.translatable("text.KeyBindingConfig.autoRanged.OFF"), false);
                BetterPvPConfig.autoBow = false;
                BetterPvPConfig.autoCrossBow = false;
                BetterPvPConfig.autoTrident = false;
            } else {
                client.player.sendMessage(Text.translatable("text.KeyBindingConfig.autoRanged.ON"), false);
                BetterPvPConfig.autoBow = true;
                BetterPvPConfig.autoCrossBow = true;
                BetterPvPConfig.autoTrident = true;
            }
        }
    }
}
