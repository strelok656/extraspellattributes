package com.extraspellattributes.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

@Config(name = "server_v3")
public class ServerConfig  implements ConfigData {
    public ServerConfig(){}
    @Comment("Enchantments exclusive with each other - Default: True")
    public boolean exclusiveEachOther = true;
    @Comment("Enchantments exclusive with protection - Default: False")
    public boolean exclusiveProtection = false;

    @Comment("Warding enchant bonus per 1 level - Default: 1.0")
    public float wardingenchant = 1F;
    @Comment("Battle Rouse enchant bonus per 1 level - Default: 0.04")
    public float battlerouseenchant = 0.04F;
    @Comment("Defiance enchant bonus per 1 level - Default: 0.25")
    public float defianceEnchant = 0.25F;
    @Comment("Evasion enchant bonus per 1 level - Default: 0.04")
    public float evasionEnchant = 0.04F;
    @Comment("Spellbreak enchant bonus per 1 level - Default: 0.04")
    public float spellbreakEnchant = 0.04F;
    @Comment("Suppressing enchant bonus per 1 level - Default: 0.04")
    public float suppressingEnchant = 0.04F;
    @Comment("Glancing Blow damage factor - Default: 0.65")
    public float glancingBlowFactor = 0.65F;
    @Comment("Suppression damage factor - Default: 0.5")
    public float suppressionDamageFactor = 0.5F;
    @Comment("Spellbreak damage factor - Default: 0.0")
    public float spellbreakDamageFactor = 0.0F;
    @Comment("Max spellbreak - Default: 0.75")
    public float maxSpellbreak = 0.75F;
    @Comment("Reabsorption regeneration factor (proportion of 25% of maximum per second) - Default: 1.0")
    public float factor = 1.0F;
    @Comment("Reabsorption regeneration delay in seconds - Default: 4.0")
    public float delay = 4.0F;
    @Comment("Reabsorption custom visuals (Default: true)")
    public boolean visuals = true;
    @Comment("Registration of turtle bracer")
    public boolean turtle_bracer = true;
    @Comment("Mod of turtle bracer")
    public float turtle_bracer_mod = 0.3F;
    @Comment("Registration of turtle girdle")
    public boolean turtle_girdle = true;

    @Comment("Mod of turtle girdle")
    public float turtle_girdle_mod = 1F;
    @Comment("Registration of arcane bracer")
    public boolean arcane_bracer = true;
    @Comment("Mod of arcane bracer")
    public float arcane_bracer_mod = 0.25F;
    @Comment("Registration of nonbeliever amulet")
    public boolean nonbeliever = true;

    @Comment("Mod of nonbeliever amulet")
    public float nonbeliever_mod = 0.5F;
    @Comment("Registration of defiance ring")
    public boolean defiance_ring = true;

    @Comment("Mod of defiance ring")
    public float defiance_ring_mod = 0.15F;
    @Comment("Registration of undying soul")
    public boolean undying_soul = true;

    @Comment("Mod of undying soul")
    public float undying_soul_mod = 0.3F;
    @Comment("Mod of gold absorption ring")
    public float gold_absorption_ring_mod = 4;
    @Comment("Mod of netherite absorption ring")
    public float netherite_absorption_ring_mod = 6;
    @Comment("Mod of gold necklace")
    public float gold_absorption_necklace_mod = 0.25F;
    @Comment("Mod of netherite necklace")
    public float netherite_absorption_necklace_mod = 0.5F;
}
