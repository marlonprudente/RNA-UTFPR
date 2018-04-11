/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import com.utfpr.rna.adalaine.Adalaine;
import com.utfpr.rna.adalaine.ArquivoTreino;
import com.utfpr.rna.perceptron.Perceptron;
import static java.lang.Math.*;
import java.util.Scanner;
import java.util.InputMismatchException;
import java.util.Locale;

/**
 *
 * @author marlo
 */
public class Main {

    public static void main(String[] args) {
        Boolean isBatelada = false;
        Double taxaAprendizagem;
        Integer epocas;
        Perceptron perceptron = null;
        Integer padroesAdaline = 0;
        ArquivoTreino at;
        Adalaine adaline = null;
        Double[] entradas = new Double[2];
        Scanner s = new Scanner(System.in).useLocale(Locale.US);

        System.out.println("|==============================|");
        System.out.println("| [1] - Treinar Perceptron     |");
        System.out.println("| [2] - Gerar Padrões Adaline  |");
        System.out.println("| [3] - Treinar Adaline        |");
        System.out.println("|==============================|");
        System.out.println("| [4] - Usar Perceptron AND    |");
        System.out.println("| [5] - Usar Perceptron OR     |");
        System.out.println("| [6] - Usar Adaline           |");
        System.out.println("| [0] - Sair                   |");
        System.out.println("|==============================|");
        System.out.print("Opção: ");

        Integer OP = s.nextInt();
        try {
            while (OP != 0) {
                switch (OP) {
                    case 1:
                        System.out.println("Digite a taxa de aprendizagem: ");
                        taxaAprendizagem = s.nextDouble();
                        System.out.println("Digite a quatidade de épocas:");
                        epocas = s.nextInt();
                        System.out.println("Batelada? ");
                        String opcao = s.next();
                        isBatelada = opcao.equalsIgnoreCase("sim");
                        System.out.println("Treinando Perceptron...");
                        perceptron = new Perceptron(taxaAprendizagem, epocas, isBatelada);
                        System.out.println("Perceptron treinado!");
                        break;
                    case 2:
                        System.out.println("Digite a quantidade de padrões para treinar o Adaline: ");
                        padroesAdaline = s.nextInt();
                        System.out.println("Gerando padrões de treino do adaline...");
                        at = new ArquivoTreino(padroesAdaline);
                        System.out.println("Arquivo gerado!");
                        break;
                    case 3:
                        System.out.println("Digite a taxa de aprendizagem: ");
                        taxaAprendizagem = s.nextDouble();
                        System.out.println("Digite a quatidade de épocas (Quantas vezes irá treinar):");
                        epocas = s.nextInt();
                        System.out.println("Batelada? ");
                        opcao = s.next();
                        isBatelada = opcao.equalsIgnoreCase("sim");
                        if (padroesAdaline == 0) {
                            System.out.println("Digite a quantidade de padrões (dados de treinamento) para treinar o Adaline: ");
                            padroesAdaline = s.nextInt();
                            at = new ArquivoTreino(padroesAdaline);
                        }
                        System.out.println("Treinando Adaline...");
                        adaline = new Adalaine(taxaAprendizagem, epocas, isBatelada, padroesAdaline);
                        System.out.println("Adaline treinado!");
                        break;
                    case 4:
                        System.out.println("Digite as entradas AND: ");
                        entradas[0] = s.nextDouble();
                        entradas[1] = s.nextDouble();
                        if (perceptron != null) {
                            System.out.println("Saída: " + perceptron.Neuronio_AND(entradas));
                        } else {
                            System.out.println("É necessário treinar o Perceptron Primeiro!!");
                        }
                        break;
                    case 5:
                        System.out.println("Digite as entradas OR: ");
                        entradas[0] = s.nextDouble();
                        entradas[1] = s.nextDouble();
                        if (perceptron != null) {
                            System.out.println("Saída: " + perceptron.Neuronio_OR(entradas));
                        } else {
                            System.out.println("É necessário treinar o Perceptron Primeiro!!");
                        }
                        break;
                    case 6:
                        System.out.println("Digite a entrada Z: ");
                        entradas[0] = s.nextDouble();
                        if (adaline != null) {
                            System.out.println("Saída Adaline: " + adaline.Saida(entradas[0]));
                        } else {
                            System.out.println("É necessário treinar o Adaline Primeiro!!");
                        }
                        break;
                    default:
                        System.out.println("Opção inválida!");
                        break;
                }
                System.out.println("|==============================|");
                System.out.println("| [1] - Treinar Perceptron     |");
                System.out.println("| [2] - Gerar Padrões Adaline  |");
                System.out.println("| [3] - Treinar Adaline        |");
                System.out.println("|==============================|");
                System.out.println("| [4] - Usar Perceptron AND    |");
                System.out.println("| [5] - Usar Perceptron OR     |");
                System.out.println("| [6] - Usar Adaline           |");
                System.out.println("| [0] - Sair                   |");
                System.out.println("|==============================|");
                System.out.print("Opção: ");
                OP = s.nextInt();

            }
            System.out.println("\nIsto fica feliz em ser útil!...");

        } catch (InputMismatchException e) {
            System.out.println("" + e);
        }
    }

    public static void Funcao(Double z) {

        Double saida = -Math.PI + 0.565 * sin(z) + 2.657 * cos(z) + 0.674 * z;
        System.out.println("z: " + z);
        System.out.println("f1(z): " + sin(z));
        System.out.println("f2(z): " + cos(z));
        System.out.println("f3(z): " + z);
        System.out.println("f(z): " + saida);
    }

}
