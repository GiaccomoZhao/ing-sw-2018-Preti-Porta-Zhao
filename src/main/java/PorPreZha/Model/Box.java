package PorPreZha.Model;

public class Box {
    private Dice.ColorDice color;
    private int number;

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

    public Box(Dice.ColorDice color, int number) {
        this.color = color;
        this.number = number;
    }

    public Box() {
        this.color=Dice.ColorDice.WHITE;
        this.number=0;
    }

    public Dice.ColorDice getColor() {
        return color;
    }

    public int getNumber() {
        return number;
    }
}

