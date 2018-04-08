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
    private Double teta;
    private int epoca = 5000;
    private Double[] peso_and;
    private Double[] peso_or;
    private Double[][] treinamento_and = new Double[4][3];
    private Double[][] treinamento_or = new Double[4][3];

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

    //Funçõ de ativação: Degrau
    private Double Degrau(Double x) {
        if (x > 0) {
            return 1.0;
        } else {
            return 0.0;
        }
    }

    //Potencial de Ativação u
    private Double potencialAtivacao(Double entrada1, Double entrada2, Double pesos[]) {

        Double saida = 0.0;
        saida += pesos[0] * teta;
        saida += pesos[1] * entrada1;
        saida += pesos[2] * entrada2;
        return saida;
    }

    //regra de atualização dos pesos
    private Double regraDelta(Double entrada, Double peso, Double erro) {

        return (peso + (this.taxa_aprendizado * erro * entrada));
    }

    public Perceptron(Double tx_ap, Treinamento t) {
        this.teta = 1.0;
        this.taxa_aprendizado = tx_ap;
        this.erro = 0.0;
        peso_or = new Double[3];
        peso_and = new Double[3];
        Random r = new Random();
        for (int i = 0; i < 3; i++) {
            peso_or[i] = r.nextDouble();
            peso_and[i] = r.nextDouble();
        }

        try {
            FileWriter arq = new FileWriter("saida.txt");
            PrintWriter gravarArq = new PrintWriter(arq);
            BufferedReader brand = new BufferedReader(new FileReader("treinamento_and.txt"));
            BufferedReader bror = new BufferedReader(new FileReader("treinamento_or.txt"));
            String[] treino_and;
            String[] treino_or;
            Double saidaYand = null;
            Double saidaYor = null;
            Double error = null;
            for (int i = 0; i < 4; i++) {
                String linha_and = brand.readLine();
                treino_and = linha_and.split(" ");
                String linha_or = bror.readLine();
                treino_or = linha_or.split(" ");
                for (int j = 0; j < 3; j++) {
                    this.treinamento_or[i][j] = Double.parseDouble(treino_or[j + 1]);
                    this.treinamento_and[i][j] = Double.parseDouble(treino_and[j + 1]);
                }
            }
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 3; j++) {
                    System.out.print("X" + j + ": " + treinamento_and[i][j] + " ");
                }
                System.out.println("");
            }

            for (int j = 0; j < this.epoca; j++) {

                if (t == Treinamento.Padrao) {
                    gravarArq.printf("==>Treinamento Padrao%n");
                    for (int tam = 0; tam < 4; tam++) {
                        saidaYand = Degrau(potencialAtivacao(treinamento_and[tam][0], treinamento_and[tam][1], peso_and));
                        saidaYor = Degrau(potencialAtivacao(treinamento_or[tam][0], treinamento_or[tam][1], peso_or));
                        error = treinamento_and[tam][2] - saidaYand;
                        if (error != 0.0) {
                            this.peso_and[0] = regraDelta(teta, peso_and[0], error);
                            this.peso_and[1] = regraDelta(treinamento_and[tam][0], peso_and[1], error);
                            this.peso_and[2] = regraDelta(treinamento_and[tam][1], peso_and[2], error);
                        }
                        gravarArq.printf("AND: E1: " + treinamento_and[tam][0] + " E2: " + treinamento_and[tam][1] + " Y: " + treinamento_and[tam][2] + "%n");
                        gravarArq.printf("AND: w1: " + peso_and[1] + " w2: " + peso_and[2] + " Erro: " + error + " Saida: " + saidaYand + "%n");

                        //======================================================================================================
                        error = treinamento_or[tam][2] - saidaYor;
                        if (error != 0.0) {
                            // saidaYor = Degrau(potencialAtivacao(treinamento_or[tam][0], treinamento_or[tam][1], peso_or));
                            // error = treinamento_or[tam][2] - saidaYor;
                            this.peso_or[0] = regraDelta(teta, peso_or[0], error);
                            this.peso_or[1] = regraDelta(treinamento_or[tam][0], peso_or[1], error);
                            this.peso_or[2] = regraDelta(treinamento_or[tam][1], peso_or[2], error);
                        }
                        gravarArq.printf("OR: E1: " + treinamento_or[tam][0] + " E2: " + treinamento_or[tam][1] + " Y: " + treinamento_or[tam][2] + "%n");
                        gravarArq.printf("OR: w1: " + peso_or[1] + " w2: " + peso_or[2] + " Erro: " + error + " Saida: " + saidaYor + "%n");
                        gravarArq.print(j + "%n");
                    }
                    gravarArq.printf("+-------------+%n");
                }

                if (t == Treinamento.Batelada) {
                    gravarArq.printf("==>Treinamento Batelada%n");
                    Double[] batelada = new Double[3];
                    batelada[0] = 0.0;
                    batelada[1] = 0.0;
                    batelada[2] = 0.0;
                    for (int tam = 0; tam < 4; tam++) {
                        saidaYand = Degrau(potencialAtivacao(treinamento_and[tam][0], treinamento_and[tam][1], peso_and));
                        error = treinamento_and[tam][2] - saidaYand;
                        if (error != 0) {
                            batelada[0] += error;
                            batelada[1] += error;
                            batelada[2] += error;
                        }
                    }
                    for (int tam = 0; tam < 4; tam++) {
                        peso_and[0] = regraDelta(teta, peso_and[0], batelada[0]);;
                        peso_and[1] = regraDelta(treinamento_and[tam][0], peso_and[1], batelada[1]);
                        peso_and[2] = regraDelta(treinamento_and[tam][1], peso_and[2], batelada[2]);
                    }
                    gravarArq.printf("AND: w1: " + peso_and[1] + " w2: " + peso_and[2] + " Erro: " + error + " Saida: " + saidaYand + "%n");
                    //=============================
                    batelada[0] = 0.0;
                    batelada[1] = 0.0;
                    batelada[2] = 0.0;
                    for (int tam = 0; tam < 4; tam++) {
                        saidaYor = Degrau(potencialAtivacao(treinamento_or[tam][0], treinamento_or[tam][1], peso_or));
                        error = treinamento_or[tam][2] - saidaYor;
                        if (error != 0) {
                            batelada[0] += error;
                            batelada[1] += error;
                            batelada[2] += error;
                        }
                    }
                    for (int tam = 0; tam < 4; tam++) {
                        peso_or[0] = regraDelta(teta, peso_or[0], batelada[0]);;
                        peso_or[1] = regraDelta(treinamento_or[tam][0], peso_or[1], batelada[1]);
                        peso_or[2] = regraDelta(treinamento_or[tam][1], peso_or[2], batelada[2]);
                    }
                    gravarArq.printf("OR: w1: " + peso_or[1] + " w2: " + peso_or[2] + " Erro: " + error + " Saida: " + saidaYor + "%n");
                    gravarArq.print(j + "%n");
                    gravarArq.printf("+-------------+%n");
                }

            }
            arq.close();
            brand.close();
            bror.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

    }

    public Double Neuronio_OR(Double[] entrada) {

        return Degrau(potencialAtivacao(entrada[0], entrada[1], peso_or));

    }

    public Double Neuronio_AND(Double entrada[]) {
        System.out.println("Peso 0: " + peso_and[0] + " // Peso 1: " + peso_and[1] + " // Peso 2: " + peso_and[2]);
        //System.out.println("Saida: " + Degrau(potencialAtivacao(entrada[0], entrada[1], peso_and)));
        return Degrau(potencialAtivacao(entrada[0], entrada[1], peso_and));
    }

}
