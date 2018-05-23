package porprezhas.view.fx.controller;

import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.scene.paint.ImagePattern;
import porprezhas.model.dices.Dice;
import porprezhas.model.dices.Pattern;
import porprezhas.view.fx.Image.DiceView;

import java.util.Random;

public class EnemyViewController {
    private final boolean bDebug = false;

    private static final String pathToPattern = new String("pattern/");
    private static final String pathToIcon = new String("head/64x64/");
    private static final double DICE_ZOOM = 1.12;    // 1.2 would be already too big

    @FXML private HBox enemyPane;    // root of enemy pane
    @FXML private Label name;
    @FXML private ImageView icon;
    @FXML private GridPane board;

    private DiceView[][] diceView;

    private final int COLUMN = 5;
    private final int ROW = 4;

    GameViewController.PlayerInfo playerInfo;

/*    public EnemyViewController(GameViewController.PlayerInfo playerInfo) {
        this.playerInfo = playerInfo;
    }*/

    public void setPlayerInfo(GameViewController.PlayerInfo playerInfo) {
        this.playerInfo = playerInfo;
        setPattern(board, playerInfo.typePattern);
        this.name.setText(playerInfo.name);
        this.icon.setImage(new Image(pathToIcon + "head_" + playerInfo.iconId + ".png"));
    }

    public GridPane getBoard() {
        return board;
    }

    public void initialize() {
        if(bDebug) {
            System.out.println("Initializing EnemyView");
//            System.out.println(playerInfo);
        }
        addBoardResizeListener(board);  // NOTE of 2018-5-23: currently it is doing nothing
        addBoardDragListener(board);

        // should i set a default background ? uncomment this : eliminate this
//        setPattern(board, defaultPattern);    // this will be called by GameViewController

    }


    // Add Dice to Board
    // return a reference to the added Dice
    // requires col < 5 && col > 0 &&
    //          row < 4 && row > 0
    public static DiceView addDice(GridPane board, int col, int row, Dice dice) { //int num, char color){
//        System.out.println(pathToDice + dice.getDiceNumber() + dice.getColorDice().name().toLowerCase().charAt(0) + ".png");
        DiceView diceImage = new DiceView(dice, col, row);
//        diceImage.setFitHeight(32);
        diceImage.fitHeightProperty().bind(
                board.heightProperty().divide(board.getRowConstraints().size()).multiply(DICE_ZOOM));
        diceImage.fitWidthProperty().bind(
                board.widthProperty().divide(board.getColumnConstraints().size()).multiply(DICE_ZOOM));
//                board.getRowConstraints().get(row).prefHeightProperty().multiply(1));
//        diceImage.fitWidthProperty().bind(
//                board.getColumnConstraints().get(col).prefWidthProperty().multiply(1));
//        diceImage.setPreserveRatio(true);
        diceImage.setSmooth(true);
        diceImage.setCache(true);

        // Action
        addDiceDragListener(board, diceImage);

        board.add(diceImage, col, row);
        return diceImage;
    }
    public DiceView addDice(Dice dice, int col, int row) {
        diceView[col][row] = addDice(board, col, row, dice);
        return diceView[col][row];
    }

    public void deleteDice(DiceView diceView) {
        ((GridPane) diceView.getParent()).getChildren().remove(diceView);
    }
    public static void deleteDice(GridPane board, DiceView diceView) {
        board.getChildren().remove(diceView);
    }

    public static void addDiceDragListener(GridPane board, DiceView DiceView) {
        DiceView.setOnDragDetected(event -> {
            Dragboard db = DiceView.startDragAndDrop(TransferMode.MOVE);

            /* Put the image information on a dragBoard */
            ClipboardContent content = new ClipboardContent();
            content.putString(DiceView.toString());
            db.setContent(content);

            db.setDragView(DiceView.getImage(), DiceView.getFitWidth()/2, DiceView.getFitHeight()/2);

            event.consume();
        });

        DiceView.setOnDragDone(event -> {
            /* the drag and drop gesture ended */
            /* if the data was successfully moved, clear it */
            if (event.getTransferMode() == TransferMode.MOVE) {
                deleteDice(board, DiceView);//clear();
            }
            event.consume();
        });
    }

    public static void addBoardDragListener(GridPane board) {
        board.setOnDragDropped(event -> {
            /* data dropped */
            /* if there is a string data on dragboard, read it and use it */
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasString()) {
                // read Dice date
                DiceView diceView = new DiceView();
                diceView.fromString(db.getString());
//                System.out.println(db.getString());

                // calculate place position
                int nCol = board.getColumnConstraints().size();
                int nRow = board.getRowConstraints().size();
                int col = (int) (event.getX() / (board.getWidth() / nCol));
                int row = (int) (event.getY() / (board.getHeight() / nRow));
                if(col >= nCol)
                    col = nCol - 1;
                if(row >= nRow)
                    row = nRow - 1;

                // place down
                addDice(board, col, row, diceView.getDice());
                success = true;
//                System.out.println("Dropped to " + col + "\t" + row);
            }
            /* let the source know whether the string was successfully
             * transferred and used */
            event.setDropCompleted(success);

            event.consume();
        });
        board.setOnDragOver(event -> {
            /* data is dragged over the target */
            /* accept it only if it is not dragged from the same node
             * and if it has a string data */
            if (event.getGestureSource() != board &&
                    event.getDragboard().hasString()) {
                /* allow for both copying and moving, whatever user chooses */
                event.acceptTransferModes(TransferMode.MOVE);

                // change cursor image
//                ((Node) event.getSource()).setCursor(cursorHandDown);

/*                // calculate place position
                int nCol = board.getColumnConstraints().size();
                int nRow = board.getRowConstraints().size();
                int col = (int) (event.getX() / (board.getWidth() / nCol));
                int row = (int) (event.getY() / (board.getHeight() / nRow));
                if(col >= nCol)
                    col = nCol - 1;
                if(row >= nRow)
                    row = nRow - 1;

                System.out.println("Dropping to col = " + col + "\trow = " + row);
*/            }

            event.consume();
        });

        board.setOnDragEntered(event -> {
            /* the drag-and-drop gesture entered the target */
            /* show to the user that it is an actual gesture target */
//            System.out.println(this + "Entered");
            if (event.getGestureSource() != board &&
                    event.getDragboard().hasString()) {
                // change cursor image
//                ((Node) event.getSource()).setCursor(cursorHandDown);
            }

            event.consume();
        });

        board.setOnDragExited(event -> {
            /* mouse moved away, remove the graphical cues */
            // reset cursor image
//            board.setCursor(cursorHand);

            event.consume();
        });
    }


    // used to adapt dice in the grid
    public static void addBoardResizeListener(Pane pane) {
        ChangeListener<Number> stageSizeListener = (observable, oldValue, newValue) -> {
            // adjust dice size
/*            for (Node diceView :
                    ((List<Node>) pane.getChildren())) {
                DiceView diceView1 = (DiceView) diceView;
            }
*/
/*            // Add V/H Gap
            final double borderRatio = 10.0 / 700.0;
            double h = pane.getHeight();
            double border = borderRatio * h;

            ((GridPane)pane).setVgap(border);
            ((GridPane)pane).setHgap(border);
*/
/*            // Add Padding to resize the boxes at correct position
            // 800 x 700
            // border = 20-23
            // bottom = 80-83
            final double bottomRatio = 86.0 / 700.0;
            final double borderRatio = 10.0 / 700.0;
            double h = pane.getHeight();
            double bottom = bottomRatio * h;
            double border = borderRatio * h;
            h = h - border - bottom;
            double w  = 5.0 / 4.0 * h + 2 * border;
            pane.setPrefWidth(w);
            pane.setPadding(new Insets(border, border, bottom, border < 3 ? 0 : border-3));
*/
//            System.out.println(5.0 / 4.0 * pane.getHeight() + "\th = " + pane.getHeight());
        };
//        gamePane.widthProperty().addListener(stageSizeListener);
        pane.heightProperty().addListener(stageSizeListener);

//        System.out.println("1" + pane);

//        img.fitWidthProperty().bind(stage.widthProperty());

//        pane.prefHeightProperty().bind(
//                pane.widthProperty());
//        pane.prefWidthProperty().bind(pane.prefHeightProperty().multiply(
//                pane.getPrefWidth() / pane.getPrefHeight()));

//        List<Node> items = gamePane.getChildren();
    }

    // not static field is used for enemy pane
    public void setPattern(Pattern.TypePattern patternType) {
        Background patternImage = new Background(
                new BackgroundFill(new ImagePattern(
                        new Image(pathToPattern + patternType.name().toLowerCase() + ".png")),
                        CornerRadii.EMPTY, Insets.EMPTY));
        ((GridPane) (board.getParent())).setBackground(patternImage);
    }

    // static field used for player pane
    static public void setPattern(GridPane board, Pattern.TypePattern patternType) {
        Background patternImage = new Background(
                new BackgroundFill(new ImagePattern(
                        new Image(pathToPattern + patternType.name().toLowerCase() + ".png")),
                        CornerRadii.EMPTY, Insets.EMPTY));
/*                    new BackgroundImage(
                            new Image(pathToPattern + Pattern.TypePattern.values()[1].name().toLowerCase() + ".png"),
                            BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT,
                            BackgroundPosition.CENTER, BackgroundSize.DEFAULT));
*/
        ((GridPane) (board.getParent())).setBackground(patternImage);
    }
}
