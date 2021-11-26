import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

public class PlayerProfileManager {
    private static final String OPEN_FILE_ERROR = "Could not find ";
	private static final String PROFILE_FILE_PATH = "res/profiles/";
	private static final String FIRST_LINE = "USER: ";
	private static final String SECOND_LINE = "HIGHSCORE: ";
	private static final String THIRD_LINE = "MAX LEVEL: ";
	
	private static ArrayList<PlayerProfile> profiles = new ArrayList<>();
	
	
	public static void addNewProfile(String username) {
		String fileName = PROFILE_FILE_PATH + username + ".txt";
		
		//TODO needs to be refactored to remove repeated filewriter code
		
		//create file named after the username if it does not already exist.
		try {
			
			File profileFile = new File(fileName);
			if (profileFile.createNewFile()) {
				System.out.println("File created: " + profileFile.getName());
				
				//if the file does exist open it and write the username to file 
				FileWriter writer = new FileWriter(fileName);
				writer.write("USER: " + username);
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
	
	public static void updateProfileData(String whichData, String data, String fileName) {
		String profileFileContents = readProfileFile(fileName);
		System.out.println("Contents of the file: "+ profileFileContents);
		
		Scanner in = openProfileFile(fileName);
		
		String oldLine = null;
		String newLine = null;
		
		if(whichData == "highscore") {
			in.nextLine(); //ignore first line (username)
			oldLine = in.nextLine();
			newLine = data;
		}else if(whichData == "maxLevel") {
			in.nextLine(); //ignore first line (username)
			in.nextLine(); //ignore second line (highscore)
			oldLine = in.nextLine();
			newLine = data;
		}else {
			throw new IllegalArgumentException("whichData arguement is not highscore or maxLevel");
		}
		
		
		in.close();
		
		profileFileContents = profileFileContents.replaceAll(oldLine, newLine);
		
		try {
			FileWriter writer = new FileWriter(fileName);
			writer.append(profileFileContents);
		} catch (IOException e) {
			System.out.println("Cannot read file.");
			e.printStackTrace();
		}	
	}
	
	public void removeProfile(String username) {
		String fileName = PROFILE_FILE_PATH + username + ".txt";
	    File fileToDelete = new File(fileName);
	    if (fileToDelete.delete()) { 
	      System.out.println("Deleted the user: " + username);;
	    } else {
	      System.out.println("Failed to delete the file.");
	    }
	}
	
    private static String readProfileFile(String fileName) {
        Scanner in = openProfileFile(fileName);

        String fileText = "";
        while(in.hasNext()) {
            fileText = fileText + in.nextLine();
        }

        in.close();

        return fileText;
    }
    
    private static Scanner openProfileFile(String fileName) {
        File inputFile = new File(PROFILE_FILE_PATH + fileName);
        Scanner in = null;

        //attempts to open file and returns exception if it is not found
        try {
            in = new Scanner (inputFile);
        }
        catch (FileNotFoundException e) {
            System.out.println (OPEN_FILE_ERROR + fileName);
            System.exit (0);
        }

        return in;
    }
}
