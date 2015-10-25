package syntaticAnalyzer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class Tables {

	public static ArrayList<String> listOfSymbols = new ArrayList<String>();

	public static ArrayList<ArrayList<Integer>> actionList = new ArrayList<ArrayList<Integer>>();
	
	public static ArrayList<Integer> lengthList = new ArrayList<Integer>();

	public static ArrayList<String> leftList = new ArrayList<String>();
	
	
	public static void CreateActionTable() throws BiffException, IOException {
		
		Workbook arq = Workbook.getWorkbook(new File(
				"Resources/ActionTable/ActionTable4.xls"));
		Sheet table = arq.getSheet(0);

		int collums = table.getColumns();
		int lines = table.getRows();

		for (int i = 1; i < collums; i++) {

			listOfSymbols.add(table.getCell(i, 0).getContents());
		}
		
		for (int j = 1; j < lines; j++) {
			
			int valueOfRule = 0;
			ArrayList<Integer> actionLine = new ArrayList<Integer>();
			
			for (int i = 1; i < collums; i++) {
				String rule = table.getCell(i, j).getContents();
				
				if(rule.length() == 0){
					valueOfRule = 0;
				}else{
			
					if(rule.contains("r")){
						rule = rule.substring(1);
						valueOfRule = Integer.parseInt(rule)*-1;
					}else{
						rule = rule.replace('s', '0');	
						if(!rule.contains("a")){
							valueOfRule = Integer.parseInt(rule);
						}else{
							valueOfRule = 1000;
						}
						
					}
				}
					
				actionLine.add(valueOfRule);	
			}
			
			actionList.add(actionLine);
		}

	}
	
	public static void CreateReductionTable() throws BiffException, IOException {
		
		Workbook arq = Workbook.getWorkbook(new File(
				"Resources/ActionTable/ReductionTable4.xls"));
		Sheet table = arq.getSheet(0);
		int lines = table.getRows();
		
		for (int i = 1; i < lines; i++) {
		
			lengthList.add(Integer.parseInt(table.getCell(1, i).getContents()));
			leftList.add(table.getCell(2, i).getContents());
		}
	}
}
