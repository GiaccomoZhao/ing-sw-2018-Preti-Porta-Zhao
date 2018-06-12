package porprezhas.view.fx.choosePatternScene;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import porprezhas.model.dices.Pattern;

import java.util.ArrayList;
import java.util.List;

import static porprezhas.view.fx.gameScene.GuiSettings.pathToPattern;

public class ChoosePatternViewController {



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


    public void initialize(){
        List<Pattern.TypePattern> patternList = new ArrayList<>();
        patternList.add(Pattern.TypePattern.KALEIDOSCOPIC_DREAM);
        patternList.add(Pattern.TypePattern.WATER_OF_LIFE);
        patternList.add(Pattern.TypePattern.VIRTUS);
        patternList.add(Pattern.TypePattern.SUNS_GLORY);

        patternSetup(patternList);

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

    @FXML
    private void onMouseEnteredPattern(MouseEvent event){

        // String borderpath = getPathToFileIgnoreExt(pathToBorder ,"PRIVATE_CARD" + ".gif");
        System.out.println(labelList);
        for (Label label:labelList) {
            if(label.equals(event.getSource())){
                label.setBorder(new Border(new BorderStroke( Color.rgb(200, 114, 73),
                        BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

            }
        }

        /*choosePatternViewPattern1.setBorder(new Border(new BorderStroke( Color.rgb(200, 200, 200),
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));*/
    }

}
