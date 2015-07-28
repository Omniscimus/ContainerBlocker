package net.omniscimus.containerblocker;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

/**
 * @author Omniscimus
 */
public class ContainerBlockerListener implements Listener {

	private ContainerBlocker plugin;
	public ContainerBlockerListener(ContainerBlocker plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {

		if(e.getWhoClicked().hasPermission("containerblocker.override")) return;

		ItemStack currentItem = e.getCurrentItem();
		ItemStack cursorItem = e.getCursor();
		
		// moet ik hier currentItem of cursorItem hebben?
		if(e.getWhoClicked().hasPermission("containerblocker.override." + currentItem.getType().name())) return;

		if(plugin.getBlockMode()) {
			// All items are being blocked except those in config.yml
			// Return if the current item is on the list of allowed items
			if(!plugin.itemsAreOnList(currentItem, cursorItem)) return;
		}
		else {
			// Items in config.yml are being blocked
			// Return if the current item isn't on the list of blocked items
			if(plugin.itemsAreOnList(currentItem, cursorItem)) return;
		}
		
		/*
		 * Events that haven't been returned here yet:
		 * - All items on the list are allowed, and the clicked item is on the list
		 * - Items on the list are being blocked, and the clicked item isn't on the list
		 */
		
		// Do only stop the event if the restrictions apply to the current inventory.
		if(plugin.getInventoryList().contains(e.getInventory().getType())) {
			e.setCancelled(true);
			if(plugin.getMessage_on_block()) e.getWhoClicked().sendMessage("You can't place that item in there!");
		}

	}

}
