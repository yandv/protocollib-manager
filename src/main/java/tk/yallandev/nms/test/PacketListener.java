package tk.yallandev.nms.test;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketEvent;

import tk.yallandev.nms.packet.PacketHandler;
import tk.yallandev.nms.packet.wrapper.AbstractPacket;

public class PacketListener implements PacketHandler {

	@Override
	public boolean onPacketReceive(AbstractPacket abstractPacket, PacketType packetType, PacketEvent packetEvent) {
		System.out.println("[RECEIVE] PacketType -> " + packetType + " -> ");
		return false;
	}

	@Override
	public boolean onPacketSend(AbstractPacket abstractPacket, PacketType packetType, PacketEvent packetEvent) {
		System.out.println("[SEND] PacketType -> " + packetType + " -> ");
		return false;
	}
}
