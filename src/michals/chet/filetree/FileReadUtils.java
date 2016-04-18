package michals.chet.filetree;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.AbstractMap.SimpleEntry;

public class FileReadUtils {
	
	//Read each line of a file, and put each line into a Set of Strings
	public static Set<String> textFileToSet(String fileLocation) throws FileNotFoundException {
		Scanner fileScanner = new Scanner(new File(fileLocation));
		Set<String> returnList = new HashSet<String>();
		while (fileScanner.hasNextLine()){
			returnList.add(fileScanner.nextLine());
		}
		fileScanner.close();
		return returnList;
	}
	
	//Reads a 2 filed Comma Separated file, and puts the contents into a list of of key value pair. 
	public static List<SimpleEntry <String, String>> textFileToListOfKeyValues(String fileLocation) throws FileNotFoundException {
		Scanner fileScanner = new Scanner(new File(fileLocation));
		List<SimpleEntry <String, String>> returnList = new ArrayList<SimpleEntry <String, String>>();
		while (fileScanner.hasNextLine()){
			returnList.add(commaStringToSimpleEntry(fileScanner.nextLine()));
		}
		fileScanner.close();
		return returnList;
	}
	
	public static SimpleEntry <String, String> commaStringToSimpleEntry(String inputString) {
		List<String> tempList = commaStringToList(inputString);
		String key = tempList.get(0);
		String value = tempList.get(1);
		SimpleEntry <String, String> returnValue = new SimpleEntry<String, String>(key, value);
		return returnValue;
	}
	
	public static List<String> commaStringToList(String inputString) {
		return Arrays.asList(inputString.split("\\s*,\\s*"));
	}
}
