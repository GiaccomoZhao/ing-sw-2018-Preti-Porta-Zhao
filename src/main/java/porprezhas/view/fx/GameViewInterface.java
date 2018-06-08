package porprezhas.view.fx;

import porprezhas.model.dices.Dice;
import porprezhas.view.fx.controller.GameViewController;

public interface GameViewInterface {
    void updateBoard(int idBoard, Dice[][] dices);
}
