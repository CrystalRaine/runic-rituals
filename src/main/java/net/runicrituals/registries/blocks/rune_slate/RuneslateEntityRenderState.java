package net.runicrituals.registries.blocks.rune_slate;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.core.Direction;
import net.runicrituals.logic.RuneSymbol;
import net.runicrituals.logic.runes.RuneType;

import java.util.Collections;
import java.util.List;

@Environment(EnvType.CLIENT)
public class RuneslateEntityRenderState extends BlockEntityRenderState {

    private boolean active = false;
    public boolean getActive() {
        return active;
    }
    public void setActive(boolean active) {
        this.active = active;
    }

    private RuneSymbol runeSymbol = null;
    public void setRuneSymbol(RuneSymbol symbol) {
        this.runeSymbol = symbol;
    }
    public RuneSymbol getRuneSymbol() {
        return runeSymbol;
    }

    private Direction facing = Direction.NORTH;
    public void setFacing(Direction direction) {
        this.facing = direction;
    }
    public Direction getFacing(){
        return facing;
    }
}
