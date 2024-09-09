package com.extraspellattributes;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.util.Identifier;

public class ReabsorptionClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(new Identifier(ReabsorptionInit.MOD_ID,"reab"), (client, handler, buf, responseSender) -> {
           if(client.player instanceof PlayerInterface playerInterface){
               playerInterface.setReabsorbing(true);

           }
        });
        ClientPlayNetworking.registerGlobalReceiver(new Identifier(ReabsorptionInit.MOD_ID,"lasthurt"), (client, handler, buf, responseSender) -> {
            if(client.player instanceof PlayerInterface playerInterface){
                playerInterface.setReabLasthurt(client.player.age);

            }
        });
        ClientPlayNetworking.registerGlobalReceiver(new Identifier(ReabsorptionInit.MOD_ID,"reabstop"), (client, handler, buf, responseSender) -> {
            if(client.player instanceof PlayerInterface playerInterface){
                playerInterface.setReabsorbing(false);

            }
        });
    }
}
