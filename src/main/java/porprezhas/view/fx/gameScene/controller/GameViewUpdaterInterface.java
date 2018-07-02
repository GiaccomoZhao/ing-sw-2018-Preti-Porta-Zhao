package porprezhas.view.fx.gameScene.controller;

import porprezhas.model.Player;
import porprezhas.model.cards.Card;
import porprezhas.model.dices.Dice;

import java.util.List;

public interface GameViewUpdaterInterface {
    void updatePlayerInfo(List<Player> players);
    void setupView(List<Player> players, List<Card> toolCards, List<Card> publicObjectiveCards, List<Card> privateObjectiveCards);

    void updateBoard(int idBoard, Dice[][] dices);
    void updateDraftPool(List<Dice> dices);
    void updateRoundTrack(List<Dice>[] dices);

    void updateTimer(Player player);
    void updateTokens(List<Player> players);
}
