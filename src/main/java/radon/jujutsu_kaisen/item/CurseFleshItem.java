package radon.jujutsu_kaisen.item;

import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import radon.jujutsu_kaisen.data.sorcerer.ISorcererData;
import radon.jujutsu_kaisen.data.JJKAttachmentTypes;
import radon.jujutsu_kaisen.data.capability.IJujutsuCapability;
import radon.jujutsu_kaisen.data.capability.JujutsuCapabilityHandler;
import radon.jujutsu_kaisen.data.sorcerer.JujutsuType;
import radon.jujutsu_kaisen.data.sorcerer.SorcererGrade;
import radon.jujutsu_kaisen.config.ConfigHolder;

public class CurseFleshItem extends CursedEnergyFleshItem {
    private static final int DURATION = 30 * 20;
    private static final int AMPLIFIER = 5;

    public CurseFleshItem(Properties pProperties) {
        super(pProperties);
    }

    public @NotNull ItemStack finishUsingItem(@NotNull ItemStack pStack, @NotNull Level pLevel, @NotNull LivingEntity pEntityLiving) {
        ItemStack stack = super.finishUsingItem(pStack, pLevel, pEntityLiving);

        IJujutsuCapability cap = pEntityLiving.getCapability(JujutsuCapabilityHandler.INSTANCE);

        if (cap == null) return stack;

        ISorcererData data = cap.getSorcererData();

        if (data != null && data.getType() == JujutsuType.CURSE) {
            data.addExtraEnergy((getGrade(pStack).ordinal() + 1) * ConfigHolder.SERVER.cursedObjectEnergyForGrade.get().floatValue());
        } else {
            pEntityLiving.addEffect(new MobEffectInstance(MobEffects.WITHER, Mth.floor(DURATION * ((float) (getGrade(pStack).ordinal() + 1) / SorcererGrade.values().length)),
                    Mth.floor(AMPLIFIER * ((float) (getGrade(pStack).ordinal() + 1) / SorcererGrade.values().length))));
        }
        return stack;
    }
}
