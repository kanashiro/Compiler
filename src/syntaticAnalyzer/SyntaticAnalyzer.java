package syntaticAnalyzer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import jdk.internal.org.objectweb.asm.tree.TryCatchBlockNode;
import scopeAnalyzer.ScopeAnalyzer;
import lexicalAnalyzer.LexicalAnalyzer;
import lexicalAnalyzer.Token;

public class SyntaticAnalyzer {

	private final int FINAL = 1000;

	private ArrayList<Integer> syntacticStack = new ArrayList<Integer>();

	private LexicalAnalyzer lexicalAnalyzer;

	private ScopeAnalyzer scopeAnalyzer;

	public SyntaticAnalyzer(File arq) throws IOException {
		lexicalAnalyzer = new LexicalAnalyzer(arq);
		scopeAnalyzer = new ScopeAnalyzer();
		scopeAnalyzer.NewBlock();
	}

	public void Analyze() {
	
		try {
			int state = 0;
			int action = 0;
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
				} else {
					if (action < 0) {
						int len = Tables.lengthList.get(action * -1 - 1);
						String left = Tables.leftList.get(action * -1 - 1);

						for (int i = 0; i < len; i++) {
							this.syntacticStack.remove(0);
						}

						int newAction = getAction(this.syntacticStack.get(0), left);

						if (action == -30) {
							//System.out.println("------>newBlock<--------");
							scopeAnalyzer.NewBlock();
						}

						if (action == -10) {
							//System.out.println("------>endBlock<--------");
							scopeAnalyzer.EndBlock();
						}
						if (action == -29) {
							token.secondaryToken = seconToken;
							//System.out.println(">>IDD>>" + left + " | "
							//		+ token.secondaryToken);
							scopeAnalyzer.Search(token.secondaryToken);
							scopeAnalyzer.Define(token);
						}

						if (action == -28) {
							token.secondaryToken = seconToken;
							//System.out.println(">>IDU>>" + left + " | "
							//		+ token.secondaryToken);
							scopeAnalyzer.Find(token.secondaryToken);
						}

						this.syntacticStack.add(0, newAction);

					} else {
						throw new Exception("Syntax Error");
					}
				}

				state = this.syntacticStack.get(0);

			} while (state != FINAL);

			System.out.println("Programa compilado com sucesso!");
		} catch (Exception e) {
			System.err.println(e.toString());
		}
		
		
	}

	private int getAction(Integer state, String left) {
		int action = 0;

		for (int i = 0; i < Tables.listOfSymbols.size(); i++) {
			if (left.equals(Tables.listOfSymbols.get(i))) {
				// System.out.println("reducao: " + "estado:" + state
				// +" simbolo:" + left);
				action = Tables.actionList.get(state).get(i);
				//System.out.println("ação " + action);
			}
		}

		return action;
	}

	private int getAction(int state, Token token) {
		int action = 0;

		for (int i = 0; i < Tables.listOfSymbols.size(); i++) {
			if (token.word.equals(Tables.listOfSymbols.get(i))) {
				// System.out.println("empilhamento: " + "estado:" + state +
				// " simbolo:" + token.word);
				action = Tables.actionList.get(state).get(i);
				//System.out.println("ação " + action);
			}
		}

		return action;
	}

}
