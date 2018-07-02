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
     * @param fromParam1 dice identifier, for board is int row, for draft pool is int index
     * @param fromParam2                  for board is int col
     * @param idBoardTo
     * @param toParam1    for board is row,   for track is indexRound
     * @param toParam2    for board is col    for track is indexDice
     * @return the builder
     */
    public ToolCardParamBuilder build(int idBoardFrom, int fromParam1, int fromParam2, int idBoardTo, int toParam1, int toParam2) {
        Integer firstParam = null;
        Integer paramOne = null;
        Integer paramTwo = null;

        if(idBoardFrom == DiceContainerType.DRAFT.toInt()) {
            params.add(fromParam1);
        }
        if(idBoardTo == DiceContainerType.BOARD1.toInt()) {
            params.add(fromParam1);
            params.add(fromParam2);
        }
        if(idBoardTo == DiceContainerType.BOARD1.toInt()) {
            params.add( toParam1 );
            params.add( toParam2 );
        } else if ( idBoardTo == DiceContainerType.TRACK.toInt()) {
            params.add( toParam1 );
            params.add( toParam2 );
        }
        return this;
    }

    /**
     * add one parameter manually to the builder
     * @param param the parameter to add
     * @return the builder
     */
    public ToolCardParamBuilder add(int param) {
        params.add(param);
        return this;
    }


    public ArrayList<Integer> getParams() {
        return params;
    }
}
