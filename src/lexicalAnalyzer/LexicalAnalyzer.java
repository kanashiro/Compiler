package lexicalAnalyzer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.sun.corba.se.impl.io.ValueUtility;

public class LexicalAnalyzer {

	private static String[] names = {};

	private static String[] constants = {};

	private char nextChar = ' ';

	private InputStreamReader program;

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
		this.program = new InputStreamReader(in);
	}

	public void Analyzer() throws IOException {
		Token token;

		do {
			token = nextToken();
			token.print();
		} while (token.type != -3);

	}

	public char readChar() throws IOException {
		char caracter = (char) (this.program.read());

		return caracter;
	}

	public Token nextToken() throws IOException {
		Token token;
		while (Character.isSpaceChar(this.nextChar)) {
			// ignorar todos os espa�os at� chegar no pr�ximo token
			this.nextChar = readChar();
		}

		// testando se � letra
		if (Character.isLetter(this.nextChar)) {
			String text = "";

			do {
				text += this.nextChar;
				this.nextChar = readChar();
			} while (Character.isLetterOrDigit(this.nextChar)
					|| this.nextChar == '_');
			text += '\0';

			token = searchKeyWord(text);
		} else if (Character.isDigit(this.nextChar)) {
			String numeral = "";

			do {
				numeral += this.nextChar;
				this.nextChar = readChar();
			} while (Character.isDigit(this.nextChar));
			numeral += '\0';

			token = new Token(keyWords.length - 3, searchConst(numeral), 1);
		} else if (this.nextChar == '"') {
			String string = "";

			do {
				string += this.nextChar;
				this.nextChar = readChar();
			} while (this.nextChar != '"');
			string += '"';

			token = new Token(keyWords.length - 3, searchConst(string), 2);
		} else if (this.nextChar == '\'') {
			this.nextChar = readChar();
			token = new Token(keyWords.length - 4,
					searchConst(Character.toString(this.nextChar)), 0);
			// pular ' e ler o pr�ximo
			this.nextChar = readChar();
			this.nextChar = readChar();
		} else {
			token = searchSimbol(Character.toString(this.nextChar));
		}

		return token;
	}

	public Token searchKeyWord(String name) {
		int i = 0;

		for (i = 0; i < keyWords.length; i++) {
			if (keyWords[i] == name) {
				return new Token(i);
			}
		}

		return new Token(keyWords.length - 1, searchName(name), 3);
	}

	public Token searchSimbol(String name) {
		int i = 0;

		for (i = 0; i < simbols.length; i++) {
			if (simbols[i] == name) {
				return new Token(i, -1, -2);
			}
		}

		return Token.unknown();
	}

	public int searchName(String name) {
		int i = 0;

		for (i = 0; i < names.length; i++) {
			if (names[i] == name) {
				return i;
			}
		}

		i = names.length;
		names[i] = name;
		return i;
	}

	public int searchConst(String constant) {
		int i = 0;

		for (i = 0; i < constants.length; i++) {
			if (names[i] == constant) {
				return i;
			}
		}

		i = constants.length;
		constants[i] = constant;
		return i;
	}

}
