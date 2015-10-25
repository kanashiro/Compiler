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
			//System.out.println("_" + token.word + "_" + token.type);
			action = getAction(state, token);
		
		 if (action > 0) {
			this.syntacticStack.add(0, action);
			token = lexicalAnalyzer.nextToken();
		}else{
			if(action < 0){
				int len = Tables.lengthList.get(action*-1 - 1);
				String left = Tables.leftList.get(action*-1 - 1);  
						
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
		int action = 0;
		 
		for (int i = 0; i < 75; i++) {
			if(left.equals(Tables.listOfSymbols.get(i))){
			   System.out.println("estado:" + state + " simbolo:" + left);
			   action =  Tables.actionList.get(state).get(i);  
			   System.out.println("ação " + action);
			}
		}

		return action;
	}


	private int getAction(int state, Token token) {
		int action = 0;
		 
		for (int i = 0; i < 75; i++) {
			if(token.word.equals(Tables.listOfSymbols.get(i))){
			   System.out.println("estado:" + state + " simbolo:" + token.word);
			   action =  Tables.actionList.get(state).get(i);  
			   System.out.println("ação " + action);
			}
		}

		return action;
	}
	
	
}
