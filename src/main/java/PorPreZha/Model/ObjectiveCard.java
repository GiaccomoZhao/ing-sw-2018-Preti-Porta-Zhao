package PorPreZha.Model;

import java.util.ArrayList;
import java.util.List;

public abstract class ObjectiveCard extends Card {

    public int numScore;

    int abstract apply(Board board){};

    public ObjectiveCard() {

    }
}
