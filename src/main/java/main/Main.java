/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import com.utfpr.rna.adalaine.Adalaine;
import com.utfpr.rna.adalaine.Adalaine.Treinamento;
import com.utfpr.rna.perceptron.Perceptron;
import static java.lang.Math.*;

/**
 *
 * @author marlo
 */

public class Main {
    public static void main(String[] args) {
        //Perceptron p = new Perceptron(0.1, Treinamento.Padrao);
        //Double[] entrada = new Double[2];
        //entrada[0] = 0.0;
        //entrada[1] = 1.0;
        //System.out.println("OR: " + p.Neuronio_OR(entrada) + "/ AND: " + p.Neuronio_AND(entrada));
        
        //Funcao(1.0*(2*Math.PI));
        Adalaine a = new Adalaine(0.1, Treinamento.Batelada);
        System.out.println("Resultado: " + a.Resultado(0.588, -0.809, 2.513)); //-3.265
        
        

    }
    public static void Funcao(Double z){
        
        Double saida = -Math.PI+0.565*sin(z)+2.657*cos(z)+0.674*z;
        System.out.println("z: " + z);
        System.out.println("f1(z): " + sin(z));
        System.out.println("f2(z): " + cos(z));
        System.out.println("f3(z): " + z);
        System.out.println("f(z): " + saida);        
}

}
