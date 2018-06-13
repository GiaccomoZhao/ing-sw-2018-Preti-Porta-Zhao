package porprezhas.view.fx;

public interface SceneController {

    /**
     * This method will allow the injection of the Parent Controller - Stage Manager
     * Concrete Child Class should save them
     *
     * @param stageManager the Object that creates this Scene Controller and manages all Stages
     * @param stageName    name of the Stage that contains this Scene
     */
    void setStageManager(StageManager stageManager, String stageName);


    /**
     * Every Stage should be able to Switch to an other stage
     */
    void goToNextStage();

    /**
     *  Every Scene Controller should implement a Transition to show itself
     *  like: Fade-In Animation
     */
    void setCurrentStageTransition();

}
