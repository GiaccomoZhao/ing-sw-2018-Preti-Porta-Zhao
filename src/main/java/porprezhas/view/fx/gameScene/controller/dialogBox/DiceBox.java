package porprezhas.view.fx.gameScene.controller.dialogBox;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import porprezhas.model.dices.Dice;
import porprezhas.view.fx.gameScene.controller.component.DiceView;
import porprezhas.view.fx.gameScene.state.DiceContainerType;

import java.util.List;

public class DiceBox extends Stage{
    HBox layout;

    public DiceBox display(String title, List<Dice> diceList) {
//        Stage window = new Stage();
        Stage window = this;

        window.initModality(Modality.WINDOW_MODAL);
//        window.setWidth(200);
//        window.setHeight(50);
        window.setTitle(title);
        window.initStyle(StageStyle.UTILITY);

        layout = new HBox();
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(10));

        for (int index = 0; index < diceList.size(); index++) {
            layout.getChildren().add(
                    new DiceView(diceList.get(index), index, 0, DiceContainerType.BOX.toInt()).addDragListener() );
        }

        Scene scene = new Scene(layout);
        window.setScene(scene);

        window.toFront();
        window.setAlwaysOnTop(true);
        window.showAndWait();
        return this;
    }

    public DiceBox display(String title) {
        Stage window = new Stage();
        window.initModality(Modality.WINDOW_MODAL);
        window.setWidth(200);
        window.setHeight(50);
        window.setTitle(title);
        window.initStyle(StageStyle.TRANSPARENT);

        layout = new HBox();
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(10));

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
        return this;
    }

    public void setup(List<Dice> diceList) {
        diceList.forEach(dice ->
                layout.getChildren().add(
                        new DiceView(dice, 0,0, DiceContainerType.BOX.toInt())
                                .addDragListener())
        );
    }

    public void exit() {
//        Platform.setImplicitExit(true);
//        Platform.exit();
        this.hide();    // the same of .close()
    }
}
