package porprezhas.model.dices;

import java.io.Serializable;

import static porprezhas.model.dices.Dice.ColorDice.WHITE;

public class Box implements Serializable {
    private int number;
    private Dice.ColorDice color;

    public Box(Dice.ColorDice color, int number) {
        this.color = color;
        this.number = number;
    }

    public Box() {
        this.color=WHITE;
        this.number=0;
    }

    @Override
    public String toString() {
        return "Box{" +
                "number=" + number +
                ", color=" + color +
                '}';
    }

    public Boolean freeBox(){
        if(color.equals(WHITE) && number==0 )
            return Boolean.TRUE;
        else
            return Boolean.FALSE;

    }

    public Boolean white(){
        if(color.equals(WHITE))
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


    public Object getConstraint(){
        if(this.color!=WHITE)
            return this.color;
        else return number;
    }

    public Boolean checkConstraint(Dice dice, Board.Restriction restriction){
        if(this.freeBox())
            return Boolean.TRUE;

        //Check Constraints
        if (restriction.hasColorRestriction() &&
                !this.white() &&
                !this.color.equals(dice.getColorDice()) ) {
            return Boolean.FALSE;
        }

        if (restriction.hasNumberRestriction() &&
                !noNumber() &&
                this.number != dice.getDiceNumber() ) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    public Boolean checkConstraint(Dice dice){
        return checkConstraint(dice, Board.Restriction.ALL);
    }

/*    public Boolean checkConstraint(Dice dice){
        if(this.freeBox())
            return Boolean.TRUE;
        if(!this.white() && this.color.equals(dice.getColorDice()))
            return Boolean.TRUE;
        if(this.number== dice.getDiceNumber())
            return Boolean.TRUE;
        else
            return Boolean.FALSE;
    }
*/

    public Dice.ColorDice getColor() {
        return color;
    }

    public int getNumber() {
        return number;
    }
}

