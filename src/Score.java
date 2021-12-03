/**
 * Description.
 *
 * @author William Randle
 */
public class Score {
    private static final String PROFILE_FILE_PATH = "res/maps/highScores";

    private String username;
    private int score;


    /**
     * Description.
     *
     * @param username
     * @param score
     */
    public Score(String username, int score) {
        this.username = username;
        this.score = score;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int maxLevel) {
        this.score = score;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String toString() {
        return username + " " + score;
    }


}
