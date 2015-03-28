package net.omniscimus.containerblocker;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

public class ContainerBlockerListener implements Listener {
	
	private ContainerBlocker plugin;
	
	public ContainerBlockerListener(ContainerBlocker plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onFurnace(InventoryClickEvent e) {

		// If the inventory isn't a furnace, return
		if(e.getInventory().getType() != InventoryType.FURNACE) return;

		// If the player has override perms, return
		if(e.getWhoClicked().hasPermission("jcfurnace.override")) return;

		// Return if the current item is on the list of allowed items
		for(ItemStack is : plugin.getItemList()) {
			if(is.isSimilar(e.getCurrentItem()) || is.isSimilar(e.getCursor())) return;
		}

		// Else, block the attempt.
		e.setCancelled(true);
		return;

	}
	
}