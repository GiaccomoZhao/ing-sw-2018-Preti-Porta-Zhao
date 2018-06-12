package porprezhas.view.fx.gameScene.controller.component;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import porprezhas.model.dices.Dice;

import java.util.Scanner;

import static porprezhas.view.fx.gameScene.GuiSettings.*;

public class DiceView extends ImageView {

    private final int column;
    private final int row;

    private final long diceID;


    // this dice is created for dragging message
    // so we do not need set image, any case we can set it later
    public DiceView(int row, int column, long diceID) {
        super();    // for the convention, we always call the parent constructor
        this.column = column;
        this.row = row;
        this.diceID = diceID;
    }

    // main Constructor
    // User see only DiceView created by this constructor
    public  DiceView(Dice dice, int row, int column) {
        super();    // for the convention, we always call the parent constructor
        if(bDebug)
            System.out.println("Create new DiceView from: " + getPathToDice(dice));
        setImage(dice);
        this.diceID = dice.getId();
        this.column = column;
        this.row = row;
    }
/*
    public DiceView(Image image, int row, int column, long diceID) {
        super(image);
        if(bDebug)
            System.out.println("Create new DiceView from image = " + image + "");
        this.column = column;
        this.row = row;
        this.diceID = diceID;
    }

    public DiceView(String url, int row, int column, long diceID) {
        super(url);
        if(bDebug)
            System.out.println("Create new DiceView from url = " + url + "");
        this.column = column;
        this.row = row;
        this.diceID = diceID;
    }
*/

    @Override
    public String toString() {
        return "DiceView{ " +
                "column=" + column +
                ", row=" + row +
                ", id=" + diceID +
                " }";
    }

    // this method is used for Dragging: Clip Board
    public static DiceView fromString(String string) {
        int column, row, diceID;

        Scanner scanner = new Scanner(string);

        // I know this block of code can be simplified by one REGEX, but the following code should be more clearer
        scanner.useDelimiter(",|\\s");

        scanner.findInLine("column=");
        column = scanner.nextInt();
        if(bDebug) System.out.print("\tcol=" + column);   // I have to print one by one because scanner would throw a exception when a value is not correct

        scanner.findInLine("row=");
        row = scanner.nextInt();
        if(bDebug) System.out.print("\trow=" + row);

        scanner.findInLine("id=");
        diceID = scanner.nextInt();
        if(bDebug) System.out.println("\tid=" + diceID);

/*        scanner.findInLine("color=(\\w+)");
        String szColor = scanner.match().group(1);
        if(bOutput) System.out.println("\tcolor=" + szColor);
*/
        scanner.close();

//        Dice.ColorDice color = Dice.ColorDice.getByString( szColor );
//        dice = new Dice(number, color);

        return new DiceView(column, row, diceID);
    }

    public int getColumn() {
        return column;
    }

    public int getRow() {
        return row;
    }

    public long getDiceID() {
        return diceID;
    }

    public DiceView setImage(Dice dice) {
        setImage( new Image (getPathToDice(dice)) );
        return this;
    }

}
