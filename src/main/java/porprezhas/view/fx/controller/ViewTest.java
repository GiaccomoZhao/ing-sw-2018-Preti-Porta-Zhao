package porprezhas.view.fx.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import jdk.internal.dynalink.linker.GuardedInvocation;
import porprezhas.model.Player;
import porprezhas.model.dices.Pattern;

public class ViewTest extends Application {
    public static class GUIConstants {
        public static final int ICON_QUANTITY = 145;   // index from 0 to 144
    }

    private Stage primaryStage;
    private AnchorPane rootLayout;


    List<Player> players;
    List<GameViewController.PlayerInfo> playersInfo;

    GameViewController gameViewController;

    public ViewTest() {
        players = new ArrayList<>();
        players.add( new Player("me"));
        players.add( new Player("p1"));
        players.add( new Player("p2"));
        players.add( new Player("p3"));

        this.playersInfo = new ArrayList<>();
        Random random = new Random();
        for (Player player : players) {
            playersInfo.add(new GameViewController.PlayerInfo(
                    player.getName(),
                    random.nextInt(GUIConstants.ICON_QUANTITY),
                    Pattern.TypePattern.values()[1]));
        }
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("tester");

        initRootLayout();
        initMainLogic();

//        gamePane = (StackPane) rootLayout.lookup("#gamePane");

//        showElementOverview();
    }

    private void initMainLogic (){
        primaryStage.setOnCloseRequest(event -> {
            event.consume();    // consume close_request, because we are going to handle it
            quitGame();
        });

        primaryStage.maximizedProperty().addListener((ov, t, t1) -> {
            gameViewController.updateSize();
//            if(bDebug)
//                System.out.println("maximized:" + t1.booleanValue());
        });

    }

    private void quitGame() {

        Boolean bQuit = ConfirmBox.display("Title", "Are you sure to quit during a Game?");
        if(bQuit) {
            primaryStage.close();
        }
    }

    static class ConfirmBox {   // TODO: use this for request a Cards
        static boolean answer;
        public static boolean display(String title, String message) {
            Stage window = new Stage();
            window.initModality(Modality.APPLICATION_MODAL);
            window.setMinWidth(200);
            Label label = new Label();
            label.setText(message);

            Button yesButton = new Button("Yes");
            Button noButton = new Button("No");

            yesButton.setOnAction(event -> {
                answer = true;
                window.close();
            });
            noButton.setOnAction(event -> {
                answer = false;
                window.close();
            });

            VBox layout = new VBox(10);
            layout.getChildren().addAll(label, yesButton, noButton);
            layout.setAlignment(Pos.CENTER);
            layout.setPadding(new Insets(10, 12 ,16, 12));
            Scene scene  =new Scene(layout);
            window.setScene(scene);
            window.showAndWait();

            return answer;
        }
    }

    /**
     * Initializes the root layout.
     */
    public void initRootLayout() {
        try {
            // create a FXMLLoader and open fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/GameView.fxml"));
            if(loader == null)
                System.err.println(this + ": Error with loader.setLocation(" + getClass().getResource("/GameView.fxml") + ")");
            // Create a controller instance, passing the information about players
            gameViewController = new GameViewController(playersInfo);
            // Set it in the FXMLLoader
            loader.setController(gameViewController);

            // Load root layout from fxml file.
            rootLayout = loader.load();     // NOTE: If you get ERROR in this line, it may because you haven't mark the folder 'resource' as resources root
                                            //       look for folder resource on the project root path, there is a folder resource, right click and choose in the end of list: 'Mark directory as'
            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the main stage.
     * @return
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}


// TODO: every DICE fall around the center of draft poll
//       every dice move away from the colliding dices, in the rival direction
//       this should be done in 3 seconds, so make this move a bit away from center to speed up