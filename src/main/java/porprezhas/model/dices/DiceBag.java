package porprezhas.model.dices;


import java.util.ArrayList;
import java.util.Random;

public class DiceBag {

    private ArrayList<Dice> diceSet;

    //Constructor creates an arraylist of 90 dice, 18 for each color
    public DiceBag() {
        this.diceSet = new ArrayList(90);
        for(int i=0; i<18; i++){
            diceSet.add( new Dice( Dice.ColorDice.RED));
            diceSet.add( new Dice( Dice.ColorDice.YELLOW));
            diceSet.add( new Dice( Dice.ColorDice.GREEN));
            diceSet.add( new Dice( Dice.ColorDice.BLUE));
            diceSet.add( new Dice( Dice.ColorDice.PURPLE));

        }
    }

    //This method extracts 2*numberofplayers + 1 random dice from the dicebag and returns the list
    public ArrayList<Dice> GetRandomDices(int numberOfPlayers) throws IndexOutOfBoundsException
    {
        ArrayList<Dice> draftPool= new ArrayList<Dice>(2*numberOfPlayers +1 );
        // Wrong number of player case
        if(numberOfPlayers>4 || numberOfPlayers<1)
            throw new IndexOutOfBoundsException();

        int iterator= numberOfPlayers*2+1;
        int extraction;
        Dice die;
        while(iterator>0){

            Random random = new Random();

            extraction =random.nextInt(diceSet.size());
            die =diceSet.get(extraction);
            die.roll();
            draftPool.add(die);
            diceSet.remove(extraction);
            iterator--;

        }
        return draftPool;

    }

    public int diceBagSize(){
        return diceSet.size();
    }


    public void addDice(Dice dice){
        diceSet.add(dice);
    }

    public Dice extractDice(){

        Random random = new Random();
        Dice die;
        int extraction;

        extraction = random.nextInt(diceSet.size());
        die =diceSet.get(extraction);
        return die;
    }
}
