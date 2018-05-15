package porprezhas.control;

import porprezhas.model.GameInterface;
import porprezhas.model.Player;

import java.rmi.Remote;

public interface GameControllerInterface extends Remote {
    void startGame();       // startGame a thread that manage whole game process
    void pass();        // unblock thread
    int calcScore(Player player);
    StateMachine getState();    // when server want to know what is game doing -- running or waiting player exit
    GameInterface getGame();

    // do i put this in concrete class or in interface? depends by getState method
    public enum StateMachine {
        WAITING_FOR_PLAYER, // INITIAL STATE, Waiting for host's Start
        STARTED,            // Flag, STARTED < state < FINISHED means a game is running
        PLAYER_PREPARING,  // Give player cards etc.
        GAME_PREPARING,     // Place toolCard and public object and decide the first player
        PLAYING,            // During round phase
        FINISHED,           // Flag, state > FINISHED means game has already been finished
        ENDING;             // LAST STATE, Player watching score and chatting

        private static StateMachine[] states = values();    // make private static copy of the values avoids copying each time it is used

        public StateMachine getState() {
            return this;
        }

        // we have a cycle sequence state machine
        public StateMachine getNextState() {
            return this.ordinal() != StateMachine.ENDING.ordinal() ?
                    states[this.ordinal() + 1] :
                    StateMachine.WAITING_FOR_PLAYER;
        }

        public boolean isGameRunning() {
            return hasGameStarted() && !hasGameFinished();
        }

        public boolean hasGameStarted() {
            return this.getState().ordinal() >= StateMachine.STARTED.ordinal();
        }

        public boolean hasGameFinished() {
            return this.getState().ordinal() >= StateMachine.FINISHED.ordinal();
        }
    }
}
