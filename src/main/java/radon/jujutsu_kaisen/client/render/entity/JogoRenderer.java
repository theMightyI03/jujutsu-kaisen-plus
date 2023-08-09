package radon.jujutsu_kaisen.client.render.entity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import radon.jujutsu_kaisen.JujutsuKaisen;
import radon.jujutsu_kaisen.client.model.entity.DefaultedTurnHeadEntityGeoModel;
import radon.jujutsu_kaisen.entity.curse.JogoEntity;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class JogoRenderer extends GeoEntityRenderer<JogoEntity> {
    public JogoRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new DefaultedTurnHeadEntityGeoModel<>(new ResourceLocation(JujutsuKaisen.MOD_ID, "jogo")));
    }
}
