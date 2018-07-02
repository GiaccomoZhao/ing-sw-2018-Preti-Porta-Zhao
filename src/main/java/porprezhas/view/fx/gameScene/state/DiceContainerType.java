package porprezhas.view.fx.gameScene.state;

public enum DiceContainerType {
    DRAFT,
    TRACK,
    BAG,
    BOARD1,
    BOARD2,
    BOARD3,
    BOARD4;

    public static DiceContainerType fromPlayer(int position) {
        return values()[BOARD1.ordinal() + position];
    }


    public int toInt() {
        return this.ordinal();
    }

    public static DiceContainerType fromInt(int val) {
        return values()[val];
    }

    @Override
    public String toString() {
        return "DiceContainerType{" + name() + "}";
    }
}
