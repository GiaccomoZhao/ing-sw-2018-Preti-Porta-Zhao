package porprezhas.model.dices;

import porprezhas.Useful;
import porprezhas.exceptions.diceMove.DiceNotFoundException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DraftPool implements Serializable {

    private List<Dice> draftPool;

    public DraftPool() {
        draftPool = new ArrayList<>();
    }

    //constructor used only for testing in toolCardTest
    public DraftPool(List<Dice> draftPool) {
        this.draftPool = draftPool;
    }

    public DraftPool(DiceBag diceBag, int numberOfPlayers) {
        this.draftPool = diceBag.GetRandomDices(numberOfPlayers);
    }

    public void setDraftPool(DiceBag diceBag, int numberOfPlayers) {
        this.draftPool = diceBag.GetRandomDices(numberOfPlayers);
    }



    // for external use
    public static Dice getDiceByID(List<Dice> diceList, long diceID) {

        for (Dice dice : diceList) {
            if( dice.getId() == diceID ) {
                return dice;
            }
        }

        // if not found then:
        throw new DiceNotFoundException("dice with id = " + diceID + " does NOT Exist in Draft Pool");
    }

    // get index of the dice in draft pool based by the id
    public int getDiceIndexByID(long diceID) {

        for (int i = 0; i < diceList().size(); i++) {
            if( diceList().get(i).getId() == diceID ) {
                return i;
            }
        }

        // if not found then:
        throw new DiceNotFoundException("dice with id = " + diceID + " does NOT Exist in Draft Pool");
    }

    // search the dice based by the id
    public Dice getDiceByID(long diceID) {

        for (Dice dice : diceList()) {
            if( dice.getId() == diceID ) {
                return dice;
            }
        }

        // if not found then:
        throw new DiceNotFoundException("dice with id = " + diceID + " does NOT Exist in Draft Pool");
    }


    // remove dice from the list and return it
    public Dice chooseDice(long diceID){
        int index = getDiceIndexByID(diceID);   // throw except when not found
        Dice chosenDice = draftPool.remove(index);

        if(null == chosenDice)
            System.err.println("Could NOT Remove dice with id = " + diceID + " in position = " + index + " from Draft Pool");

        return chosenDice;
    }

/*
    public Dice chooseDiceByIndex(int indexDice){
        if (!Useful.isValueBetweenInclusive(indexDice, 0, diceList().size() -1))
            throw new IndexOutOfBoundsException("\nIndex in draft pool should be: 0 <= " + indexDice + " <= " + (diceList().size() -1));

        Dice chosenDice = diceList().get(indexDice);

        draftPool.remove(chosenDice);

        return chosenDice;
    }
*/

    // Only server can do this operation
    public void setDice(int indexDice, Dice dice) {
        draftPool.set(indexDice, dice);
    }

    public List<Dice> diceList(){

        List<Dice> listDice = new ArrayList<>(draftPool.size());

        for (Dice dice : draftPool) {
            listDice.add(new Dice(dice));
        }

        return listDice;
    }

    // return old.dice or throw notFound Exception
    public Dice diceSubstitute(Dice newDice, long idDiceDraftPool){
        int index = getDiceIndexByID(idDiceDraftPool);
        Dice oldDice;

        oldDice = draftPool.get(index-1);
        draftPool.set(index-1,newDice);
        return oldDice;
    }


    public void addDice(Dice dice){
        this.draftPool.add(dice);
    }

    @Override
    public String toString() {
        return "DraftPool" +
                "{" + draftPool +
                '}';
    }

    public boolean safetyCheck(int indexDice) {
        // check Null Pointer
        if(null == draftPool  ||  0 == draftPool.size())
            return false;
        // check Index Out of Bounds
        if(Useful.isValueOutOfBounds(indexDice, 0, draftPool.size()-1)) {
            return false;
        }
        return true;
    }
}
