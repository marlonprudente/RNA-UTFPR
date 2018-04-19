/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.utfpr.rna.perceptron;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

/**
 *
 * @author Marlon Prudente
 */
public class Perceptron {
    
    private Double taxa_aprendizado;
    private Double x0;
    private int epoca;
    private Double[] peso_and;
    private Double[] peso_or;
    private Double[][] treinamento_and = {{0.0, 0.0, 0.0},{0.0, 1.0, 0.0}, {1.0, 0.0, 0.0}, {1.0,1.0,1.0}};
    private Double[][] treinamento_or = {{0.0, 0.0, 0.0},{0.0, 1.0, 1.0}, {1.0, 0.0, 1.0}, {1.0,1.0,1.0}};

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
        saida += pesos[0] * x0;
        saida += pesos[1] * entrada1;
        saida += pesos[2] * entrada2;
        return saida;
    }

    //regra de atualização dos pesos
    private Double regraDelta(Double entrada, Double peso, Double erro) {
        return (peso + (this.taxa_aprendizado * erro * entrada));
    }

    public Perceptron(Double tx_ap, Integer epocas, Boolean isBatelada) {
        this.x0 = 1.0;
        this.taxa_aprendizado = tx_ap;        
        this.epoca = epocas; 
        peso_or = new Double[3];
        peso_and = new Double[3];
        Random r = new Random();
        for (int i = 0; i < 3; i++) {
            peso_or[i] = r.nextDouble();
            peso_and[i] = r.nextDouble();
        }

        try {
            FileWriter arq = new FileWriter("logs/saida_perceptron.txt");
            PrintWriter gravarArq = new PrintWriter(arq);
            Double saidaYand = null;
            Double saidaYor = null;
            Double error = null;
            Double[] batelada = new Double[4] ;

            for (int j = 0; j < this.epoca; j++) {

                if (!isBatelada) {
                    gravarArq.printf("==>Treinamento Padrao\r\n");
                    for (int tam = 0; tam < 4; tam++) {
                        saidaYand = Degrau(potencialAtivacao(treinamento_and[tam][0], treinamento_and[tam][1], peso_and));
                        saidaYor = Degrau(potencialAtivacao(treinamento_or[tam][0], treinamento_or[tam][1], peso_or));
                        error = treinamento_and[tam][2] - saidaYand;
                        if (error != 0.0) {
                            this.peso_and[0] = regraDelta(x0, peso_and[0], error);
                            this.peso_and[1] = regraDelta(treinamento_and[tam][0], peso_and[1], error);
                            this.peso_and[2] = regraDelta(treinamento_and[tam][1], peso_and[2], error);
                        }
                        gravarArq.printf("AND: w1: " + peso_and[1] + " w2: " + peso_and[2] + " Erro: " + error + " Saida: " + saidaYand + "\r\n");

                        //======================================================================================================
                        error = treinamento_or[tam][2] - saidaYor;
                        if (error != 0.0) {
                            this.peso_or[0] = regraDelta(x0, peso_or[0], error);
                            this.peso_or[1] = regraDelta(treinamento_or[tam][0], peso_or[1], error);
                            this.peso_or[2] = regraDelta(treinamento_or[tam][1], peso_or[2], error);
                        }
                        gravarArq.printf("OR: w1: " + peso_or[1] + " w2: " + peso_or[2] + " Erro: " + error + " Saida: " + saidaYor + "\r\n");
                        gravarArq.print(j + "\r\n");
                    }
                    gravarArq.printf("+-------------+\r\n");
                }

                if (isBatelada) {
                    gravarArq.printf("==>Treinamento Batelada\r\n");                    
                    batelada[0] = 0.0;
                    batelada[1] = 0.0;
                    batelada[2] = 0.0;
                    for (int tam = 0; tam < 4; tam++) {
                        saidaYand = Degrau(potencialAtivacao(treinamento_and[tam][0], treinamento_and[tam][1], peso_and));
                        error = treinamento_and[tam][2] - saidaYand;
                        if (error != 0) {
                            batelada[0] += x0* error;
                            batelada[1] += treinamento_and[tam][0]* error;
                            batelada[2] += treinamento_and[tam][1]* error;
                        }
                    }                    
                        peso_and[0] += 0.25*taxa_aprendizado*batelada[0];
                        peso_and[1] += 0.25*taxa_aprendizado*batelada[1];
                        peso_and[2] += 0.25*taxa_aprendizado*batelada[2];
                    
                    gravarArq.printf("AND: w1: " + peso_and[1] + " w2: " + peso_and[2] + " Erro: " + error + " Saida: " + saidaYand + "\r\n");
                    //=============================
                    batelada[0] = 0.0;
                    batelada[1] = 0.0;
                    batelada[2] = 0.0;
                    for (int tam = 0; tam < 4; tam++) {
                        saidaYor = Degrau(potencialAtivacao(treinamento_or[tam][0], treinamento_or[tam][1], peso_or));
                        error = treinamento_or[tam][2] - saidaYor;
                        if (error != 0) {
                            batelada[0] += x0* error;
                            batelada[1] += treinamento_or[tam][0]* error;
                            batelada[2] += treinamento_or[tam][1]* error;
                        }
                    }
                        peso_or[0] += 0.25*taxa_aprendizado*batelada[0];
                        peso_or[1] += 0.25*taxa_aprendizado*batelada[1];
                        peso_or[2] += 0.25*taxa_aprendizado*batelada[2];
                    gravarArq.printf("OR: w1: " + peso_or[1] + " w2: " + peso_or[2] + " Erro: " + error + " Saida: " + saidaYor + "\r\n");
                    gravarArq.print("Epoca: " + j + " \r\n");
                    gravarArq.printf("+-------------+\r\n");
                }

            }
            arq.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

    }

    public Double Neuronio_OR(Double[] entrada) {
        return Degrau(potencialAtivacao(entrada[0], entrada[1], peso_or));
    }

    public Double Neuronio_AND(Double entrada[]) {
        return Degrau(potencialAtivacao(entrada[0], entrada[1], peso_and));
    }

}
