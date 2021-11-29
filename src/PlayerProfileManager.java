import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class PlayerProfileManager {
    private static final String OPEN_FILE_ERROR = "Could not find ";
	private static final String PROFILE_FILE_PATH = "res/profiles/";
	private static final String FIRST_LINE = "USER: ";
	private static final String SECOND_LINE = "HIGHSCORE: ";
	private static final String THIRD_LINE = "MAX LEVEL: ";
	
	private static ArrayList<PlayerProfile> profiles = new ArrayList<>();
	
	
	public static void addNewProfile(String username) {
		String fileName = usernameToPath(username);
		
		//TODO needs to be refactored to remove repeated filewriter code
		
		//create file named after the username if it does not already exist.
		try {
			
			File profileFile = new File(fileName);
			if (profileFile.createNewFile()) {
				System.out.println("File created: " + profileFile.getName());
				
				/*
				 * if the file does exist open it and write the username to file on line 1
				 * will look like this:
				 * USER: *username*
				 * HIGHSCORE: 
				 * MAX LEVEL: 
				 */
				FileWriter writer = new FileWriter(fileName);
				writer.write(FIRST_LINE + username + "\n");
				writer.write(SECOND_LINE + "\n");
				writer.write(THIRD_LINE);
				writer.close();
			} else {
				System.out.println("File already exists.");
			}
		} catch (IOException e) {
	        System.out.println("There was an error with the profile file.");
	        e.printStackTrace();
	    }
		
		//new player profile is initalised to start at level 1 and is added to static array of profiles.
		PlayerProfile newProfile = new PlayerProfile(username, 1);
		profiles.add(newProfile);
	}
	
	/*
	 * TODO instead of using String whichdata make an enum type for profile data
	 */
	public static void updateProfileData(String whichData, int data, String username) {
		String fileName = usernameToPath(username);
		String profileFileContents = readProfileFile(username);
		System.out.println("Contents of the file: "+ profileFileContents);
		
		Scanner in = openProfileFile(username);
		
		String oldLine = null;
		String newLine = null;
		
		if(whichData == "highscore") {
			String hs = in.nextLine(); //ignore first line (username)
			oldLine = in.nextLine(); //old highscore
			newLine = SECOND_LINE + data; //new highscore
		}else if(whichData == "maxLevel") {
			in.nextLine(); //ignore first line (username)
			in.nextLine(); //ignore second line (highscore)
			oldLine = in.nextLine();
			newLine = THIRD_LINE + data;
		}else {
			throw new IllegalArgumentException("whichData arguement is not highscore or maxLevel");
		}
		
		
		in.close();
		
		profileFileContents = profileFileContents.replaceAll(oldLine, newLine);
		
		try {
			FileWriter writer = new FileWriter(fileName);
			writer.append(profileFileContents);
			writer.close();
		} catch (IOException e) {
			System.out.println("Cannot read file.");
			e.printStackTrace();
		}	
	}
	
	public void removeProfile(String username) {
		String filePath = usernameToPath(username);
	    File fileToDelete = new File(filePath);
	    if (fileToDelete.delete()) { 
	      System.out.println("Deleted the user: " + username);;
	    } else {
	      System.out.println("Failed to delete the file.");
	    }
	}
	
    private static String readProfileFile(String username) {
        Scanner in = openProfileFile(username);

        String fileText = "";
        while(in.hasNext()) {
            fileText = fileText + in.nextLine() + "\n";
        }

        in.close();

        return fileText;
    }
    
    private static Scanner openProfileFile(String username) {
        File inputFile = new File(usernameToPath(username));
        Scanner in = null;

        //attempts to open file and returns exception if it is not found
        try {
            in = new Scanner (inputFile);
        }
        catch (FileNotFoundException e) {
            System.out.println (OPEN_FILE_ERROR + username);
            System.exit (0);
        }

        return in;
    }
    
    private static String usernameToPath(String username) {
    	String filePath = PROFILE_FILE_PATH + username + ".txt";
    	return filePath;
    }
    
//    public static void main(String[] args) {
//		updateProfileData("highscore", 1000, "User1");
//		updateProfileData("maxLevel", 6, "User1");
//	}
}
