package net.runicrituals.logic.runes.form;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.runicrituals.registries.blocks.ritual_anchor.RitualAnchor;
import net.runicrituals.registries.blocks.rune_slate.Runeslate;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static net.runicrituals.logic.Util.*;

/**
 * Cubic form
 * this class is extremely basic, as it is default behavior of FormRune.
 */
public class Prism extends FormRune {

    @Override
    public double applyEfficiencyToCost(double cost) {
        return cost * efficiency();
    }

    @Override
    public String name() {
        return "Prism";
    }
}
