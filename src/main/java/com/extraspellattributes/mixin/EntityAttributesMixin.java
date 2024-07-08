package com.extraspellattributes.mixin;

import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.extraspellattributes.ReabsorptionInit.*;

@Mixin(EntityAttributes.class)
public class EntityAttributesMixin {
    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void static_tail_Cleann(CallbackInfo ci) {

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
        Registry.register(Registries.ATTRIBUTE,new Identifier(MOD_ID,"defiance"),DEFIANCE);
        Registry.register(Registries.ATTRIBUTE,new Identifier(MOD_ID,"determination"),RECOUP);
    }
}