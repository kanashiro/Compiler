package syntaticAnalyzer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class Tables {

	public static ArrayList<String> listOfSymbols = new ArrayList<String>();

	public static ArrayList<ArrayList<Integer>> actionList = new ArrayList<ArrayList<Integer>>();

	
	
	public static void CreateActionTable() throws BiffException, IOException {
		actionList.add(new ArrayList<Integer>());
		
		Workbook arq = Workbook.getWorkbook(new File(
				"Resources/ActionTable/ActionTable.xls"));
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

	public static void main(String[] args) throws IOException, BiffException {

		CreateActionTable();
	
		
		for (int i = 0; i < 75; i++) {
			System.out.println(i+1 + "  ---  " + actionList.get(3).get(i));
		}
	}

}
