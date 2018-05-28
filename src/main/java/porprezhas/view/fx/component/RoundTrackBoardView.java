package porprezhas.view.fx.component;

import javafx.scene.Node;
import javafx.scene.input.Dragboard;
import porprezhas.model.dices.Dice;

public class RoundTrackBoardView extends GenericBoardView {
    private final double DICE_ZOOM = 0.96;      // NOTE: this value must be below 1.0!!!


    // Create a new BoardView by ... for RoundTrack, can be used for different
    public RoundTrackBoardView(int COLUMN, int ROW) {
        super(COLUMN, ROW);
        setDiceZoom(DICE_ZOOM);
    }


    // Add Dice to Round Track
    @Override
    public DiceView addDice(Dice dice, int col, int nothing) {
        final int iRound = col;    // conversion of variable number to better understand its meaning
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
            DiceView diceView = super.addDice(dice, col, row);
            return diceView;
        }
        return null;
    }

    @Override
    public boolean deleteDice(DiceView viewToDelete) {
        int col = viewToDelete.getColumn();
        int row = viewToDelete.getRow();
        if (getDiceMatrix(col, row).equals(viewToDelete.getDice())) {
            setDiceMatrix(null, col, row);
            for (int i = row; i < getROW() - 1; i++) {
                Dice value = getDiceMatrix(col, i + 1);
                setDiceMatrix(value, col, i);
            }
            setDiceMatrix(null, col, getROW() - 1);
//            if( getBoard().getChildren().remove(viewToDelete) ) {
            update();
            return true;
//            }
        }
        return false;
    }

    @Override
    protected void addBoardDragListener() {
        // Override with a void method to disable direct drag and place on round track
        // we place dice in the track table dragging it on Round Track Number
    }
}