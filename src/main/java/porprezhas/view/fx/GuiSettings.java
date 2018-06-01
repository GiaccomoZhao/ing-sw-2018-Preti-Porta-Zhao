package porprezhas.view.fx;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderImage;
import javafx.scene.layout.BorderRepeat;
import javafx.scene.layout.BorderWidths;
import javafx.scene.media.MediaPlayer;
import porprezhas.model.dices.Board;

import java.util.ArrayList;
import java.util.List;

public class GuiSettings {
    // ***** Game GUI attributes *****
    // path
    public static final String pathToBackground = "background/";
    public static final String pathToBorder = "border/";
    public static final String pathToToolCard = "cards/toolCard/";
    public static final String pathToPrivateCard = "cards/privateCard/";
    public static final String pathToPublicCard = "cards/publicCard/";
    public static final String pathToPattern = "pattern/";
    public static final String pathToDice = "dice/36x36/";
    public static final String pathToCursor = "cursor/";
    public static final String pathToMusic = "sound/music/";

    public static final String pathToBorderFile = pathToBorder + "border.gif";


    // number
    public static final int BOARD_COLUMN = 5;
    public static final int BOARD_ROW = 4;
    public static final int ICON_QUANTITY = 68;   // index from 0 to 67
    public static final int SOLITAIRE_WIDTH = 550;
    public static final double BOARD_DICE_ZOOM = 0.92;       //this value depends by dice image. for dice_64x64 zoom = 1.12 should be fine
    public static final double TRACK_DICE_ZOOM = 0.8086;       // NOTE: this value must be below 1.0!!! 0.8~0.96 are good.
    public static final double CARD_FIT_RATIO = 0.86;       // this should be below 1.0 and as big as possible
    public static final double CARD_PANE_PADDING = 15;       // adjust this value based on .fxml settings


    // ***** Develop use attributes *****
    public static final boolean bDebug = false;
    public static final boolean bShowGridLines = false;
    public static final boolean bShowFrames = false;

    // ***** User settings *****
    // View
    public static double FPS_PRINT_AT_MIN = 10;     // print frequency in a minute, unit = [1/60 Hz]
    public static double DRAFT_DICE_ZOOM = 0.96;    // this value must be below 2, and should be much bigger than 0.5
    public static double BORDER_SIZE = 1.8;         // should be around 2

    public static double cardOpacity = 0.8086;
    public static double CARD_FADE_IN = 420;
    public static double CARD_FADE_OUT = 1200;

    // Sound
    public static BooleanProperty bMuteMusic = new SimpleBooleanProperty(false);
    public static DoubleProperty musicVolume = new SimpleDoubleProperty(1.0);



    // ***** Global attributes *****
    // images

    // borders
//    public static Border cardBorder;


    // ***** initialization method *****
}
