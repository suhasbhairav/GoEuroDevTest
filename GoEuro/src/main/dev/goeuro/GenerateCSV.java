package main.dev.goeuro;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;


public class GenerateCSV {

	PrintWriter writer;
	String filename;
	
	public GenerateCSV(String filename) throws UnsupportedEncodingException, FileNotFoundException{
		this.filename = filename;
		writer = new PrintWriter(this.filename, "UTF-8");
		writer.close();
	}
	/**
	 * Write values to file
	 * @param values
	 * @throws FileNotFoundException
	 */
	
	public void writeToFile(Object[] values) throws FileNotFoundException{
		writer = new PrintWriter(new FileOutputStream(new File(filename), true));
		StringBuilder builder = new StringBuilder();
		for(Object value: values){
			builder.append(String.valueOf(value));
			builder.append(",");
		}
		String line = builder.length()>0?builder.substring(0, builder.length()-1): "";
		
		writer.println(line);
		writer.close();	
	}

}
