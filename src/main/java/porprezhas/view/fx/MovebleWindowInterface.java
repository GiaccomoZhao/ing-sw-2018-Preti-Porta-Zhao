package porprezhas.view.fx;

import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public interface MovebleWindowInterface {
    void setupWindowMoveListener(Pane rootLayout, Stage stage);
    void addWindowMoveListener();
}
