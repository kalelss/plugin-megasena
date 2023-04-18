package me.DevKaL.megasena.objects;

import java.util.List;
import java.util.UUID;

public class Apostas {
	
	private UUID uuid;
	private List<Integer> numeros;
	
	public Apostas(UUID uuid,List<Integer> numeros) {
		super();
		this.uuid = uuid;
		this.numeros = numeros;
	}

	public UUID getUuid() {
		return uuid;
	}
	public List<Integer> getNumeros() {
		return numeros;
	}
}
