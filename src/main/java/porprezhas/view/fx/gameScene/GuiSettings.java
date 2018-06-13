package porprezhas.view.fx.gameScene;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import porprezhas.CircularArrayList;
import porprezhas.model.dices.Dice;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class GuiSettings {
    public static final boolean bFixedFont = false;

    // ***** Game GUI attributes *****
    // path
    public static final String pathToRoot = "/";
    public static final String pathToFxml = pathToRoot;
    public static final String pathToBackground = "background/";
    public static final String pathToBorder = "border/";
    public static final String pathToToolCard = "cards/templateFolder/";
    public static final String pathToPrivateCard = "cards/privateCard/";
    public static final String pathToPublicCard = "cards/publicCard/";
    public static final String pathToPattern = "pattern/";
    public static final String pathToHeadIcon = "head/64x64x64/";
    public static final String pathToDice = "dice/36x36/";
    public static final String pathToCursor = "cursor/";

    public static final String pathToMusic = "sound/music/";
    public static final String pathToGameMusic = pathToMusic + "gameMusic/";
    public static final String pathToLoginMusic = pathToMusic + "loginMusic/";
    public static final String pathToResultMusic = pathToMusic + "resultsMusic/";
    public static final String pathToPatternMusic = pathToGameMusic;


    // string
    public static final String GAME_TITLE = "SAGRADA";

    // scene names
    public static final String stageLoginID = "login";
    public static final String stageLoginFile = "LoginView";
    public static final String stagePatternID = "pattern";
    public static final String stagePatternFile = "ChoosePatternView";
    public static final String stageGameID = "game";
    public static final String stageGameFile = "GameView";
    public static final String stageResultsID = "results";
    public static final String stageResultsFile = "ResultsView";
//    public static final List<String> stageIDs = Arrays.asList(
//            stageLoginID, stageGameID, stagePatternID, stageResultsID);


    // number
    public static final int BOARD_COLUMN = 5;
    public static final int BOARD_ROW = 4;
    public static final int ICON_QUANTITY = 64;   // index from 1 to 64
    public static final int SOLITAIRE_WIDTH = 550;
    public static final double BOARD_DICE_ZOOM = 0.92;       //this value depends by dice image. for dice_64x64 zoom = 1.12 should be fine
    public static final double TRACK_DICE_ZOOM = 0.8086;       // NOTE: this value must be below 1.0!!! 0.8~0.96 are good.
    public static final double CARD_FIT_RATIO = 0.86;       // this should be below 1.0 and as big as possible
    public static final double CARD_PANE_PADDING = 15;       // adjust this value based on .fxml settings
    public enum CardTab{
        TOOL_CARD,
        PUBLIC_CARD,
        PRIVATE_CARD
    }


    // ***** Develop use attributes *****
    public static final boolean bDebug = true;
    public static final boolean bShowGridLines = false;
    public static final boolean bShowFrames = false;

    // ***** User settings *****
    // View
    public static double FPS_PRINT_AT_MIN = 10;     // print frequency in a minute, unit = [1/60 Hz]
    public static double DRAFT_DICE_ZOOM = 0.96;    // this value must be below 2, and should be much bigger than 0.5
    public static double BORDER_SIZE = 1.8;         // should be around 2

    public static double cardOpacity = 0.8086;      // normal opacity of the cards
    public static double CARD_FADE_IN = 420;        // time in [ms]
    public static double CARD_FADE_OUT = 1200;

    public static double STAGE_FADE_IN = 500;
    public static double STAGE_FADE_OUT = 1000;

    // Sound
    public static BooleanProperty bMuteMusic = new SimpleBooleanProperty(false);
    public static DoubleProperty musicVolume = new SimpleDoubleProperty(0.3);



    // ***** Global attributes *****
    // images

    // borders
//    public static Border cardBorder;


    // ***** initialization method *****


    // ***** Global methods *****

    //
    public static long minuteFrequencyToMillis(double frequencyInMinute) {
        return (long) (60*1000.0 / frequencyInMinute);
    }


    public static String getPathToDice(Dice dice) {
        return pathToDice + dice.getDiceNumber() + dice.getColorDice().name().toLowerCase().charAt(0) + ".png";
    }

    // get the absolute path to the file with any extension. It is like */relativePath/fileName.*
    public static String getPathToFileIgnoreExt(String relativePath, String fileName) {
        // get resource path
        System.out.println(GuiSettings.class.getResource("/" ));
        String resourcePath = GuiSettings.class.getResource("/" ).getPath();  // get absolute path to resource
        resourcePath = resourcePath.substring(1, resourcePath.length());    // cut '/' at beginning of path

        // Convert the file url in file path format
        resourcePath = resourcePath.replaceAll("%20", " ");
        if(bDebug) {
            System.out.println("searching \'" + fileName + "\' in resource path = " + resourcePath + relativePath );
        }

        // Open the directory
        final File dir = new File(resourcePath + relativePath);
        if (!dir.exists() && dir.isDirectory()) {
            System.err.println("Cannot find source directory: " + dir);
            return null;
        }

        // Filter
        String[] filePath = dir.list((dir1, name) ->
                name.startsWith(fileName + ".")
        );
        if(filePath.length == 0) {
            System.err.println("0 file found in : " + dir);
            return null;
        }

        if(bDebug) {
            System.out.println(filePath.length + " files found, returning: \t" + filePath[0]);
        }

//        return "file:///" + dir + "\\" + filePath[0];
        return relativePath + filePath[0];
    }
}
