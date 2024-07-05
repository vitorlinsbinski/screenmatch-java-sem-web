package br.com.alura.screenmatch.principal;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ParallelStream {
    public static void main(String[] args) {
        List<Integer> numeros = new ArrayList<>();
        for (int i = 1; i <= 100; i++) {
            numeros.add(i);
        }

        Optional<Integer> numeroQualquer = numeros.parallelStream()
                .filter(numero -> numero % 10 == 0)
                .findAny();

        if(numeroQualquer.isPresent()) {
            System.out.println("Encontrado: " + numeroQualquer);
        } else {
            System.out.println("Nenhum número encontrado!");
        }
    }
}
