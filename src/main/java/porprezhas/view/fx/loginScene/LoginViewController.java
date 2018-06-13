package porprezhas.view.fx.loginScene;

import com.sun.javafx.css.Style;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.util.Duration;

import static porprezhas.view.fx.loginScene.LoginViewController.ConnectionType.RMI;
import static porprezhas.view.fx.loginScene.LoginViewController.ConnectionType.SOCKET;

public class LoginViewController {

    @FXML
    Button loginViewButton;
    @FXML
    TextField textFieldLoginView;
    @FXML
    Button joinViewButton;
    @FXML
    Button loginViewRMIButton;
    @FXML
    Button loginViewSocketButton;
    @FXML
    Text loginViewText;

    private String voidString ="";
    enum ConnectionType{
        RMI,SOCKET
    }
    //Default connection method: RMI
    ConnectionType connectionType=RMI;


    public void initialize() {
        connectionButtonsSetup();
    }

    public void connectionButtonsSetup(){
        loginViewRMIButton.setBorder(new Border(new BorderStroke( Color.rgb(200, 177, 39),
                BorderStrokeStyle.SOLID, new CornerRadii(2),new BorderWidths(2))));
    }


    @FXML
    public void loginDone(ActionEvent event) {

        if(textFieldLoginView.getText()!=null&&!(textFieldLoginView.getText().equals(voidString))){
            loginViewButton.setVisible(false);
            textFieldLoginView.setVisible(false);
            joinViewButton.setStyle(
                    "-fx-background-color: #FFF1C6; "+
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
        else{

            loginViewText.setText("⚠Choose an username⚠");
            loginViewText.setFill(Paint.valueOf("#CB3C15"));
            loginViewText.setStyle("-fx-stroke:black;"+
            "-fx-stroke-width:1.5;");
            final Timeline timeline = new Timeline();
            timeline.setCycleCount(2);
            timeline.setAutoReverse(true);
            timeline.getKeyFrames().add(new KeyFrame(Duration.millis(5000),
                    new KeyValue(loginViewText.opacityProperty(), 0)));
            timeline.play();
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
            loginViewText.setFill(Paint.valueOf("#7BD7E1"));
            loginViewText.setStyle("-fx-stroke:black;"+
                    "-fx-stroke-width:1.5;");
            final Timeline timeline = new Timeline();
            timeline.setCycleCount(2);
            timeline.setAutoReverse(true);
            timeline.getKeyFrames().add(new KeyFrame(Duration.millis(5000),
                    new KeyValue(loginViewText.opacityProperty(), 0)));
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
            loginViewText.setFill(Paint.valueOf("#7BD7E1"));
            loginViewText.setStyle("-fx-stroke:black;"+
                    "-fx-stroke-width:1.5;");
            final Timeline timeline = new Timeline();
            timeline.setCycleCount(2);
            timeline.setAutoReverse(true);
            timeline.getKeyFrames().add(new KeyFrame(Duration.millis(5000),
                    new KeyValue(loginViewText.opacityProperty(), 0)));
            timeline.play();
        }
        //If the connection is already set to RMI, there is no  need to do anything

    }


}
