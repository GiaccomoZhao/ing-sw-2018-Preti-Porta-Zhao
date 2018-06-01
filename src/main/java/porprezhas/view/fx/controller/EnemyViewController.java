package porprezhas.view.fx.controller;

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
import porprezhas.view.fx.component.BoardView;
import porprezhas.view.fx.component.DiceView;

import static porprezhas.view.fx.GuiSettings.*;

public class EnemyViewController {

    @FXML private HBox enemyPane;    // root of enemy pane
    @FXML private Label name;
    @FXML private ImageView icon;
    @FXML private GridPane board;

    BoardView boardView;    // this contain board

    GameViewController.PlayerInfo playerInfo;

/*    public EnemyViewController(GameViewController.PlayerInfo playerInfo) {
        this.playerInfo = playerInfo;
    }*/

    public void initialize() {
        if(bDebug) {
            System.out.println("Initializing EnemyView");
//            System.out.println(playerInfo);
        }
        boardView = new BoardView(board);

        // should i set a default background ? uncomment this : eliminate this
//        setPattern(board, defaultPattern);    // this will be called by GameViewController
    }


    public GridPane getBoard() {
        return boardView.getBoard();
    }

    public void setPlayerInfo(GameViewController.PlayerInfo playerInfo) {
        this.playerInfo = playerInfo;
        boardView.setPattern(playerInfo.typePattern);
        this.name.setText(playerInfo.name);
        this.icon.setImage(new Image(pathToHeadIcon + "head_" + playerInfo.iconId + ".png"));
    }

    // not static field is used for enemy pane
    public void setPattern(Pattern.TypePattern patternType) {
        boardView.setPattern(patternType);
    }

    public DiceView addDice(Dice dice, int col, int row) {
        return boardView.addDice(dice, col, row);
    }

    public boolean deleteDice(DiceView diceView) {
        return boardView.deleteDice(diceView);
    }

}
