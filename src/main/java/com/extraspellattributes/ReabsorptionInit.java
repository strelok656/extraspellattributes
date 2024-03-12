package com.extraspellattributes;

import com.extraspellattributes.items.ItemInit;
import com.extraspellattributes.trades.CustomTrades;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.minecraft.block.Blocks;
import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.world.GameRules;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReabsorptionInit implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
    public static final Logger LOGGER = LoggerFactory.getLogger("extraspellattributes");
	public static final String MOD_ID = "extraspellattributes";
	public static final ClampedEntityAttribute WARDING = new ClampedEntityAttribute("attribute.name.extraspellattributes.reabsorption", 0,0,9999);
	public static final ClampedEntityAttribute CONVERTFROMFIRE = new ClampedEntityAttribute("attribute.name.extraspellattributes.convertfromfire", 100,100,9999);
	public static final ClampedEntityAttribute CONVERTFROMFROST = new ClampedEntityAttribute("attribute.name.extraspellattributes.convertfromfrost", 100,100,9999);
	public static final ClampedEntityAttribute CONVERTFROMARCANE = new ClampedEntityAttribute("attribute.name.extraspellattributes.convertfromarcane", 100,100,9999);
	public static final ClampedEntityAttribute CONVERTTOFIRE = new ClampedEntityAttribute("attribute.name.extraspellattributes.converttofire", 100,100,9999);
	public static final ClampedEntityAttribute CONVERTTOFROST = new ClampedEntityAttribute("attribute.name.extraspellattributes.converttofrost", 100,100,9999);
	public static final ClampedEntityAttribute CONVERTTOARCANE = new ClampedEntityAttribute("attribute.name.extraspellattributes.converttoarcane", 100,100,9999);
	public static final ClampedEntityAttribute CONVERTTOHEAL = new ClampedEntityAttribute("attribute.name.extraspellattributes.converttoheal", 100,100,9999);
	public static final ClampedEntityAttribute GLANCINGBLOW = new ClampedEntityAttribute("attribute.name.extraspellattributes.glancingblow", 100,100,9999);
	public static final ClampedEntityAttribute SPELLSUPPRESS = new ClampedEntityAttribute("attribute.name.extraspellattributes.spellsuppression", 100,100,9999);
	public static final ClampedEntityAttribute ACRO = new ClampedEntityAttribute("attribute.name.extraspellattributes.serenity", 100,100,9999);
	public static final ClampedEntityAttribute ENDURANCE = new ClampedEntityAttribute("attribute.name.extraspellattributes.endu", 100,100,9999);
	public static final ClampedEntityAttribute RECOUP = new ClampedEntityAttribute("attribute.name.extraspellattributes.determination", 100,100,9999);

	public static final GameRules.Key<GameRules.BooleanRule> CLASSIC_ENERGYSHIELD = GameRuleRegistry.register("classicEnergyShield", GameRules.Category.PLAYER, GameRuleFactory.createBooleanRule(true));
	@Override
	public void onInitialize() {
		Registry.register(Registries.ATTRIBUTE,new Identifier(MOD_ID,"reabsorption"),WARDING);
		Registry.register(Registries.ATTRIBUTE,new Identifier(MOD_ID,"converttofire"),CONVERTTOFIRE);
		Registry.register(Registries.ATTRIBUTE,new Identifier(MOD_ID,"convertfromfire"),CONVERTFROMFIRE);
		Registry.register(Registries.ATTRIBUTE,new Identifier(MOD_ID,"converttoarcane"),CONVERTTOARCANE);
		Registry.register(Registries.ATTRIBUTE,new Identifier(MOD_ID,"convertfromarcane"),CONVERTFROMARCANE);
		Registry.register(Registries.ATTRIBUTE,new Identifier(MOD_ID,"converttofrost"),CONVERTTOFROST);
		Registry.register(Registries.ATTRIBUTE,new Identifier(MOD_ID,"convertfromfrost"),CONVERTFROMFROST);
		Registry.register(Registries.ATTRIBUTE,new Identifier(MOD_ID,"converttoheal"),CONVERTTOHEAL);
		Registry.register(Registries.ATTRIBUTE,new Identifier(MOD_ID,"glancingblow"),GLANCINGBLOW);
		Registry.register(Registries.ATTRIBUTE,new Identifier(MOD_ID,"spellsuppression"),SPELLSUPPRESS);
		Registry.register(Registries.ATTRIBUTE,new Identifier(MOD_ID,"serenity"),ACRO);
		Registry.register(Registries.ATTRIBUTE,new Identifier(MOD_ID,"endurance"),ENDURANCE);
		Registry.register(Registries.ATTRIBUTE,new Identifier(MOD_ID,"determination"),RECOUP);

		ItemInit.register();
		CustomTrades.registerCustomTrades();
		LootTableEvents.MODIFY.register((resourceManager, lootManager, id, tableBuilder, source) -> {
			// Let's only modify built-in loot tables and leave data pack loot tables untouched by checking the source.
			// We also check that the loot table ID is equal to the ID we want.
			if (source.isBuiltin() && LootTables.BASTION_TREASURE_CHEST.equals(id)) {
				LootPool.Builder poolBuilder = LootPool.builder()
						.with(ItemEntry.builder(ItemInit.NETHERITEDIAMOND));

				tableBuilder.pool(poolBuilder);
				LootPool.Builder poolBuilder2 = LootPool.builder()
						.with(ItemEntry.builder(ItemInit.NETHERITEDIAMONDAMULET));

				tableBuilder.pool(poolBuilder2);
			}
			if (source.isBuiltin() && LootTables.SIMPLE_DUNGEON_CHEST.equals(id)) {
				LootPool.Builder poolBuilder = LootPool.builder()
						.with(ItemEntry.builder(ItemInit.GOLDQUARTZRING));

				tableBuilder.pool(poolBuilder);
				LootPool.Builder poolBuilder2 = LootPool.builder()
						.with(ItemEntry.builder(ItemInit.GOLDQUARTZAMULET));

				tableBuilder.pool(poolBuilder2);
			}
		});		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LOGGER.info("Hello Fabric world!");
	}
}