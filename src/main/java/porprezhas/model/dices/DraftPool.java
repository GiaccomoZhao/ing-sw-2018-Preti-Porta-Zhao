package porprezhas.model.dices;

import java.util.ArrayList;
import java.util.List;

public class DraftPool {

    private List<Dice> draftPool;

    public DraftPool() {
    }

    public DraftPool(DiceBag diceBag, int numberOfPlayers) {
        this.draftPool = diceBag.GetRandomDices(numberOfPlayers);
    }

    public void setDraftPool(DiceBag diceBag, int numberOfPlayers) {
        this.draftPool = diceBag.GetRandomDices(numberOfPlayers);
    }
    public Dice chooseDice(int dice){

        Dice choosenDice;

        if(dice <= draftPool.size()){
            choosenDice = draftPool.remove(dice);

            return choosenDice;
        }
        return null;
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

        if(position <= draftPool.size()) {
            oldDice = draftPool.remove(position);
            draftPool.add(newDice);
            return oldDice;
        }

        return null;
    }



}
