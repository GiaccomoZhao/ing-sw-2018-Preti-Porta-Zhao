package porprezhas.view.fx.component;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import porprezhas.model.dices.Dice;

import java.util.Scanner;

import static porprezhas.view.fx.GuiSettings.*;

public class DiceView extends ImageView {

    private int column;
    private int row;

    private Dice dice;

    public DiceView() {
        super();
    }
    public DiceView(Dice dice, int column, int row) {
        super();
//        System.out.println(pathToDice + dice.getDiceNumber() + dice.getColorDice().name().toLowerCase().charAt(0) + ".png");
        setImage( new Image (pathToDice +
                dice.getDiceNumber() + dice.getColorDice().name().toLowerCase().charAt(0) + ".png") );      // TODO: Load all images in RAM to avoid disk reading during game
        this.column = column;
        this.row = row;
        this.dice = dice;
    }

    public DiceView(Image image, int column, int row, Dice dice) {
        super(image);
        this.column = column;
        this.row = row;
        this.dice = dice;
    }

    public DiceView(String url, int column, int row, Dice dice) {
        super(url);
        this.column = column;
        this.row = row;
//        this.player = player;
        this.dice = dice;
    }

    @Override
    public String toString() {
        return "DiceView{ " +
                "column=" + column +
                ", row=" + row +
                ", dice=" + dice.getDiceNumber() +
                ", color=" + dice.getColorDice().name() +
                " }";
    }

    public DiceView fromString(String string) {
        boolean bOutput = false;
        Scanner scanner = new Scanner(string);

        // I know this block of code can be simplified by one REGEX, but the following code should be more clearer
        scanner.useDelimiter(",");

        scanner.findInLine("column=");
        column = scanner.nextInt();
        if(bOutput) System.out.print("col=" + column);   // I have to print one by one because scanner would throw a exception when a value is not correct

        scanner.findInLine("row=");
        row = scanner.nextInt();
        if(bOutput) System.out.print("\trow=" + row);

        scanner.findInLine("dice=");
        int number = scanner.nextInt();
        if(bOutput) System.out.print("\tnum=" + number);

        scanner.findInLine("color=(\\w+)");
        String szColor = scanner.match().group(1);
        if(bOutput) System.out.println("\tcolor=" + szColor);

        scanner.close();

        Dice.ColorDice color = Dice.ColorDice.getByString( szColor );
        dice = new Dice(number, color);

        setImage( new Image (pathToDice +
                dice.getDiceNumber() + dice.getColorDice().name().toLowerCase().charAt(0) + ".png") );
        return this;
    }

    public int getColumn() {
        return column;
    }

    public int getRow() {
        return row;
    }

    public void setPosition (int column, int row) {
        this.column = column;
        this.row = row;
    }

    public Dice getDice() {
        return dice;
    }

    public void setDice(Dice dice) {
        this.dice = dice;
    }

}
