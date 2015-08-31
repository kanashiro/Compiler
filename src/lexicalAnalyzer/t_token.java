package lexicalAnalyzer;

public enum t_token {

	//palavras reservadas
	ARRAY, BOOLEAN, BREAK, CHAR, CONTINUE, DO, ELSE, FALSE, FUNCTION, IF, INTEGER, OF, RETURN, STRING, STRUCT, TRUE, TYPE, VAR, WHILE,
	//1       2        3    4      5        6   7     8      9        10    11      12   13      14     15     16   17    18     19
	
	//simbolos
	COLON, SEMI_COLON, COMMA, EQUALS, LEFT_SQUARE, RIGHT_SQUARE, LEFT_BRACES, RIGHT_BRACES, LEFT_PARENTHESIS, RIGHT_PARENTHESIS, AND,
	//20         21    22       23          24             25          26             27              28             29           30
	OR, LESS_THAN, GREATER_THAN, LESS_OR_EQUAL, GREATER_OR_EQUAL, NOT_EQUAL, EQUAL_EQUAL, PLUS, PLUS_PLUS, MINUS, MINUS_MINUS, TIMES,
	//31     32             33            34              35          36          37      38        39       40        41        42
	DIVIDE, DOT, NOT,
	// 43   44   45
	
	//tokens regulares
	CHARACTER, NUMERAL, STRINGVAL, ID,
	//46         47       48       49
	
	//token desconhecido
	UNKNOWN;
	//50
}
