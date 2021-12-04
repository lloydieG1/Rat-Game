import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * @author Lloyd
 */
public class PlayerProfileManager {
	private static final String OPEN_FILE_ERROR = "Error with file ";
	private static final String PROFILE_FILE_PATH = "res/profiles/";
	private static final String FILE_EXTENSION = ".txt";
	private static final String FIRST_LINE = "USER: ";
	private static final String SECOND_LINE = "MAX LEVEL: ";
	private static final String NEWLINE = "\n";
	private static final String STARTING_LEVEL = "1";
	private static final int LEVELS_UNLOCKED_PER_WIN = 1;
	
	
	private static ArrayList<PlayerProfile> profiles = new ArrayList<>();
	
	/**
	 * Creates a new profile file initialised with max level equal to 1
	 * and adds the new profile as a profile object in the profiles array.
	 *
	 * @param username
	 */
	public static void addNewProfile(String username) {
		String fileName = usernameToPath(username);
		//create file named after the username if it does not already exist.
		try {
			File profileFile = new File(fileName);
			if (profileFile.createNewFile()) {
				System.out.println("File created: " + profileFile.getName());
				/*
				 * if the file does exist open it and write the username to file on line 1
				 * will look like this:
				 * USER: *username*
				 * MAX LEVEL: 1
				 */
				FileWriter writer = new FileWriter(fileName);
				writer.write(FIRST_LINE + username + NEWLINE);
				writer.write(SECOND_LINE + STARTING_LEVEL + NEWLINE);
				writer.close();
			} else {
				System.out.println("File already exists.");
			}
		} catch (IOException e) {
			System.out.println(OPEN_FILE_ERROR + fileName);
			e.printStackTrace();
		}
		//new player profile is initalised to start at level 1 and is added to static array of profiles.
		PlayerProfile newProfile = new PlayerProfile(username, 1);
		profiles.add(newProfile);
	}

    public static int getSize() {
        return profiles.size();
    }

    public static PlayerProfile getProfileInt(int i) {
        return profiles.get(i);
    }
	
	/**
	 * Updates the string data stored in a profile file for either username or max level
	 *
	 * @param whichData
	 * @param data
	 * @param username
	 */
	public static void updateProfileData(ProfileData whichData, String data, String username) {
		String fileName = usernameToPath(username);
		String profileFileContents = readProfileFile(username);
		System.out.println("Contents of the file: " + profileFileContents);
	
		Scanner in = openProfileFile(username);
		String oldLine = null;
		String newLine = null;

		if (whichData == ProfileData.Username) {
			oldLine = in.nextLine(); //old highscore
			newLine = FIRST_LINE + data; //new highscore
			getProfile(username).setUsername(data); //update profile object
		} else if (whichData == ProfileData.MaxLevel) {
			in.nextLine(); //ignore first line (username)
			oldLine = in.nextLine(); //old max level
			newLine = SECOND_LINE + data; //new max level
			getProfile(username).setMaxLevel(Integer.parseInt(data)); //update profile object
		} else {
			throw new IllegalArgumentException("whichData argument is not highscore or maxLevel");
		}
		in.close();
		
		profileFileContents = profileFileContents.replaceAll(oldLine, newLine);
		writeToFile(fileName, profileFileContents);
	}
	
	/**
	 * Writes a string to file.
	 *
	 * @param fileName name of file
	 * @param data string to be written
	 */
	private static void writeToFile(String fileName, String data) {
		try {	
			FileWriter writer = new FileWriter(fileName);
			writer.append(data);
			writer.close();
		} catch (IOException e) {
			System.out.println("Cannot read file.");
			e.printStackTrace();
		}	
	}
	
	/**
	 * reads a profile file and returns that file as a PlayerProfile object
	 *
	 * @param username username that the profile file is named after
	 * @return PlayerProfile with data from file
	 */
	public static PlayerProfile parseProfileFile(String username) {
		try {
			Scanner in = openProfileFile(username);
				
			in.findInLine(FIRST_LINE); //Skips past "USER: " 
			String user = in.nextLine();
			in.findInLine(SECOND_LINE); //Skips past "MAX LEVEL: " 
			int maxLevel = in.nextInt();
	
			PlayerProfile profile = new PlayerProfile(user, maxLevel);
			return profile;
			
		} catch (Exception e) {
			System.out.println("Failed to parse profile " + username);
			return null;
		}
	}
	
	/**
	 * Populates the profile array with all the profiles that are stored
	 * in the profiles directory.
	 *
	 */
	public static void initializeProfileArray() {
		File directory = new File(PROFILE_FILE_PATH);
		File[] files = directory.listFiles();
		
		for (int i = 0; i < files.length; i++) {
			String user = files[i].getName().replace(FILE_EXTENSION, "");
			profiles.add(parseProfileFile(user));
		}
	}
	
	/**
	 * Increases the maximum level of a profile by 1
	 *
	 * @param username username that the profile file is named after
	 * @return PlayerProfile with data from file
	 */
	public static void setMaxLevel(String username, String levelString) {
		int newLevel = Integer.parseInt(levelString);
		int currentLevel = getProfile(username).getMaxLevel();
		if (newLevel > currentLevel - 1) {
			String nextLevel = Integer.toString(newLevel + LEVELS_UNLOCKED_PER_WIN);
			updateProfileData(ProfileData.MaxLevel, nextLevel, username);
		}
	}

	public static int getMaxLevel(String username) {
        for(PlayerProfile playerProfile : profiles) {
            if (playerProfile.getUsername().equals(username)) {
                return playerProfile.getMaxLevel();
            }
        }
        return 0;
	}
	
	/**
	 * Description.
	 *
	 * @param username
	 */
	public static void removeProfile(String username) {
		String filePath = usernameToPath(username);
		File fileToDelete = new File(filePath);
		fileToDelete.delete();
        System.out.println("file should now delete");

        boolean gameProfileConflict = false;
        if(Game.currentProfile.getUsername().equals(username)) {
            gameProfileConflict = true;
        }

		profiles.remove(getProfile(username)); //remove profile from global profile array
        if (gameProfileConflict) {
            Game.currentProfile = profiles.get(0);
        }
        removeSaves(username);
	}

    private static void removeSaves(String username) {
        File directory = new File("res\\maps\\save\\");

        File[] saves = directory.listFiles();
        int fileCount = directory.list().length;


        for (int i = 0; i < fileCount; i++) {
            //any non alphanumeric is an illegal character, so none would have '_' in their name and thus, this would not be a problem.
            String saveUser = saves[i].getName().replace(username, "_delete_user_");
            if (saveUser.contains("_delete_user_")) {
                saves[i].delete();
            }
        }
    }


	
	private static String readProfileFile(String username) {
		Scanner in = openProfileFile(username);
		String fileText = "";
		while (in.hasNext()) {
			fileText = fileText + in.nextLine() + NEWLINE;
		}
		in.close();
		return fileText;
	}
		
	private static Scanner openProfileFile(String username) {
		File inputFile = new File(usernameToPath(username));
		Scanner in = null;
		//attempts to open file and returns exception if it is not found
		try {
			in = new Scanner(inputFile);
		} catch (FileNotFoundException e) {
			System.out.println(OPEN_FILE_ERROR + username);
			System.exit(0);
		}
		return in;
	}
		
	public static PlayerProfile getProfile(String username) {
		try {
			PlayerProfile profile = null;
			for (int i = 0; i < profiles.size(); i++) {
				String storedUsername = (profiles.get(i)).getUsername();
				if (storedUsername.equals(username)) {
					profile = profiles.get(i);
				}
			}
			return profile;
		} catch (Exception e) {
			System.out.println("Error caused by " + username);
			return null;
		}
	}
		
	private static String usernameToPath(String username) {
		String filePath = PROFILE_FILE_PATH + username + FILE_EXTENSION;
		return filePath;
	}

	public static boolean userExists(String username) {
			File user = new File(usernameToPath(username));
			return user.exists();
	}
}
