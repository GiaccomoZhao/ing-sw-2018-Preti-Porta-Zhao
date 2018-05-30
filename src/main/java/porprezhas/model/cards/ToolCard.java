package porprezhas.model.cards;
import porprezhas.model.dices.Board;
import porprezhas.model.dices.Dice;
import porprezhas.model.dices.DiceBag;
import porprezhas.model.dices.DiceBag.*;
import porprezhas.model.dices.DraftPool;
import porprezhas.model.track.RoundTrack;


import java.io.Serializable;

import static porprezhas.model.dices.Dice.ColorDice.*;



public class ToolCard extends Card implements Serializable {


    private int tokensQuantity;

    private  Dice.ColorDice cardColor;

    Dice.ColorDice[] cardColors={PURPLE,BLUE,RED,YELLOW,GREEN,PURPLE,BLUE,RED,YELLOW,GREEN,PURPLE,BLUE};

    public ToolCard(Effect effect) {
        super(effect);
        //if(effect.ID<16) throw  new InvalidIdException();

        this.tokensQuantity = 0;
        this.cardColor = cardColors[this.effect.ID-16];
    }



    public void addTokens(){

        tokensQuantity = tokensQuantity + 1;
    }


    @Override
    public int apply(Board board) {
        return 0;
    }

    public void use(Board board, DraftPool draftPool, int xStart1, int yStart1, int xDestination1, int yDestination1, int xStart2, int yStart2, int xDestination2, int yDestination2, Dice dice1, int  number, boolean operation, RoundTrack roundTrack, DiceBag diceBag) {

        switch (this.effect.ID-16) {

            case 0:

                if(dice1 .getDiceNumber()!=6&&operation)
                    draftPool.chooseDice(number).setNumber(draftPool.chooseDice(number).getDiceNumber()+1);

                if(dice1 .getDiceNumber()!=1&&!operation)
                        draftPool.chooseDice(number).setNumber(draftPool.chooseDice(number).getDiceNumber()-1);

                break;

            case 1:

                if(board.canBeRemoved(xStart1,yStart1))
                board.insertDiceWithoutColorRestrictions(board.removeDice(xStart1,yStart1),xDestination1,yDestination1);
                break;

            case 2:

                if(board.canBeRemoved(xStart1,yStart1))
                    board.insertDiceWithoutNumberRestrictions(board.removeDice(xStart1,yStart1),xDestination1,yDestination1);
                break;


            case 3:

                if(board.canBeRemoved(xStart1,yStart1))
                    board.insertDice(board.removeDice(xStart1,yStart1),xDestination1,yDestination1);
                if(board.canBeRemoved(xStart2,yStart2))
                    board.insertDice(board.removeDice(xStart2,yStart2),xDestination2,yDestination2);
                break;


            case 4:
                //remove the die from the roundTrack
                roundTrack.removeDice(xStart1,dice1);
                //add the die removed from the roundTrack to the draftPool and replace the old die in the roundTrack
                // with the die in the chosen position
                roundTrack.addExternalDice(xStart1,draftPool.diceSubstitute(dice1,xStart2));
                break;


            case 5:
                //rolls a choosen die from the draftPool and, if it cannot placed in the board, re-adds it to the draftPool
                Dice rolledDice;
                rolledDice = draftPool.chooseDice(number);
                rolledDice.roll();
                boolean flag = false;

                for(int i=0; i<4; i++){
                    for(int j=0; j<5; j++){
                       if(board.validMove(rolledDice,i,j))
                            flag = true;
                    }
                }

                /*if(!flag)*/
                    draftPool.addDice(rolledDice);
               break;

               //this card can be used only on the second turn before drafting
            case 6:

                draftPool.diceList().forEach(Dice::roll);

                break;

                //this card can be used only after the first turn, if it's used the player must skip the second turn of the round
            case 7:

                draftPool.chooseDice(number);
                break;

            case 8:

                board.insertDiceWithoutAdjacentRestrictions(draftPool.chooseDice(number),xDestination1,yDestination1);
                break;


            case 9:

                if((draftPool.chooseDice(number).getDiceNumber()-6)<0){
                    draftPool.chooseDice(number).setNumber(7-draftPool.chooseDice(number).getDiceNumber());
                }

                else
                    draftPool.chooseDice(number).setNumber(draftPool.chooseDice(number).getDiceNumber()-5);

                break;


            case 10:

               Dice dice;

               diceBag.addDice(draftPool.chooseDice(number));
               dice=diceBag.extractDice();

               dice.setNumber(number);

               board.insertDice(dice,xDestination1,yDestination1);

               break;


            case 11:

                boolean exists=false;

                if(board.getPattern().getBox(xStart1,yStart1).getColor()==board.getPattern().getBox(xStart2,yDestination2).getColor());

                for(int i=1;i<=roundTrack.getActualRound();i++){

                    for (Dice die:roundTrack.getRoundDice(i))
                         {
                        if (die.getColorDice().equals(board.getPattern().getBox(xStart1,yStart1).getColor()))
                            exists=true;
                    }
                }

                if(exists) {

                    if (board.canBeRemoved(xStart1, yStart1))
                        board.insertDice(board.removeDice(xStart1, yStart1), xDestination1, yDestination1);
                    if (board.canBeRemoved(xStart2, yStart2))
                        board.insertDice(board.removeDice(xStart2, yStart2), xDestination2, yDestination2);
                }

                break;

        }

    }



}
