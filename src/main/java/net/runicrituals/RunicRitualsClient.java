package net.runicrituals;

import net.fabricmc.api.ClientModInitializer;
import net.runicrituals.registries.RunicRitualsBlockEntityRenderer;

public class RunicRitualsClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        new RunicRitualsBlockEntityRenderer().onInitializeClient();
    }
}
