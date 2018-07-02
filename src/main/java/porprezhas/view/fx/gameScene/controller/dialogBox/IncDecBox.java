package porprezhas.view.fx.gameScene.controller.dialogBox;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class IncDecBox {
    private Boolean answer;

    public Boolean display() {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setWidth(300);
        window.setHeight(100);
        window.setTitle("Tool Card N.1");
        String message = "Tell me what do you want";

        Label label = new Label();
        label.setText(message);
        label.setWrapText(true);

        Button yesButton = new Button("Increment");
        Button noButton = new Button("Decrement");

        yesButton.setOnAction(event -> {
            answer = true;
            window.close();
        });
        noButton.setOnAction(event -> {
            answer = false;
            window.close();
        });

        HBox bottomLayout = new HBox(10);
        bottomLayout.getChildren().addAll(yesButton, noButton);
        bottomLayout.setAlignment(Pos.CENTER);

        VBox layout = new VBox(12);
        layout.getChildren().addAll(label, bottomLayout);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(10, 12, 16, 12));

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();

        return answer;
    }
}
