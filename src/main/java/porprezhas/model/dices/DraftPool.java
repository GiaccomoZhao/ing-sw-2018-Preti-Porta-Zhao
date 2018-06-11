package porprezhas.model.dices;

import porprezhas.Useful;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DraftPool implements Serializable {

    private List<Dice> draftPool;

    public DraftPool() {
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
    public Dice chooseDice(int indexDice){

        Dice chosenDice;

        if(Useful.isValueBetweenInclusive(indexDice, 0 , draftPool.size()-1)){
            chosenDice = draftPool.remove(indexDice);

            return chosenDice;
        } else
            throw new IndexOutOfBoundsException("index in draft pool: 0 <= " +  indexDice + " <= " + (draftPool.size()-1));
    }

    // Only Server can do this operation
    public void setDice(int indexDice, Dice dice) {
        draftPool.set(indexDice, dice);
    }

    public ArrayList<Dice> diceList(){

        ArrayList<Dice> listDice = new ArrayList<Dice>(draftPool.size());

        for (Dice dice : draftPool) {
            listDice.add(new Dice(dice));
        }

        return listDice;
    }

    public Dice diceSubstitute(Dice newDice, int position){

        Dice oldDice;
        if(position-1< draftPool.size()) {
            oldDice = draftPool.get(position-1);
            draftPool.set(position-1,newDice);
            return oldDice;
        }

        return null;
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
}
