package com.extraspellattributes.mixin;


import com.extraspellattributes.PlayerInterface;
import com.extraspellattributes.api.RecoupInstance;
import com.extraspellattributes.interfaces.RecoupLivingEntityInterface;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static java.lang.Math.max;
import static net.minecraft.util.math.MathHelper.sqrt;

@Mixin(PlayerEntity.class)
public class PlayerMixin implements PlayerInterface, RecoupLivingEntityInterface {
    public Entity lastReabAttacked;
    public int lastReabhurt;
    public float damageReabAbsorbed;
    public List<RecoupInstance> recoupInstances = new ArrayList<RecoupInstance>(List.of());

    public int getReabLasthurt() {
        return lastReabhurt;
    }

    @Override
    public float getReabDamageAbsorbed() {
        return damageReabAbsorbed;
    }

    @Override
    public void resetReabDamageAbsorbed() {
        damageReabAbsorbed = 0;
        this.lastReabhurt = ((PlayerEntity) (Object) this).age;
    }

    @Override
    public void ReababsorbDamage(float i) {
        damageReabAbsorbed = damageReabAbsorbed + i;
    }

    public void setReabLasthurt(int lasthurt) {
        this.lastReabhurt = lasthurt;
    }



    @Inject(at = @At("HEAD"), method = "applyDamage", cancellable = true)
    protected void applyDamageMixinSpellblade(DamageSource source, float amount, CallbackInfo info) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        if (!player.isInvulnerableTo(source)) {
            this.setReabLasthurt(player.age);
            this.resetReabDamageAbsorbed();
        }
    }


    @Override
    public List<RecoupInstance> getRecoups() {
        return this.recoupInstances;
    }

    @Override
    public void tickRecoups() {
        for(RecoupInstance instance : this.recoupInstances){
            instance.tick();
        }
        if(!this.recoupInstances.isEmpty()) {
            this.recoupInstances.removeIf(recoupInstance -> recoupInstance.remainingduration <= 0);
        }
    }

    @Override
    public void addRecoup(RecoupInstance instance) {
        this.recoupInstances.add(instance);
    }
}