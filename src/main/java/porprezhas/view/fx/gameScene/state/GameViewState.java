package porprezhas.view.fx.gameScene.state;

import porprezhas.exceptions.GameAbnormalException;
import porprezhas.model.cards.Card;
import porprezhas.model.cards.ToolCardParamBuilder;
import porprezhas.model.cards.ToolCardStrategy;
import porprezhas.view.fx.gameScene.controller.GameViewController;
import porprezhas.view.fx.gameScene.controller.component.*;

import java.util.ArrayList;
import java.util.List;

import static porprezhas.view.fx.gameScene.GuiSettings.bDebug;

public class GameViewState implements SubController {
    GameViewController gameViewController;
    List<DiceContainer> diceContainers; // used to activate or disable

    Card.Effect usingCard;
    int iProcess;

    // save params to send to server
    ToolCardParamBuilder params = new ToolCardParamBuilder();

    List<DiceContainerType> fromContainers;
    List<DiceView> savedDiceViews;
    List<DiceContainerType> toContainers;
    List<Integer> param1;
    List<Integer> param2;

    @Override
    public void setupSubController(GameViewController gameViewController) {
        this.gameViewController = gameViewController;
    }

    public GameViewState() {
        iProcess = 0;
        this.usingCard = null;

        savedDiceViews = new ArrayList<>();
        fromContainers = new ArrayList<>();
        toContainers = new ArrayList<>();
        param1 = new ArrayList<>();
        param2 = new ArrayList<>();

        diceContainers = new ArrayList<>();
    }

    public void setup(DraftPoolView draftPoolView, RoundTrackBoardView roundTrackBoard, BoardView board) {    //, BoardView board2, BoardView board3, BoardView board4) {
        iProcess = 0;
        this.usingCard = null;

        savedDiceViews = new ArrayList<>();
        fromContainers = new ArrayList<>();
        toContainers = new ArrayList<>();
        param1 = new ArrayList<>();
        param2 = new ArrayList<>();


        diceContainers = new ArrayList<>();
        diceContainers.add(draftPoolView);
        diceContainers.add(roundTrackBoard);
        diceContainers.add(null);   // BAG
        diceContainers.add(board);
    }

    @Override
    public void activate() {
        diceContainers.get(DiceContainerType.DRAFT.toInt())
                .activate();
        diceContainers.get(DiceContainerType.BOARD1.toInt())
                .activate();
        diceContainers.get(DiceContainerType.TRACK.toInt())
                .activate();
    }

    @Override
    public void disable() {
        for (SubController diceContainer : diceContainers) {
            if(null != diceContainer)
                diceContainer.disable();
        }
    }

    public void clear() {
        params = new ToolCardParamBuilder();


        fromContainers.clear();
        savedDiceViews.clear();
        toContainers.clear();
        param1.clear();
        param2.clear();

    }

    public Card.Effect hasUsingCard() {
        return usingCard;
    }


    public void useToolCard(Card.Effect usingCard) {
        this.usingCard = usingCard;
        clear();

        if(usingCard == null) {
            iProcess = 0;
            activate();

        } else {
            disable();
            switch (usingCard) {
                case TC1:
                    diceContainers.get(DiceContainerType.DRAFT.toInt())
                            .activate();
                    break;
                case TC2:
                case TC3:
                    // move dice inside board one time
                    diceContainers.get(DiceContainerType.BOARD1.toInt())
                            .activate();
                    break;
                case TC4:
                case TC12:
                    // move dice in board two time
                    diceContainers.get(DiceContainerType.BOARD1.toInt())
                            .activate();
                    iProcess ++;
                    break;
                case TC5:
                    // exchange between round track and draft pool
                    diceContainers.get(DiceContainerType.TRACK.toInt())
                            .activate();
                    diceContainers.get(DiceContainerType.DRAFT.toInt())
                            .activate();
                    break;
                case TC6:
                    // chosen dice from the draftPool
                    // if can be placed, place it in board
                    diceContainers.get(DiceContainerType.DRAFT.toInt())
                            .activate();
                    break;
                case TC7:
                    // no constraint
                    activate();
                    action();
                    break;
                case TC8:
                case TC9:
                    // move a dice from draft pool to board
                    diceContainers.get(DiceContainerType.DRAFT.toInt())
                            .activate();
                    diceContainers.get(DiceContainerType.BOARD1.toInt())
                            .activate();
                    break;
                case TC10:
                    // no constraint
                    activate();
                    action();
                    break;
                case TC11:
                    // 1. discards a dice in draft pool for
                    //      pick a new dice from diceBag and
                    // 2. choose it's value to
                    // 3. place in board

                    if(iProcess == 1 -1)
                        diceContainers.get(DiceContainerType.DRAFT.toInt())
                                .activate();
                    if(iProcess == 2 -1)
                        ;// dialog box
                    if(iProcess == 3 -1)
                        diceContainers.get(DiceContainerType.BOARD1.toInt())
                                .activate();
                    iProcess++;
                    break;
            }
        }
    }

    public boolean clickDice(int idBoard, DiceView diceView) {
        if( null != this.hasUsingCard() ) {
            if(idBoard == DiceContainerType.DRAFT.toInt()) {
                params.build(idBoard, diceView.getDiceID(), -1, -1, -1);
            }
            return this.action();
        }
        return false;
    }


    public boolean moveDice(int idBoardFrom, DiceView diceView, int idBoardTo, int toRow, int toCol) {
        boolean bSuccess = false;
        int fromRow = diceView.getRow();
        int fromCol = diceView.getColumn();
        long diceID = diceView.getDiceID();

        params.build(idBoardFrom, diceView.getDiceID(), idBoardTo, toRow, toCol);
/*
        fromContainers.add(DiceContainerType.fromInt(idBoardFrom));
        savedDiceViews.add(diceView);
        toContainers.add(DiceContainerType.fromInt(idBoardTo));
        param1.add(toRow);
        param2.add(toCol);
*/

//        bSuccess = this.action();
        if( null != this.hasUsingCard() ) {
            bSuccess = this.action();
        } else {

            switch (DiceContainerType.fromInt(idBoardFrom)) {
                case DRAFT:
//                int index = draftPoolView.getIndexByDiceID(diceID);
                    if(idBoardTo >=  DiceContainerType.BOARD1.ordinal())
                    bSuccess = gameViewController.insertDice(diceID, toRow, toCol);
                    break;
                case BAG:
                    break;
                case TRACK:
                    // TODO: save the action for tool card?
                    break;
                case BOARD1:
                case BOARD2:
                case BOARD3:
                case BOARD4:
                    // todo
                    break;
                default:
                    throw new GameAbnormalException("Abnormal Error in moveDice with dice container id = " + DiceContainerType.fromInt(idBoardFrom));
            }
        }
        return bSuccess;
    }

    public boolean action () {
        boolean bResult = false;
        // send action
        if(null != usingCard) {
//            ToolCardParamBuilder params = new ToolCardParamBuilder();
//            for (int i = 0; i < savedDiceViews.size(); i++) {
//                params.build(fromContainers.get(i).toInt(), savedDiceViews.get(i).getDiceID(), toContainers.get(i).toInt(), param1.get(i), param2.get(i));
//            }
            if(params.getParams().size() == ToolCardStrategy.parameterSizes[ usingCard.ID ]) {
                if (bDebug) {
                    System.out.print("\nparams:");
                    for (int p : params.getParams()) {
                        System.out.print(" \t" + p);
                    }
                }
                bResult = gameViewController.useToolCard(usingCard.ID, params.getParams());
                clear();
            } else {
                if (bDebug) {
                    System.err.println("required param quantity = " + ToolCardStrategy.parameterSizes[ usingCard.ID ] + "\t inputed = " + params.getParams().size());
                }
            }
        }
        else {
            // insert dice normally when we are using any tool card
            if(fromContainers.equals(DiceContainerType.DRAFT)  &&  toContainers.get(0).equals(DiceContainerType.BOARD1)) {
                if(savedDiceViews.size() > 0)
                    bResult = gameViewController.insertDice(savedDiceViews.get(0).getDiceID(), param1.get(0), param2.get(0) );
            }
            clear();
        }
        return bResult;
    }

}
