/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import com.utfpr.rna.perceptron.MultiLayerPerceptron;

/**
 *
 * @author Marlon Prudente
 */
public class MLP {
    public static void main(String[] args) {
        MultiLayerPerceptron mlp = new MultiLayerPerceptron(0.05, 50000, true);
        Double[] entrada = new Double[2];
        entrada[0] = 1.0;
        entrada[1] = 1.0;
        System.out.println("XOR: " + mlp.XOR(entrada));
    }
    
}
