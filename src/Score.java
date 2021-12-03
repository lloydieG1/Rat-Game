import java.math.MathContext;
import java.util.Comparator;

/**
 * Description.
 *
 * @author William Randle
 */
public class Score implements Comparable {
    private static final String PROFILE_FILE_PATH = "res/maps/highScores";

    private String username;
    private double time;
    private int score;

    public static final int PRECISION = 1000;


    /**
     * Description.
     *
     * @param username
     * @param score
     */
    public Score(String username, int score, double time) {
        this.username = username;
        this.score = score;
        this.time =  time;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int maxLevel) {
        this.score = score;
    }

    public double getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String toString() {
        return username + " - " + score + " - in: "  +String.format("%.3f", time) + " seconds" ;
    }



    @Override
    public int compareTo(Object o) {
        Score score1 = (Score) o;
        return score1.getScore()-this.score;
    }

    public static Comparator<Score> ScoreTimeComparator

            = new Comparator<Score>() {

        public int compare(Score score1, Score score2) {


            return (int)(score1.getTime()*Score.PRECISION - score2.getTime()*Score.PRECISION);

        }

    };
}
