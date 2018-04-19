package PorPreZha.Model;

public class Box {
    private Dice.ColorDice color;
    private int number;

    public Box(Dice.ColorDice color, int number) {
        this.color = color;
        this.number = number;
    }

    public Box() {
        this.color=Dice.ColorDice.WHITE;
        this.number=0;
    }

    public Boolean freeBox(){
        if(color.equals(Dice.ColorDice.WHITE) && number==0 )
            return Boolean.TRUE;
        else
            return Boolean.FALSE;

    }

    public Boolean white(){
        if(color.equals(Dice.ColorDice.WHITE))
            return Boolean.TRUE;
        else
            return Boolean.FALSE;
    }

    public Boolean noNumber(){
        if(number==0)
            return Boolean.TRUE;
        else
            return Boolean.FALSE;
    }


    public Object getCostraint(){
        if(this.color!=Dice.ColorDice.WHITE)
            return this.color;
        else return number;
    }

    public Boolean checkCostraint(Dice dice){
        if(this.freeBox())
            return Boolean.TRUE;
        if(!this.white() && this.color.equals(dice.colorDice))
            return Boolean.TRUE;
        if(this.number== dice.getDiceNumber())
            return Boolean.TRUE;
        else
            return Boolean.FALSE;
    }

    public Dice.ColorDice getColor() {
        return color;
    }

    public int getNumber() {
        return number;
    }
}

