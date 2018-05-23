package porprezhas.view.controller;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ViewTest extends Application {

    private Stage primaryStage;
    private AnchorPane rootLayout;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("tester");

        initRootLayout();
        initMainLogic();

//        gamePane = (StackPane) rootLayout.lookup("#gamePane");

//        showElementOverview();
    }

    private void initMainLogic (){
        primaryStage.setOnCloseRequest(event -> {
            event.consume();    // consume close_request, because we are going to handle it
            quitGame();
        });
    }

    private void quitGame() {

        Boolean bQuit = ConfirmBox.display("Title", "Are you sure to quit during a Game?");
        if(bQuit) {
            primaryStage.close();
        }
    }

    static class ConfirmBox {   // TODO: use this for request a Cards
        static boolean answer;
        public static boolean display(String title, String message) {
            Stage window = new Stage();
            window.initModality(Modality.APPLICATION_MODAL);
            window.setMinWidth(200);
            Label label = new Label();
            label.setText(message);

            Button yesButton = new Button("Yes");
            Button noButton = new Button("No");

            yesButton.setOnAction(event -> {
                answer = true;
                window.close();
            });
            noButton.setOnAction(event -> {
                answer = false;
                window.close();
            });

            VBox layout = new VBox(10);
            layout.getChildren().addAll(label, yesButton, noButton);
            layout.setAlignment(Pos.CENTER);
            layout.setPadding(new Insets(10, 12 ,16, 12));
            Scene scene  =new Scene(layout);
            window.setScene(scene);
            window.showAndWait();

            return answer;
        }
    }

    /**
     * Initializes the root layout.
     */
    public void initRootLayout() {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(ViewTest.class.getResource("/GameView.fxml"));
            rootLayout = loader.load();
//            rootLayout = loader.load(getClass().getResource("/GameView.fxml"));

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the main stage.
     * @return
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}


// TODO: every DICE fall around the center of draft poll
//       every dice move away from the colliding dices, in the rival direction
//       this should be done in 3 seconds, so make this move a bit away from center to speed up