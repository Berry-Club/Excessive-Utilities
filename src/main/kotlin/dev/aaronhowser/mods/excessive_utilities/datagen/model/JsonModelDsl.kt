package dev.aaronhowser.mods.excessive_utilities.datagen.model

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import net.minecraft.core.Direction
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.data.CachedOutput
import net.minecraft.data.DataProvider
import net.minecraft.data.PackOutput
import net.minecraft.resources.Identifier
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemDisplayContext
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.block.state.properties.Property
import java.util.concurrent.CompletableFuture

/** Typed resource generation shared by the block and item providers.
 * Geometry remains procedural, while client item selection uses the current item-model format.
 */
open class ModelFile(val location: Identifier) {
    class UncheckedModelFile(path: String) : ModelFile(Identifier.parse(path))
}

typealias BlockModelBuilder = JsonModelBuilder
typealias ItemModelBuilder = JsonModelBuilder

object ModelBuilder {
    enum class FaceRotation(val degrees: Int) { ZERO(0), CLOCKWISE_90(90), UPSIDE_DOWN(180), COUNTERCLOCKWISE_90(270) }
}

private fun vector(vararg values: Float): JsonArray = JsonArray().apply { values.forEach { add(it) } }

class JsonModelBuilder(location: Identifier) : ModelFile(location) {
    private val json = JsonObject()
    private val textures = JsonObject()
    private val elements = JsonArray()
    private val display = JsonObject()
    private var forceTranslucent = false
    internal val overrides = mutableListOf<ItemOverride>()

    fun parent(model: ModelFile) = apply { json.addProperty("parent", model.location.toString()) }
    fun texture(key: String, texture: Identifier) = texture(key, texture.toString())
    fun texture(key: String, texture: String) = apply { textures.addProperty(key, texture) }
    fun particle(texture: Identifier) = texture("particle", texture)
    fun renderType(type: String) = apply {
        require(type == "cutout" || type == "translucent") { "Unsupported model material: $type" }
        // 26.1 selects cutout from sprite transparency; translucent materials opt in per texture.
        forceTranslucent = type == "translucent"
    }
    fun element(block: ElementBuilder.() -> Unit) = apply {
        elements.add(ElementBuilder().apply(block).toJson())
    }
    fun transforms(block: TransformBuilder.() -> Unit) = apply { TransformBuilder(display).apply(block) }
    fun override(block: ItemOverride.() -> Unit) = apply { overrides.add(ItemOverride().apply(block).validate()) }
    internal fun toJson(): JsonObject = json.deepCopy().apply {
        if (!textures.isEmpty) add("textures", textures.deepCopy().apply {
            if (forceTranslucent) {
                for (key in keySet().toList()) {
                    val texture = get(key).asString
                    if (!texture.startsWith("#")) add(key, JsonObject().apply {
                        addProperty("sprite", texture)
                        addProperty("force_translucent", true)
                    })
                }
            }
        })
        if (!elements.isEmpty) add("elements", elements)
        if (!display.isEmpty) add("display", display)
        check(has("parent") || has("elements")) { "Model $location has no parent or geometry" }
    }
}

class ElementBuilder {
    private val json = JsonObject()
    private val faces = JsonObject()
    fun from(x: Float, y: Float, z: Float) = apply { json.add("from", vector(x, y, z)) }
    fun to(x: Float, y: Float, z: Float) = apply { json.add("to", vector(x, y, z)) }
    fun shade(shade: Boolean) = apply { json.addProperty("shade", shade) }
    fun emissivity(block: Int, sky: Int) = apply {
        require(block in 0..15 && sky in 0..15)
        json.addProperty("light_emission", maxOf(block, sky))
    }
    fun allFaces(block: (Direction, FaceBuilder) -> Unit) = apply {
        Direction.entries.forEach { direction -> face(direction) { block(direction, this) } }
    }
    fun allFacesExcept(block: (Direction, FaceBuilder) -> Unit, excluded: Set<Direction>) = apply {
        Direction.entries.filterNot { it in excluded }.forEach { direction -> face(direction) { block(direction, this) } }
    }
    fun face(direction: Direction, block: FaceBuilder.() -> Unit) = apply {
        faces.add(direction.serializedName, FaceBuilder().apply(block).toJson())
    }
    fun rotation(): RotationBuilder = RotationBuilder(this, JsonObject().also { json.add("rotation", it) })
    internal fun toJson(): JsonObject {
        check(json.has("from") && json.has("to") && !faces.isEmpty) { "Incomplete model element: $json" }
        json.add("faces", faces)
        return json
    }
}

class RotationBuilder(private val element: ElementBuilder, private val json: JsonObject) {
    fun origin(x: Float, y: Float, z: Float) = apply { json.add("origin", vector(x, y, z)) }
    fun axis(axis: Direction.Axis) = apply { json.addProperty("axis", axis.serializedName) }
    fun angle(angle: Float) = apply { json.addProperty("angle", angle) }
    fun end(): ElementBuilder = element
}

class FaceBuilder {
    private val json = JsonObject()
    fun texture(texture: String) = apply { json.addProperty("texture", texture) }
    fun uvs(u1: Float, v1: Float, u2: Float, v2: Float) = apply { json.add("uv", vector(u1, v1, u2, v2)) }
    fun cullface(direction: Direction) = apply { json.addProperty("cullface", direction.serializedName) }
    fun tintindex(index: Int) = apply { json.addProperty("tintindex", index) }
    fun rotation(rotation: ModelBuilder.FaceRotation) = apply { json.addProperty("rotation", rotation.degrees) }
    fun emissivity(block: Int, sky: Int) = apply {
        require(block in 0..15 && sky in 0..15)
        json.add("neoforge_data", JsonObject().apply { addProperty("light_emission", maxOf(block, sky)) })
    }
    internal fun toJson(): JsonObject {
        check(json.has("texture")) { "Model face has no texture" }
        return json
    }
}

class TransformBuilder(private val display: JsonObject) {
    fun transform(context: ItemDisplayContext, block: Transform.() -> Unit) {
        display.add(context.serializedName, Transform().apply(block).json)
    }
}

class Transform {
    internal val json = JsonObject()
    fun rotation(x: Float, y: Float, z: Float) = apply { json.add("rotation", vector(x, y, z)) }
    fun translation(x: Float, y: Float, z: Float) = apply { json.add("translation", vector(x, y, z)) }
    fun scale(scale: Float) = scale(scale, scale, scale)
    fun scale(x: Float, y: Float, z: Float) = apply { json.add("scale", vector(x, y, z)) }
}

class ItemOverride {
    internal val predicates = linkedMapOf<Identifier, Float>()
    internal lateinit var target: ModelFile
    fun predicate(property: Identifier, threshold: Float) = apply { predicates[property] = threshold }
    fun model(model: ModelFile) = apply { target = model }
    internal fun validate() = apply { check(::target.isInitialized && predicates.isNotEmpty()) { "Incomplete item model override" } }
}

class ModelCollection(private val namespace: String, private val folder: String) {
    internal val entries = linkedMapOf<Identifier, JsonModelBuilder>()
    fun getBuilder(name: String): JsonModelBuilder {
        val id = if (':' in name) Identifier.parse(name) else Identifier.fromNamespaceAndPath(namespace, name)
        val location = id.withPrefix("$folder/")
        return entries.getOrPut(location) { JsonModelBuilder(location) }
    }
    fun getExistingFile(id: Identifier): ModelFile = ModelFile(id)
    fun withExistingParent(name: String, parent: Identifier) = getBuilder(name).parent(ModelFile(parent))
    fun withExistingParent(name: String, parent: String) = withExistingParent(name, Identifier.parse(parent))
    private fun template(name: String, template: String, vararg textures: Pair<String, Identifier>): JsonModelBuilder =
        withExistingParent(name, Identifier.withDefaultNamespace("block/$template")).apply {
            textures.forEach { (slot, texture) -> texture(slot, texture) }
        }
    fun cubeAll(name: String, all: Identifier) = template(name, "cube_all", "all" to all)
    fun cubeBottomTop(name: String, side: Identifier, bottom: Identifier, top: Identifier) =
        template(name, "cube_bottom_top", "side" to side, "bottom" to bottom, "top" to top)
    fun cubeTop(name: String, side: Identifier, top: Identifier) = template(name, "cube_top", "side" to side, "top" to top)
    fun cube(name: String, down: Identifier, up: Identifier, north: Identifier, south: Identifier, east: Identifier, west: Identifier) =
        template(name, "cube", "down" to down, "up" to up, "north" to north, "south" to south, "east" to east, "west" to west)
    fun orientable(name: String, side: Identifier, front: Identifier, top: Identifier) =
        template(name, "orientable", "side" to side, "front" to front, "top" to top)
    fun orientableWithBottom(name: String, side: Identifier, front: Identifier, bottom: Identifier, top: Identifier) =
        template(name, "orientable_with_bottom", "side" to side, "front" to front, "bottom" to bottom, "top" to top)
    fun cross(name: String, cross: Identifier) = template(name, "cross", "cross" to cross)
}

class ConfiguredModel private constructor(private val json: JsonObject) {
    internal fun toJson(): JsonObject = json
    class Builder {
        private val json = JsonObject()
        fun modelFile(model: ModelFile) = apply { json.addProperty("model", model.location.toString()) }
        fun rotationX(degrees: Int) = apply { require(degrees % 90 == 0); json.addProperty("x", Math.floorMod(degrees, 360)) }
        fun rotationY(degrees: Int) = apply { require(degrees % 90 == 0); json.addProperty("y", Math.floorMod(degrees, 360)) }
        fun build(): Array<ConfiguredModel> {
            check(json.has("model")) { "Blockstate variant has no model" }
            return arrayOf(ConfiguredModel(json))
        }
    }
    companion object { fun builder() = Builder() }
}

class VariantBuilder(private val block: Block, private val root: JsonObject) {
    private fun <T : Comparable<T>> propertyName(state: BlockState, property: Property<T>): String =
        property.name + "=" + property.getName(state.getValue(property))
    fun forAllStates(models: (BlockState) -> Array<ConfiguredModel>) = apply {
        check(!root.has("multipart")) { "Cannot mix variant and multipart blockstates" }
        root.add("variants", JsonObject().apply {
            block.stateDefinition.possibleStates.forEach { state ->
                val key = state.properties.sortedBy { it.name }.joinToString(",") { propertyName(state, it) }
                val variants = models(state)
                require(variants.isNotEmpty()) { "No model for $state" }
                add(key, if (variants.size == 1) variants.single().toJson() else JsonArray().apply { variants.forEach { add(it.toJson()) } })
            }
        })
    }
}

class MultipartBuilder(private val root: JsonObject) {
    private val parts = JsonArray().also {
        check(!root.has("variants") && !root.has("multipart")) { "Duplicate blockstate definition" }
        root.add("multipart", it)
    }
    fun part() = Part(this)
    class Part(private val parent: MultipartBuilder) {
        private val model = ConfiguredModel.builder()
        private val conditions = JsonObject()
        private var configured: ConfiguredModel? = null
        fun modelFile(file: ModelFile) = apply { model.modelFile(file) }
        fun rotationX(degrees: Int) = apply { model.rotationX(degrees) }
        fun rotationY(degrees: Int) = apply { model.rotationY(degrees) }
        fun addModel() = apply { check(configured == null); configured = model.build().single() }
        fun <T : Comparable<T>> condition(property: Property<T>, value: T) = apply { conditions.addProperty(property.name, property.getName(value)) }
        fun end(): MultipartBuilder {
            parent.parts.add(JsonObject().apply {
                if (!conditions.isEmpty) add("when", conditions)
                add("apply", checkNotNull(configured) { "Multipart part has no model" }.toJson())
            })
            return parent
        }
    }
}

abstract class JsonResourceProvider(private val output: PackOutput, protected val namespace: String) : DataProvider {
    protected val blockModels = ModelCollection(namespace, "block")
    protected val itemModels = ModelCollection(namespace, "item")
    protected val blockStates = linkedMapOf<Identifier, JsonObject>()
    private val staticItemModels = linkedMapOf<Identifier, ModelFile>()
    protected fun existingItemModel(item: Item, model: Identifier) {
        staticItemModels[BuiltInRegistries.ITEM.getKey(item)] = ModelFile(model)
    }
    protected abstract fun generate()
    fun modLoc(path: String): Identifier = Identifier.fromNamespaceAndPath(namespace, path)
    fun mcLoc(path: String): Identifier = Identifier.withDefaultNamespace(path)
    override fun run(cache: CachedOutput): CompletableFuture<*> {
        blockModels.entries.clear()
        itemModels.entries.clear()
        blockStates.clear()
        staticItemModels.clear()
        generate()
        val writes = mutableListOf<CompletableFuture<*>>()
        val modelPaths = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "models")
        val statePaths = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "blockstates")
        val itemPaths = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "items")
        (blockModels.entries + itemModels.entries).forEach { (id, model) ->
            writes.add(DataProvider.saveStable(cache, model.toJson(), modelPaths.json(id)))
            if (id.path == "item/manual_mill") {
                val body = model.toJson().deepCopy().apply {
                    addProperty("parent", "$namespace:block/manual_mill")
                }
                writes.add(DataProvider.saveStable(cache, body, modelPaths.json(id.withPath("item/manual_mill_body"))))
            }
        }
        blockStates.forEach { (id, json) -> writes.add(DataProvider.saveStable(cache, json, statePaths.json(id))) }
        itemModels.entries.forEach { (id, model) ->
            val itemId = id.withPath(id.path.removePrefix("item/"))
            if (BuiltInRegistries.ITEM.containsKey(itemId)) {
                val definition = JsonObject().apply { add("model", itemDefinition(model)) }
                writes.add(DataProvider.saveStable(cache, definition, itemPaths.json(itemId)))
            }
        }
        staticItemModels.forEach { (id, model) ->
            writes.add(DataProvider.saveStable(cache, JsonObject().apply { add("model", reference(model)) }, itemPaths.json(id)))
        }
        return CompletableFuture.allOf(*writes.toTypedArray())
    }

    private fun reference(model: ModelFile) = JsonObject().apply {
        addProperty("type", "minecraft:model")
        addProperty("model", model.location.toString())
        val path = model.location.path.removePrefix("item/")
        if (path in setOf("heating_coil", "sun_crystal", "unstable_ingot", "paintbrush") || path.endsWith("_drum") || path.contains("lapis_caelestis")) {
            add("tints", JsonArray().apply {
                repeat(3) { index -> add(JsonObject().apply {
                    addProperty("type", "${ExcessiveUtilities.MOD_ID}:item_color")
                    addProperty("index", index)
                }) }
            })
        }
    }

    private fun itemDefinition(model: JsonModelBuilder): JsonObject {
        if (model.location.path in setOf("item/opinium_core", "item/tesseract", "item/manual_mill")) {
            fun special(gui: Boolean, baseModel: String = model.location.toString()): JsonObject = JsonObject().apply {
                addProperty("type", "minecraft:special")
                addProperty("base", baseModel)
                add("model", JsonObject().apply {
                    addProperty("type", "${ExcessiveUtilities.MOD_ID}:${model.location.path.removePrefix("item/")}")
                    if (model.location.path == "item/tesseract") addProperty("gui", gui)
                })
            }
            if (model.location.path == "item/manual_mill") {
                return JsonObject().apply {
                    addProperty("type", "minecraft:composite")
                    add("models", JsonArray().apply {
                        add(reference(ModelFile(model.location.withPath("item/manual_mill_body"))))
                        add(special(false, model.location.withPath("item/manual_mill_body").toString()))
                    })
                }
            }
            if (model.location.path != "item/tesseract") return special(false)
            return JsonObject().apply {
                addProperty("type", "minecraft:select")
                addProperty("property", "minecraft:display_context")
                add("cases", JsonArray().apply { add(JsonObject().apply {
                    addProperty("when", "gui")
                    add("model", special(true))
                }) })
                add("fallback", special(false))
            }
        }

        // Later overrides win. Each branch preserves the complete earlier selection below its threshold.
        var result = reference(model)
        for (override in model.overrides) {
            val previous = result
            var matched = reference(override.target)
            for ((predicate, threshold) in override.predicates.entries.reversed()) {
                matched = JsonObject().apply {
                    addProperty("type", "minecraft:range_dispatch")
                    addProperty("property", "${ExcessiveUtilities.MOD_ID}:item_predicate")
                    addProperty("predicate", predicate.toString())
                    add("entries", JsonArray().apply { add(JsonObject().apply {
                        addProperty("threshold", threshold)
                        add("model", matched)
                    }) })
                    add("fallback", previous)
                }
            }
            result = matched
        }
        return result
    }
}

abstract class BlockStateProvider(output: PackOutput, namespace: String) : JsonResourceProvider(output, namespace) {
    protected abstract fun registerStatesAndModels()
    final override fun generate() = registerStatesAndModels()
    override fun getName(): String = "$namespace block models and states"
    fun models(): ModelCollection = blockModels
    fun itemModels(): ModelCollection = itemModels
    fun getVariantBuilder(block: Block) = VariantBuilder(block, blockStates.getOrPut(BuiltInRegistries.BLOCK.getKey(block)) { JsonObject() })
    fun getMultipartBuilder(block: Block) = MultipartBuilder(blockStates.getOrPut(BuiltInRegistries.BLOCK.getKey(block)) { JsonObject() })
    fun cubeAll(block: Block) = models().cubeAll(BuiltInRegistries.BLOCK.getKey(block).path, BuiltInRegistries.BLOCK.getKey(block).withPrefix("block/"))
    fun simpleBlock(block: Block, model: ModelFile) {
        val id = BuiltInRegistries.BLOCK.getKey(block)
        check(!blockStates.containsKey(id)) { "Duplicate blockstate: $id" }
        blockStates[id] = JsonObject().apply {
            add("variants", JsonObject().apply { add("", ConfiguredModel.builder().modelFile(model).build().single().toJson()) })
        }
    }
    fun simpleBlockItem(block: Block, model: ModelFile) { itemModels.getBuilder(BuiltInRegistries.BLOCK.getKey(block).path).parent(model) }
    fun simpleBlockWithItem(block: Block, model: ModelFile) { simpleBlock(block, model); simpleBlockItem(block, model) }
    fun directionalBlock(block: Block, model: ModelFile) {
        getVariantBuilder(block).forAllStates { state ->
            val facing = state.getValue(BlockStateProperties.FACING)
            ConfiguredModel.builder().modelFile(model)
                .rotationX(when (facing) { Direction.DOWN -> 180; Direction.UP -> 0; else -> 90 })
                .rotationY(when (facing) { Direction.EAST -> 90; Direction.SOUTH -> 180; Direction.WEST -> 270; else -> 0 }).build()
        }
    }
}

abstract class ItemModelProvider(output: PackOutput, namespace: String) : JsonResourceProvider(output, namespace) {
    protected abstract fun registerModels()
    final override fun generate() = registerModels()
    override fun getName(): String = "$namespace item models and definitions"
    fun getBuilder(name: String): JsonModelBuilder = itemModels.getBuilder(name)
    fun withExistingParent(name: String, parent: Identifier) = itemModels.withExistingParent(name, parent)
    fun basicItem(item: Item) = item(item, "generated")
    fun handheldItem(item: Item) = item(item, "handheld")
    private fun item(item: Item, parent: String): JsonModelBuilder {
        val id = BuiltInRegistries.ITEM.getKey(item)
        return withExistingParent(id.toString(), mcLoc("item/$parent")).texture("layer0", id.withPrefix("item/"))
    }
}
