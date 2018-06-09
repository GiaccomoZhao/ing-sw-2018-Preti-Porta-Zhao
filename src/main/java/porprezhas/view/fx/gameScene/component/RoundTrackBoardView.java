package porprezhas.view.fx.gameScene.component;

import javafx.geometry.HPos;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import porprezhas.view.state.DiceContainer;
import porprezhas.model.dices.Dice;

import java.util.List;

import static porprezhas.Useful.isValueBetweenInclusive;
import static porprezhas.model.Game.GameConstants.*;
import static porprezhas.view.fx.gameScene.GuiSettings.*;

public class RoundTrackBoardView extends GenericBoardView {
    private VBox[] backGround;

    private Image backgroundImage;

    private final int ROUND_QUANTITY;

    // Create a new BoardView by ... for RoundTrack, can be used for different
    public RoundTrackBoardView(int ROW, int COLUMN) {
        super(DiceContainer.TRACK, ROW, COLUMN);
        setDiceZoom(TRACK_DICE_ZOOM);
        ROUND_QUANTITY  = COLUMN;
        backGround = new VBox[ROUND_QUANTITY];
        for (int i = 0; i < ROUND_QUANTITY; i++) {
            backGround[i] = new VBox();
        }
        backgroundImage = new Image(pathToBackground + "track.jpeg");   // load image and save it, to avoid reading from disk during game
    }

    public void setup() {
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

    // Add Dice to Round Track
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
        if (row < getROW()) {
            DiceView diceView = super.addDice(dice, indexRound, row);
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
        Dice[][] diceMatrix = new Dice[getROW()][ROUND_QUANTITY];

        getBoard().getChildren().clear();
        for (int col = 0; col < ROUND_QUANTITY; col++) {
            for (int row = 0; row < getROW(); row++) {
                if(null != diceMatrix[row][col]) {
                    super.addDice(diceMatrix[row][col], row, col);
                }
            }
        }
        show(-1, true);
    }

    @Override
    protected void addBoardDragListener() {
        // Override with a empty method to disable DIRECT drag and place on round track
        // we place dice in the track table by DRAGGING it ON Round Track NUMBER!!!
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
    public void show(int iRound, boolean bShow) {
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