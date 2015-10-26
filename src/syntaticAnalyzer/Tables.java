package syntaticAnalyzer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class Tables {

	// par�metros da tabela de a��o
	public static ArrayList<String> listOfSymbols = new ArrayList<String>();

	// tabela de a��o, onde o indice na lista representa o n�mero do estado
	public static ArrayList<ArrayList<Integer>> actionList = new ArrayList<ArrayList<Integer>>();

	// Tabela auxiliar, onde o �ndice nas listas representam o n�mero da regra
	// ao qual se aplicam
	public static ArrayList<Integer> lengthList = new ArrayList<Integer>();
	public static ArrayList<String> leftList = new ArrayList<String>();

	// criar tabela de a��o em mem�ria
	public static void CreateActionTable() throws BiffException, IOException {

		// leitura do arquivo excel onde se encontra a tabela de a��o
		Workbook arq = Workbook.getWorkbook(new File(
				"Resources/ActionTable/ActionSimpleTable.xls"));
		Sheet table = arq.getSheet(0);

		int collums = table.getColumns();
		int lines = table.getRows();

		// cria a lista de par�metros da tabela de a��o
		for (int i = 1; i < collums; i++) {
			listOfSymbols.add(table.getCell(i, 0).getContents());
		}

		// cria a tabela de a��o dentro de uma lista de listas
		for (int j = 1; j < lines; j++) {

			int valueOfRule = 0;
			ArrayList<Integer> actionLine = new ArrayList<Integer>();

			for (int i = 1; i < collums; i++) {
				String rule = table.getCell(i, j).getContents();

				if (rule.length() == 0) {
					valueOfRule = 0;
				} else if (rule.contains("r")) {
					rule = rule.substring(1);
					valueOfRule = Integer.parseInt(rule) * -1;
				} else if (rule.contains("a")) {
					valueOfRule = 1000;
				} else {
					rule = rule.replace('s', '0');
					valueOfRule = Integer.parseInt(rule);
				}
				actionLine.add(valueOfRule);
			}

			actionList.add(actionLine);
		}

	}

	// cria a tabela auxiliar em duas listas
	public static void CreateReductionTable() throws BiffException, IOException {

		// le do arquivo excel
		Workbook arq = Workbook.getWorkbook(new File(
				"Resources/ActionTable/ReductionSimpleTable.xls"));
		Sheet table = arq.getSheet(0);
		int lines = table.getRows();

		// cria a tabela auxiliar
		for (int i = 1; i < lines; i++) {
			lengthList.add(Integer.parseInt(table.getCell(1, i).getContents()));
			leftList.add(table.getCell(2, i).getContents());
		}
	}
}
