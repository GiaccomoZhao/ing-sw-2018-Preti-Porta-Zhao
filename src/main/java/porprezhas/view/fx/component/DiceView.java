package porprezhas.view.fx.component;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import porprezhas.model.dices.Dice;
import porprezhas.view.fx.GuiSettings;

import java.util.Scanner;

import static porprezhas.view.fx.GuiSettings.*;

public class DiceView extends ImageView {

    private final int column;
    private final int row;

    private final int indexDice;

    public DiceView(int column, int row, int indexDice) {
        super();    // for the convention, we always call the parent constructor
        this.column = column;
        this.row = row;
        this.indexDice = indexDice;
    }

    public DiceView(Dice dice, int column, int row, int indexDice) {
        super();    // for the convention, we always call the parent constructor
        System.out.println(pathToDice + dice.getDiceNumber() + dice.getColorDice().name().toLowerCase().charAt(0) + ".png");
        this.column = column;
        this.row = row;
        this.indexDice = indexDice;
    }

    public DiceView(Image image, int column, int row, int indexDice) {
        super(image);
        this.column = column;
        this.row = row;
        this.indexDice = indexDice;
    }

    public DiceView(String url, int column, int row, int indexDice) {
        super(url);
        this.column = column;
        this.row = row;
        this.indexDice = indexDice;
    }

    @Override
    public String toString() {
        return "DiceView{ " +
                "column=" + column +
                ", row=" + row +
                ", index=" + indexDice +
                " }";
    }

    // this method is used for Dragging: Clip Board
    public static DiceView fromString(String string) {
        int column, row, indexDice;

        Scanner scanner = new Scanner(string);

        // I know this block of code can be simplified by one REGEX, but the following code should be more clearer
        scanner.useDelimiter(",");

        scanner.findInLine("column=");
        column = scanner.nextInt();
        if(bDebug) System.out.print("col=" + column);   // I have to print one by one because scanner would throw a exception when a value is not correct

        scanner.findInLine("row=");
        row = scanner.nextInt();
        if(bDebug) System.out.print("\trow=" + row);

        scanner.findInLine("index=");
        indexDice = scanner.nextInt();
        if(bDebug) System.out.print("\tindex=" + indexDice);

/*        scanner.findInLine("color=(\\w+)");
        String szColor = scanner.match().group(1);
        if(bOutput) System.out.println("\tcolor=" + szColor);
*/
        scanner.close();

//        Dice.ColorDice color = Dice.ColorDice.getByString( szColor );
//        dice = new Dice(number, color);

        return new DiceView(column, row, indexDice);
    }

    public int getColumn() {
        return column;
    }

    public int getRow() {
        return row;
    }

    public int getIndexDice() {
        return indexDice;
    }

    public DiceView setImage(Dice dice) {
        setImage( new Image (pathToDice +
                dice.getDiceNumber() + dice.getColorDice().name().toLowerCase().charAt(0) + ".png") );
        return this;
    }

}
