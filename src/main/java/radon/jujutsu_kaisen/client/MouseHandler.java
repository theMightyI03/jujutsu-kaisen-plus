package radon.jujutsu_kaisen.client;

import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import radon.jujutsu_kaisen.JujutsuKaisen;
import radon.jujutsu_kaisen.client.gui.overlay.AbilityOverlay;

@Mod.EventBusSubscriber(modid = JujutsuKaisen.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class MouseHandler {
    @SubscribeEvent
    public static void onMouseScroll(InputEvent.MouseScrollingEvent event) {
        Minecraft mc = Minecraft.getInstance();

        double delta = event.getScrollDelta();

        int i = (int) Math.signum(delta);

        if (i == 0) {
            return;
        }

        if (mc.options.keyShift.isDown()) {
            if (AbilityOverlay.scroll(i)) {
                event.setCanceled(true);
            }
        }
    }
}