package PorPreZha.Model;



public class Pattern {



    private final String namePattern;
    private final int difficulty;
    private Box[][] pattern;

    public Pattern(int typePattern) {
       pattern = new Box[4][5];

       switch(typePattern){
           case 0:
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

           case 1:
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

           case 2:
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

           case 3:
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

           case 4:
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

           case 5:
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

           case 6:
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


           case 7:
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


           case 8:
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


           case 9:
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


           case 10:
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


           case 11:
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


           case 12:
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


           case 13:
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


           case 14:
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


           case 15:
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


           case 16:
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


           case 17:
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


           case 18:
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


           case 19:
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


           case 20:
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


           case 21:
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


           case 22:
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


           case 23:
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



           default:
                namePattern="noName";
                difficulty=0;


       }
       for(int i=0;i<4;i++){
           for(int j=0;j<5;j++){
            if(pattern[i][j]==null)
                pattern[i][j]=new Box();
           }
       }
    }
    

    public Box getBox(int x, int y){

        return pattern[x][y];
    }

    public int getDifficulty() {

        return difficulty;
    }

    public String getNamePattern() {

        return namePattern;
    }

    public Boolean checkEdges(int x, int y){

        if(x==0 || x== 4 || y==0 || y==3 )
            return Boolean.TRUE;
        else
            return Boolean.FALSE;

    }







}
