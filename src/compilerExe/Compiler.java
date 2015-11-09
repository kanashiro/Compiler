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

		// Colocar Tabelas de Ação e Auxiliar em memória para tornar o programa
		// mais rápido
		Tables.CreateActionTable();
		Tables.CreateReductionTable();
		File program;

		// Menu com opções para escolher um programa válido
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
				System.out.println("Opção Inválida!");
				continue;
			}
			
			String programName = "Resources/Programs/program" + option + ".txt";
			program = new File(programName);

			// Criação do Analisador sintático
			SyntaticAnalyzer syntaticAnalyzer = new SyntaticAnalyzer(program);

			// Imprimir o programa lido
			ReadProgram(program);
			
			
			// Fazer análise sintática
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
		System.out.println("Escolha uma opção:");
		System.out.println("1 - programa sem erros");
		System.out.println("2 - erro sintático no início do código");
		System.out.println("3 - erro sintático no fim do código");
		System.out.println("4 - erro de escopo: Variável não declarada");
		System.out
				.println("5 - programa sem erros. Variável com mesmo nome em escopo diferente");
		System.out
				.println("6 - erro de escopo: Identificador já existente");
		System.out.println("7 - erro de tipo. atribuição incorreta");
		System.out.println("8 - erro de tipo. operações com tipos diferentes");
		System.out.println("9 - erro de tipo. operação incorreta com tipo string");
		System.out
		.println("10 - programa sem erros. soma entre strings é correta");
		System.out.println("\n0 - sair");
	}
}
