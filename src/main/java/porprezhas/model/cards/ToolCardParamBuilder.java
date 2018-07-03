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
     * @param idBoard
     * @param param1 dice identifier, for board is int row, for track is indexRound, for draft pool is int index
     * @param param2                  for board is int col, for track is indexDice,  for draft pool is nothing
     * @return the builder
     */
    public ToolCardParamBuilder build(int idBoard, int param1, int param2) {

        if(idBoard == DiceContainerType.DRAFT.toInt()) {
            params.add(param1);
        }
        if(idBoard == DiceContainerType.BOARD1.toInt()) {
            params.add( param1 );
            params.add( param2 );
        } else if ( idBoard == DiceContainerType.TRACK.toInt()) {
            params.add( param1 );
            params.add( param2 );
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
