package porprezhas.view.fx.resultsScene;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import porprezhas.view.fx.BackgroundMusicPlayer;

import java.io.File;

public class ResultsView extends Application {
    private Stage primaryStage;



    @Override
    public void start(Stage primaryStage) throws Exception {

        this.primaryStage = primaryStage;
        initRootLayout();

    }

    public void initRootLayout() {

            primaryStage.show();

    }


    public static void main(String[] args) {
        launch(args);
    }

}
