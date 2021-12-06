/**
 * This class represents data for a given player profile.
 * @author Lloyd
 * @version 1.0
 */
public class PlayerProfile {
    private static final String PROFILE_FILE_PATH = "res/profiles/";

    private String username;
    private int maxLevel;
    private String playerProfilePath;

    /**
     * Create a player profile with a specified username.
     * @param username Player username.
     * @param maxLevel Current max level of specific player.
     */
    public PlayerProfile(String username, int maxLevel) {
        this.username = username;
        this.maxLevel = maxLevel;
        this.playerProfilePath = PROFILE_FILE_PATH + username + ".txt";
    }
    
    /**
     * Get the max level.
     * @return The max level.
     */
    public int getMaxLevel() {
        return maxLevel;
    }
    
    /**
     * Set the max level.
     * @param maxLevel The current max level.
     */
    public void setMaxLevel(int maxLevel) {
        this.maxLevel = maxLevel;
    }

    /**
     * Get the username.
     * @return The username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Set the username.
     * @param username Specify the username.
     */
    public void setUsername(String username) {
        this.username = username;
    }


}

