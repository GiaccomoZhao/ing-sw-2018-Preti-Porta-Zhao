package porprezhas.view.fx.loginScene;

import javafx.animation.*;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import porprezhas.Network.*;
import porprezhas.Useful;
import porprezhas.view.fx.*;
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

public class LoginViewController implements Initializable, SceneController, MovebleWindowInterface {

    @FXML Button loginViewButton;
    @FXML TextField userNameTextField;
    @FXML Button joinViewButton;
    @FXML Button loginViewRMIButton;
    @FXML Button loginViewSocketButton;
    @FXML Text warningText;
    @FXML ImageView loginViewImage;
    @FXML StackPane backgroundPane;

    @FXML VBox loginScene;
    @FXML AnchorPane joinScene;
    @FXML AnchorPane loginView;

    private Pane rootLayout;


    private StageManager stageManager;
    private String stageName;

    private MovebleWindowInterface movable;

    private Circle clipWindow;
    private double clipRadius;

//    private DoubleProperty opacityProperty;
    private Timeline timeline;


    private final String voidString = "";


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
        if(bDebug)
            System.out.println("Set " + stageManager + "\n\t\t to " + stageName + "\n\t\t in " + this);
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
        // Create a Timeline to animate the transition between stages
        Timeline timeline = new Timeline();

        // Add the transition animation
        // Using Opacity Fading
        KeyFrame keyFadeIn = new KeyFrame(Duration.millis(STAGE_FADE_IN * 0.2),
                new KeyValue(rootLayout.opacityProperty(), 1));

        // and using Rotation transformation
        KeyFrame keyRotate = new KeyFrame(Duration.millis(STAGE_FADE_IN),
                new KeyValue(backgroundPane.rotateProperty(), 1* 360));

        // and add window dimension Growing effect
        // using shape Clip
        KeyFrame keyViewPortDimension = new KeyFrame(Duration.millis(STAGE_FADE_IN*0.6),
                new KeyValue(clipWindow.radiusProperty(), clipRadius));


        // add the effects in the time line
        timeline.getKeyFrames().add(keyFadeIn);
        timeline.getKeyFrames().add(keyRotate);
        timeline.getKeyFrames().add(keyViewPortDimension);

        timeline.setDelay(Duration.millis(STAGE_FADE_IN/2));

        //
        timeline.setOnFinished((actionEvent) -> {
            ;
        });

        stageManager.getStage(stageName).setOnShowing(event -> {
            rootLayout.setOpacity(0.8f);    // Set starting Opacity value
            clipWindow.setRadius(50);      // Set starting Dimension
//            backgroundPane.setRotate(180);
            timeline.play();
        });
        if(bDebug)
            System.out.println("set " + rootLayout);
    }




    @Override
    public void setupWindowMoveListener(Pane rootLayout, Stage stage) {
        movable.setupWindowMoveListener(rootLayout, stage);
    }

    @Override
    public void addWindowMoveListener() {
        movable.addWindowMoveListener();
    }




    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if(bDebug)
            System.out.println("Initializing LoginView");

        // assign the rootLayout the top most parent pane, now that it is initialized
        rootLayout = loginView;

        Platform.runLater(() -> {
            // Add Move Window Listener
            movable = new MovebleWindow();
            setupWindowMoveListener(rootLayout, stageManager.getStage(this.stageName));
            addWindowMoveListener();

            // Add Window Appear Animation
            setCurrentStageTransition();
        });


        // Clip the Top most Pane -Root Layout- into a Circle
        clipWindow = clipToCircle(rootLayout);
        clipRadius = clipWindow.getRadius();


        connectionButtonsSetup();
        joinButtonSetup();
        timeline = new Timeline();

        BackgroundMusicPlayer.playRandomMusic(pathToLoginMusic);
    }




    private void connectionButtonsSetup(){
        loginScene.setVisible(true);
        loginViewRMIButton.setBorder(new Border(new BorderStroke( Color.rgb(200, 177, 39),
                BorderStrokeStyle.SOLID, new CornerRadii(2),new BorderWidths(2))));
    }

    private void joinButtonSetup() {
        joinScene.setVisible(false);
        joinViewButton.setStyle(
                "-fx-background-color: #FFF1C6; " +
                        "-fx-background-radius: 50em; " +
                        "-fx-min-width: 90px; " +
                        "-fx-min-height: 90px; " +
                        "-fx-max-width: 90px; " +
                        "-fx-max-height: 90px;"
        );
    }



    /**
     * Clip the Root Layout into a Circle shape.
     * Requires his Scene to be Transparent,
     * if you do not want see a White fill of background outside of the circle.
     *
     * @param tempPane  Top most Container that contain all panes and controls.
     */
    private Circle clipToCircle(Pane tempPane){
        // Create a new Circle Sharpe
        Circle circle = new Circle();

        double x = tempPane.getPrefWidth();
        double y = tempPane.getPrefHeight();

        // Calculate the Radius of the Circle Window
        double radius = Double.min(x , y) /2;

        //Setting the properties of the circle
        circle.setCenterX( x /2 );
        circle.setCenterY( y /2 );
        circle.setRadius(radius);

        // Clip the Pane -Layout- into a Circle
        tempPane.setClip(circle);

        return circle;
    }

    /**
     * For not prefect Circle Window
     * Clip the Root Layout into a Ellipse shape.
     * Requires his Scene to be Transparent,
     * if you do not want see a White fill of background outside of the ellipse.
     *
     * @param tempPane  Top most Container that contain all panes and controls.
     */
    private Ellipse clipToEllipse(Pane tempPane){
        // Create a new Ellipse Sharpe
        Ellipse ellipse = new Ellipse();

        double x = tempPane.getPrefWidth();
        double y = tempPane.getPrefHeight();

        // Calculate the Radius of the Circle Window
//        double radius = Useful.getMinBetween(x , y) /2;
        x /= 2;
        y /= 2;

        //Setting the properties of the circle
        ellipse.setCenterX( x );
        ellipse.setCenterY( y );
        ellipse.setRadiusX( x );
        ellipse.setRadiusX( y );

        // Clip the Pane -Layout- into a Circle
        tempPane.setClip(ellipse);

        return ellipse;
    }






    private void showWarningText(String text) {
        warningText.setText(text);
        warningText.setFill(Color.rgb(0xCB, 0x3C, 0x15));
        warningText.setStyle("-fx-stroke:black;" +
                "-fx-stroke-width:1.5;");
        warningText.setOpacity(1);

        timeline.stop();
        timeline = new Timeline();
        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(5000),
                new KeyValue(warningText.opacityProperty(), 0, Interpolator.DISCRETE)));
        timeline.play();
    }

    private void showTipText(String text) {
        warningText.setText(text);
        warningText.setFill(Color.rgb(0x7B, 0xD7, 0xE1));
        warningText.setStyle("-fx-stroke:black;"+
                "-fx-stroke-width:1.5;");
        warningText.setOpacity(1);

        timeline.stop();
        timeline = new Timeline();
        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(5000),
                new KeyValue(warningText.opacityProperty(), 0, Interpolator.LINEAR)));
        timeline.play();
    }





    @FXML
    public void onJoinButton(ActionEvent event) {
        System.out.println("Click Join");

        GameViewController gameViewController = Useful.convertInstanceOfObject(stageManager.getController(stageGameID), GameViewController.class);
        ViewUpdateHandlerInterface viewUpdateHandlerInterface = new GUIViewUpdateHandler(gameViewController);

        gameViewController.setUserName(username);

        System.out.println(viewUpdateHandlerInterface);
        System.out.println(username);

        if(this.connectionType==RMI) {
            try {
                ClientObserver clientObserver = new ClientObserver(viewUpdateHandlerInterface, username);
            } catch (RemoteException e) {
                e.printStackTrace();
            } catch (NotBoundException e) {
                e.printStackTrace();
            }
        }
        ClientActionSingleton.getClientAction().join(viewUpdateHandlerInterface);

        System.out.println("Goto next");

        Platform.runLater(() -> {
            goToNextStage();
        });
    }

    @FXML
    public void loginDone(ActionEvent event) {

        // if user typed his UserName
        if(userNameTextField.getText()!=null  &&  !(userNameTextField.getText().equals(voidString))) {

            // Start the Connection to the Server
            if (this.connectionType == RMI) {
                try {
                    ClientActionSingleton.setClientActionInstance(new RMIClientAction());
                } catch (RemoteException e) {
                    System.err.println(e.getMessage());
                } catch (NotBoundException e) {
                    System.err.println(e.getMessage());
                }
            }
            else if(this.connectionType == SOCKET){
                try {
                    ip = InetAddress.getLocalHost();
                    ClientActionSingleton.setClientActionInstance(new SocketClientAction(ip, port));
                } catch (UnknownHostException e) {
                    System.err.println(e.getMessage());
                }
            }

            // Error cached
            if(null == ClientActionSingleton.getClientAction()  ||
                    !ClientActionSingleton.getClientAction().isConnected() ) {
                showWarningText("404: Server NOT Found");

            // Connected to server
            } else {
                // Try to Login with given user name
                if (ClientActionSingleton.getClientAction().login(userNameTextField.getText())) {
                    // Logged In
                    this.username = userNameTextField.getText();

                    // Open next Scene - Join Scene
                    loginScene.setVisible(false);
                    joinScene.setVisible(true);
//                    joinScene.toFront();


                // Login Failed!!!
                } else {
                    //if there is an active Socket connection we close it
                    if (this.connectionType == SOCKET && ClientActionSingleton.getClientAction() != null)
                        ((SocketClientAction) ClientActionSingleton.getClientAction()).closeConnection();
                }
            }
        } else {
            showWarningText("⚠Choose an username⚠");
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

            showTipText("Connection mode set to RMI");
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
            showTipText("Connection mode set to Socket");
        }
        //If the connection is already set to RMI, there is no  need to do anything

    }


}
