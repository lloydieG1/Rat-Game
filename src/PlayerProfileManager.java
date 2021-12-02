import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * @author lloyd
 */
public class PlayerProfileManager {
  private static final String OPEN_FILE_ERROR = "Could not find ";
  private static final String PROFILE_FILE_PATH = "res/profiles/";
  private static final String FIRST_LINE = "USER: ";
  private static final String SECOND_LINE = "MAX LEVEL: ";
	
  private static ArrayList<PlayerProfile> profiles = new ArrayList<>();
	
  /**
   * Description.
   *
   * @param username
   */
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
				 * MAX LEVEL: 1
				 */
        FileWriter writer = new FileWriter(fileName);
        writer.write(FIRST_LINE + username + "\n");
        writer.write(SECOND_LINE + "1 \n"); //player starts at level 1
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
  /**
   * Description.
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
      in.nextLine(); //ignore second line (highscore)
      oldLine = in.nextLine(); //old max level
      newLine = SECOND_LINE + data; //new max level
      getProfile(username).setMaxLevel(Integer.parseInt(data)); //update profile object
    } else {
      throw new IllegalArgumentException("whichData argument is not highscore or maxLevel");
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
  
  public static void initializeProfileArray() {
	  File directory = new File("res\\profiles");
	  File[] files = directory.listFiles();
	  
	  for (int i = 0; i < files.length; i++) {
		String user = files[i].getName().replace(".txt", "");
		profiles.add(parseProfileFile(user));
	}
  }
  
  public static void incrementMaxLevel(String username) {
	  int currentLevel = getProfile(username).getMaxLevel();
	  String nextLevel = Integer.toString(currentLevel + 1);
	  updateProfileData(ProfileData.MaxLevel, nextLevel, username);
  }
	
  /**
   * Description.
   *
   * @param username
   */
  public static void removeProfile(String username) {
    String filePath = usernameToPath(username);
    File fileToDelete = new File(filePath);
    if (fileToDelete.delete()) { 
      System.out.println("Deleted the user: " + username);;
    } else {
      System.out.println("Failed to delete the file.");
    }
    profiles.remove(getProfile(username)); //remove profile from global profile array
  }
	
  private static String readProfileFile(String username) {
    Scanner in = openProfileFile(username);
    String fileText = "";
    while (in.hasNext()) {
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
	      }else {
	    	System.out.println("Could not get profile with username " + username);
	      }
	    }
	    return profile;
	} catch (Exception e) {
		System.out.println("Error caused by " + username);
		return null;
	}
  }
    
  private static String usernameToPath(String username) {
    String filePath = PROFILE_FILE_PATH + username + ".txt";
    return filePath;
  }
}
