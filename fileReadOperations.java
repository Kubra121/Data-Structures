import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class fileReadOperations {
	
	public String[] readingFile() {
		
		File dir=new File("sport");
		String[] list=dir.list();
		return list;
	}
	
	
	
	public String readingFile(String path) {
		try {
			FileReader fileReader = new FileReader(path);
			String line;
			BufferedReader br = new BufferedReader(fileReader);
			String s = "";
			while ((line = br.readLine()) != null) {
				s += line + " ";
			}
			br.close();
			s = s.toLowerCase();
			return s;
		}
		 catch (Exception e) {
			System.out.println("File not found: " + path);
			return "";
		}
		
	}
}
