package typeAnalyzer;

import java.io.IOException;
import java.util.ArrayList;

import codeGenerator.CodeGenerator;
import lexicalAnalyzer.Token;

public class TypeAnalyzer {
	
	private int elementosEmLI = 0;
	
	private ArrayList<Integer> currentBlockNumberOfVars = new ArrayList<>();
	
	private int currentBlock = 0;
	
	private ArrayList<Token> variaveis = new ArrayList<>();
	
	private CodeGenerator codeGenerator;
	
	public TypeAnalyzer() throws IOException{
		this.codeGenerator = new CodeGenerator("teste");
		this.currentBlockNumberOfVars.add(0);
	}
	
	public void typeAnalisis(String left, int len, Token token, ArrayList<Token> tokensToReduce) {
		try{
		//começo a avaliar o penultimo elemento
		int size = tokensToReduce.size()-1;
		
		//esse codigo só deveria ser usado no início de uma função. na falta delas foi colocado no começo de um bloco como exemplo
		if(left.equals("NB")){
			currentBlock++;
			currentBlockNumberOfVars.add(0);
		}
		
		if(left.equals("B")){
			currentBlockNumberOfVars.remove(currentBlock);
			currentBlock--;
		}
		
		if (left.equals("IDD")) {
			tokensToReduce.get(size-1).kind = Kind.NO_KIND_DEF_;
		}
		
		if(left.equals("T")){
			if(tokensToReduce.get(size-1).word.equals("integer")){
				tokensToReduce.get(size-1).type = 1;
				tokensToReduce.get(size-1).size = 1;
			}
			if(tokensToReduce.get(size-1).word.equals("char")){
				tokensToReduce.get(size-1).type = 2;
				tokensToReduce.get(size-1).size = 1;
			}
			if(tokensToReduce.get(size-1).word.equals("boolean")){
				tokensToReduce.get(size-1).type = 3;
				tokensToReduce.get(size-1).size = 1;
			}
			if(tokensToReduce.get(size-1).word.equals("string")){
				tokensToReduce.get(size-1).type = 4;
				tokensToReduce.get(size-1).size = 1;
			}
		}
		
		if(left.equals("NUM")){
			tokensToReduce.get(size-1).type = 1;
		}
		
		if(left.equals("STR")){
			tokensToReduce.get(size-1).type = 4;
		}
		
		if(left.equals("LI")) {
			if(len==1){
				tokensToReduce.get(size-1).kind = Kind.VAR_;
				int numOfVars = currentBlockNumberOfVars.get(currentBlock);
				tokensToReduce.get(size-1).index = numOfVars + 1;
				elementosEmLI++;
				currentBlockNumberOfVars.set(currentBlock, numOfVars);
			}
			if(len==3){
				Token idd = tokensToReduce.get(size-1);
				tokensToReduce.get(size-2);
				tokensToReduce.get(size-3).kind = idd.kind;
				elementosEmLI++;
				int numOfVars = currentBlockNumberOfVars.get(currentBlock);
				tokensToReduce.get(size-1).index = numOfVars + 1;
				currentBlockNumberOfVars.set(currentBlock, numOfVars);
			}
		}
		
		if(left.equals("DV")) {
			//retiro o ;
			tokensToReduce.remove(size-1);
			Token t = tokensToReduce.remove(size-2);
			//retiro :
			tokensToReduce.remove(size-3);
			//atribuo tipo de t a li
			for(int i = 0; i<elementosEmLI*2-1; i++){
				Token li = tokensToReduce.remove(size-(4+i));
				if(i%2==0){
					li.type = t.type;
					variaveis.add(li);
				}
			}
			//retiro "var"
			tokensToReduce.remove(tokensToReduce.size()-2);
			elementosEmLI = 0;
		}
		
		if(left.equals("IDU")){
			Token idu = tokensToReduce.get(size-1);
			for(Token variavel:variaveis){
				if(variavel.primaryToken == idu.primaryToken && variavel.secondaryToken == idu.secondaryToken){
					tokensToReduce.get(size-1).type = variavel.type;
					tokensToReduce.get(size-1).kind = variavel.kind;
					tokensToReduce.get(size-1).index = variavel.index;
				}
			}
			
		}
		
		
		if(left.equals("E")) {
			if(len == 3){
				
				//remove os dois ultimos, mas mantem o outro para passar o tipo adiante para as demais reduções
				//regra do tipo E --> l simbolo e
				Token e = tokensToReduce.remove(size-1);
				Token simbolo = tokensToReduce.remove(size-2);
				Token l = tokensToReduce.get(size-3);
				
				
				//verifica se os dois operandos tem o mesmo tipo
				if(!CheckTypes(l, e)){
					throw new Exception ("Erro: type mismatch - operação entre tipos diferentes");
				}
				
				//verifica se o tipo é inteiro ou, no caso de ser '+', string
				if(simbolo.word.equals("+")) {
					if(!(CheckTypes(l, 4) || CheckTypes(l, 1))){
						throw new Exception ("Erro: invalid type - operação inválida para esse tipo");
					}
				}
				else{
					if(!CheckTypes(l, 1)){
						throw new Exception ("Erro: invalid type - operação inválida para esse tipo");
					}
				}
				
				//escreve o código para essa redução
				switch(simbolo.word){
				case "+":
					codeGenerator.writeCode("\tADD\n");
					break;
				case "-":
					codeGenerator.writeCode("\tSUB\n");
					break;
				case "*":
					codeGenerator.writeCode("\tMUL\n");
					break;
				case "/":
					codeGenerator.writeCode("\tDIV\n");
					break;
				}
			}
		}
		
		if(left.equals("S")){
			if(len == 4){
				tokensToReduce.remove(size-1);
				Token e = tokensToReduce.remove(size-2);
				tokensToReduce.remove(size-3);
				Token idu = tokensToReduce.get(size-4);
				if(!CheckTypes(e, idu)){
					throw new Exception("Erro: type mismatch - atribuição inválida");
				}
				codeGenerator.writeCode("\tDUP\n\tSTORE_VAR " + idu.index);
			}
		}
		
		if(left.equals("L")){
			Token rightToken = tokensToReduce.get(size-1);
			int index = tokensToReduce.get(size-1).index;
			if(rightToken.word.equals("ID")){
				codeGenerator.writeCode("\tLOAD_VAR " + index + "\n");
			}
			if(rightToken.word.equals("NUM")||rightToken.word.equals("STR")){
				codeGenerator.writeCode("\tLOAD_CONST " + rightToken.secondaryToken);
			}
			
		}
		
		}
		catch(IndexOutOfBoundsException e){
			e.printStackTrace();
		}
		catch(Exception e){
			System.err.println(e.toString());
		}
		
	}
	
	
	public boolean CheckTypes(Token t1, Token t2) {
		if(t1.type == t2.type)
			return true;
		else{
			//verifica se é tipo universal(0) e aceita
			if(t1.type == 0 || t2.type == 0)
				return true;
			else{
				
				if(t1.kind == t2.kind){
					if(t1.kind == Kind.ALIAS_TYPE_) {
						//verifica se o tipo base é o mesmo
					}
					if(t1.kind == Kind.ARRAY_TYPE_) {
						//verifica se o numero de elementos e o tipo deles é o mesmo
					}
					if(t1.kind == Kind.STRUCT_TYPE_) {
						//verifica se os campos são equivalentes em tipo
					}
					
				}
				
				return false;
			}
		}
	}
	
	public boolean CheckTypes(Token t1, int t2){
		if(t1.type == t2)
			return true;
		else{
			//verifica se é tipo universal(0) e aceita
			if(t1.type == 0 || t2 == 0)
				return true;
			else{
				return false;
			}
		}
	}
	
}
