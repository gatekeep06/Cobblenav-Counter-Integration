package com.metacontent.cobblenav_counter_integration.event;

import com.cobblemon.mod.common.api.Priority;
import com.cobblemon.mod.common.api.events.CobblemonEvents;
import com.cobblemon.mod.common.api.events.entity.SpawnEvent;
import com.cobblemon.mod.common.api.moves.BenchedMove;
import com.cobblemon.mod.common.api.moves.MoveTemplate;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.metacontent.cobblenav_counter_integration.config.CounterIntegrationConfig;
import kotlin.Pair;
import kotlin.Unit;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.random.Random;
import us.timinc.mc.cobblemon.counter.api.CaptureApi;

import java.util.ArrayList;
import java.util.List;

public class EggMoveGiver {

    private static Unit possiblyGiveEggMove(SpawnEvent<PokemonEntity> event) {
        Entity entity = event.getCtx().getCause().getEntity();
        Pokemon pokemon = event.getEntity().getPokemon();
        String pokemonName = pokemon.getSpecies().showdownId();
        if (entity instanceof PlayerEntity player) {
            Pair<String, Integer> streak = CaptureApi.INSTANCE.getStreak(player);
            if (streak.component1().equals(pokemonName)) {
                int chance = getChance(streak);
                Random random = player.getWorld().getRandom();
                int randomInt = random.nextBetween(1, 100);
                if (randomInt <= chance) {
                    List<MoveTemplate> eggMoves = pokemon.getForm().getMoves().getEggMoves();
                    List<MoveTemplate> levelupMoves = new ArrayList<>();
                    pokemon.getForm().getMoves().getLevelUpMoves().values().forEach(levelupMoves::addAll);
                    eggMoves.removeIf(levelupMoves::contains);
                    if (!eggMoves.isEmpty()) {
                        MoveTemplate moveTemplate = eggMoves.get(random.nextBetween(0, eggMoves.size() - 1));
                        pokemon.getBenchedMoves().add(new BenchedMove(moveTemplate, 0));
                    }
                }
            }
        }
        return Unit.INSTANCE;
    }

    private static int getChance(Pair<String, Integer> streak) {
        int value = streak.component2();
        int chance = CounterIntegrationConfig.levelZeroEggMoveChance;
        if (value >= CounterIntegrationConfig.levelOneStreak) {
            if (value < CounterIntegrationConfig.levelTwoStreak) {
                chance = CounterIntegrationConfig.levelOneEggMoveChance;
            }
            else if (value < CounterIntegrationConfig.levelThreeStreak) {
                chance = CounterIntegrationConfig.levelTwoEggMoveChance;
            }
            else if (value < CounterIntegrationConfig.levelFourStreak) {
                chance = CounterIntegrationConfig.levelThreeEggMoveChance;
            }
            else {
                chance = CounterIntegrationConfig.levelFourEggMoveChance;
            }
        }
        return chance;
    }

    public static void subscribe() {
        CobblemonEvents.POKEMON_ENTITY_SPAWN.subscribe(Priority.NORMAL, EggMoveGiver::possiblyGiveEggMove);
    }
}
