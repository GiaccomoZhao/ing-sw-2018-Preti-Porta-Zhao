package porprezha.control;

import java.rmi.Remote;

public interface GameControllerInterface extends Remote {
    // start a thread that manage whole game process
    void start();
    void pass();

    // TODO: server do not control game directly, they are momentary here for testing
    void playerPrepare();
    void gamePrepare();
    void playRound();
    void endGame();

}
