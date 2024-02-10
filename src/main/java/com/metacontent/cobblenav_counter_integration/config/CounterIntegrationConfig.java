package com.metacontent.cobblenav_counter_integration.config;

import com.metacontent.cobblenav.config.SimpleConfig;
import com.metacontent.cobblenav_counter_integration.CobblenavCounterIntegration;

public class CounterIntegrationConfig {
    public static int levelOneStreak;
    public static int levelTwoStreak;
    public static int levelThreeStreak;
    public static int levelFourStreak;
    public static int levelZeroEggMoveChance;
    public static int levelOneEggMoveChance;
    public static int levelTwoEggMoveChance;
    public static int levelThreeEggMoveChance;
    public static int levelFourEggMoveChance;

    public static void initConfig() {
        CounterIntegrationConfigProvider provider = new CounterIntegrationConfigProvider();
        createProvider(provider);
        SimpleConfig config = SimpleConfig.of(CobblenavCounterIntegration.ID + "config").provider(provider).request();
        assignParameters(config);
    }

    private static void createProvider(CounterIntegrationConfigProvider provider) {
        provider.addParameter("levelOneStreak", 5,
                "Integer, Determines how many pokemon you have to catch to get the first streak level");
        provider.addParameter("levelTwoStreak", 10,
                "Integer, Determines how many pokemon you have to catch to get the second streak level");
        provider.addParameter("levelThreeStreak", 20,
                "Integer, Determines how many pokemon you have to catch to get the third streak level");
        provider.addParameter("levelFourStreak", 30,
                "Integer, Determines how many pokemon you have to catch to get the fourth streak level");
        provider.addParameter("levelZeroEggMoveChance", 0,
                "Integer, Determines the chance of an egg move being given out when spawning at level zero");
        provider.addParameter("levelOneEggMoveChance", 21,
                "Integer, Determines the chance of an egg move being given out when spawning at level one");
        provider.addParameter("levelTwoEggMoveChance", 46,
                "Integer, Determines the chance of an egg move being given out when spawning at level two");
        provider.addParameter("levelThreeEggMoveChance", 58,
                "Integer, Determines the chance of an egg move being given out when spawning at level three");
        provider.addParameter("levelFourEggMoveChance", 65,
                "Integer, Determines the chance of an egg move being given out when spawning at level four");
    }

    private static void assignParameters(SimpleConfig config) {
        levelOneStreak = config.getOrDefault("levelOneStreak", 5);
        levelTwoStreak = config.getOrDefault("levelTwoStreak", 10);
        levelThreeStreak = config.getOrDefault("levelThreeStreak", 20);
        levelFourStreak = config.getOrDefault("levelFourStreak", 30);
        levelZeroEggMoveChance = config.getOrDefault("levelZeroEggMoveChance", 0);
        levelOneEggMoveChance = config.getOrDefault("levelOneEggMoveChance", 21);
        levelTwoEggMoveChance = config.getOrDefault("levelTwoEggMoveChance", 46);
        levelThreeEggMoveChance = config.getOrDefault("levelThreeEggMoveChance", 58);
        levelFourEggMoveChance = config.getOrDefault("levelFourEggMoveChance", 65);
    }
}
