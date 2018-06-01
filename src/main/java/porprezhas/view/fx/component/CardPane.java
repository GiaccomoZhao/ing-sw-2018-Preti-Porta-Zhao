package porprezhas.view.fx.component;

import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

import static porprezhas.view.fx.GuiSettings.*;

public class CardPane {
    private Pane cardPane;
    private Border cardBorder;

    private void setupCardPane(String path, List<String> cardsName) {
        if(bDebug) {
            System.out.println("Printing setupToolCardPane(): ");
            System.out.println(new Image(pathToToolCard + "back.jpg"));
            System.out.println(new Image(pathToBorderFile));
        }
        cardPane.setBorder(new Border(new BorderStroke( Color.rgb(200, 200, 200),
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

        cardBorder = new Border(new BorderImage(
                new Image(pathToBorderFile),
                new BorderWidths(BORDER_SIZE), Insets.EMPTY, // new Insets(10, 10, 10, 10),
                new BorderWidths(BORDER_SIZE), true,
                BorderRepeat.STRETCH, BorderRepeat.STRETCH
        ));

        List<Label> labels = new ArrayList<>();
        for (int i = 0; i < cardsName.size(); i++) {
            ImageView imageView = new ImageView(new Image(pathToToolCard + cardsName.get(i)));
            imageView.fitWidthProperty().bind(cardPane.widthProperty().multiply(CARD_FIT_RATIO));
            imageView.fitHeightProperty().bind(cardPane.heightProperty().multiply(CARD_FIT_RATIO));
            imageView.setPreserveRatio(true);

            labels.add(new Label());
            labels.get(i).setGraphic(imageView);

            labels.get(i).translateXProperty().bind( cardPane.widthProperty().subtract(labels.get(i).widthProperty()).subtract(CARD_PANE_PADDING).multiply((double)(i) / (cardsName.size()-1)) );
            labels.get(i).translateYProperty().bind( cardPane.heightProperty().subtract(labels.get(i).heightProperty()).subtract(CARD_PANE_PADDING).multiply((double)(i) / (cardsName.size()-1)) );
            labels.get(i).setBorder(cardBorder);

            labels.get(i).setOnMouseEntered(event -> {
                Label source = ((Label)event.getSource());
                source.toFront();
                // add transition animation
                FadeTransition fadeTransition=new FadeTransition(Duration.millis(680), source);
                fadeTransition.setFromValue(0.80f);
                fadeTransition.setToValue(1.0f);
                fadeTransition.setAutoReverse(false);
                fadeTransition.play();
            });
        }

        cardPane.getChildren().addAll(labels);
//        toolCardPane.getChildren().addAll(imageView, imageView1, imageView2);
        cardPane.setOnSwipeLeft(event -> {
            // change tab to next one
            System.out.println("Swipe to left");
        });
    }


}
