package porprezha.model;

import porprezha.model.cards.Board;
import porprezha.model.cards.Pattern;
import porprezha.model.dices.Dice;
import porprezha.model.dices.DiceBag;
import porprezha.model.dices.DraftPool;

public class TestDrive {

    public static void main(String[] args) {

        DiceBag dicebag = new DiceBag();

        DraftPool draftPool = new DraftPool(dicebag, 4 );

        Pattern pattern = new Pattern(0);


        for (int i = 0; i <4 ; i++) {
            for (int j = 0; j<5 ; j++) {
                System.out.print(pattern.getBox(i,j).getNumber());
                System.out.print(pattern.getBox(i,j).getColor() + " ");
            }
            System.out.println();
        }
        System.out.println();

        for (Dice cas:
             draftPool.diceList()) {
            System.out.print(cas.getDiceNumber());
            System.out.print(cas.colorDice);
        };

        Dice dice0=draftPool.chooseDice(0);
        Dice dice1=draftPool.chooseDice(0);
        Dice dice2=draftPool.chooseDice(0);

        System.out.println();


        Board board = new Board(pattern);
        //System.out.println(board.insertDice(dice, 0,1));

        //System.out.println(board.insertDice(dice1, 0,1));

        board.insertDice(dice0, 0, 0);

        board.insertDice(dice1, 0,1);
        board.insertDice(dice2, 1, 1);
        for (int i = 0; i <4 ; i++) {
            for (int j = 0; j<5 ; j++) {
                System.out.print(board.getDice(i,j).getDiceNumber());
                System.out.print(board.getDice(i,j).colorDice + " ");
            }
            System.out.println();
        }
    }


    }
