package porprezhas.view.fx;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.util.Duration;
import porprezhas.view.fx.gameScene.GuiSettings;

import static porprezhas.view.fx.gameScene.GuiSettings.STAGE_FADE_IN;
import static porprezhas.view.fx.gameScene.GuiSettings.STAGE_FADE_OUT;
import static porprezhas.view.fx.gameScene.GuiSettings.bDebug;

public class DecoratedScene implements SceneController {
    protected SceneController controller;

    StageManager stageManager;
    String stageName;

    public DecoratedScene(SceneController controller) {
        this.controller = controller;
    }

    @Override
    public void setStageManager(StageManager stageManager, String stageName) {
        this.stageManager = stageManager;
        this.stageName = stageName;
    }

    @Override
    public void goToNextStage() {
        if(bDebug)
            System.out.println("Goto next");
        // Create a Timeline to animate the transition between stages
        Timeline timeline = new Timeline();
        KeyFrame key = new KeyFrame(Duration.millis(STAGE_FADE_OUT),
                new KeyValue(stageManager.getStage(stageName).getScene().getRoot().
                        opacityProperty(), 0));
        timeline.getKeyFrames().add(key);
        timeline.setOnFinished((ae) -> {
            // Switch the Stage
            stageManager.setStage(GuiSettings.stagePatternID, this.stageName);
        });

        timeline.play();
    }

    @Override
    public void setCurrentStageTransition() {
    }
}
