package dev.aaronhowser.mods.excessive_utilities.registry

import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import net.minecraft.core.registries.Registries
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.item.Items
import net.minecraft.world.item.alchemy.Potion
import net.minecraft.world.item.alchemy.Potions
import net.neoforged.neoforge.event.brewing.RegisterBrewingRecipesEvent
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister
import java.util.function.Supplier

object ModPotions {


	val POTION_REGISTRY: DeferredRegister<Potion> =
		DeferredRegister.create(Registries.POTION, ExcessiveUtilities.MOD_ID)

	val GRAVITY: DeferredHolder<Potion, Potion> =
		register("gravity", { MobEffectInstance(ModMobEffects.GRAVITY, 20 * 60) })
	val LONG_GRAVITY: DeferredHolder<Potion, Potion> =
		register("long_gravity", "gravity") { MobEffectInstance(ModMobEffects.GRAVITY, 20 * 60 * 8) }
	val OILY: DeferredHolder<Potion, Potion> =
		register("oily") { MobEffectInstance(ModMobEffects.OILY, 20 * 60) }
	val LONG_OILY: DeferredHolder<Potion, Potion> =
		register("long_oily", "oily") { MobEffectInstance(ModMobEffects.OILY, 20 * 60 * 8) }
	val GREEK_FIRE: DeferredHolder<Potion, Potion> =
		register("greek_fire") { MobEffectInstance(ModMobEffects.GREEK_FIRE, 20 * 60 * 2) }
	val LOVE: DeferredHolder<Potion, Potion> =
		register("love") { MobEffectInstance(ModMobEffects.LOVE, 20 * 10) }
	val RELAPSE: DeferredHolder<Potion, Potion> =
		register("relapse") { MobEffectInstance(ModMobEffects.RELAPSE, 20 * 60 * 8) }
	val SECOND_CHANCE: DeferredHolder<Potion, Potion> =
		register("second_chance") { MobEffectInstance(ModMobEffects.SECOND_CHANCE, 20 * 60 * 2) }
	val DOOM: DeferredHolder<Potion, Potion> =
		register("doom") { MobEffectInstance(ModMobEffects.DOOM, 20 * 60) }
	val PURGING: DeferredHolder<Potion, Potion> =
		register("purging") { MobEffectInstance(ModMobEffects.PURGING) }

	private fun register(
		name: String,
		effect: () -> MobEffectInstance
	): DeferredHolder<Potion, Potion> {
		return POTION_REGISTRY.register(name, Supplier {
			Potion(ExcessiveUtilities.MOD_ID + "." + name, effect())
		})
	}

	private fun register(
		holderName: String,
		potionName: String,
		effect: () -> MobEffectInstance
	): DeferredHolder<Potion, Potion> {
		return POTION_REGISTRY.register(holderName, Supplier {
			Potion(ExcessiveUtilities.MOD_ID + "." + potionName, effect())
		})
	}

	fun registerRecipes(event: RegisterBrewingRecipesEvent) {
		val builder = event.builder

		builder.addMix(Potions.AWKWARD, Items.ROTTEN_FLESH, PURGING)
		builder.addMix(Potions.AWKWARD, Items.BEETROOT, OILY)
		builder.addMix(OILY, Items.REDSTONE, LONG_OILY)
		builder.addMix(OILY, Items.LAVA_BUCKET, GREEK_FIRE)
		builder.addMix(Potions.AWKWARD, Items.OBSIDIAN, GRAVITY)
		builder.addMix(GRAVITY, Items.REDSTONE, LONG_GRAVITY)
		builder.addMix(Potions.AWKWARD, Items.ROSE_BUSH, LOVE)
		builder.addMix(Potions.AWKWARD, Items.POPPY, LOVE)
		builder.addMix(Potions.AWKWARD, Items.RED_TULIP, LOVE)
		builder.addMix(Potions.AWKWARD, Items.JACK_O_LANTERN, RELAPSE)
		builder.addMix(Potions.STRONG_HEALING, Items.ENCHANTED_GOLDEN_APPLE, SECOND_CHANCE)

	}

}