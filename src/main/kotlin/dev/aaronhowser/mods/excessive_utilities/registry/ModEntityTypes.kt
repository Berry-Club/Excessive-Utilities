package dev.aaronhowser.mods.excessive_utilities.registry

import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import dev.aaronhowser.mods.excessive_utilities.entity.FlatTransferNodeEntity
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.MobCategory
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister
import java.util.function.Supplier

object ModEntityTypes {

	val ENTITY_TYPE_REGISTRY: DeferredRegister<EntityType<*>> =
		DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, ExcessiveUtilities.MOD_ID)

	val FLAT_TRANSFER_NODE =
		registerEntityType(
			"flat_transfer_node",
			MobCategory.MISC,
			1f, 1f,
			::FlatTransferNodeEntity
		) {
			eyeHeight(0.5f)
		}

	fun <T : Entity> registerEntityType(
		name: String,
		category: MobCategory,
		width: Float, height: Float,
		entityFactory: EntityType.EntityFactory<T>,
		builderModifier: EntityType.Builder<T>.() -> Unit = {}
	): DeferredHolder<EntityType<*>, EntityType<T>> {
		return ENTITY_TYPE_REGISTRY.register(name, Supplier {
			EntityType.Builder.of(entityFactory, category)
				.sized(width, height)
				.apply(builderModifier)
				.build(name)
		})
	}

}