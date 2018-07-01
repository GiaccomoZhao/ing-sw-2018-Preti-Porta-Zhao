package porprezhas.control;

import porprezhas.exceptions.diceMove.NotYourTurnException;
import porprezhas.model.GameConstants;
import porprezhas.model.GameInterface;
import porprezhas.model.Player;
import porprezhas.model.cards.ToolCard;
import porprezhas.model.cards.ToolCardParam;
import porprezhas.model.database.DatabaseInterface;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

public class GameController  implements GameControllerInterface, Runnable {
    static Logger logger = Logger.getLogger(GameController.class.getName());


    // *********************************
    // ---------- Attributes -----------

	private GameInterface game;
	private DatabaseInterface databaseInt;
    private StateMachine state;

    private final Object playTimeOut;
    private final Object chooseTimeOut;


    // *********************************
    // ---------- Game Logic -----------
    // --------- Inner Methods ---------    can be private

    private void startGame() {
        System.out.println("" + "\n\n\n***** >>> Game Start <<< *****\n");
        state = StateMachine.STARTED;

        game.playerPrePrepare();

        System.out.println("" + "Players Preparing");
        state = StateMachine.PLAYER_PREPARING;
    }

    private void setup() {
        System.out.println("" + "Game Preparing");
        state = StateMachine.GAME_PREPARING;

        game.playerPostPrepare();

        game.gamePrepare();
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
/*    private void playerPrepare() {
        List<Player> playerList = game.getPlayerList();

        if (game.isSolitaire()) {
            // select a difficulty then
            Player player = playerList.get(0);
            player.setFavorToken(0);    // single player uses dice instead of tokens
            player.setPrivateObjectCardList(null);
        } else {
            // wait playerList choose Pattern Card
            // with observer.update: player.choosePatternCard();

            // give a random private card
            // give 2 random PatternCard to choose one from 4 faces
            // give favorTokens based on difficulty of patternCard
            for (Player player: playerList) {
                player.setPrivateObjectCardList(new ArrayList<>( ));
                if(player.getBoard() == null)
                {
                    Pattern pattern = new Pattern(Pattern.TypePattern.KALEIDOSCOPIC_DREAM); //NOTE: this should be one of 2 random pattern of player
//                    game.setPattern(player, pattern);
                }
            }
        }
    }

*/
    /* @requires playerList.size() > 0
       @ensure (*in Solitaire*) &&
       @       (*place 2 Public Objective Cards*) &&
       @       (*place 1(difficulty=extreme) to 5(easy) Tool Cards*) ||
       @ensure (*in Multi-player*) &&
       @       (*place 3 Public Objective Cards*) &&
       @       (*place 3 Tool Cards*)
       @ */
/*    private void gamePrepare() {
        System.out.println("Game Preparing");
        state = StateMachine.GAME_PREPARING;
//        randToolCard();
//        View.update();
        if (game.isSolitaire()) {
//			publicObjectiveCardList = new ArrayList<PublicObjectiveCard>(2);
//			toolCardList = new ArrayList<ToolCard>(solitaireDifficulty.toToolCardsQuantity());
        } else {
//			publicObjectiveCardList = new ArrayList<PublicObjectiveCard>(3);
//			toolCardList = new ArrayList<ToolCard>(3);
        }
    }
*/

    /*
       @ensure (*in Solitaire*) &&
       @       (*a Pool of 4 Dice, 2 times at round*) ||
       @ensure (*in Multi-player*) &&
       @       (*a Pool of 2 * PlayerList.size() + 1 Dices*)&&
       @       (*rotate clockwise and counter-clockwise, so every player has 2 times at round*)
       @ */
    private synchronized void playRound() {
        Player player;
        List<Player> playerList = game.getPlayerList();
        state = StateMachine.PLAYING;
        for (int i = 0; i < 2 * playerList.size(); i++) {
            player = game.getCurrentPlayer();


            Date dNow = new Date( );
            SimpleDateFormat ft = new SimpleDateFormat ("hh:mm:ss:SS");
            System.out.format("\tTurn of player n.%-2d  %-14s \t%s\n", player.getPosition() + 1, player.getName(), ft.format(dNow));

            // let player play if the player isn't frozen
                player.play();  // NOTE: this statement must be in the same synchronized block of wait()
            if (game.isfreeze(player))
                player.passes(true);

            //wait everybody for a timeout then pass or player pass
            //possible other solution: Timer or while sleep nanoTime
            while(false == player.hasPassed()) {    // wait player passes or timeout make him pass the turn
                try {
                    synchronized(playTimeOut) {
                        if(game.getCurrentPlayer().getName().toUpperCase().contains("ZX"))      // NOTE: test use, remove this
                            playTimeOut.wait( game.getRoundTimeOut() );
                        else  {
                            for (Player p : playerList) {
                                if(p.getName().toUpperCase().contains("ZX")) {
                                    playTimeOut.wait(game.getRoundTimeOut()/50);
                                    player.passes(true);
                                    break;
                                }
                            }
                            if (!player.hasPassed())
                                playTimeOut.wait(game.getRoundTimeOut());
                        }

                        pass();
                        player.passes(true);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            game.rotatePlayer();
            game.newTurn();
        }
    }

    private void endGame() {
        List<Player> playerList = game.getPlayerList();

        state = StateMachine.ENDING;
        game.calcAllScore();
        // print Board!
        System.out.println("" +  "\n\n");
        for (Player player : playerList) {
            System.out.println("\nPlayer name: " + player.getName());
            player.getBoard().print(true);
        }

        System.out.println( "\n\n\n" +
                "  ***** >>> GAME OVER <<< *****  \n\n" +
                "              Score              \n");
        for (Player player : playerList) {
            System.out.format("    %-15s \t%4d\n",
                    player.getName(), calcScore(player));
        }

    }



    // *********************************
    // -------- Public Methods ---------

    public GameController(GameInterface game)  {

        this.game = game;
        playTimeOut = new Object(); // Lock
        chooseTimeOut = new Object(); // Lock
//        pass(); // notifyAll to clear all the locks


    }

    public StateMachine getState() {
        return state;
    }

    public GameInterface getGame() {
        return game;
    }

    @Override
	public void run() {
        // Give player private card and then the pattern to choose
        startGame();

        // wait player choose a pattern for a certain time
        boolean bChosen = false;
        while(!bChosen) {
            try {
                synchronized(chooseTimeOut) {
                    chooseTimeOut.wait( GameConstants.secondsToMillis(
                            GameConstants.TIMEOUT_PREPARING_SEC));
                    chooseTimeOut.notifyAll();
                    setup();
                    bChosen = true;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // ROUND phase
        for (int iRound = 0; iRound < GameConstants.ROUND_NUM; iRound++) {
//            System.out.format("\nRound %-2d starts: {\t%d\n", iRound + 1, game.getDiceBag().diceBagSize());
            System.out.format("" + "\nRound %-2d starts: {\n", iRound + 1);
            game.newRound(iRound);      // Prepare for the new round: Setup a new DraftPool
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

    @Override
    public String toString() {
        return "GameController: ";
    }



// *********************************
    // ------- External Methods --------            // Clients call these
    // TODO: command Pattern?

    /* @requires parameter != null
     * @ensure (*return sum of all game's public and player's private objectives*)
     * @param Player who have to be calculated, can be different from the player who asks
     */
    public int calcScore(Player player) {
        return game.calcScore(player);
    }

    public void pass() {
        synchronized(playTimeOut) {
            playTimeOut.notifyAll();
        }
    }

    /**
     *
     * @param player    the player who chooses the pattern
     * @param indexPatternType  the index of pattern's list that this player can choose
     * @return  bSuccess
     */
    public boolean choosePattern(Player player, int indexPatternType) {      //NOTE: pattern or indexPattern?
	                                                                 // for anti-cheat we must use indexPattern and verify the player is true
        boolean bSet = false;
        boolean bAllSet = true;
	    if(state.equals(StateMachine.PLAYER_PREPARING)) {
	        bSet = game.setPattern(player, indexPatternType);
        }
        // unlock timeout when all player has chosen
        for (Player p : game.getPlayerList()) {
            if(null == p.getBoard()) {      // when pattern hasn't be chosen, the board is created together the pattern
                bAllSet = false;
            }
        }
        if(bAllSet) {
            synchronized (chooseTimeOut) {
                chooseTimeOut.notifyAll();
            }
        }
        return bSet;
    }

    public boolean insertDice(String username, long diceID, Integer row, Integer column)  throws NotYourTurnException{
        if (!this.game.getCurrentPlayer().getName().equals(username))
            throw new NotYourTurnException(
                    "\n" +
                            "This is not your turn!\n" +
                            "Current player is: " + game.getCurrentPlayer().getName());
        else
            return game.insertDice(diceID, row, column);
    }

    //With resumeGame a player that has lost his connection can resume the game only
    // if the game isn't in the end state
    @Override
    public boolean resumeGame(String username) {

        //Here we check if the game state is admissible
        if (state.equals(StateMachine.PLAYER_PREPARING) || state.equals(StateMachine.PLAYING) || state.equals(StateMachine.GAME_PREPARING)){
            game.resumePlayer(username);
            return true;
        }
        return false;
    }

    public boolean useToolCard(String username, int cardIndex,  ArrayList<Integer> paramList){
        if (!this.game.getCurrentPlayer().getName().equals(username))
            return false;

        ToolCardParam param = new ToolCardParam(
                game.getRoundTrack(),
                game.getDraftPool(),
                game.getDiceBag(),
                game.getCurrentPlayer().getBoard(),
                paramList
        );

        ToolCard toolCard = (ToolCard) game.getToolCardList().get(cardIndex);
        return toolCard.getStrategy().use(param);
//       game.useToolCard();
    }

    @Override
    public boolean passUser(String username) {
        if (this.game.getCurrentPlayer().getName().equals(username)){
            this.pass();
            return true;
        }
        else
            return false;
    }


}
