package porprezhas.model.cards;

import porprezhas.model.dices.Board;

import java.io.Serializable;

public abstract class Card implements Serializable {


    public final Effect effect;

    public enum Effect{

        PUC1(  1,  "Row Color Variety",         "Rows with no repeated colors"),
        PUC2(  2,  "Column Color Variety",      "Columns with no repeated colors"),
        PUC3(  3,  "Row Shade Variety",      "Rows with no repeated shades"),
        PUC4(  4,  "Column Shade Variety",   "Columns with no repeated shades"),
        PUC5(  5,  "Light Shades",              "Set of 1 & 2 anywhere"),
        PUC6(  6,  "Middle Shades",             "Set of 3 & 4 anywhere"),
        PUC7(  7,  "Deep Shades",               "Set of 5 & 6 anywhere"),
        PUC8(  8,  "Shade Variety",             "Set of one of each value anywhere"),
        PUC9(  9,  "Color Diagonals",            "Number of dices of the same color diagonally adjacent"),
        PUC10(10,  "Color Variety",           "Set of one of each value anywhere"),
        PRC1(  1,  "Shades of Red - Private",     "Sum of values on red dice"),
        PRC2(  2,  "Shades of Yellow - Private",     "Sum of values on yellow dice"),
        PRC3(  3,  "Shades of Green - Private",     "Sum of values on green dice"),
        PRC4(  4,  "Shades of Blue - Private",     "Sum of values on blue dice"),
        PRC5(  5,  "Shades of Purple - Private",     "Sum of values on purple dice"),
        TC1(   1,  "Grozing Pliers",            "After drafting, increase or decrease the value of the drafted die by 1 1 may not change to 6, or 6 to 1"),
        TC2(   2,  "Eglomise Brush",         "Move any one die in your window ignoring the color restrictions You must obey all other placement restrictions"),
        TC3(   3,  "Copper Coil Burnisher",  "Move any one die in your window ignoring the shade restrictions You must obey all other placement restrictions"),
        TC4(   4,  "Lathekin",                      "Move exactly two dice, obeying all placement restrictions"),
        TC5(   5,  "Lens Cutter",          "After drafting, swap the drafted die with a die from the Round Track"),
        TC6(   6,  "Flux Brush",      "After drafting, re-roll the drafted die If it cannot be placed, return it to the Draft Pool"),
        TC7(   7,  "Glazing Hammer",                "Re-roll all dice in the Draft Pool This may only be used on your second turn before drafting"),
        TC8(   8,  "Running Pliers",            "After your first turn, immediately draft a die Skip yuor next turn this round"),
        TC9(   9,  "Cork-backed Straightedge",      "After drafting, place the die in a spot that is not adjacent to another die You must obey all other placement restrictions"),
        TC10( 10,  "Grinding Stone",            "After drafting, flip the die to its opposite side 6 flips to 1, 5 to 2, 4 to 3, etc."),
        TC11( 11,  "Flux Remover",      "After drafting, return the die to the Dice Bag and pull 1 die from the bag Choose a value and place the new die, obeying all placement restrictions, or return it to the Draft Pool"),
        TC12( 12,  "Tap Wheel",            "Move up to two dice of the same color that match the color of a die on the Round Track You must obey all placement restrictions");


        public final int ID;
        public final String name;
        public final String description;

        public static final int PUBLIC_OBJECTIVE_CARD_NUMBER = 10;
        public static final int PRIVATE_OBJECTIVE_CARD_NUMBER = 5;
        public static final int TOOL_CARD_NUMBER = 12;

        Effect(int ID, String name, String description){
            this.name = name;
            this.ID = ID;
            this.description = description;
        }

        public static Effect getToolCardEffect(int index) {
            return values()[TC1.ordinal() + index];
        }
    }



    public Card(Effect effect){
        this.effect=effect;
    }

    @Override
    public String toString() {
        return effect.name.concat(":  " + effect.description);
    }


}
