package syntaticAnalyzer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import scopeAnalyzer.ScopeAnalyzer;
import lexicalAnalyzer.LexicalAnalyzer;
import lexicalAnalyzer.Token;

public class SyntaticAnalyzer {

	// estado final definido como 1000
	private final int FINAL = 1000;

	private ArrayList<Integer> syntacticStack = new ArrayList<Integer>();

	private LexicalAnalyzer lexicalAnalyzer;

	private ScopeAnalyzer scopeAnalyzer;

	public SyntaticAnalyzer(File arq) throws IOException {
		lexicalAnalyzer = new LexicalAnalyzer(arq);
		scopeAnalyzer = new ScopeAnalyzer();
		scopeAnalyzer.NewBlock();
	}

	// an�lise sint�tica
	public void Analyze() {

		// encapsulado em try-catch para poder ler mais de um programa
		// na execu��o do compiler.main
		try {
			int state = 0;
			int action = 0;
			// adiciona na pilha o estado 0
			this.syntacticStack.add(0, 0);
			Token token = this.lexicalAnalyzer.nextToken();

			int seconToken = 0;

			do {
				if (token.word == "ID") {
					seconToken = token.secondaryToken;
				}
				action = getAction(state, token);

				if (action > 0) {
					this.syntacticStack.add(0, action);
					token = lexicalAnalyzer.nextToken();
				} else if (action < 0) {

					int len = Tables.lengthList.get(action * -1 - 1);
					String left = Tables.leftList.get(action * -1 - 1);

					for (int i = 0; i < len; i++) {
						this.syntacticStack.remove(0);
					}

					int newAction = getAction(this.syntacticStack.get(0), left);

					scopeAnalise(action, token, seconToken);
					
					this.syntacticStack.add(0, newAction);

				} else {
					throw new Exception("Syntax Error");
				}

				state = this.syntacticStack.get(0);

			} while (state != FINAL);

			System.out.println("Programa compilado com sucesso!");
		} catch (Exception e) {
			System.err.println(e.toString());
		}

	}

	// de acordo com a regra de redu��o, pode executar alguma fun��o da analise de escopo
	private void scopeAnalise(int action, Token token, int seconToken) throws Exception{

		// redu��o em NB
		if (action == -30) {
			scopeAnalyzer.NewBlock();
		}

		// fim do bloco
		if (action == -10) {
			scopeAnalyzer.EndBlock();
		}
		
		// IDD
		if (action == -29) {
			token.secondaryToken = seconToken;
			scopeAnalyzer.Search(token.secondaryToken);
			scopeAnalyzer.Define(token);
		}

		// IDU
		if (action == -28) {
			token.secondaryToken = seconToken;
			scopeAnalyzer.Find(token.secondaryToken);
		}
	}
	
	// pega a��o da tabela de a��o de acordo com o left da tabela auxiliar
	private int getAction(Integer state, String left) {
		int action = 0;

		for (int i = 0; i < Tables.listOfSymbols.size(); i++) {
			if (left.equals(Tables.listOfSymbols.get(i))) {
				action = Tables.actionList.get(state).get(i);
			}
		}

		return action;
	}

	// pega a��o da tabela de a��o de acordo com o token
	private int getAction(int state, Token token) {
		int action = 0;

		for (int i = 0; i < Tables.listOfSymbols.size(); i++) {
			if (token.word.equals(Tables.listOfSymbols.get(i))) {
				action = Tables.actionList.get(state).get(i);
			}
		}

		return action;
	}

}
