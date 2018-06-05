package porprezhas.control;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerRMIInterface extends Remote {
    Boolean joinGame(String username) throws RemoteException;
    Boolean login(String username) throws  RemoteException;
    Boolean logout(String username) throws RemoteException;
    Boolean insertedDice(int dicePosition, int xBoard, int yBoard, String username) throws RemoteException;
    Boolean chooseDPattern(String namePattern) throws RemoteException;
    Boolean passUser(String username) throws RemoteException;
    Boolean usedToolCard() throws RemoteException;

}
