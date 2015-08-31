package compilerExe;

import java.io.File;
import java.io.IOException;

import lexicalAnalyzer.LexicalAnalyzer;

public class Compiler {

	public static void main(String[] args) throws IOException {
		File program = new File("Resources/Programs/programa1.txt");
		
		// Analizador Léxico
		
		LexicalAnalyzer lexical = new LexicalAnalyzer(program);
		lexical.Analyzer();

	}

}
