package me.DevKaL.megasena.objects;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Perfil {
	
	private UUID uuid;
	private String nome;
	private HashMap<List<Integer>, Apostas> numerosApostas;
	
	public Perfil(UUID uuid, String nome, HashMap<List<Integer>, Apostas> numerosApostas) {
		super();
		this.uuid = uuid;
		this.nome = nome;
		this.numerosApostas = numerosApostas;
	}

	public UUID getUuid() {
		return uuid;
	}

	public String getNome() {
		return nome;
	}

	public HashMap<List<Integer>, Apostas> getNumerosApostas() {
		return numerosApostas;
	}

	public void zerarApostas() {
		this.numerosApostas = new HashMap<>();
	}
}
