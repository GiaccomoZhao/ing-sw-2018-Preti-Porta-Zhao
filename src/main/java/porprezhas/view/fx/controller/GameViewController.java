package porprezhas.view.fx.controller;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.util.Duration;
import porprezhas.Network.ClientActionInterface;
import porprezhas.Network.ClientActionSingleton;
import porprezhas.Network.RMIClientAction;
import porprezhas.model.*;
import porprezhas.model.cards.Card;
import porprezhas.model.cards.PrivateObjectiveCard;
import porprezhas.model.cards.PublicObjectiveCard;
import porprezhas.model.cards.ToolCard;
import porprezhas.model.dices.*;
import porprezhas.view.fx.GuiSettings;
import porprezhas.view.fx.component.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import static porprezhas.Useful.*;
import static porprezhas.view.fx.GuiSettings.*;

public class GameViewController implements GameViewUpdaterInterface {

    //  ***** JavaFX attributes *****
    // these will be initialized by the FXMLLoader when the load() method is called
    @FXML private AnchorPane gameScene;   // top parent layout
    @FXML private StackPane gamePane;   // fx:id="gamePane"

    @FXML private VBox enemyPanesParent;    // contain all enemies board

    @FXML private HBox playerPane;     // parent of playerBoard, used to resize playerBoard
    @FXML private GridPane playerBoard;
    @FXML private Button buttonPass;

//    @FXML private GridPane roundTrackDiceTable;
    @FXML private HBox roundTrack;
    @FXML private VBox diceListReference;   // this is used because grid pane didn't cover all the space of parent grid pane

    @FXML private HBox draftPoolParent;

    @FXML private TabPane tabPane;
    @FXML private StackPane toolCardPane;
    @FXML private StackPane privateCardPane;
    @FXML private StackPane publicCardPane;


    //  ***** VIDEO Setting attributes *****
    // TODO: this can be imported in the VIDEO settings
    private final double enemyPaneSpacingRatio = 0.3 ;  // should be in range [0, 1]

    private final double buttonSpacingRatio = 0.4;      // sum of 2 button ratio should be in range [0,1.4], otherwise the playerBoard's size would decrease
    private final double buttonIncreaseRatio = 1.0;     // if the sum == 1.4 then the playerBoard size would keep width/height ratio
                                                        // NB. this value of 1.4 may depends by monitor
    private final double defaultPassButtonMinSize = 60;          // this should be bigger than 60, for big resolution monitor would need a bigger value



    //  ***** Game ViewController attributes *****
    private List<EnemyViewController> enemyViewControllers;
    private Pane[] enemyPanes;      // panel of 0-3 enemies, contain board, name, icon
    //private List<VBox> roundTrackLists;

    private List<BoardView> boardList;      // board of all players
    private RoundTrackBoardView roundTrackBoard;
//    private List<DiceView>[] roundTrackDiceLists;   // make care about List<String> is not a Object. But our List<DiceView> is a Object.
    private DraftPoolView draftPoolView;

    private CardPane[] cardPanes;


    private ImageCursor cursorHand;
    private ImageCursor cursorHandDown;
    private ImageCursor cursorHandUp;

    private boolean[] bShowRoundTrackList;  // save which list are shown
    private boolean bShowRoundTrackDices;   // has clicked show_all button


    //  ***** Player attributes *****
    private final int playerPosition;       // this identify the client's player
    private final int num_player;
    private final List<PlayerInfo> playersInfo;

    public static class PlayerInfo {    // NOTE: should i create a new class?
        public final int position;
        public final String name;
        public final int iconId;
        public final Pattern.TypePattern typePattern;

        public PlayerInfo(int position, String name, int iconId, Pattern.TypePattern typePattern) {
            this.position = position;
            this.name = name;
            this.iconId = iconId;
            this.typePattern = typePattern;
        }
    }



    // *************************************
    // ********** <<< Methods >>> **********


    // @requires playersInfo.size >= 1
    // @Param playersInfo.get(0).typePattern == player.typePattern &&
    //        forall( 1 <= i < playersInfo.size(); playersInfo.get(i).typePattern == enemies[i-1].typePattern
    public GameViewController(List<PlayerInfo> playersInfo, int playerPosition) {   // NOTE: during the construction method the fxml variables haven't be set yet
        if(bDebug)
            System.out.println("Constructing GameView");
        this.playerPosition = playerPosition;
        this.playersInfo = playersInfo;
        this.num_player = playersInfo.size();
//        if(this.num_player > 1)
            enemyPanes = new Pane[this.num_player-1];
//        else
//            enemyPanes = new Pane[0];
        boardList = new ArrayList<>(num_player);

        enemyViewControllers = new ArrayList<>();

        roundTrackBoard = new RoundTrackBoardView(Game.GameConstants.ROUND_NUM, Game.GameConstants.MAX_DICE_PER_ROUND);
/*        for (int i = 0; i < num_player; i++) {
            boardList.add(new GridPane());
        }*/
//        roundTrackLists = new ArrayList<>();
/*        roundTrackDiceLists = new ArrayList[Game.GameConstants.ROUND_NUM];
        for (int i = 0; i < Game.GameConstants.ROUND_NUM; i++) {
            roundTrackDiceLists[i] = new ArrayList<>();
        }
*/
        bShowRoundTrackList = new boolean[Game.GameConstants.ROUND_NUM];
        bShowRoundTrackDices = true;

        draftPoolView = new DraftPoolView();

        cardPanes = new CardPane[CardTab.values().length];

        if(bDebug)
            System.out.println("GameView Constructed");
    }


    // this will be called by JavaFX
    public void initialize() {
        if(bDebug)
            System.out.println("Initializing GameView");

        // Load EnemyPanels; get their ViewController; setup the PlayerInfo;
        setEnemyPanes();    //NOTE: If the program give error on loading fxml, the problem may be here.

        // create BoardList and set pattern image to all player
        setupBoard();

        // attach RoundTrack dice table
        setupRoundTrack();

        setupDraftPool();

        setupCards();


        // setup our game GUI with following methods
        // Images
        setGameCursor();
        setBackground();

        // Listeners
        setResizeListener();        // add Resize Listener for GamePane(including all players panel, round track, etc)
        setupRoundTrackListener();  // add action listener for round track's dice list dropping

/*
        for (int i = 0; i < Game.GameConstants.ROUND_NUM; i++) {
            VBox vBox = new VBox();
            vBox.setAlignment(Pos.TOP_CENTER);
            vBox.setBorder(new Border(new BorderStroke(Color.RED,
                    BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
            roundTrackLists.add(vBox);
            roundTrackDiceTable.add(vBox, i, 1, 1, 8);
        }
*/
    }


    private void setupCards() {
        System.out.println("Setup Cards");

        cardPanes[CardTab.TOOL_CARD.ordinal()] = new CardPane(toolCardPane, CardTab.TOOL_CARD, pathToToolCard);
        cardPanes[CardTab.PRIVATE_CARD.ordinal()] = new CardPane(privateCardPane, CardTab.PRIVATE_CARD, pathToPrivateCard);
        cardPanes[CardTab.PUBLIC_CARD.ordinal()] = new CardPane(publicCardPane, CardTab.PUBLIC_CARD, pathToPublicCard);

        List<Card> toolCards = new ArrayList<>();
        toolCards.add(new ToolCard(Card.Effect.TC4));
        toolCards.add(new ToolCard(Card.Effect.TC2));
        toolCards.add(new ToolCard(Card.Effect.TC11));
        cardPanes[CardTab.TOOL_CARD.ordinal()].setupCardPane(toolCards);

        List<Card> privateObjectCards = new ArrayList<>();
        privateObjectCards.add(new PrivateObjectiveCard(Card.Effect.PRC1));
        privateObjectCards.add(new PrivateObjectiveCard(Card.Effect.PRC4));
        cardPanes[CardTab.PRIVATE_CARD.ordinal()].setupCardPane(privateObjectCards);

        List<Card> publicObjectCards = new ArrayList<>();
        publicObjectCards.add(new PublicObjectiveCard(Card.Effect.PUC10));
        cardPanes[CardTab.PUBLIC_CARD.ordinal()].setupCardPane(publicObjectCards);
    }

    private void setupCardPane(Pane cardPane, String pathToCards, List<String> cardsName) {
        if(bDebug) {
            System.out.println("Printing setupCardPane() for " + pathToCards);
        }
        cardPane.setBorder(new Border(new BorderStroke( Color.rgb(200, 200, 200),
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
/*

        Border cardBorder = new Border(new BorderImage(
                new Image(pathToBorderFile),
                new BorderWidths(BORDER_SIZE), Insets.EMPTY, // new Insets(10, 10, 10, 10),
                new BorderWidths(BORDER_SIZE), true,
                BorderRepeat.STRETCH, BorderRepeat.STRETCH
        ));
*/


        List<Label> labels = new ArrayList<>();
        for (int i = 0; i < cardsName.size(); i++) {
            if(bDebug) {
                System.out.println("loading: " + pathToCards + cardsName.get(i) + ".jpg"); }
            ImageView imageView = new ImageView(new Image(pathToCards + cardsName.get(i) + ".jpg"));
            imageView.fitWidthProperty().bind(cardPane.widthProperty().multiply(CARD_FIT_RATIO));
            imageView.fitHeightProperty().bind(cardPane.heightProperty().multiply(CARD_FIT_RATIO));
            imageView.setPreserveRatio(true);

            labels.add(new Label());
            labels.get(i).setGraphic(imageView);

            if(cardsName.size() == 1) {
                labels.get(i).translateXProperty().bind( cardPane.widthProperty().subtract(labels.get(i).widthProperty()).subtract(CARD_PANE_PADDING).multiply(0.5) );
                labels.get(i).translateYProperty().bind( cardPane.heightProperty().subtract(labels.get(i).heightProperty()).subtract(CARD_PANE_PADDING).multiply(0.5) );
            } else {
                labels.get(i).translateXProperty().bind(cardPane.widthProperty().subtract(labels.get(i).widthProperty()).subtract(CARD_PANE_PADDING).multiply((double) (i) / (cardsName.size() - 1)));
                labels.get(i).translateYProperty().bind(cardPane.heightProperty().subtract(labels.get(i).heightProperty()).subtract(CARD_PANE_PADDING).multiply((double) (i) / (cardsName.size() - 1)));
            }
//            labels.get(i).setBorder(cardBorder);
            labels.get(i).setOpacity(cardOpacity);

            // add transition animation
            FadeTransition fadeIn = new FadeTransition(Duration.millis(CARD_FADE_IN), labels.get(i));
            fadeIn.setFromValue(cardOpacity);
            fadeIn.setToValue(1.0f);

            FadeTransition fadeOut = new FadeTransition(Duration.millis(CARD_FADE_OUT), labels.get(i));
            fadeOut.setFromValue(1.0f);
            fadeOut.setToValue(cardOpacity);

            labels.get(i).setOnMouseEntered(event -> {
                Label source = ((Label)event.getSource());
                source.toFront();
                fadeIn.play();
            });

            labels.get(i).setOnMouseExited(event -> {
                Label source = ((Label)event.getSource());
                source.setOpacity(cardOpacity);
                fadeIn.stop();
                fadeOut.play();
            });
        }

        cardPane.getChildren().addAll(labels);

        cardPane.setOnSwipeLeft(event -> {
            // change tab to next one
            System.out.println("Swipe to left");
        });
    }


    // return a round number from 1 to ROUND_NUM
    private int getRoundNumberFromEvent(InputEvent event) throws Exception {
        Object eventSource;
        double eventX;
        double eventSceneX;
        if(event instanceof MouseEvent) {
            MouseEvent mouseEvent = (MouseEvent) event;
            eventSource = mouseEvent.getSource();
            eventX = mouseEvent.getX();
            eventSceneX = mouseEvent.getSceneX();
        } else if(event instanceof  DragEvent) {
            DragEvent dragEvent = (DragEvent) event;
            eventSource = dragEvent.getSource();
            eventX = dragEvent.getX();
            eventSceneX = dragEvent.getSceneX();
        } else
            throw new Exception();

        // if we have placed round number in a HBox
        if(eventSource instanceof HBox) {
            int iRound = (int) (eventX / ((HBox) eventSource).getWidth() * Game.GameConstants.ROUND_NUM);
            if (iRound > Game.GameConstants.ROUND_NUM)
                return Game.GameConstants.ROUND_NUM ;
            return iRound +1;
        }

        // if we are moving on a DiceView, We can simply return it's attribute
        else if (eventSource instanceof DiceView) {
            return ((DiceView) eventSource).getColumn();
        }

        // if we have a normal ImageView, We have to calculate his position in Parent
        else if (eventSource instanceof ImageView) {    // NOTE: 2018-5-25 we are using this currently
            ImageView imageView = ((ImageView) eventSource);
            if(imageView.getParent() instanceof  HBox) {
                HBox parent = (HBox) imageView.getParent();
                int iRound = (int) (imageView.getLayoutX() / parent.getWidth() * Game.GameConstants.ROUND_NUM);
//                System.out.println("Calculated round = " + iRound);
                if( iRound > Game.GameConstants.ROUND_NUM)
                    return Game.GameConstants.ROUND_NUM ;
                return iRound +1;
            }
        }

        // if we are moving on the Round Track
        else if (eventSource instanceof GridPane) {
            GridPane gridPane = ((GridPane) eventSource);
            Bounds bounds = gridPane.localToScene(gridPane.getBoundsInLocal());
            double x = eventSceneX;
            double width = bounds.getWidth();
            int iRound = (int) ((x - bounds.getMinX()) / width * Game.GameConstants.ROUND_NUM);
            if( iRound > Game.GameConstants.ROUND_NUM)
                return Game.GameConstants.ROUND_NUM ;
            return iRound +1;
        }

        // not in those case
        System.err.println(eventSource);
        throw new Exception();
    }

    private void setupDraftPool() {
        draftPoolView.setup(draftPoolParent);
    }



    private void setupRoundTrackListener() {
        // set show/hide round dice list listener on every round number image view
        for (Node node : roundTrack.getChildren()) {
            ImageView roundNumberImage = (ImageView) node;

            // drop down the list of dice
            roundNumberImage.setOnMouseEntered(event -> {
                try {
                    int iRound = getRoundNumberFromEvent(event);
                    showRoundTrackDices(iRound, true);
                } catch (Exception e) {
                    System.err.println(roundNumberImage);
                    e.printStackTrace();
                }
            });
            roundNumberImage.setOnDragEntered(event -> {
//                System.out.println("entered");
                try {
                    int iRound = getRoundNumberFromEvent(event);
                    showRoundTrackDices(iRound, true);
                } catch (Exception e) {
                    System.err.println(roundNumberImage);
                    e.printStackTrace();
                }
            });


            // NOTE: OnDragOver{acceptTransferModes(TransferMode.ANY)} is need for OnDragDropped
            roundNumberImage.setOnDragOver(event -> {
                event.acceptTransferModes(TransferMode.ANY);
                event.consume();
            });
            roundNumberImage.setOnDragDropped(event -> {
//                System.out.print("Dropped. \t");
                Dragboard dragboard = event.getDragboard();
                boolean success = false;
                if (dragboard.hasString()) {
                    String draggedString = dragboard.getString();

                    Scanner scanner = new Scanner(draggedString);
                    scanner.findInLine("board=");
                    int idBoardFrom = scanner.nextInt();
                    if(GuiSettings.bDebug) System.out.print("id board=" + idBoardFrom);

                    DiceView diceView = DiceView.fromString(draggedString);

                    // calculate place position
                    try {
                        int iRound = getRoundNumberFromEvent(event);
                        // place down
//                        System.out.println("round number = " + iRound);
                        // TODO: success = ClientActionInterface.moveDice(idBoardFrom, diceView.getIndexDice(), roundTrackBoard.getBoardId(), iRound, 666);
//                        if (null != addDiceToRoundTrack(diceView.getDice(), iRound-1)) {
                            success = true;
//                        }
                    }catch (Exception e){
                        System.err.println(roundNumberImage);
                        e.printStackTrace();
                    }
//                System.out.println(((GridPane)event.getSource()).getLayoutY() + " \t" + ((GridPane)event.getTarget()).getLayoutY());
//                System.out.println("Dropped to " + col + "\t" + row);
                }
                /* let the source know whether the string was successfully
                 * transferred and used */
                event.setDropCompleted(success);

                event.consume();
            });

            // close drop down list
            roundNumberImage.setOnMouseExited(event -> {
                Bounds bounds = roundNumberImage.localToScene(roundNumberImage.getBoundsInLocal());
                double x = event.getSceneX();
                double y = event.getSceneY();
                double leftBound = bounds.getMinX();
                double rightBound = bounds.getMaxX();
                double topBound = bounds.getMinY();
//                    System.out.println("x=" + x + "\ty=" + y + "\tleft=" + leftBound + " \tright=" + rightBound + " \ttop=" + topBound);
                // when the cursor went out of left or right or top bound, or on the bound limits
                if (!isValueBetween(x, leftBound, rightBound) ||
                        y <= topBound) {
//                        System.out.println("Exited");
                    try {
                        int iRound = getRoundNumberFromEvent(event);
                        showRoundTrackDices(iRound, false);
                    } catch (Exception e) {
                        System.err.println("" + roundNumberImage);
                    }
                }
            });
        }

        // BOARD's listener

        // close all round track lists
        roundTrackBoard.getBoard().setOnMouseExited(event -> {
            if(bDebug)
                System.out.println("Exit from round track board");
            showRoundTrackDices(-1, false);
        });

        // close the list when move out from the dice list, when we don't want show all
        roundTrackBoard.getBoard().setOnMouseMoved(event -> {
//            if(bDebug)
//                System.out.println("Move in round track board. \tbShow=" + bShowRoundTrackDices);

            // do close any list when player pressed show_all button
            if(bShowRoundTrackDices) {
                return;
            } else {    // or when some dice has been dragging
                boolean bDragging = false;
                for (BoardView boardView : boardList) {
                    if(boardView.isbDragging()) {
                        bDragging = true;
                        break;
                    }
                }
                if(bDragging == true)
                    return;
            }

            // hide all the list that is not current one, we show one list at time without button press
            try {
                int iRound = getRoundNumberFromEvent(event);
//                System.out.println("round = " + iRound);
                for (int i = 1; i <= Game.GameConstants.ROUND_NUM; i++) {
                    if(i != iRound && bShowRoundTrackList[i-1]) {
                        showRoundTrackDices(i, false);
                    }
                }
            } catch (Exception e) {
                System.err.println("" + event);
            }
        });
/*        roundTrack.setOnMouseMoved(event -> {
            onMovingInRoundTrack(event);
        });
*/    }


    private void setupRoundTrack() {
        if( roundTrack.getParent() instanceof GridPane) {
            GridPane parentGridPane = (GridPane) roundTrack.getParent();

//            roundTrackBoard.getBoard().prefWidthProperty().bind( parentGridPane.widthProperty() );
//            roundTrackBoard.getBoard().prefHeightProperty().bind( parentGridPane.heightProperty());

            if(bShowGridLines) {
                parentGridPane.setGridLinesVisible(true);
            }

            parentGridPane.add(roundTrackBoard.getBoard(), 0, 1, parentGridPane.getColumnConstraints().size(), parentGridPane.getRowConstraints().size()-1);
            roundTrackBoard.getBoard().toBack();
        }
        if(bShowFrames) {
            roundTrackBoard.getBoard().setBorder(new Border(new BorderStroke(Color.AQUA,
                    BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        }
        if(bShowGridLines) {
            roundTrackBoard.getBoard().setGridLinesVisible(true);
        }
        showRoundTrackDices(-1, bShowRoundTrackDices);  // use bShowRoundTrackDices that is initialized in constructor as the default value
        roundTrackBoard.setup();
    }

    private void setupBoard() {
        if(bDebug) {
            System.out.println("Setup board"); }
        boardList.clear();  // this must be redundant but i want keep safe

        for (int i = 0, offset = 0; i < playersInfo.size(); i++) {
            PlayerInfo playerInfo = playersInfo.get(i);
            // assign the boards to a list, for simplify the use
            if( i == getPlayerPosition()) {
                boardList.add(new BoardView(playerBoard, i));
                offset++;
            } else {
//                 gridPane = enemyViewControllers.get(i + offset).getBoard();
                boardList.add(new BoardView(
                        enemyViewControllers.get(i - offset).getBoard(),
                        i));
            }

            boardList.get(i).setPattern(playerInfo.typePattern);
            if(bDebug)
                System.out.println(boardList.get(i).getBoard() + " \tpatter=" + playerInfo.typePattern);
        }
    }

    private void setEnemyPanes() {
        if(bDebug) {
            System.out.println("Loading Enemy Pane"); }
        enemyPanesParent.getChildren().clear();
        for (int i = 0; i < enemyPanes.length; i++) {
            // Load the panel from .fxml
            FXMLLoader loader = new FXMLLoader();   //NOTE: We must create more time loader to get multiple pane; Otherwise only one pane would be displayed
            loader.setLocation(getClass().getResource("/EnemyPaneView.fxml"));
            if(loader == null)
                System.err.println(this + ": Error with loader.setLocation(" + getClass().getResource("/EnemyPaneView.fxml") + ")");
            try {
                enemyPanes[i] = loader.load();  // if there is a error, it may be caused by incorrect setting in EnemyPaneView.fxml
            } catch (IOException e) {
                e.printStackTrace();
            }

            if(bDebug) {
                System.out.println("Enemy Pane View Loaded successfully"); }

            // add the enemy panel on the game view
            enemyPanesParent.getChildren().add(enemyPanes[i]);

            // get controller
            EnemyViewController enemyViewController = loader.getController();
            enemyViewControllers.add(enemyViewController);
            // setup player info
            enemyViewController.setPlayerInfo(playersInfo.get(i >= playerPosition ? i+1 : i));
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

        gameScene.setCursor(cursorHand);
//        gamePane.setCursor(cursorHand);
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
            updateEnemyPaneSize(newValue.doubleValue());
//            updatePlayerPaneSize(playerPane.getWidth(), playerPane.getHeight());  // commented because we don't know the new value of height
        });

        playerPane.heightProperty().addListener((observable, oldValue, newValue) -> {
            updatePlayerPaneSize(playerPane.getWidth(), newValue.doubleValue());
        });
        playerPane.widthProperty().addListener((observable, oldValue, newValue) -> {
            updatePlayerPaneSize(newValue.doubleValue(), playerPane.getHeight());
        });

        ((Pane) roundTrack.getParent()).widthProperty().addListener((observable, oldValue, newValue) -> {
            updateRoundTrackSize(newValue.doubleValue(), ((Pane) roundTrack.getParent()).getHeight());
        });
        ((Pane) roundTrack.getParent()).heightProperty().addListener((observable, oldValue, newValue) -> {
            updateRoundTrackSize(((Pane) roundTrack.getParent()).getWidth(), newValue.doubleValue());
        });
    }

    // adjust enemy panel size and gap between panels
    private void updateEnemyPaneSize(double gamePaneHeight) {

        // these are the dimension we designed for enemy board
        final int designedEnemyNumber = 3;
        final double desiredEnemyPaneWidth = 260.0;
        final double desiredEnemyPaneHeight = 160.0;
        final double referenceEnemyPaneHeight = 500.0;   // this is the reference min height of the gamePane with 3 enemies
        final double enemyPaneWidthFactor = desiredEnemyPaneWidth / desiredEnemyPaneHeight;    // this is the ratio between width and height of a single enemy pane
        final double enemyPaneHeightFactor = desiredEnemyPaneHeight / referenceEnemyPaneHeight;    // this is the ratio of enemy panel's height on entire game's height

        // My note:
        // H: 600 -> 3 x 160 = 480 + 4 x 120/4
        // H: 300 : 160 - 222
        // H: 600 : 600 - 222
        // W: 300 : 260 - 360

        final double newHeight = gamePaneHeight;    // final is used to mark this read only
        boolean bMinimum = newHeight <= referenceEnemyPaneHeight;
//            double minSpacing = (referenceEnemyPaneHeight/3 *  (3- (num_player-1)));
        double totalSpacing =
                bMinimum ?
                        0 :
                        ( newHeight - (referenceEnemyPaneHeight) * (num_player-1)/designedEnemyNumber ) * enemyPaneSpacingRatio;
        double paneHeight =
                bMinimum ?
                        newHeight * enemyPaneHeightFactor :
                        enemyPaneHeightFactor * (newHeight - totalSpacing);
        double paneWidth = paneHeight * enemyPaneWidthFactor;

        // if the height isn't narrow increase size and gap between enemy panels
        enemyPanesParent.setSpacing(totalSpacing / (num_player-1 +1));  // num_player -1 == enemy_num;
                                                                        // +1 for spacing top and bottom too,
                                                                        // -1 if you just want increase the gap
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
    private void updatePlayerPaneSize(double playerPaneWidth, double playerPaneHeight) {
        if(bDebug)
            System.out.println("playerPane: w=" + playerPaneWidth + "\th=" + playerPaneHeight);

        // dimension configured for player panel
        final double playerPaneDesiredWidth = 340.0;
        final double playerPaneDesiredRatio = 128.0 / 160.0;    // desiredEnemyPaneHeight / desiredEnemyPaneWidth
        final double playerPaneWidthFactor = 240.0 / playerPaneDesiredWidth;

        double w = playerPaneWidth * playerPaneWidthFactor;
        if (w > playerPaneHeight / playerPaneDesiredRatio) {
            double inc = (w - playerPaneHeight / playerPaneDesiredRatio);
            double spacing = inc * buttonSpacingRatio;
            double buttonSize = defaultPassButtonMinSize + inc * buttonIncreaseRatio;
            buttonPass.setPrefWidth(buttonSize);
            ((Pane) buttonPass.getParent()).setPadding(new Insets(
                    spacing / 2, spacing / 2, spacing / 2, spacing / 2));
        } else {
            buttonPass.setPrefWidth(defaultPassButtonMinSize);
            ((Pane) buttonPass.getParent()).setPadding(new Insets(0, 0, 0, 0));
        }
   }

   // this resize only the round numbers
   private void updateRoundTrackSize (double roundTrackParentWidth, double roundTrackHeight) {
       if (bDebug)
           System.out.println("update round track: w=" + roundTrackParentWidth + "\th=" + roundTrackHeight);
       final double referenceButtonSize = 24;  // estimated button size, not necessary to have an accurate measure of this
       double height = roundTrackHeight;   // roundTrack number icon height
       double width = (roundTrackParentWidth - referenceButtonSize) / 10;  // roundTrack number icon width, 10 numbers
       if (width >= height) {
           width = height;     // adapt to height
       } else {
           height = width;     // adapt to width, cut height
       }
       for (Node node : roundTrack.getChildren()) {
           ImageView imageView = (ImageView) node;
           imageView.setFitHeight(width);  // height == width
           imageView.setFitWidth(width);
//            imageView.setPreserveRatio(true); // this is the default value
       }
   }




    // ******************************************
    // ********** <<< FXML Methods >>> **********

    @FXML protected  void onShowRoundTrackDices(ActionEvent event) {
        showRoundTrackDices(-1, !bShowRoundTrackDices);
    }

    @FXML protected void onMovingInRoundTrack(MouseEvent event) {
        try { return; } catch (Exception e) { e.printStackTrace(); }
        int i = (int) (event.getX() / ((HBox) event.getSource()).getWidth() *10);
        if( i > 10)
            i  = 9;
        System.out.println("Moving on " + (i+1) );
    }
    @FXML protected void onTabelEntered(MouseEvent event) {
        System.out.println("Tabel Entered!");
    }
    @FXML protected void onTabelExited(MouseEvent event) {
        System.out.println("Tabel Exited");
    }

    @FXML protected void onPass(ActionEvent event) {
        System.out.println("PASS");
        // TODO: ClientActionInterface.Pass();



        ClientActionSingleton.getClientAction().pass();
        // for test
        Random random = new Random();
        List<Dice> diceList = new ArrayList<>();
        for (int i = 0; i < Game.GameConstants.MAX_DICE_PER_ROUND; i++) {
            diceList.add(
                    new Dice(random.nextInt(6) + 1,
                            Dice.ColorDice.values()[random.nextInt(Dice.ColorDice.values().length - 1)])
            );
        }
        draftPoolView.reroll(diceList);
    }

    @FXML protected void onToolCardTab() {
        System.out.println("Tool Cards");;
    }
    @FXML protected void onPrivateCardTab() {
        System.out.println("Private Objectives");
    }
    @FXML protected void onPublicCardTab() {
        System.out.println("Public Objectives");
    }



    // ********************************************
    // ********** <<< Public Methods >>> **********


    public int getPlayerPosition() {
        return playerPosition;
    }

    public void setDraftPool(List<Dice> newDiceList) {
        draftPoolView.reroll(newDiceList);
    }

   public void updateSize() {
        updateEnemyPaneSize(gamePane.getHeight());
        updatePlayerPaneSize(playerPane.getWidth(), playerPane.getHeight());
        updateRoundTrackSize( ((Pane) roundTrack.getParent()).getWidth(),
                              ((Pane) roundTrack.getParent()).getHeight());
    }

    public DiceView addDice(int indexPlayer, Dice dice, int col, int row) {
        System.out.println(boardList);
        return boardList.get(indexPlayer).addDice(dice, col, row);
    }

    public DiceView addDiceToRoundTrack(Dice dice, int round0_9){
        DiceView diceImage = roundTrackBoard.addDice(dice, round0_9, 0);
//        if(null != diceImage)
//        diceImage.setPreserveRatio(false);

//        diceImage.fitWidthProperty().bind( diceListReference.widthProperty() );
//        diceImage.fitHeightProperty().bind( diceListReference.heightProperty().divide(Game.GameConstants.MAX_DICE_PER_ROUND));
        return diceImage;
    }



    //@Param iRound The number of the round dice list to show, from 1 to ROUND_NUM
    //              if it's out of range, we show/hide all
    public boolean showRoundTrackDices(int iRound, boolean bShow) {
//            System.out.println("round = " + iRound);
        GridPane gridPane = roundTrackBoard.getBoard();
        if(bShowGridLines)
            roundTrackBoard.getBoard().setGridLinesVisible(true);

        try {
            // show the specified round's dices
            if (isValueBetweenInclusive(iRound, 1, Game.GameConstants.ROUND_NUM) ){

                // set bShowRoundTrackDices false when all list are hidden
                bShowRoundTrackList[iRound -1] = bShow; // iRound is a index that starts from 1
                if(!bShow && bShowRoundTrackDices == true) {
                    int i;
                    for (i = 0; i < Game.GameConstants.ROUND_NUM; i++) {
                        if(bShowRoundTrackList[i] == true) {
                            break;
                        }
                    }
                    if(i == Game.GameConstants.ROUND_NUM) {
                        bShowRoundTrackDices = false;
                    }
                }

                // show-hide a list of dices
                roundTrackBoard.show(iRound, bShow);
            } else  {

                // set all booleans = bShow
                bShowRoundTrackDices = bShow;
                for (int i = 0; i < Game.GameConstants.ROUND_NUM; i++) {
                    bShowRoundTrackList[i] = bShow;
                }

                // show-hide all round track dices
                roundTrackBoard.show(iRound, bShow);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bShow;
    }



    // MVC interface methods

    public void updateBoard(int idBoard, Dice[][] dices) {
        boardList.get(idBoard).update(dices);
    }

    public void updateDraftPool(List<Dice> dices) {
        draftPoolView.update(dices);
    }

    public void updateRoundTrack(List<Dice>[] dices) {
        roundTrackBoard.update(dices);
    }

}
