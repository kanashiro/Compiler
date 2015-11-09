package compilerExe;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import jxl.read.biff.BiffException;
import syntaticAnalyzer.SyntaticAnalyzer;
import syntaticAnalyzer.Tables;

public class Compiler {

	public static void main(String[] args) throws BiffException, IOException {

		// Colocar Tabelas de A��o e Auxiliar em mem�ria para tornar o programa
		// mais r�pido
		Tables.CreateActionTable();
		Tables.CreateReductionTable();
		File program;

		// Menu com op��es para escolher um programa v�lido
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
		int option = -1;

		while (option != 0) {
			PrintMenu();
			option = scanner.nextInt();

			if (option == 0) {
				break;
			}

			if(option <0 || option > 10){
				System.out.println("Op��o Inv�lida!");
				continue;
			}
			
			String programName = "Resources/Programs/program" + option + ".txt";
			program = new File(programName);

			// Cria��o do Analisador sint�tico
			SyntaticAnalyzer syntaticAnalyzer = new SyntaticAnalyzer(program);

			// Imprimir o programa lido
			ReadProgram(program);
			
			
			// Fazer an�lise sint�tica
			syntaticAnalyzer.Analyze();

		}

	}

	public static void ReadProgram(File programName){
		System.out.println("--------PROGRAMA---------");
		try {
			FileReader arq = new FileReader(programName);
			BufferedReader lerArq = new BufferedReader(arq);
			String linha = lerArq.readLine();
			while (linha != null) {
				System.out.printf("%s\n", linha);
				linha = lerArq.readLine();
			}
			arq.close();

		} catch (IOException e) {
			System.err.printf("Erro na abertura do arquivo: %s.\n",
					e.getMessage());
		}

		System.out.println("-------------------------");
	}
	
	public static void PrintMenu(){
		System.out.println("\n\n_______________________");
		System.out.println("Escolha uma op��o:");
		System.out.println("1 - programa sem erros");
		System.out.println("2 - erro sint�tico no in�cio do c�digo");
		System.out.println("3 - erro sint�tico no fim do c�digo");
		System.out.println("4 - erro de escopo: Vari�vel n�o declarada");
		System.out
				.println("5 - programa sem erros. Vari�vel com mesmo nome em escopo diferente");
		System.out
				.println("6 - erro de escopo: Identificador j� existente");
		System.out.println("7 - erro de tipo. atribui��o incorreta");
		System.out.println("8 - erro de tipo. opera��es com tipos diferentes");
		System.out.println("9 - erro de tipo. opera��o incorreta com tipo string");
		System.out
		.println("10 - programa sem erros. soma entre strings � correta");
		System.out.println("\n0 - sair");
	}
}
