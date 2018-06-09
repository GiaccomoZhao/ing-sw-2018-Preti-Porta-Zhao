package porprezhas.view.fx.gameScene.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import porprezhas.view.fx.gameScene.component.BoardView;

import static porprezhas.view.fx.gameScene.GuiSettings.*;

public class EnemyViewController {

    @FXML private HBox fx_enemyPane;    // root of enemy pane
    @FXML private Label fx_name;
    @FXML private ImageView fx_icon;
    @FXML private GridPane fx_board;
    @FXML private ImageView fx_bag;
    @FXML private FlowPane fx_tokens;
    @FXML private ImageView fx_timeout;

//    BoardView boardView;    // this contain board

    GameViewController.PlayerInfo playerInfo;

/*    public EnemyViewController(GameViewController.PlayerInfo playerInfo) {
        this.playerInfo = playerInfo;
    }*/

    public void initialize() {
        if(bDebug) {
            System.out.println("Initializing EnemyView");
//            System.out.println(playerInfo);
        }
//        boardView = new BoardView(fx_board, -1);   // NOTE: -1 would create an error if you do not setPlayerInfo

        // should i set a default background ? uncomment this : eliminate this
//        setPattern(fx_board, defaultPattern);    // this will be called by GameViewController
    }

    public HBox getEnemyPane() {
        return fx_enemyPane;
    }

    public GridPane getBoard() {
        return fx_board;
    }

    public ImageView getBag() {
        return fx_bag;
    }

    public FlowPane getTokens() {
        return fx_tokens;
    }

    public ImageView getTimer() {
        return fx_timeout;
    }

    public void setPlayerInfo(GameViewController.PlayerInfo playerInfo) {
        this.playerInfo = playerInfo;
//        boardView.setPattern(playerInfo.typePattern);
        this.fx_name.setText(playerInfo.name);
        this.fx_icon.setImage(new Image(pathToHeadIcon + "head_" + (playerInfo.iconId+1) + ".png"));        // the name of heads start from 1

//        boardView.setIdBoard(DiceContainer.fromPlayer(playerInfo.position));
    }
/*
    public void setPattern(Pattern.TypePattern patternType) {
        boardView.setPattern(patternType);
    }

    public DiceView addDice(Dice dice, int row, int col) {
        return boardView.addDice(dice, row, col);
    }
*/
}
