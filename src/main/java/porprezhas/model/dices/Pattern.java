package porprezhas.model.dices;


import java.io.Serializable;

public class Pattern implements Serializable {
    // 24 types + 1
    public enum TypePattern {
        VOID,   // for test use
        KALEIDOSCOPIC_DREAM,
        VIRTUS,
        AURORAE_MAGNIFICUS,
        VIA_LUX,
        SUN_CATCHER,
        BELLESGUARD,
        BATLLO,
        SHADOW_THIEF,
        INDUSTRIA,
        AURORA_SAGRADIS,
        SYMPHONY_OF_LIGHT,
        FIRMITAS,
        LUZ_CELESTIAL,
        FIRELIGHT,
        CHROMATIC_SPLENDOR,
        LUX_ASTRAM,
        FRACTAL_DROPS,
        GRAVITAS,
        WATER_OF_LIFE,
        RIPPLES_OF_LIGHT,
        LUX_MUNDI,
        COMITAS,
        SUNS_GLORY,
        FULGOR_DEL_CIELO
    }

    private final int ROW = Board.ROW;
    private final int COLUMN = Board.COLUMN;
    private final String namePattern;
    private final TypePattern typePattern;
    private final int difficulty;
    private Box[][] pattern;

    public Pattern(TypePattern typePattern) {
       pattern = new Box[ROW][COLUMN];
        this. typePattern = typePattern;

       switch(typePattern){
           case KALEIDOSCOPIC_DREAM:
               //KALEIDOSCOPIC DREAM
               namePattern="KaleidoscopicDream";
               difficulty=4;
               pattern[0][0]= new Box(Dice.ColorDice.YELLOW, 0);
               pattern[0][1]= new Box(Dice.ColorDice.BLUE, 0);
               pattern[0][4]= new Box(Dice.ColorDice.WHITE, 1);
               pattern[1][0]= new Box(Dice.ColorDice.GREEN, 0);
               pattern[1][2]= new Box(Dice.ColorDice.WHITE, 5);
               pattern[1][4]= new Box(Dice.ColorDice.WHITE, 4);
               pattern[2][0]= new Box(Dice.ColorDice.WHITE, 3);
               pattern[2][2]= new Box(Dice.ColorDice.RED, 0);
               pattern[2][4]= new Box(Dice.ColorDice.GREEN, 0);
               pattern[3][0]= new Box(Dice.ColorDice.WHITE, 2);
               pattern[3][3]= new Box(Dice.ColorDice.BLUE, 0);
               pattern[3][4]= new Box(Dice.ColorDice.YELLOW, 0);

               break;

           case VIRTUS:
               //VIRTUS
               namePattern="Virtus";
               difficulty=5;
               pattern[0][0]= new Box(Dice.ColorDice.WHITE, 4);
               pattern[0][2]= new Box(Dice.ColorDice.WHITE, 2);
               pattern[0][3]= new Box(Dice.ColorDice.WHITE, 5);
               pattern[0][4]= new Box(Dice.ColorDice.GREEN, 0);
               pattern[1][2]= new Box(Dice.ColorDice.WHITE, 6);
               pattern[1][3]= new Box(Dice.ColorDice.GREEN, 0);
               pattern[1][4]= new Box(Dice.ColorDice.WHITE, 2);
               pattern[2][1]= new Box(Dice.ColorDice.WHITE, 3);
               pattern[2][2]= new Box(Dice.ColorDice.GREEN, 0);
               pattern[2][3]= new Box(Dice.ColorDice.WHITE, 4);
               pattern[3][0]= new Box(Dice.ColorDice.WHITE, 5);
               pattern[3][1]= new Box(Dice.ColorDice.GREEN, 0);
               pattern[3][2]= new Box(Dice.ColorDice.WHITE, 1);

               break;

           case AURORAE_MAGNIFICUS:
               //AURORAE MAGNIFICUS
               namePattern="Aurorae Magnificus";
               difficulty=5;
               pattern[0][0]= new Box(Dice.ColorDice.WHITE, 5);
               pattern[0][1]= new Box(Dice.ColorDice.GREEN, 0);
               pattern[0][2]= new Box(Dice.ColorDice.BLUE, 0);
               pattern[0][3]= new Box(Dice.ColorDice.PURPLE, 0);
               pattern[0][4]= new Box(Dice.ColorDice.WHITE, 2);
               pattern[1][0]= new Box(Dice.ColorDice.PURPLE, 0);
               pattern[1][4]= new Box(Dice.ColorDice.YELLOW, 0);
               pattern[2][0]= new Box(Dice.ColorDice.YELLOW, 0);
               pattern[2][2]= new Box(Dice.ColorDice.WHITE, 6);
               pattern[2][4]= new Box(Dice.ColorDice.PURPLE, 0);
               pattern[3][0]= new Box(Dice.ColorDice.WHITE, 1);
               pattern[3][3]= new Box(Dice.ColorDice.GREEN, 0);
               pattern[3][4]= new Box(Dice.ColorDice.WHITE, 4);

               break;

           case VIA_LUX:
               //VIA LUX
               namePattern="Via Lux";
               difficulty=4;
               pattern[0][0]= new Box(Dice.ColorDice.YELLOW, 0);
               pattern[0][2]= new Box(Dice.ColorDice.WHITE, 6);
               pattern[1][1]= new Box(Dice.ColorDice.WHITE, 1);
               pattern[1][2]= new Box(Dice.ColorDice.WHITE, 5);
               pattern[1][4]= new Box(Dice.ColorDice.WHITE, 2);
               pattern[2][0]= new Box(Dice.ColorDice.WHITE, 3);
               pattern[2][1]= new Box(Dice.ColorDice.YELLOW, 0);
               pattern[2][2]= new Box(Dice.ColorDice.RED, 0);
               pattern[2][3]= new Box(Dice.ColorDice.PURPLE, 0);
               pattern[3][2]= new Box(Dice.ColorDice.WHITE, 4);
               pattern[3][3]= new Box(Dice.ColorDice.WHITE, 3);
               pattern[3][4]= new Box(Dice.ColorDice.RED, 0);

               break;

           case SUN_CATCHER:
               //SUN CATCHER
               namePattern="Sun Catcher";
               difficulty=3;
               pattern[0][1]= new Box(Dice.ColorDice.BLUE, 0);
               pattern[0][2]= new Box(Dice.ColorDice.WHITE, 2);
               pattern[0][4]= new Box(Dice.ColorDice.YELLOW, 0);
               pattern[1][1]= new Box(Dice.ColorDice.WHITE, 4);
               pattern[1][3]= new Box(Dice.ColorDice.RED, 0);
               pattern[2][2]= new Box(Dice.ColorDice.WHITE, 5);
               pattern[2][3]= new Box(Dice.ColorDice.YELLOW, 0);
               pattern[3][0]= new Box(Dice.ColorDice.GREEN, 0);
               pattern[3][1]= new Box(Dice.ColorDice.WHITE, 3);
               pattern[3][4]= new Box(Dice.ColorDice.PURPLE, 0);

               break;

           case BELLESGUARD:
               //BELLESGUARD
               namePattern="Bellesguard";
               difficulty=3;
               pattern[0][0]= new Box(Dice.ColorDice.BLUE, 0);
               pattern[0][1]= new Box(Dice.ColorDice.WHITE, 6);
               pattern[0][4]= new Box(Dice.ColorDice.YELLOW, 0);
               pattern[1][1]= new Box(Dice.ColorDice.WHITE, 3);
               pattern[1][2]= new Box(Dice.ColorDice.BLUE, 0);
               pattern[2][1]= new Box(Dice.ColorDice.WHITE, 5);
               pattern[2][2]= new Box(Dice.ColorDice.WHITE, 6);
               pattern[2][3]= new Box(Dice.ColorDice.WHITE, 2);
               pattern[3][1]= new Box(Dice.ColorDice.WHITE, 4);
               pattern[3][3]= new Box(Dice.ColorDice.WHITE, 1);
               pattern[3][4]= new Box(Dice.ColorDice.GREEN, 0);

               break;

           case BATLLO:
               //BATLLO
               namePattern="Batllo";
               difficulty=5;
               pattern[0][2]= new Box(Dice.ColorDice.WHITE, 6);
               pattern[1][1]= new Box(Dice.ColorDice.WHITE, 5);
               pattern[1][2]= new Box(Dice.ColorDice.BLUE, 0);
               pattern[1][3]= new Box(Dice.ColorDice.WHITE, 4);
               pattern[2][0]= new Box(Dice.ColorDice.WHITE, 3);
               pattern[2][1]= new Box(Dice.ColorDice.GREEN, 0);
               pattern[2][2]= new Box(Dice.ColorDice.YELLOW, 0);
               pattern[2][3]= new Box(Dice.ColorDice.PURPLE, 0);
               pattern[2][4]= new Box(Dice.ColorDice.WHITE, 2);
               pattern[3][0]= new Box(Dice.ColorDice.WHITE, 1);
               pattern[3][1]= new Box(Dice.ColorDice.WHITE, 4);
               pattern[3][2]= new Box(Dice.ColorDice.RED, 0);
               pattern[3][3]= new Box(Dice.ColorDice.WHITE, 5);
               pattern[3][4]= new Box(Dice.ColorDice.WHITE, 3);

               break;


           case SHADOW_THIEF:
               //SHADOW THIEF
               namePattern="Shadow Thief";
               difficulty=5;
               pattern[0][0]= new Box(Dice.ColorDice.WHITE, 6);
               pattern[0][1]= new Box(Dice.ColorDice.PURPLE, 0);
               pattern[0][4]= new Box(Dice.ColorDice.WHITE, 5);
               pattern[1][0]= new Box(Dice.ColorDice.WHITE, 5);
               pattern[1][2]= new Box(Dice.ColorDice.PURPLE, 0);
               pattern[2][0]= new Box(Dice.ColorDice.RED, 0);
               pattern[2][1]= new Box(Dice.ColorDice.WHITE, 6);
               pattern[2][3]= new Box(Dice.ColorDice.PURPLE, 0);
               pattern[3][0]= new Box(Dice.ColorDice.YELLOW, 0);
               pattern[3][1]= new Box(Dice.ColorDice.RED, 0);
               pattern[3][2]= new Box(Dice.ColorDice.WHITE, 5);
               pattern[3][3]= new Box(Dice.ColorDice.WHITE, 4);
               pattern[3][4]= new Box(Dice.ColorDice.WHITE, 3);

               break;


           case INDUSTRIA:
               //INDUSTRIA
               namePattern="Industria";
               difficulty=5;
               pattern[0][0]= new Box(Dice.ColorDice.WHITE, 1);
               pattern[0][1]= new Box(Dice.ColorDice.RED, 0);
               pattern[0][2]= new Box(Dice.ColorDice.WHITE, 3);
               pattern[0][4]= new Box(Dice.ColorDice.WHITE, 6);
               pattern[1][0]= new Box(Dice.ColorDice.WHITE, 5);
               pattern[1][1]= new Box(Dice.ColorDice.WHITE, 4);
               pattern[1][2]= new Box(Dice.ColorDice.RED, 0);
               pattern[1][3]= new Box(Dice.ColorDice.WHITE, 2);
               pattern[2][2]= new Box(Dice.ColorDice.WHITE, 5);
               pattern[2][3]= new Box(Dice.ColorDice.RED, 0);
               pattern[2][4]= new Box(Dice.ColorDice.WHITE, 1);
               pattern[3][3]= new Box(Dice.ColorDice.WHITE, 3);
               pattern[3][4]= new Box(Dice.ColorDice.RED, 0);

               break;


           case AURORA_SAGRADIS:
               //AURORA SAGRADIS
               namePattern="Aurora Sagradis";
               difficulty=4;
               pattern[0][0]= new Box(Dice.ColorDice.RED, 0);
               pattern[0][2]= new Box(Dice.ColorDice.BLUE, 0);
               pattern[0][4]= new Box(Dice.ColorDice.YELLOW, 0);
               pattern[1][0]= new Box(Dice.ColorDice.WHITE, 4);
               pattern[1][1]= new Box(Dice.ColorDice.PURPLE, 0);
               pattern[1][2]= new Box(Dice.ColorDice.WHITE, 3);
               pattern[1][3]= new Box(Dice.ColorDice.GREEN, 0);
               pattern[1][4]= new Box(Dice.ColorDice.WHITE, 2);
               pattern[2][1]= new Box(Dice.ColorDice.WHITE, 1);
               pattern[2][3]= new Box(Dice.ColorDice.WHITE, 5);
               pattern[3][2]= new Box(Dice.ColorDice.WHITE, 6);

               break;


           case SYMPHONY_OF_LIGHT:
               //SYMPHONY OF LIGHT
               namePattern="Symphony of Light";
               difficulty=6;
               pattern[0][0]= new Box(Dice.ColorDice.WHITE, 2);
               pattern[0][2]= new Box(Dice.ColorDice.WHITE, 5);
               pattern[0][4]= new Box(Dice.ColorDice.WHITE, 1);
               pattern[1][0]= new Box(Dice.ColorDice.YELLOW, 0);
               pattern[1][1]= new Box(Dice.ColorDice.WHITE, 6);
               pattern[1][2]= new Box(Dice.ColorDice.PURPLE, 0);
               pattern[1][3]= new Box(Dice.ColorDice.WHITE, 2);
               pattern[1][4]= new Box(Dice.ColorDice.RED, 0);
               pattern[2][1]= new Box(Dice.ColorDice.BLUE, 0);
               pattern[2][2]= new Box(Dice.ColorDice.WHITE, 4);
               pattern[2][3]= new Box(Dice.ColorDice.GREEN, 0);
               pattern[3][1]= new Box(Dice.ColorDice.WHITE, 3);
               pattern[3][3]= new Box(Dice.ColorDice.WHITE, 5);

               break;


           case FIRMITAS:
               //FIRMITAS
               namePattern="Firmitas";
               difficulty=5;
               pattern[0][0]= new Box(Dice.ColorDice.PURPLE, 0);
               pattern[0][1]= new Box(Dice.ColorDice.WHITE, 6);
               pattern[0][4]= new Box(Dice.ColorDice.WHITE, 3);
               pattern[1][0]= new Box(Dice.ColorDice.WHITE, 5);
               pattern[1][1]= new Box(Dice.ColorDice.PURPLE, 0);
               pattern[1][2]= new Box(Dice.ColorDice.WHITE, 3);
               pattern[2][1]= new Box(Dice.ColorDice.WHITE, 2);
               pattern[2][2]= new Box(Dice.ColorDice.PURPLE, 0);
               pattern[2][3]= new Box(Dice.ColorDice.WHITE, 1);
               pattern[3][1]= new Box(Dice.ColorDice.WHITE, 1);
               pattern[3][2]= new Box(Dice.ColorDice.WHITE, 5);
               pattern[3][3]= new Box(Dice.ColorDice.PURPLE, 0);
               pattern[3][4]= new Box(Dice.ColorDice.WHITE, 4);

               break;


           case LUZ_CELESTIAL:
               //LUZ CELESTIAL
               namePattern="Luz Celestial";
               difficulty=3;
               pattern[0][2]= new Box(Dice.ColorDice.RED, 0);
               pattern[0][3]= new Box(Dice.ColorDice.WHITE, 5);
               pattern[1][0]= new Box(Dice.ColorDice.PURPLE, 0);
               pattern[1][1]= new Box(Dice.ColorDice.WHITE, 4);
               pattern[1][3]= new Box(Dice.ColorDice.GREEN, 0);
               pattern[1][4]= new Box(Dice.ColorDice.WHITE, 3);
               pattern[2][0]= new Box(Dice.ColorDice.WHITE, 6);
               pattern[2][3]= new Box(Dice.ColorDice.BLUE, 0);
               pattern[3][1]= new Box(Dice.ColorDice.YELLOW, 1);
               pattern[3][2]= new Box(Dice.ColorDice.WHITE, 2);

               break;


           case FIRELIGHT:
               //FIRELIGHT
               namePattern="Firelight";
               difficulty=5;
               pattern[0][0]= new Box(Dice.ColorDice.WHITE, 3);
               pattern[0][1]= new Box(Dice.ColorDice.WHITE, 4);
               pattern[0][2]= new Box(Dice.ColorDice.WHITE, 1);
               pattern[0][3]= new Box(Dice.ColorDice.WHITE, 5);
               pattern[1][1]= new Box(Dice.ColorDice.WHITE, 6);
               pattern[1][2]= new Box(Dice.ColorDice.WHITE, 2);
               pattern[1][4]= new Box(Dice.ColorDice.YELLOW, 0);
               pattern[2][3]= new Box(Dice.ColorDice.YELLOW, 0);
               pattern[2][4]= new Box(Dice.ColorDice.RED, 0);
               pattern[3][0]= new Box(Dice.ColorDice.WHITE, 5);
               pattern[3][2]= new Box(Dice.ColorDice.YELLOW, 0);
               pattern[3][3]= new Box(Dice.ColorDice.RED, 0);
               pattern[3][4]= new Box(Dice.ColorDice.WHITE, 6);

               break;


           case CHROMATIC_SPLENDOR:
               //CHROMATIC SPLENDOR
               namePattern="Chromatic Splendor";
               difficulty=4;
               pattern[0][2]= new Box(Dice.ColorDice.GREEN, 0);
               pattern[1][0]= new Box(Dice.ColorDice.WHITE, 2);
               pattern[1][1]= new Box(Dice.ColorDice.YELLOW, 0);
               pattern[1][2]= new Box(Dice.ColorDice.WHITE, 5);
               pattern[1][3]= new Box(Dice.ColorDice.BLUE, 0);
               pattern[1][4]= new Box(Dice.ColorDice.WHITE, 1);
               pattern[2][1]= new Box(Dice.ColorDice.RED, 0);
               pattern[2][2]= new Box(Dice.ColorDice.WHITE, 3);
               pattern[2][3]= new Box(Dice.ColorDice.PURPLE, 0);
               pattern[3][0]= new Box(Dice.ColorDice.WHITE, 1);
               pattern[3][2]= new Box(Dice.ColorDice.WHITE, 6);
               pattern[3][4]= new Box(Dice.ColorDice.WHITE, 4);

               break;


           case LUX_ASTRAM:
               //LUX ASTRAM
               namePattern="Lux Astram";
               difficulty=5;
               pattern[0][1]= new Box(Dice.ColorDice.WHITE, 1);
               pattern[0][2]= new Box(Dice.ColorDice.GREEN, 0);
               pattern[0][3]= new Box(Dice.ColorDice.PURPLE, 0);
               pattern[0][4]= new Box(Dice.ColorDice.WHITE, 4);
               pattern[1][0]= new Box(Dice.ColorDice.WHITE, 6);
               pattern[1][1]= new Box(Dice.ColorDice.PURPLE, 0);
               pattern[1][2]= new Box(Dice.ColorDice.WHITE, 2);
               pattern[1][3]= new Box(Dice.ColorDice.WHITE, 5);
               pattern[1][4]= new Box(Dice.ColorDice.GREEN, 0);
               pattern[2][0]= new Box(Dice.ColorDice.WHITE, 1);
               pattern[2][1]= new Box(Dice.ColorDice.GREEN, 0);
               pattern[2][2]= new Box(Dice.ColorDice.WHITE, 5);
               pattern[2][3]= new Box(Dice.ColorDice.WHITE, 3);
               pattern[2][4]= new Box(Dice.ColorDice.PURPLE, 0);

               break;


           case FRACTAL_DROPS:
               //FRACTAL DROPS
               namePattern="Fractal Drops";
               difficulty=3;
               pattern[0][1]= new Box(Dice.ColorDice.WHITE, 4);
               pattern[0][3]= new Box(Dice.ColorDice.YELLOW, 0);
               pattern[0][4]= new Box(Dice.ColorDice.WHITE, 6);
               pattern[1][0]= new Box(Dice.ColorDice.RED, 0);
               pattern[1][2]= new Box(Dice.ColorDice.WHITE, 2);
               pattern[2][2]= new Box(Dice.ColorDice.RED, 0);
               pattern[2][3]= new Box(Dice.ColorDice.PURPLE, 0);
               pattern[2][4]= new Box(Dice.ColorDice.WHITE, 1);
               pattern[3][0]= new Box(Dice.ColorDice.BLUE, 0);
               pattern[3][1]= new Box(Dice.ColorDice.YELLOW, 0);

               break;


           case GRAVITAS:
               //GRAVITAS
               namePattern="Gravitas";
               difficulty=5;
               pattern[0][0]= new Box(Dice.ColorDice.WHITE, 1);
               pattern[0][2]= new Box(Dice.ColorDice.WHITE, 3);
               pattern[0][3]= new Box(Dice.ColorDice.BLUE, 0);
               pattern[1][1]= new Box(Dice.ColorDice.WHITE, 2);
               pattern[1][2]= new Box(Dice.ColorDice.BLUE, 0);
               pattern[2][0]= new Box(Dice.ColorDice.WHITE, 6);
               pattern[2][1]= new Box(Dice.ColorDice.BLUE, 0);
               pattern[2][3]= new Box(Dice.ColorDice.WHITE, 4);
               pattern[3][0]= new Box(Dice.ColorDice.BLUE, 0);
               pattern[3][1]= new Box(Dice.ColorDice.WHITE, 5);
               pattern[3][2]= new Box(Dice.ColorDice.WHITE, 2);
               pattern[3][4]= new Box(Dice.ColorDice.WHITE, 1);

               break;


           case WATER_OF_LIFE:
               //WATER OF LIFE
               namePattern="Water of Life";
               difficulty=6;
               pattern[0][0]= new Box(Dice.ColorDice.WHITE, 6);
               pattern[0][1]= new Box(Dice.ColorDice.BLUE, 0);
               pattern[0][4]= new Box(Dice.ColorDice.WHITE, 1);
               pattern[1][1]= new Box(Dice.ColorDice.WHITE, 5);
               pattern[1][2]= new Box(Dice.ColorDice.BLUE, 0);
               pattern[2][0]= new Box(Dice.ColorDice.WHITE, 4);
               pattern[2][1]= new Box(Dice.ColorDice.RED, 0);
               pattern[2][2]= new Box(Dice.ColorDice.WHITE, 2);
               pattern[2][3]= new Box(Dice.ColorDice.BLUE, 0);
               pattern[3][0]= new Box(Dice.ColorDice.GREEN, 0);
               pattern[3][1]= new Box(Dice.ColorDice.WHITE, 6);
               pattern[3][2]= new Box(Dice.ColorDice.YELLOW, 0);
               pattern[3][3]= new Box(Dice.ColorDice.WHITE, 3);
               pattern[3][4]= new Box(Dice.ColorDice.PURPLE, 0);

               break;


           case RIPPLES_OF_LIGHT:
               //RIPPLES OF LIGHT
               namePattern="Ripples of Light";
               difficulty=5;
               pattern[0][3]= new Box(Dice.ColorDice.RED, 0);
               pattern[0][4]= new Box(Dice.ColorDice.WHITE, 5);
               pattern[1][2]= new Box(Dice.ColorDice.PURPLE, 0);
               pattern[1][3]= new Box(Dice.ColorDice.WHITE, 4);
               pattern[1][4]= new Box(Dice.ColorDice.BLUE, 0);
               pattern[2][1]= new Box(Dice.ColorDice.BLUE, 0);
               pattern[2][2]= new Box(Dice.ColorDice.WHITE, 3);
               pattern[2][3]= new Box(Dice.ColorDice.YELLOW, 0);
               pattern[2][4]= new Box(Dice.ColorDice.WHITE, 6);
               pattern[3][0]= new Box(Dice.ColorDice.YELLOW, 0);
               pattern[3][1]= new Box(Dice.ColorDice.WHITE, 2);
               pattern[3][2]= new Box(Dice.ColorDice.GREEN, 0);
               pattern[3][3]= new Box(Dice.ColorDice.WHITE, 1);
               pattern[3][4]= new Box(Dice.ColorDice.RED, 0);

               break;


           case LUX_MUNDI:
               //LUX MUNDI
               namePattern="Lux Mundi";
               difficulty=6;
               pattern[0][2]= new Box(Dice.ColorDice.WHITE, 1);
               pattern[1][0]= new Box(Dice.ColorDice.WHITE, 1);
               pattern[1][1]= new Box(Dice.ColorDice.GREEN, 0);
               pattern[1][2]= new Box(Dice.ColorDice.WHITE, 3);
               pattern[1][3]= new Box(Dice.ColorDice.BLUE, 0);
               pattern[1][4]= new Box(Dice.ColorDice.WHITE, 2);
               pattern[2][0]= new Box(Dice.ColorDice.BLUE, 0);
               pattern[2][1]= new Box(Dice.ColorDice.WHITE, 5);
               pattern[2][2]= new Box(Dice.ColorDice.WHITE, 4);
               pattern[2][3]= new Box(Dice.ColorDice.WHITE, 6);
               pattern[2][4]= new Box(Dice.ColorDice.GREEN, 0);
               pattern[3][1]= new Box(Dice.ColorDice.BLUE, 0);
               pattern[3][2]= new Box(Dice.ColorDice.WHITE, 5);
               pattern[3][3]= new Box(Dice.ColorDice.GREEN, 0);

               break;


           case COMITAS:
               //COMITAS
               namePattern="Comitas";
               difficulty=5;
               pattern[0][0]= new Box(Dice.ColorDice.YELLOW, 0);
               pattern[0][2]= new Box(Dice.ColorDice.WHITE, 2);
               pattern[0][4]= new Box(Dice.ColorDice.WHITE, 6);
               pattern[1][1]= new Box(Dice.ColorDice.WHITE, 4);
               pattern[1][3]= new Box(Dice.ColorDice.WHITE, 5);
               pattern[1][4]= new Box(Dice.ColorDice.YELLOW, 0);
               pattern[2][3]= new Box(Dice.ColorDice.YELLOW, 0);
               pattern[2][4]= new Box(Dice.ColorDice.WHITE, 5);
               pattern[3][0]= new Box(Dice.ColorDice.WHITE, 1);
               pattern[3][1]= new Box(Dice.ColorDice.WHITE, 2);
               pattern[3][2]= new Box(Dice.ColorDice.YELLOW, 0);
               pattern[3][3]= new Box(Dice.ColorDice.WHITE, 3);

               break;


           case SUNS_GLORY:
               //SUN'S GLORY
               namePattern="Sun's Glory";
               difficulty=6;
               pattern[0][0]= new Box(Dice.ColorDice.WHITE, 1);
               pattern[0][1]= new Box(Dice.ColorDice.PURPLE, 0);
               pattern[0][2]= new Box(Dice.ColorDice.YELLOW, 0);
               pattern[0][4]= new Box(Dice.ColorDice.WHITE, 4);
               pattern[1][0]= new Box(Dice.ColorDice.PURPLE, 0);
               pattern[1][1]= new Box(Dice.ColorDice.YELLOW, 0);
               pattern[1][4]= new Box(Dice.ColorDice.WHITE, 6);
               pattern[2][0]= new Box(Dice.ColorDice.YELLOW, 0);
               pattern[2][3]= new Box(Dice.ColorDice.WHITE, 5);
               pattern[2][4]= new Box(Dice.ColorDice.WHITE, 3);
               pattern[3][1]= new Box(Dice.ColorDice.WHITE, 5);
               pattern[3][2]= new Box(Dice.ColorDice.WHITE, 4);
               pattern[3][3]= new Box(Dice.ColorDice.WHITE, 2);
               pattern[3][4]= new Box(Dice.ColorDice.WHITE, 1);

               break;


           case FULGOR_DEL_CIELO:
               //FULGOR DEL CIELO
               namePattern="Fulgor del Cielo";
               difficulty=5;
               pattern[0][1]= new Box(Dice.ColorDice.BLUE, 0);
               pattern[0][2]= new Box(Dice.ColorDice.RED, 0);
               pattern[1][1]= new Box(Dice.ColorDice.WHITE, 4);
               pattern[1][2]= new Box(Dice.ColorDice.WHITE, 5);
               pattern[1][4]= new Box(Dice.ColorDice.BLUE, 0);
               pattern[2][0]= new Box(Dice.ColorDice.BLUE, 0);
               pattern[2][1]= new Box(Dice.ColorDice.WHITE, 2);
               pattern[2][3]= new Box(Dice.ColorDice.RED, 0);
               pattern[2][4]= new Box(Dice.ColorDice.WHITE, 5);
               pattern[3][0]= new Box(Dice.ColorDice.WHITE, 6);
               pattern[3][1]= new Box(Dice.ColorDice.RED, 0);
               pattern[3][2]= new Box(Dice.ColorDice.WHITE, 3);
               pattern[3][3]= new Box(Dice.ColorDice.WHITE, 1);

               break;


           default: // VOID
                namePattern="noName";
                difficulty=0;


       }
        for(int i = 0; i< ROW; i++){
            for(int j = 0; j< COLUMN; j++)
            {
                if(pattern[i][j]==null)
                    pattern[i][j]=new Box();
            }
       }
    }


    public int getHeight() {
        return ROW;
    }

    public int getWidth() {
        return COLUMN;
    }

    public Box getBox(int x, int y){

        return pattern[x][y];
    }

    public int getDifficulty() {

        return difficulty;
    }

    public TypePattern getTypePattern() {
        return typePattern;
    }

    public String getNamePattern() {

        return namePattern;
    }

    public Boolean checkEdges(int row, int col){

        if(col == 0  ||  col == COLUMN -1  ||  row == 0  ||  row == ROW -1 )
            return Boolean.TRUE;
        else
            return Boolean.FALSE;

    }


    public void print() {
        print3();
    }

    public void print1() {
        System.out.println("\nPattern name: " + getNamePattern());
        System.out.print("╭──");
        for (int i = 0; i < getWidth(); i++) {
            System.out.print("───");
        }
        System.out.println("──╮");
        for (int x = 0; x < getHeight(); x++) {
            System.out.print("│ ");
            for (int y = 0; y < getWidth(); y++) {
                Box box = getBox(x,y);
                System.out.format("%C%d ", box.getColor().name().charAt(0), box.getNumber());
            }
            System.out.println("┃");
        }
        System.out.print("╰━━");
        for (int i = 0; i < getWidth(); i++) {
            System.out.print("━━━");
        }
        System.out.println("━━╯");
        //           System.out.format("%C%d\t", pattern.getBox(y,x));
    }

    public void print2() {
//        ┌─┬─┬─┬─┬─┬─┬─┬─┬─┬─┐
//        │　　　　　　　　　　　　　　　　　　　│
//        └─┴─┴─┴─┴─┴─┴─┴─┴─┴─┘
        System.out.println("\nPattern name: " + getNamePattern());
        System.out.print("┌───");
        for (int i = 0; i < getWidth(); i++) {
            System.out.print("───");
        }
        System.out.println("──┐");
        for (int y = 0; y < getHeight(); y++) {
            System.out.print("│ ");
            for (int x = 0; x < getWidth(); x++) {
                Box box = getBox(x, y);
                System.out.format("%d%C ", box.getNumber(), box.getColor().name().charAt(0));
            }
            System.out.println("│");
        }
        System.out.print("└───");
        for (int i = 0; i < getWidth(); i++) {
            System.out.print("───");
        }
        System.out.println("─┘");
        //           System.out.format("%C%d\t", pattern.getBox(y,x));
    }
    public void print3() {
        System.out.println("\nPattern name: " + getNamePattern());
        System.out.print("╔══");
//        ╔╗╚════║║║╝
//        ╧ ╤ ╟┼╢──│
        for (int i = 0; i < getWidth()-1; i++) {
            System.out.print("═══╤══");
        }
        System.out.println("═══╗");
        for (int y = 0; y < getHeight(); y++) {
            System.out.print("║");
            for (int x = 0; x < getWidth(); x++) {
                Box box = getBox(y, x);
                System.out.format(" %d%C ", box.getNumber(), box.getColor().name().charAt(0));
                if( x!= getWidth()-1) {
                    if(x%2 == 0)
                       System.out.print("|");
                    else
                       System.out.print("│");
                }
            }
            System.out.println("║");
            if(y != getHeight()-1) {
                System.out.print("╟──");
                for (int x = 0; x < getWidth() - 1; x++) {
                    System.out.print("───┼──");
                }
                System.out.println("───╢");
            }
        }
        System.out.print("╚══");
        for (int i = 0; i < getWidth()-1; i++) {
            System.out.print("═══╧══");
        }
        System.out.println("═══╝");
        //           System.out.format("%C%d\t", pattern.getBox(y,x));
    }
}
