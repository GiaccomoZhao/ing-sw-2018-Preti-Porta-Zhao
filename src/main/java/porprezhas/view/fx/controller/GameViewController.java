package porprezhas.view.fx.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.ImageCursor;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.ImagePattern;
import porprezhas.model.dices.Dice;
import porprezhas.model.dices.Pattern;
import porprezhas.view.fx.Image.DiceView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static porprezhas.view.fx.controller.EnemyViewController.*;

public class GameViewController {
    private final boolean bDebug = false;

    private final String pathToCursor = new String("cursor/");
    private final String pathToBackground = new String("background/");

    // these will be initialized by the FXMLLoader when the load() method is called
    @FXML private StackPane gamePane;   // fx:id="gamePane"
    @FXML private HBox playerPane;     // parent of board, used to resize board
    @FXML private VBox boardParent;        // parent of board, used to resize board
    @FXML private GridPane board;
    @FXML private VBox enemyPanesParent;
    @FXML private Button buttonPass;

    private ImageCursor cursorHand;
    private ImageCursor cursorHandDown;
    private ImageCursor cursorHandUp;


    private final int COLUMN = 5;
    private final int ROW = 4;


    //  ***** VIDEO Setting attributes *****
    // TODO: this can be imported in the VIDEO settings
    final double enemyPaneSpacingRatio = 0.3 ;   // should be in range [0, 1]

    final double buttonSpacingRatio = 0.3;        // sum of 2 button ratio should be in range [0,1], otherwise the board's size would decrease
    final double buttonIncreaseRatio = 0.3;       // if the sum == 1 then the board size would keep width/height ratio

    // these are the dimension we designed for enemy board
    final double desiredWidth = 260.0;
    final double desiredHeight = 160.0;
    final double referenceHeight = 500.0;   // this is the reference min height of the gamePane with 3 enemies
    final double paneWidthFactor = desiredWidth / desiredHeight;    // this is the ratio between width and height of a single enemy pane
    final double heightFactor = desiredHeight / referenceHeight;    // this is the ratio of enemy panel's height on entire game's height

    // dimension configured for player panel
    final double playerPaneWidth = 340.0;
    final double desiredRatio = 128.0 / 160.0;    // desiredHeight / desiredWidth
    final double playerPaneWidthFactor = 240.0 / playerPaneWidth;




    // Game Variable
    List<EnemyViewController> enemyViewControllers;
    Pane[] enemyPanes;
    List<GridPane> boardList;

    public static class PlayerInfo {
        public String name;
        public int iconId;
        public Pattern.TypePattern typePattern;

        public PlayerInfo(String name, int iconId, Pattern.TypePattern typePattern) {
            this.name = name;
            this.iconId = iconId;
            this.typePattern = typePattern;
        }
    }
    final List<PlayerInfo> playersInfo;

    final int num_player;


    // @requires playersInfo.size >= 1
    // @Param playersInfo.get(0).typePattern == player.typePattern &&
    //        forall( 1 <= i < playersInfo.size(); playersInfo.get(i).typePattern == enemies[i-1].typePattern
    public GameViewController(List<PlayerInfo> playersInfo) {
        if(bDebug)
            System.out.println("Constructing GameView");
        this.playersInfo = playersInfo;
        this.num_player = playersInfo.size();
        if(this.num_player > 1)
            enemyPanes = new Pane[this.num_player-1];
        else
            enemyPanes = null;
        boardList = new ArrayList<>(num_player);
        enemyViewControllers = new ArrayList<>();
/*        for (int i = 0; i < num_player; i++) {
            boardList.add(new GridPane());
        }*/
        if(bDebug)
            System.out.println("GameView Constructed");
    }

    // this will be called by JavaFX
    public void initialize() {
        if(bDebug)
            System.out.println("Initializing GameView");
        boardList.add(board);

        // Load enemy panels and setup them
        // add enemy boards
        try {
            setEnemyPanes();    //NOTE: If the program give error on loading fxml, the problem may be here.
        } catch (IOException e) {
            e.printStackTrace();
        }

        // setup our game GUI
        setGameCursor();
        setBackground();

        // add GamePane(include all players panel) Resize Listener
        setResizeListener();

        // add player panel(include board, button, etc.) Resize and Drag Listener
        setupPlayerPaneListener();

        // set pattern to all player
        setupPattern();



        // insert a lot of dices to test
        for (int col = 0; col < COLUMN; col++) {
            for (int row = 0; row < ROW; row++) {
                Random random = new Random();
                if (random.nextInt(10) < 6)
                    addDice(board, col, row,
                            new Dice(random.nextInt(6) + 1, Dice.ColorDice.values()[random.nextInt(Dice.ColorDice.values().length - 1)])
                    );
            }
        }
        for (int i = 0; i < enemyViewControllers.size(); i++) {
            GridPane board = enemyViewControllers.get(i).getBoard();
            for (int col = 0; col < COLUMN; col++) {
                for (int row = 0; row < ROW; row++) {
                    Random random = new Random();
                    if (random.nextInt(10) < 6)
                        addDice(board, col, row,
                                new Dice(random.nextInt(6) + 1, Dice.ColorDice.values()[random.nextInt(Dice.ColorDice.values().length - 1)])
                        );
                }
            }
        }
    }

    private void setupPattern() {
//        for (PlayerInfo playerInfo : playersInfo) {
        for (int i = 0; i < playersInfo.size(); i++) {
//            GridPane board = enemyPanes[i].getChildren();
            GridPane gridPane;
            PlayerInfo playerInfo = playersInfo.get(i);
            if( i == 0) {
                gridPane = this.board;
            } else {
                 gridPane = enemyViewControllers.get(i - 1).getBoard();
            }
            setPattern(gridPane, playerInfo.typePattern);
            if(bDebug)
                System.out.println(gridPane + "\t" + playerInfo.typePattern);
            boardList.add(gridPane);
        }
    }

    private void setupPlayerPaneListener() {
        addBoardResizeListener(board);  // used to adapt dice in the grid
        addBoardDragListener(board);    // implements dice dragging action
   }

    private void setEnemyPanes() throws IOException {
        enemyPanesParent.getChildren().clear();
        for (int i = 0; i < enemyPanes.length; i++) {
            // Load the panel from .fxml
            FXMLLoader loader = new FXMLLoader();   //NOTE: We must create more time loader to get multiple pane; Otherwise only one pane would be displayed
            loader.setLocation(getClass().getResource("/EnemyPaneView.fxml"));
            if(loader == null)
                System.err.println(this + ": Error with loader.setLocation(" + getClass().getResource("/EnemyPaneView.fxml") + ")");
            enemyPanes[i] = loader.load();

            // add the enemy panel on the game view
            enemyPanesParent.getChildren().add(enemyPanes[i]);

            // get controller
            EnemyViewController enemyViewController = loader.getController();
            enemyViewControllers.add(enemyViewController);
            // setup player info
            enemyViewController.setPlayerInfo(playersInfo.get(i + 1));
        }
    }

    private void setGameCursor() {
        cursorHand = new ImageCursor(
                new Image(pathToCursor + "cursor_hand.png", 64.0, 64.0, true, true),
                12, 12);
        cursorHandDown = new ImageCursor(
                new Image(pathToCursor + "cursor_hand_down.png") );
        cursorHandUp = new ImageCursor(
                new Image(pathToCursor + "cursor_hand_up.png") );

        gamePane.setCursor(cursorHand);
    }

    private void setBackground() {
        Background background = new Background(
                new BackgroundFill(new ImagePattern(
                        new Image(pathToBackground + "game.jpeg")),
                        CornerRadii.EMPTY, Insets.EMPTY));
        gamePane.setBackground(background);
    }

    private void setResizeListener() {
        gamePane.heightProperty().addListener( (observable, oldValue, newValue) -> {
            updateEnemyPaneSize();
            updatePlayerPaneSize();
        });

        gamePane.widthProperty().addListener((observable, oldValue, newValue) -> {
            updatePlayerPaneSize();
        });
/*            final double actualWidth = newValue.doubleValue();
            boolean bMinimum = actualWidth <= referenceHeight;
            if(bMinimum) {
                gamePane.setPadding(new Insets(0, 0, 0,
                        bMinimum ? 0 : actualWidth - actualWidth));
            }
        });
*/
    }

    // adjust enemy panel size and gap between panels
    private void updateEnemyPaneSize() {

        // My note:
        // H: 600 -> 3 x 160 = 480 + 4 x 120/4
        // H: 300 : 160 - 222
        // H: 600 : 600 - 222
        // W: 300 : 260 - 360

//        final double actualHeight = newValue.doubleValue();
        final double actualHeight = gamePane.getHeight();
        boolean bMinimum = actualHeight <= referenceHeight;
//            double minSpacing = (referenceHeight/3 *  (3- (num_player-1)));
        double totalSpacing =
                bMinimum ?
                        0 :
                        ( actualHeight - (referenceHeight) * (num_player-1)/3 ) * enemyPaneSpacingRatio;
        double paneHeight =
                bMinimum ?
                        actualHeight * heightFactor :
                        heightFactor * (actualHeight - totalSpacing);
        double paneWidth = paneHeight * paneWidthFactor;

        // if the height isn't narrow increase size and gap between enemy panels
        enemyPanesParent.setSpacing(totalSpacing / (num_player-1 +1));

        for ( Pane pane: enemyPanes) {
            pane.setPrefWidth(paneWidth);
            pane.setPrefHeight(paneHeight);
/*                if(enemyPanes.length == 1) {
                    enemyPanesParent.setPadding(new Insets((newValue.doubleValue() - enemyPanes[0].getHeight())/10, 0, 0, 0));
                }
*/
        }
    }

    // adjust Player panel size, working on button size and layout padding
    private void updatePlayerPaneSize() {
        double width = playerPane.getWidth();
        double height = playerPane.getHeight();
        double w = width * playerPaneWidthFactor;
        if (w > height / desiredRatio && true) {
            double inc = (w - height / desiredRatio);
            double spacing = inc * buttonSpacingRatio;
            double buttonSize = 68 + inc * buttonIncreaseRatio;
            buttonPass.setPrefWidth(buttonSize);
            ((Pane) buttonPass.getParent()).setPadding(new Insets(
                    spacing / 2, spacing / 2, spacing / 2, spacing / 2));
        }

        // 160x128
        // player pane width = 350; player board width = 240
        //

//            board.prefHeightProperty().bind(board.widthProperty().multiply(128.0/160.0));
//            board.prefWidthProperty().bind(board.heightProperty());
        // since prefHeightProperty().bind() doesn't work because we have auto fill checked.
        // we do this:


/*            // this is the accepted solution
            double width = playerPane.getWidth();
            double height = playerPane.getHeight();
            double w = width * playerPaneWidthFactor;
            if (w > height / desiredRatio && true) {
                double inc = (w - height / desiredRatio );
                double spacing = inc * buttonSpacingRatio;
                double buttonSize = 68 + inc * buttonIncreaseRatio;
                buttonPass.setPrefWidth(buttonSize);
                ((Pane) buttonPass.getParent()).setPadding(new Insets(
                        spacing / 2, spacing / 2, spacing / 2, spacing / 2));
//                System.out.println("inc = " + inc);
            }*/
/*             if( h > w * desiredRatio) {
                // height is higher, we adjust the height in base width
                w *= desiredRatio;
            } else {
                // adjust width in base height
                h /= desiredRatio;
            }
            boardParent.setPrefSize(w, h);

            System.out.println("\np width = " + playerPane.getWidth() + "\tp height = " + playerPane.getHeight());
            System.out.println("v width = " + boardParent.getWidth() + "\tv height = " + boardParent.getHeight());
            System.out.println("w = " + w + "\th = " + h);
*/    }

    public void updateSize() {
        updateEnemyPaneSize();
        updatePlayerPaneSize();
    }

    @FXML protected void onPass(ActionEvent event) {
        System.out.println("PASS");
    }
}
