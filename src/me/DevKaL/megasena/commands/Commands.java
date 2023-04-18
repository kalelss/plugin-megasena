package me.DevKaL.megasena.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.DevKaL.megasena.main.Main;
import me.DevKaL.megasena.objects.Apostas;
import me.DevKaL.megasena.objects.Perfil;

public class Commands implements CommandExecutor{

	Long valorPremio = (long) 0;
	Main main = Main.getMain();
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		List<Integer> numerosDigitados = new ArrayList<>();
		Player player = (Player)sender;
		Perfil perfil = Main.getMain().perfilList.get(player.getUniqueId());
		
		if(!(sender instanceof Player)) {
			sender.sendMessage("Comando disponivel apenas para jogadores.");
			return true;
		}

		if (args.length == 0) {
			if(perfil == null) {
				player.sendMessage(main.toMessage("&cVocê não possui um cadastro na MegaSena"));
				player.sendMessage(main.toMessage("&cUse &f/megasena cadastrar"));
				return true;
			} else {
				player.sendMessage(main.toMessage("&eDigite &f/megasena ajuda &epara saber como fazer sua aposta."));;
			return true;
			}
		}
		
		if(args[0].equalsIgnoreCase("cadastrar")) {
			if(!(Main.getMain().perfilList.containsKey(player.getUniqueId()))) {
	    		Perfil novoPerfil = new Perfil(player.getUniqueId(), player.getName(), new HashMap<>());
	    		Main.getMain().perfilList.put(player.getUniqueId(), novoPerfil);
	    		player.sendMessage(main.toMessage("&2Seu cadastro foi criado com sucesso, faça sua aposta."));
			} else {
				player.sendMessage(main.toMessage("&eVocê já possui um cadastro na MegaSena."));
				player.sendMessage(main.toMessage("&eDigite &f/megasena ajuda &epara saber como fazer sua aposta."));

			}
		}
		
		if(args[0].equalsIgnoreCase("ajuda")) {
			player.sendMessage(main.toMessage("&ePara fazer sua aposta, siga os passos abaixo:"));
			player.sendMessage(main.toMessage("&f/megasena apostar 10 23 34 45 56 60"));
			player.sendMessage(main.toMessage("&eEscolha seus números e faça quantas apostas quiser."));
			player.sendMessage(main.toMessage("&eSepare os numeros apenas com 1 espaço."));
			player.sendMessage(main.toMessage("&eNão repita um numero e digite valores entre 1 e 60."));
			player.sendMessage(main.toMessage("&eBoa sorte."));
		}
		
		if(args[0].equalsIgnoreCase("apostar")) {
			if(main.getEcon().getBalance(player) < main.getNumConfig("Aposta.valor")) {
				player.sendMessage(main.toMessage("&cVocê não possui dinheiro suficiente para fazer a aposta."));
			} else {
				if(perfil == null) {
					player.sendMessage(main.toMessage("&cVocê não possui um cadastro na MegaSena"));
					player.sendMessage(main.toMessage("&cUse /megasena cadastrar"));
					return true;
				} 
				if(args.length > 1 && args.length == 7) {
					for(int i = 1; i < args.length; i++) {
						int n = Integer.parseInt(args[i]);
						if(n > 60) {
							player.sendMessage(main.toMessage("&eOs números para apostas são de 1 até 60."));
							return true;
						}
						if(numerosDigitados.contains(n)) {
							player.sendMessage(main.toMessage("&eVocê não pode digitar numeros iguais."));
							return true;
						}
						numerosDigitados.add(n);
					}
					Apostas apostas = new Apostas(player.getUniqueId(), numerosDigitados);
					if(perfil.getNumerosApostas().containsKey(numerosDigitados)) {
						player.sendMessage(main.toMessage("&cVocê já apostou esse numero, escolha outra sequência."));
					} else {
						perfil.getNumerosApostas().put(numerosDigitados, apostas);
						player.sendMessage(main.toMessage("&2Aposta feita com sucesso, boa sorte."));
						valorPremio += main.getNumConfig("Aposta.valor");
						main.getEcon().withdrawPlayer(player, main.getNumConfig("Aposta.valor"));
					}
					
				} else {
					player.sendMessage(main.toMessage("&eA sua aposta deve conter 6 numeros."));
				}
			}
			
		}
		
		if(args[0].equalsIgnoreCase("infos")) {
			player.sendMessage(main.toMessage("&2Valor do prêmio: &f $ " + main.decimalFormat(valorPremio)));
			player.sendMessage(main.toMessage("&2Valor da aposta: &f $ " + main.decimalFormat(main.getNumConfig("Aposta.valor"))));
		}
		
		if(args[0].equalsIgnoreCase("sorteio")) {
			String messageLineSeparated = String.join("\n", main.getConfig().getStringList("Mensagem_sorteio")).replaceAll("&", "§");
			player.sendMessage(messageLineSeparated);
		}
		
		if(args[0].equalsIgnoreCase("iniciar")) {
			if(sender.hasPermission("megasena.staff")) {
				List<Integer> numerosSorteados = MegaSena.sorteioFixo();
				List<String> ganhadores = new ArrayList<>();
				for(Entry<UUID, Perfil> perfilPlayer : Main.getMain().perfilList.entrySet()) {
					for(List<Integer> lista : perfilPlayer.getValue().getNumerosApostas().keySet()) {
						if(lista.equals(numerosSorteados)) {
							ganhadores.add(perfilPlayer.getValue().getNome());
						} 
					}
				}
				
				String nomePlayer = ganhadores.toString().replace("[", "").replace("]", "");
				OfflinePlayer vencedor = Bukkit.getOfflinePlayer(nomePlayer);
				if(ganhadores.isEmpty()) {
					Bukkit.broadcastMessage(main.toMessage("&4Não tivemos nenhum ganhador nesse sorteio"));
					Bukkit.broadcastMessage(main.toMessage("&4O valor ficou acumulado em: " + valorPremio));
					Bukkit.broadcastMessage(main.toMessage("&4Continuem suas apostas e até o próximo sorteio"));
					Bukkit.broadcastMessage(main.toMessage("&4Boa sorte"));
					for(Entry<UUID, Perfil> listaPerfil : Main.getMain().perfilList.entrySet()) {
						listaPerfil.getValue().zerarApostas();
					}
				} else if(ganhadores.size() > 1) {
					Long divisaoPremio = valorPremio / ganhadores.size();
					Bukkit.broadcastMessage(main.toMessage("&aTivemos mais de um ganhador e os nomes são:"));
					Bukkit.broadcastMessage(main.toMessage("&a" + nomePlayer + "."));
					Bukkit.broadcastMessage(main.toMessage("&aParabéns, o prêmio foi adicionado nas suas contas."));
					Bukkit.broadcastMessage(main.toMessage("&aCada um recebeu " + divisaoPremio));
					Bukkit.broadcastMessage(main.toMessage("&aFaçam novamente suas apostas e até o próximo sorteio."));
					for(int i = 0; i < ganhadores.size(); i++) {
						String nomeGanhador = ganhadores.get(i);
						OfflinePlayer vencedores = Bukkit.getOfflinePlayer(nomeGanhador);
						main.getEcon().depositPlayer(vencedores, divisaoPremio);
					}
					 
					for(Entry<UUID, Perfil> listaPerfil : Main.getMain().perfilList.entrySet()) {
						listaPerfil.getValue().zerarApostas();
					}
					valorPremio = (long) 0;
				} else {
					Bukkit.broadcastMessage(main.toMessage("&aO vencedor da MegaSena foi: " + nomePlayer));
					Bukkit.broadcastMessage(main.toMessage("&aParabéns, o prêmio de " + valorPremio + " foi adicionado na sua conta"));
					Bukkit.broadcastMessage(main.toMessage("&aFaçam novamente suas apostas e até o próximo sorteio."));
					for(Entry<UUID, Perfil> listaPerfil : Main.getMain().perfilList.entrySet()) {
						listaPerfil.getValue().zerarApostas();
					}
					main.getEcon().depositPlayer(vencedor, valorPremio);
					valorPremio = (long) 0;
					
				}
			}
		}
		return false;
	}
}
