package porprezhas.view.fx.gameScene.controller.component;

import javafx.animation.*;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.util.Duration;
import porprezhas.exceptions.diceMove.DiceNotFoundInDraftPoolException;
import porprezhas.model.dices.Dice;
import porprezhas.model.dices.DraftPool;
import porprezhas.view.fx.gameScene.controller.GameViewController;
import porprezhas.view.fx.gameScene.state.DiceContainer;
import porprezhas.view.fx.gameScene.state.DiceContainerType;

import java.util.List;
import java.util.Random;
import java.util.Scanner;

import static porprezhas.view.fx.gameScene.GuiSettings.*;
import static porprezhas.view.fx.gameScene.controller.component.GenericBoardView.getDragDiceString;

public class DraftPoolView implements DiceContainer {
    private StackPane stackPane;
//    private List<DiceView> diceList;

    private DiceContainerType idBoard = DiceContainerType.DRAFT;

    public DraftPoolView() {
        stackPane = new StackPane();
    }




    private GameViewController parentController;
    @Override
    public void setupSubController(GameViewController parentController) {
        this.parentController = parentController;
    }

    @Override
    public void activate() {
        stackPane.setDisable(false);
    }
    @Override
    public void disable() {
        stackPane.setDisable(true);
    }

    @Override
    public DiceContainerType getDiceContainer() {
        return idBoard;
    }


    // Remote Called Methods

    public void setup(Pane parent) {
        parent.getChildren().add( this.get() ); // add this.stackPane to parent.pane

        addBackground(parent);
        addResizeListener(parent);
        addDragListener();

        if(bShowFrames) {
            get().setBorder(new Border(new BorderStroke(Color.rgb(214, 54, 79),
                    BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        }
    }

    // create new dices
    private void roll(List<Dice> newDiceList) {
        if(bDebug)
            System.out.println("roll draft pool dices");
        stackPane.getChildren().clear();
        for (int i = 0; i < newDiceList.size(); i++) {
            addDice(newDiceList.get(i));
        }
    }


    // on new round, create new dices
    public void update(List<Dice> diceList) {
        roll(diceList);       // roll all dices
    }


    // refresh existing dices
    // add / remove different dices
    public void update(DraftPool draftPool) {
        if(bDebug)
            System.out.println("refresh draft pool dices");

        // check not present dices in view,
        // to add them later
        List<Dice> draftPoolDices = draftPool.diceList();

        for (int index = 0; index < stackPane.getChildren().size(); index++) {
            Node node = stackPane.getChildren().get(index);

            if(node instanceof DiceView) {
                DiceView diceView = (DiceView) node;

                try {
                    // check the present of dice in Model

                    boolean bTrue =
                            draftPoolDices.remove(
                                    DraftPool.getDiceByID( draftPoolDices, diceView.getDiceID() )   // this throws runtime exception
                    );    // remain dices will be added in View
                    if(!bTrue) {
                        System.err.println("Can not Remove dice: " + diceView);
                    }

                } catch (DiceNotFoundInDraftPoolException e) {
                    // Remove not found dice from View
                    stackPane.getChildren().remove(index);
                } }
        }

        // Add missed dices in View
        for (Dice dice : draftPoolDices) {
            addDice(dice);  // this does animations
        }
    }


/*
    // update a single dice during game
    public void update(long diceID, Dice newDice) {
        for (int indexInStackPane = 0; indexInStackPane < stackPane.getChildren().size(); indexInStackPane++) {
            Node node = stackPane.getChildren().get(indexInStackPane);
            if(node instanceof DiceView) {
                DiceView diceView = (DiceView) node;

                if(diceID == diceView.getDiceID()) {
                    addDice(newDice, diceView.getRow(), diceView.getColumn());
//                    stackPane.getChildren().set(indexInStackPane, diceView);
                }

            }
        }
    }
*/



    // Local Public Methods

    public StackPane get() {
        return stackPane;
    }

    public int getIndexByDiceID(long diceID) throws DiceNotFoundInDraftPoolException {
        boolean bFound = false;
        int i = 0;
        for (Node node : stackPane.getChildren()) {
            if(node instanceof DiceView) {
                DiceView diceView = (DiceView) node;
                if(diceView.getDiceID() == (diceID)) {
                    bFound = true;
                    break;
                }
                i++;
            }
        }
        if(bFound)
            return i;
        else
            throw new DiceNotFoundInDraftPoolException("Dice with id = " + diceID + " NOT FOUND in " + DiceContainerType.fromInt(idBoard.toInt()) );
    }


    // *****************************************
    // *** Private Methods - View Management ***

    private void addBackground(Pane parent) {
        Background background = new Background(
                new BackgroundFill(new ImagePattern(
                        new Image(pathToBackground + "draftPool0.png")),
                        CornerRadii.EMPTY, Insets.EMPTY));
        stackPane.setBackground(background);

        if(bShowGridLines) {
            parent.setBorder(new Border(new BorderStroke(Color.rgb(34,245,23),
                    BorderStrokeStyle.SOLID, new CornerRadii(5), new BorderWidths(1))));
            stackPane.setBorder(new Border(new BorderStroke(Color.rgb(34,245,23),
                    BorderStrokeStyle.SOLID, new CornerRadii(5), new BorderWidths(1))));
        }
    }


    private void addResizeListener(Pane parent) {
        stackPane.prefWidthProperty().bindBidirectional(parent.prefWidthProperty());

//        parent.minWidthProperty().bind( parent.heightProperty().multiply(1.1f) );
//        parent.prefWidthProperty().bind( parent.heightProperty().multiply(1.1f) );
    }

    private void addDragListener () {
        stackPane.setOnDragDropped(event -> {
            // remove and place a new dice there
            Dragboard dragboard = event.getDragboard();
            boolean bSuccess = false;
            if (dragboard.hasString()) {
                // read Dice date

                if (dragboard.hasString()) {
                    String draggedString = dragboard.getString();

                    Scanner scanner = new Scanner(draggedString);
                    scanner.useDelimiter(":|\\s");

                    scanner.findInLine("board=");
                    if (scanner.hasNextInt()) {
                        int idBoardFrom = scanner.nextInt();    // NOTE: watch out. We need have a space after the number.
                        if (bDebug) System.out.print("id board=" + idBoardFrom + ": \t");


                        DiceView draggedDiceView = DiceView.fromString(scanner.nextLine());
//                System.out.println(db.getString());

                        // calculate place position
                        int x = (int) (event.getX() - stackPane.getWidth() / 2);
                        int y = (int) (event.getY() - stackPane.getHeight() / 2);

                        if (idBoardFrom == this.idBoard.toInt()) {
                            for (int i = 0, indexDiceView = 0; i < stackPane.getChildren().size(); i++) {
                                Node node = stackPane.getChildren().get(i);
                                if (node instanceof DiceView) {
                                    DiceView guiDiceView = (DiceView) node;
//                                    if (draggedDiceView.getDiceID() == draggedDiceView.getDiceID() &&
//                                            guiDiceView.getColumn() == draggedDiceView.getColumn() &&
                                    if(guiDiceView.getDiceID() == draggedDiceView.getDiceID()) {
                                        System.out.println("Translate DiceView " + guiDiceView + " to x = " + x + " \ty = " + y);
                                        translateDice(guiDiceView, x, y);
                                        break;
                                    }
                                    indexDiceView++;
                                }
                            }
                        }
//                if (null != addDice(diceView.getDice(), x, y)) {
                        // TODO: success = ClientActionInterface.moveDice(idBoardFrom, diceView.getDiceID(), this.idBoard, row, col);
//                bSuccess = ClientActionSingleton.getClientAction().moveDice(idBoardFrom, diceView.getDiceID(), this.idBoard.toInt(), 0, 0);

//                    bSuccess = true;
//                }
                    }
                }
            }
            // let the source know whether the string was successfully transferred and used
            event.setDropCompleted(bSuccess);

            event.consume();
        });
        stackPane.setOnDragOver(event -> {
            // redo collide detection
            if (event.getGestureSource() != stackPane &&
                    event.getDragboard().hasString()) {
                // allow user translate drag
                event.acceptTransferModes(TransferMode.MOVE);
            }
            event.consume();
        });
    }




    // calculate x, y position from Polar coordinates
    private DiceView addDice(Dice dice, double width, double height, double radiant) {
        int x, y;
        double cx = 0;  // center of eclipse
        double cy = 0;
        x = (int) (cx + width/2 * Math.cos(radiant));     // equation of eclipse in polar representation
        y = (int) (cy + height/2 * Math.sin(radiant));

        return addDice(dice, x, y);
    }
/*
    private DiceView addDice(Dice dice, double ρ, double θ) {
        int x,y;
        x  = (int) (ρ * Math.sin(θ));
        y  = (int) (ρ * Math.cos(θ));

        return addDice(dice, x, y);
    }
*/


    // @Param x,y are the position of dice relative at center of draftPool
    //            can be negative or positive, but should be abs(x) < draftPool.width() && abs(y) < draftPool.height()
    private DiceView addDice(Dice dice, int x, int y) {
        if(bDebug) {
            System.out.println("DraftPool: add dice to x = " + x + " \ty = " + y);
        }
        DiceView diceView= new DiceView(dice, (int) x, (int) y, idBoard.toInt()); // Create a new Dice Image View
        stackPane.getChildren().add(diceView);


        diceView.fitWidthProperty().bind(stackPane.widthProperty().divide(8).multiply(DRAFT_DICE_ZOOM));
        diceView.fitHeightProperty().bind(stackPane.heightProperty().divide(8).multiply(DRAFT_DICE_ZOOM));
        diceView.setPreserveRatio(true);

        translateDice(diceView, x, y);

        diceView.setupSubController(parentController);
        diceView.addClickListener();
        diceView.addDragListener();
        return diceView;
    }


    private void translateDice(DiceView diceView, int x, int y) {
        diceView.setTranslateX(x);
        diceView.setTranslateY(y);

        diceView.toFront();
    }


/*    private int getIndexByDiceView(DiceView searchedDiceView) {
        boolean bFound = false;
        int i = 0;
        for (Node node : stackPane.getChildren()) {
            if(node instanceof DiceView) {
                DiceView diceView = (DiceView) node;
                if(diceView.equals(searchedDiceView)) {
                    bFound = true;
                    break;
                }
                i++;
            }
        }
        if(bFound)
            return i;
        return -1;  // or throw exception
    }
    */
/*
    public Dice chooseDice(int indexDice) {
        stackPane.getChildren().remove(indexDice);
        return diceView.getDice();
    }
*/


    // add extra dice during game
    private void addDice(Dice dice) {
        Random random = new Random();
        double cx = stackPane.getWidth() - 60;
        double cy = stackPane.getHeight() - 60;
        DiceView diceView = addDice( dice,
                (3- Math.abs(random.nextGaussian())%3)/3 * cx,
                (3-Math.abs(random.nextGaussian())%3)/3 * cy,
                random.nextDouble()*2*Math.PI);
/*
            DiceView diceView = addDice(newDiceList.get(i),
                    (int) ((random.nextGaussian() * stackPane.getWidth()) % (stackPane.getWidth()/(2 * Math.sqrt(2)))),
                    (int) ((random.nextGaussian() * stackPane.getHeight()) % (stackPane.getHeight()/(2 * Math.sqrt(2)))));
*/
        // set their position
        playDiceRollAnimation(diceView, diceView.getRow(), diceView.getColumn());
    }


    private void playDiceRollAnimation(DiceView diceView, int toX, int toY) {
        final double fadeDuration = 1600;
        final double translateDuration = 2000;
        final double rotateDuration = 2400;
        final double scaleDuration = 1600;
        final int maxDeviation = 1000;
        Random random = new Random();
        int randomDeviation = random.nextInt(maxDeviation);

//        System.out.println("to x = " + toX + " \tto y =" + toY);

        //Add dice opacity fading effect
        FadeTransition fadeTransition=new FadeTransition(Duration.millis(fadeDuration), diceView);
        fadeTransition.setFromValue(0.01f);
        fadeTransition.setToValue(1.0f);

        //Add dice position translating effect
        TranslateTransition translateTransition=new TranslateTransition(Duration.millis(translateDuration + randomDeviation), diceView);
        translateTransition.setFromX(random.nextGaussian() * 100 % 100);        // this will make dice move throw the center zone of the draft pool
        translateTransition.setFromY(random.nextGaussian() * 100 % 100);
        translateTransition.setToX(toX);
        translateTransition.setToY(toY);

        //Add dice image rotating effect
        RotateTransition rotateTransition =
                new RotateTransition(Duration.millis(rotateDuration + randomDeviation), diceView);
        rotateTransition.setByAngle(1.2f * (2400+randomDeviation) );     // rotation in degree (360°)

        //Add dice zooming out effect
        ScaleTransition scaleTransition =
                new ScaleTransition(Duration.millis(scaleDuration), diceView);
        scaleTransition.setFromX(2f);
        scaleTransition.setFromY(2f);
        scaleTransition.setToX(1f);
        scaleTransition.setToY(1f);


        //Paralleling execute these effects
        ParallelTransition parallelTransition=new ParallelTransition(fadeTransition,rotateTransition,
                translateTransition,scaleTransition);
        parallelTransition.setCycleCount(1);
        parallelTransition.play();
    }

}
