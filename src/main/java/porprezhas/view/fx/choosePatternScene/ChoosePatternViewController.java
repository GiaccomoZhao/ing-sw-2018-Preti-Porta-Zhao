package porprezhas.view.fx.choosePatternScene;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.ImageCursor;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.mockito.Mock;
import porprezhas.Network.ClientActionSingleton;
import porprezhas.model.dices.Pattern;
import porprezhas.view.fx.MovebleWindow;
import porprezhas.view.fx.MovebleWindowInterface;
import porprezhas.view.fx.SceneController;
import porprezhas.view.fx.StageManager;
import porprezhas.view.fx.gameScene.GuiSettings;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static porprezhas.view.fx.gameScene.GuiSettings.*;

public class ChoosePatternViewController implements Initializable, SceneController, MovebleWindowInterface {



    @FXML private ImageView choosePatternViewPattern1;
    @FXML private ImageView choosePatternViewPattern2;
    @FXML private ImageView choosePatternViewPattern3;
    @FXML private ImageView choosePatternViewPattern4;
    List<ImageView> imageViewpatternList;

    @FXML private Label choosePatternViewPatternLabel1;
    @FXML private Label choosePatternViewPatternLabel2;
    @FXML private Label choosePatternViewPatternLabel3;
    @FXML private Label choosePatternViewPatternLabel4;
    List<Label> labelList;

    @FXML AnchorPane ChoosePatternView;
    private Pane rootLayout;


    // Parent Controller
    StageManager stageManager;
    String stageName;

    private MovebleWindowInterface movable;




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
        // Create a Timeline to animate the transition between stages
        Timeline timeline = new Timeline();
        KeyFrame key = new KeyFrame(Duration.millis(STAGE_FADE_OUT),
                new KeyValue(stageManager.getStage(stageName).getScene().getRoot().
                        opacityProperty(), 0));
        timeline.getKeyFrames().add(key);
        timeline.setOnFinished((ae) -> {
            // Switch the Stage
            stageManager.setStage(GuiSettings.stageGameID, this.stageName);
        }
        );
        timeline.play();

    }

    @Override
    public void setCurrentStageTransition() {
        // Create a Timeline to animate the transition between stages
        Timeline timeline = new Timeline();

        // Add the transition animation
        // Using Opacity Fading
        KeyFrame key = new KeyFrame(Duration.millis(STAGE_FADE_IN),
                new KeyValue(stageManager.getStage(stageName).
                        getScene().getRoot().opacityProperty(), 1));
        timeline.getKeyFrames().add(key);

        // Change Stage
        timeline.setOnFinished((actionEvent) -> {
            ;
        });

        stageManager.getStage(stageName).setOnShowing(event -> {
            timeline.play();
        });
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
            System.out.println("Initializing PatternView");

        // assign the rootLayout the top most parent pane, now that it is initialized
        rootLayout = ChoosePatternView;

        Platform.runLater(() -> {
            // Add Move Window Listener
            movable = new MovebleWindow();
            setupWindowMoveListener(rootLayout, stageManager.getStage(this.stageName));
            addWindowMoveListener();

            // Add Window Appear Animation
            setCurrentStageTransition();
        });


        List<Pattern.TypePattern> patternList = new ArrayList<>();
        patternList.add(Pattern.TypePattern.KALEIDOSCOPIC_DREAM);
        patternList.add(Pattern.TypePattern.WATER_OF_LIFE);
        patternList.add(Pattern.TypePattern.VIRTUS);
        patternList.add(Pattern.TypePattern.SUNS_GLORY);

        patternSetup(patternList);

        setGameCursor();
    }





    public  void patternSetup(List<Pattern.TypePattern> typePatternList){

        imageViewpatternList = new ArrayList<>();
        imageViewpatternList.add(choosePatternViewPattern1);
        imageViewpatternList.add(choosePatternViewPattern2);
        imageViewpatternList.add(choosePatternViewPattern3);
        imageViewpatternList.add(choosePatternViewPattern4);

        labelList = new ArrayList<>();
        labelList.add(choosePatternViewPatternLabel1);
        labelList.add(choosePatternViewPatternLabel2);
        labelList.add(choosePatternViewPatternLabel3);
        labelList.add(choosePatternViewPatternLabel4);

        int i=0;
        for (Pattern.TypePattern patternType: typePatternList) {
            System.out.println(pathToPattern + patternType.name().toLowerCase() + ".png");
            System.out.println(new Image(pathToPattern + patternType.name().toLowerCase() + ".png"));
            imageViewpatternList.get(i).setImage(new Image(
                    pathToPattern + patternType.name().toLowerCase() + ".png"
            ));
            i++;
        }



/*       String filePath = getPathToFileIgnoreExt(pathToBorder, "pattern");
        if(filePath != null) {
            cardBorder = new Border(new BorderImage(
                    new Image(filePath),
                    new BorderWidths(BORDER_SIZE), Insets.EMPTY, // new Insets(10, 10, 10, 10),
                    new BorderWidths(BORDER_SIZE), true,
                    BorderRepeat.STRETCH, BorderRepeat.STRETCH));
        } else {
            cardBorder = null;
        }   */

    }

    private void setGameCursor() {
        rootLayout.setCursor(new ImageCursor(
                new Image(pathToCursor + "cursor_hand.png", 64.0, 64.0, true, true)));
    }


    @FXML
    private void onMouseClickedPattern(MouseEvent event){
        Label label= (Label)event.getSource();
        String choosen = label.getId().substring(29, 30);
        ClientActionSingleton.getClientAction().choosePattern(Integer.parseInt(choosen)-1);
        for (Label labelDisable:
             labelList) {
            if (!labelDisable.equals(label))
                labelDisable.setVisible(false);
        }
    }

    @FXML
    private void onMouseExitedPattern(MouseEvent event) {
        for (Label label:labelList) {
            if(label.equals(event.getSource())){
                label.setBorder(null);
            }
        }
    }

    @FXML
    private void onMouseEnteredPattern(MouseEvent event){

        for (Label label:labelList) {
            if(label.equals(event.getSource())){
                label.setBorder(new Border(new BorderStroke( Color.rgb(200, 114, 73),
                        BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

            }
        }
    }



}
