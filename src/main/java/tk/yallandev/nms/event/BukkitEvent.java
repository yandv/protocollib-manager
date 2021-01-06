package tk.yallandev.nms.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class BukkitEvent extends Event {
	
	public static final HandlerList HANDLER = new HandlerList();

	@Override
	public HandlerList getHandlers() {
		return HANDLER;
	}

}
