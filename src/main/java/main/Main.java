/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import com.utfpr.rna.perceptron.Perceptron;
import com.utfpr.rna.perceptron.Perceptron.Treinamento;

/**
 *
 * @author marlo
 */
public class Main {
    public static void main(String[] args) {
        Perceptron p = new Perceptron(0.1, Treinamento.Padrao);
        Double[] entrada = new Double[2];
        entrada[0] = 0.0;
        entrada[1] = 1.0;
        System.out.println("OR: " + p.Neuronio_OR(entrada) + "/ AND: " + p.Neuronio_AND(entrada));

    }

}
