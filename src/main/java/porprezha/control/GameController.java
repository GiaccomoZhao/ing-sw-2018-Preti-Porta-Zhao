package porprezha.control;

import porprezha.model.Game;
import porprezha.model.GameInterface;
import porprezha.model.Player;
import porprezha.model.cards.Board;
import porprezha.model.cards.PrivateObjectiveCard;
import porprezha.model.cards.PublicObjectiveCard;
import porprezha.model.database.DatabaseInterface;
import sun.rmi.runtime.Log;

import java.util.List;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class GameController implements GameControllerInterface, Runnable {
    static Logger logger = Logger.getLogger(GameController.class.getName());


    // *********************************
    // ---------- Attributes -----------

	private GameInterface game;
	private DatabaseInterface databaseInt;
    private StateMachine state;

    private Object playTimeOut;


    // *********************************
    // ----------- Methods -------------

    public GameController(GameInterface game) {
        this.game = game;
    }

    public StateMachine getState() {
        return state;
    }

	@Override
	public void run() {
        playerPrepare();
        gamePrepare();
        for (int iRound = 0; iRound < Game.GameConstants.ROUND_NUM; iRound++) {
            System.out.format("%-2d Round starts:\n", iRound);
            playRound();
        }
        endGame();

        /* TODO: wait all players exit then thread terminates
         * let players chat after game
         * let players save game registration from GameDatabase
         * after exiting from game, player can download registration from ServerController
         * GameDatabase should be read by players in a hour time, then they will be cleared from server
         */
	}


    // player call this
    public void pass() {
        notifyAll();
    }


    /* @requires parameter != null
     * @ensure (*return sum of all game's public and player's private objectives*)
     */
    public int calcScore(Player player) {
        int scorePublic = 0;
        int scorePrivate = 0;
        Board board = player.getBoard();

        // sum of private objectives
        List<PrivateObjectiveCard> privateObjectiveCardList = privateObjectiveCardList = player.getPrivateObjectiveCardList();
        for (PrivateObjectiveCard privateObjectiveCard : privateObjectiveCardList) {
            scorePrivate += privateObjectiveCard.apply(board);
        }
//        System.out.println(this + ": sum of private objectives = " + scorePrivate);
        logger.info("Sum of private objectives = " + scorePrivate);

        // sum of public objectives
        List<PublicObjectiveCard> publicObjectiveCardList = game.getPublicObjectiveCardList();
        for (PublicObjectiveCard publicObjectiveCard : publicObjectiveCardList) {
            scorePublic += publicObjectiveCard.apply(board);
        }
//        System.out.println(this + ": sum of public objectives = " + scorePublic);
        logger.info("Sum of public objectives = " + scorePublic);

        return scorePrivate + scorePublic;
    }

	// *********************************
    // ---------- Game Logic -----------
    // --------- Inner Methods ---------    can be private

	public void start() {
		System.out.println("Game: Start");
		game.orderPlayers();
		state = StateMachine.STARTED;
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
        System.out.println("Players Preparing");
        List<Player> playerList = game.getPlayerList();
        Player player = playerList.get(0);
		state = StateMachine.PLAYER_PREPARING;
		// wait playerList choose Pattern Card
		// with observer.update: player.choosePatternCard();
        if (game.isSolitaire()) {
			player.setFavorToken(0);
			player.setPrivateObjectCardList(null); // TODO: 2 random cards
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
        System.out.println("Game Preparing");
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
        System.out.println("Start to Play");
	    Player player;
	    List<Player> playerList = game.getPlayerList();
		state = StateMachine.PLAYING;
// TODO:            draftPool.reroll(); // remove and put new dices
		for (int i = 0; i < 2 * playerList.size(); i++) {
            player = game.getCurrentPlayer();
            System.out.format("%20s plays", player.getName());

            // let player play
            player.play();

            /* TODO: should i create a thread to receive actions?
             *
             * 3 optional actions:
             *   1. pick and place a Dice from Draft Pool to Board
             *   2. buy and use a Tool Card when player wantss
             *   3. pass/finish
             */

            //wait everybody for a timeout then pass or player pass
			//possible other solution: Timer or while sleep nanoTime
            while(false == player.hasPassed()) {    // wait player passes or timeout make him pass the turn
                try {
                    wait(Game.GameConstants.secondsToMillis(
                            Game.GameConstants.TIMEOUT_ROUND_SOLITAIRE_SEC));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

			game.rotatePlayer();
		}
	}


    public void endGame() {
        List<Player> playerList = game.getPlayerList();
        Player player;

        state = StateMachine.ENDING;

        System.out.println( "\n\n\t\tGAME OVER\n\n\n" +
                                "\t\t  Score  \n");
        for (int i = 0; i < playerList.size(); i++) {
            player = playerList.get(i);
            System.out.format("  %20s\t%-5d\n",
                    playerList.get(i), calcScore(player));
        }

        // TODO: save all in databases
    }
}
