/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.utfpr.rna.perceptron;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import static java.lang.Math.*;
import java.util.Random;

/**
 *
* @author Marlon Prudente <marlonoliveira at alunos.utfpr.edu.br>
 */
public class MultiLayerPerceptronXOR {

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
        if (neuronio.equalsIgnoreCase("N1")) {
            saida += pesos[0] * x0;
            saida += pesos[1] * entrada1;
            saida += pesos[2] * entrada2;
        }
        if (neuronio.equalsIgnoreCase("N2")) {
            saida += pesos[3] * x0;
            saida += pesos[4] * entrada1;
            saida += pesos[5] * entrada2;
        }
        if (neuronio.equalsIgnoreCase("NS")) {
            saida += pesos[6] * x0;
            saida += pesos[7] * entrada1;
            saida += pesos[8] * entrada2;
        }
        return saida;
    }

    public MultiLayerPerceptronXOR(Double aprendizado, Integer epocas, Boolean isBatelada) {
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
            FileWriter arq = new FileWriter("output_data/saida_mlp.txt");
            PrintWriter gravarArq = new PrintWriter(arq);
            Double[] Y = new Double[2]; //Saida dos neuronios da camada
            Double[][] dw = new Double[3][3]; // ΔW = n*δ*x  /ou/ ΔW = n*δ*g'(u(w))*x
            Double saida; // g(u(w))
            Double erro; // saida desejada - saida
            Double eqm; //Erro quadrático médio
            Double deltinha; //δ = erro * g'(u(w))
            Double ebp; //Erro backpropagation
            Double u; //u = ∑w*x
            do {
                eqm = 0.0;
                if (!isBatelada) {
                    for (int tam = 0; tam < 4; tam++) {
                        Y[0] = Sigmoidal(potencialAtivacao(treinamento_xor[tam][0], treinamento_xor[tam][1], pesos, "N1"));
                        Y[1] = Sigmoidal(potencialAtivacao(treinamento_xor[tam][0], treinamento_xor[tam][1], pesos, "N2"));
                        saida = Sigmoidal(potencialAtivacao(Y[0], Y[1], pesos, "NS"));
                        erro = treinamento_xor[tam][2] - saida;
                        gravarArq.printf("Erro: " + erro + "\r\n");
                        eqm += pow(erro, 2);
                        if (erro != 0.0) {
                            //deltinha==============================================
                            u = potencialAtivacao(Y[0], Y[1], pesos, "NS");
                            deltinha = erro * DerivadaSigmoidal(u);

                            //deltaW Neuronio 1=====================================                      
                            u = potencialAtivacao(treinamento_xor[tam][0], treinamento_xor[tam][1], pesos, "N1");
                            ebp = deltinha * pesos[7];
                            dw[0][0] = taxa_aprendizado * ebp * DerivadaSigmoidal(u) * x0;
                            dw[0][1] = taxa_aprendizado * ebp * DerivadaSigmoidal(u) * treinamento_xor[tam][0];
                            dw[0][2] = taxa_aprendizado * ebp * DerivadaSigmoidal(u) * treinamento_xor[tam][1];

                            //deltaW Neuronio 2=====================================
                            u = potencialAtivacao(treinamento_xor[tam][0], treinamento_xor[tam][1], pesos, "N2");
                            ebp = deltinha * pesos[8];
                            dw[1][0] = taxa_aprendizado * ebp * DerivadaSigmoidal(u) * x0;
                            dw[1][1] = taxa_aprendizado * ebp * DerivadaSigmoidal(u) * treinamento_xor[tam][0];
                            dw[1][2] = taxa_aprendizado * ebp * DerivadaSigmoidal(u) * treinamento_xor[tam][1];

                            //deltaW Neuronio Saída=================================
                            dw[2][0] = taxa_aprendizado * deltinha * x0;
                            dw[2][1] = taxa_aprendizado * deltinha * Y[0];
                            dw[2][2] = taxa_aprendizado * deltinha * Y[1];

                            //Atualização pesos Neuronio 1
                            pesos[0] += dw[0][0];
                            pesos[1] += dw[0][1];
                            pesos[2] += dw[0][2];
                            //Atualização pesos Neuronio 2
                            pesos[3] += dw[1][0];
                            pesos[4] += dw[1][1];
                            pesos[5] += dw[1][2];
                            //Atualização pesos Neuronio Saída
                            pesos[6] += dw[2][0];
                            pesos[7] += dw[2][1];
                            pesos[8] += dw[2][2];
                        }
                    }

                }
                if (isBatelada) {
                    gravarArq.print("=> Aprendizado BATELADA: >Epoca: " + epocas + " \r\n");
                    dw[0][0] = dw[0][1] = dw[0][2] = dw[1][0] = dw[1][1] = dw[1][2] = dw[2][0] = dw[2][1] = dw[2][2] = 0.0;
                    for (int tam = 0; tam < 4; tam++) {
                        Y[0] = Sigmoidal(potencialAtivacao(treinamento_xor[tam][0], treinamento_xor[tam][1], pesos, "N1"));
                        Y[1] = Sigmoidal(potencialAtivacao(treinamento_xor[tam][0], treinamento_xor[tam][1], pesos, "N2"));
                        saida = Sigmoidal(potencialAtivacao(Y[0], Y[1], pesos, "NS"));
                        erro = treinamento_xor[tam][2] - saida;
                        gravarArq.printf("Erro: " + erro + "\r\n");
                        eqm += pow(erro, 2);
                        if (erro != 0.0) {                            
                            //deltinha==============================================
                            u = potencialAtivacao(Y[0], Y[1], pesos, "NS");
                            deltinha = erro * DerivadaSigmoidal(u);

                            //deltaW Neuronio 1=====================================                      
                            u = potencialAtivacao(treinamento_xor[tam][0], treinamento_xor[tam][1], pesos, "N1");
                            ebp = deltinha * pesos[7];
                            dw[0][0] +=  ebp * DerivadaSigmoidal(u) * x0;
                            dw[0][1] +=  ebp * DerivadaSigmoidal(u) * treinamento_xor[tam][0];
                            dw[0][2] +=  ebp * DerivadaSigmoidal(u) * treinamento_xor[tam][1];

                            //deltaW Neuronio 2=====================================
                            u = potencialAtivacao(treinamento_xor[tam][0], treinamento_xor[tam][1], pesos, "N2");
                            ebp = deltinha * pesos[8];
                            dw[1][0] +=  ebp * DerivadaSigmoidal(u) * x0;
                            dw[1][1] +=  ebp * DerivadaSigmoidal(u) * treinamento_xor[tam][0];
                            dw[1][2] +=  ebp * DerivadaSigmoidal(u) * treinamento_xor[tam][1];

                            //deltaW Neuronio Saída=================================
                            dw[2][0] +=  deltinha * x0;
                            dw[2][1] +=  deltinha * Y[0];
                            dw[2][2] +=  deltinha * Y[1];
                        }                        
                    }
                            //Atualização pesos Neuronio 1
                            pesos[0] += 0.25*dw[0][0];
                            pesos[1] += 0.25*dw[0][1];
                            pesos[2] += 0.25*dw[0][2];
                            //Atualização pesos Neuronio 2
                            pesos[3] += 0.25*dw[1][0];
                            pesos[4] += 0.25*dw[1][1];
                            pesos[5] += 0.25*dw[1][2];
                            //Atualização pesos Neuronio Saída
                            pesos[6] += 0.25*dw[2][0];
                            pesos[7] += 0.25*dw[2][1];
                            pesos[8] += 0.25*dw[2][2];

                }
                eqm = 0.25 * eqm;
                epocas--;
                gravarArq.printf("Eqm: " + eqm + "\r\n");
                for (int i = 0; i < w; i++) {
                    gravarArq.printf("w" + i + ": " + pesos[i] + ", ");
                }
                gravarArq.printf("\r\n");
            } while (epocas != 0 && eqm > 0.001);

            arq.close();

        } catch (IOException e) {
            System.out.println("Err: " + e);
        }

    }

    public Double XOR(Double[] entrada) {
        Double x1, x2;
        x1 = Sigmoidal(potencialAtivacao(entrada[0], entrada[1], pesos, "N1"));
        x2 = Sigmoidal(potencialAtivacao(entrada[0], entrada[1], pesos, "N2"));
        return Sigmoidal(potencialAtivacao(x1, x2, pesos, "NS"));
    }

    //Funçõ de ativação: Degrau
    private Double Degrau(Double x) {
        if (x > 0) {
            return 1.0;
        } else {
            return 0.0;
        }
    }

}
