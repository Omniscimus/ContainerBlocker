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

	//-----------------------//
	//   config.yml values   //
	//-----------------------//
	
	private boolean blockMode;
	/** @return true if all items should be blocked except the listed ones, false if only the listed items should be blocked */
	public boolean getBlockMode() {
		return blockMode;
	}
	
	private List<ItemStack> itemList;
	private List<ItemStack> getItemList() {
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
		
		
		getServer().getPluginManager().registerEvents(listener, this);

	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {

		// If the command is something else than /containerblocker, return
		if(!commandLabel.equalsIgnoreCase("containerblocker")) return true;
		// If the sender isn't the console and hasn't got the containerblocker.admin permission, return
		if(sender instanceof Player && !sender.hasPermission("containerblocker.admin")) {
			sender.sendMessage(ChatColor.RED + "You don't have permission to execute this command.");
			return true;
		}


		// containerblocker
		if(args.length == 0) {
			sender.sendMessage(
					ChatColor.RED + "\n------------------------------\n"
							+ ChatColor.GOLD + "ContainerBlocker v." + getDescription().getVersion() + "\nPlugin made by Omniscimus\nFor command help, check /containerblocker help\n"
							+ ChatColor.RED + "------------------------------"
					);
			return true;
		}
		else if(args.length == 1) {
			if(args[0].equalsIgnoreCase("?") || args[0].equalsIgnoreCase("help")) {
				sender.sendMessage(
						ChatColor.GOLD + "\n-- ContainerBlocker command help --\n/containerblocker list: " + ChatColor.RED + "displays the list of relevant items.\n" + ChatColor.GOLD + "/containerblocker add: " + ChatColor.RED + "adds the item you're currently holding to the list.\n" + ChatColor.GOLD + "/containerblocker remove: " + ChatColor.RED + "removes the item you're currently holding from the list.\n"
						);
				return true;
			}
			else if(args[0].equalsIgnoreCase("list")) {
				sender.sendMessage(ChatColor.RED + "Items on the list:");
				for(ItemStack is : itemList) {
					sender.sendMessage(ChatColor.RED + "- " + is.getType().toString() + ":" + is.getDurability());
				}
				return true;
			}

			else if(args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("remove")) {
				if(sender instanceof Player) {
					if(args[0].equalsIgnoreCase("add")) {
						itemList.add(((Player)sender).getItemInHand());
						getConfig().set("items", itemList);
						sender.sendMessage(ChatColor.RED + "Item has been added to the list.");
						saveConfig();
						return true;
					}
					else if(args[0].equalsIgnoreCase("remove")) {
						if(itemList.contains(((Player)sender).getItemInHand())) {
							itemList.remove(((Player)sender).getItemInHand());
							getConfig().set("items", itemList);
							sender.sendMessage(ChatColor.RED + "Item has been removed from the list.");
							saveConfig();
							return true;
						}
						else {
							sender.sendMessage(ChatColor.RED + "Specified item isn't in the list.");
							return true;
						}
					}
				}
				else sender.sendMessage("This command can only be executed by a player.");

			}

		}
		// Wrong syntax
		else if(args.length > 2) {
			sender.sendMessage(ChatColor.RED + "Wrong command syntax. Please try /jcf help for some help.");
			return true;
		}

		return true;

	}

}