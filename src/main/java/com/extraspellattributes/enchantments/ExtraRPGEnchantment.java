package com.extraspellattributes.enchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.enchantment.ProtectionEnchantment;
import net.minecraft.entity.EquipmentSlot;
import net.spell_power.internals.MagicProtectionEnchantment;

public class ExtraRPGEnchantment extends Enchantment {
    public ExtraRPGEnchantment(Rarity weight, EnchantmentTarget target, EquipmentSlot[] slotTypes) {
        super(weight, target, slotTypes);
    }

    public int getMaxLevel() {
        return 5;
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
