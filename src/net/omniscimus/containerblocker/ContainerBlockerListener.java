package net.omniscimus.containerblocker;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
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
	public void onFurnace(InventoryClickEvent e) {

		if(e.getWhoClicked().hasPermission("containerblocker.override")) return;

		ItemStack currentItem = e.getCurrentItem();
		ItemStack cursorItem = e.getCursor();

		if(plugin.getBlockMode()) {
			// All items are being blocked except those in config.yml
			// Return if the current item isn't on the list of blocked items
			if(!plugin.itemsAreOnList(currentItem, cursorItem)) return;
		}
		else {
			// Items in config.yml are being blocked
			// Return if the current item is on the list of allowed items
			if(plugin.itemsAreOnList(currentItem, cursorItem)) return;
		}

		if(.contains(e.getInventory().getType())

				// Else, block the attempt.
				e.setCancelled(true);
		return;

	}

}