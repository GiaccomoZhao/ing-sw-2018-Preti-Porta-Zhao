package porprezhas.model.cards;

public enum ToolCardParamType {
    DRAFT(1),
    TRACK(2),
    BAG(0),    // this is not used
    DIALOG_BOX(1),
    BOARD(2);

    public final int quantity;
    ToolCardParamType(int quantity) {
        this.quantity = quantity;
    }

    public int getParamQuantity() {
        return quantity;
    }

    @Override
    public String toString() {
        return name();
//        return "ToolCardParamType{" +
//                name() +
//                '}';
    }
}
