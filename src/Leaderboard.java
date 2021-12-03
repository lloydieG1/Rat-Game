import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
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
    public static void addScore(String levelName, Score newScore) {
        createFileIfNotExists(levelName);

        String fileName = levelToPath(levelName);


        try {
            FileWriter writer = new FileWriter(fileName);
            writer.append(newScore.toString() + "\n");
            writer.close();
        } catch (IOException e) {
            System.out.println("Cannot read file.");
            e.printStackTrace();
        }
    }

    public static ArrayList<Score> getScores(String levelName) {
        ArrayList<Score> scores = new ArrayList<>();
        try {
            Scanner in = openLeaderboard(levelName);

            while(in.hasNextLine()) {
                Score score = new Score(in.next(), in.nextInt());
                scores.add(score);
            }



        } catch (Exception e) {
            System.out.println("Failed to parse leaderboard " + levelName);
            return null;
        }
        return scores;
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
