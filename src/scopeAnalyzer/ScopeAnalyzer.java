package scopeAnalyzer;

import java.util.ArrayList;

import lexicalAnalyzer.Token;

public class ScopeAnalyzer {

	private ArrayList<ArrayList<Token>> symbolTable = new ArrayList<ArrayList<Token>>();
	
	private int currentLevel = -1;
	
	public int NewBlock(){
		ArrayList<Token> newLevel = new ArrayList<Token>();
		symbolTable.add(newLevel);
		currentLevel++;
		return currentLevel;
	}
	
	public int EndBlock(){
		currentLevel--;
		return currentLevel;
	}
	
	public void Define(Token token){
		//System.out.println("DEFINE: lvl=" + currentLevel + " elemento:" + token.secondaryToken );
		symbolTable.get(currentLevel).add(token);
		
		
	}
	
	public void Search(int secundaryToken) throws Exception{
		
		for(Token token : symbolTable.get(currentLevel)) {
			if(token.secondaryToken == secundaryToken){
				throw new Exception("Erro de escopo. Já existe um identificador com este nome.");
			}
		}	
	}
	
	public Token Find(int secundaryToken) throws Exception{
		
		for (int j = currentLevel; j >= 0 ; j--) {
			for(Token token : symbolTable.get(j)) {
				if(token.secondaryToken == secundaryToken){
					return token;
				}
			}	
		}
		throw new Exception("Erro de escopo. Identificador não declarado.");
	}
}
