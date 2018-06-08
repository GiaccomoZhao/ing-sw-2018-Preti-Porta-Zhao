package porprezhas.view.fx.controller;

import porprezhas.model.dices.Dice;

import java.util.List;

public interface GameViewUpdaterInterface {
    void updateBoard(int idBoard, Dice[][] dices);
    void updateDraftPool(List<Dice> dices);
    void updateRoundTrack(List<Dice>[] dices);
}
