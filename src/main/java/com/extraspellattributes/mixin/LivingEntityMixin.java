package com.extraspellattributes.mixin;

import com.extraspellattributes.PlayerInterface;
import com.extraspellattributes.ReabsorptionInit;
import com.extraspellattributes.enchantments.ExtraRPGEnchantment;
import com.extraspellattributes.interfaces.RecoupLivingEntityInterface;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.spell_engine.api.spell.SpellInfo;
import net.spell_power.mixin.DamageSourcesAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;
import java.util.Map;

import static com.extraspellattributes.ReabsorptionInit.*;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
	@Shadow
	private DefaultedList<ItemStack> syncedHandStacks;
	@Shadow
	private  DefaultedList<ItemStack> syncedArmorStacks;

	private ItemStack getSyncedHandStack(EquipmentSlot slot) {
		return (ItemStack)this.syncedHandStacks.get(slot.getEntitySlotId());
	}
	private ItemStack getSyncedArmorStack(EquipmentSlot slot) {
		return (ItemStack)this.syncedArmorStacks.get(slot.getEntitySlotId());
	}
	@ModifyVariable(at = @At("HEAD"), method = "damage", argsOnly = true)
	private float damageHeadReab(float amount, DamageSource source, float originalAmount){
		LivingEntity living = (LivingEntity) (Object) this;
		amount = originalAmount;
		if(living.getAttributeInstance(GLANCINGBLOW) != null && source.getAttacker() != null){
			double glancingchance = 0.01*(living.getAttributeValue(GLANCINGBLOW)-100);
			glancingchance += ExtraRPGEnchantment.getEnchantmentLevelEquipmentSum(EVASION,living)*config.evasionEnchant;

			if (living.getRandom().nextFloat() < glancingchance) {
				amount *= config.glancingBlowFactor;
				if (living.getWorld() instanceof ServerWorld serverWorld) {

					serverWorld.playSound(null, living.getBlockPos(), SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, SoundCategory.PLAYERS, 0.5F, 1);
				}

			}

		}
		Registry<DamageType> registry = ((DamageSourcesAccessor)living.getDamageSources()).getRegistry();

		if(living.getAttributeInstance(SPELLSUPPRESS) != null && source.getType().equals(registry.entryOf(DamageTypes.MAGIC).value()) || source.getType().equals(registry.entryOf(DamageTypes.INDIRECT_MAGIC).value())){
			double suppresschance = 0.01*(living.getAttributeValue(SPELLSUPPRESS)-100);
			suppresschance += ExtraRPGEnchantment.getEnchantmentLevelEquipmentSum(SUPPRESSING,living)*config.suppressingEnchant;

			if(living.getRandom().nextFloat() < suppresschance){
				amount *= config.suppressionDamageFactor;
				double acro = 0.01 * (living.getAttributeValue(ACRO) - 100);
				acro += ExtraRPGEnchantment.getEnchantmentLevelEquipmentSum(SPELLBREAK,living)*config.spellbreakEnchant;
				acro = Math.max(acro,config.maxSpellbreak);
				if (living.getRandom().nextFloat() <  acro) {
					amount *= config.spellbreakDamageFactor;
					if(living.getWorld() instanceof ServerWorld serverWorld) {
						serverWorld.playSound(null,living.getBlockPos(),SoundEvents.ENTITY_ELDER_GUARDIAN_CURSE, SoundCategory.PLAYERS, 0.5F, 1);
					}

				}
				else{
					if(living.getWorld() instanceof ServerWorld serverWorld) {

						serverWorld.playSound(null, living.getBlockPos(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 0.5F, 1);

					}
				}
			}
		}
		if(living.getAttributeInstance(DEFIANCE) != null && amount > 1) {
			amount -= (float) Math.pow(living.getAttributeValue(DEFIANCE)+ExtraRPGEnchantment.getEnchantmentLevelEquipmentSum(DEFIANCEENCHANT,living)*config.defianceEnchant,0.5);
			amount = Math.max(1,amount);

		}
		return amount;
	}
	@Inject(at = @At("HEAD"), method = "tick", cancellable = true)
	public void tick_absorption_HEAD(CallbackInfo info) {
		LivingEntity living = (LivingEntity) (Object) this;
		if(living instanceof RecoupLivingEntityInterface recoupLivingEntityInterface && living instanceof PlayerEntity player && !player.getWorld().isClient()){
			recoupLivingEntityInterface.tickRecoups();
		}
		double maximum = living.getAttributeValue(WARDING);
		maximum += ExtraRPGEnchantment.getEnchantmentLevelEquipmentSum(WARDINGENCHANT,living)*config.wardingenchant;
		if (living instanceof PlayerInterface damageInterface && maximum > 0) {

			if(!living.getWorld().getGameRules().getBoolean(CLASSIC_ENERGYSHIELD)) {
				float additional = (float) (config.factor * 0.05 * maximum * (0.173287 * Math.pow(Math.E, -0.173287 * 0.05 * (living.age - damageInterface.getReabLasthurt()))));
				if (damageInterface.getReabLasthurt() != 0 && living.age - damageInterface.getReabLasthurt() < 100 * 20 && damageInterface.getReabDamageAbsorbed() + additional <= maximum) {
					damageInterface.ReababsorbDamage(additional);
				}

				if (living.age < 16 * 20 && damageInterface.getReabLasthurt() == 0 && damageInterface.getReabDamageAbsorbed() + additional <= maximum) {
					damageInterface.ReababsorbDamage(additional);
				}
				if (damageInterface.getReabDamageAbsorbed() > living.getAbsorptionAmount()) {

					if (!living.getWorld().isClient()) {
					living.setAbsorptionAmount(damageInterface.getReabDamageAbsorbed());
				}
				}
			}
			else{
				float additional = (float)maximum*0.25F*0.05F*config.factor;

				if(living.age - damageInterface.getReabLasthurt() >= config.delay *20){
					if(!damageInterface.getReabsorbing()) {

						damageInterface.setReabsorbing(true);
						if (living instanceof ServerPlayerEntity) {
							ServerPlayNetworking.send((ServerPlayerEntity) living, new Identifier(ReabsorptionInit.MOD_ID, "reab"), PacketByteBufs.empty());
						}
					}
					if(living.getAbsorptionAmount() < maximum) {
						if (!living.getWorld().isClient()) {
							living.setAbsorptionAmount((float) Math.min(living.getAbsorptionAmount() + additional, maximum));
						}
					}
				}
			}

		}
	}

	@Unique
	private static final ThreadLocal<Boolean> PROCESSING = ThreadLocal.withInitial(() -> false);

	@Inject(method = "onEquipStack", at = @At("HEAD"))
	private void onEquipStackAbsorption(EquipmentSlot slot, ItemStack oldStack, ItemStack newStack, CallbackInfo ci) {
		if (PROCESSING.get()) return;
		PROCESSING.set(true);
		try {
			if (this instanceof PlayerInterface playerDamageInterface) {
				if (!newStack.isEmpty() && newStack.getAttributeModifiers(slot).containsKey(WARDING)) {
					playerDamageInterface.resetReabDamageAbsorbed();
				}
			}
		} finally {
			PROCESSING.set(false);
		}
	}

	@Inject(method = "createLivingAttributes", at = @At("RETURN"))
	private static void addAttributesextraspellattributes_RETURN(final CallbackInfoReturnable<DefaultAttributeContainer.Builder> info) {
		info.getReturnValue().add(WARDING);
		info.getReturnValue().add(CONVERTFROMFIRE);
		info.getReturnValue().add(CONVERTFROMFROST);
		info.getReturnValue().add(CONVERTFROMARCANE);
		info.getReturnValue().add(CONVERTTOFIRE);
		info.getReturnValue().add(CONVERTTOFROST);
		info.getReturnValue().add(CONVERTTOARCANE);
		info.getReturnValue().add(CONVERTTOHEAL);
		info.getReturnValue().add(GLANCINGBLOW);
		info.getReturnValue().add(SPELLSUPPRESS);
		info.getReturnValue().add(ACRO);
		info.getReturnValue().add(ENDURANCE);
		info.getReturnValue().add(DEFIANCE);
		info.getReturnValue().add(RECOUP);

	}
}