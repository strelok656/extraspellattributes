package com.extraspellattributes;

import com.extraspellattributes.config.ServerConfig;
import com.extraspellattributes.config.ServerConfigWrapper;
import com.extraspellattributes.enchantments.BattleRouse;
import com.extraspellattributes.enchantments.ExtraRPGEnchantment;
import com.extraspellattributes.items.ItemInit;
import com.extraspellattributes.trades.CustomTrades;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import me.shedaniel.autoconfig.serializer.PartitioningSerializer;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.provider.number.BinomialLootNumberProvider;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.world.GameRules;
import net.spell_power.api.SpellDamageSource;
import net.spell_power.api.SpellSchool;
import net.spell_power.api.SpellSchools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReabsorptionInit implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
    public static final Logger LOGGER = LoggerFactory.getLogger("extraspellattributes");
	public static final String MOD_ID = "extraspellattributes";
	public static ServerConfig config;

	public static final ClampedEntityAttribute WARDING = new ClampedEntityAttribute("attribute.name.extraspellattributes.reabsorption", 0,0,9999);
	public static final ClampedEntityAttribute CONVERTFROMFIRE = new ClampedEntityAttribute("attribute.name.extraspellattributes.convertfromfire", 100,100,9999);
	public static final ClampedEntityAttribute CONVERTFROMFROST = new ClampedEntityAttribute("attribute.name.extraspellattributes.convertfromfrost", 100,100,9999);
	public static final ClampedEntityAttribute CONVERTFROMARCANE = new ClampedEntityAttribute("attribute.name.extraspellattributes.convertfromarcane", 100,100,9999);
	public static final ClampedEntityAttribute CONVERTTOFIRE = new ClampedEntityAttribute("attribute.name.extraspellattributes.converttofire", 100,100,9999);
	public static final ClampedEntityAttribute CONVERTTOFROST = new ClampedEntityAttribute("attribute.name.extraspellattributes.converttofrost", 100,100,9999);
	public static final ClampedEntityAttribute CONVERTTOARCANE = new ClampedEntityAttribute("attribute.name.extraspellattributes.converttoarcane", 100,100,9999);
	public static final ClampedEntityAttribute CONVERTTOHEAL = new ClampedEntityAttribute("attribute.name.extraspellattributes.converttoheal", 100,100,9999);
	public static final ClampedEntityAttribute GLANCINGBLOW = new ClampedEntityAttribute("attribute.name.extraspellattributes.glancingblow", 100,100,200);
	public static final ClampedEntityAttribute SPELLSUPPRESS = new ClampedEntityAttribute("attribute.name.extraspellattributes.spellsuppression", 100,100,200);
	public static final ClampedEntityAttribute ACRO = new ClampedEntityAttribute("attribute.name.extraspellattributes.serenity", 100,100,175);
	public static final ClampedEntityAttribute ENDURANCE = new ClampedEntityAttribute("attribute.name.extraspellattributes.endu", 0,0,999);
	public static final ClampedEntityAttribute DEFIANCE = new ClampedEntityAttribute("attribute.name.extraspellattributes.defi", 0,0,999);

	public static final ClampedEntityAttribute RECOUP = new ClampedEntityAttribute("attribute.name.extraspellattributes.determination", 100,100,9999);

	public static Enchantment BATTLEROUSE = new ExtraRPGEnchantment(Enchantment.Rarity.UNCOMMON, EnchantmentTarget.ARMOR, EquipmentSlot.values());
	public static Enchantment DEFIANCEENCHANT = new ExtraRPGEnchantment(Enchantment.Rarity.UNCOMMON, EnchantmentTarget.ARMOR, EquipmentSlot.values());
	public static Enchantment EVASION = new ExtraRPGEnchantment(Enchantment.Rarity.UNCOMMON, EnchantmentTarget.ARMOR, EquipmentSlot.values());
	public static Enchantment SPELLBREAK = new ExtraRPGEnchantment(Enchantment.Rarity.UNCOMMON, EnchantmentTarget.ARMOR, EquipmentSlot.values());
	public static Enchantment SUPPRESSING = new ExtraRPGEnchantment(Enchantment.Rarity.UNCOMMON, EnchantmentTarget.ARMOR, EquipmentSlot.values());
	public static Enchantment WARDINGENCHANT = new ExtraRPGEnchantment(Enchantment.Rarity.UNCOMMON, EnchantmentTarget.ARMOR, EquipmentSlot.values());

	public static final GameRules.Key<GameRules.BooleanRule> CLASSIC_ENERGYSHIELD = GameRuleRegistry.register("classicEnergyShield", GameRules.Category.PLAYER, GameRuleFactory.createBooleanRule(true));
	@Override
	public void onInitialize() {

		AutoConfig.register(ServerConfigWrapper.class, PartitioningSerializer.wrap(JanksonConfigSerializer::new));
		config = AutoConfig.getConfigHolder(ServerConfigWrapper.class).getConfig().server;
		Registry.register(Registries.ENCHANTMENT, new Identifier(MOD_ID, "battle"), FROST);
		Registry.register(Registries.ENCHANTMENT, new Identifier(MOD_ID, "frost"), FROST);
		Registry.register(Registries.ENCHANTMENT, new Identifier(MOD_ID, "frost"), FROST);
		Registry.register(Registries.ENCHANTMENT, new Identifier(MOD_ID, "frost"), FROST);
		Registry.register(Registries.ENCHANTMENT, new Identifier(MOD_ID, "frost"), FROST);
		Registry.register(Registries.ENCHANTMENT, new Identifier(MOD_ID, "frost"), FROST);
		ItemInit.register();
		CustomTrades.registerCustomTrades();
		LootTableEvents.MODIFY.register((resourceManager, lootManager, id, tableBuilder, source) -> {
			// Let's only modify built-in loot tables and leave data pack loot tables untouched by checking the source.
			// We also check that the loot table ID is equal to the ID we want.
			if (source.isBuiltin() && LootTables.BASTION_TREASURE_CHEST.equals(id)) {
				LootPool.Builder poolBuilder = LootPool.builder()
						.with(ItemEntry.builder(ItemInit.NETHERITEDIAMOND));
				poolBuilder.rolls(BinomialLootNumberProvider.create(1,0.2F));
				tableBuilder.pool(poolBuilder);
				LootPool.Builder poolBuilder2 = LootPool.builder()
						.with(ItemEntry.builder(ItemInit.NETHERITEDIAMONDAMULET));

				poolBuilder2.rolls(BinomialLootNumberProvider.create(1,0.2F));
				tableBuilder.pool(poolBuilder2);
			}
			if (source.isBuiltin() && LootTables.SIMPLE_DUNGEON_CHEST.equals(id)) {
				LootPool.Builder poolBuilder = LootPool.builder()
						.with(ItemEntry.builder(ItemInit.GOLDQUARTZRING));

				poolBuilder.rolls(BinomialLootNumberProvider.create(1,0.2F));
				tableBuilder.pool(poolBuilder);
				LootPool.Builder poolBuilder2 = LootPool.builder()
						.with(ItemEntry.builder(ItemInit.GOLDQUARTZAMULET));

				poolBuilder2.rolls(BinomialLootNumberProvider.create(1,0.2F));
				tableBuilder.pool(poolBuilder2);
			}
		});		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		CONVERTTOFROST.setTracked(true);
		CONVERTTOFIRE.setTracked(true);
		CONVERTTOARCANE.setTracked(true);
		CONVERTTOHEAL.setTracked(true);
		CONVERTFROMARCANE.setTracked(true);
		CONVERTFROMFROST.setTracked(true);
		CONVERTFROMFIRE.setTracked(true);
		WARDING.setTracked(true);
		SpellSchools.FROST.addSource(SpellSchool.Trait.POWER, SpellSchool.Apply.ADD,queryArgs -> {
			return queryArgs.entity().getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE)*0.01*(queryArgs.entity().getAttributeValue(CONVERTTOFROST)-100);
		});
		SpellSchools.FIRE.addSource(SpellSchool.Trait.POWER, SpellSchool.Apply.ADD,queryArgs -> {
			return queryArgs.entity().getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE)*0.01*(queryArgs.entity().getAttributeValue(CONVERTTOFIRE)-100);
		});
		SpellSchools.ARCANE.addSource(SpellSchool.Trait.POWER, SpellSchool.Apply.ADD,queryArgs -> {
			return queryArgs.entity().getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE)*0.01*(queryArgs.entity().getAttributeValue(CONVERTTOARCANE)-100);
		});
		SpellSchools.HEALING.addSource(SpellSchool.Trait.POWER, SpellSchool.Apply.ADD,queryArgs -> {
			return queryArgs.entity().getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE)*0.01*(queryArgs.entity().getAttributeValue(CONVERTTOHEAL)-100);
		});
		LOGGER.info("Hello Fabric world!");
	}
}