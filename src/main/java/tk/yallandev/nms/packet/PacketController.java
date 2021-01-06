package tk.yallandev.nms.packet;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;

import tk.yallandev.nms.event.packet.PacketReceiveEvent;
import tk.yallandev.nms.event.packet.PacketSendEvent;
import tk.yallandev.nms.packet.wrapper.AbstractPacket;
import tk.yallandev.nms.packet.wrapper.WrapperPlayServerEntityDestroy;
import tk.yallandev.nms.packet.wrapper.WrapperPlayServerEntityMetadata;
import tk.yallandev.nms.packet.wrapper.WrapperPlayServerNamedEntitySpawn;
import tk.yallandev.nms.packet.wrapper.WrapperPlayServerRelEntityMove;
import tk.yallandev.nms.packet.wrapper.WrapperPlayServerSpawnEntity;
import tk.yallandev.nms.packet.wrapper.WrapperPlayServerSpawnEntityExperienceOrb;
import tk.yallandev.nms.packet.wrapper.WrapperPlayServerSpawnEntityLiving;
import tk.yallandev.nms.packet.wrapper.WrapperPlayServerSpawnPosition;

/**
 * 
 * Class to help creation of packet
 * 
 * @author yandv
 *
 */

public class PacketController {

	private Map<PacketType, Class<? extends AbstractPacket>> packetMap;
	private List<PacketHandler> handlerList;

	private boolean bukkitEvent;

	public PacketController(JavaPlugin javaPlugin) {

		packetMap = new HashMap<>();
		handlerList = new ArrayList<>();

		List<Class<? extends AbstractPacket>> packetList = new ArrayList<>();

		packetList.add(WrapperPlayServerEntityDestroy.class);
		packetList.add(WrapperPlayServerEntityMetadata.class);
		packetList.add(WrapperPlayServerNamedEntitySpawn.class);
		packetList.add(WrapperPlayServerRelEntityMove.class);
		packetList.add(WrapperPlayServerSpawnEntity.class);
		packetList.add(WrapperPlayServerSpawnEntityExperienceOrb.class);
		packetList.add(WrapperPlayServerSpawnEntityLiving.class);
		packetList.add(WrapperPlayServerSpawnPosition.class);

		List<PacketType> packetTypes = new ArrayList<>();

		for (Class<? extends AbstractPacket> abstractPacket : packetList) {

			PacketType type = null;

			try {
				Field field = abstractPacket.getField("TYPE");
				field.setAccessible(true);
				type = (PacketType) field.get(null);
			} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}

			if (type != null) {
				packetMap.put(type, abstractPacket);
				packetTypes.add(type);
			}

		}

		ProtocolLibrary.getProtocolManager().addPacketListener(
				new PacketAdapter(javaPlugin, packetTypes.toArray(new PacketType[packetTypes.size()])) {

					@Override
					public void onPacketReceiving(PacketEvent event) {
						if (packetMap.containsKey(event.getPacketType())) {
							try {
								AbstractPacket abstractPacket = packetMap.get(event.getPacketType())
										.getConstructor(PacketContainer.class).newInstance(event.getPacket());

								event.setCancelled(handleReceive(abstractPacket, event.getPacketType(), event));
								event.setPacket(abstractPacket.getHandle());

							} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
									| InvocationTargetException | NoSuchMethodException | SecurityException e) {
								e.printStackTrace();
							}
						}
					}

					@Override
					public void onPacketSending(PacketEvent event) {
						if (packetMap.containsKey(event.getPacketType())) {
							try {
								AbstractPacket abstractPacket = packetMap.get(event.getPacketType())
										.getConstructor(PacketContainer.class).newInstance(event.getPacket());

								event.setCancelled(handleSend(abstractPacket, event.getPacketType(), event));
								event.setPacket(abstractPacket.getHandle());
							} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
									| InvocationTargetException | NoSuchMethodException | SecurityException e) {
								e.printStackTrace();
							}
						}
					}

				});
	}

	public boolean handleReceive(AbstractPacket abstractPacket, PacketType packetType, PacketEvent packetEvent) {
		boolean cancelled = false;

		if (bukkitEvent) {
			PacketReceiveEvent event = new PacketReceiveEvent(abstractPacket, packetType, packetEvent);
			Bukkit.getPluginManager().callEvent(event);
			cancelled = event.isCancelled();
		}

		for (PacketHandler handler : handlerList) {
			if (handler.onPacketReceive(abstractPacket, packetType, packetEvent)) {
				cancelled = true;
			}
		}

		return cancelled;
	}

	public boolean handleSend(AbstractPacket abstractPacket, PacketType packetType, PacketEvent packetEvent) {
		boolean cancelled = false;

		if (bukkitEvent) {
			PacketSendEvent event = new PacketSendEvent(abstractPacket, packetType, packetEvent);
			Bukkit.getPluginManager().callEvent(event);
			cancelled = event.isCancelled();
		}

		for (PacketHandler handler : handlerList) {
			if (handler.onPacketSend(abstractPacket, packetType, packetEvent)) {
				cancelled = true;
			}
		}

		return cancelled;
	}

	public void registerHandler(PacketHandler packetHandler) {
		handlerList.add(packetHandler);
	}

}
