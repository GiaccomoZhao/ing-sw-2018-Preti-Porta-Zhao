package porprezhas.view.fx;

public class GuiSettings {
    // path
    public static final String pathToPattern = new String("pattern/");
    public static final String pathToBackground = new String("background/");
    public static final String pathToCursor = new String("cursor/");

    // number
    public static final int ICON_QUANTITY = 145;   // index from 0 to 144
    public static final int SOLITAIRE_WIDTH = 550;

    // ***** Develop use attributes *****
    public static final boolean bDebug = false;
    public static final boolean bShowGridLines = false;
    public static final boolean bShowFrames = false;

    // User settings:
    public static double DRAFT_DICE_ZOOM = 1.2;     // this value must be below 2, and should be much bigger than 0.5
}
