package scopeAnalyzer;

import java.util.ArrayList;

import lexicalAnalyzer.Token;

public class ScopeAnalyzer {

	// lista de listas para simular as pilhas das variáveis utilizadas
	private ArrayList<ArrayList<Token>> symbolTable = new ArrayList<ArrayList<Token>>();

	private int currentLevel = -1;

	// de acordo com a regra de redução, pode executar alguma função da analise
	// de escopo
	public void ScopeAnalize(int action, Token token, int seconToken)
			throws Exception {

		// redução em NB
		if (action == -30) {
			NewBlock();
		}

		// fim do bloco
		if (action == -10) {
			EndBlock();
		}

		// IDD, se encontra o token no mesmo nível retorna um erro de escopo
		if (action == -29) {
			token.secondaryToken = seconToken;
			if (Search(token.secondaryToken) != -1) {
				throw new Exception(
						"Erro de escopo. Já existe um identificador com este nome.");
			} else {
				Define(token);
			}

		}

		// IDU, se não encontra o token em nenhum nível retorna um erro de
		// escopo
		if (action == -28) {
			token.secondaryToken = seconToken;
			if (Find(token.secondaryToken) == null) {
				throw new Exception(
						"Erro de escopo. Identificador não declarado.");
			}
		}
	}

	// cria um novo nível na pilha de variáveis e muda o nivel atual
	public int NewBlock() {
		ArrayList<Token> newLevel = new ArrayList<Token>();
		symbolTable.add(newLevel);
		currentLevel++;
		return currentLevel;
	}

	// diminui o nivel atual para simular o fim de um bloco
	public int EndBlock() {
		currentLevel--;
		return currentLevel;
	}

	// adiciona um token na pilha de variaveis
	public void Define(Token token) {
		symbolTable.get(currentLevel).add(token);
	}

	// procura o token no nível atual.
	public int Search(int secundaryToken) throws Exception {
		for (Token token : symbolTable.get(currentLevel)) {
			if (token.secondaryToken == secundaryToken) {
				return secundaryToken;
			}
		}
		return -1;
	}

	// procura o tokem em todos os níveis
	public Token Find(int secundaryToken) throws Exception {

		for (int j = currentLevel; j >= 0; j--) {
			for (Token token : symbolTable.get(j)) {
				if (token.secondaryToken == secundaryToken) {
					return token;
				}
			}
		}

		return null;
	}
}
