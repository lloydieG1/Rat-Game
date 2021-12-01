/**
 * Description.
 *
 * @author Lloyd
 */
public class PlayerProfile {
  private static final String PROFILE_FILE_PATH = "res/profiles/";
	
  private String username;
  private int maxLevel;
  private int highscore;
  private String playerProfilePath;
	
  /**
   * Description.
   *
   * @param username
   * @param maxLevel
   */
  public PlayerProfile(String username, int maxLevel) {
    this.username = username;
    this.maxLevel = maxLevel;
    this.playerProfilePath = PROFILE_FILE_PATH + username + ".txt";
  }
	
  public int getMaxLevel() {
    return maxLevel;
  }
  
  public void setMaxLevel(int maxLevel) {
    this.maxLevel = maxLevel;
  }
  
  public int getHighscore() {
    return highscore;
  }
  
  public void setHighscore(int highscore) {
    this.highscore = highscore;
  }
  
  public String getUsername() {
    return username;
  }
  
  public void setUsername(String username) {
    this.username = username;
  }
	
  public String getPlayerProfilePath() {
    return playerProfilePath;
  }
	
}
