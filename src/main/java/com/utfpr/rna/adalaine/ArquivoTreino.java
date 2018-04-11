/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.utfpr.rna.adalaine;

import java.io.FileWriter;
import java.io.PrintWriter;
import static java.lang.Math.*;

/**
 *
 * @author Marlon Prudente
 */
public class ArquivoTreino {

    public ArquivoTreino(Integer qtTreino) {
        Double z;
        try {
            FileWriter arq = new FileWriter("treino_adaline.txt");
            PrintWriter gravarArq = new PrintWriter(arq);
           
            for(Double i = 0.0;i<qtTreino;i++){
                z = (i/qtTreino.doubleValue())*(2*Math.PI);
                gravarArq.print(z + " " + sin(z) + " "+ cos(z) + " "+ z + " " + Funcao(z) + "\r\n");
            }
            arq.close();
            
            
        } catch (Exception e) {
            System.out.println("" + e);
        }
    
    }
    
    public static Double Funcao(Double z){
    return -Math.PI+0.565*sin(z)+2.657*cos(z)+0.674*z;
}
}




