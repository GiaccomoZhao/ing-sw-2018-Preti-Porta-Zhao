package porprezhas.view.fx.component;

import javafx.geometry.HPos;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import porprezhas.model.dices.Dice;

import java.util.List;

import static porprezhas.Useful.isValueBetweenInclusive;
import static porprezhas.model.Game.GameConstants.*;
import static porprezhas.view.fx.GuiSettings.*;

public class RoundTrackBoardView extends GenericBoardView {
    private VBox[] backGround;

    private Image backgroundImage;

    // Create a new BoardView by ... for RoundTrack, can be used for different
    public RoundTrackBoardView(int COLUMN, int ROW) {
        super(COLUMN, ROW);
        setDiceZoom(TRACK_DICE_ZOOM);
        backGround = new VBox[getCOLUMN()];
        for (int i = 0; i < COLUMN; i++) {
            backGround[i] = new VBox();
        }
        backgroundImage = new Image(pathToBackground + "track.jpeg");   // load image and save it, to avoid reading from disk during game
    }

    public void setup() {
        // add backgrounds and set background visibility
        for (int iRound = 0; iRound < getCOLUMN(); iRound++) {
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
            DiceView diceView = super.addDice(dice, indexRound, row);   // add dice in 0th row
            getBoard().setRowSpan(backGround[indexRound], ++row);       // row span -> 1
        }
    }

    // Add Dice to Round Track
    @Override
    public DiceView addDice(Dice dice, int indexRound, int nothing) {
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
    public boolean deleteDice(DiceView viewToDelete) {
        int col = viewToDelete.getColumn();
        int row = viewToDelete.getRow();
        if (getDiceMatrix(col, row).equals(viewToDelete.getDice())) {
            setDiceMatrix(null, col, row);  // eliminate selected cell
            // translate all the following cells up
            for (int i = row; i < getROW() - 1; i++) {
                Dice value = getDiceMatrix(col, i + 1);
                setDiceMatrix(value, col, i);
            }
            setDiceMatrix(null, col, getROW() - 1); // eliminate last row
//            if( getBoard().getChildren().remove(viewToDelete) ) {

            // correct the background size
            if(row == 0) {
                backGround[col].setVisible(false);
            } else {
                getBoard().setRowSpan(backGround[col], row);
            }

            // refresh GridPane elements using DiceViews of the matrix
            update();
            return true;
//            }
        }
        return false;
    }

    @Override
    public void update() {
//        super.update();   // NOTE: we can reuse the super method if we make super.addDice abstract
        getBoard().getChildren().clear();
        setup();        // there make difference with the super()
        for (int col = 0; col < getCOLUMN(); col++) {
            for (int row = 0; row < getROW(); row++) {
                if(null != getDiceMatrix(col, row)) {
                    addDice(getDiceMatrix(col, row), col, row);     // there make a big difference with the super().
                }
            }
        }
        show(-1, true);
    }

    @Override
    protected void addBoardDragListener() {
        // Override with a void method to disable direct drag and place on round track
        // we place dice in the track table dragging it on Round Track Number
    }


    // when the specified round is in bound: show the specified round
    //                                 else: show all
    public void show(int iRound, boolean bShow) {
        GridPane gridPane = getBoard();
        if (isValueBetweenInclusive(iRound, 1, ROUND_NUM)) {
            // show backGround, if there is at least one dice
            if(null != getDiceMatrix(iRound -1, 0))
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
                if(null != getDiceMatrix(i, 0))
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