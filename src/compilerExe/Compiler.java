package compilerExe;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import syntaticAnalyzer.SyntaticAnalyzer;
import syntaticAnalyzer.Tables;

public class Compiler {

	public static void main(String[] args) throws Exception {
		
		Tables.CreateActionTable();
		Tables.CreateReductionTable();
		File program;
		
		Scanner scanner = new Scanner(System.in);
		int option = -1;
		
		while (option != 0) {
			System.out.println("\n\n_______________________");
			System.out.println("Escolha uma opção:");
			System.out.println("1 - programa sem erros");
			System.out.println("2 - erro sintático no início do código");
			System.out.println("3 - erro sintático no fim do código");
			System.out.println("4 - erro de escopo: Variável não declarada");
			System.out.println("5 - programa sem erros. Variável com mesmo nome em escopo diferente");
			System.out.println("6 - erro de escopo: Identificador já existente");
			System.out.println("\n0 - sair");
			option = scanner.nextInt();
			
			if(option==0){
				break;
			}
			// Analizador Sintatico
			String programName = "Resources/Programs/programa"+option+".txt";
			program = new File(programName);
			SyntaticAnalyzer syntaticAnalyzer = new  SyntaticAnalyzer(program);
			
			System.out.println("--------PROGRAMA---------");
			try {
				FileReader arq = new FileReader(programName); 
				BufferedReader lerArq = new BufferedReader(arq); 
				String linha = lerArq.readLine(); 
				while (linha != null)
				{ 
					System.out.printf("%s\n", linha); 
					linha = lerArq.readLine(); 
				} 
				arq.close();
				
					} catch (IOException e) { 
						System.err.printf("Erro na abertura do arquivo: %s.\n", e.getMessage()); 
					}
				
		
			
			System.out.println("-------------------------");
			try {
			
				syntaticAnalyzer.Analyze();
				
			} catch (Exception e) {
				System.err.println(e.toString());
			}
			
		}
		
	
	
		
		
	}

}
