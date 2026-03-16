package dev.aaronhowser.mods.excessive_utilities.recipe.base

import com.mojang.datafixers.util.Either
import com.mojang.serialization.Codec
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isBlock
import dev.aaronhowser.mods.aaron.serialization.AaronExtraStreamCodecs
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.tags.TagKey
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import java.util.function.Predicate

typealias BlockEither = Either<Block, TagKey<Block>>

class BlockStateIngredient(
	val blockEither: BlockEither
) : Predicate<BlockState> {

	constructor(block: Block) : this(Either.left(block))
	constructor(tag: TagKey<Block>) : this(Either.right(tag))

	override fun test(t: BlockState): Boolean {
		var success = false

		blockEither
			.ifLeft { block ->
				if (t.isBlock(block)) success = true
			}
			.ifRight { tag ->
				if (t.isBlock(tag)) success = true
			}

		return success
	}

	companion object {
		val CODEC: Codec<BlockStateIngredient> =
			Codec.either(
				Block.CODEC.codec(),
				TagKey.codec(Registries.BLOCK)
			).xmap(::BlockStateIngredient, BlockStateIngredient::blockEither)

		val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, BlockStateIngredient> =
			ByteBufCodecs.either(
				ByteBufCodecs.registry(Registries.BLOCK),
				AaronExtraStreamCodecs.tagKeyStreamCodec(Registries.BLOCK)
			).map(::BlockStateIngredient, BlockStateIngredient::blockEither)
	}

}