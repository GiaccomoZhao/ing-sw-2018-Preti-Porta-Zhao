package porprezhas.view.fx;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class MovebleWindow implements MovebleWindowInterface{
    private Pane rootLayout;
    private Stage stage;

    private double xOffset = 0;
    private double yOffset = 0;

    public void setupWindowMoveListener(Pane rootLayout, Stage stage) {
        this.rootLayout = rootLayout;
        this.stage = stage;
    }


    public void addWindowMoveListener() {

        // Save the coordinate
        rootLayout.setOnMousePressed(event -> {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
        });

        rootLayout.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });
    }
}
