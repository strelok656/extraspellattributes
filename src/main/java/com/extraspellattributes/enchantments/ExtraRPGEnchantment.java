package com.extraspellattributes.enchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.enchantment.ProtectionEnchantment;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.spell_power.SpellPowerMod;
import net.spell_power.config.EnchantmentsConfig;
import net.spell_power.internals.MagicProtectionEnchantment;

import java.util.Iterator;

public class ExtraRPGEnchantment extends Enchantment {
    public ExtraRPGEnchantment(Rarity weight, EnchantmentTarget target, EquipmentSlot[] slotTypes) {
        super(weight, target, slotTypes);
    }
    public int getMinPower(int level) {
        return 1 + (level - 1) * 11;
    }

    public int getMaxPower(int level) {
        return this.getMinPower(level) + 11;
    }

    public int getMaxLevel() {
        return 5;
    }
    public static int getEnchantmentLevel(Enchantment enchantment, LivingEntity entity, ItemStack provisionedWeapon) {
        int level;
            level = getEnchantmentLevelEquipmentSum(enchantment, entity);



        return level;
    }
    public static int getEnchantmentLevelEquipmentSum(Enchantment enchantment, LivingEntity entity) {
        int level = 0;

        ItemStack offHandStack;
        for(Iterator var3 = entity.getArmorItems().iterator(); var3.hasNext(); level += EnchantmentHelper.getLevel(enchantment, offHandStack)) {
            offHandStack = (ItemStack)var3.next();
        }

        ItemStack mainHandStack = entity.getMainHandStack();
        if (mainHandStack != null) {
            level += EnchantmentHelper.getLevel(enchantment, mainHandStack);
        }

        offHandStack = entity.getOffHandStack();
        if (offHandStack != null) {
            level += EnchantmentHelper.getLevel(enchantment, offHandStack);
        }

        return level;
    }


    @Override
    protected boolean canAccept(Enchantment other) {
        if (other instanceof MagicProtectionEnchantment) {
            return false;
        } else if(other instanceof ExtraRPGEnchantment) {
            return false;
        }else if ((other instanceof ProtectionEnchantment)) {
            return false;
        }
        else {
            return super.canAccept(other);
        }
    }
}
