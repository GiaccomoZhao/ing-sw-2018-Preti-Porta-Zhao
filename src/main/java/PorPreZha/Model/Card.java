package PorPreZha.Model;

import java.util.ArrayList;
import java.util.List;

public abstract class Card {

    private String description;

    public String getDescription() {
        return description;
    }

    public abstract int getEffectID(){
        return effect.ID;
    };
}
