package porprezhas.view.fx.component;

import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.ImagePattern;
import porprezhas.model.dices.Dice;
import porprezhas.model.dices.Pattern;

import static porprezhas.view.fx.GuiSettings.*;

public class BoardView extends GenericBoardView {// extends GridPane {


    // create a BoardView by passing a configured(may in FXML) GridPane
    public BoardView(GridPane board) {
        super(board);
    }


    // static field used for player pane
    public void setPattern(Pattern.TypePattern patternType) {
        Background patternImage = new Background(
                new BackgroundFill(new ImagePattern(
                        new Image(pathToPattern + patternType.name().toLowerCase() + ".png")),
                        CornerRadii.EMPTY, Insets.EMPTY));
/*                    new BackgroundImage(
                            new Image(pathToPattern + Pattern.TypePattern.values()[1].name().toLowerCase() + ".png"),
                            BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT,
                            BackgroundPosition.CENTER, BackgroundSize.DEFAULT));
*/
        ((GridPane) (getBoard().getParent())).setBackground(patternImage);
    }

    // Add Dice to Board, if it is free
    // return a reference to the Dice at position col, row
    // requires col < 5 && col > 0 &&
    //          row < 4 && row > 0
    @Override
    public DiceView addDice(Dice dice, int col, int row) { //int num, char color){
        if(null == getDiceMatrix(col, row))
            return super.addDice(dice, col, row);
        else
            return getDiceView(col, row);
    }

    @Override
    public boolean deleteDice(DiceView diceView) {
        int col = diceView.getColumn();
        int row = diceView.getRow();
//        if( diceView.getDice().equals( getDiceMatrix(col, row)) ) {
            setDiceMatrix(null, col, row);
            return getBoard().getChildren().remove(diceView);
//        }
//        return false;
    }

}