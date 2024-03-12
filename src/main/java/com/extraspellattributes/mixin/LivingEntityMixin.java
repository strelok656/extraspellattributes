package com.extraspellattributes.mixin;

import com.extraspellattributes.PlayerInterface;
import com.extraspellattributes.interfaces.RecoupLivingEntityInterface;
import net.fabricmc.loader.api.FabricLoader;
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
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.spell_power.mixin.DamageSourcesAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
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
			if (living.getRandom().nextFloat() < glancingchance) {
				amount *= 0.65;
			}
			if(living.getAttributeInstance(ENDURANCE) != null) {
				double endurance = 0.01 * (living.getAttributeValue(ENDURANCE) - 100);

				if(living.getRandom().nextFloat() < glancingchance){
					amount = (float) Math.max(0,amount - living.getMaxHealth()*endurance);
				}

			}

		}
		Registry<DamageType> registry = ((DamageSourcesAccessor)living.getDamageSources()).getRegistry();

		if(living.getAttributeInstance(SPELLSUPPRESS) != null && source.getType().equals(registry.entryOf(DamageTypes.MAGIC).value()) || source.getType().equals(registry.entryOf(DamageTypes.INDIRECT_MAGIC).value())){
			double suppresschance = 0.01*(living.getAttributeValue(SPELLSUPPRESS)-100);

			if(living.getRandom().nextFloat() < suppresschance){
				amount *= 0.5;
				double acro = 0.01 * (living.getAttributeValue(ACRO) - 100);
				if (living.getRandom().nextFloat() <  acro) {
					amount *= 0;
				}
			}
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
		if(FabricLoader.getInstance().isModLoaded("spellbladenext") && Registries.ATTRIBUTE.get(new Identifier("spellbladenext","warding")) != null){
			maximum += living.getAttributeValue(Registries.ATTRIBUTE.get(new Identifier("spellbladenext","warding")));
		}
		if (living instanceof PlayerInterface damageInterface && maximum > 0) {

			if(!living.getWorld().getGameRules().getBoolean(CLASSIC_ENERGYSHIELD)) {
				float additional = (float) (0.05 * maximum * (0.173287 * Math.pow(Math.E, -0.173287 * 0.05 * (living.age - damageInterface.getReabLasthurt()))));
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
				float additional = (float)maximum*0.25F*0.05F;

				if(living.age - damageInterface.getReabLasthurt() >= 4*20){
					if(living.getAbsorptionAmount() < maximum) {
						if (!living.getWorld().isClient()) {
							living.setAbsorptionAmount((float) Math.min(living.getAbsorptionAmount() + additional, maximum));
						}
					}
				}
			}

		}
	}
	@Inject(at = @At("HEAD"), method = "onEquipStack", cancellable = true)
	public void onEquipStackabsorption(EquipmentSlot slot, ItemStack oldStack, ItemStack newStack, CallbackInfo info) {
		if (newStack.getAttributeModifiers(slot).containsKey(WARDING)) {
			LivingEntity entity = (LivingEntity) (Object) this;
			if (entity instanceof PlayerInterface playerDamageInterface) {
				playerDamageInterface.resetReabDamageAbsorbed();
			}
		}
	}
	@Inject(at = @At("HEAD"), method = "Lnet/minecraft/entity/LivingEntity;sendEquipmentChanges(Ljava/util/Map;)V", cancellable = true)
	private void sendEquipmentChanges(Map<EquipmentSlot, ItemStack> equipmentChanges, CallbackInfo callbackInfo) {
		LivingEntity living = (LivingEntity) (Object) this;
		Map<EquipmentSlot, ItemStack> map = null;
		EquipmentSlot[] var2 = EquipmentSlot.values();
		int var3 = var2.length;

		for(int var4 = 0; var4 < var3; ++var4) {
			EquipmentSlot equipmentSlot = var2[var4];
			ItemStack itemStack;
			if(equipmentSlot.getType().equals(EquipmentSlot.Type.ARMOR)) {
				itemStack = this.getSyncedArmorStack(equipmentSlot);

				ItemStack itemStack2 = living.getEquippedStack(equipmentSlot);
				if (living.areItemsDifferent(itemStack, itemStack2)) {
					float toremove = 0;
					Collection<EntityAttributeModifier> modifiers = itemStack.getAttributeModifiers(equipmentSlot).get(WARDING);
					Collection<EntityAttributeModifier> modifiers2 = itemStack2.getAttributeModifiers(equipmentSlot).get(WARDING);

					for (EntityAttributeModifier modifier : modifiers) {
						if (modifier.getOperation().equals(EntityAttributeModifier.Operation.ADDITION)) {
							toremove -= modifier.getValue();
						}
					}
					for (EntityAttributeModifier modifier : modifiers2) {
						if (modifier.getOperation().equals(EntityAttributeModifier.Operation.ADDITION)) {
							toremove += modifier.getValue();
						}
					}
					Collection<EntityAttributeModifier> modifiers3 = living.getAttributeInstance(WARDING).getModifiers(EntityAttributeModifier.Operation.MULTIPLY_BASE);
					Collection<EntityAttributeModifier> modifiers4 = living.getAttributeInstance(WARDING).getModifiers(EntityAttributeModifier.Operation.MULTIPLY_TOTAL);
					float mult = 1;
					for (EntityAttributeModifier modifier : modifiers3) {
						mult += modifier.getValue();
					}
					toremove *= mult;
					for (EntityAttributeModifier modifier : modifiers4) {
						toremove *= 1+modifier.getValue();
					}
					if (toremove < 0 && !living.getWorld().isClient()) {
						living.setAbsorptionAmount(living.getAbsorptionAmount() + toremove);
						if (living instanceof PlayerInterface playerDamageInterface) {
							playerDamageInterface.resetReabDamageAbsorbed();
						}
					}

				}
			}
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
		info.getReturnValue().add(RECOUP);

	}
}