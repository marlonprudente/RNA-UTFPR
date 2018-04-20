/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.utfpr.rna.perceptron;

import java.io.FileWriter;
import java.io.PrintWriter;
import static java.lang.Math.*;
import java.util.Random;

/**
 *
 * @author Marlon Prudente
 */
public class MultiLayerPerceptron {

    private Double taxa_aprendizado;
    private Double x0;
    private int epocas;
    private int w;
    private Double[] pesos;
    private Double[][] treinamento_xor = {{0.0, 0.0, 0.0}, {0.0, 1.0, 1.0}, {1.0, 0.0, 1.0}, {1.0, 1.0, 0.0}};

    public static Double Tanh(Double x) {
        return tanh(x);
    }

    public static Double DerivadaTanh(Double x) {
        Double s = 1.0 / cosh(x);
        return pow(s, 2);
    }

    //Função sigmoidal
    public static Double Sigmoidal(Double x) {
        return (1.0 / (1.0 + pow(E, -1.0 * x)));
    }

    public static Double DerivadaSigmoidal(Double x) {
        Double s = (1.0 / (1.0 + pow(E, -1.0 * x)));
        return (s * (1 - s));
    }

    private Double potencialAtivacao(Double entrada1, Double entrada2, Double pesos[], String neuronio) {
        Double saida = 0.0;
        if(neuronio.equalsIgnoreCase("N1")){
        saida += pesos[0] * x0;
        saida += pesos[1] * entrada1;
        saida += pesos[2] * entrada2;
        }
        if(neuronio.equalsIgnoreCase("N2")){
        saida += pesos[3] * x0;
        saida += pesos[4] * entrada1;
        saida += pesos[5] * entrada2;
        }
        if(neuronio.equalsIgnoreCase("NS")){
        saida += pesos[6] * x0;
        saida += pesos[7] * entrada1;
        saida += pesos[8] * entrada2;
        }
        return saida;
    }

    public MultiLayerPerceptron(Double aprendizado, Integer epocas, Boolean isBatelada) {
        this.x0 = 1.0;
        w = 9;
        this.taxa_aprendizado = aprendizado;
        this.epocas = epocas;
        pesos = new Double[w];

        Random r = new Random();
        for (int i = 0; i < w; i++) {
            pesos[i] = r.nextDouble();
        }

        try {
            FileWriter arq = new FileWriter("logs/saida_mlp.txt");
            PrintWriter gravarArq = new PrintWriter(arq);
            Double[] Y = new Double[2];
            Double[] dwsaida = new Double[3];
            Double[][] dwY = new Double[2][3];
            Double saida;
            Double erro;
            Double eqm = 0.0;
            Double ebp;
            for (int epoca = 0; epoca < this.epocas; epoca++) {

                for (int tam = 0; tam < 4; tam++) {
                    Y[0] = Tanh(potencialAtivacao(treinamento_xor[tam][0], treinamento_xor[tam][1], pesos, "N1"));
                    Y[1] = Tanh(potencialAtivacao(treinamento_xor[tam][0], treinamento_xor[tam][1], pesos, "N2"));
                    saida = Tanh(potencialAtivacao(Y[0], Y[1], pesos, "NS"));
                    erro = treinamento_xor[tam][2] - saida;
                    gravarArq.printf("Erro: " + erro + "\r\n");
                    eqm += pow(erro,2);
                    if(erro > 0.01){
                        //delta
                        ebp = (erro*DerivadaTanh(potencialAtivacao(Y[0], Y[1], pesos, "NS")));
                        //deltaW Neuronio Saída
                        dwsaida[0] = taxa_aprendizado*ebp*x0;
                        dwsaida[1] = taxa_aprendizado*ebp*Y[0];
                        dwsaida[2] = taxa_aprendizado*ebp*Y[1];                        
                        //deltaW Neuronio 1
                        dwY[0][0] =  taxa_aprendizado*ebp*pesos[7]*DerivadaTanh(potencialAtivacao(treinamento_xor[tam][0], treinamento_xor[tam][1], pesos, "N1"))*x0;
                        dwY[0][1] =  taxa_aprendizado*ebp*pesos[7]*DerivadaTanh(potencialAtivacao(treinamento_xor[tam][0], treinamento_xor[tam][1], pesos, "N1"))*treinamento_xor[tam][0];
                        dwY[0][2] =  taxa_aprendizado*ebp*pesos[7]*DerivadaTanh(potencialAtivacao(treinamento_xor[tam][0], treinamento_xor[tam][1], pesos, "N1"))*treinamento_xor[tam][1];                        
                        //deltaW Neuronio 2
                        dwY[1][0] =  taxa_aprendizado*ebp*pesos[8]*DerivadaTanh(potencialAtivacao(treinamento_xor[tam][0], treinamento_xor[tam][1], pesos, "N1"))*x0;
                        dwY[1][1] =  taxa_aprendizado*ebp*pesos[8]*DerivadaTanh(potencialAtivacao(treinamento_xor[tam][0], treinamento_xor[tam][1], pesos, "N1"))*treinamento_xor[tam][0];
                        dwY[1][2] =  taxa_aprendizado*ebp*pesos[8]*DerivadaTanh(potencialAtivacao(treinamento_xor[tam][0], treinamento_xor[tam][1], pesos, "N1"))*treinamento_xor[tam][1];                       
                        
                        //Atualização pesos Neuronio 1
                        pesos[0] += dwY[0][0];
                        pesos[1] += dwY[0][1];
                        pesos[2] += dwY[0][2];
                        //Atualização pesos Neuronio 2
                        pesos[3] += dwY[1][0];
                        pesos[4] += dwY[1][1];
                        pesos[5] += dwY[1][2];
                        //Atualização pesos Neuronio Saída
                        pesos[6] += dwsaida[0];
                        pesos[7] += dwsaida[1];
                        pesos[8] += dwsaida[2];
                    }
                }
                eqm = 0.25*eqm;
                gravarArq.printf("Epoca: " + epoca + " Eqm: " + eqm + "\r\n");
                for(int i = 0; i<w;i++){
                    gravarArq.printf("w" + i + ": " + pesos[i] + " ");                    
                }                
                gravarArq.printf("\r\n");
            }
            arq.close();
        } catch (Exception e) {
            System.out.println("Err: " + e);
        }
        
        
    }
    
    public Double XOR(Double[] entrada){
        Double x1, x2;
        x1 = Tanh(potencialAtivacao(entrada[0], entrada[1], pesos, "N1"));
        x2 = Tanh(potencialAtivacao(entrada[0], entrada[1], pesos, "N2"));
        
        return Tanh(potencialAtivacao(x1, x2, pesos, "NS"));
    }

}
