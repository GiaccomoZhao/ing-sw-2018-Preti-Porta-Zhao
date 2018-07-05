package porprezhas.view.fx.gameScene.state;

import porprezhas.Useful;
import porprezhas.control.GameController;
import porprezhas.exceptions.GameAbnormalException;
import porprezhas.model.cards.*;
import porprezhas.model.dices.Dice;
import porprezhas.view.fx.gameScene.controller.GameViewController;
import porprezhas.view.fx.gameScene.controller.component.*;
import porprezhas.view.fx.gameScene.controller.dialogBox.DiceBox;
import porprezhas.view.fx.gameScene.controller.dialogBox.IncDecBox;

import java.util.ArrayList;
import java.util.List;

import static porprezhas.model.cards.ToolCardParamType.DIALOG_BOX;
import static porprezhas.view.fx.gameScene.GuiSettings.TRACK_DICE_ZOOM;
import static porprezhas.view.fx.gameScene.GuiSettings.bDebug;
import static porprezhas.view.fx.gameScene.state.DiceContainerType.BOARD1;
import static porprezhas.view.fx.gameScene.state.DiceContainerType.DRAFT;
import static porprezhas.view.fx.gameScene.state.DiceContainerType.TRACK;

public class GameViewState implements SubController {
    GameViewController gameViewController;
    List<DiceContainer> diceContainers; // used to activate or disable

    Card.Effect usingCard;
    int iProcess;

    DiceBox diceBox;

    // save params to send to server
    ToolCardParamBuilder params;// = new ToolCardParamBuilder();

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
        diceContainers.add(null);   // Dice Box
        diceContainers.add(board);
    }

    @Override
    public void activate() {
        diceContainers.get(DRAFT.toInt())
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
//        iProcess = 0;

        params = null; // new ToolCardParamBuilder();

        fromContainers.clear();
        savedDiceViews.clear();
        toContainers.clear();
        param1.clear();
        param2.clear();

        if(null != diceBox) {
            diceBox.close();
        }

    }

    public boolean hasUsingCard() {
        return null != usingCard;
    }


    public void useToolCard(Card.Effect usingCard) {
        this.usingCard = usingCard;

        clear();

        if(usingCard == null) {
            iProcess = 0;
            clear();
            activate();
            diceBox.close();

        } else {
            disable();
            switch (usingCard) {
                case TC1:
                    diceContainers.get(DRAFT.toInt())
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
                    break;
                case TC5:
                    // exchange between round track and draft pool
                    diceContainers.get(DiceContainerType.TRACK.toInt())
                            .activate();
                    diceContainers.get(DRAFT.toInt())
                            .activate();
                    break;
                case TC7:
                    // no constraint
                    params = new ToolCardParamBuilder(usingCard.ID, 0);
                    action();
                    break;
                case TC8:
                case TC9:
                    // move a dice from draft pool to board
                    diceContainers.get(DRAFT.toInt())
                            .activate();
                    diceContainers.get(DiceContainerType.BOARD1.toInt())
                            .activate();
                    break;
                case TC10:
                    // choose one dice in draft
                    diceContainers.get(DRAFT.toInt())
                            .activate();
                    break;

                case TC6:
                    // 1. chosen dice from the draftPool
                    // 2. if can be placed, place it in board
                    if(iProcess == 0) {
                        diceContainers.get(DRAFT.toInt())
                                .activate();
                    } else if(iProcess == 1) {
                        diceContainers.get(DiceContainerType.BOARD1.toInt())
                                .activate();
                    }
                    break;

                case TC11:
                    // 1. discards a dice in draft pool for
                    //      pick a new dice from diceBag and
                    // 2. choose it's value to
                    // 3. place in board

                    if(iProcess == 0)
                        diceContainers.get(DRAFT.toInt())
                                .activate();
                    if(iProcess == 1)
                            diceContainers.get(DiceContainerType.BOARD1.toInt())
                                .activate();
                    break;
            }
        }
    }

    public boolean clickDice(int idBoard, DiceView diceView) {
        if( this.hasUsingCard() ) {
            if(null == params)
                params = new ToolCardParamBuilder(usingCard.ID, iProcess);

            if(idBoard == DRAFT.toInt()) {
                int indexDice = gameViewController.getDraftPoolView().getIndexByDiceID(diceView.getDiceID());

                params.build(ToolCardParamType.values()[idBoard], indexDice);

                if(usingCard.ID == Card.Effect.TC1.ID) {
                    Boolean incDec = new IncDecBox().display();
                    if(null != incDec) {
                        params.build(DIALOG_BOX, incDec ? 1 : 0);
                    } else {    // when user click X to close the dialog box
                        useToolCard(null);  // reset the view, as not used tool card
                        return false;
                    }
                }

            } else if(idBoard == TRACK.toInt()) {
                params.build(ToolCardParamType.values()[idBoard], diceView.getRow()+1, diceView.getColumn());   // index of round in game starts from 1 instead of 0
            }

            return this.action();
        }
        return false;
    }


    public boolean moveDice(int idBoardFrom, DiceView diceView, int idBoardTo, int toRow, int toCol) {
        boolean bSuccess = false;
/*
        fromContainers.add(DiceContainerType.fromInt(idBoardFrom));
        savedDiceViews.add(diceView);
        toContainers.add(DiceContainerType.fromInt(idBoardTo));
        param1.add(toRow);
        param2.add(toCol);
*/

//        bSuccess = this.action();
        if( this.hasUsingCard() ) {

            // build the params
            if(null == params)
                params = new ToolCardParamBuilder(usingCard.ID, iProcess);

            int fromRow = diceView.getRow();
            int fromCol = diceView.getColumn();


            if(idBoardFrom == DRAFT.toInt()) {
                int indexDice = gameViewController.getDraftPoolView().getIndexByDiceID(diceView.getDiceID());
                params.build(ToolCardParamType.values()[idBoardFrom], indexDice);
//                params.add(indexDice);

            } else if(Useful.isValueBetweenInclusive( idBoardTo,
                    DiceContainerType.BOARD1.toInt(), DiceContainerType.BOARD4.toInt())) {
                params.build(ToolCardParamType.values()[idBoardFrom], fromRow, fromCol);

            } else if(idBoardFrom == DiceContainerType.TRACK.toInt()) {
                params.build(ToolCardParamType.values()[idBoardFrom], fromRow+1, fromCol);

            } else if(idBoardFrom == DiceContainerType.BOX.toInt()) {
                params.build(DIALOG_BOX, diceView.getRow());    // used only for TC11, getRow == index in the available dice list
            }

            if(idBoardTo == DRAFT.toInt()) {
                int indexDice = gameViewController.getDraftPoolView().getIndexByDiceID(diceView.getDiceID());
                params.build(ToolCardParamType.values()[idBoardTo], indexDice);
//                params.add(indexDice);

            } else if(Useful.isValueBetweenInclusive( idBoardTo,
                    DiceContainerType.BOARD1.toInt(), DiceContainerType.BOARD4.toInt())) {
                params.build(ToolCardParamType.values()[BOARD1.toInt()], toRow, toCol);

            } else if(idBoardFrom == DiceContainerType.TRACK.toInt()) {
                params.build(ToolCardParamType.values()[idBoardFrom], fromRow+1, fromCol);
            }

            bSuccess = this.action();
        } else {

            // not using tool card: we can only insert dice
            if (DiceContainerType.fromInt(idBoardFrom) == DRAFT  &&
                    idBoardTo >=  DiceContainerType.BOARD1.ordinal()) {
//                int index = draftPoolView.getIndexByDiceID(diceID);
                bSuccess = gameViewController.insertDice(diceView.getDiceID(), toRow, toCol);
            }
        }
        return bSuccess;
    }

    public boolean action () {
        boolean bResult = false;
        // send action
        if(null != usingCard) {
            if(null == params) {    // this should never happens
                System.err.println("Trying to use a card without initializing parameters");
                return false;
            }
//            ToolCardParamBuilder params = new ToolCardParamBuilder();
//            for (int i = 0; i < savedDiceViews.size(); i++) {
//                params.build(fromContainers.get(i).toInt(), savedDiceViews.get(i).getDiceID(), toContainers.get(i).toInt(), param1.get(i), param2.get(i));
//            }
            if(params.isReady()) {
                if (bDebug) {
                    System.out.print("\nparams:");
                    if(params.getParams().size() == 0)
                        System.out.println("null");
                    else {
                        for (int p : params.getParams()) {
                            System.out.print(" \t" + p);
                        }
                        System.out.println();
                    }
                }
                bResult = gameViewController.useToolCard(usingCard.ID, params.getParams());

                if(ToolCardParamBuilder.getStep(usingCard.ID) > 1 && iProcess == 0) {
                    nextStage();

                } else {
                    // reset the state, user finished to use tool card
                    useToolCard(null);
                }
            } else {
                if (bDebug) {
                    System.out.println("required param quantity = " + ToolCardStrategy.parameterSizes[ usingCard.ID ][iProcess] + "\t inputed = " + params.getParams().size());
                    System.out.println(params.getNext());
                    gameViewController.updateMessage("Using tool card N." + usingCard.ID + ":\n" +
                            "Action the Dice in/to " + params.getNext() );
                }
            }
        }
        else {
            // insert dice normally when we are using any tool card
            if(fromContainers.equals(DRAFT)  &&  toContainers.get(0).equals(DiceContainerType.BOARD1)) {
                if(savedDiceViews.size() > 0)
                    bResult = gameViewController.insertDice(savedDiceViews.get(0).getDiceID(), param1.get(0), param2.get(0) );
            }
            // clear the parameters previously built
            clear();
        }
        return bResult;
    }


    public void nextStage() {
        iProcess++;
        useToolCard(usingCard);
    }


    public DiceBox getDiceBox() {
        return diceBox;
    }

    public void updateBox(List<Dice> diceList) {
        if(bDebug) {
            System.out.println("Show Dice Box");
        }
        if(!diceList.isEmpty()) {
            while (iProcess == 1 && hasUsingCard()) {
                diceBox = new DiceBox().display(
                        "TC" + usingCard.ID + ": " + usingCard.name,
                        diceList);
            }
        } else {
            useToolCard(null);
        }
    }
}
