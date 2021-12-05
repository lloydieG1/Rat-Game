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

    private static final String timeBoard = "T";

    private static final String FIRST_LINE = "USER: ";
    private static final String SECOND_LINE = "MAX LEVEL: ";


    private static int DISPLAY_COUNT = 10;


    /**
     * Description.
     *
     * @param newScore
     * @param levelName
     * @param type      the type of sort (0= based on score, 1 =based on time)
     */
    public static void addScore(String levelName, Score newScore, int type) {
        createFileIfNotExists(levelName);
        createFileIfNotExists(levelName + timeBoard);

        //now add to the two tables for time and score top 10:

        //add score to the score leaderboard
        addScore(newScore, levelName);

        //check if the score is in the top 10 for time
        addTime(newScore, levelName);


    }

    private static void addScore(Score newScore, String levelName) {
        String leaderboardFile = levelToPath(levelName);
        ArrayList<Score> scores = getScores(levelName, 0);

        boolean max;

        max = DISPLAY_COUNT > scores.size();
        //check if the score is in the top 10 for score
        scores = sortScores(scores);

        boolean isTop10;

        if (max) {
            isTop10 = true;
        } else {
            isTop10 = (scores.get(DISPLAY_COUNT - 1).getPoints() < newScore.getPoints());
        }


        if (isTop10) {
            scores.add(newScore);
            scores = sortScores(scores);

            try {
                FileWriter writer = new FileWriter(leaderboardFile);
                for (Score score : scores) {
                    writer.append(score.toString() + "\n");
                    System.out.println("new score!");
                }
                writer.close();
            } catch (IOException e) {
                System.out.println("Cannot read file.");
                e.printStackTrace();
            }
        }
    }

    private static void addTime(Score newScore, String levelName) {
        String timeBoardFile = levelToPath(levelName + timeBoard);
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

            try {
                FileWriter writer = new FileWriter(timeBoardFile);
                for (Score score : scores) {
                    writer.append(score.toString() + "\n");

                }
                writer.close();
            } catch (IOException e) {
                System.out.println("Cannot read file.");
                e.printStackTrace();
            }
        }
    }


    public static void removeScores(String username) {
        File scoreDirectory = new File("res\\maps\\highScores\\");
        int fileCount = scoreDirectory.list().length;

        for (int i = 0; i < fileCount; i++) {
            String levelName = Integer.toString(i + 1);
            ArrayList<Score> leaderboard = getScores(levelName, 0);
            leaderboard.removeIf(score -> score.getUsername().equals(username));
            leaderboard = sortScores(leaderboard);
            try {
                FileWriter writer = new FileWriter(levelToPath(levelName));
                for (Score score : leaderboard) {
                    writer.append(score.toString() + "\n");
                }
                writer.close();
            } catch (IOException e) {
                System.out.println("Cannot read file.");
                e.printStackTrace();
            }
        }
    }


    private static ArrayList<Score> sortScores(ArrayList<Score> scores) {

        Collections.sort(scores);


        scores = truncateScores(scores);

        return scores;
    }

    private static ArrayList<Score> sortTimes(ArrayList<Score> scores) {

        Collections.sort(scores, Score.ScoreTimeComparator);


        scores = truncateScores(scores);

        return scores;
    }

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


    public static Score getHighScore(String userName, String level, int type) {

        ArrayList<Score> scores = getScores(level, type);

        for (Score score : scores) {
            if (score.getUsername().equals(userName)) {
                return score;
            }
        }
        return new Score(userName, 0, 0);
    }


    public static ArrayList<Score> getScores(String levelName, int type) {


        ArrayList<Score> scores = new ArrayList<>();
        try {

            if (type == 0) {
                Scanner in = openLeaderboard(levelName);

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
                return sortScores(scores);
            } else {
                Scanner in = openLeaderboard(levelName + timeBoard);

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


    private static String readLeaderboard(String levelName) {
        Scanner in = openLeaderboard(levelName);
        String fileText = "";
        while (in.hasNext()) {
            fileText = fileText + in.nextLine() + "\n";
        }
        in.close();
        return fileText;
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

    public static boolean leaderboardExists(String username) {
        File user = new File(levelToPath(username));
        return user.exists();
    }
}
