package com.metacontent.cobblenav_counter_integration.config;

import com.metacontent.cobblenav.config.SimpleConfig;

public class CounterIntegrationConfigProvider implements SimpleConfig.DefaultConfig {
    private String content = "";

    public void addParameter(String key, Object value, String comment) {
        content += "#" + comment + " | default: " + value + "\n" + key + "=" + value + "\n";
    }

    @Override
    public String get(String namespace) {
        return content;
    }
}
