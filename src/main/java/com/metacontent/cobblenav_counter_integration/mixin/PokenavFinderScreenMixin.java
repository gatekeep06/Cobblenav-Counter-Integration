package com.metacontent.cobblenav_counter_integration.mixin;

import com.cobblemon.mod.common.pokemon.RenderablePokemon;
import com.metacontent.cobblenav.client.screen.AbstractPokenavItemScreen;
import com.metacontent.cobblenav.client.screen.pokenav.FinderScreen;
import com.metacontent.cobblenav_counter_integration.CobblenavCounterIntegration;
import com.metacontent.cobblenav_counter_integration.config.CounterIntegrationConfig;
import com.metacontent.cobblenav_counter_integration.networking.CounterIntegrationPackets;
import com.metacontent.cobblenav_counter_integration.util.StreakReceiver;
import kotlin.Pair;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
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
import static com.cobblemon.mod.common.client.render.RenderHelperKt.drawScaledText;

@Environment(EnvType.CLIENT)
@Mixin(FinderScreen.class)
public class PokenavFinderScreenMixin implements StreakReceiver {
    @Unique
    private static final Identifier ASSETS = new Identifier(CobblenavCounterIntegration.ID, "textures/gui/counter_integration_assets.png");

    @Shadow @Final private RenderablePokemon pokemon;

    @Shadow @Final private static Identifier FINDER_ASSETS;

    @Shadow private int borderX;

    @Shadow private int borderY;

    @Unique private Pair<String, Integer> streak = null;

    @Inject(method = "init", at = @At("TAIL"))
    protected void injectInitMethod(CallbackInfo ci) {
        streak = null;
        ClientPlayNetworking.send(CounterIntegrationPackets.STREAK_PACKET_SERVER, PacketByteBufs.create());
    }

    @Override
    public void cobblenavCounterIntegration$receiveStreak(String s, int i) {
        this.streak = new Pair<>(s, i);
    }

    @Inject(method = "renderBackgroundImage", at = @At("HEAD"), cancellable = true)
    protected void injectRenderBackgroundImageMethod(MatrixStack matrixStack, CallbackInfo ci) {
        if (streak != null) {
            Identifier texture = ASSETS;
            int offsetY = 0;
            int textureHeight = 512;
            int value = streak.component2();

            if (streak.component1().equals(pokemon.getSpecies().showdownId()) && value >= CounterIntegrationConfig.levelOneStreak) {
                if (value < CounterIntegrationConfig.levelTwoStreak) {
                    offsetY = 116;
                } else if (value < CounterIntegrationConfig.levelThreeStreak) {
                    offsetY = 232;
                } else if (value < CounterIntegrationConfig.levelFourStreak) {
                    offsetY = 348;
                } else {
                    texture = FINDER_ASSETS;
                    offsetY = 116;
                    textureHeight = 256;
                }
            }

            blitk(matrixStack, texture,
                    borderX + AbstractPokenavItemScreen.BORDER_DEPTH, borderY + AbstractPokenavItemScreen.BORDER_DEPTH + 20,
                    116, 210, 0, offsetY, 256, textureHeight,
                    0, 1, 1, 1, 1, false, 1);
            ci.cancel();
        }
    }

    @Inject(method = "renderLevel", at = @At("HEAD"))
    protected void injectRenderLevelMethod(DrawContext drawContext, int i, int j, CallbackInfo ci) {
        if (streak != null) {
            int value = streak.component2();
            if (streak.component1().equals(pokemon.getSpecies().showdownId())) {
                blitk(drawContext.getMatrices(), ASSETS,
                        borderX + AbstractPokenavItemScreen.BORDER_DEPTH, borderY + AbstractPokenavItemScreen.BORDER_HEIGHT / 2,
                        14, 41, 0, 465, 256, 512,
                        0, 1, 1, 1, 1, false, 1);
                drawScaledText(drawContext, AbstractPokenavItemScreen.FONT, Text.literal("CS: " + value),
                        borderX + AbstractPokenavItemScreen.BORDER_DEPTH + 8, borderY + AbstractPokenavItemScreen.BORDER_HEIGHT / 2 + 2,
                        1, 1, 30, 0xFFFFFF, false, true, i, j);
            }
        }
    }
}
