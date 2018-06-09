package porprezhas.view.fx.gameScene.component;

import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.ImagePattern;
import porprezhas.view.state.DiceContainer;
import porprezhas.model.dices.Dice;
import porprezhas.model.dices.Pattern;

import static porprezhas.view.fx.gameScene.GuiSettings.*;

public class BoardView extends GenericBoardView {// extends GridPane {
    private ImageView bag;
    private FlowPane tokens;
    private ImageView timer;

    // create a BoardView by passing a configured(may in FXML) GridPane
    public BoardView(int playerPosition, GridPane board, ImageView bag, FlowPane tokens, ImageView timer) {
        super(board, DiceContainer.fromPlayer(playerPosition));
        this.bag = bag;
        this.tokens = tokens;
        this.timer = timer;
    }

    public FlowPane getTokens() {
        return tokens;
    }

    public void showBag(boolean bShow) {
        bag.setVisible(bShow);
    }

    public void showTokens(boolean bShow) {
        tokens.setVisible(bShow);
    }

    public void showTimer(boolean bShow) {
        timer.setVisible(bShow);
    }

    // static field used for player pane
    public void setPattern(Pattern.TypePattern patternType) {

        Image patterImage = null;
        try {
            patterImage = new Image(pathToPattern + patternType.name().toLowerCase() + ".png");
        } catch (Exception e) {
            System.err.println("Can not load image from: " + pathToPattern + patternType.name().toLowerCase() + ".png");
//            e.printStackTrace();
        }
        if (null != patterImage) {

            Background patternImage = new Background(
                    new BackgroundFill(new ImagePattern(patterImage),
                            CornerRadii.EMPTY, Insets.EMPTY));
/*                    new BackgroundImage(
                            new Image(pathToPattern + Pattern.TypePattern.values()[1].name().toLowerCase() + ".png"),
                            BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT,
                            BackgroundPosition.CENTER, BackgroundSize.DEFAULT));
*/
            if (getBoard().getParent() instanceof GridPane)
                ((GridPane) (getBoard().getParent())).setBackground(patternImage);
            else {
                getBoard().setBackground(patternImage);
                System.err.println("Couldn't set background on parent: '" + getBoard().getParent() + "' that is not a GridPane.");
            }
        }
    }

    // Add Dice to Board, if it is free
    // return a reference to the Dice at position col, row
    // requires col < 5 && col > 0 &&
    //          row < 4 && row > 0
    @Override
    public DiceView addDice(Dice dice, int row, int col) { //int num, char color){
        return super.addDice(dice, row, col);
    }


}