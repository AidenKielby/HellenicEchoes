package com.aiden.hellenic_echoes.event;


import com.aiden.hellenic_echoes.HellenicEchoes;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class ModClientOverlays {
    private static int overlayTicks = 0;
    private static int overlayTotalTicks = 0;
    private static int FADE_TICKS = 10;

    private static final ResourceLocation OVERLAY_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(HellenicEchoes.MODID, "textures/gui/aether_overlay.png");

    public static void triggerOverlay(int ticks) {
        if (ticks <= 0) return;
        overlayTicks = ticks;
        overlayTotalTicks = ticks+30;
        FADE_TICKS = 30;
    }

    @SubscribeEvent
    public static void onClientTick(net.minecraftforge.event.TickEvent.ClientTickEvent event) {
        if (event.phase != net.minecraftforge.event.TickEvent.Phase.END) return;
        if (overlayTicks > 0) overlayTicks--;
    }

    @SubscribeEvent
    public static void onRenderOverlay(RenderGuiOverlayEvent.Pre event) {
        if (overlayTicks <= 0 || overlayTotalTicks <= 0) return;

        Minecraft mc = Minecraft.getInstance();

        int w = mc.getWindow().getGuiScaledWidth();
        int h = mc.getWindow().getGuiScaledHeight();

        int elapsed = overlayTotalTicks - overlayTicks;
        int fade = Math.max(1, Math.min(FADE_TICKS, overlayTotalTicks / 2));

        float alpha;
        if (elapsed < fade) {
            alpha = (float) elapsed / fade; // fade in
        } else if (overlayTicks < fade) {
            alpha = (float) overlayTicks / fade; // fade out
        } else {
            alpha = 1f;
        }

        alpha = Math.max(0f, Math.min(1f, alpha));

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShaderColor(1f, 1f, 1f, alpha);

        event.getGuiGraphics().blit(
                OVERLAY_TEXTURE,
                0, 0,
                0, 0,
                w, h,
                w, h
        );

        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.disableBlend();
    }
}
