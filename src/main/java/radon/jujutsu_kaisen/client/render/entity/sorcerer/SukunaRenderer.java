package radon.jujutsu_kaisen.client.render.entity.sorcerer;

import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidArmorModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import radon.jujutsu_kaisen.JujutsuKaisen;
import radon.jujutsu_kaisen.client.layer.JJKOverlayLayer;
import radon.jujutsu_kaisen.client.layer.SukunaMarkingsLayer;
import radon.jujutsu_kaisen.entity.sorcerer.SukunaEntity;
import radon.jujutsu_kaisen.mixin.client.IPlayerModelAccessor;

public class SukunaRenderer extends HumanoidMobRenderer<SukunaEntity, PlayerModel<SukunaEntity>> {
    public static ModelLayerLocation LAYER = new ModelLayerLocation(new ResourceLocation(JujutsuKaisen.MOD_ID, "sukuna"), "main");
    public static ModelLayerLocation LAYER_SLIM = new ModelLayerLocation(new ResourceLocation(JujutsuKaisen.MOD_ID, "sukuna"), "slim");
    public static ModelLayerLocation INNER_LAYER = new ModelLayerLocation(new ResourceLocation(JujutsuKaisen.MOD_ID, "sukuna"), "inner_armor");
    public static ModelLayerLocation OUTER_LAYER = new ModelLayerLocation(new ResourceLocation(JujutsuKaisen.MOD_ID, "sukuna"), "outer_armor");

    private final PlayerModel<SukunaEntity> normal;
    private final PlayerModel<SukunaEntity> slim;

    @Nullable
    private ResourceLocation texture;

    public SukunaRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, null, 0.5F);

        this.addLayer(new HumanoidArmorLayer<>(this, new HumanoidArmorModel<>(pContext.bakeLayer(INNER_LAYER)),
                new HumanoidArmorModel<>(pContext.bakeLayer(OUTER_LAYER)), pContext.getModelManager()));
        this.addLayer(new SukunaMarkingsLayer<>(this));
        this.addLayer(new JJKOverlayLayer<>(this));

        this.normal = new PlayerModel<>(pContext.bakeLayer(LAYER), false);
        this.slim = new PlayerModel<>(pContext.bakeLayer(LAYER_SLIM), true);
    }

    @Override
    public void render(SukunaEntity pEntity, float pEntityYaw, float pPartialTicks, @NotNull PoseStack pPoseStack, @NotNull MultiBufferSource pBuffer, int pPackedLight) {
        EntityType<?> type = pEntity.getKey();

        if (this.texture == null) {
            if (type == EntityType.PLAYER) {
                GameProfile profile = pEntity.getPlayer();

                ClientPacketListener conn = Minecraft.getInstance().getConnection();
                PlayerInfo info = conn == null ? null : conn.getPlayerInfo(profile.getId());
                this.texture = info == null ? DefaultPlayerSkin.getDefaultSkin(profile.getId()) : info.getSkinLocation();
                this.model = (info == null ? DefaultPlayerSkin.getSkinModelName(profile.getId()) : info.getModelName()).equals("default") ? this.normal : this.slim;
            } else {
                Minecraft mc = Minecraft.getInstance();
                assert mc.level != null;
                Entity entity = type.create(mc.level);
                assert entity != null;
                this.texture = this.entityRenderDispatcher.getRenderer(entity).getTextureLocation(entity);
            }
        }

        if (this.model == null) {
            Minecraft mc = Minecraft.getInstance();

            assert mc.level != null;

            LivingEntity entity = (LivingEntity) type.create(mc.level);

            if (entity == null) return;

            LivingEntityRenderer<?, ?> renderer = (LivingEntityRenderer<?, ?>) this.entityRenderDispatcher.getRenderer(entity);

            if (!(renderer.getModel() instanceof PlayerModel<?> player)) return;

            this.model = ((IPlayerModelAccessor) player).getSlimAccessor() ? this.slim : this.normal;
        }
        super.render(pEntity, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, pPackedLight);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull SukunaEntity pEntity) {
        return this.texture;
    }
}
