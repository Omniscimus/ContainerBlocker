package net.omniscimus.containerblocker;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author Omniscimus
 */
public class ContainerBlocker extends JavaPlugin {

	//-----------------------//
	//        Classes        //
	//-----------------------//
	
	private ContainerBlockerListener listener;
	private ContainerBlockerCommandExecutor commandExecutor;

	//-----------------------//
	//     Useful stuff      //
	//-----------------------//
	
	private FileConfiguration config;
	private Logger logger;
	
	//-----------------------//
	//   config.yml values   //
	//-----------------------//
	
	private boolean blockMode;
	/** @return true if all items should be blocked except the listed ones, false if only the listed items should be blocked */
	public boolean getBlockMode() {
		return blockMode;
	}
	
	private List<InventoryType> inventoryList;
	public List<InventoryType> getInventoryList() {
		return inventoryList;
	}
	
	private List<ItemStack> itemList;
	public List<ItemStack> getItemList() {
		return itemList;
	}
	
	/** @return true if one of this item is on the list */
	public boolean itemIsOnList(ItemStack item) {
		for(ItemStack is : getItemList()) {
			if(is.isSimilar(item)) return true;
		}
		return false;
	}
	/** @return true if one of these items is on the list */
	public boolean itemsAreOnList(ItemStack item1, ItemStack item2) {
		if(itemIsOnList(item1) || itemIsOnList(item2)) return true;
		else return false;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void onEnable() {

		config = getConfig();
		logger = getLogger();
		
		//-----------------------//
		//     Configuration     //
		//-----------------------//
		saveDefaultConfig();
		blockMode = config.getBoolean("blockallitems");
		itemList = (List<ItemStack>) config.getList("items");
		
		inventoryList = new ArrayList<InventoryType>();
		List<String> stringInventories = config.getStringList("inventories");
		for(String invStr : stringInventories) {
			try {
				inventoryList.add(InventoryType.valueOf(invStr.toUpperCase()));
			} catch(IllegalArgumentException e) {
				logger.warning("Wrong config.yml setup! Inventory type " + invStr.toUpperCase() + " doesn't exist!");
			}
		}

		//-----------------------//
		//        Classes        //
		//-----------------------//
		listener = new ContainerBlockerListener(this);
		commandExecutor = new ContainerBlockerCommandExecutor(this);
		
		
		getServer().getPluginManager().registerEvents(listener, this);
		getCommand("containerblocker").setExecutor(commandExecutor);
		getCommand("cb").setExecutor(commandExecutor);

	}
	
}
