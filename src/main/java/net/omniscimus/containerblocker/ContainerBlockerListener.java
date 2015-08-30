package net.omniscimus.containerblocker;

import org.bukkit.entity.HumanEntity;
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

		HumanEntity clicker = e.getWhoClicked();
		if(clicker.hasPermission("containerblocker.override")) return;

		ItemStack relevantItem;
		
		int hotbarButton = e.getHotbarButton();
		if(hotbarButton != -1) {
			// This InventoryClickEvent was caused by a HotbarButton.
			// Get the item on the location of that hotbarbutton, if the item on that spot is blocked, cancel.
			ItemStack hotbarButtonItemStack = clicker.getInventory().getItem(hotbarButton);
			if(hotbarButtonItemStack != null) relevantItem = hotbarButtonItemStack;
			else if(e.getCurrentItem() != null) {
				// The player is trying to grab something out of a container, get that item.
				relevantItem = e.getCurrentItem();
			}
			else return;
		}
		else {
			
			ItemStack currentItem = e.getCurrentItem();
			ItemStack cursorItem = e.getCursor();
			
			if(currentItem != null) {
				relevantItem = currentItem;
			}
			else if(cursorItem != null) {
				relevantItem = cursorItem;
			}
			else return;

		}
		
		if(clicker.hasPermission("containerblocker.override." + relevantItem.getType().name())) return;
		
		if(plugin.getBlockMode()) {
			// All items are being blocked except those in config.yml
			// Return if the current item is on the list of allowed items
			if(!plugin.itemIsOnList(relevantItem)) return;
		}
		else {
			// Items in config.yml are being blocked
			// Return if the current item isn't on the list of blocked items
			if(plugin.itemIsOnList(relevantItem)) return;
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
