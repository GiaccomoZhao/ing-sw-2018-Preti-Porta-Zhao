package porprezhas.view.state;

public enum DiceContainer {
        DRAFT,
        TRACK,
        BAG,
        BOARD1,
        BOARD2,
        BOARD3,
        BOARD4;

    public static DiceContainer fromPlayer(int position) {
        return values()[BOARD1.ordinal() + position];
    }

/*
    public int toInt() {
        return this.ordinal();
    }
    public static DiceContainer fromInt(int val) {
        return values()[val];
    }
*/
}
