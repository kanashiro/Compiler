package compilerExe;

import java.io.File;

import syntaticAnalyzer.SyntaticAnalyzer;
import syntaticAnalyzer.Tables;

public class Compiler {

	public static void main(String[] args) throws Exception {
		File program = new File("Resources/Programs/programa1.txt");
		
		// Analizador Sintatico
		
		Tables.CreateActionTable();
		Tables.CreateReductionTable();
		
		SyntaticAnalyzer syntaticAnalyzer = new  SyntaticAnalyzer(program);
		syntaticAnalyzer.Analyze();
	}

}
