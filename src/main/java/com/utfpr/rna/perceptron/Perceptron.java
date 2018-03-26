/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.utfpr.rna.perceptron;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

/**
 *
 * @author Marlon Prudente
 */
public class Perceptron {

    private Double[] entrada_treino = new Double[2];
    private Double saida_treino;
    private Double erro;
    private Double taxa_aprendizado;
    private int epoca = 100;
    private Double[] peso_and;
    private Double[] peso_or;
    private boolean tipo_treinamento;

    //Funçõ de ativação: Degrau
    private Double Degrau(Double x) {
        if (x > 0) {
            return 1.0;
        } else {
            return 0.0;
        }
    }
    //Potencial de Ativação u
    private Double potencialAtivacao(Double[] entrada, Double pesos[]) {

        Double saida = 0.0;

        for (int i = 0; i < 2; i++) {
            saida += pesos[i] * entrada[i];
        }
        return saida;
    }
    
    //regra de atualização dos pesos
    private Double regraDelta(Double entrada, Double peso, Double erro) {

        return (peso + (this.taxa_aprendizado * erro * entrada));
    }

    public Perceptron(Double tx_ap, boolean tipo_treinamento) {

        this.taxa_aprendizado = tx_ap;
        this.erro = 0.0;
        peso_or = new Double[2];
        peso_and = new Double[2];
        Random r = new Random();
        for (int i = 0; i < 2; i++) {
            peso_or[i] = r.nextDouble();
            peso_and[i] = r.nextDouble();
        }

        try {
            FileWriter arq = new FileWriter("saida.txt");
            PrintWriter gravarArq = new PrintWriter(arq);
            BufferedReader br = new BufferedReader(new FileReader("treinamento.txt"));
            
            for (int j = 0; j < this.epoca; j++) {
                for (int tam = 0; tam < 4; tam++) {
                    String linha = br.readLine();
                    String[] treino;
                    
                    if (linha.contains("AND")) {
                        treino = linha.split(" ");
                        entrada_treino[0] = Double.parseDouble(treino[1]);
                        entrada_treino[1] = Double.parseDouble(treino[2]);
                        saida_treino = Double.parseDouble(treino[3]);
                        Double saida = 0.0;
                        saida = Degrau(potencialAtivacao(entrada_treino, peso_and));
                        erro = saida_treino - saida;
                        if (erro!=0) {
                            for (int i = 0; i < 2; i++) {
                                peso_and[i] = regraDelta(entrada_treino[i], peso_and[i], erro);
                            }
                            saida = Degrau(potencialAtivacao(entrada_treino, peso_and));
                        }
                        gravarArq.printf("+--Resultado treinmento AND--+%n");
                        gravarArq.printf(entrada_treino[0] + " " + entrada_treino[1] + " " + saida + "%n");
                        gravarArq.printf("+-------------+%n");

                    }

                    if (linha.contains("OR")) {
                        treino = linha.split(" ");
                        entrada_treino[0] = Double.parseDouble(treino[1]);
                        entrada_treino[1] = Double.parseDouble(treino[2]);
                        saida_treino = Double.parseDouble(treino[3]);
                        Double saida = 0.0;
                        saida = Degrau(potencialAtivacao(entrada_treino, peso_or));
                        erro = saida_treino - saida;
                        if (erro!=0) {
                            for (int i = 0; i < 2; i++) {
                                peso_or[i] = regraDelta(entrada_treino[i], peso_or[i], erro);
                            }
                            saida = Degrau(potencialAtivacao(entrada_treino, peso_or));
                        }
                        gravarArq.printf("+--Resultado treinamento OR --+%n");
                        gravarArq.printf(entrada_treino[0] + " " + entrada_treino[1] + " " + saida + "%n");
                        gravarArq.printf("+-------------+%n");
                    }
                    if (!br.ready()) {
                        br.close();
                        br = new BufferedReader(new FileReader("treinamento.txt"));
                    }
                }
            }
            arq.close();
            br.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

    }

    public Double Neuronio_OR(Double[] entrada) {
        return Degrau(potencialAtivacao(entrada, peso_or));

    }

    public Double Neuronio_AND(Double[] entrada) {
        return Degrau(potencialAtivacao(entrada, peso_and));
    }

}
