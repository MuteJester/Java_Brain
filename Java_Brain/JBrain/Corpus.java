package JBrain;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.net.URL;
import java.util.ArrayList;

public class Corpus {
	public ArrayList<String> Words;
	public Corpus() {
		Words = new ArrayList<String>();
	}
	public void Render_Txt_File(String FilePath) throws IOException {
	     try {
			BufferedReader  Reader = new BufferedReader(new FileReader(FilePath));
			String line;
			 while ((line = Reader.readLine()) != null) {
			        String[] values = line.split("[^a-z0-9]");
			        for(int i = 0;i<values.length;i++) {
			        	if(!values[i].equals("")) {
			        		Words.add(values[i]);
			        	}
			        }
			    }
			 Reader.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void Render_Txt_File(File File) throws IOException {
	     try {
			BufferedReader  Reader = new BufferedReader(new FileReader(File));
			String line;
			 while ((line = Reader.readLine()) != null) {
			        String[] values = line.split("[^a-z0-9]");
			        for(int i = 0;i<values.length;i++) {
			        	if(!values[i].equals("")) {
			        		Words.add(values[i]);
			        	}
			        }
			    }
			 Reader.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void Print_Words() {
		for(String word : Words) {
			System.out.println(word);
		}
	}
	
}
