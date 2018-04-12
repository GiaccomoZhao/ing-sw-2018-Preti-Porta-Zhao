package PorPreZha.Model;

import java.util.ArrayList;
import java.util.List;

public class ToolCard extends Card{

    public int tokensQuantity;

    public List<FavorTokens> tokens;

    public void use(){};

    public void addTokens(){
        tokensQuantity=tokensQuantity+1;
    }


}
