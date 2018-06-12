package porprezhas.view.fx.choosePatternScene;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import porprezhas.model.dices.Pattern;

import java.util.ArrayList;
import java.util.List;

import static porprezhas.view.fx.gameScene.GuiSettings.*;

public class ChoosePatternView extends Application {

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

    private Stage primaryStage;
    private Parent rootLayout;
    private double xOffset = 0;
    private double yOffset = 0;

    @Override
    public void start(Stage primaryStage) throws Exception {
    this.primaryStage = primaryStage;

  /*  FileInputStream input = new FileInputStream("target/classes/pattern/aurora_sagradis.png");
    Image image = new Image(input);
    ImageView imageView = new ImageView(image);
    Button button = new Button("Pattern1",imageView);*/


    initRootLayout();
    }

    public void initRootLayout() {
        try {
            // create a FXMLLoader and open fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/ChoosePatternView.fxml"));
            if (loader == null)
                System.err.println(this + ": Error with loader.setLocation(" + getClass().getResource("/ChoosePatternView.fxml") + ")");
            rootLayout = loader.load();
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.initStyle(StageStyle.UNDECORATED);

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
                    primaryStage.setX(event.getScreenX() - xOffset);
                    primaryStage.setY(event.getScreenY() - yOffset);
                }
            });

            List<Pattern.TypePattern> patternList = new ArrayList<>();
            patternList.add(Pattern.TypePattern.KALEIDOSCOPIC_DREAM);
            patternList.add(Pattern.TypePattern.WATER_OF_LIFE);
            patternList.add(Pattern.TypePattern.VIRTUS);
            patternList.add(Pattern.TypePattern.SUNS_GLORY);

            patternSetup(patternList);


            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void patternSetup(List<Pattern.TypePattern> typePatternList){

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

    @FXML
    private void onMouseEnteredPattern(MouseEvent event){

       // String borderpath = getPathToFileIgnoreExt(pathToBorder ,"PRIVATE_CARD" + ".gif");
        System.out.println(labelList);
        for (Label label:labelList) {
            if(label.equals(event.getSource())){
                label.setBorder(new Border(new BorderStroke( Color.rgb(200, 0, 0),
                        BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

            }
        }

        /*choosePatternViewPattern1.setBorder(new Border(new BorderStroke( Color.rgb(200, 200, 200),
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));*/
    }

    public static void main(String[] args) {
        launch(args);
    }


}
