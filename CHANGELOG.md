# 1.7.0

### Added

- The Depths dimension!
	- AKA the Deep Dark, but renamed
	- Uses Deepslate instead of Stone
	- Vanilla ores spawn more densely
	- Added some vanilla Deep Dark stuff to it as well
		- Veins of Sculk permeate the dimension
		- Ancient Cities can spawn rarely
- Kikoku
	- Deals 2 Soul Rending
		- Soul Rending is a new attribute that lowers the max health of anything you attack
		- Currently only used by the Kikoku, but since it's an Attribute, anything can use it
		- The Soul Rent attribute modifier on max health decreases over time
- The Tesseract
	- Replacing the Klein Bottle
	- The Klein Bottle was RWTema flexing his modeling abilities. I can't model, but I _can_ do fancy math

### Changed

- When a Lasso is destroyed, it will spawn the held mob
- Added recipes for the Creative Builder's Wand and Creative Destruction Wand
- The Sound Muffler now prevents vibration events in a configurable radius
	- This means they protect you from the Warden and Sculk Sensors!
- Unstable Ingot recipes can now require if the ingot is stable or not
	- The Unstable Ingot Block and the two Creative Wands require it be stable
- Division Sigil recipes can now require if the Sigil is inverted or not
	- Unstable Nuggets require an inverted Sigil
- Which Angel Ring wing you have is now a Data Attachment
	- It was previously a packet that modified a static `Map<UUID, Type>` which is gross
- Items with the `#excessive_utilities:not_yet_implemented` now say it in the tooltip that they're NYI

### Fixed

- Lassos only try to pick up `Mob`s. Previously it would try to pick up any `LivingEntity`, including players
- Fixed a crash when looking at a Lasso's tooltip if the held mob does NOT have a custom name
- The Ender Quarry's beam renders while the EQ itself is off-screen now

# 1.6.0

### Changed

- Accepted a Simplified Chinese translation from KronosXup
- Implemented the Inversion Ritual for the Division Sigil
- Code refactors to the Division Sigil activation as well
	- It only activates the Sigil of the person that killed the mob
- Overhauled Cursed Earth
	- Massive improvements to the code
	- Lights with Soul Fire instead of Fire
	- Lights on fire when exposed to sunlight (configurable)
	- Mobs now spawn as Cursed
		- Configurable boosts to damage and speed
		- They also render as near fully dark
		- Also added a command to set an entity to be Cursed or not
- Mobs spawned by the Resturbed Mob Spawner also now spawn Cursed
- Transfer Nodes now cache their shape, since building it was very slow
- Hopefully somewhat improved performance of the Quantum Quarry

### Fixed

- Fixed a crash where the Heating Coil would read the config before it's loaded (#30)
- Fixed the Reversing Hoe's conversion datamap not being actually registered
- Lassos now use `#c:capturing_not_supported` instead of a custom tag

# 1.5.0

### Changed

- Crusher recipes now have more options:
	- `ticks` for how many ticks the recipe takes (default 200)
	- `fe_per_tick` for how much FE the recipe consumes per tick (default 20)
- Enchanter recipes now use a SizedIngredient instead of an Ingredient and count
- Enchanter recipes also now have a required amount of Enchanting Power from nearby bookshelves
	- Bookshelves go in the same places they would for a vanilla Enchanting Table
	- All default recipes require an enchanting power of 15
		- Equivalent to 15 Bookshelves

### Fixed

- Fixed menus not having shift-click functionality
	- Please report any that are incorrect!
- Fixed the Crusher not having a menu (#25)
- Drums now properly display the color of their held fluid

# 1.4.0

### Changed

- Accepted a Japanese translation from taromaru6251!
- Accepted a PR from Saereth that significantly improves the functionality of Unstable Ingot explosions (#19)
- Accepted a PR from Saereth that gives Generators a FluidBar that shows how much fluid they have (#20, #21)
- The Golden and Cursed Lassos can no longer pick up mobs that are owned by other players
- The Lassos now tell you why they fail

### Fixed

- Accepted a PR from Saereth that fixes the Enchantment Generator not working (#14, #18)
- Fixed the later tiers of Opinium Core being super broken in JEI (#23)
- Added `doggytalents:dog` to the Lasso blacklist (#24)
- Lassos now say what entity is held in them (#22)
- Fixed Magical Snow Globe recipes requiring that all the biomes be visited instead of just 7 (#15)

# 1.3.1

### Fixed

- Added recipes for Swirling Glass and the Portal to the Last Millennium

# 1.3.0

### Changed

- The Ender Quarry can now mine block entities, picking up their contents (#10)
- Added the following to the block tag `#excessive_utilities:ender_quarry_blacklist` (#10)
	- Water
	- Lava
	- Grass Blocks
	- Tall/Short Grass
	- `#sand`
	- `#logs`
	- `#flowers`
	- `#replaceable_by_trees`
- Added a failsafe for leaving The Last Millennium if you don't have the return data in your persistent data

### Fixed

- Colored block recipes now output 7 instead of 1, to match the amount in the input (#9)
- Fixed the Ender Quarry placing a Dirt block where it starts (#10)
- Fixed the Cursed Lasso and Golden Lasso effectively being swapped in functionality (#12)

# 1.2.1

### Fixed

- Increased the min Aaron requirement

# 1.2.0

### Added

- Energy Transfer/Retrieval Nodes
- The Last Millennium

### Changed

- Demon Ingots etc now have a JEI info page about how to craft them
- Division Sigils can now be found in chests

# 1.1.1

### Changed

- Made Aaron's max version not explicit

# 1.1.0

### Added

- Athena-compatible blocks now have a tooltip if you don't have it installed (which can be disabled in the client config)

### Changed

- The Creative Harvest now displays what it's mimicking (#5)
- Redstone Lantern
	- No longer a variable-strength light source like I thought it was supposed to be, now it emits a variable-strength redstone signal (#7)
	- Updated model so it has the visible numbers (but it needs a new texture that doesn't look like a Lamp)
	- It makes a sound when it cycles
- New textures for the Generators and colored blocks
- Updated many recipes to use data component predicates
- Magical Snow Globe
	- Improved tooltip (#5)
	- Now it only requires visiting 7 of the biomes instead of all
- The Magnum Torch now gives off light (#4)
- Fixed the color of the Nether Star Generator's particles from white to "bistre"
- Generators can no longer accept energy from outside sources
- Generators now push energy into adjacent blocks
- When holding a Chandelier or Magnum Torch, it will render a box showing the area of effect

### Fixed

- Bedrockium Drum localized (#5)
- Ender Collector wasn't saving to NBT, and therefore also wasn't syncing its radius to client
- Fixed Quantum Quarry recipe requiring a Machine Block instead of a Magical Snow Globe

# 1.0.0

Initial release! Expect bugs, please report them at [the issue tracker](https://github.com/Berry-Club/Excessive-Utilities/issues)