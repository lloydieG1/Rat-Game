import java.io.IOException;
import java.net.*;
import java.io.*;

/**
 * This Class is designed to send an HTML request to a site, retrieve a string
 * and use an algorithm to translate it into a key which when sent as an HTML
 * request will return the Message of the day, which is then displayed in-game.
 * @author Jack Lennard
 * @author William Randle
 * @version 1.0
 */
public class DailyMessage {
    private static final String WEBSITE_URL = "http://cswebcat.swansea.ac.uk/puzzle";

    /**
     * Gets the unsolved code used to retrieve the Message Of The Day.
     * @param args Array of arguments.
     * @return String of unsolved cipher.
     * @throws IOException
     */
    private static String getURLstring(String args[]) throws IOException, UnknownHostException {
        URL puzzle = new URL(WEBSITE_URL);
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(puzzle.openStream()));
            String htmlResponse;

            if ((htmlResponse = in.readLine()) != null) {
                return htmlResponse;
            } else {
                return "Could not fetch website";
            }
        } catch (UnknownHostException e) {
            return "No Internet - Daily Message Unavailable";
        }
    }

    /**
     * Solves the code from parsed unsolved code, which is then used to get the
     * message of the day.
     * @param args Array of arguments.
     * @return String solved cipher to get Message Of The Day. 
     * @throws IOException
     */
    private static String translateURLstring(String args[]) throws IOException {
        char[] cipher = getURLstring(null).toCharArray();
        String convertedCipher = "";
        for (int i = 1; i <= cipher.length; i++) {
            int selectedChar = cipher[i - 1];
            if ((selectedChar > 90) || (selectedChar < 65)) {
                convertedCipher = convertedCipher + cipher[i - 1];
            } else if (i % 2 == 0) {
                selectedChar += i;
                if (selectedChar > 90) {
                    selectedChar = cipher[i - 1] + (i) - 26;
                }
                convertedCipher = convertedCipher + (char) selectedChar;
            } else {
                selectedChar -= i;
                if (selectedChar < 65) {
                    selectedChar = cipher[(i - 1)] + 26 - (i);
                }
                convertedCipher = convertedCipher + (char) selectedChar;
            }
        }
        convertedCipher = convertedCipher + ("CS-230");
        convertedCipher = (convertedCipher.length() + "" + convertedCipher);
        return convertedCipher;

    }

    /**
     * Returns the message of the day as a string.
     * @param args Array of arguments.
     * @return String message of the day.
     * @throws IOException
     */
    public static String getMessage(String args[]) throws IOException, UnknownHostException {
        String message = "http://cswebcat.swansea.ac.uk/message?solution=".concat(translateURLstring(args));
        URL messageURL = new URL(message);
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(messageURL.openStream()));
            String motd;
            if ((motd = in.readLine()) != null) {
                return motd;
            } else {
                return "Could not fetch website";
            }
        } catch (UnknownHostException e) {
            return "No Internet - Daily Message Unavailable";
        }
    }
}
