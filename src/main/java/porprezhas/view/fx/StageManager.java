package porprezhas.view.fx;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import porprezhas.view.fx.gameScene.GuiSettings;

import java.util.HashMap;

import static porprezhas.view.fx.gameScene.GuiSettings.bDebug;
import static porprezhas.view.fx.gameScene.GuiSettings.pathToFxml;

public class StageManager {

    // create a HashMap to save all Stages
    private HashMap<String, Stage> stages = new HashMap<String, Stage>();


    /**
     * Add the loaded Stage into the Map collection
     * to manage them later
     *
     * @param name  set Stage name
     * @param stage Stage's Object
     */
    public void addStage(String name, Stage stage) {
        stages.put(name, stage);
    }


    /**
     * get Stage object by name
     *
     * @param name Stage's name
     * @return Correspondent Stage's object
     */
    public Stage getStage(String name) {
        return stages.get(name);
    }


    /**
     * Save the main Stage
     * currently, we do not ues it
     *
     * @param primaryStageName name of the stage to set
     * @param primaryStage     Main stage's Object, Created by JavaFx in Start() method
     */
    public void setPrimaryStage(String primaryStageName, Stage primaryStage) {
        this.addStage(primaryStageName, primaryStage);
    }


    private void clipToCircle(Pane tempPane){
        System.out.println("Circle!!!!!!!!!!!!!!!!!");
        //Drawing a Circle
        Circle circle = new Circle();

        //Setting the properties of the circle
        double radius = ( tempPane.getPrefWidth() > tempPane.getPrefHeight() ? tempPane.getPrefHeight() : tempPane.getPrefWidth() ) /2;

        circle.setCenterX( tempPane.getPrefWidth() /2 );
        circle.setCenterY( tempPane.getPrefHeight() /2 );
        circle.setRadius(radius);

        tempPane.setClip(circle);
    }


    /**
     * Load the the .FXML file from FXML resource path
     * the loaded stage should have an independent window and
     * uses a Pane or a Sub-class of it as root layout
     *
     * @param stageName Name of Stage loaded from fxml resource
     * @param resources Name of .fxml file
     * @param styles    Optional parameter, used to initialize the style settings
     * @return is load successful
     */
    public boolean loadStage(String stageName, String resources, StageStyle... styles) {
        try {
            // Load .FXML resource
            FXMLLoader loader = new FXMLLoader(getClass().getResource(pathToFxml + resources + ".fxml"));
            Pane tempPane = loader.load();


            // Clip the Root Layout in a Circle for Login Stage
            if(stageName.equals(GuiSettings.stageLoginID)) {
                clipToCircle(tempPane);
            }


            // Get the ViewController from the FXML resource using Loader and
            // Set the this StageManager into the ViewController
            SceneController controlledStage = (SceneController) loader.getController();
            System.out.println(controlledStage);
            controlledStage.setStageManager(this, stageName);


            // Create a transparent scene
            Scene tempScene = new Scene(tempPane);
            tempScene.setFill(Color.TRANSPARENT);

            // Construct the correspondent Stage
            Stage tempStage = new Stage();
            tempStage.setScene(tempScene);

            // setup with styles
            for (StageStyle style : styles) {
                tempStage.initStyle(style);
            }

            // Save the loaded Stage in the HashMap
            this.addStage(stageName, tempStage);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Show Stage, but do not hide any Stage
     *
     * @param name Name of the Stage -or rather window- to show
     * @return nothing
     */
    public void setStage(String name) {
        this.getStage(name).show();
    }


    /**
     * Show and Hide the correspondent Stages
     *
     * @param show  Name of the window to show
     * @param close Name of the window to close
     * @return nothing
     */
    public void setStage(String show, String close) {
        getStage(close).close();
        setStage(show);
    }


    /**
     * Eliminate the loaded Stage from the Map
     *
     * @param name Name of the window of fxml file to remove
     * @return is delete successful
     */
    public boolean unloadStage(String name) {
        if (stages.remove(name) == null) {
            System.err.println("Not existing window, recheck the name, please");
            return false;
        } else {
            if(bDebug)
                System.out.println("The Window has been removed successfully!");
            return true;
        }
    }
}

