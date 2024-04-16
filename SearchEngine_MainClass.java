import java.io.*;
import java.sql.Time;
import java.util.*;

public class SearchEngine_MainClass{
	//public static File folder = new File("C:\\Users\\Toshiba\\eclipse-workspace\\SearchEngine\\src\\sport");
	//static String temp = "";
	private static fileReadOperations fro = new fileReadOperations();
	private static String[] filesArray1;
	private static String[] searchArray = fro.readingFile("search.txt").split(" ");
	
	public static void main(String[] args) throws IOException {
				
		
		filesArray1 = fro.readingFile();
		//for(int i = 0; i < filesArray1.length;i++) {
			//System.out.println(filesArray1[i]);
		//}
		String[][] wordsArray = new String[filesArray1.length][];
		for (int i = 0; i < filesArray1.length; i++) {
			
			String fileContent = fro.readingFile("sport\\"+filesArray1[i]);
			String[] splitted = splittedDelimiter(fileContent);
			splitted = removingStopWords(splitted);
			
			for(int j = 0; j < splitted.length; j++) {
				wordsArray[i] = splitted;//stop word ve noktalamadan ayrılmış tüm kelimeler wordsArray'de				
			}
		}
		//HashTable<String,ArrayList<arrayNode>> hashTable = new HashTable<>(0.5,"SSF","LP");
		//HashTable<String,ArrayList<arrayNode>> hashTable = new HashTable<>(0.5,"SSF","DH");
		//HashTable<String,ArrayList<arrayNode>> hashTable = new HashTable<>(0.5,"PAF","LP");
		//HashTable<String,ArrayList<arrayNode>> hashTable = new HashTable<>(0.5,"PAF","DH");
		
		//HashTable<String,ArrayList<arrayNode>> hashTable = new HashTable<>(0.8,"SSF","LP");
		//HashTable<String,ArrayList<arrayNode>> hashTable = new HashTable<>(0.8,"SSF","DH");
		HashTable<String,ArrayList<arrayNode>> hashTable = new HashTable<>(0.8,"PAF","LP");
		//HashTable<String,ArrayList<arrayNode>> hashTable = new HashTable<>(0.8,"PAF","DH");
		long initial_hashTable1 = System.nanoTime();
		addingToHashtable(wordsArray, hashTable);
		long final_hashTable1 = System.nanoTime();
		
		search(hashTable);
		//display(hashTable);
		
		long HT1 = final_hashTable1 - initial_hashTable1;
		
		System.out.println("Time: " + HT1);
		System.out.println("Collision count: " + hashTable.getnumberOfCollisions());
		//inputSearch(getInput(), hashTable);
	}
	
	public static void inputSearch(String[] arr, HashTable<String,ArrayList<arrayNode>> hashTable) {
		int maxSum = 0;
		
		int index = 0;
		String resFileName = "";
		String tempFileName = "";
		
		for(int i = 0; i < filesArray1.length; i++) {
			int sum = 0;
			for(int j = 0; j < arr.length; j++) {
				index = hashTable.findIndex(arr[j]);
				if(hashTable.getTableEntry(index) != null) {
					sum += getWordCount(hashTable.getTableEntry(index).getValue(), i);
					tempFileName = indexToFileName(i);
				}
			}
			if(sum > maxSum) {
				maxSum = sum;
				resFileName = tempFileName;
			}
			
			
		}
		System.out.println(resFileName.equals("") ? "There is no such file" :
			"The most relevant txt is: " + resFileName);
	}
	
	static String indexToFileName(int fileIndex) {
		String fileStr = "";
		if(fileIndex<9)
			fileStr = "00";
		else if (fileIndex<99)
			fileStr = "0";
		fileStr += (fileIndex+1) + ".txt";
		return fileStr;
	}
	
	static int getWordCount(ArrayList<arrayNode> array, int fileIndex) {
		String fileStr = indexToFileName(fileIndex);
		
		for (arrayNode n : array) {
			if(n.getFile().equals(fileStr))
				return n.getCount();
				
		}
		
		return 0;
		
	}
	
	public static String[] getInput() {
		Scanner scn = new Scanner(System.in);
		String[] arr = {};
		while (arr.length != 3) {
			System.out.print("Please enter 3 words: ");
			String s = scn.nextLine();
			arr = s.split(" ");
		}
		return arr;
	}
	public static void search(HashTable<String,ArrayList<arrayNode>> hashTable) {
		long min = 999999999;
		long max = 0;
		long sum = 0;
		long time = 0;
		for(int i = 0; i < searchArray.length; i++) {
			long startTime = System.nanoTime();
			hashTable.findIndex(searchArray[i]);
			time = System.nanoTime() - startTime;
			if(time > max) {
				max = time;
			}
			if(time < min) {
				min = time;
			}
			sum += time;
		}
		System.out.println("time:" + time + "\nmax: " + max + "\nmin: " + min );
	}
	
	public static void performanceMatrix() {
		double[] loadFactorArray = new double[2];loadFactorArray[0] = 0.5; loadFactorArray[1] = 0.8;
		String[] hashFunctionArray = new String[2]; hashFunctionArray[0] = "SSF"; hashFunctionArray[1] = "PAF";
		String[] collusionHandlingFunction = new String[2]; collusionHandlingFunction[0] = "LP";collusionHandlingFunction[1] = "DH";
		
		HashTable<String,ArrayList<arrayNode>> hashTable = new HashTable<>(0.5,"SSF","LP");
		
	}
	
	public static HashTable<String, ArrayList<arrayNode>> addingToHashtable(String[][] wordsArray, HashTable<String,ArrayList<arrayNode>> hashTable) {
		for(int i = 0; i < wordsArray.length; i++) {
			for(int j = 0; j < wordsArray[i].length; j++) {
				if(wordsArray[i][j] != "*" && wordsArray[i][j] != "") {
					TableEntrys<String,ArrayList<arrayNode>> oldTableEntry;
					oldTableEntry = hashTable.add(wordsArray[i][j], createValue(filesArray1[i]));
					if(oldTableEntry != null){
						hashTable.add(wordsArray[i][j], increaseValue(filesArray1[i], oldTableEntry));
					}
					
				}
				
			}
		}
		return hashTable;
	}
	
	
	public static void display(HashTable<String,ArrayList<arrayNode>> hashTable) {
		TableEntrys<String,ArrayList<arrayNode>> tblEntry;
		for(int i = 0; i < hashTable.getSize(); i++) {
			
			tblEntry = hashTable.getTableEntry(i);
			if(tblEntry != null) {
				System.out.print(tblEntry.getKey() + " ");
				for(int j = 0; j < tblEntry.getValue().size(); j++) {
					System.out.print(tblEntry.getValue().get(j).getFile() + " ");
					System.out.print(tblEntry.getValue().get(j).getCount() + " ");
				}
				System.out.println();
			}
			
				
		}
	}
	
	public static ArrayList<arrayNode> createValue(String txtName) {
		 
		ArrayList<arrayNode> arr = new ArrayList<>();
		arrayNode a_n = new arrayNode(txtName, 1);
		arr.add(a_n); 
		return arr;
		
	}
	public static ArrayList<arrayNode> increaseValue(String txtName, TableEntrys<String,ArrayList<arrayNode>> oldTableEntry) {
		ArrayList<arrayNode> arrList = oldTableEntry.getValue();
		
		int size = arrList.size();
		boolean flag = false;
		for(int i = 0; i < size; i++) {
			if(arrList.get(i).getFile().equals(txtName)) {
				int count = arrList.get(i).getCount() + 1;
				arrList.get(i).setCount(count);
				flag = true;
				break;
			}			
		}	
		if(!flag) {
			arrayNode a_n = new arrayNode(txtName, 1);
			arrList.add(a_n); 
		}
		
		return arrList;
	}
	
	public static String[] removingStopWords(String[] arr) throws IOException {
		fileReadOperations fro = new fileReadOperations();		
		String stopWordsString = fro.readingFile("stop_words_en.txt");
		String[] stopWordsArray = splittedDelimiter(stopWordsString);		
		for(int i = 0; i < stopWordsArray.length; i++) {
			for(int j = 0; j < arr.length; j++) {
				if(stopWordsArray[i].equals(arr[j])) {
					arr[j] = "*";
				}
			}			
		}
		return arr;
	}
	
	public static String[] splittedDelimiter(String fileContent) {		
		String DELIMITERS = "[-+=" +

		        " " +        //space

		        "\r\n " +    //carriage return line fit

				"1234567890" + //numbers

				"’'\"" +       // apostrophe

				"(){}<>\\[\\]" + // brackets

				":" +        // colon

				"," +        // comma

				"‒–—―" +     // dashes

				"…" +        // ellipsis

				"!" +        // exclamation mark

				"." +        // full stop/period

				"«»" +       // guillemets

				"-‐" +       // hyphen

				"?" +        // question mark

				"‘’“”" +     // quotation marks

				";" +        // semicolon

				"/" +        // slash/stroke

				"⁄" +        // solidus

				"␠" +        // space?   

				"·" +        // interpunct

				"&" +        // ampersand

				"@" +        // at sign

				"*" +        // asterisk

				"\\" +       // backslash

				"•" +        // bullet

				"^" +        // caret

				"¤¢$€£¥₩₪" + // currency

				"†‡" +       // dagger

				"°" +        // degree

				"¡" +        // inverted exclamation point

				"¿" +        // inverted question mark

				"¬" +        // negation

				"#" +        // number sign (hashtag)

				"№" +        // numero sign ()

				"%‰‱" +      // percent and related signs

				"¶" +        // pilcrow

				"′" +        // prime

				"§" +        // section sign

				"~" +        // tilde/swung dash

				"¨" +        // umlaut/diaeresis

				"_" +        // underscore/understrike

				"|¦" +       // vertical/pipe/broken bar

				"⁂" +        // asterism

				"☞" +        // index/fist

				"∴" +        // therefore sign

				"‽" +        // interrobang

				"※" +          // reference mark

		        "]";
		
		String[] splitted = fileContent.split(DELIMITERS);
		return splitted;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
	
	

