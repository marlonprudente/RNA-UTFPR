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
    public int epoca = 5000;
    public Double teta;
    private Double[] peso = new Double[4];

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
        saida += pesos[0] * teta;
        saida += pesos[1] * entrada1;
        saida += pesos[2] * entrada2;
        saida += pesos[3] * entrada3;
        return saida;
    }

    public Adalaine(Double tx_aprend, Treinamento t) {
        taxa_aprendizado = tx_aprend;
        teta = 1.0;
        Random r = new Random();
        for (int i = 0; i < 4; i++) {
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
            ImprimirMatriz();
            Double saida = 0.0;
            Double erro = 0.0;
            Double eqm = 0.0;  
            Double[] batelada = new Double[4];
            //Matriz preenchida
            do {
                eqm = 0.0;
                if (t == Treinamento.Padrao) {
                    for (int i = 0; i < 15; i++) {
                        saida = potencialAtivacao(matriz_treino[i][1], matriz_treino[i][2], matriz_treino[i][3], peso);
                        eqm += (Math.pow(matriz_treino[i][4] - saida, 2));
                        erro = (matriz_treino[i][4] - saida);
                        if (erro!=0) {
                            peso[0] = regraDelta(teta, peso[0], erro);
                            peso[1] = regraDelta(matriz_treino[i][1], peso[1], erro);
                            peso[2] = regraDelta(matriz_treino[i][2], peso[2], erro);
                            peso[3] = regraDelta(matriz_treino[i][3], peso[3], erro);
                        }
                        System.out.println("Epoca: " + epoca);
                        System.out.println("Saida: " + saida);
                        System.out.println("Eqm: " + eqm);
                        System.out.println("Erro: " + erro);
                        System.out.println("w1: " + peso[1] + " w2: " + peso[2] + " w3: " + peso[3]);
                    }
                    gravarArq.print(epoca+" %n");
                    gravarArq.print("=Padrao= %n");
                    gravarArq.print("w1: " + peso[1] + " w2: " + peso[2] + " w3: " + peso[3]+"%n");
                }
                //===============================================================================================
                if (t == Treinamento.Batelada) {
                    batelada[0] = 0.0;
                    batelada[1] = 0.0;
                    batelada[2] = 0.0;
                    batelada[3] = 0.0;
                    for (int i = 0; i < 15; i++) {
                        saida = potencialAtivacao(matriz_treino[i][1], matriz_treino[i][2], matriz_treino[i][3], peso);
                        eqm += (Math.pow(matriz_treino[i][4] - saida, 2));
                        erro = (matriz_treino[i][4] - saida);
                        if (erro!=0) {
                            batelada[0] += teta*peso[0]* erro;
                            batelada[1] += matriz_treino[i][1]*peso[1]* erro;
                            batelada[2] += matriz_treino[i][2]*peso[2]* erro;
                            batelada[3] += matriz_treino[i][3]*peso[3]* erro;
                        }
                    }
                    
                    peso[0] += (1/15)*batelada[0];
                    peso[1] += (1/15)*batelada[1];
                    peso[2] += (1/15)*batelada[2];
                    peso[3] += (1/15)*batelada[3];
                    
                    System.out.println("Epoca: " + epoca);
                    System.out.println("Saida: " + saida);
                    System.out.println("Eqm: " + eqm);
                    System.out.println("Erro: " + erro);
                    System.out.println("w1: " + peso[1] + " w2: " + peso[2] + " w3: " + peso[3]);
                    
                    gravarArq.print(epoca+"%n");
                    gravarArq.print("=Batelada=%n");
                    gravarArq.print("w1: " + peso[1] + " w2: " + peso[2] + " w3: " + peso[3]+"%n");
                } 
                epoca--;
                eqm = 1 / 15 * eqm;
            } while (epoca != 0 || eqm > 0.1);
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
    
    public Double Resultado(Double x1, Double x2, Double x3){
        return potencialAtivacao(x1, x2, x3, peso);
    }

}
