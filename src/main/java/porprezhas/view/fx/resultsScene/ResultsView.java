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
import porprezhas.view.fx.gameScene.component.BackgroundMusicPlayer;

import java.io.File;

public class ResultsView extends Application {
    private Stage primaryStage;
    private Parent rootLayout;
    private static BorderPane bpi;
    private double xOffset = 0;
    private double yOffset = 0;
    public static final String pathToMusicDirectory = "sound/music/resultsMusic/";
    public static final String pathToVideoDirectory = "video/";

    @FXML
    private Button resultsViewCloseButton;


    @FXML
    public void handleCloseButtonAction(ActionEvent event) {
    Stage stage = (Stage)resultsViewCloseButton.getScene().getWindow();
        Platform.exit();
        System.exit(0);
    }



    @Override
    public void start(Stage primaryStage) throws Exception {

        String resourcePath = BackgroundMusicPlayer.class.getResource("/" ).getPath();
        resourcePath = resourcePath.substring(1, resourcePath.length());
        resourcePath = resourcePath.replaceAll("%20", " ");
        final File directory = new File(resourcePath + pathToMusicDirectory);
        String[] musicFiles = directory.list((dir1, name) -> name.endsWith(".mp3"));

        musicFiles[0] = "file:///" + (directory + "\\" + musicFiles[0]).replace("\\", "/").replaceAll(" ", "%20");
        MediaPlayer player = new MediaPlayer( new Media(musicFiles[0]) );

        player.play();


        this.primaryStage = primaryStage;
        initRootLayout();

    }

    public void initRootLayout() {
        try {
            // create a FXMLLoader and open fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/ResultsView.fxml"));
            if (loader == null)
                System.err.println(this + ": Error with loader.setLocation(" + getClass().getResource("/ResultsView.fxml") + ")");
            rootLayout = loader.load();
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.initStyle(StageStyle.UNDECORATED);

            rootLayout.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    xOffset = event.getSceneX();
                    yOffset = event.getSceneY();
                }
            });
            rootLayout.setOnMouseDragged(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    primaryStage.setX(event.getScreenX() - xOffset);
                    primaryStage.setY(event.getScreenY() - yOffset);
                }
            });




            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        launch(args);
    }

}
