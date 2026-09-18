# Runic Rituals

## About

Runic Rituals is a Minecraft 26.2 Fabric mod that adds a magic system for conducting rituals. the system is outlined in detail below, but in short,
Runic Rituals adds an engravable runeslate block that when placed in the world can be used to generate effects. 

Runic Rituals focuses on a more solid, in-world magic system as opposed to more inventory or UI based systems. 
this means if you want an item or ability, you have to physically build a structure in the world to do so, keep it powered, etc. in order to have that item/ability/effect.

Lastly, Runic Rituals generally tries not to add "complete" effects, and lets those be constructed by the player with the pieces it applies. 
for example, an a spawner block or direct "spawn" effect wouldn't be added.

Instead players would be given pieces to create an effect they want. Start with a cobblestone generator, feeding a ![manifest action rune](src/main/resources/assets/runic-rituals/textures/block/manifest_action_rune.png)![thermal element rune](src/main/resources/assets/runic-rituals/textures/block/thermal_rune.png) (Manifest, Thermal) rune effect to produce lava, that is then used by an [animate]![thermal element rune](src/main/resources/assets/runic-rituals/textures/block/thermal_rune.png) (Animate, Thermal) rune to turn that lava into fire elementals. 
*may* be a viable sequence for the mod. (given the mana generation to power it)

This gives players more room for creativity, keeps the asthetics and tone of the mod, and pushes players to find material/item pipelines to produce what they want, including from usage of other mods as applicable.

## Ritual Structure
### Components
Rituals have 3 _required_ components. 
- Form
- Action
- Element

the form will define the area a ritual applies to, while the combination of action and element defines the effect that happens in that area. 

From there however, there are a number of *optional* components that can be added to this structure in order to modify it. 
these will change things like where the effect happens, when it happens, fine tune the size and shape of the effect. and more. 

A Casting Block is a set of required components to create an effect, along with any optional ones that modify it. the Block will 
always start with the form, and is followed by 1 or more iterations of actions followed by the runes that use that action.

So we might have: 
![prism form rune](src/main/resources/assets/runic-rituals/textures/block/prism_form_rune.png)![manifest action rune](src/main/resources/assets/runic-rituals/textures/block/manifest_action_rune.png)![kinetic element rune](src/main/resources/assets/runic-rituals/textures/block/kinetic_rune.png) 
(Prism, Manifest, Kinetic) which would make any entities faster in the cubic area. (so long as it is powered with mana)

but we could also have a casting block like: ![prism form rune](src/main/resources/assets/runic-rituals/textures/block/prism_form_rune.png)![sacrifice action rune](src/main/resources/assets/runic-rituals/textures/block/sacrifice_action_rune.png)![kinetic element rune](src/main/resources/assets/runic-rituals/textures/block/kinetic_rune.png)![manifest action rune](src/main/resources/assets/runic-rituals/textures/block/manifest_action_rune.png)![electric element rune](src/main/resources/assets/runic-rituals/textures/block/electric_rune.png)
(Prism, Sacrifice, Kinetic, Manifest, Electric)
which would slow down entities, and hit them with lightning.

### Casting Blocks
You can have multiple casting blocks in the same ritual, with potentially different forms. It is worth noting however, 
that casting blocks are applied as a single effect with a single cost. thus, while the above kinetic/electric block generates mana off of the kinetic sacrifice, 
and spends it on lightning, it has a positive total cost, and will never cast all other things disregarded. 

However, if we instead split it into two casting blocks: 

![prism form rune](src/main/resources/assets/runic-rituals/textures/block/prism_form_rune.png)![sacrifice action rune](src/main/resources/assets/runic-rituals/textures/block/sacrifice_action_rune.png)![kinetic element rune](src/main/resources/assets/runic-rituals/textures/block/kinetic_rune.png) | ![prism form rune](src/main/resources/assets/runic-rituals/textures/block/prism_form_rune.png)![manifest action rune](src/main/resources/assets/runic-rituals/textures/block/manifest_action_rune.png)![electric element rune](src/main/resources/assets/runic-rituals/textures/block/electric_rune.png)

then they will cast separately, and the kinetic sacrifice block will create mana until enough exists to summon lightning, at which point *that* block will cast and do so.

### Mana
as a general rule (though not universal) ![sacrifice action rune](src/main/resources/assets/runic-rituals/textures/block/sacrifice_action_rune.png)[element] casting blocks will generate mana, while ![manifest action rune](src/main/resources/assets/runic-rituals/textures/block/manifest_action_rune.png)[element] casting blocks will spend it. 

Casting blocks require two mana conditions in order to do anything. 
1. the block's total mana cost must be less than or equal to the stored mana in the ritual. 
2. if the block *generates* mana, then the mana generation must fit within the storage remaining

once these are met, the casting block will apply it's effect, and generate/spend the appropriate amount of mana from the ritual's pool

## Runes
### Elemental
Elemental Runes govern the actual effect(s) a ritual creates. the exact effect depends on the Action rune that applies to the action rune. Manifest/Sacrifice typically are inversions of each other, while others (like the planned Animate) may have other effects. 

| Rune Name                       | Arcane                                                                                         | Kinetic                                                                                          | Thermal                                                                                          | Electric                                                                                           | Light                                                                                        | Matter                                                                                         | Space<br/>(Unimplemented)                                                                    | Time                                                                                                                                       | Life<br/>(Unimplemented) |
|---------------------------------|------------------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------|------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------|--------------------------|
| Symbol                          | ![arcane element rune](src/main/resources/assets/runic-rituals/textures/block/arcane_rune.png) | ![kinetic element rune](src/main/resources/assets/runic-rituals/textures/block/kinetic_rune.png) | ![thermal element rune](src/main/resources/assets/runic-rituals/textures/block/thermal_rune.png) | ![electric element rune](src/main/resources/assets/runic-rituals/textures/block/electric_rune.png) | ![light element rune](src/main/resources/assets/runic-rituals/textures/block/light_rune.png) | ![matter element rune](src/main/resources/assets/runic-rituals/textures/block/matter_rune.png) | ![space element rune](src/main/resources/assets/runic-rituals/textures/block/space_rune.png) | ![time element rune](src/main/resources/assets/runic-rituals/textures/block/time_rune.png)                                                 |                          |
| Manifest <br/> Effect           | Efficiency + 1                                                                                 | Increase Entity <br/>Movement Speed                                                              | Melt Ice<br/>(blue->packed->ice->water)<br/>set mobs/players on fire                             | Summons Lightning Bolts                                                                            | Creates Decaying Light Blocks                                                                | Spawns Terrain Blocks                                                                          | Creates a pocket dimension                                                                   | Speeds up Time<br/>- Block Tick rate<br/>- Entity Tick Rate<br/>- random ticks<br/>- scheduled ticks<br/> for blocks, entities and players | Heal living entities     | 
| Sacrifice<br/>Effect            | Efficiency / 2                                                                                 | Decrease Entity <br/>Movement Speed                                                              | freeze ice <br/>(water->ice->packed->blue)<br/>freeze mobs/players                               | None                                                                                               | Creates Decaying Shadow Blocks                                                               | Destroys Terrain Blocks                                                                        | Teleports to linked position                                                                 | Slows down Time<br/>(see above)                                                                                                            | Damage living entities   |
| Allowed<br/>Inlay<br/>Materials | - Amethyst Shard<br/>- Diamond                                                                 | - [None] <br/>- Iron Ingot<br/>- Netherite Scrap<br/>- Soul Sand<br/>- Breeze Rod                | - Obsidian<br/>- Copper Ingot<br/>- Blaze Rod<br/>- Blue Ice                                     | - Gold Ingot<br/>- Copper Ingot<br/>- Amethyst Shard<br/>- Redstone Dust                           | - Glass<br/> - Amethyst Shard<br/> - Diamond<br/> - Obsidian                                 | - Netherite Scrap<br/>- Obsidian                                                               | - Echo Shard<br/>- Chorus Fruit                                                              | - Echo Shard                                                                                                                               |                          |

### Actions
As mentioned in [Elemental](#Elemental), actions are a required ritual component that governs which effect the element rune applies. 

| Rune Name                       | Manifest                                                                                                 | Sacrifice                                                                                                  | Animate<br/>(Planned)                |
|---------------------------------|----------------------------------------------------------------------------------------------------------|------------------------------------------------------------------------------------------------------------|--------------------------------------|
| Symbol                          | ![manifest action rune](src/main/resources/assets/runic-rituals/textures/block/manifest_action_rune.png) | ![sacrifice action rune](src/main/resources/assets/runic-rituals/textures/block/sacrifice_action_rune.png) | ![shrink logic rune](src/main/resources/assets/runic-rituals/textures/block/animate_action_rune.png)                                     |
| Effect                          | Select Manifest<br/>Elemental Effect                                                                     | Select Sacrifice<br/>Elemental Effect,<br/>Invert mana cost <br/>(creates mana)                            | Selects Animate<br/>Elemental Effect |
| Allowed<br/>Inlay<br/>Materials | - Any Inlay Material<br/>- [None]                                                                        | - Any Inlay Material<br/>- [None]                                                                          |                                      |

### Forms 
Forms are a required ritual component that governs the area effected by a ritual. this simply defines the 3-d "shape" where the Element-Action takes place.  

| Rune Name                       | Prism                                                                                          | Sheet                                                                                          | Dome | Beam<br/>(Planned)                        |
|---------------------------------|------------------------------------------------------------------------------------------------|------------------------------------------------------------------------------------------------|---|-------------------------------------------|
| Symbol                          | ![prism form rune](src/main/resources/assets/runic-rituals/textures/block/prism_form_rune.png) | ![sheet form rune](src/main/resources/assets/runic-rituals/textures/block/sheet_form_rune.png) | ![sheet form rune](src/main/resources/assets/runic-rituals/textures/block/dome_form_rune.png) |                                           |
| Effect                          | Applies Elemental <br/> Effect on Cubic area                                                   | Applies Elemental<br/>Effect on Flat Horizontal Plane<br/>(1 block thick square)               | applies elemental effect in a "up" hemisphere | Applies Elemental Effect on directed beam |
| Allowed<br/>Inlay<br/>Materials | - Iron Ingot<br/>- Gold Ingot<br/>- Diamond<br/>- Glass                                        | - Iron Ingot<br/>- Copper Ingot<br/>- Blaze Rod<br/>- Glass                                    |  |                                          |

### Form Modifiers
Form modifiers are an optional ritual component that changes the shape a form rune applies on. for example, ![grow logic rune](src/main/resources/assets/runic-rituals/textures/block/grow_modification_rune.png)![prism form rune](src/main/resources/assets/runic-rituals/textures/block/prism_form_rune.png) (Grow, Prism) would effect a larger-than-usual cubic area.

| Rune Name                       | Grow<br/>(Unimplemented)                                                                       | Shrink<br/>(Unimplemented)                                                                         |
|---------------------------------|------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------|
| Symbol                          | ![grow logic rune](src/main/resources/assets/runic-rituals/textures/block/grow_modification_rune.png) | ![shrink logic rune](src/main/resources/assets/runic-rituals/textures/block/shrink_modification_rune.png) |
| Effect                          | Form Effects a Larger Area                                                                     | Form Effects a Smaller Area                                                                        |
| Allowed<br/>Inlay<br/>Materials | - Redstone Dust<br/>- Blaze Rod<br/>- Chorus Fruit                                             |                                                                                                    |

### Position Modifiers
Position modifiers are an optional ritual component that changes the "center" of a ritual. additionally, some of these change the behavior of the ritual to a pulse with higher intensity instead of a constant area-effect.

| Rune Name                       | Bolt<br/>(Unimplemented)                                                                       | Static<br/>(Planned)                                 | Bound<br/>(Planned)                     | Hit<br/>(Planned)            |
|---------------------------------|------------------------------------------------------------------------------------------------|------------------------------------------------------|-----------------------------------------|------------------------------|
| Symbol                          | ![bolt logic rune](src/main/resources/assets/runic-rituals/textures/block/bolt_position_rune.png) | ![shrink logic rune](src/main/resources/assets/runic-rituals/textures/block/static_position_rune.png) | ![shrink logic rune](src/main/resources/assets/runic-rituals/textures/block/bind_position_rune.png) | ![shrink logic rune](src/main/resources/assets/runic-rituals/textures/block/hit_position_rune.png)                             |
| Effect                          | Form / Ritual effect happens at hit of a projectile                                            | Form / Ritual effect happens at base ritual location | Form / Ritual happens at bound location | Effect Applies to hit Entity |
| Allowed<br/>Inlay<br/>Materials | - Breeze Rod<br/>- Blaze Rod<br/>- Chorus Fruit                                                |                                                      |                                         |                              |

### Activation Condition Modifier
These modifiers change when the effect applies. 

| Rune Name                       | Control<br/>(Unimplemented)                                                                          | Redstone<br/>(Planned)                          | Delay<br/>(Planned)                                                               |
|---------------------------------|------------------------------------------------------------------------------------------------------|-------------------------------------------------|-----------------------------------------------------------------------------------|
| Symbol                          | ![control logic rune](src/main/resources/assets/runic-rituals/textures/block/control_activation_rune.png) | ![shrink logic rune](src/main/resources/assets/runic-rituals/textures/block/redstone_condition_rune.png)                                                | ![shrink logic rune](src/main/resources/assets/runic-rituals/textures/block/delay_condition_rune.png)                                                                                  |
| Effect                          | Ritual Effect only applies when bound player/item right-clicks                                       | Ritual Effect turns on when powered by redstone | Ritual Effect happens after a 1s delay (does nothing on Static or Bound Position) |
| Allowed<br/>Inlay<br/>Materials | - Netherite Scrap<br/>- Echo Shard<br/>- Chorus Fruit                                                |                                                 |                                                                                   |

## Inlay Materials
Materials fall into efficiency tiers. more valuable materials typically fall into a more efficient/higher tier. 
not all materials can be used on all runes (noted above in [Runes](#Runes)). I have attempted to make the materials appropreate to the rune effect, while having access to a couple tiers, though tier 4 is often not craftable. 

worth noting that with commands you can /give items with a rune that has any material applied as inlay, and will act properly in the tier of that material. As such the allowed inlay materials are only a crafting restriction and can be bypassed. 

| Tier       | Tier 0            | Tier 1                                                                     | Tier 2                                                                          | Tier 3                                         | Tier 4                             |
|------------|-------------------|----------------------------------------------------------------------------|---------------------------------------------------------------------------------|------------------------------------------------|------------------------------------|
| Efficiency | 0.1               | 0.3                                                                        | 0.5                                                                             | 0.75                                           | 0.95                               |
| Materials  | Etched<br/>(None) | - Glass<br/>- Amethyst <br/>- Blue Ice<br/>- Soul Sand<br/>- Redstone Dust | - Blaze Rod<br/>- Breeze Rod<br/>- Copper Ingot<br/>- Iron Ingot<br/>- Obsidian | - Diamond <br/>- Chorus Fruit<br/>- Gold Ingot | - Echo Shard<br/>- Netherite Scrap |


## Setup

For setup instructions, please see the [Fabric Documentation page](https://docs.fabricmc.net/develop/getting-started/creating-a-project#setting-up) related to the IDE that you are using.


## License

This template is available under the CC0 license. Feel free to learn from it and incorporate it in your own projects.
