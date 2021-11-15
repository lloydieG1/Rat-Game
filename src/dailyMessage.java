import java.io.IOException;
import java.net.*;
import java.io.*;

/**
 * this class fetches the message of the day
 * @author Jack
 */
public class dailyMessage {

    /**
     * gets the unsolved code used to retrieve the motd
     * @param args
     * @return String unsolved code
     * @throws IOException
     */
	private static String getURLstring(String args[]) throws IOException {
		
		URL test = new URL ("http://cswebcat.swansea.ac.uk/puzzle");
		BufferedReader in = new BufferedReader(
		new InputStreamReader(test.openStream()));
		
		String inputLine;
		if ((inputLine = in.readLine()) != null)
			return inputLine;
		else return "something ain't right";
	}


    /** solves the code from parsed unsolved code, which is then
     * used to get the message of the day
     *
     * @param args
     * @return String code to get message of the day
     * @throws IOException
     */
	private static String translateURLstring(String args[]) throws IOException {
		char[] cipher = getURLstring(null).toCharArray(); 
		char[] translated = new char[cipher.length];
		String convert;
		for(int i = 1; i <= cipher.length; ++i){
			int selectedChar = cipher[i-1];
			if ((selectedChar > 90) || (selectedChar < 65)) {
				translated[i] = cipher[i-1];
			} else if (i % 2 == 0) {
				selectedChar = ( cipher[i-1]);
				selectedChar += i;
				if (selectedChar > 90) {
					selectedChar = cipher[i-1] + (i) - 26;
				}
				translated[i-1] += (char) selectedChar;	
				
			} else if (i % 2 != 0) {
				selectedChar = (cipher[i-1]);
				selectedChar -= i;
				if (selectedChar < 65) {
					selectedChar = cipher[(i-1)] + 26 - (i);
				}
				translated[i-1] += (char) selectedChar;
			}
			
		}
		convert = String.valueOf(translated).concat("CS-230");	
		convert = (convert.length()+""+convert);
		return convert;
		
	}

    /**
     * returns the message of the day as a string
     * @param args
     * @return String message of the day
     * @throws IOException
     */
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

}