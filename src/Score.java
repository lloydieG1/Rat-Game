import java.util.Comparator;

/**
 * A score holds the number of points someone achieved and the time they did
 * it in. it allows the use of comparisons for the time and the points so
 * they can be sorted in the right order.
 *
 * @author William Randle
 */
public class Score implements Comparable {
    public static final int PRECISION = 1000;

    private static final String PROFILE_FILE_PATH = "res/maps/highScores";

    private String username;
    private double time;
    private int points;


    /**
     * constructs a score
     *
     * @param username String parsed username for the profile which got the time
     * @param points   int number of points achieved
     */
    public Score(String username, int points, double time) {
        this.username = username;
        this.points = points;
        this.time = time;
    }

    /**
     * @return int the number of points achieved for the score
     */
    public int getPoints() {
        return points;
    }

    /**
     * @return double the time the score took to complete
     */
    public double getTime() {
        return time;
    }


    /**
     * gives the username of the profile who completed the score
     *
     * @return string profile username of the player who got this score
     */
    public String getUsername() {
        return username;
    }

    /**
     * @return string the data for a score formatted in a presentable way
     */
    public String toString() {
        int lettersUnderLimit = 10 - username.length();
        String padding = "";
        for (int i = 0; i < lettersUnderLimit; i++) {
            padding = padding + " ";
        }
        return username + padding + " - " + points + " - in: " + String.format("%.3f", time) + " seconds";
    }


    /**
     * @param score the score comparing against
     * @return the comparison result points, sorting in ascending order of points
     */
    @Override
    public int compareTo(Object score) {
        Score score1 = (Score) score;
        return score1.getPoints() - this.points;
    }

    /**
     * allows comparison for times in descending order
     */
    public static Comparator<Score> ScoreTimeComparator

            = new Comparator<Score>() {

        /**
         * compares two times together and gives comparison in descending order
         * @param score1 first score to compare against
         * @param score2 second score to compare agianst
         * @return the comparison result time sorting in descending order
         */
        public int compare(Score score1, Score score2) {


            return (int) (score1.getTime() * Score.PRECISION - score2.getTime() * Score.PRECISION);

        }

    };
}
