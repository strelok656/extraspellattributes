package com.extraspellattributes.enchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;

public class Warding extends ExtraRPGEnchantment {
    protected Warding(Rarity weight, EnchantmentTarget target, EquipmentSlot[] slotTypes) {
        super(weight, target, slotTypes);
    }

    public int getMaxLevel() {
        return 5;
    }
}
