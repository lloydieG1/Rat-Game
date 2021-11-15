import java.io.IOException;
import java.net.*;
import java.io.*;
public class dailyMessage{  
	public static String getURLstring(String args[]) throws IOException {
		
		URL test = new URL ("http://cswebcat.swansea.ac.uk/puzzle");
		BufferedReader in = new BufferedReader(
		new InputStreamReader(test.openStream()));
		
		String inputLine;
		if ((inputLine = in.readLine()) != null)
			return inputLine;
		else return "something ain't right";
	}
	
	public static String translateURLstring(String args[]) throws IOException {
		char[] cipher = getURLstring(null).toCharArray(); 
		char[] translated = new char[cipher.length];
		String convert;
		for(int i = 1; i <= cipher.length; ++i){
			int selectedChar = (int) cipher[i-1];
			if ((selectedChar > 90) || (selectedChar < 65)) {
				translated[i] = cipher[i-1];
			}
			
			else if (i % 2 == 0) {
				selectedChar = ((int) cipher[i-1]);
				selectedChar += i;
				if (selectedChar > 90) {
					selectedChar = (int) cipher[i-1] + (i) - 26;
				}
				translated[i-1] += (char) selectedChar;	
				
			} else if (i % 2 != 0) {
				selectedChar = ((int) cipher[i-1]);
				selectedChar -= i;
				if (selectedChar < 65) {
					selectedChar = (int) cipher[(i-1)] + 26 - (i);
				}
				translated[i-1] += (char) selectedChar;
			}
			
		}
		convert = String.valueOf(translated).concat("CS-230");	
		convert = (convert.length()+""+convert);
		return convert;
		
	}
	
	public static String getMessage(String args[]) throws IOException {
		String url = "http://cswebcat.swansea.ac.uk/message?solution=".concat(translateURLstring(args));
		URL test = new URL (url);
		BufferedReader in = new BufferedReader(
		new InputStreamReader(test.openStream()));
		String inputLine;
		if ((inputLine = in.readLine()) != null)
			return inputLine;
		else return "something ain't right";
	}
	
	public static String main(String args[]) throws IOException {
		return getMessage(args);
	}
}