package porprezhas.view.fx.loginScene;

import com.sun.javafx.css.Style;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import porprezhas.Network.*;
import porprezhas.Network.Command.Action;
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
    @FXML TextField textFieldLoginView;
    @FXML Button joinViewButton;
    @FXML Button loginViewRMIButton;
    @FXML Button loginViewSocketButton;
    @FXML Text loginViewText;
    @FXML ImageView loginViewImage;
    @FXML StackPane backgroundPane;
    @FXML AnchorPane loginView;

    private Pane rootLayout;


    private StageManager stageManager;
    private String stageName;

    private MovebleWindowInterface movable;

    private Circle clipWindow;
    private double clipRadius;

    private DoubleProperty opacityProperty;


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
        KeyFrame keyFadeIn = new KeyFrame(Duration.millis(STAGE_FADE_IN),
                new KeyValue(rootLayout.opacityProperty(), 1));

        // and using Rotation transformation
        KeyFrame keyRotate = new KeyFrame(Duration.millis(STAGE_FADE_IN),
                new KeyValue(backgroundPane.rotateProperty(), 3* 360));

        // and add window dimension Growing effect
        // using shape Clip
        KeyFrame keyViewPortDimension = new KeyFrame(Duration.millis(STAGE_FADE_IN),
                new KeyValue(clipWindow.radiusProperty(), clipRadius, Interpolator.EASE_IN));


        // add the effects in the time line
        timeline.getKeyFrames().add(keyFadeIn);
        timeline.getKeyFrames().add(keyRotate);
        timeline.getKeyFrames().add(keyViewPortDimension);

        //
        timeline.setOnFinished((actionEvent) -> {
            ;
        });

        stageManager.getStage(stageName).setOnShowing(event -> {
            rootLayout.setOpacity(0.8f);    // Set starting Opacity value
            clipWindow.setRadius(1);      // Set starting Dimension
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


        BackgroundMusicPlayer.playRandomMusic(pathToLoginMusic);
    }




    private void connectionButtonsSetup(){
        loginViewRMIButton.setBorder(new Border(new BorderStroke( Color.rgb(200, 177, 39),
                BorderStrokeStyle.SOLID, new CornerRadii(2),new BorderWidths(2))));
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
        double radius = Useful.getMinBetween(x , y) /2;

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











    @FXML
    public void onJoinButton(ActionEvent event) {

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
        ClientActionSingleton.getClientAction().join();

        System.out.println("Goto next");

        Platform.runLater(() -> {
            goToNextStage();
        });
    }

    @FXML
    public void loginDone(ActionEvent event) {



        if(textFieldLoginView.getText()!=null  &&  !(textFieldLoginView.getText().equals(voidString))){



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
                this.username = textFieldLoginView.getText();
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
                loginViewText.setText("");
                loginViewText.setFill(Color.rgb(0xCB, 0x3C, 0x15));
                loginViewText.setStyle("-fx-stroke:black;"+
                        "-fx-stroke-width:1.5;");
//                loginViewText.setOpacity(1);
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
            loginViewText.setText("");
            loginViewText.setFill(Color.rgb(0x7B, 0xD7, 0xE1));
            loginViewText.setStyle("-fx-stroke:black;"+
                    "-fx-stroke-width:1.5;");
//            loginViewText.setOpacity(1);
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
            loginViewText.setText("");
            loginViewText.setFill(Color.rgb(0x7B, 0xD7, 0xE1));
            loginViewText.setStyle("-fx-stroke:black;"+
                    "-fx-stroke-width:1.5;");
//            loginViewText.setOpacity(1);
            final Timeline timeline = new Timeline();
            timeline.getKeyFrames().add(new KeyFrame(Duration.millis(5000),
                    new KeyValue(loginViewText.opacityProperty(), 0, Interpolator.LINEAR)));
            timeline.play();
        }
        //If the connection is already set to RMI, there is no  need to do anything

    }


}
