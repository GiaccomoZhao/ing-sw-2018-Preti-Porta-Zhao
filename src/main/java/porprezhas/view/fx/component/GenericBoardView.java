package porprezhas.view.fx.component;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import porprezhas.model.dices.Dice;
import porprezhas.view.fx.GuiSettings;

public abstract class GenericBoardView {
    private final int COLUMN;   // default value
    private final int ROW;
    private double DICE_ZOOM = GuiSettings.BOARD_DICE_ZOOM;    // 1.2 would be already too big

    private GridPane board; // since we can not import this in fxml file, we can not extend GridPane
    private Dice[][] diceMatrix;

    private boolean bDragging = false;


    // create a BoardView by passing a configured(may in FXML) GridPane
    public GenericBoardView(GridPane board) {
        this.COLUMN = 5;
        this.ROW = 4;
        if(board == null)
            System.err.println(this + " \tboard=" + board);
        this.board = board;
        this.diceMatrix = new Dice[COLUMN][ROW];
        this.addBoardDragListener();
    }

    // Create a new BoardView by ... for RoundTrack, can be used for different
    public GenericBoardView(int COLUMN, int ROW) {
        this.COLUMN = COLUMN;
        this.ROW = ROW;
        this.board = new GridPane();
        this.diceMatrix = new Dice[COLUMN][ROW];
        this.addBoardDragListener();

        for (int i = 0; i < COLUMN; i++) {
            ColumnConstraints columnConstraints = new ColumnConstraints();
            columnConstraints.setPercentWidth(100.0 / COLUMN);
            board.getColumnConstraints().add(columnConstraints);
        }
        for (int i = 0; i < ROW; i++) {
            RowConstraints rowConstraints = new RowConstraints();
            rowConstraints.setPercentHeight(100.0 / ROW);
            board.getRowConstraints().add(rowConstraints);
        }
    }


    // **** Abstract methods ****
    abstract boolean deleteDice(DiceView diceView);


    // **** Getter methods ****
    public int getCOLUMN() {
        return COLUMN;
    }

    public int getROW() {
        return ROW;
    }
    public boolean isbDragging() {
        return bDragging;
    }

    public Dice getDiceMatrix(int col, int row) {
        return diceMatrix[col][row];
    }

    public GridPane getBoard() {
        return board;
    }

    public double getDICE_ZOOM() {
        return DICE_ZOOM;
    }

    // **** Setter methods ****
    public void setDiceZoom(double DICE_ZOOM) {
        this.DICE_ZOOM = DICE_ZOOM;
    }

    public void setDiceMatrix(Dice value, int col, int row) {
        this.diceMatrix[col][row] = value;
    }

    public DiceView getDiceView(int column, int row) {
        DiceView diceView = null;
        ObservableList<Node> childrens = board.getChildren();

        for (Node node : childrens) {
            if(board.getRowIndex(node) == row && board.getColumnIndex(node) == column) {
                diceView = (DiceView) node;
                break;
            }
        }
        return diceView;
    }


    // Add Dice to Board
    // return a reference to the added Dice
    // requires col < 5 && col > 0 &&
    //          row < 4 && row > 0
    public DiceView addDice(Dice dice, int col, int row) { //int num, char color){
        DiceView diceImage = new DiceView(dice, col, row);
        diceImage.setSmooth(true);
        diceImage.setCache(true);
//        diceImage.setFitHeight(32);
        diceImage.fitHeightProperty().bind(
                getBoard().heightProperty().divide(getBoard().getRowConstraints().size()).multiply(getDICE_ZOOM()));
        diceImage.fitWidthProperty().bind(
                getBoard().widthProperty().divide(getBoard().getColumnConstraints().size()).multiply(getDICE_ZOOM()));
//                board.getRowConstraints().get(row).prefHeightProperty().multiply(1));
//        diceImage.fitWidthProperty().bind(
//                board.getColumnConstraints().get(col).prefWidthProperty().multiply(1));
//        diceImage.setPreserveRatio(true);

        // Action
        addDiceDragListener(diceImage);

        getBoard().add(diceImage, col, row);
        setDiceMatrix(dice, col, row);
        return diceImage;
    }


    // Refresh
    // @ensure board.get(col, row).dice() == diceMatrix[col][row]
    public void update() {
         board.getChildren().clear();
/*        for (int i = 0; i < board.getChildren().size(); i++) {
            Node node = board.getChildren().get(i);
            if(node instanceof DiceView)
                board.getChildren().remove(i);
        }
*/
        // reInsert dices
        for (int col = 0; col < COLUMN; col++) {
            for (int row = 0; row < ROW; row++) {
                if(null != diceMatrix[col][row]) {
                    addDice(diceMatrix[col][row], col, row);
                }
            }
        }
    }


    // **** Drag Listeners ****
    // we have dice drag listener here instead of DiceView because it need to call method of this class
    private void addDiceDragListener(DiceView diceView) {
        diceView.setOnDragDetected(event -> {
            Dragboard dragboard = diceView.startDragAndDrop(TransferMode.MOVE);

            /* Put the image information on a dragBoard */
            ClipboardContent content = new ClipboardContent();
            content.putString(diceView.toString());
            dragboard.setContent(content);

            dragboard.setDragView(diceView.getImage(), diceView.getFitWidth()/2, diceView.getFitHeight()/2);
//            diceView.setCursor(Cursor.NONE);

            event.consume();
            bDragging = true;
        });

        diceView.setOnDragDone(event -> {
            /* the drag and drop gesture ended */
            /* if the data was successfully moved, clear it */
            if (event.getTransferMode() == TransferMode.MOVE) {
                deleteDice(diceView);//clear();
//                System.out.println(event.getTarget());    // these 2 are the same!!!
//                System.out.println(event.getSource());
            }
            event.consume();
            bDragging = false;
        });
    }

    protected void addBoardDragListener() {
        board.setOnDragDropped(event -> {
            // data dropped
            // if there is a string data on dragBoard, read it and use it
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasString()) {
                // read Dice date
                DiceView diceView = new DiceView();
                diceView.fromString(db.getString());
//                System.out.println(db.getString());

                // calculate place position
                int nCol = board.getColumnConstraints().size();
                int nRow = board.getRowConstraints().size();
                int col = (int) (event.getX() / (board.getWidth() / nCol));
                int row = (int) (event.getY() / (board.getHeight() / nRow));
                if(col >= nCol)
                    col = nCol - 1;
                if(row >= nRow)
                    row = nRow - 1;

                // place down
                if (null != addDice(diceView.getDice(), col, row)) {
                    success = true;
                }
//                System.out.println(((GridPane)event.getSource()).getLayoutY() + " \t" + ((GridPane)event.getTarget()).getLayoutY());
//                System.out.println("Dropped to " + col + "\t" + row);
            }
            /* let the source know whether the string was successfully
             * transferred and used */
            event.setDropCompleted(success);

            event.consume();
        });
        board.setOnDragOver(event -> {
            // data is dragged over the target
            // accept it only if it is not dragged from the same node
            // and if it has a string data
            if (event.getGestureSource() != board &&
                    event.getDragboard().hasString()) {
                /* allow for both copying and moving, whatever user chooses */
                event.acceptTransferModes(TransferMode.MOVE);

                // change cursor image
//                ((Node) event.getSource()).setCursor(cursorHandDown);

/*                // calculate place position
                int nCol = board.getColumnConstraints().size();
                int nRow = board.getRowConstraints().size();
                int col = (int) (event.getX() / (board.getWidth() / nCol));
                int row = (int) (event.getY() / (board.getHeight() / nRow));
                if(col >= nCol)
                    col = nCol - 1;
                if(row >= nRow)
                    row = nRow - 1;

                System.out.println("Dropping to col = " + col + "\trow = " + row);
*/            }

            event.consume();
        });

        board.setOnDragEntered(event -> {
            /* the drag-and-drop gesture entered the target */
            /* show to the user that it is an actual gesture target */
//            System.out.println(board + "Entered");
            if (event.getGestureSource() != board &&
                    event.getDragboard().hasString()) {
                // change cursor image
//                ((Node) event.getSource()).setCursor(cursorHandDown);
            }

            event.consume();
        });

        board.setOnDragExited(event -> {
            /* mouse moved away, remove the graphical cues */
            // reset cursor image
//            board.setCursor(cursorHand);

            event.consume();
        });
    }

}