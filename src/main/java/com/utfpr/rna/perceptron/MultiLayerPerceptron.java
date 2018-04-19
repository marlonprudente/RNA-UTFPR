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
    private int epoca;
    private int w;
    private Double[] pesos;    
    private Double[][] treinamento_xor = {{0.0, 0.0, 0.0},{0.0, 1.0, 1.0}, {1.0, 0.0, 1.0}, {1.0,1.0,0.0}};    
    
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
        saida += pesos[0] * x0;
        saida += pesos[1] * entrada1;
        saida += pesos[2] * entrada2;
        return saida;
    }
    
    
    public MultiLayerPerceptron(Double taxa_aprendizado, Integer epocas, Boolean isBatelada) {
        this.x0 = 1.0;
        w = 9;
        this.taxa_aprendizado = taxa_aprendizado;        
        this.epoca = epocas; 
        pesos = new Double[w];
        
        Random r = new Random();
        for (int i = 0; i < w; i++) {
            pesos[i] = r.nextDouble();            
        }
        
        try{
            FileWriter arq = new FileWriter("logs/saida_mlp.txt");
            PrintWriter gravarArq = new PrintWriter(arq);
            
        }catch(Exception e){
            
        }
    }

}
