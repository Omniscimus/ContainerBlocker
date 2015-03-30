package net.omniscimus.containerblocker;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * @author Omniscimus
 */
public class ContainerBlockerCommandExecutor implements CommandExecutor {

	private ContainerBlocker plugin;

	public ContainerBlockerCommandExecutor(ContainerBlocker plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		// We don't have to check if the command is /cb or /containerblocker because this executor class is only passed those commands.
		
		// If the sender isn't the console and hasn't got the containerblocker.admin permission, return
		if(sender instanceof Player && !sender.hasPermission("containerblocker.admin")) {
			sender.sendMessage(ChatColor.RED + "You don't have permission to execute this command.");
			return true;
		}
		// containerblocker
		if(args.length == 0) {
			sender.sendMessage(ChatColor.RED + "\n------------------------------\n"
					+ ChatColor.GOLD + "ContainerBlocker v." + plugin.getDescription().getVersion() + "\nPlugin made by Omniscimus\nFor command help, check /containerblocker help\n"
					+ ChatColor.RED + "------------------------------"
					);
			return true;
		}
		else if(args.length == 1) {
			if(args[0].equalsIgnoreCase("?") || args[0].equalsIgnoreCase("help")) {
				sender.sendMessage(
						ChatColor.RED + "\n-- " + ChatColor.GOLD + "ContainerBlocker command help " + ChatColor.RED + "--\n" + ChatColor.GOLD + "/containerblocker list: " + ChatColor.RED + "displays the list of relevant items.\n" + ChatColor.GOLD + "/containerblocker add: " + ChatColor.RED + "adds the item you're currently holding to the list.\n" + ChatColor.GOLD + "/containerblocker remove: " + ChatColor.RED + "removes the item you're currently holding from the list.\n"
						);
				return true;
			}
			else if(args[0].equalsIgnoreCase("list")) {
				sender.sendMessage(ChatColor.RED + "Items on the list:");
				for(ItemStack is : plugin.getItemList()) {
					sender.sendMessage(ChatColor.RED + "- " + is.getType().toString() + ":" + is.getDurability());
				}
				if(plugin.getItemList().size() == 0) sender.sendMessage(ChatColor.RED + "There are no items on the list.");
				return true;
			}
			else if(args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("remove")) {
				if(sender instanceof Player) {
					if(args[0].equalsIgnoreCase("add")) {
						plugin.getItemList().add(((Player)sender).getItemInHand());
						plugin.getConfig().set("items", plugin.getItemList());
						sender.sendMessage(ChatColor.RED + "Item has been added to the list.");
						plugin.saveConfig();
						return true;
					}
					else if(args[0].equalsIgnoreCase("remove")) {
						if(plugin.getItemList().contains(((Player)sender).getItemInHand())) {
							plugin.getItemList().remove(((Player)sender).getItemInHand());
							plugin.getConfig().set("items", plugin.getItemList());
							sender.sendMessage(ChatColor.RED + "Item has been removed from the list.");
							plugin.saveConfig();
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
