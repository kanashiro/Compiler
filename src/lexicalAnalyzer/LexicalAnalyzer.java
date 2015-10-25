package lexicalAnalyzer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import com.sun.corba.se.impl.io.ValueUtility;

public class LexicalAnalyzer {

	private static ArrayList<String> names = new ArrayList<String>();

	private static ArrayList<String> constants = new ArrayList<String>();

	private char nextChar = ' ';

	private BufferedReader program;

	private static final String[] keyWords = {

			// palavras reservadas
			"array", "boolean", "break", "char", "continue", "do", "else",
			"false", "function", "if", "integer", "of", "return", "string",
			"struct", "true", "type", "var", "while",

			// tokens regulares
			"character", "numeral", "stringval", "id" };

	private static final String[] simbols = {

			// simbolos
			":", ";", ",", "=", "{", "}", "[", "]", "(", ")", "&&", "||", "<",
			">", "<=", ">=", "!=", "==", "+", "++", "-", "--", "*", "/", ".",
			"!" };

	public LexicalAnalyzer(File arq) throws IOException {

		FileInputStream in = new FileInputStream(arq);
		program = new BufferedReader(new InputStreamReader(in));
	}

	public void Analyzer() throws IOException {
		Token token = nextToken();

		do{
			
			token.print();
			token = nextToken();
		} while (token.type != -3);

	}

	public char readChar() throws IOException {
		int value ;
		do {
			value = this.program.read();	
		} while (value == 10 || value == 13);
		
		char caracter = (char) (value);
		
		return caracter;
	}

	public Token nextToken() throws IOException {
		Token token;
		while (Character.isSpaceChar(this.nextChar)) {
			// ignorar todos os espaços até chegar no próximo token
			this.nextChar = readChar();
		}
		
		String text = "";
		
		// testando se é letra
		if (Character.isLetter(this.nextChar)) {
			

			do {
				text += this.nextChar;
				this.nextChar = readChar();
			} while (Character.isLetterOrDigit(this.nextChar)
					|| this.nextChar == '_');
			
			System.out.println("word: " + text);
			token = searchKeyWord(text);
		} else if (Character.isDigit(this.nextChar)) {
			String numeral = "";

			do {
				numeral += this.nextChar;
				this.nextChar = readChar();
			} while (Character.isDigit(this.nextChar));
			
			System.out.println("numeral: " + numeral);
			token = new Token(keyWords.length - 3, searchConst(numeral), 1);
		} else if (this.nextChar == '"') {
			String string = "";

			do {
				string += this.nextChar;
				this.nextChar = readChar();
			} while (this.nextChar != '"');
			string += '"';
			System.out.println("strinval: " + string);
			token = new Token(keyWords.length - 3, searchConst(string), 2);
		} else if (this.nextChar == '\'') {
			this.nextChar = readChar();
			System.out.println("char: " + this.nextChar);
			token = new Token(keyWords.length - 4,
					searchConst(Character.toString(this.nextChar)), 0);
			// pular ' e ler o próximo
			this.nextChar = readChar();
			this.nextChar = readChar();
		} else {
			System.out.println("simbol: " + this.nextChar);
			token = searchSimbol(Character.toString(this.nextChar));
			this.nextChar = readChar();
		}

		token.word = text;
		return token;
	}

	public Token searchKeyWord(String name) {
		int i = 0;

		for (i = 0; i < keyWords.length; i++) {
			if (keyWords[i].equals(name)) {
				return new Token(i, -1, -1);
			}
		}

		return new Token(keyWords.length - 1, searchName(name), 3);
	}

	public Token searchSimbol(String name) {
		int i = 0;

		for (i = 0; i < simbols.length; i++) {
			if (simbols[i].equals(name)) {
				return new Token(i, -1, -2);
			}
		}

		return Token.unknown();
	}

	public int searchName(String name) {
		int i = 0;

		for (i = 0; i < names.size(); i++) {
			if (names.get(i).equals(name)) {
				return i;
			}
		}

		names.add(name);
		return names.size();
	}

	public int searchConst(String constant) {
		int i = 0;

		for (i = 0; i < constants.size(); i++) {
			if (constants.get(i).endsWith(constant)) {
				return i;
			}
		}

		constants.add(constant);
		return constants.size();
	}

}
