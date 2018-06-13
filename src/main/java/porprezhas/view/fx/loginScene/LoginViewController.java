package porprezhas.view.fx.loginScene;

import com.sun.javafx.css.Style;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import porprezhas.Network.*;
import porprezhas.Network.Command.Action;
import porprezhas.view.fx.BackgroundMusicPlayer;
import porprezhas.view.fx.SceneController;
import porprezhas.view.fx.StageManager;
import porprezhas.view.fx.gameScene.GuiSettings;
import porprezhas.view.fx.gameScene.controller.GameViewController;

import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ResourceBundle;

import static porprezhas.view.fx.gameScene.GuiSettings.*;
import static porprezhas.view.fx.loginScene.LoginViewController.ConnectionType.RMI;
import static porprezhas.view.fx.loginScene.LoginViewController.ConnectionType.SOCKET;

public class LoginViewController implements SceneController, Initializable {

    @FXML Button loginViewButton;
    @FXML TextField textFieldLoginView;
    @FXML Button joinViewButton;
    @FXML Button loginViewRMIButton;
    @FXML Button loginViewSocketButton;
    @FXML Text loginViewText;
    @FXML AnchorPane loginView;

    private Pane rootLayout;


    private StageManager stageManager;
    private String stageName;

    private final String voidString = "";
    private double xOffset = 0;
    private double yOffset = 0;

    private DoubleProperty opacityProperty;


    private int port=1457;
    private InetAddress ip;
    enum ConnectionType{
        RMI,SOCKET
    }
    //Default connection method: RMI
    ConnectionType connectionType=RMI;

    private String username;




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

            // Switch the Stage
            stageManager.setStage(GuiSettings.stagePatternID, this.stageName);
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



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if(bDebug)
            System.out.println("Initializing LoginView");

        connectionButtonsSetup();

        // assign the rootLayout the top most parent pane, now that it is initialized
        rootLayout = loginView;

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
                stageManager.getStage(stageName).setX(event.getScreenX() - xOffset);
                stageManager.getStage(stageName).setY(event.getScreenY() - yOffset);
            }
        });

        BackgroundMusicPlayer.playRandomMusic(pathToLoginMusic);
    }

    public void connectionButtonsSetup(){
        loginViewRMIButton.setBorder(new Border(new BorderStroke( Color.rgb(200, 177, 39),
                BorderStrokeStyle.SOLID, new CornerRadii(2),new BorderWidths(2))));
    }


    @FXML
    public void onJoinButton(ActionEvent event) {
        GameViewController gameViewController=null;
        //TODO TO-DO jack metti la get qua sopra

        ViewUpdateHandlerInterface viewUpdateHandlerInterface = new GUIViewUpdateHandler(gameViewController);
        if(this.connectionType==RMI) {
            try {
                ClientObserver clientObserver = new ClientObserver(viewUpdateHandlerInterface, username);
            } catch (RemoteException e) {
                e.printStackTrace();
            } catch (NotBoundException e) {
                e.printStackTrace();
            }
        }
        ClientActionSingleton.getClientAction().join();

        System.out.println("Goto next");

        goToNextStage();
    }

    @FXML
    public void loginDone(ActionEvent event) {



        if(textFieldLoginView.getText()!=null&&!(textFieldLoginView.getText().equals(voidString))){



            if(this.connectionType==RMI)
                ClientActionSingleton.setClientActionInstance(new RMIClientAction());
            else{

                try {
                    ip = InetAddress.getLocalHost();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                ClientActionSingleton.setClientActionInstance(new SocketClientAction(ip , port));
            }

            if(ClientActionSingleton.getClientAction().login(textFieldLoginView.getText())) {
                this.username=username;
                loginViewButton.setVisible(false);
                textFieldLoginView.setVisible(false);
                joinViewButton.setStyle(
                        "-fx-background-color: #FFF1C6; " +
                                "-fx-background-radius: 50em; " +
                                "-fx-min-width: 90px; " +
                                "-fx-min-height: 90px; " +
                                "-fx-max-width: 90px; " +
                                "-fx-max-height: 90px;"
                );
                loginViewRMIButton.setVisible(false);
                loginViewSocketButton.setVisible(false);
                joinViewButton.setVisible(true);
            }
            else {
                //if there is an active Socket connection we close it
                if(this.connectionType==SOCKET && ClientActionSingleton.getClientAction()!=null)
                    ((SocketClientAction)ClientActionSingleton.getClientAction()).closeConnection();

                loginViewText.setText("⚠Choose an username⚠");
                loginViewText.setFill(Color.rgb(0xCB, 0x3C, 0x15));
                loginViewText.setStyle("-fx-stroke:black;"+
                        "-fx-stroke-width:1.5;");
                loginViewText.setOpacity(1);
                final Timeline timeline = new Timeline();
                timeline.getKeyFrames().add(new KeyFrame(Duration.millis(5000),
                        new KeyValue(loginViewText.opacityProperty(), 0, Interpolator.LINEAR)));
                timeline.play();
            }


        }





    }
    @FXML
    public void connectionMethodChooseRMI(ActionEvent event){
       //If the connection is already set to Socket, the RMI border is highlighted
        if (connectionType.equals(SOCKET)) {
            connectionType = RMI;
            loginViewRMIButton.setBorder(new Border(new BorderStroke( Color.rgb(200, 177, 39),
                    BorderStrokeStyle.SOLID, new CornerRadii(2), new BorderWidths(2))));
            loginViewSocketButton.setBorder(new Border(new BorderStroke( Color.rgb(200, 177, 39),
                    BorderStrokeStyle.SOLID,new CornerRadii(0), new BorderWidths(0))));
            loginViewText.setText("Connection mode set to RMI");
            loginViewText.setFill(Color.rgb(0x7B, 0xD7, 0xE1));
            loginViewText.setStyle("-fx-stroke:black;"+
                    "-fx-stroke-width:1.5;");
            loginViewText.setOpacity(1);
            final Timeline timeline = new Timeline();
            timeline.getKeyFrames().add(new KeyFrame(Duration.millis(5000),
                    new KeyValue(loginViewText.opacityProperty(), 0, Interpolator.LINEAR)));
            timeline.play();
        }
        //If the connection is already set to RMI, there is no  need to do anything

    }


    @FXML
    public void connectionMethodChooseSocket(ActionEvent event){
        //If the connection is already set to RMI, the Socket border is highlighted
        if (connectionType.equals(RMI)) {
            connectionType = SOCKET;
            loginViewSocketButton.setBorder(new Border(new BorderStroke( Color.rgb(200, 177, 39),
                    BorderStrokeStyle.SOLID, new CornerRadii(2), new BorderWidths(2))));
            loginViewRMIButton.setBorder(new Border(new BorderStroke( Color.rgb(200, 177, 39),
                    BorderStrokeStyle.SOLID, new CornerRadii(0), new BorderWidths(0))));
            loginViewText.setText("Connection mode set to Socket");
            loginViewText.setFill(Color.rgb(0x7B, 0xD7, 0xE1));
            loginViewText.setStyle("-fx-stroke:black;"+
                    "-fx-stroke-width:1.5;");
            loginViewText.setOpacity(1);
            final Timeline timeline = new Timeline();
            timeline.getKeyFrames().add(new KeyFrame(Duration.millis(5000),
                    new KeyValue(loginViewText.opacityProperty(), 0, Interpolator.LINEAR)));
            timeline.play();
        }
        //If the connection is already set to RMI, there is no  need to do anything

    }


}
