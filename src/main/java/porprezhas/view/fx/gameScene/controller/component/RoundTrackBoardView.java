package porprezhas.view.fx.gameScene.controller.component;

import javafx.geometry.Bounds;
import javafx.geometry.HPos;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import porprezhas.Network.ClientActionSingleton;
import porprezhas.model.Game;
import porprezhas.model.GameConstants;
import porprezhas.view.fx.gameScene.state.DiceContainer;
import porprezhas.model.dices.Dice;

import java.util.List;
import java.util.Scanner;

import static porprezhas.Useful.isValueBetween;
import static porprezhas.Useful.isValueBetweenInclusive;
import static porprezhas.model.GameConstants.*;
import static porprezhas.model.GameConstants.ROUND_NUM;
import static porprezhas.view.fx.gameScene.GuiSettings.*;

public class RoundTrackBoardView extends GenericBoardView implements SubController {

    private final int ROUND_QUANTITY;
    private final int MAX_DICE_PER_ROUND;

    private HBox roundTrackNumbers;
    private VBox[] backGround;
    private Image backgroundImage;

    private boolean[] bShowRoundTrackList;  // save which list are shown
    private boolean bShowRoundTrackDices;   // has clicked show_all button



    // Create a new BoardView by ... for RoundTrack, can be used for different
    public RoundTrackBoardView(int ROW, int COLUMN) {
        super(DiceContainer.TRACK, ROW, COLUMN);

        setDiceZoom(TRACK_DICE_ZOOM);
        MAX_DICE_PER_ROUND = ROW;
        ROUND_QUANTITY  = COLUMN;
        backGround = new VBox[ROUND_QUANTITY];
        for (int i = 0; i < ROUND_QUANTITY; i++) {
            backGround[i] = new VBox();
        }
        backgroundImage = new Image(pathToBackground + "track.jpeg");   // load image and save it, to avoid reading from disk during game
        bShowRoundTrackList = new boolean[ROUND_QUANTITY];
        bShowRoundTrackDices = false;   // show round track at start? no!
    }

    public void setupBackground() {
        // add backgrounds and set background visibility
        for (int iRound = 0; iRound < ROUND_QUANTITY; iRound++) {
            getBoard().add(backGround[iRound], iRound, 0 );
            backGround[iRound].setVisible(false);   // default value
        }

        for (int i = 0; i < ROUND_NUM; i++) {
            Background background = new Background(
                    new BackgroundImage(
                            backgroundImage,
                            BackgroundRepeat.NO_REPEAT, BackgroundRepeat.REPEAT,
                            BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT));
            backGround[i].setBackground(background);
        }
        getBoard().toFront();
    }




    public boolean showRoundTrackDices(int iRound) {
        bShowRoundTrackDices = !bShowRoundTrackDices;
        return showRoundTrackDices(iRound, bShowRoundTrackDices);
    }

    //@Param iRound The number of the round dice list to show, from 1 to ROUND_NUM
    //              if it's out of range, we show/hide all
    public boolean showRoundTrackDices(int iRound, boolean bShow) {
//            System.out.println("round = " + iRound);
        if(bShowGridLines)
            this.getBoard().setGridLinesVisible(true);

        try {
            // show the specified round's dices
            if (isValueBetweenInclusive(iRound, 1, GameConstants.ROUND_NUM) ){

                // set bShowRoundTrackDices false when all list are hidden
                bShowRoundTrackList[iRound -1] = bShow; // iRound is a index that starts from 1
                if(!bShow && bShowRoundTrackDices == true) {
                    int i;
                    for (i = 0; i < GameConstants.ROUND_NUM; i++) {
                        if(bShowRoundTrackList[i] == true) {
                            break;
                        }
                    }
                    if(i == GameConstants.ROUND_NUM) {
                        bShowRoundTrackDices = false;
                    }
                }

                // show-hide a list of dices
                this.show(iRound, bShow);
            } else  {

                // set all booleans = bShow
                bShowRoundTrackDices = bShow;
                for (int i = 0; i < GameConstants.ROUND_NUM; i++) {
                    bShowRoundTrackList[i] = bShow;
                }

                // show-hide all round track dices
                this.show(iRound, bShow);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bShow;
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
        } else if(event instanceof DragEvent) {
            DragEvent dragEvent = (DragEvent) event;
            eventSource = dragEvent.getSource();
            eventX = dragEvent.getX();
            eventSceneX = dragEvent.getSceneX();
        } else
            throw new Exception();

        // if we have placed round number in a HBox
        if(eventSource instanceof HBox) {
            int iRound = (int) (eventX / ((HBox) eventSource).getWidth() * GameConstants.ROUND_NUM);
            if (iRound > GameConstants.ROUND_NUM)
                return GameConstants.ROUND_NUM ;
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
                int iRound = (int) (imageView.getLayoutX() / parent.getWidth() * GameConstants.ROUND_NUM);
//                System.out.println("Calculated round = " + iRound);
                if( iRound > GameConstants.ROUND_NUM)
                    return GameConstants.ROUND_NUM ;
                return iRound +1;
            }
        }

        // if we are moving on the Round Track
        else if (eventSource instanceof GridPane) {
            GridPane gridPane = ((GridPane) eventSource);
            Bounds bounds = gridPane.localToScene(gridPane.getBoundsInLocal());
            double x = eventSceneX;
            double width = bounds.getWidth();
            int iRound = (int) ((x - bounds.getMinX()) / width * GameConstants.ROUND_NUM);
            if( iRound > GameConstants.ROUND_NUM)
                return GameConstants.ROUND_NUM ;
            return iRound +1;
        }

        // not in those case
        System.err.println(eventSource);
        throw new Exception();
    }


    private void setupRoundTrackNumberListener() {
        // set show/hide round dice list listener on every round number image view
        for (Node node : roundTrackNumbers.getChildren()) {
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
                    scanner.useDelimiter(":|\\s");

                    scanner.findInLine("board=");
                    if (scanner.hasNextInt()) {
                        int idBoardFrom = scanner.nextInt();    // NOTE: watch out. We need have a space after the number.
                        if (bDebug) System.out.print("id board=" + idBoardFrom + ": \t");


                        DiceView diceView = DiceView.fromString(scanner.nextLine());

                        // calculate place position
                        try {
                            int iRound = getRoundNumberFromEvent(event);
                            // place down
//                        System.out.println("round number = " + iRound);
                            success = getParentController().moveDice(
                                    idBoardFrom, diceView,
                                    this.getBoardId().toInt(), iRound, 666);      // we don't care about the column value
//                        if (null != addDiceToRoundTrack(diceView.getDice(), iRound-1)) {
//                            success = true;
//                        }
                        } catch (Exception e) {
                            System.err.println(roundNumberImage);
                            e.printStackTrace();
                        }
//                System.out.println(((GridPane)event.getSource()).getLayoutY() + " \t" + ((GridPane)event.getTarget()).getLayoutY());
//                System.out.println("Dropped to " + col + "\t" + row);
                    }
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
        this.getBoard().setOnMouseExited(event -> {
            if(bDebug)
                System.out.println("Exit from round track board");
            showRoundTrackDices(-1, false);
        });

        // close the list when move out from the dice list, when we don't want show all
        this.getBoard().setOnMouseMoved(event -> {
//            if(bDebug)
//                System.out.println("Move in round track board. \tbShow=" + bShowRoundTrackDices);

            // do close any list when player pressed show_all Button
            if(bShowRoundTrackDices) {
                return;
            } else {    // or close when some dice has been Dragging
                boolean bDragging = false;
                for (BoardView boardView : getParentController().getBoardList()) {
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
                for (int i = 1; i <= GameConstants.ROUND_NUM; i++) {
                    if(i != iRound && bShowRoundTrackList[i-1]) {
                        showRoundTrackDices(i, false);
                    }
                }
            } catch (Exception e) {
                System.err.println("" + event);
            }
        });
/*        roundTrackNumbers.setOnMouseMoved(event -> {
            onMovingInRoundTrack(event);
        });
*/    }


    public void setup(HBox fx_roundTrack) {
        this.roundTrackNumbers = fx_roundTrack;

        if( roundTrackNumbers.getParent() instanceof GridPane) {
            GridPane parentGridPane = (GridPane) roundTrackNumbers.getParent();

//            this.getBoard().prefWidthProperty().bind( parentGridPane.widthProperty() );
//            this.getBoard().prefHeightProperty().bind( parentGridPane.heightProperty());

            if(bShowGridLines) {
                parentGridPane.setGridLinesVisible(true);
            }

            parentGridPane.add(this.getBoard(), 0, 1, parentGridPane.getColumnConstraints().size(), parentGridPane.getRowConstraints().size()-1);
            this.getBoard().toBack();
        }
        if(bShowFrames) {
            this.getBoard().setBorder(new Border(new BorderStroke(Color.AQUA,
                    BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        }
        if(bShowGridLines) {
            this.getBoard().setGridLinesVisible(true);
        }
        showRoundTrackDices(-1, bShowRoundTrackDices);  // use bShowRoundTrackDices that is initialized in constructor as the default value

        this.setupBackground();

        this.setupRoundTrackNumberListener();
    }




/*
    // add more dices in a column
    public void addDice(List<Dice> diceList, int indexRound) {
        final int iRound = indexRound;
        int row = 0;
        for (Node node : getBoard().getChildren()) {
            if (node instanceof DiceView) {
                if (getBoard().getColumnIndex(node) == iRound) {
                    row++;
                }
            }
        }
        for (Dice dice : diceList) {
            DiceView diceView = super.addDice(dice, row, indexRound);   // add dice in 0th row
            getBoard().setRowSpan(backGround[indexRound], ++row);       // row span -> 1
        }
    }
*/

    // Add a Dice to Round Track
    @Override
    public DiceView addDice(Dice dice, int nothing, int indexRound) {
        final int iRound = indexRound;    // conversion of variable number to better understand its meaning
        int row = 0;    // we just ignore row parameter
        for (Node node : getBoard().getChildren()) {
//            if(!(node instanceof Group)) {
            if (node instanceof DiceView) {
                if (getBoard().getColumnIndex(node) == iRound) {
                    row++;
                }
            }
        }
        if (row < MAX_DICE_PER_ROUND) {
            DiceView diceView = super.addDice(dice, row, indexRound);
            getBoard().setRowSpan(backGround[indexRound], row +1);
            GridPane.setHalignment(diceView, HPos.CENTER);
            return diceView;
        }
        return null;
    }

    // @requires row > 0 // that means there is at least one dice to eliminate
    @Override
    public void update(Dice[][] diceMatrix) {
        super.update(diceMatrix);
        show(-1, true);         // Show all rounds
    }

    public void update(List<Dice>[] dices) {
//        Dice[][] diceMatrix = new Dice[getROW()][ROUND_QUANTITY];

        getBoard().getChildren().clear();
        for (int round = 0; round < ROUND_QUANTITY; round++) {
            if(null != dices[round]) {
                for (int row = 0; row < MAX_DICE_PER_ROUND && row < dices[round].size(); row++) {
                    Dice dice = dices[round].get(row);
                    if (null != dice) {

                        super.addDice(dice, row, round);

                    } } }
        }
        show(-1, true);
    }

    @Override
    protected void addBoardDragListener() {
        // Override the board listener method with a empty method
        // to disable DIRECT drag and place on round track board
        // we place dice in the track table by DRAGGING it ON Round Track NUMBER!!!
        // see: setupRoundTrackNumberListener();
    }


    public int getIndexByDiceID(long diceID) {
        int indexCounter = 0;
        for (Node node : getBoard().getChildren()) {
            if (node instanceof DiceView) {
                DiceView diceView = (DiceView) node;
                if( diceView.getDiceID() == diceID ) {
                    return  indexCounter;
                }
                indexCounter++;
            }
        }
        return -1;
    }

    public boolean hasDiceInRound(int iRound0) {    // iRound0 is an index from 0
        for (Node node : getBoard().getChildren()) {
            if (node instanceof DiceView) {
                DiceView diceView = (DiceView) node;
                if( diceView.getColumn() == iRound0 ) {
                    return true;
                }
            }
        }
        return false;
    }
    // when the specified round is in bound: show the specified round
    //                                 else: show all
    private void show(int iRound, boolean bShow) {
        GridPane gridPane = getBoard();
        if (isValueBetweenInclusive(iRound, 1, ROUND_NUM)) {
            // show backGround, if there is at least one dice
            if(hasDiceInRound(iRound-1))
                backGround[iRound-1].setVisible(bShow);
            else
                backGround[iRound-1].setVisible(false);
            // show/hide the specified list of dices
            for (Node node : gridPane.getChildren()) {
                if (node instanceof ImageView) {
                    if (gridPane.getColumnIndex(node) == iRound - 1) {    // the index start from 0, while iRound from 1
                        node.setVisible(bShow);
//                            node.setDisable(bShow);
                    }
                }
            }
        } else {    // show all
            // show/hide backGround
            for (int i = 0; i < ROUND_NUM; i++) {
                if(hasDiceInRound(i))
                    backGround[i].setVisible(bShow);
                else
                    backGround[i].setVisible(false);
            }
            // show/hide all dices
            for (Node node : gridPane.getChildren()) {
                if (node instanceof ImageView) {
                    node.setVisible(bShow);
//                        node.setDisable(bShow);
                }
            }

        }

        // show/hide entire round track panel
        if (bShow) {
            gridPane.getParent().getParent().toFront();
        }
        else {
            gridPane.getParent().getParent().toBack();
        }
    }

}