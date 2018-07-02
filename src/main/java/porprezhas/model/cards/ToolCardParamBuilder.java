package porprezhas.model.cards;

import porprezhas.view.fx.gameScene.state.DiceContainerType;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class ToolCardParamBuilder {
    ArrayList<Integer> params;

    public ToolCardParamBuilder() {
         params = new ArrayList<>();
    }



    public ToolCardParamBuilder(int idBoardFrom, int iDice, int idBoardTo, int param1, int param2) {
        params = new ToolCardParamBuilder(idBoardFrom, iDice, idBoardTo, param1, param2).params;
    }

    public ToolCardParamBuilder(ArrayList<Integer> params) {
        this.params = params;
    }

    /**
     * Build a Tool Card Parameter
     *
     * @param idBoardFrom
     * @param iDice     dice identifier, for draft pool is long diceID
     * @param idBoardTo
     * @param param1    for board is row,   for track is indexRound
     * @param param2    for board is col    for track is indexDice
     * @return
     */
    public ToolCardParamBuilder build(int idBoardFrom, long iDice, int idBoardTo, int param1, int param2) {
        Integer firstParam = null;
        Integer paramOne = null;
        Integer paramTwo = null;

        if(idBoardFrom == DiceContainerType.DRAFT.toInt()) {
            params.add((int) iDice);
        }
        if(idBoardTo == DiceContainerType.BOARD1.toInt()) {
            params.add( param1 );
            params.add( param2 );
        } else if ( idBoardTo == DiceContainerType.TRACK.toInt()) {
            params.add( param1 );
            params.add( param2 );
        }


        return this;
    }


    public ArrayList<Integer> getParams() {
        return params;
    }
}
