package tk.yallandev.nms.event;

import org.bukkit.event.Cancellable;

import lombok.Setter;

import lombok.Getter;

public class BukkitCancellableEvent extends BukkitEvent implements Cancellable {
	
	@Getter
	@Setter
	private boolean cancelled;
	
}
