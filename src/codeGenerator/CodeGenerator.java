package codeGenerator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CodeGenerator {

	private static int labelNo = 0;
	private File file;
	private BufferedWriter writer;
	public String code = "";
	
	public CodeGenerator(String program) throws IOException {
		String fileName = "Resources/GeneratedCodes/" + program + ".txt";
		file = new File(fileName);
		if(!file.exists())
			file.createNewFile();
		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		writer = new BufferedWriter(fw);
	}
	
	public int newLabel() {
		return labelNo++;
	}
	
	public void writeCode(String code) throws IOException {
		System.out.println(code);
		writer.write(code);
		//this.code += code;
	}
	
	public void closeFile() throws IOException {
		writer.close();
	}
	
}
