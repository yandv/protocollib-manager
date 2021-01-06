package tk.yallandev.nms;

import org.bukkit.plugin.java.JavaPlugin;

import tk.yallandev.nms.packet.PacketController;
import tk.yallandev.nms.test.PacketListener;

public class ControllerMain extends JavaPlugin {
	
	private PacketController packetController;
	
	@Override
	public void onEnable() {
		packetController = new PacketController(this);
		
		packetController.registerHandler(new PacketListener());
		super.onEnable();
	}

}
