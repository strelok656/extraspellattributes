package com.extraspellattributes.items;

import com.extraspellattributes.ReabsorptionInit;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ItemInit {
    public static ItemGroup extraspellattributes;
    public static final Item GOLDQUARTZRING = new Ring(new FabricItemSettings().maxCount(1),4);
    public static final Item NETHERITEDIAMOND = new Ring(new FabricItemSettings().maxCount(1),6);
    public static final Item GOLDQUARTZAMULET = new Amulet(new FabricItemSettings().maxCount(1),0.25F);
    public static final Item NETHERITEDIAMONDAMULET = new Amulet(new FabricItemSettings().maxCount(1),0.5F);
    public static final Item MARTIALBRACER = new MartialBracer(new FabricItemSettings().maxCount(1),0.3F);
    public static final Item ARCANEBRACER = new ArcaneBracer(new FabricItemSettings().maxCount(1),0.25F);
    public static final Item NONBELIEVER = new NonbelieverAmulet(new FabricItemSettings().maxCount(1),0.5F);
    public static final Item BLACKBELT = new BlackBelt(new FabricItemSettings().maxCount(1),0.25F);
    public static final Item SANGUINERING = new SanguineRing(new FabricItemSettings().maxCount(1),0.15F);
    public static final Item UNDYINGNECKLACE = new SanguineNecklace(new FabricItemSettings().maxCount(1),0.30F);

    public static RegistryKey<ItemGroup> KEY = RegistryKey.of(Registries.ITEM_GROUP.getKey(),new Identifier(ReabsorptionInit.MOD_ID,"generic"));

    public static void register(){
        extraspellattributes = FabricItemGroup.builder()
                .icon(() -> new ItemStack(ItemInit.GOLDQUARTZRING))
                .displayName(Text.translatable("itemGroup.extraspellattributes.general"))
                .build();

        Registry.register(Registries.ITEM_GROUP, KEY, extraspellattributes);
        Registry.register(Registries.ITEM,new Identifier(ReabsorptionInit.MOD_ID,"goldquartzring"),GOLDQUARTZRING);
        Registry.register(Registries.ITEM,new Identifier(ReabsorptionInit.MOD_ID,"goldquartzamulet"),GOLDQUARTZAMULET);
        Registry.register(Registries.ITEM,new Identifier(ReabsorptionInit.MOD_ID,"netheritediamondring"),NETHERITEDIAMOND);
        Registry.register(Registries.ITEM,new Identifier(ReabsorptionInit.MOD_ID,"netheritediamondamulet"),NETHERITEDIAMONDAMULET);
        Registry.register(Registries.ITEM,new Identifier(ReabsorptionInit.MOD_ID,"martialbracer"),MARTIALBRACER);
        Registry.register(Registries.ITEM,new Identifier(ReabsorptionInit.MOD_ID,"arcanebracer"),ARCANEBRACER);
        Registry.register(Registries.ITEM,new Identifier(ReabsorptionInit.MOD_ID,"nonbelieversamulet"),NONBELIEVER);
        Registry.register(Registries.ITEM,new Identifier(ReabsorptionInit.MOD_ID,"turtlegirdle"),BLACKBELT);
        Registry.register(Registries.ITEM,new Identifier(ReabsorptionInit.MOD_ID,"defiancering"),SANGUINERING);
        Registry.register(Registries.ITEM,new Identifier(ReabsorptionInit.MOD_ID,"undyingsoul"),UNDYINGNECKLACE);

        ItemGroupEvents.modifyEntriesEvent(KEY).register((content) -> {
            content.add(GOLDQUARTZRING);
            content.add(NETHERITEDIAMOND);
            content.add(GOLDQUARTZAMULET);
            content.add(NETHERITEDIAMONDAMULET);
            content.add(MARTIALBRACER);
            content.add(BLACKBELT);
            content.add(ARCANEBRACER);
            content.add(NONBELIEVER);
            content.add(SANGUINERING);
            content.add(UNDYINGNECKLACE);
        });

    }
}
