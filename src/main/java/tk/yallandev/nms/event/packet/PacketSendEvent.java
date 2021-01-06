package tk.yallandev.nms.event.packet;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketEvent;

import lombok.AllArgsConstructor;
import lombok.Getter;
import tk.yallandev.nms.event.BukkitCancellableEvent;
import tk.yallandev.nms.packet.wrapper.AbstractPacket;

@Getter
@AllArgsConstructor
public class PacketSendEvent extends BukkitCancellableEvent {
	
	private AbstractPacket abstractPacket;
	private PacketType packetType;
	private PacketEvent packetEvent;
	
}
