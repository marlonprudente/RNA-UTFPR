/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.utfpr.rna.adalaine;

import com.utfpr.rna.perceptron.Perceptron;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Objects;
import java.util.Random;

/**
 *
 * @author Marlon Prudente
 */
public class Adalaine {

    public Double[][] matriz_treino = new Double[15][5];
    public Double taxa_aprendizado;
    public int epoca = 100;
    private Double[] peso = new Double[3];

    public enum Treinamento {
        Padrao(0), Batelada(1);
        private final int valor;

        Treinamento(int valorOpcao) {
            valor = valorOpcao;
        }

        public int getValor() {
            return valor;
        }
    }

    private Double regraDelta(Double entrada, Double peso, Double erro) {
        return (peso + (this.taxa_aprendizado * erro * entrada));
    }

    private Double potencialAtivacao(Double entrada1, Double entrada2, Double entrada3, Double pesos[]) {
        Double saida = 0.0;
        saida += pesos[0] * entrada1;
        saida += pesos[1] * entrada2;
        saida += pesos[2] * entrada3;
        return saida;
    }

    public Adalaine(Double tx_aprend, Treinamento t) {
        taxa_aprendizado = tx_aprend;
        Random r = new Random();
        for (int i = 0; i < 3; i++) {
            peso[i] = r.nextDouble();
        }

        try {
            FileWriter arq = new FileWriter("saida_adalaine.txt");
            PrintWriter gravarArq = new PrintWriter(arq);
            BufferedReader br = new BufferedReader(new FileReader("treinamento_adalaine.txt"));
            String[] treino;
            String linha;
            while (br.ready()) {
                for (int i = 0; i < 15; i++) {
                    linha = br.readLine();
                    treino = linha.split(" ");
                    for (int j = 0; j < 5; j++) {
                        matriz_treino[i][j] = Double.parseDouble(treino[j]);
                    }
                }
            }
            //ImprimirMatriz();
            Double saida = 0.0;
            Double erro = 0.0;
            Double eqm = 0.0;
            //Matriz preenchida
            do {
                if (t == Treinamento.Padrao) {

                    for (int i = 0; i < 15; i++) {
                        saida = potencialAtivacao(matriz_treino[i][1], matriz_treino[i][2], matriz_treino[i][3], peso);
                        eqm += (Math.pow(matriz_treino[i][4] - saida, 2));
                        erro = 0.5 * Math.pow(matriz_treino[i][4] - saida, 2);
                        if (!Objects.equals(saida, matriz_treino[i][4])) {
                            peso[0] = regraDelta(matriz_treino[i][1], peso[0], erro);
                            peso[1] = regraDelta(matriz_treino[i][2], peso[1], erro);
                            peso[2] = regraDelta(matriz_treino[i][3], peso[2], erro);
                        }
                        System.out.println("Saida: " + saida);
                        System.out.println("Eqm: " + eqm);
                        System.out.println("Erro: " + erro);
                        System.out.println("w1: " + peso[0] + " w2: " + peso[1] + " w3: " + peso[2]);
                    }
                    eqm = 1 / 15 * eqm;
                    epoca--;
                }
                if (t == Treinamento.Batelada) {
                    Double batelada = 0.0;
                    for (int i = 0; i < 15; i++) {
                        saida = potencialAtivacao(matriz_treino[i][1], matriz_treino[i][2], matriz_treino[i][3], peso);
                        eqm += (Math.pow(matriz_treino[i][4] - saida, 2));
                        erro = 0.5 * Math.pow(matriz_treino[i][4] - saida, 2);
                        if (!Objects.equals(saida, matriz_treino[i][4])) {
                            batelada += erro;
                        }
                    }
                    peso[0] = batelada;
                    peso[1] = batelada;
                    peso[2] = batelada;
                    System.out.println("Saida: " + saida);
                    System.out.println("Eqm: " + eqm);
                    System.out.println("Erro: " + erro);
                    System.out.println("w1: " + peso[0] + " w2: " + peso[1] + " w3: " + peso[2]);
                    batelada = 0.0;
                    eqm = 1 / 15 * eqm;
                    epoca--;
                }
            } while (eqm < 0.1 || erro > 0.1 || epoca != 0);
            arq.close();
            br.close();
        } catch (IOException ioe) {
            System.out.println(ioe);
        }

    }

    public void ImprimirMatriz() {
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 5; j++) {
                System.out.print("" + i + " ");
                System.out.print(" " + this.matriz_treino[i][j]);
            }
            System.out.println("");
        }
    }

}
