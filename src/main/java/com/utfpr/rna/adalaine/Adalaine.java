/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.utfpr.rna.adalaine;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import static java.lang.Math.*;
import java.util.Random;

/**
 *
* @author Marlon Prudente <marlonoliveira at alunos.utfpr.edu.br>
 */
public class Adalaine {

    public Double[][] matriz_treino;
    public Double taxa_aprendizado;
    public int epoca;
    public Double x0;
    private Double[] peso = new Double[4];

    private Double regraDelta(Double entrada, Double peso, Double erro) {
        return (peso + (this.taxa_aprendizado * erro * entrada));
    }

    private Double potencialAtivacao(Double entrada1, Double entrada2, Double entrada3, Double pesos[]) {
        Double saida = 0.0;
        saida += pesos[0] * x0;
        saida += pesos[1] * entrada1;
        saida += pesos[2] * entrada2;
        saida += pesos[3] * entrada3;
        return saida;
    }

    public Adalaine(Double tx_aprend, Integer epocas, Boolean isBatelada, Integer qtPadroes) {
        taxa_aprendizado = tx_aprend;
        this.epoca = epocas;
        x0 = 1.0;
        matriz_treino = new Double[qtPadroes][5];
        
        Random r = new Random();        
        for (int i = 0; i < 4; i++) {
            peso[i] = r.nextDouble();
        }
        
        try {
            FileWriter arq = new FileWriter("output_data/saida_adalaine.txt");
            PrintWriter gravarArq = new PrintWriter(arq);
            BufferedReader br = new BufferedReader(new FileReader("input_data/treino_adaline.txt"));
            String[] treino;
            String linha;
            while (br.ready()) {
                for (int i = 0; i < qtPadroes; i++) {
                    linha = br.readLine();
                    treino = linha.split(" ");
                    for (int j = 0; j < 5; j++) {
                        matriz_treino[i][j] = Double.parseDouble(treino[j]);
                    }
                }
            }
            Double saida;
            Double erro;
            Double eqm;
            Double[] batelada = new Double[4];
            //Matriz preenchida
            gravarArq.print("Taxa de Aprendizagem: " + taxa_aprendizado + " | Padroes: " + qtPadroes + "\r\n");
            do {
                eqm = 0.0;
                if (!isBatelada) {
                    gravarArq.print("=> Aprendizado PADRÃO: >Epoca: " + epoca + " \r\n");
                    for (int i = 0; i < qtPadroes; i++) {
                        saida = potencialAtivacao(matriz_treino[i][1], matriz_treino[i][2], matriz_treino[i][3], peso);
                        eqm += (Math.pow(matriz_treino[i][4] - saida, 2));
                        erro = (matriz_treino[i][4] - saida);
                        if (erro != 0) {
                            peso[0] = regraDelta(x0, peso[0], erro);
                            peso[1] = regraDelta(matriz_treino[i][1], peso[1], erro);
                            peso[2] = regraDelta(matriz_treino[i][2], peso[2], erro);
                            peso[3] = regraDelta(matriz_treino[i][3], peso[3], erro);
                        }
                    }
                    gravarArq.print("w0: " + peso[0] + "w1: " + peso[1] + " w2: " + peso[2] + " w3: " + peso[3] + "\r\n");
                }

                //===============================================================================================
                if (isBatelada) {
                    gravarArq.print("=> Aprendizado BATELADA: >Epoca: " + epoca + " \r\n");
                    batelada[0] = 0.0;
                    batelada[1] = 0.0;
                    batelada[2] = 0.0;
                    batelada[3] = 0.0;
                    for (int i = 0; i < qtPadroes; i++) {
                        saida = potencialAtivacao(matriz_treino[i][1], matriz_treino[i][2], matriz_treino[i][3], peso);
                        eqm += (Math.pow(matriz_treino[i][4] - saida, 2));
                        erro = (matriz_treino[i][4] - saida);
                        if (erro != 0) {
                            batelada[0] += x0 * erro;
                            batelada[1] += matriz_treino[i][1] * erro;
                            batelada[2] += matriz_treino[i][2] * erro;
                            batelada[3] += matriz_treino[i][3] * erro;
                        }
                    }
                    peso[0] += (1 / qtPadroes.doubleValue()) * taxa_aprendizado * batelada[0];
                    peso[1] += (1 / qtPadroes.doubleValue()) * taxa_aprendizado * batelada[1];
                    peso[2] += (1 / qtPadroes.doubleValue()) * taxa_aprendizado * batelada[2];
                    peso[3] += (1 / qtPadroes.doubleValue()) * taxa_aprendizado * batelada[3];
                    gravarArq.print("w0: " + peso[0] + " w1: " + peso[1] + " w2: " + peso[2] + " w3: " + peso[3] + "\r\n");
                }
                epoca--;
                eqm = 1 / qtPadroes.doubleValue() * eqm;
                gravarArq.print("=>Eqm: " + eqm + " \r\n");
            } while (epoca != 0 && eqm > 0.001);
            arq.close();
            br.close();
        } catch (IOException ioe) {
            System.out.println(ioe);
        }

    }

    public void ImprimirMatriz() {
        for (int i = 0; i < 50; i++) {
            for (int j = 0; j < 5; j++) {
                System.out.print("" + i + " ");
                System.out.print(" " + this.matriz_treino[i][j]);
            }
            System.out.println("");
        }
    }

    public Double Resultado(Double x1, Double x2, Double x3) {
        return potencialAtivacao(x1, x2, x3, peso);
    }

    public Double Saida(Double z) {
        return potencialAtivacao(sin(z), cos(z), z, peso);
    }

}
