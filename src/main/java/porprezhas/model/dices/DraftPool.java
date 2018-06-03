package porprezhas.model.dices;

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
    public Dice chooseDice(int dice){

        Dice chosenDice;

        if(dice <= draftPool.size()){
            chosenDice = draftPool.remove(dice);

            return chosenDice;
        }
        return null;
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

}
