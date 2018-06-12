package porprezhas.view.fx.loginScene;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import porprezhas.view.fx.BackgroundMusicPlayer;

import java.io.File;


public class LoginView extends Application{

    private Stage primaryStage;
    private Parent rootLayout;
    private static BorderPane bpi;
    private double xOffset = 0;
    private double yOffset = 0;
    public static final String pathToMusicDirectory = "sound/music/loginMusic/";
    private String voidString ="";

    @FXML
    Button loginViewButton;
    @FXML
    TextField textFieldLoginView;
    @FXML
    Button joinViewButton;



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
            loader.setLocation(getClass().getResource("/LoginView.fxml"));
            if (loader == null)
                System.err.println(this + ": Error with loader.setLocation(" + getClass().getResource("/LoginView.fxml") + ")");
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

    @FXML
    public void loginDone(ActionEvent event) {
        if(textFieldLoginView.getText()!=null&&!(textFieldLoginView.getText().equals(voidString))){
            loginViewButton.setVisible(false);
            textFieldLoginView.setVisible(false);
            joinViewButton.setStyle(
                    "-fx-background-color: #FFF1C6; "+
                    "-fx-background-radius: 50em; " +
                            "-fx-min-width: 90px; " +
                            "-fx-min-height: 90px; " +
                            "-fx-max-width: 90px; " +
                            "-fx-max-height: 90px;"
            );
            joinViewButton.setVisible(true);

        }

    }

    public static void main(String[] args) {
        launch(args);
}

}
