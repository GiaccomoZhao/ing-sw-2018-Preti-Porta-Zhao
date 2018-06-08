package porprezhas.view.fx.gameScene.component;

import javafx.animation.*;
import javafx.application.Platform;
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
import porprezhas.Network.ClientActionSingleton;
import porprezhas.model.dices.Dice;
import porprezhas.view.fx.gameScene.GuiSettings;
import porprezhas.view.state.DiceContainer;

import java.util.List;
import java.util.Random;
import java.util.Scanner;

import static porprezhas.view.fx.gameScene.GuiSettings.*;
import static porprezhas.view.fx.gameScene.component.GenericBoardView.getDragDiceString;

public class DraftPoolView {
    private StackPane stackPane;
//    private List<DiceView> diceList;

    private DiceContainer idBoard = DiceContainer.DRAFT;    // Unknonw
    private int nDice;

    public DraftPoolView() {
        stackPane = new StackPane();
        nDice = 0;
    }

    public void setup(Pane parent) {
        addBackground(parent);
        addDragListener();

        if(bShowFrames || true) {
            get().setBorder(new Border(new BorderStroke(Color.rgb(214, 54, 79),
                    BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        }
    }


    public void update(List<Dice> diceList) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {

                // TODO: if state == ...
                reroll(diceList);                  // roll all dicess
//        refresh(diceList);               // reset all existing dices, and roll the not exist's
//        refresh(iModify, modifiedDice);  // Exchange one dice in draft pool

            }
        });
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
                                    if (draggedDiceView.getIndexDice() == draggedDiceView.getIndexDice() &&
                                            guiDiceView.getColumn() == draggedDiceView.getColumn() &&
                                            guiDiceView.getRow() == draggedDiceView.getRow()) {
                                        stackPane.getChildren().get(i).setTranslateX(x);
                                        stackPane.getChildren().get(i).setTranslateY(y);
                                        break;
                                    }
                                    indexDiceView++;
                                }
                            }
                        }
//                if (null != addDice(diceView.getDice(), x, y)) {
                        // TODO: success = ClientActionInterface.moveDice(idBoardFrom, diceView.getIndexDice(), this.idBoard, row, col);
//                bSuccess = ClientActionSingleton.getClientAction().moveDice(idBoardFrom, diceView.getIndexDice(), this.idBoard.toInt(), 0, 0);

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

    private void addBackground(Pane parent) {
        parent.getChildren().add( this.get() );
        stackPane.prefWidthProperty().bind(parent.widthProperty());
        stackPane.prefHeightProperty().bind(parent.heightProperty());
        Background background = new Background(
                new BackgroundFill(new ImagePattern(
                        new Image(pathToBackground + "draftPool0.png")),
                        CornerRadii.EMPTY, Insets.EMPTY));
        stackPane.setBackground(background);

        if(bShowGridLines) {
            stackPane.setBorder(new Border(new BorderStroke(Color.rgb(34,245,23),
                    BorderStrokeStyle.SOLID, new CornerRadii(5), new BorderWidths(1))));
        }
    }


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
        DiceView diceView= new DiceView(dice, (int) x, (int) y, nDice); // Create a new Dice Image View
        stackPane.getChildren().add(diceView);

        diceView.fitWidthProperty().bind(stackPane.widthProperty().divide(8).multiply(DRAFT_DICE_ZOOM));
        diceView.fitHeightProperty().bind(stackPane.heightProperty().divide(8).multiply(DRAFT_DICE_ZOOM));
        diceView.setPreserveRatio(true);

        translateDice(diceView, x, y);

        diceView.setOnDragDetected(event -> {
            Dragboard dragboard = diceView.startDragAndDrop(TransferMode.MOVE);

            // Put the Dice information on a dragBoard
            ClipboardContent content = new ClipboardContent();
            content.putString(getDragDiceString(this.idBoard.toInt(), diceView.toString()));
            dragboard.setContent(content);

            dragboard.setDragView(diceView.getImage(), diceView.getFitWidth()/2, diceView.getFitHeight()/2);

            event.consume();
        });
        diceView.setOnDragDone(event -> {
            // the drag and drop gesture ended
            // if the data was successfully moved, clear it
            if (event.getTransferMode() == TransferMode.MOVE) {
                int index = getIndexByDiceView(diceView);
                if(index >= 0) {
                    // TODO: call controller
                    // controller.chooseDice(index)
//                    chooseDice(index); //clear()
                    event.consume();
                }
            }
        });
        return diceView;
    }

    private void translateDice(DiceView diceView, int x, int y) {
        diceView.setTranslateX(x);
        diceView.setTranslateY(y);

        diceView.toFront();
    }

    public StackPane get() {
        return stackPane;
    }


    public int getIndexByDiceView(DiceView searchedDiceView) {
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
/*
    public Dice chooseDice(int indexDice) {
        stackPane.getChildren().remove(indexDice);
        return diceView.getDice();
    }
*/

    // add extra dice during game
    public void addDice(Dice dice) {
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
        playAnimation(diceView, diceView.getColumn(), diceView.getRow());
    }

    // new round
    public void reroll(List<Dice> newDiceList) {
        if(bDebug)
            System.out.println("reroll");
        stackPane.getChildren().clear();
        for (int i = 0; i < newDiceList.size(); i++) {
            addDice(newDiceList.get(i));
        }
    }


    // refresh during game
    public void refresh(List<Dice> newDiceList) {
        int indexOfDice = 0;
        int indexInStackPane = 0;
        for (Node node : stackPane.getChildren()) {
            if(node instanceof DiceView) {
                Dice newDice = newDiceList.get(indexOfDice);
                DiceView diceView = (DiceView) node;
                DiceView newDiceView = new DiceView(newDice, diceView.getColumn(), diceView.getRow(), indexOfDice);

                stackPane.getChildren().set(indexInStackPane, newDiceView);

                indexOfDice++;
                if(newDiceList.size() == indexOfDice)
                    return;
            }
            indexInStackPane ++;
        }
    }
    public void refresh(int indexDice, Dice newDice) {
        for (int indexInStackPane = 0; indexInStackPane < stackPane.getChildren().size(); indexInStackPane++) {
            Node node = stackPane.getChildren().get(indexInStackPane);
            if(node instanceof DiceView) {
                DiceView diceView = (DiceView) node;

                if(indexDice == diceView.getIndexDice()) {
                    stackPane.getChildren().set(indexDice, diceView);
                }

            }
        }
    }


    private void playAnimation (DiceView diceView, int toX, int toY) {
        Random random = new Random();
        int ranndomDuration = random.nextInt(1000);

//        System.out.println("to x = " + toX + " \tto y =" + toY);
        //定义矩形的淡入淡出效果
        FadeTransition fadeTransition=new FadeTransition(Duration.millis(1600), diceView);
        fadeTransition.setFromValue(0.01f);
        fadeTransition.setToValue(1.0f);
//        fadeTransition.setCycleCount(1);
//        fadeTransition.setAutoReverse(true);
        //fadeTransition.play();

        //定义矩形的平移效果
        TranslateTransition translateTransition=new TranslateTransition(Duration.millis(2000 + ranndomDuration), diceView);
        translateTransition.setFromX(random.nextGaussian() * 100 % 100);
        translateTransition.setFromY(random.nextGaussian() * 100 % 100);
        translateTransition.setToX(toX);
        translateTransition.setToY(toY);
//        translateTransition.setCycleCount(1);
//        translateTransition.setAutoReverse(true);
        //translateTransition.play();

        //定义矩形旋转效果
        RotateTransition rotateTransition =
                new RotateTransition(Duration.millis(2400 + ranndomDuration), diceView);
        rotateTransition.setByAngle(1.2f * (2400+ranndomDuration) );//旋转度数
//        rotateTransition.setCycleCount(1);
//        rotateTransition.setAutoReverse(true);
        //rotateTransition.play();

        //矩形的缩放效果
        ScaleTransition scaleTransition =
                new ScaleTransition(Duration.millis(1600), diceView);
        scaleTransition.setFromX(2f);
        scaleTransition.setFromY(2f);
        scaleTransition.setToX(1f);
        scaleTransition.setToY(1f);
//        scaleTransition.setCycleCount(1);
//        scaleTransition.setAutoReverse(true);
        //scaleTransition.play();

        //并行执行动画
        ParallelTransition parallelTransition=new ParallelTransition(fadeTransition,rotateTransition,
                translateTransition,scaleTransition);
        parallelTransition.setCycleCount(1);
        parallelTransition.play();
    }

}
