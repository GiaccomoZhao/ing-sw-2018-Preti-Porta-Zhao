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
