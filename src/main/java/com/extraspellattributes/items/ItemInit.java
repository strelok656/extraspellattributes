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
    public static Item GOLDQUARTZRING;
    public static Item NETHERITEDIAMOND;
    public static Item GOLDQUARTZAMULET;
    public static Item NETHERITEDIAMONDAMULET;
    public static RegistryKey<ItemGroup> KEY = RegistryKey.of(Registries.ITEM_GROUP.getKey(),new Identifier(ReabsorptionInit.MOD_ID,"generic"));
    private static Item registerItem(String name, Item item){
        return Registry.register(Registries.ITEM, new Identifier(ReabsorptionInit.MOD_ID,name),item);
    }
    private static void addItemToGroup(Item item){
        ItemGroupEvents.modifyEntriesEvent(KEY).register((content) -> {
            content.add(item);
        });
    }
    public static void register() {
        GOLDQUARTZRING = registerItem("goldquartzring", new Ring(new FabricItemSettings().maxCount(1), ReabsorptionInit.config.gold_absorption_ring_mod));
        extraspellattributes = FabricItemGroup.builder()
                .icon(() -> new ItemStack(GOLDQUARTZRING))
                .displayName(Text.translatable("itemGroup.extraspellattributes.general"))
                .build();
        Registry.register(Registries.ITEM_GROUP, KEY, extraspellattributes);

        addItemToGroup(GOLDQUARTZRING);

        NETHERITEDIAMOND = registerItem("netheritediamondring",new Ring(new FabricItemSettings().maxCount(1),ReabsorptionInit.config.netherite_absorption_ring_mod));
        addItemToGroup(NETHERITEDIAMOND);

        GOLDQUARTZAMULET = registerItem("goldquartzamulet",new Amulet(new FabricItemSettings().maxCount(1),ReabsorptionInit.config.gold_absorption_necklace_mod));
        addItemToGroup(GOLDQUARTZAMULET);

        NETHERITEDIAMONDAMULET = registerItem("netheritediamondamulet",new Amulet(new FabricItemSettings().maxCount(1),ReabsorptionInit.config.netherite_absorption_necklace_mod));
        addItemToGroup(NETHERITEDIAMONDAMULET);

        if(ReabsorptionInit.config.turtle_bracer)
        {
            Item MARTIALBRACER = registerItem("martialbracer",new MartialBracer(new FabricItemSettings().maxCount(1),ReabsorptionInit.config.turtle_bracer_mod));
            addItemToGroup(MARTIALBRACER);

        }
        if(ReabsorptionInit.config.arcane_bracer)
        {
            Item ARCANEBRACER = registerItem("arcanebracer",new ArcaneBracer(new FabricItemSettings().maxCount(1),ReabsorptionInit.config.arcane_bracer_mod));
            addItemToGroup(ARCANEBRACER);

        }
        if(ReabsorptionInit.config.nonbeliever)
        {
            Item NONBELIEVER = registerItem("nonbelieversamulet",new NonbelieverAmulet(new FabricItemSettings().maxCount(1),ReabsorptionInit.config.nonbeliever_mod));
            addItemToGroup(NONBELIEVER);

        }
        if(ReabsorptionInit.config.turtle_girdle)
        {
            Item BLACKBELT = registerItem("turtlegirdle",new BlackBelt(new FabricItemSettings().maxCount(1),ReabsorptionInit.config.turtle_girdle_mod));
            addItemToGroup(BLACKBELT);

        }
        if(ReabsorptionInit.config.defiance_ring)
        {
            Item SANGUINERING = registerItem("defiancering",new SanguineRing(new FabricItemSettings().maxCount(1),ReabsorptionInit.config.defiance_ring_mod));
            addItemToGroup(SANGUINERING);

        }
        if(ReabsorptionInit.config.undying_soul)
        {
            Item UNDYINGNECKLACE = registerItem("undyingsoul",new SanguineNecklace(new FabricItemSettings().maxCount(1),ReabsorptionInit.config.undying_soul_mod));
            addItemToGroup(UNDYINGNECKLACE);

        }



    }
}
