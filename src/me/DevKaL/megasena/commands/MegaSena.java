package me.DevKaL.megasena.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MegaSena {
	
	public static List<Integer> sorteioAleatorio(int numeroMaximo, int tamanhoLista){
		List<Integer> numeros = new ArrayList<>();
		Random random = new Random();
		while(numeros.size() < tamanhoLista) {
			int n = random.nextInt(numeroMaximo) + 1;
			if(!(numeros.contains(n))) {
				numeros.add(n);
			}
		}
		Collections.sort(numeros);
		return numeros;
	}
	
	public static List<Integer> sorteioFixo(){
		List<Integer> numeros = new ArrayList<>();
		int vetor[] = {10,11,12,13,14,15};
		for(int i = 0; i < vetor.length; i++) {
			numeros.add(vetor[i]);
		}
		return numeros;
	}
	
}
