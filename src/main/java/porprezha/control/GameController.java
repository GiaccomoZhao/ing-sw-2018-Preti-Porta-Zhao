package porprezha.control;

import porprezha.model.Game;
import porprezha.model.GameInterface;
import porprezha.model.Player;
import porprezha.model.database.DatabaseInterface;

import java.util.List;

public class GameController implements GameControllerInterface {

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


    // *********************************
    // ---------- Attributes -----------

    private Game game;
	private GameInterface gameInterface;
	private DatabaseInterface databaseInt;

    private StateMachine state;


    // *********************************
    // ----------- Methods -------------

    public GameController(Game game) {
        this.game = game;
    }

    public StateMachine getState() {
        return state;
    }


    // *********************************
    // ---------- Game Logic -----------

	public void start() {
		System.out.println("Game: Start");
		game.orderPlayers();
		state = StateMachine.STARTED;
		/* TODO: open a thread
        game.playerPrepare();
        game.gamePrepare();
        for (int iRound = 0; iRound < Game.GameConstants.ROUND_NUM; iRound++) {
            game.playRound();
        }
        game.endGame();
        */
	}


	/* @requires playerList.size() > 0
       @ensure (*in Solitaire*) &&
       @       (*Player choose a difficulty 1 to 5*) &&
       @       (*the Player receives 2 Private Objective Cards*) &&
       @       (*2 random PatternCard to choose one from 4 faces*) &&
       @       (*FavorTokens == 0*) ||
       @ensure (*in Multi-player game*) &&
       @       (*every Player receives: 2 random PatternCard to choose one from 4 faces*) &&
       @       (*a random hidden Private Objective Card*) &&
       @       (*a quantity of Favor Tokens equals to difficulty of Pattern Card*)
       @ */
	public void playerPrepare() {
        List<Player> playerList = game.getPlayerList();
        Player player = playerList.get(0);
		state = StateMachine.PLAYER_PREPARING;
		// wait playerList choose Pattern Card
		// with observer.update: player.choosePatternCard();
        if (game.isSolitaire()) {
			player.setFavorToken(0);
			player.takePrivateObjectCard(null); // TODO: 2 random cards
		} else {
			// give a random private card
			// give 2 random PatternCard to choose one from 4 faces
			// give favorTokens based on difficulty of patternCard
		}
	}


	/* @requires playerList.size() > 0
       @ensure (*in Solitaire*) &&
       @       (*place 2 Public Objective Cards*) &&
       @       (*place 1(difficulty=extreme) to 5(easy) Tool Cards*) ||
       @ensure (*in Multi-player*) &&
       @       (*place 3 Public Objective Cards*) &&
       @       (*place 3 Tool Cards*)
       @ */
	public void gamePrepare() {
		state = StateMachine.GAME_PREPARING;
//        randToolCard();
//        View.update();
        if (game.isSolitaire()) {
//			publicObjectiveCardList = new ArrayList<PublicObjectiveCard>(2);        // TODO: give them a concrete value
//			toolCardList = new ArrayList<ToolCard>(solitaireDifficulty.toToolCardsQuantity());
		} else {
//			publicObjectiveCardList = new ArrayList<PublicObjectiveCard>(3);
//			toolCardList = new ArrayList<ToolCard>(3);
		}
	}


	/*
       @ensure (*in Solitaire*) &&
       @       (*a Pool of 4 Dice, 2 times at round*) ||
       @ensure (*in Multi-player*) &&
       @       (*a Pool of 2 * PlayerList.size() + 1 Dices*)&&
       @       (*rotate clockwise and counter-clockwise, so every player has 2 times at round*)
       @ */
	public void playRound() {
	    List<Player> playerList = game.getPlayerList();
		state = StateMachine.PLAYING;
		for (int i = 0; i < 2 * playerList.size(); i++) {
// TODO:            draftPool.reroll(); // remove and put new dices
			//wait everybody for a timeout then pass or player pass
			game.getCurrentPlayer().play();   // let player play

			//TODO: put this in a thread, or use other solution like Timer or while sleep nanoTime
			try {
				wait(Game.GameConstants.secondsToMillis(
						Game.GameConstants.TIMEOUT_ROUND_SOLITAIRE_SEC));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			game.rotatePlayer();
		}
	}

	// player call this
	public void pass() {
		notifyAll();
	}

	private int calcScore() {
		return 0;
	}

	public void endGame() {
		System.out.println("Game: End");
		;
		System.out.println("Score = " + calcScore());
		state = StateMachine.ENDING;
	}

}
