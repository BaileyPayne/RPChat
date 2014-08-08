
package me.baileypayne.rpchat;

import java.util.ArrayList;
import java.util.HashMap;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author Bailey
 */
public class RPChat extends JavaPlugin implements Listener {
    
    int defaultrange = 0;
    int maxrange = 0;
    
    HashMap<String, Integer> enable = new HashMap<>();
    
    @Override
    public void onEnable(){
        this.getConfig().options().copyDefaults(true);
        defaultrange = this.getConfig().getInt("defaultrange");
        maxrange = this.getConfig().getInt("maxrange");
        for(Player p : this.getServer().getOnlinePlayers()){
            enable.put(p.getName(), defaultrange);
        }
        this.getServer().getPluginManager().registerEvents(this, this);
    }
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String command, String[] args)
	{
		if (command.equalsIgnoreCase("RangedChat"))
		{
			if (!(sender instanceof Player)) return true;
			Player p = (Player)sender;
			if (Integer.parseInt(args[0]) > maxrange) { enable.put(p.getName() , maxrange); return true; }
			enable.put(p.getName() , Integer.parseInt(args[0]));
		}
		return true;
	}
    @EventHandler
	public void onPlayerJoin(PlayerJoinEvent e)
	{
		this.enable.put(e.getPlayer().getName(), defaultrange);
	}

	@EventHandler
	public void onChatEvent(PlayerChatEvent e)
	{
		Player p = e.getPlayer();
		if (enable.get(p.getName()) > 0 && p.hasPermission("RadiusChat.use"))
		{
			ArrayList<Player> rangedPlayers = new ArrayList<>();
			e.setCancelled(true);
			double range = (double)enable.get(p.getName());
			for (Entity ent : p.getNearbyEntities(range, range, range))
			{
				if (ent instanceof Player)
				{
					rangedPlayers.add((Player) ent);
				}
			}


			StringBuilder sb = new StringBuilder();
			for (Player pl : rangedPlayers)
			{
				pl.sendMessage("<" + p.getName() + "> " + e.getMessage());
				sb.append(pl.getName() + ", ");
			}
			if (!"".equals(sb.toString()) || sb.toString() != null) {
                        p.sendMessage("you sent " + e.getMessage() + " to: " + sb.toString());
                    }
			else {
                        p.sendMessage("No one can hear you!");
                    }
		}
	}
    

}
