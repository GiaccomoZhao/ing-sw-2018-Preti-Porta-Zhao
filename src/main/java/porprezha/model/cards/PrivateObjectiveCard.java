package porprezha.model.cards;


import javax.tools.Tool;

public class PrivateObjectiveCard  extends ObjectiveCard {

    public PrivateObjectiveCard(Effect effect) {
        super(effect);
    }

    @Override
    public int apply(Board board) {
        return 0;
    }
}
