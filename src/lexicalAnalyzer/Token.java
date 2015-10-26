package lexicalAnalyzer;

public class Token {
	
	//qual palavra foi lida para o token
	//no caso de ID ou s�mbolo, a word ser� o nome do ID ou do s�mbolo
	//no caso de constantes, a word ser�
	// 	n - numeral
	//  s - string
	//  c - char
	public String word = ""; 
	
	public int primaryToken;
	
	public int secondaryToken;
	
	//OS TIPOS QUE UM TOKEN PODE TER:
	// -4 -> final
	// -3 -> unknown
	// -2 -> simbol
	// -1 -> keyWord
	// 0  -> char
	// 1  -> int
	// 2  -> string
	// 3  -> id
	public int type;
	
	public Token(int primary){
		this.primaryToken = primary;
		this.secondaryToken = -1;
		this.type = -1;
	}
	
	public Token(int primary, int secundary, int type){
		this.primaryToken = primary;
		this.secondaryToken = secundary;
		this.type = type;
	}
	
	
	//retorna um token do tipo unknown
	public static Token unknown(){
		return new Token(-1,-1,-3);
	}
	
	//imprime o valor do token prim�rio, seu tipo e, se existir, o token secund�rio
	//s� � usada para testes do analisador sint�tico, n�o aparecendo na vers�o final do compilador
	public void print(){
		switch (this.type) {
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
}
