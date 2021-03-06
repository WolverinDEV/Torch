package net.minecraft.server;

import java.io.IOException;

public class PacketPlayInBlockPlace implements Packet<PacketListenerPlayIn> {

    private EnumHand a;
    public long timestamp; // Spigot

    public PacketPlayInBlockPlace() {}

    public PacketPlayInBlockPlace(EnumHand enumhand) {
        this.a = enumhand;
    }

    @Override
    public void a(PacketDataSerializer packetdataserializer) throws IOException {
        this.timestamp = System.currentTimeMillis(); // Spigot
        this.a = packetdataserializer.a(EnumHand.class);
    }

    @Override
    public void b(PacketDataSerializer packetdataserializer) throws IOException {
        packetdataserializer.a(this.a);
    }

    @Override
    public void a(PacketListenerPlayIn packetlistenerplayin) {
        packetlistenerplayin.a(this);
    }

    public EnumHand a() {
        return this.a;
    }
}
