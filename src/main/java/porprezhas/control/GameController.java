package porprezhas.control;

import porprezhas.model.Game;
import porprezhas.model.GameInterface;
import porprezhas.model.Player;
import porprezhas.model.dices.Board;
import porprezhas.model.cards.PrivateObjectiveCard;
import porprezhas.model.cards.PublicObjectiveCard;
import porprezhas.model.database.DatabaseInterface;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
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
        playTimeOut = new Object(); // Lock
    }

    public StateMachine getState() {
        return state;
    }

    public GameInterface getGame() {
        return game;
    }

    @Override
	public void run() {
        startGame();
        playerPrepare();
        gamePrepare();
        for (int iRound = 0; iRound < Game.GameConstants.ROUND_NUM; iRound++) {
            System.out.format("\nRound %-2d starts: {\n", iRound);
            playRound();
            System.out.println("}");
        }
        endGame();

        /* TODO: wait all players exit then thread terminates
         * let players chat after game
         * let players save game registration from GameDatabase
         * after exiting from game, player can download registration from ServerController
         * GameDatabase should be read by players in a hour time, then they will be cleared from server
         */
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
        if(null != privateObjectiveCardList) {
            for (PrivateObjectiveCard privateObjectiveCard : privateObjectiveCardList) {
                scorePrivate += privateObjectiveCard.apply(board);
            }
        }
//        System.out.println(player.getName() + ": sum of private objectives = " + scorePrivate);
//        logger.info("" + Player.getName() + "\tSum of private objectives = " + scorePrivate);

        // sum of public objectives
        List<PublicObjectiveCard> publicObjectiveCardList = game.getPublicObjectiveCardList();
        if(null != publicObjectiveCardList) {
            for (PublicObjectiveCard publicObjectiveCard : publicObjectiveCardList) {
                scorePublic += publicObjectiveCard.apply(board);
            }
        }
//        System.out.println(this + ": sum of public objectives = " + scorePublic);
//        logger.info("" + Player.getName() + "\tSum of public objectives = " + scorePublic);

        return scorePrivate + scorePublic;
    }

	// *********************************
    // ---------- Game Logic -----------
    // --------- Inner Methods ---------    can be private

	public void startGame() {
		System.out.println("\n\n\n***** >>> Game Start <<< *****\n");
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
	public synchronized void playRound() {
	    Player player;
	    List<Player> playerList = game.getPlayerList();
		state = StateMachine.PLAYING;
// TODO:            draftPool.reroll(); // remove and put new dices
		for (int i = 0; i < 2 * playerList.size(); i++) {
            player = game.getCurrentPlayer();

            Date dNow = new Date( );
            SimpleDateFormat ft = new SimpleDateFormat ("hh:mm:ss:SS");
            System.out.format("\tTurn of player n.%-2d  %-14s \t%s\n", player.getPosition(), player.getName(), ft.format(dNow));

            // let player play
            player.play();  // WARNING: this statement must be in the same synchronized block of wait()

            /* TODO: should i create a thread to receive actions?
             *
             * 3 optional actions:
             *   1. pick and place a Dice from Draft Pool to Board
             *   2. buy and use a Tool Card when player wants
             *   3. pass/finish
             */

            //wait everybody for a timeout then pass or player pass
			//possible other solution: Timer or while sleep nanoTime
            while(false == player.hasPassed()) {    // wait player passes or timeout make him pass the turn
                try {
                    synchronized(playTimeOut) {
                        playTimeOut.wait( (int) Game.GameConstants.secondsToMillis(
                                Game.GameConstants.TIMEOUT_ROUND_SOLITAIRE_SEC));
                        pass();
                        player.passes(true);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

			game.rotatePlayer();
		}
	}

	// player call this
	public synchronized void pass() {
        synchronized(playTimeOut) {
            playTimeOut.notifyAll();
        }
	}

	private int calcScore() {
		return 0;
	}

    public void endGame() {
        List<Player> playerList = game.getPlayerList();
        Player player;

        state = StateMachine.ENDING;

        System.out.println( "\n\n\n" +
                            "  ***** >>> GAME OVER <<< *****  \n\n" +
                            "              Score              \n");
        for (int i = 0; i < playerList.size(); i++) {
            player = playerList.get(i);
            System.out.format("    %-15s \t%5d\n",
                    playerList.get(i).getName(), calcScore(player));
        }

        // TODO: save all in databases
    }
}
