package me.DevKaL.megasena.main;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import me.DevKaL.megasena.commands.Commands;
import me.DevKaL.megasena.objects.Perfil;
import net.milkbowl.vault.economy.Economy;


public class Main extends JavaPlugin{
	
	public HashMap<UUID, Perfil> perfilList = new HashMap<>();
	private static Economy econ = null;
	
	@Override
	public void onEnable() {
		if (!setupEconomy() ) {
			getServer().getPluginManager().disablePlugin(this);
			return;
		}
		registerCommands();
		registerEvents();
		saveDefaultConfig();
		getServer().getConsoleSender().sendMessage("§a["+ getDescription().getName() + "] ============================");
		getServer().getConsoleSender().sendMessage("§a["+ getDescription().getName() + "] Iniciado com sucesso!");
		getServer().getConsoleSender().sendMessage("§a["+ getDescription().getName() + "] ============================");
		
	}

	@Override
	public void onDisable() {

		HandlerList.unregisterAll();
		
		getServer().getConsoleSender().sendMessage("§c["+ getDescription().getName() + "] ============================");
		getServer().getConsoleSender().sendMessage("§c["+ getDescription().getName() + "] Finalizado com sucesso!");
		getServer().getConsoleSender().sendMessage("§c["+ getDescription().getName() + "] ============================");
		
	}

	private boolean setupEconomy() {
		if (getServer().getPluginManager().getPlugin("Vault") == null) {
			return false;
		}
		RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
		if (rsp == null) {
			return false;
		}
		econ = rsp.getProvider();
		return econ != null;
	}
	
	public void registerCommands() {
		final Commands commands = new Commands();
		getCommand("megasena").setExecutor(commands);
	}
	
	public void registerEvents() {
		//getServer().getPluginManager().registerEvents(new Events(), this);
	}
	
	public Economy getEcon() {
		return econ;
	}
	
	public String toMessage(String path) {
		return ChatColor.translateAlternateColorCodes('&', path);
	}
	
	public String getTextConfig(String path) {
		return ChatColor.translateAlternateColorCodes('&',
				getConfig().getString(path));
	}
	
	public Integer getNumConfig(String path) {
		return getConfig().getInt(path);
	}
	
	public String decimalFormat(double d) {
		DecimalFormat df = new DecimalFormat();
		return df.format(d) + ".00";
	}
	
	public static Main getMain() {
		return (Main) Bukkit.getPluginManager().getPlugin("K-MegaSena");
	}
	
}
