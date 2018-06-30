package porprezhas.model;

public class GameConstants {
    private GameConstants() {
    }

    public static final int DICE_QUANTITY = 90;
    public static final int MAX_PLAYER_QUANTITY = 4;
    public static final int MAX_DICE_PER_ROUND = 2*MAX_PLAYER_QUANTITY +1;
    public static final int DICE_PICK_PER_TURN = 1;
    public static final int ROUND_NUM = 10;
    public static final int BOARD_BOXES = 20;
    //        public static final int FAVOR_TOKEN_QUANTITY = 3;
    public static final double TIMEOUT_QUEUE_SEC = 20;   //60;
    public static final double TIMEOUT_PREPARING_SEC = 10;   //60;
    public static final double TIMEOUT_ROUND_SEC = 60;     //33;             // this game should spends at max 45 min: 45*60 == 33(sec)*4(players)*2*10(round) + 60
    public static final double TIMEOUT_ROUND_SOLITAIRE_SEC = 5;// 90;   // solitaire should spend 30 min: 90sec * 2*10round == 30min

    public static long secondsToMillis(double seconds) {
        return (long) seconds * 1000;
    }
}
