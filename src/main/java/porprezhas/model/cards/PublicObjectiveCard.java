package porprezhas.model.cards;
import porprezhas.model.dices.Board;
import porprezhas.model.dices.Dice;
import porprezhas.model.dices.DiceBag;
import porprezhas.model.dices.DraftPool;
import porprezhas.model.track.RoundTrack;

import java.io.Serializable;

import static porprezhas.model.dices.Board.ROW;
import static porprezhas.model.dices.Board.COLUMN;

public class PublicObjectiveCard extends ObjectiveCard implements Serializable {

    public PublicObjectiveCard(Effect effect) {

        super(effect);
    }


    @Override
    public int apply(Board board) {

        int i, j;
        int point=0;
        int counter=0;
        int helper=1;
        int[] num = new int[6];


      switch(this.effect){

          case PUC1:

              for(i=0; i < ROW; i++) {
                  for (j = 0; j <COLUMN; j++){
                      if(board.getDice(i, j).getColorDice().equals(Dice.ColorDice.WHITE))
                          counter=100;

                      counter = counter + (board.getDice(i, j).getColorDice().ordinal()+1);
                        helper= helper*(board.getDice(i, j).getColorDice().ordinal()+1);
                  }
                  if(counter==15 && helper==120)
                      point++;
              }
              return this.getNumScore()*point;


          case PUC2:

              for(j=0; j <COLUMN; j++) {
                  for (i = 0; i < ROW; i++){
                      if(board.getDice(i, j).getColorDice().equals(Dice.ColorDice.WHITE))
                          counter=100;

                      counter = counter + (board.getDice(i, j).getColorDice().ordinal()+1);
                      helper= helper*(board.getDice(i, j).getColorDice().ordinal()+1);
                  }
                  if((counter==10 && helper==24)||(counter==11 && helper==30)||(counter==12 && helper==40)||(counter==13 && helper==60)||(counter==14 && helper==120))
                      point++;
              }
              return this.getNumScore()*point;


          case PUC3:

              for(i=0; i < ROW; i++) {
                  for (j = 0; j <COLUMN; j++){
                      if(board.getDice(i, j).getColorDice().equals(Dice.ColorDice.WHITE))
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


          case PUC4:

              for(j=0; j <COLUMN; j++) {
                  for (i = 0; i< ROW; i++){
                      if(board.getDice(i, j).getColorDice().equals(Dice.ColorDice.WHITE))
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


          case PUC5:

              for(i=0; i < ROW; i++) {
                  for (j = 0; j <COLUMN; j++){
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



          case PUC6:

              for(i=0; i < ROW; i++) {
                  for (j = 0; j <COLUMN; j++){
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


          case PUC7:

              for(i=0; i < ROW; i++) {
                  for (j = 0; j <COLUMN; j++){
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


          case PUC8:

              for(i=0; i < ROW; i++) {
                  for (j = 0; j <COLUMN; j++){
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


          case PUC9:

              boolean supportDiagonal[][]=new boolean[ROW][COLUMN];
              Dice.ColorDice colorSupport;

              for(i=0; i < ROW; i++) {
                  for (j = 0; j <COLUMN; j++){

                      if(board.getDice(i,j).getColorDice()!= Dice.ColorDice.WHITE){
                          colorSupport=board.getDice(i, j).getColorDice();

                          if(i>0 && j>0)
                              if(board.getDice(i-1,j-1).getColorDice()==colorSupport){
                                  supportDiagonal[i][j]=true;
                                  supportDiagonal[i-1][j-1]=true;
                              }

                          if(i>0 && j<COLUMN-1)
                              if(board.getDice(i-1,j+1).getColorDice()==colorSupport){
                                  supportDiagonal[i][j]=true;
                                  supportDiagonal[i-1][j+1]=true;
                              }

                          if(i<ROW-1 && j>0)
                              if(board.getDice(i+1,j-1).getColorDice()==colorSupport){
                                  supportDiagonal[i][j]=true;
                                  supportDiagonal[i+1][j-1]=true;
                              }

                          if(i<ROW-1 && j<COLUMN-1)
                              if(board.getDice(i+1,j+1).getColorDice()==colorSupport){
                                  supportDiagonal[i][j]=true;
                                  supportDiagonal[i+1][j+1]=true;
                              }
                      }


                  }
              }

              for(i=0;i<ROW;i++){
                  for(j=0;j<COLUMN;j++){

                      if(supportDiagonal[i][j])
                          point++;

                  }
              }


              return point;


          case PUC10:

              for(i=0; i < ROW; i++) {
                  for (j = 0; j <COLUMN; j++){
                      for(counter=0; counter<num.length; counter++) {
                          if(board.getDice(i,j).getColorDice().ordinal()+1 == counter +1)
                              num[counter] = num[counter] + 1;
                      }
                  }
              }

              point=num[0];
              for(counter=1;counter<num.length;counter++){
                  if(num[counter]<point)
                      point=num[counter];
              }

              return this.getNumScore()*point;



      }
      return 0;
    }
}
