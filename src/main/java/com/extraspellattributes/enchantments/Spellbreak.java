package com.extraspellattributes.enchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;

public class Spellbreak extends ExtraRPGEnchantment {
    protected Spellbreak(Rarity weight, EnchantmentTarget target, EquipmentSlot[] slotTypes) {
        super(weight, target, slotTypes);
    }

    public int getMaxLevel() {
        return 5;
    }
}
