package syntaticAnalyzer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import codeGenerator.CodeGenerator;
import scopeAnalyzer.ScopeAnalyzer;
import typeAnalyzer.TypeAnalyzer;
import lexicalAnalyzer.LexicalAnalyzer;
import lexicalAnalyzer.Token;

public class SyntaticAnalyzer {

	// estado final definido como 1000
	private final int FINAL = 1000;

	private ArrayList<Integer> syntacticStack = new ArrayList<Integer>();

	private LexicalAnalyzer lexicalAnalyzer;

	private ScopeAnalyzer scopeAnalyzer;
	
	private TypeAnalyzer typeAnalyzer;
	
	private ArrayList<Token> tokensToReduce = new ArrayList<>();

	public SyntaticAnalyzer(File arq) throws IOException {
		lexicalAnalyzer = new LexicalAnalyzer(arq);
		scopeAnalyzer = new ScopeAnalyzer();
		scopeAnalyzer.NewBlock();
		typeAnalyzer = new TypeAnalyzer();
	}

	// análise sintática
	public void Analyze() {

		// encapsulado em try-catch para poder ler mais de um programa
		// na execução do compiler.main
		try {
			int state = 0;
			int action = 0;
			// adiciona na pilha o estado 0
			this.syntacticStack.add(0, 0);
			Token token = this.lexicalAnalyzer.nextToken();
			
			tokensToReduce.add(token);
			
			int secondToken = 0;

			do {
				// se o token lido é ID, guardamos o valor do seu token
				// secundário que será
				// utilizado em reduções para IDD e IDU
				if (token.word == "ID") {
					secondToken = token.secondaryToken;
				}
				action = getAction(state, token);

				// se a ação é positiva, ou seja, existe apenas uma mudança de
				// estado
				// adiciona o estado na pilha e pega o próxima token
				if (action > 0) {
					this.syntacticStack.add(0, action);
					token = lexicalAnalyzer.nextToken();
					
					tokensToReduce.add(token);
					
					// se a ação é negativa, então existe uma redução
				} else if (action < 0) {

					int len = Tables.lengthList.get(action * -1 - 1);
					String left = Tables.leftList.get(action * -1 - 1);
					

					// remove elementos da pilha de acordo com a tabela auxiliar
					for (int i = 0; i < len; i++) {
						this.syntacticStack.remove(0);
					}

					// empilha a nova ação
					int newAction = getAction(this.syntacticStack.get(0), left);

					// executa a analise de escopo
					scopeAnalyzer.ScopeAnalize(left, token, secondToken);
					typeAnalyzer.typeAnalisis(left, len, token, tokensToReduce);
					
					this.syntacticStack.add(0, newAction);

					// se a ação é zero, então existe um erro sintático
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



	// pega ação da tabela de ação de acordo com o left da tabela auxiliar
	private int getAction(Integer state, String left) {
		int action = 0;

		for (int i = 0; i < Tables.listOfSymbols.size(); i++) {
			if (left.equals(Tables.listOfSymbols.get(i))) {
				action = Tables.actionList.get(state).get(i);
			}
		}

		return action;
	}

	// pega ação da tabela de ação de acordo com o token
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
