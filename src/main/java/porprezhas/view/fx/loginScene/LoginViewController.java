package porprezhas.view.fx.loginScene;

import com.sun.javafx.css.Style;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

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
        }
        //If the connection is already set to RMI, there is no  need to do anything

    }
}
