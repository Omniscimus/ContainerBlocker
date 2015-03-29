package net.omniscimus.containerblocker;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
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
	//   config.yml values   //
	//-----------------------//
	
	private boolean blockMode;
	/** @return true if all items should be blocked except the listed ones, false if only the listed items should be blocked */
	public boolean getBlockMode() {
		return blockMode;
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

		//-----------------------//
		//     Configuration     //
		//-----------------------//
		saveDefaultConfig();
		blockMode = getConfig().getBoolean("blockallitems");
		itemList = (List<ItemStack>) getConfig().getList("items");

		//-----------------------//
		//        Classes        //
		//-----------------------//
		listener = new ContainerBlockerListener(this);
		commandExecutor = new ContainerBlockerCommandExecutor(this);
		
		
		getServer().getPluginManager().registerEvents(listener, this);
		getCommand("containerblocker").setExecutor(commandExecutor);

	}
	
}