package net.runicrituals.registries.items;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;
import net.runicrituals.data_generation.RunicRitualsBlockTagProvider;

public class RunicRitualsItem extends Item {


    public RunicRitualsItem(Properties properties) {
        super(properties);
    }

    public static class RunicRitualsItemProperties extends Item.Properties {
        public Item.Properties wand(final ToolMaterial material, final float attackDamageBaseline, final float attackSpeedBaseline) {
            return tool(material, RunicRitualsBlockTagProvider.MINEABLE_WITH_WAND, attackDamageBaseline, attackSpeedBaseline, 0.0F);
        }

        public Item.Properties staff(final ToolMaterial material, final float attackDamageBaseline, final float attackSpeedBaseline) {
            return tool(material, RunicRitualsBlockTagProvider.MINEABLE_WITH_WAND, attackDamageBaseline, attackSpeedBaseline, 0.0F);
        }
    }

}
