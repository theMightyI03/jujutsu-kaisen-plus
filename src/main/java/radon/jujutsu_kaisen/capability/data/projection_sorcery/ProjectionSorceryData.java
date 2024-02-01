package radon.jujutsu_kaisen.capability.data.projection_sorcery;

import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import radon.jujutsu_kaisen.JJKConstants;
import radon.jujutsu_kaisen.ability.JJKAbilities;
import radon.jujutsu_kaisen.ability.base.Ability;
import radon.jujutsu_kaisen.capability.data.sorcerer.ISorcererData;
import radon.jujutsu_kaisen.capability.data.sorcerer.SorcererDataHandler;
import radon.jujutsu_kaisen.capability.data.ten_shadows.Adaptation;
import radon.jujutsu_kaisen.cursed_technique.base.ICursedTechnique;
import radon.jujutsu_kaisen.damage.JJKDamageSources;
import radon.jujutsu_kaisen.entity.ten_shadows.MahoragaEntity;
import radon.jujutsu_kaisen.entity.ten_shadows.WheelEntity;
import radon.jujutsu_kaisen.util.EntityUtil;

import java.util.*;

public class ProjectionSorceryData implements IProjectionSorceryData {
    private final List<AbstractMap.SimpleEntry<Vec3, Float>> frames;
    private int speedStacks;
    private int noMotionTime;

    private LivingEntity owner;

    private static final UUID PROJECTION_SORCERY_MOVEMENT_SPEED_UUID = UUID.fromString("23ecaba3-fbe8-44c1-93c4-5291aa9ee777");
    private static final UUID PROJECTION_ATTACK_SPEED_UUID = UUID.fromString("18cd1e25-656d-4172-b9f7-2f1b3daf4b89");
    private static final UUID PROJECTION_STEP_HEIGHT_UUID = UUID.fromString("1dbcbef7-8193-406a-b64d-8766ea505fdb");

    public ProjectionSorceryData() {
        this.frames = new ArrayList<>();
    }

    @Override
    public void tick(LivingEntity owner) {
        if (this.owner == null) {
            this.owner = owner;
        }

        if (!this.owner.level().isClientSide) {
            if (this.speedStacks > 0) {
                EntityUtil.applyModifier(this.owner, Attributes.MOVEMENT_SPEED, PROJECTION_SORCERY_MOVEMENT_SPEED_UUID, "Movement speed", this.speedStacks * 2.0D, AttributeModifier.Operation.MULTIPLY_TOTAL);
                EntityUtil.applyModifier(this.owner, Attributes.ATTACK_SPEED, PROJECTION_ATTACK_SPEED_UUID, "Attack speed", this.speedStacks, AttributeModifier.Operation.MULTIPLY_TOTAL);
                EntityUtil.applyModifier(this.owner, ForgeMod.STEP_HEIGHT_ADDITION.get(), PROJECTION_STEP_HEIGHT_UUID, "Step height addition", 2.0F, AttributeModifier.Operation.ADDITION);

                if (this.owner.walkDist == this.owner.walkDistO) {
                    this.noMotionTime++;
                } else if (this.noMotionTime == 1) {
                    this.noMotionTime = 0;
                }

                if (this.noMotionTime > 1) {
                    this.resetSpeedStacks();
                }
            } else {
                EntityUtil.removeModifier(this.owner, Attributes.MOVEMENT_SPEED, PROJECTION_SORCERY_MOVEMENT_SPEED_UUID);
                EntityUtil.removeModifier(this.owner, Attributes.ATTACK_SPEED, PROJECTION_ATTACK_SPEED_UUID);
                EntityUtil.removeModifier(this.owner, ForgeMod.STEP_HEIGHT_ADDITION.get(), PROJECTION_STEP_HEIGHT_UUID);
            }
        }
    }

    @Override
    public List<AbstractMap.SimpleEntry<Vec3, Float>> getFrames() {
        return this.frames;
    }

    @Override
    public void addFrame(Vec3 frame, float yaw) {
        this.frames.add(new AbstractMap.SimpleEntry<>(frame, yaw));
    }

    @Override
    public void removeFrame(AbstractMap.SimpleEntry<Vec3, Float> frame) {
        this.frames.remove(frame);
    }

    @Override
    public void resetFrames() {
        this.frames.clear();
    }

    @Override
    public void init(LivingEntity owner) {
        this.owner = owner;
    }

    @Override
    public int getSpeedStacks() {
        return this.speedStacks;
    }

    @Override
    public void addSpeedStack() {
        this.speedStacks = Math.min(JJKConstants.MAX_PROJECTION_SORCERY_STACKS, this.speedStacks + 1);
    }

    @Override
    public void resetSpeedStacks() {
        this.speedStacks = 0;
        this.noMotionTime = 0;
    }

    @Override
    public CompoundTag serializeNBT() {
        return new CompoundTag();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {

    }
}
