package net.runicrituals;

import net.fabricmc.api.ClientModInitializer;
import net.runicrituals.registries.RunicRitualsBlockEntityRenderer;
import net.runicrituals.registries.client_only.RunicRitualsComponentsClient;
import net.runicrituals.registries.client_only.RunicRitualsScreens;

public class RunicRitualsClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        new RunicRitualsBlockEntityRenderer().onInitializeClient();
        RunicRitualsScreens.registerScreens();

        RunicRitualsComponentsClient.registerComponents();
    }
}
