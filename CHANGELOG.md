# 1.2.0

### Added

- Energy Transfer/Retrieval Nodes

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