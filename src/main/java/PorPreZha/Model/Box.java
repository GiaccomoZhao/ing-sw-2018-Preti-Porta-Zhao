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

    public Dice.ColorDice getColor() {
        return color;
    }

    public int getNumber() {
        return number;
    }
}

