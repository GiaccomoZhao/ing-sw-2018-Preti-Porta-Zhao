package porprezhas.view.fx.gameScene.component;

import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import porprezhas.model.cards.Card;

import java.util.ArrayList;
import java.util.List;

import static porprezhas.view.fx.gameScene.GuiSettings.*;

public class CardPane {
    private final Pane cardPane;
    private final Border cardBorder;
    private final String pathToCards;

    public CardPane(Pane cardPane, CardTab tab, String pathToCards) {
        this.cardPane = cardPane;
        this.pathToCards = pathToCards;
        String filePath = getPathToFile(pathToBorder, tab.name());
        if(filePath != null) {
            cardBorder = new Border(new BorderImage(
                    new Image(filePath),
                    new BorderWidths(BORDER_SIZE), Insets.EMPTY, // new Insets(10, 10, 10, 10),
                    new BorderWidths(BORDER_SIZE), true,
                    BorderRepeat.STRETCH, BorderRepeat.STRETCH));
        } else {
            cardBorder = null;
        }
    }

    public void setupCardPane(List<Card> cards) {
        if(bDebug) {
            System.out.println("\nsetupCardPane() for " + pathToCards);
        }
        cardPane.setBorder(new Border(new BorderStroke( Color.rgb(200, 200, 200),
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

        List<Label> labels = new ArrayList<>();
        for (int i = 0; i < cards.size(); i++) {
            if(bDebug) {
                System.out.println("loading: " + cards.get(i) + ".jpg"); }
            ImageView imageView = new ImageView(new Image(pathToCards + cards.get(i) + ".jpg"));
            imageView.fitWidthProperty().bind(cardPane.widthProperty().multiply(CARD_FIT_RATIO));
            imageView.fitHeightProperty().bind(cardPane.heightProperty().multiply(CARD_FIT_RATIO));
            imageView.setPreserveRatio(true);

            labels.add(new Label());
            labels.get(i).setGraphic(imageView);

            if(cards.size() == 1) {
                labels.get(i).translateXProperty().bind( cardPane.widthProperty().subtract(labels.get(i).widthProperty()).subtract(CARD_PANE_PADDING).multiply(0.5) );
                labels.get(i).translateYProperty().bind( cardPane.heightProperty().subtract(labels.get(i).heightProperty()).subtract(CARD_PANE_PADDING).multiply(0.5) );
            } else {
                labels.get(i).translateXProperty().bind(cardPane.widthProperty().subtract(labels.get(i).widthProperty()).subtract(CARD_PANE_PADDING).multiply((double) (i) / (cards.size() - 1)));
                labels.get(i).translateYProperty().bind(cardPane.heightProperty().subtract(labels.get(i).heightProperty()).subtract(CARD_PANE_PADDING).multiply((double) (i) / (cards.size() - 1)));
            }
            labels.get(i).setBorder(cardBorder);
            labels.get(i).setOpacity(cardOpacity);

            // add transition animation
            FadeTransition fadeIn = new FadeTransition(Duration.millis(CARD_FADE_IN), labels.get(i));
            fadeIn.setFromValue(cardOpacity);
            fadeIn.setToValue(1.0f);

            FadeTransition fadeOut = new FadeTransition(Duration.millis(CARD_FADE_OUT), labels.get(i));
            fadeOut.setFromValue(1.0f);
            fadeOut.setToValue(cardOpacity);

            labels.get(i).setOnMouseEntered(event -> {
                Label source = ((Label)event.getSource());
                source.toFront();
                fadeIn.play();
            });

            labels.get(i).setOnMouseExited(event -> {
                Label source = ((Label)event.getSource());
                source.setOpacity(cardOpacity);
                fadeIn.stop();
                fadeOut.play();
            });

            labels.get(i).setOnMouseClicked(event -> {
                Card source = (Card) event.getSource();
                System.out.println(source.toString());
                System.out.println(source.effect);
            });
        }

        cardPane.getChildren().addAll(labels);

        cardPane.setOnSwipeLeft(event -> {
            // change tab to next one
            System.out.println("Swipe to left");
        });
    }


}
