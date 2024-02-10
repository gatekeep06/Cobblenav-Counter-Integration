package com.metacontent.cobblenav_counter_integration.mixin;

import com.cobblemon.mod.common.pokemon.RenderablePokemon;
import com.metacontent.cobblenav.client.screen.AbstractPokenavItemScreen;
import com.metacontent.cobblenav.client.screen.pokenav.FinderScreen;
import com.metacontent.cobblenav_counter_integration.CobblenavCounterIntegration;
import com.metacontent.cobblenav_counter_integration.config.CounterIntegrationConfig;
import kotlin.Pair;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import us.timinc.mc.cobblemon.counter.api.CaptureApi;

import static com.cobblemon.mod.common.api.gui.GuiUtilsKt.blitk;

@Mixin(FinderScreen.class)
public class PokenavFinderScreenMixin {
    @Unique
    private static final Identifier ASSETS = new Identifier(CobblenavCounterIntegration.ID, "textures/gui/counter_integration_assets.png");

    @Shadow @Final private RenderablePokemon pokemon;

    @Shadow @Final private static Identifier FINDER_ASSETS;

    @Shadow private int borderX;

    @Shadow private int borderY;

    @Inject(method = "renderBackgroundImage", at = @At("HEAD"), cancellable = true)
    protected void injectRenderBackgroundImageMethod(MatrixStack matrixStack, CallbackInfo ci) {
        PlayerEntity player = MinecraftClient.getInstance().player;

        Identifier texture = ASSETS;
        int offsetY = 0;
        int textureHeight = 512;

        if (player != null) {
            Pair<String, Integer> streak = CaptureApi.INSTANCE.getStreak(player);
            int value = streak.component2();

            if (streak.component1().equals(pokemon.getSpecies().showdownId()) && value >= CounterIntegrationConfig.levelOneStreak) {
                if (value < CounterIntegrationConfig.levelTwoStreak) {
                    offsetY = 116;
                }
                else if (value < CounterIntegrationConfig.levelThreeStreak) {
                    offsetY = 232;
                }
                else if (value < CounterIntegrationConfig.levelFourStreak) {
                    offsetY = 348;
                }
                else {
                    texture = FINDER_ASSETS;
                    offsetY = 116;
                    textureHeight = 256;
                }
            }
        }
        blitk(matrixStack, texture,
                borderX + AbstractPokenavItemScreen.BORDER_DEPTH, borderY + AbstractPokenavItemScreen.BORDER_DEPTH + 20,
                116, 210, 0, offsetY, 256, textureHeight,
                0, 1, 1, 1, 1, false, 1);
        ci.cancel();
    }
}
