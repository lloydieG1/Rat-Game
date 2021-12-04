import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

/**
 * @author William Randle
 */
public class Leaderboard {
    private static final String OPEN_FILE_ERROR = "Could not find ";
    private static final String LEADER_BOARD_PATH = "res/maps/highScores/";
    private static final String FIRST_LINE = "USER: ";
    private static final String SECOND_LINE = "MAX LEVEL: ";

    private static ArrayList<Score> scores = new ArrayList<>();




    /**
     * Description.
     *
     * @param newScore
     * @param levelName
     */
    public static void addScore(String levelName, Score newScore, int type) {
        createFileIfNotExists(levelName);

        String fileName = levelToPath(levelName);
        ArrayList<Score> scores = getScores(levelName, type);
        scores.add(newScore);
        scores = sortScores(scores, type);
        try {
            FileWriter writer = new FileWriter(fileName);
            for (Score score : scores) {
                writer.append(score.toString() + "\n");
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("Cannot read file.");
            e.printStackTrace();
        }


    }


    public static void removeScores(String username) {
        File scoreDirectory = new File("res\\maps\\highScores\\");
        int fileCount = scoreDirectory.list().length;

        for (int i = 0; i < fileCount; i++) {
            String levelName = Integer.toString(i+1);
            ArrayList<Score> leaderboard = getScores(levelName, 0);
            leaderboard.removeIf(score -> score.getUsername().equals(username));
            leaderboard = sortScores(leaderboard, 0);
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



    private static ArrayList<Score> sortScores(ArrayList<Score> scores, int type) {
        switch (type) {
            case 0:
                Collections.sort(scores);
                break;
            case 1:
                Collections.sort(scores, Score.ScoreTimeComparator);
                break;
            default:
                System.out.println("invalid sort type");

        }

        return scores;
    }

    public static Score getHighScore(String userName, String level, int type) {

        ArrayList<Score> scores =getScores(level, type);

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
            Scanner in = openLeaderboard(levelName);

            while(in.hasNextLine()) {
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
            return sortScores(scores, type);


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
                System.out.println("creating file " + file.getName());
            } else {
                System.out.println("save already exists. now writing to it");
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
            System.out.println(inputFile);
        } catch (FileNotFoundException e) {
            System.out.println(OPEN_FILE_ERROR + level);
            System.exit(0);
        }
        return in;
    }


    private static String levelToPath(String  level) {
        String filePath = LEADER_BOARD_PATH + level + ".txt";
        return filePath;
    }

    public static boolean leaderboardExists(String username) {
        File user = new File(levelToPath(username));
        return user.exists();
    }
}
