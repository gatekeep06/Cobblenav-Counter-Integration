package com.metacontent.cobblenav_counter_integration.networking;

import com.metacontent.cobblenav.client.screen.pokenav.FinderScreen;
import com.metacontent.cobblenav_counter_integration.CobblenavCounterIntegration;
import com.metacontent.cobblenav_counter_integration.util.StreakReceiver;
import kotlin.Pair;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import us.timinc.mc.cobblemon.counter.api.CaptureApi;

public class CounterIntegrationPackets {
    public static final Identifier STREAK_PACKET_SERVER = new Identifier(CobblenavCounterIntegration.ID, "streak_packet_server");
    public static final Identifier STREAK_PACKET_CLIENT = new Identifier(CobblenavCounterIntegration.ID, "streak_packet_client");

    public static void registerC2SPackets() {
        ServerPlayNetworking.registerGlobalReceiver(STREAK_PACKET_SERVER, (server, player, handler, buf, responseSender) -> {
            server.execute(() -> {
                Pair<String, Integer> streak = CaptureApi.INSTANCE.getStreak(player);
                PacketByteBuf responseBuf = PacketByteBufs.create();
                responseBuf.writeString(streak.component1());
                responseBuf.writeInt(streak.component2());
                responseSender.sendPacket(STREAK_PACKET_CLIENT, responseBuf);
            });
        });
    }

    public static void registerS2CPackets() {
        ClientPlayNetworking.registerGlobalReceiver(STREAK_PACKET_CLIENT, (client, handler, buf, responseSender) -> {
            String s = buf.readString();
            int i = buf.readInt();
            if (client.currentScreen instanceof StreakReceiver streakReceiver) {
                streakReceiver.cobblenavCounterIntegration$receiveStreak(s, i);
            }
        });
    }
}
