import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

/**
 * @author William Randle
 */
public class Leaderboard {
    private static final String OPEN_FILE_ERROR = "Could not find ";
    private static final String LEADER_BOARD_PATH = "res/maps/highScores/";

    private static final String TIMES_LEADERBOARD_NAME = "T";

    private static final int DISPLAY_COUNT = 10;


    /**
     * if the score is in the top 10 of the leaderboard for times or points,
     * add it to that leaderboard.
     *
     * @param newScore  Score to add to the leaderboard/s
     * @param levelName the level the score was achieved on
     */
    public static void addScore(String levelName, Score newScore) {
        createFileIfNotExists(levelName);
        createFileIfNotExists(levelName + TIMES_LEADERBOARD_NAME);

        //now add to the two tables for time and score top 10:

        //add score to the score leaderboard
        addPoints(newScore, levelName);

        //check if the score is in the top 10 for time
        addTime(newScore, levelName);


    }

    /**
     * adds a score to the top 10 leaderboard of points
     * (if they are in the top 10)
     *
     * @param newScore  Score to add to the leaderboard
     * @param levelName level the score was achieved on
     */
    private static void addPoints(Score newScore, String levelName) {
        String leaderboardFile = levelToPath(levelName);
        ArrayList<Score> scores = getScores(levelName, 0);

        boolean max;

        max = DISPLAY_COUNT > scores.size();
        //check if the score is in the top 10 for score
        scores = sortPoints(scores);

        boolean isTop10;

        if (max) {
            isTop10 = true;
        } else {
            isTop10 = (scores.get(DISPLAY_COUNT - 1).getPoints() < newScore.getPoints());
        }


        if (isTop10) {
            scores.add(newScore);
            scores = sortPoints(scores);

            updateTable(leaderboardFile, scores);
        }
    }

    /**
     * adds a score to the top 10 leaderboard of times
     * (if they are in the top 10)
     *
     * @param newScore  Score to add to the leaderboard
     * @param levelName level the score was achieved on
     */
    private static void addTime(Score newScore, String levelName) {
        String timeBoardFile = levelToPath(levelName + TIMES_LEADERBOARD_NAME);
        ArrayList<Score> scores = getScores(levelName, 1);
        boolean max;

        max = DISPLAY_COUNT > scores.size();
        //check if the score is in the top 10 for time
        scores = sortTimes(scores);

        boolean isTop10;

        if (max) {
            isTop10 = true;
        } else {
            isTop10 = (scores.get(DISPLAY_COUNT - 1).getTime() > newScore.getTime());
        }


        if (isTop10) {
            scores.add(newScore);
            scores = sortTimes(scores);

            updateTable(timeBoardFile, scores);
        }
    }

    /**
     * updates the table to show the current scores for that table.
     *
     * @param tableName String the file path for the table to update
     * @param scores    Arraylist of scores to set the table to
     */
    private static void updateTable(String tableName, ArrayList<Score> scores) {
        try {
            FileWriter writer = new FileWriter(tableName);
            for (Score score : scores) {
                writer.append(score.toString() + "\n");

            }
            writer.close();
        } catch (IOException e) {
            System.out.println("Cannot read table.");
            e.printStackTrace();
        }
    }


    /**
     * removes the scores achieved by a user parsed username
     *
     * @param username String username of the user being removed
     */
    public static void removeScores(String username) {
        File scoreDirectory = new File("res\\maps\\highScores\\");


        //remove from
        for (File file : scoreDirectory.listFiles()) {
            String levelName = file.getName().replace(".txt"
                    , "");

            if (levelName.contains("T")) {
                levelName = levelName.replace("T", "");

                ArrayList<Score> leaderboard = getScores(levelName, 1);
                leaderboard.removeIf(score
                        -> score.getUsername().equals(username));

                leaderboard = sortPoints(leaderboard);
                updateTable(levelToPath(levelName), leaderboard);


            } else {
                ArrayList<Score> leaderboard = getScores(levelName, 0);
                leaderboard.removeIf(score
                        -> score.getUsername().equals(username));

                leaderboard = sortPoints(leaderboard);
                updateTable(levelToPath(levelName), leaderboard);

            }

        }
    }


    /**
     * returns an arraylist of scores sorted in ascending order of points
     * should only return the top 10
     *
     * @param scores Arraylist of scores to be sorted
     * @return Arraylist of sorted scores
     */
    private static ArrayList<Score> sortPoints(ArrayList<Score> scores) {

        Collections.sort(scores);


        scores = truncateScores(scores);

        return scores;
    }

    /**
     * returns an arraylist of scores sorted in descending order of time
     * should only return the top 10
     *
     * @param scores Arraylist of scores to be sorted
     * @return Arraylist of sorted scores
     */
    private static ArrayList<Score> sortTimes(ArrayList<Score> scores) {

        Collections.sort(scores, Score.ScoreTimeComparator);


        scores = truncateScores(scores);

        return scores;
    }

    /**
     * removes all but the first 10 scores in an arraylist of scores
     * @param scores Arraylist of scores to truncate
     * @return Arraylist of first 10 scores
     */
    private static ArrayList<Score> truncateScores(ArrayList<Score> scores) {
        ArrayList<Score> truncateScores = new ArrayList<>();

        int j = 0;
        for (Score score : scores) {
            j++;
            if (j <= DISPLAY_COUNT) {
                truncateScores.add(score);
            }

        }

        return truncateScores;
    }


    /**
     * gets the scores from the given table based on the parsed type.
     * @param levelName String name of the level for the scores
     * @param table int the table we are getting (0=points, 1 = times)
     * @return Arraylist of scores in the top 10 of the specified table
     */
    public static ArrayList<Score> getScores(String levelName, int table) {


        ArrayList<Score> scores = new ArrayList<>();
        try {
            Scanner in;
            if (table == 0) {
                in = openLeaderboard(levelName);
            } else {
                in = openLeaderboard(levelName + TIMES_LEADERBOARD_NAME);
            }

            while (in.hasNextLine()) {
                String username = in.next();
                in.next();
                int points = in.nextInt();
                in.next();
                in.next();
                double time = in.nextDouble();
                Score score = new Score(username, points, time);
                scores.add(score);
                in.nextLine();
            }
            in.close();

            if (table == 0) {
                return sortPoints(scores);
            } else {
                return sortTimes(scores);
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to parse leaderboard " + levelName);
            return null;
        }

    }

    private static void createFileIfNotExists(String level) {

        try {
            File file = new File(levelToPath(level));
            if (file.createNewFile()) {
                // System.out.println("creating file " + file.getName());
            } else {
                //  System.out.println("save already exists. now writing to it");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }



    private static Scanner openLeaderboard(String level) {
        File inputFile = new File(levelToPath(level));


        createFileIfNotExists(level);

        Scanner in = null;
        //attempts to open file and returns exception if it is not found
        try {
            in = new Scanner(inputFile);
            // System.out.println(inputFile);
        } catch (FileNotFoundException e) {
            System.out.println(OPEN_FILE_ERROR + level);
            System.exit(0);
        }
        return in;
    }


    private static String levelToPath(String level) {
        String filePath = LEADER_BOARD_PATH + level + ".txt";
        return filePath;
    }

}
