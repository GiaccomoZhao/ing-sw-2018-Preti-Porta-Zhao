package porprezhas.view.fx.component;

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
import porprezhas.model.dices.Dice;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import static porprezhas.view.fx.GuiSettings.*;

public class DraftPoolView {
    private StackPane stackPane;
    private List<DiceView> diceList;

    public DraftPoolView() {
        stackPane = new StackPane();
        diceList = new LinkedList<>();
    }

    public void setup(Pane parent) {
        addBackground(parent);
        addDragListener();

        if(bShowFrames || true) {
            get().setBorder(new Border(new BorderStroke(Color.rgb(214, 54, 79),
                    BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        }
    }

    private void addDragListener () {
        stackPane.setOnDragOver(event -> {
            // redo collide detection
            if (event.getGestureSource() != stackPane &&
                    event.getDragboard().hasString()) {
                // allow user translate drag
                event.acceptTransferModes(TransferMode.MOVE);
            }
            event.consume();
        });
        stackPane.setOnDragDropped(event -> {
            // remove and place a new dice there
            Dragboard dragboard = event.getDragboard();
            boolean bSuccess = false;
            if (dragboard.hasString()) {
                // read Dice date
                DiceView diceView = new DiceView();
                diceView.fromString(dragboard.getString());
//                System.out.println(dragboard.getString());

                // calculate place position
                int x = (int) (event.getX() - stackPane.getWidth()/2);
                int y = (int) (event.getY() - stackPane.getHeight()/2);

                if (null != addDice(diceView.getDice(), x, y)) {
                    bSuccess = true;
                }
                if(bDebug) {
                    System.out.println("Draft pool: \"successful dragged in\" = " + bSuccess);
                }
            }
            // let the source know whether the string was successfully transferred and used
            event.setDropCompleted(bSuccess);

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
        DiceView diceView= new DiceView(dice, (int) x, (int) y); // Create a new Dice Image View
        diceList.add(diceView);     // save the reference to the added dices
        stackPane.getChildren().add(diceView);

        diceView.fitWidthProperty().bind(stackPane.widthProperty().divide(8).multiply(DRAFT_DICE_ZOOM));
        diceView.fitHeightProperty().bind(stackPane.heightProperty().divide(8).multiply(DRAFT_DICE_ZOOM));
        diceView.setPreserveRatio(true);

        diceView.setTranslateX(x);
        diceView.setTranslateY(y);

        diceView.toFront();

        diceView.setOnDragDetected(event -> {
            Dragboard dragboard = diceView.startDragAndDrop(TransferMode.MOVE);

            // Put the Dice information on a dragBoard
            ClipboardContent content = new ClipboardContent();
            content.putString(diceView.toString());
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
                    chooseDice(index); //clear()
                    event.consume();
                }
            }
        });
        return diceView;
    }

    public StackPane get() {
        return stackPane;
    }

    public List<DiceView> getDiceList() {
        return diceList;
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

    public Dice chooseDice(int indexDice) {
        DiceView diceView = diceList.remove(indexDice);
        stackPane.getChildren().remove(indexDice);
        return diceView.getDice();
    }

    public void reroll(List<Dice> newDiceList) {
        stackPane.getChildren().clear();
        Random random = new Random();
        for (int i = 0; i < newDiceList.size(); i++) {
            double cx = stackPane.getWidth() - 60;
            double cy = stackPane.getHeight() - 60;
            DiceView diceView = addDice(newDiceList.get(i),
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
