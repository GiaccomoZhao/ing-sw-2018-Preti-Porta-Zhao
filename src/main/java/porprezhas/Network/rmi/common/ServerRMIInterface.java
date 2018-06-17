package porprezhas.Network.rmi.common;

import porprezhas.exceptions.diceMove.*;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface ServerRMIInterface extends Remote {
    Boolean joinGame(String username) throws RemoteException;
    Boolean login(String username) throws  RemoteException;
    Boolean logout(String username) throws RemoteException;
    Boolean insertedDice(long diceID, int rowBoard, int colBoard, String username)
            throws RemoteException,
            IndexOutOfBoundsException, // NotYourTurnException, AlreadyPickedException,
            BoardCellOccupiedException, EdgeRestrictionException, PatternColorRestrictionException, PatternNumericRestrictionException, AdjacentRestrictionException;
    Boolean chooseDPattern(String namePattern) throws RemoteException;
    Boolean passUser(String username) throws RemoteException;
    Boolean usedToolCard(String username, int toolCardID, ArrayList<Integer> paramList) throws RemoteException;

}
