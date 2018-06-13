package porprezhas.view.fx.resultsScene;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;
import porprezhas.view.fx.BackgroundMusicPlayer;
import porprezhas.view.fx.SceneController;
import porprezhas.view.fx.StageManager;
import porprezhas.view.fx.gameScene.GuiSettings;

import java.io.File;

import static porprezhas.view.fx.gameScene.GuiSettings.*;

public class ResultViewController implements SceneController {

    @FXML
    private Button resultsViewCloseButton;
    @FXML
    private AnchorPane resultsView;
    private Pane rootLayout;

    // Parent Controller
    StageManager stageManager;
    String stageName;

    // Music controller
    public final String pathToMusicDirectory = pathToResultMusic;

    private double xOffset = 0;
    private double yOffset = 0;
//    public static final String pathToVideoDirectory = "video/";


    // this will be called by JavaFX
    public void initialize() {
        if(bDebug)
            System.out.println("Initializing ResultView");

        // assign the rootLayout the top most parent pane, now that it is initialized
        rootLayout = resultsView;

        // this window is borderless and
        // we do not want it be resized
//        stageManager.getStage(stageName).setResizable(false);

        // Add Move Window by dragging listener
        rootLayout.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                // save the Mouse's actual position on the scene (inside the window,
                // with the origin(0,0) in the top left anchor below the title bar)
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            }
        });
        rootLayout.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                // calculate the differences between x,y offsets,
                // to determinate the new position of window
                stageManager.getStage(stageName).setX(event.getScreenX() - xOffset);
                stageManager.getStage(stageName).setY(event.getScreenY() - yOffset);
            }
        });

        // add Ending music
        BackgroundMusicPlayer.playRandomMusic(pathToMusicDirectory);

        // Fade In current Stage
//        currentStageTransition();
    }


    // Stage management
    @Override
    public void setStageManager(StageManager stageManager, String stageName) {
        // Change Stages
        this.stageManager = stageManager;
        this.stageName = stageName;
    }

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

    private void currentStageTransition() {
        // Create a Timeline to animate the transition between stages
        Timeline timeline = new Timeline();

        // Add the transition animation
        // Using Opacity Fading
        KeyFrame key = new KeyFrame(Duration.millis(STAGE_FADE_IN),
                new KeyValue(stageManager.getStage(stageName).
                        getScene().getRoot().opacityProperty(), 0));
        timeline.getKeyFrames().add(key);

        // Change Stage
        timeline.setOnFinished((actionEvent) -> {
            ;
        });
        timeline.play();
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
