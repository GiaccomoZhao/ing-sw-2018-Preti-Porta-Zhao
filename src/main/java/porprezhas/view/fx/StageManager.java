package porprezhas.view.fx;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import porprezhas.Useful;
import porprezhas.view.fx.gameScene.GuiSettings;

import java.util.HashMap;

import static porprezhas.view.fx.gameScene.GuiSettings.*;

public class StageManager {

    // create a HashMap to save all Stages
    private HashMap<String, Stage> stages = new HashMap<String, Stage>();
    private HashMap<String, SceneController> controllers = new HashMap<String, SceneController>();


    /**
     * Save the loaded Stage into a collection
     * to manage them later
     *s
     * @param name  set Stage name - as key
     * @param stage Stage's Object
     */
    public void addStage(String name, Stage stage) {
        stages.put(name, stage);
    }

    /**
     * Save the loaded Controller into a collection
     *
     * @param name       set Controller name - as key
     * @param controller Controller to save
     */
    public void addController(String name, SceneController controller) {
        controllers.put(name, controller);
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
     *  get the specified Scene Controller by name
     *
     * @param name  name of Stage that contain the scene with the controller
     * @return Correspondent Scene Controller
     */
    public SceneController getController(String name) {
        return controllers.get(name);
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


            // Get the ViewController from the FXML resource using Loader and
            // Set the this StageManager into the ViewController
            SceneController controlledStage = (SceneController) loader.getController();
            if(bDebug)
                System.out.println("\nScene Controller = " + controlledStage + " \n\t\t\thas been loaded successfully");
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

            // Save the loaded Stage and Controller in the HashMap
            this.addStage(stageName, tempStage);
            this.addController(stageName, controlledStage);

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

