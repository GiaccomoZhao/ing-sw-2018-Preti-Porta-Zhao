package porprezha.model.dices;


import java.util.ArrayList;
import java.util.Random;

public class DiceBag {

    ArrayList<Dice> dices;

    public DiceBag() {
        this.dices = new ArrayList(90);
        for(int i=0; i<18; i++){
            dices.add( new Dice( Dice.ColorDice.RED));
            dices.add( new Dice( Dice.ColorDice.YELLOW));
            dices.add( new Dice( Dice.ColorDice.GREEN));
            dices.add( new Dice( Dice.ColorDice.BLUE));
            dices.add( new Dice( Dice.ColorDice.PURPLE));

        }
    }

    public ArrayList<Dice> GetRandomDices(int numberOfPlayers){
        ArrayList<Dice> draftPool= new ArrayList<Dice>(2*numberOfPlayers +1 );
        if(numberOfPlayers>4) return null;
        int iterator= numberOfPlayers*2+1;
        int extraction;
        Dice dado;
        while(iterator>0){

            Random random = new Random();

            extraction =random.nextInt(dices.size());
            dado =dices.get(extraction);
            dado.roll();
            draftPool.add(dado);
            dices.remove(extraction);
            iterator--;

        }
        return draftPool;

    }
}
