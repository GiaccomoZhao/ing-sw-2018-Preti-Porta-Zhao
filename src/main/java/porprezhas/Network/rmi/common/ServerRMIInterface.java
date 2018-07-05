package porprezhas.Network.rmi.common;

import porprezhas.exceptions.diceMove.*;
import porprezhas.model.Game;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface ServerRMIInterface extends Remote {
    Boolean joinGame(String username, String ipClient, int portClient ) throws RemoteException;
    Boolean joinSinglePlayer(String username, String ipClient, int portClient,Game.SolitaireDifficulty solitaireDifficulty) throws RemoteException;
    int login(String username) throws  RemoteException;
    Boolean logout(String username) throws RemoteException;
    Boolean insertedDice(long diceID, int rowBoard, int colBoard, String username)
//    Boolean insertedDice(int index, int rowBoard, int colBoard, String username)
            throws RemoteException,
            IndexOutOfBoundsException, // NotYourTurnException, AlreadyPickedException,
            BoardCellOccupiedException, EdgeRestrictionException, PatternColorRestrictionException, PatternNumericRestrictionException, AdjacentRestrictionException;
    Boolean choosePattern(int patternIndex, String username) throws RemoteException;
    Boolean passUser(String username) throws RemoteException;
    Boolean usedToolCard(String username, int toolCardID, ArrayList<Integer> paramList) throws RemoteException;
    Boolean resumeGame(String username, String ipClient, int portClient) throws RemoteException;
    Boolean choosePrivate(String username, int choosen) throws RemoteException;
}
