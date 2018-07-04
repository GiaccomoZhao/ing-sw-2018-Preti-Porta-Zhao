package porprezhas.model.cards;

import javax.naming.ldap.PagedResultsControl;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static porprezhas.model.cards.ToolCardParamType.*;


public class ToolCardParamBuilder {
    public static final int[][] parameterSizes = ToolCardStrategy.parameterSizes;   // skip first for better using index
    public static final ToolCardParamType[][][] parameterType = { {{}},
            {{DRAFT, DIALOG_BOX}},
            {{BOARD, BOARD}},
            {{BOARD, BOARD}},
            {{BOARD, BOARD, BOARD, BOARD}},
            {{DRAFT, TRACK}}, // TC5
            {{DRAFT}, {BOARD}},        // choose dice, OPT_insert dice
            {{}},             // TC7
            {{DRAFT, BOARD}},
            {{DRAFT, BOARD}},
            {{DRAFT}},        // TC10
            {{DRAFT}, {DIALOG_BOX, BOARD}},     // choose dice, choose number, insert dice
            {{BOARD, BOARD, BOARD, BOARD}}
    };

    public final int NOT_DEFINED = -1;


    int tc;
    int step;
    List<HashMap<ToolCardParamType, ArrayList<Integer>>> paramMap;      // Want to use this instead of ArrayList<Integer>
    ArrayList<Integer> params;

    public ToolCardParamBuilder(int toolCard, int step) {
        this.tc = toolCard;
        params = new ArrayList<>();
        paramMap = new ArrayList<>();

        for (int i = 0; i < parameterType[tc][step].length; i++) {
            HashMap<ToolCardParamType, ArrayList<Integer>> newHashMap = new HashMap<>();
            newHashMap.put(parameterType[tc][step][i], new ArrayList<>());
            paramMap.add(newHashMap);
            for (int j = 0; j < parameterType[tc][step][i].getParamQuantity(); j++) {
                params.add(NOT_DEFINED);
            }
        }
    }

//    public ToolCardParamBuilder(int toolCard, ArrayList<Integer> params) {
//        this.tc = toolCard;
//        this.params = params;
//    }

    /**
     * Build a Tool Card Parameter
     * Note:
     *      forall( int i; i <= BOARD1; ToolCardParamType[i].equals(DiceContainerType[i]) )
     *
     * @param paramType dice container identifier, can be draft pool, round track or dice board
     * @param parameters  parameters[0] dice identifier, for board is int row, for track is indexRound, for draft pool is int index, for dialog box is one int
     *                    parameters[1]                  for board is int col, for track is indexDice,  for draft pool is nothing
     * @return the builder
     */
    public ToolCardParamBuilder build(ToolCardParamType paramType, int... parameters) {
        int indexNotDefined = getIndexToSet(paramType);
        int indexCounter = 0;

        if(NOT_DEFINED != indexNotDefined) {    // Nothing to set
            for (int iParam = 0; iParam < parameterType[tc][step].length; iParam++) {

                if (parameterType[tc][step][iParam] == paramType) {
                    if (indexNotDefined == iParam) {
                        for (int i = 0; i < parameterType[tc][step][iParam].getParamQuantity(); i++) {
                            params.set(indexCounter + i, parameters[i]);
                        }
                    }
                }

                indexCounter += parameterType[tc][step][iParam].getParamQuantity();
            }
        }

        return this;
    }

/*    /**
     * add one parameter manually to the builder
     * @param param the parameter to add
     * @return the builder
     */
/*    public ToolCardParamBuilder add(int param) {
        params.add(param);
        return this;
    }
*/

    /**
     * get the quantity of Parameter need to set a Tool Card
     * @param toolCardID tool card's Id
     * @return  the quantity of integer ned to setup the given tool card
     */
    public int getParamQuantity(int toolCardID) {
        int iCounter = 0;
        for (int iParam = 0; iParam < parameterType[tc][step].length; iParam++) {
            iCounter += parameterType[tc][step][iParam].getParamQuantity();
        }
        return iCounter;
    }


    public ArrayList<Integer> getParams() {
        return params;
    }

    /**
     *
     * @return if the parameters are all set.
     */
    public boolean isReady() {
        return NOT_DEFINED == getNextIndex();
    }

    /**
     * calculate the position to set a new parameter
     *
     * @param type the type of tool card parameter
     * @return  the position to set the parameter
     *          NOT_DEFINED, if there is nothing can be set
     */
    private int getIndexToSet(ToolCardParamType type) {
        int indexCounter = 0;
        int indexFirstNotSetOfType = NOT_DEFINED;

        for (int iParam = 0; iParam < params.size() && iParam < parameterType[tc][step].length; iParam++) {
            if (type.equals(parameterType[tc][step][iParam])) {

                for (int i = 0; i < parameterType[tc][step][iParam].getParamQuantity(); i++) {
                    if (NOT_DEFINED == params.get(indexCounter + i)) {
                        indexFirstNotSetOfType = iParam;
                        return indexFirstNotSetOfType;
                    }
                }
            }

            indexCounter += parameterType[tc][step][iParam].getParamQuantity();
        }

        return indexFirstNotSetOfType;
    }

    /**
     * @return  the first not defined parameter's index
     *          NOT_DEFINED, if all defined
     */
    private int getNextIndex() {
        int indexCounter = 0;
        for (int iParam = 0; iParam < parameterType[tc][step].length; iParam++) {

            for (int i = 0; i < parameterType[tc][step][iParam].getParamQuantity(); i++) {
                if( NOT_DEFINED == params.get(indexCounter + i)) {
                    return iParam;
                }
            }

            indexCounter += parameterType[tc][step][iParam].getParamQuantity();
        }
        return NOT_DEFINED;
    }

    /**
     * @return  the first not defined parameter Type
     *          null, if all defined
     */
    public ToolCardParamType getNext() {
        int index = getNextIndex();
        if(index > 0) {
            return parameterType[tc][step][index];
        } else {
            return null;
        }
    }
}
