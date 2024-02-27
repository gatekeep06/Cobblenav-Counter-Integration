package com.metacontent.cobblenav_counter_integration;

import com.metacontent.cobblenav_counter_integration.config.CounterIntegrationConfig;
import com.metacontent.cobblenav_counter_integration.event.EggMoveGiver;
import com.metacontent.cobblenav_counter_integration.networking.CounterIntegrationPackets;
import net.fabricmc.api.ModInitializer;

public class CobblenavCounterIntegration implements ModInitializer {
    public static final String ID = "cobblenav_counter_integration";

    @Override
    public void onInitialize() {
        CounterIntegrationConfig.initConfig();
        CounterIntegrationPackets.registerC2SPackets();
        EggMoveGiver.subscribe();
    }
}
