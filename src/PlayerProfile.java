
public class PlayerProfile {
	private String username;
	private int maxLevel;
	private String playerProfilePath;
	
	public PlayerProfile(String username, int maxLevel) {
		this.username = username;
		this.maxLevel = maxLevel;
	}
	
	public int getMaxLevel() {
		return maxLevel;
	}
	public void setMaxLevel(int maxLevel) {
		this.maxLevel = maxLevel;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
}
