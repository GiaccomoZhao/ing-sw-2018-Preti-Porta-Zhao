package porprezha.model.cards;
import porprezha.model.dices.Dice;





import java.util.ArrayList;

public class PublicObjectiveCard extends ObjectiveCard{

    public PublicObjectiveCard(Effect effect) {
        super(effect);
    }

    @Override
    public int apply(Board board) {

        int i, j;
        int point=0;
        int counter=0;
        int helper=1;
        int[] num = new int[0];


      switch(this.effect.ID){

          case 0:

              for(i=0; i < 4; i++) {
                  for (j = 0; j < 5; j++){
                      if(board.getDice(i, j).colorDice.equals(Dice.ColorDice.WHITE))
                          counter=100;

                      counter = counter + (board.getDice(i, j).colorDice.ordinal()+1);
                        helper= helper*(board.getDice(i, j).colorDice.ordinal()+1);
                  }
                  if(counter==15 && helper==120)
                      point++;
              }
              return this.getNumScore()*point;


          case 1:

              for(j=0; j < 5; j++) {
                  for (i = 0; i < 4; i++){
                      if(board.getDice(i, j).colorDice.equals(Dice.ColorDice.WHITE))
                          counter=100;

                      counter = counter + (board.getDice(i, j).colorDice.ordinal()+1);
                      helper= helper*(board.getDice(i, j).colorDice.ordinal()+1);
                  }
                  if((counter==10 && helper==24)||(counter==11 && helper==30)||(counter==12 && helper==40)||(counter==13 && helper==60)||(counter==14 && helper==120))
                      point++;
              }
              return this.getNumScore()*point;


          case 2:

              for(i=0; i < 4; i++) {
                  for (j = 0; j < 5; j++){
                      if(board.getDice(i, j).colorDice.equals(Dice.ColorDice.WHITE))
                          counter=100;

                      counter = counter + (board.getDice(i, j).getDiceNumber());
                      helper= helper*(board.getDice(i, j).getDiceNumber());
                  }
                  if((counter==15 && helper==120)||(counter==16 && helper==144)
                          ||(counter==17 && helper==180)||(counter==18 && helper==240)
                          ||(counter==19 && helper==360)||(counter==20 && helper==720))
                      point++;
              }
              return this.getNumScore()*point;


          case 3:

              for(j=0; j < 5; j++) {
                  for (i = 0; i< 4; i++){
                      if(board.getDice(i, j).colorDice.equals(Dice.ColorDice.WHITE))
                          counter=100;

                      counter = counter + (board.getDice(i, j).getDiceNumber());
                      helper= helper*(board.getDice(i, j).getDiceNumber());
                  }
                  if((counter==10 && helper==24)||(counter==11 && helper==30)||(counter==12 && helper==36)
                          ||(counter==12 && helper==40)||(counter==13 && helper==48)||(counter==14 && helper==60)
                          ||(counter==13 && helper==60)||(counter==14 && helper==72)||(counter==15 && helper==90)
                          ||(counter==16 && helper==120)||(counter==14 && helper==120)||(counter==15 && helper==144)
                          ||(counter==16 && helper==180)||(counter==17 && helper==240)||(counter==18 && helper==360))
                      point++;
              }
              return this.getNumScore()*point;


          case 4:

              for(i=0; i < 4; i++) {
                  for (j = 0; j < 5; j++){
                      if(board.getDice(i,j).getDiceNumber()==1)
                          num[0]=num[0]+1;

                      if(board.getDice(i,j).getDiceNumber()==2)
                          num[1]=num[1]+1;
                  }
              }

              if(num[0]<=num[1])
                  point=num[0];

              else
                  point=num[1];

              return this.getNumScore()*point;



          case 5:

              for(i=0; i < 4; i++) {
                  for (j = 0; j < 5; j++){
                      if(board.getDice(i,j).getDiceNumber()==3)
                          num[0]=num[0]+1;

                      if(board.getDice(i,j).getDiceNumber()==4)
                          num[1]=num[1]+1;
                  }
              }

              if(num[0]<=num[1])
                  point=num[0];

              else
                  point=num[1];

              return this.getNumScore()*point;


          case 6:

              for(i=0; i < 4; i++) {
                  for (j = 0; j < 5; j++){
                     if(board.getDice(i,j).getDiceNumber()==5)
                          num[0]=num[0]+1;

                      if(board.getDice(i,j).getDiceNumber()==6)
                          num[1]=num[1]+1;
                  }
              }

              if(num[0]<=num[1])
                  point=num[0];

              else
                  point=num[1];

              return this.getNumScore()*point;


          case 7:

              for(i=0; i < 4; i++) {
                  for (j = 0; j < 5; j++){
                      for(counter=0;counter<6;counter++) {
                          if (board.getDice(i, j).getDiceNumber() == counter + 1)
                              num[counter] = num[counter] + 1;
                      }
                  }
              }

              point=num[0];
              for(counter=1;counter<6;counter++){
                  if(num[counter]<point)
                      point=num[counter];
              }

              return this.getNumScore()*point;


          case 8:

              boolean supportDiagonal[][]=new boolean[4][5];
              Dice.ColorDice colorSupport;

              for(i=0; i < 4; i++) {
                  for (j = 0; j < 5; j++){

                      colorSupport=board.getDice(i, j).colorDice;

                      if(i>0&&j>0)
                          if(board.getDice(i-1,j-1).colorDice==colorSupport){
                              supportDiagonal[i][j]=true;
                              supportDiagonal[i-1][j-1]=true;
                          }

                      if(i>0&&j<4)
                          if(board.getDice(i-1,j+1).colorDice==colorSupport){
                              supportDiagonal[i][j]=true;
                              supportDiagonal[i-1][j+1]=true;
                          }

                      if(i<3&&j>0)
                          if(board.getDice(i+1,j-1).colorDice==colorSupport){
                              supportDiagonal[i][j]=true;
                              supportDiagonal[i+1][j-1]=true;
                          }

                      if(i<3&&j<4)
                          if(board.getDice(i+1,j+1).colorDice==colorSupport){
                              supportDiagonal[i][j]=true;
                              supportDiagonal[i+1][j+1]=true;
                          }

                  }
              }
              
              for(i=0;i<4;i++){
                  for(j=0;j<5;j++){

                      if(supportDiagonal[i][j])
                          point++;

                  }
              }


              return point;


          case 9:

              for(i=0; i < 4; i++) {
                  for (j = 0; j < 5; j++){
                      for(counter=0;counter<6;counter++) {
                          if(board.getDice(i,j).colorDice.ordinal()+1 == counter +1)
                              num[counter] = num[counter] + 1;
                      }
                  }
              }

              point=num[0];
              for(counter=1;counter<6;counter++){
                  if(num[counter]<point)
                      point=num[counter];
              }

              return this.getNumScore()*point;



      }
      return 0;
    }
}
