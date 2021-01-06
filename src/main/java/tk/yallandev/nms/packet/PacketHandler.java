package tk.yallandev.nms.packet;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketEvent;

import tk.yallandev.nms.packet.wrapper.AbstractPacket;

public interface PacketHandler {
	
	boolean onPacketReceive(AbstractPacket abstractPacket, PacketType packetType, PacketEvent packetEvent);
	
	boolean onPacketSend(AbstractPacket abstractPacket, PacketType packetType, PacketEvent packetEvent);

}
