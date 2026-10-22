package dev.aaronhowser.mods.excessive_utilities.recipe.base

import net.minecraft.server.level.ServerLevel
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.item.crafting.RecipeManager
import net.minecraft.world.item.crafting.RecipeMap
import net.minecraft.world.level.Level
import java.util.WeakHashMap

/** Full machine recipe content, synchronized with NeoForge's recipe content channel. */
object ModRecipes {
    var clientRecipes: RecipeMap = RecipeMap.EMPTY
    private data class CachedRecipes(val source: Collection<RecipeHolder<*>>, val recipes: RecipeMap)
    private val serverRecipes = WeakHashMap<RecipeManager, CachedRecipes>()

    fun recipes(level: Level): RecipeMap {
        if (level !is ServerLevel) return clientRecipes
        val manager = level.server.recipeManager
        val source = manager.recipes
        return synchronized(serverRecipes) {
            val cached = serverRecipes[manager]
            if (cached != null && cached.source === source) cached.recipes else {
                val recipes = RecipeMap.create(source)
                serverRecipes[manager] = CachedRecipes(source, recipes)
                recipes
            }
        }
    }
}
