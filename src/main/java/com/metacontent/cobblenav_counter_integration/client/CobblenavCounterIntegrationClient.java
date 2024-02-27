package com.metacontent.cobblenav_counter_integration.client;

import com.metacontent.cobblenav_counter_integration.networking.CounterIntegrationPackets;
import net.fabricmc.api.ClientModInitializer;

public class CobblenavCounterIntegrationClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        CounterIntegrationPackets.registerS2CPackets();
    }
}
