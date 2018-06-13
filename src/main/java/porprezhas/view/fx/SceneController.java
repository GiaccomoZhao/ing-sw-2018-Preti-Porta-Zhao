package porprezhas.view.fx;

public interface SceneController {
    // This method will allow the injection of the Parent Controller - Stage Manager
    void setStageManager(StageManager stageManager, String stageName);


    // every stage should be able to Switch the stage
    void goToNextStage();

}
