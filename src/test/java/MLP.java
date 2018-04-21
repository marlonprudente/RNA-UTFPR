/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import com.utfpr.rna.perceptron.MultiLayerPerceptronXOR;

/**
 *
 * @author Marlon Prudente
 */
public class MLP {
    public static void main(String[] args) {
        MultiLayerPerceptronXOR mlp = new MultiLayerPerceptronXOR(0.05, 50000, true);
        Double[] entrada = new Double[2];
        entrada[0] = 0.0;
        entrada[1] = 1.0;
        System.out.println("XOR: " + mlp.XOR(entrada));
    }
    
}
