/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import com.utfpr.rna.perceptron.MultiLayerPerceptron;

/**
 *
 * @author marlo
 */
public class MLP {
    public static void main(String[] args) {
        MultiLayerPerceptron mlp = new MultiLayerPerceptron(0.05, 100, true);
    }
    
}
