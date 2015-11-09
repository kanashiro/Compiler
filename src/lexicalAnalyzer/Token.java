package lexicalAnalyzer;

import typeAnalyzer.Kind;

public class Token {
	
	//qual palavra foi lida para o token
	//no caso de ID ou símbolo, a word será o nome do ID ou do símbolo
	//no caso de constantes, a word será
	// 	n - numeral
	//  s - string
	//  c - char
	public String word = ""; 
	
	public int primaryToken;
	
	public int secondaryToken;
	
	//AS CLASSES QUE UM TOKEN PODE TER:
	// -4 -> final
	// -3 -> unknown
	// -2 -> simbol
	// -1 -> keyWord
	// 0  -> char
	// 1  -> int
	// 2  -> string
	// 3  -> id
	public int classe;
	
	public Kind kind;
	
	//tipos:
	//1-int
	//2-char
	//3-boolean
	//4-string
	//0-universal
	public int type;
	
	//"size" é o tamanho do espaço ocupado pelo token e 
	//"index" é sua posição entre as variáveis de uma função
	public int size;
	public int index;
	
	public Token(int primary){
		this.primaryToken = primary;
		this.secondaryToken = -1;
		this.classe = -1;
		this.kind = Kind.NO_KIND_DEF_;
		this.size = 0;
		this.index = -1;
	}
	
	public Token(int primary, int secundary, int type){
		this.primaryToken = primary;
		this.secondaryToken = secundary;
		this.classe = type;
		this.kind = Kind.NO_KIND_DEF_;
		this.size = 0;
		this.index = -1;
	}
	
	
	//retorna um token do tipo unknown
	public static Token unknown(){
		return new Token(-1,-1,-3);
	}
	
	//imprime o valor do token primário, seu tipo e, se existir, o token secundário
	//só é usada para testes do analisador sintático, não aparecendo na versão final do compilador
	public void print(){
		switch (this.classe) {
		case (-3):
			System.out.println("primario: " + this.primaryToken + " tipo: unknown");	
			break;
	
		case (-2):
			System.out.println("primario: " + this.primaryToken + " tipo: simbol");
			break;
	
		case (-1):
			System.out.println("primario: " + this.primaryToken + " tipo: keyWord");
			break;
		
		case (0):
			System.out.println("primario: " + this.primaryToken + " secundario: " + this.secondaryToken  + " tipo: char");
			break;
		
		case (1):
			System.out.println("primario: " + this.primaryToken + " secundario: " + this.secondaryToken  + " tipo: int");
			break;
		
		case (2):
			System.out.println("primario: " + this.primaryToken + " secundario: " + this.secondaryToken  + " tipo: string");
			break;
		
		case (3):
			System.out.println("primario: " + this.primaryToken + " secundario: " + this.secondaryToken  + " tipo: id");
			break;
		
		default:
			break;
		}
	}
	
	@Override
	public String toString() {
		return word;
	}
}
