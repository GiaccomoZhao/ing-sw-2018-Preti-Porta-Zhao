package porprezhas.view.fx.gameScene.state;

import porprezhas.model.GameInterface;
import porprezhas.model.dices.Dice;

import java.rmi.RemoteException;

public class Playing implements GameViewStateInterface {
//    private GameViewInterface gameView;
//    public Playing(GameViewInterface gameView) {
//        this.gameView = gameView;
//    }



    @Override
    public Dice chooseDiceInDraft(int indexDraft) {
        return null;
    }

    @Override
    public boolean moveAnyDiceInBoard(int fromCol, int fromRow, int toCol, int toRow) {
        return false;
    }

    @Override
    public boolean exchangeDraftAndTrack(int indexDraft, int round, int rowRound) {
        return false;
    }

    @Override
    public boolean moveDiceDraftToBoard(int indexDraft, int round, int rowRound) {
        return false;
    }

    @Override
    public Dice chooseDiceValue(int value) {
        return null;
    }

    @Override
    public boolean moveSelectedDiceInBoard(int toCol, int toRow) {
        return false;
    }

    @Override
    public void pass() throws RemoteException {

    }

    @Override
    public int calcScore(int iPlayer) throws RemoteException {
        return 0;
    }

    @Override
    public GameViewStateInterface getState() throws RemoteException {
        return null;
    }

    @Override
    public GameInterface getGame() throws RemoteException {
        return null;
    }

    @Override
    public boolean useToolCard(int cardId) throws RemoteException {
        return false;
    }
}
