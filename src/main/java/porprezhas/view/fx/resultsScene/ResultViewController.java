package porprezhas.view.fx.resultsScene;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;
import porprezhas.view.fx.*;
import porprezhas.view.fx.gameScene.GuiSettings;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import static porprezhas.view.fx.gameScene.GuiSettings.*;

public class ResultViewController implements Initializable, SceneController, MovebleWindowInterface {

    @FXML
    private Button resultsViewCloseButton;
    @FXML
    private AnchorPane resultsView;
    private Pane rootLayout;

    // Parent Controller
    StageManager stageManager;
    String stageName;

    private MovebleWindowInterface movable;

    // Music controller
    public final String pathToMusicDirectory = pathToResultMusic;
//    public static final String pathToVideoDirectory = "video/";




    // Stage management
    @Override
    public void setStageManager(StageManager stageManager, String stageName) {
        if(bDebug)
            System.out.println("Set " + stageManager + "\n\t\t to " + stageName + "\n\t\t in " + this);
        this.stageManager = stageManager;
        this.stageName = stageName;
    }


    @Override
    public void goToNextStage() {
        // Create a Timeline to animate the transition between stages
        Timeline timeline = new Timeline();
        KeyFrame key = new KeyFrame(Duration.millis(STAGE_FADE_OUT),
                new KeyValue(stageManager.getStage(stageName).getScene().getRoot().
                        opacityProperty(), 0));
        timeline.getKeyFrames().add(key);
        timeline.setOnFinished((ae) -> {

            // Close this Stage
            stageManager.setStage(GuiSettings.stageLoginID, this.stageName);
        });

        timeline.play();

    }

    @Override
    public void setCurrentStageTransition() {
        // Create a Timeline to animate the transition between stages
        Timeline timeline = new Timeline();

        // Add the transition animation
        // Using Opacity Fading
        KeyFrame key = new KeyFrame(Duration.millis(STAGE_FADE_IN),
                new KeyValue(stageManager.getStage(stageName).
                        getScene().getRoot().opacityProperty(), 1));
        timeline.getKeyFrames().add(key);

        // Change Stage
        timeline.setOnFinished((actionEvent) -> {
            ;
        });

        stageManager.getStage(stageName).setOnShowing(event -> {
            timeline.play();
        });
    }




    @Override
    public void setupWindowMoveListener(Pane rootLayout, Stage stage) {
        movable.setupWindowMoveListener(rootLayout, stage);
    }

    @Override
    public void addWindowMoveListener() {
        movable.addWindowMoveListener();
    }





    // this will be called by JavaFX
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if(bDebug)
            System.out.println("Initializing ResultView");

        // assign the rootLayout the top most parent pane, now that it is initialized
        rootLayout = resultsView;

        Platform.runLater(() -> {
            // Add Move Window Listener
            movable = new MovebleWindow();
            setupWindowMoveListener(rootLayout, stageManager.getStage(this.stageName));
            addWindowMoveListener();

            // Add Window Appear Animation
            setCurrentStageTransition();
        });

        // this window is borderless and
        // we do not want it be resized
//        stageManager.getStage(stageName).setResizable(false);


        // add Ending music
        BackgroundMusicPlayer.playRandomMusic(pathToMusicDirectory);

    }








    // FXML methods

    @FXML
    public void handleCloseButtonAction(ActionEvent event) {
        goToNextStage();

/*        Stage stage = (Stage)resultsViewCloseButton.getScene().getWindow();
        Platform.exit();
        System.exit(0);
*/
    }




}
