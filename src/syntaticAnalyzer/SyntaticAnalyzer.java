package syntaticAnalyzer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import lexicalAnalyzer.LexicalAnalyzer;
import lexicalAnalyzer.Token;

public class SyntaticAnalyzer {
	
	private final int FINAL = 1000;
	
	private ArrayList<Integer> syntacticStack = new ArrayList<Integer>();
	
	private LexicalAnalyzer lexicalAnalyzer;
	
		public SyntaticAnalyzer(File arq) throws IOException {
			lexicalAnalyzer = new LexicalAnalyzer(arq);
		}
	
	
	public void Analyze() throws Exception{
		int state = 0;
		int action = 0;
		this.syntacticStack.add(0, 0);
		Token token = this.lexicalAnalyzer.nextToken();
		
		do {
	
			action = getAction(state, token);
		
		 if (action > 0) {
			this.syntacticStack.add(0, action);
			token = lexicalAnalyzer.nextToken();
		}else{
		
			int len = 0;
			String left = "";  
			if(action < 0){
				for (int i = 0;  i < len; i++) {
					this.syntacticStack.remove(0);					
				}
				int newAction = getAction(this.syntacticStack.get(0), left);
				this.syntacticStack.add(0, newAction);
			}else{
				throw new Exception("Syntax Error");
			}
		}
		 
		 state = this.syntacticStack.get(0);
		 
		} while (state != FINAL);
	}


	private int getAction(Integer state, String left) {
		System.out.println("aqui");
		return 0;
	}


	private int getAction(int state, Token token) {
		int action = 0;
		 System.out.println(token.word);
		for (int i = 0; i < 75; i++) {
			if(token.word.equals(Tables.listOfSymbols.get(i))){
			   System.out.println(state + " " + i);
			   action =  Tables.actionList.get(state).get(i);  
			   System.out.println("action " + action);
			}
		}

		return action;
	}
	
	
}
