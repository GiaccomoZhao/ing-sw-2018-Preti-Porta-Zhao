package porprezhas.view.controller;

import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.ImageCursor;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.scene.image.ImageView;
import porprezhas.model.dices.Dice;

import java.awt.image.BufferedImage;
import java.util.Random;

import static javafx.scene.effect.BlendMode.SRC_OVER;

public class GameViewController {
    private final String pathToDice = new String("dice/46x46/");
    private final String pathToCursor = new String("cursor/");

    // these will be initialized by the FXMLLoader when the load() method is called
    @FXML private StackPane gamePane;   // fx:id="gamePane"
    @FXML private HBox enemyPane1;
    @FXML private HBox enemyPane2;
    @FXML private HBox enemyPane3;
    @FXML private Label enemy1_name;
    @FXML private Label enemy2_name;
    @FXML private Label enemy3_name;
    @FXML private Label enemy1_icon;
    @FXML private Label enemy2_icon;
    @FXML private Label enemy3_icon;
    @FXML private GridPane enemy1_board;
    @FXML private GridPane enemy2_board;
    @FXML private GridPane enemy3_board;

    class EnemyView {
        public HBox pane;
        public Label name;
        public Label icon;
        public GridPane board;

        EnemyView(HBox pane, Label name, Label icon, GridPane board) {
            this.pane = pane;
            this.name = name;
            this.icon = icon;
            this.board = board;
        }
    }
    EnemyView[] enemy = new EnemyView[3];

    private ImageView[][] diceView;

    private ImageCursor cursorHand;
    private ImageCursor cursorHandDown;
    private ImageCursor cursorHandUp;

    public void initialize() {
        cursorHand = new ImageCursor(
                new Image(pathToCursor + "cursor_hand.png", 64.0, 64.0, true, true));
        cursorHandDown = new ImageCursor(
                new Image(pathToCursor + "cursor_hand_down.png") );
        cursorHandUp = new ImageCursor(
                new Image(pathToCursor + "cursor_hand_up.png") );

        gamePane.setCursor(cursorHand);

        enemy[0] = new EnemyView(enemyPane1, enemy1_name, enemy1_icon, enemy1_board);
        enemy[1] = new EnemyView(enemyPane2, enemy2_name, enemy2_icon, enemy2_board);
        enemy[2] = new EnemyView(enemyPane3, enemy3_name, enemy3_icon, enemy3_board);

        addResizeListener(gamePane);
        for (int i = 0; i < enemy.length; i++) {
            addResizeListener(enemy[i].board);
        }

        final int COLUMN = enemy[0].board.getColumnConstraints().size();
        final int ROW = enemy[0].board.getRowConstraints().size();
        diceView = new ImageView[COLUMN][ROW];

        for (int i = 0; i < enemy.length; i++) {
            for (int col = 0; col < COLUMN; col++) {
                for (int row = 0; row < ROW; row++) {
                    Random random = new Random();
                    if(random.nextBoolean())
                        diceView[col][row] = addDice(enemy[i].board, col, row, random.nextInt(6) +1, Dice.ColorDice.values()[random.nextInt(Dice.ColorDice.values().length -1)].name().toLowerCase().charAt(0));
                }
            }
            addBoardListener(enemy[i].board);
        }


        ChangeListener<Number> stageSizeListener = (observable, oldValue, newValue) -> {
//            System.out.println("Height: " + gamePane.getHeight() + " Width: " + gamePane.getWidth());
            // H: 600 -> 3 x 160 = 480 + 4 x 120/4
            // H: 300 : 160 - 222
            // H: 600 : 600 - 222
            // W: 300 : 260 - 360
            System.out.println(newValue.intValue() + "\t" + 260.0 / 500.0 * newValue.doubleValue());
            for (int i = 0; i < enemy.length; i++) {
                Pane pane = enemy[i].pane;
                pane.setPrefWidth( newValue.doubleValue() * 260.0 / 500.0);
                if(newValue.intValue() > 500) {
                    double width = pane.getHeight()* 260.0 / 500.0;
                    pane.setPadding(new Insets(
                            pane.getHeight() * 0.1 / 3.0,
                            width * 0.1,
                            0, 0 ));
                }
            }
        };
//        gamePane.widthProperty().addListener(stageSizeListener);
        gamePane.heightProperty().addListener(stageSizeListener);

    }

    private void gameResizeListener() {
        ;
    }

    private ImageView addDice(GridPane board, int col, int row, int num, char color){
        System.out.println(pathToDice + num + color + ".png");
        ImageView diceImage = new ImageView(new Image (pathToDice + num + color + ".png"));
//        diceImage.setFitHeight(32);
        diceImage.fitHeightProperty().bind(
                board.heightProperty().divide(4));
        diceImage.fitWidthProperty().bind(
                board.widthProperty().divide(5));
//                board.getRowConstraints().get(row).prefHeightProperty().multiply(1));
//        diceImage.fitWidthProperty().bind(
//                board.getColumnConstraints().get(col).prefWidthProperty().multiply(1));
//        diceImage.setPreserveRatio(true);
        diceImage.setSmooth(true);
        diceImage.setCache(true);

        // Action
        addDiceListener(diceImage);

        board.add(diceImage, col, row);
        return diceImage;
    }

    private void addDiceListener(ImageView imageView) {
        imageView.setOnDragDetected(event -> {
            Dragboard db = imageView.startDragAndDrop(TransferMode.ANY);

            /* Put a image on a dragboard */
            ClipboardContent content = new ClipboardContent();
            content.putImage(imageView.getImage());
            db.setContent(content);

            db.setDragView(imageView.getImage(), 10, 10);

            event.consume();
        });

        imageView.setOnDragDone(event -> {
            /* the drag and drop gesture ended */
            /* if the data was successfully moved, clear it */
            if (event.getTransferMode() == TransferMode.MOVE) {
                ;
            }
            event.consume();
        });
    }

    private void addBoardListener(GridPane board) {
        board.setOnDragOver(event -> {
            /* data is dragged over the target */
            /* accept it only if it is not dragged from the same node
             * and if it has a string data */
            if (event.getGestureSource() != board &&
                    event.getDragboard().hasString()) {
                /* allow for both copying and moving, whatever user chooses */
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }

            event.consume();
        });

        board.setOnDragEntered(event -> {
            /* the drag-and-drop gesture entered the target */
            /* show to the user that it is an actual gesture target */
            System.out.println(this + "Entered");
            if (event.getGestureSource() != board &&
                    event.getDragboard().hasString()) {
//                ((Node) event.getSource()).setCursor(cursorHand);
            }

            event.consume();
        });

        board.setOnDragExited(event -> {
            /* mouse moved away, remove the graphical cues */
//            board.setCursor(cursorHandUp);

            event.consume();
        });
    }


    private void addResizeListener(Pane pane) {
        ChangeListener<Number> stageSizeListener = (observable, oldValue, newValue) -> {
//                System.out.println("Height: " + pane.getHeight() + " Width: " + pane.getWidth());
                pane.setPrefWidth(5.0 / 4.0 * pane.getHeight());
//            System.out.println(5.0 / 4.0 * pane.getHeight() + "\th = " + pane.getHeight());
        };
//        gamePane.widthProperty().addListener(stageSizeListener);
        pane.heightProperty().addListener(stageSizeListener);

//        System.out.println("1" + pane);

//        img.fitWidthProperty().bind(stage.widthProperty());

//        pane.prefHeightProperty().bind(
//                pane.widthProperty());
//        pane.prefWidthProperty().bind(pane.prefHeightProperty().multiply(
//                pane.getPrefWidth() / pane.getPrefHeight()));

//        List<Node> items = gamePane.getChildren();
    }
}
