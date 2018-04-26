/*
 * Arquivo desenvolvido por aluno da UTFPR - Curitiba
 * Bacharel em Engenharia de Computação
 * Licença pública - Todos os direitos reservados.
 */
package com.utfpr.rna.perceptron;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.PrintWriter;
import static java.lang.Math.*;
import java.util.Random;

/**
 *
 * @author Marlon Prudente <marlonoliveira at alunos.utfpr.edu.br>
 */
public class MultiLayerPerceptronIRIS {

    private Double taxa_aprendizado;
    private Integer neuronios_hiden;
    private Double x0;
    private int epocas;
    private int w;
    private Double[][] pesos_hiden;
    private Double[] pesos_saida;
    private Double[][] matriz_treino;

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

    private Double potencialAtivacao(Double[] entrada, Double[] pesos) {
        
        Double saida = 0.0;
        saida += pesos[0] * x0;
        
        for(int i = 1; i< pesos.length;i++){        
        saida += pesos[i] * entrada[i - 1];
        }

        
        return saida;
    }

    public MultiLayerPerceptronIRIS(Double aprendizado, Integer epocas, Boolean isBatelada, Integer neuronios_hiden) throws IOException {

        this.x0 = 1.0;        
        pesos_hiden = new Double[neuronios_hiden][5];
        pesos_saida = new Double[neuronios_hiden];
        this.taxa_aprendizado = aprendizado;
        this.epocas = epocas;
        this.neuronios_hiden = neuronios_hiden;
        BufferedReader br = new BufferedReader(new FileReader("input_data/IRISdataset.txt"));
        FileWriter arq = new FileWriter("output_data/saida_perceptron.txt");
        PrintWriter gravarArq = new PrintWriter(arq);
        //System.out.println("Linhas: " + qtdLinha);
        matriz_treino = new Double[150][5];
        String[] treino;
        String linha;
        int i = 0;
        while (br.ready()) {
            linha = br.readLine();
            treino = linha.split(",");
            for (int j = 0; j < 5; j++) {
                treino[j] = treino[j].trim();
                System.out.print("" + treino[j] + " ");
                if (treino[j].equalsIgnoreCase("Iris-setosa")) {
                    matriz_treino[i][j] = 0.3;
                } else if (treino[j].equalsIgnoreCase("Iris-versicolor")) {
                    matriz_treino[i][j] = 0.6;
                } else if (treino[j].equalsIgnoreCase("Iris-virginica")) {
                    matriz_treino[i][j] = 0.9;
                } else {
                    matriz_treino[i][j] = Double.parseDouble(treino[j]);
                }
            }
            System.out.println("");
            i++;
        }        
        br.close();
        Double[] Y = new Double[neuronios_hiden];
        Double[][] dw_hiden = new Double[neuronios_hiden][5]; // ΔW = n*δ*x  /ou/ ΔW = n*δ*g'(u(w))*x
        Double[] dw_saida = new Double[neuronios_hiden + 1];
        Double saida; // g(u(w))
        Double erro; // saida desejada - saida
        Double eqm; //Erro quadrático médio
        Double deltinha; //δ = erro * g'(u(w))
        Double ebp; //Erro backpropagation
        Double u; //u = ∑w*x
        do {
                eqm = 0.0;
                if (!isBatelada) {
                    for (int tam = 0; tam < 150; tam++){ 
                        for(int n = 0;n<neuronios_hiden;n++){
                            Y[n] = Sigmoidal(potencialAtivacao(matriz_treino[tam], pesos_hiden[n]));
                        }
                        saida = Sigmoidal(potencialAtivacao(Y, pesos_saida));
                        erro = matriz_treino[tam][4] - saida;
                        gravarArq.printf("Erro: " + erro + "\r\n");
                        eqm += pow(erro, 2);
                        if (erro != 0.0) {
                            //deltinha==============================================
                            u = potencialAtivacao(Y, pesos_saida);
                            deltinha = erro * DerivadaSigmoidal(u);

                            //deltaW Neuronios_Hiden===================================== 
                            for(int k = 0;k<neuronios_hiden;k++){
                                u = potencialAtivacao(matriz_treino[tam], pesos_hiden[k]);
                                ebp = deltinha * pesos_saida[k+1];
                                dw_hiden[k][0] = taxa_aprendizado * ebp * DerivadaSigmoidal(u) * x0;
                                for(int l = 0; l< 4;l++){
                                    dw_hiden[k][l+1] = taxa_aprendizado * ebp * DerivadaSigmoidal(u) * matriz_treino[tam][l];
                                }
                            } 
                            //deltaW Neuronio Saída=================================
                            dw_saida[0] = taxa_aprendizado * deltinha * x0;
                            for(int k = 1; k<neuronios_hiden + 1;k++){
                                dw_saida[k] = taxa_aprendizado * deltinha * Y[k];
                            }
                            //Atualização pesos Neuronios_Hiden
                            for(int k = 0; k < neuronios_hiden;k++){
                                for(int l = 0;l<neuronios_hiden;l++){
                                    pesos_hiden[k][l] +=dw_hiden[k][l];
                                }
                            }
                            //Atualização pesos Neuronio Saída
                            for(int k = 0; k<neuronios_hiden + 1;k++){
                                pesos_saida[k] +=dw_saida[k];
                            }
                        }
                    }

                }
                eqm = 0.25 * eqm;
                epocas--;
                gravarArq.printf("Eqm: " + eqm + "\r\n");
                gravarArq.printf("\r\n");
            } while (epocas != 0 && eqm > 0.001);
    }

    public void ImprimirMatrizTreino() {
        for (Double[] matriz_treino1 : this.matriz_treino) {
            for (Double matriz_treino11 : matriz_treino1) {
                System.out.print(matriz_treino11 + " ");
            }
            System.out.println("");
        }
    }

}
