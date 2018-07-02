package porprezhas.view.fx.gameScene.controller.component;

import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import porprezhas.model.cards.Card;
import porprezhas.view.fx.gameScene.controller.GameViewController;

import java.util.ArrayList;
import java.util.List;

import static porprezhas.view.fx.gameScene.GuiSettings.*;

public class CardPane implements SubController{
    private final Pane cardPane;
    private final Border cardBorder;
    private final String pathToCards;

    public CardPane(Pane cardPane, CardTab tab, String pathToCards) {
        this.cardPane = cardPane;
        this.pathToCards = pathToCards;
        String filePath = getPathToFileIgnoreExt(pathToBorder, tab.name());
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

        for (int i = 0; i < cards.size(); i++) {
            if(bDebug) {
                System.out.println("loading: " + cards.get(i) + ".jpg"); }

            // add image to card
            // setup vector graphic: image dimension
            ImageView imageView = null;
            try {
                imageView = new ImageView(new Image(pathToCards + cards.get(i).effect.name + ".jpg"));
            } catch (IllegalArgumentException e) {
                System.err.println("The file with path+name = " + pathToCards + cards.get(i).effect.name + ".jpg" + " \t has not been found");
                e.printStackTrace();
            }
            imageView.fitWidthProperty().bind(cardPane.widthProperty().multiply(CARD_FIT_RATIO));
            imageView.fitHeightProperty().bind(cardPane.heightProperty().multiply(CARD_FIT_RATIO));
            imageView.setPreserveRatio(true);

            // attach image to card view
            CardView cardView =  new CardView(cards.get(i));
            cardView.setGraphic(imageView);

            // setup vector graphic: image offset
            if(cards.size() == 1) {
                cardView.translateXProperty().bind( cardPane.widthProperty().subtract(cardView.widthProperty()).subtract(CARD_PANE_PADDING).multiply(0.5) );
                cardView.translateYProperty().bind( cardPane.heightProperty().subtract(cardView.heightProperty()).subtract(CARD_PANE_PADDING).multiply(0.5) );
            } else {
                cardView.translateXProperty().bind(cardPane.widthProperty().subtract(cardView.widthProperty()).subtract(CARD_PANE_PADDING).multiply((double) (i) / (cards.size() - 1)));
                cardView.translateYProperty().bind(cardPane.heightProperty().subtract(cardView.heightProperty()).subtract(CARD_PANE_PADDING).multiply((double) (i) / (cards.size() - 1)));
            }
            // setup board and default opacity
            cardView.setBorder(cardBorder);
            cardView.setOpacity(cardOpacity);

            // setup Card View
            cardView.setupSubController(parentController);
            cardView.setup();

            // add card view to the card pane
            cardPane.getChildren().add(cardView);
        }

        cardPane.setOnSwipeLeft(event -> {
            // change tab to next one, NOT printing...
            System.out.println("Swipe to left");
        });
    }



    private GameViewController parentController;

    @Override
    public void setupSubController(GameViewController parentController) {
        this.parentController = parentController;
    }

    @Override
    public void activate() {
        cardPane.setDisable(false);
    }

    @Override
    public void disable() {
        cardPane.setDisable(true);
    }
}
