package porprezhas.view.fx;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import porprezhas.view.fx.gameScene.GuiSettings;
import sun.plugin2.main.server.ResultID;

import static porprezhas.view.fx.gameScene.GuiSettings.*;

/*
start rmiregistry -J-Djava.rmi.server.useCodebaseOnly=false

far partire nanohttp

Far partire test total(prima bisogna aggiungere in edit configurations la stringa nel VM options
 	-Djava.rmi.server.useCodebaseOnly=false -Djava.rmi.server.codebase=http://localhost:80/
 */
public class ClientApplication extends Application {

    private StageManager stageController;


    @Override
    public void start(Stage primaryStage) {
        // Create a new StageController
        stageController = new StageManager();

        // Give the Main Stage to the controller
        stageController.setPrimaryStage("primaryStage", primaryStage);

        // Load our stages from FMXL, every stage is a window
        stageController.loadStage(stageLoginID, stageLoginFile, StageStyle.TRANSPARENT);
        stageController.loadStage(stagePatternID, stagePatternFile, StageStyle.TRANSPARENT);
        stageController.loadStage(stageGameID, stageGameFile);  // default value = StageStyle.DECORATED
        stageController.loadStage(stageResultsID, stageResultsFile, StageStyle.TRANSPARENT);

        // display the first stage
        Platform.runLater(() -> {
            System.out.println("Show!\n\n");
            stageController.setStage(stageLoginID);
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
