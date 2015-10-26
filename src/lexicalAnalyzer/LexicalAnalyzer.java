package lexicalAnalyzer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class LexicalAnalyzer {

	//lista com o nome de todos os tokens do tipo ID que aparecerem no programa
	private static ArrayList<String> names = new ArrayList<String>();

	//lista com as constantes (string, char e numerais) que aparecerem no programa
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
			"!" , "$end", "NB"};

	
	
	public LexicalAnalyzer(File arq) throws IOException {

		FileInputStream in = new FileInputStream(arq);
		program = new BufferedReader(new InputStreamReader(in));
	}

	/**
	 * Enquanto n�o chega ao fim do arquivo, l� e imprime o pr�ximo token.
	 * m�todo usado apenas para testar o analisador l�xico, n�o entra no
	 * compilador final.
	 * @throws IOException
	 */
	public void Analyzer() throws IOException {
		Token token = nextToken();

		do{
			
			token.print();
			token = nextToken();
		} while (token.type != -3);

	}

	/**
	 * L� o pr�ximo caracter do arquivo armazenado
	 * em "program", ignorando os caracteres de new line
	 * @return o caracter lido
	 * @throws IOException
	 */
	public char readChar() throws IOException {
		int value ;
		//ignora caracter de new line
		do {
			value = this.program.read();	
		} while (value == 10 || value == 13);
		
		char caracter = (char) (value);
		
		return caracter;
	}

	/**
	 * Retorna o pr�ximo token presente no arquivo do programa.
	 * L� os caracteres do arquivo at� identificar o fim do token.
	 * Classifica o token de acordo com o que foi lido - palavra reservada,
	 * ID, fim do arquivo,...
	 * @return o token lido
	 * @throws IOException
	 */
	public Token nextToken() throws IOException {
		Token token;
		while (Character.isSpaceChar(this.nextChar)) {
			// ignorar todos os espa�os at� chegar no pr�ximo token
			this.nextChar = readChar();
		}
		
		String text = "";
		
		//se o token for o de fim do arquivo
		if(this.nextChar == '$'){
			token = new Token(-1,-1,-4);
			token.word = "$end";
			return token;
		}
		
		// testando se o primeiro char � letra
		if (Character.isLetter(this.nextChar)) {
			

			do {
				text += this.nextChar;
				this.nextChar = readChar();
			} while (Character.isLetterOrDigit(this.nextChar)
					|| this.nextChar == '_');
			

			//busco na lista de key words o valor lido para obter o token
			token = searchKeyWord(text);
			
			//testando se � n�mero
		} else if (Character.isDigit(this.nextChar)) {
			String numeral = "";

			do {
				numeral += this.nextChar;
				this.nextChar = readChar();
			} while (Character.isDigit(this.nextChar));
			
			
			token = new Token(keyWords.length - 3, searchConst(numeral), 1);
			
			//testa se � uma string
		} else if (this.nextChar == '"') {
			String string = "";

			do {
				string += this.nextChar;
				this.nextChar = readChar();
			} while (this.nextChar != '"');
			string += '"';
			token = new Token(keyWords.length - 3, searchConst(string), 2);
			
			//testa se � um caracter
		} else if (this.nextChar == '\'') {
			this.nextChar = readChar();
			token = new Token(keyWords.length - 4,
					searchConst(Character.toString(this.nextChar)), 0);
			// pular ' e ler o pr�ximo
			this.nextChar = readChar();
			this.nextChar = readChar();
			
			//testa se � um s�mbolo
		} else {
			token = searchSimbol(Character.toString(this.nextChar));
			this.nextChar = readChar();
		}

		setTokenWord(token, text);
		
		return token;
	}
	
	/**
	 * Avalia o tipo de token recebido, escolhendo seu token word a partir disso
	 * @param token o token a ser avaliado
	 * @param text o texto lido para o token no nextToken
	 */
	public void setTokenWord(Token token, String text) {
		//se o token � id ou s�mbolo, seu word seguir� sendo id ou s�mbolo
		if(token.type == 3 || token.type == -2){
			text = token.word;
		}
		
		//se for um caracter, o word ser� 'c'
		if(token.type == 0){
			text = "c";
		}
		
		//se for um n�mero, o word ser� 'n'
		if(token.type == 1){
			text = "n";
		}
		
		//se for uma string, o word ser� 's'
		if(token.type == 2){
			text = "s";
		}
			
		token.word = text;
	}

	/**
	 * Busca "name" na lista de keywords.
	 * Se encontra, retorna um token representando a keyword.
	 * Se n�o, retorna um token do tipo ID cujo valor � name.
	 * @param name o nome que deve ser procurado na lista de key words
	 * @return o token que representa o valor recebido, seja key word ou ID
	 */
	public Token searchKeyWord(String name) {
		int i = 0;

		for (i = 0; i < keyWords.length; i++) {
			if (keyWords[i].equals(name)) {
				return new Token(i, -1, -1);
			}
		}
		
        Token token = new Token(keyWords.length - 1, searchName(name), 3);
        token.word = "ID";
		return token;
	}

	/**
	 * Busca "name" na lista de simbolos.
	 * Se encontra, retorna um token representando o simbolo encontrado.
	 * Se n�o, retorna um token do tipo unknown.
	 * @param name o nome do simbolo que deve ser procurado
	 * @return o token do simbolo recebido ou unknown.
	 */
	public Token searchSimbol(String name) {
		int i = 0;

		for (i = 0; i < simbols.length; i++) {
			if (simbols[i].equals(name)) {
				Token token = new Token(i, -1, -2);
				token.word = name;
				return token;
			}
		}

		return Token.unknown();
	}

	/**
	 * Recebe o nome de um token do tipo ID para procur�-lo na lista
	 * de nomes de IDs.
	 * Se encontra, retorna o token secundario do ID recebido.
	 * Se n�o, acrescenta o novo ID na lista de nomes e retorna o token secund�rio
	 * atribu�do a esse novo ID.
	 * @param name o nome do ID a ser procurado
	 * @return o token secund�rio do ID recebido.
	 */
	public int searchName(String name) {
		int i = 0;
        for (i = 0; i < names.size(); i++) {
			if (names.get(i).equals(name)) {
				return i+1;
			}
		}

		names.add(name);
		return names.size();
	}

	/**
	 * Recebe o nome de um token que representa uma constante (numeral, string, char) para procur�-lo na lista
	 * de constantes.
	 * Se encontra, retorna o token secundario da constante recebida.
	 * Se n�o, acrescenta a nova constante na lista de constantes e retorna o token secund�rio
	 * atribu�do a ela.
	 * @param constant o valor da constante a ser procurada
	 * @return o token secund�rio da constante recebida
	 */
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
