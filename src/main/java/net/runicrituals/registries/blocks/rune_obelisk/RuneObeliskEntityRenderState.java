package net.runicrituals.registries.blocks.rune_obelisk;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;

import java.util.Collections;
import java.util.List;

@Environment(EnvType.CLIENT)
public class RuneObeliskEntityRenderState extends BlockEntityRenderState {

    boolean active = false;
    public List<ItemStackRenderState> items = Collections.emptyList();

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
