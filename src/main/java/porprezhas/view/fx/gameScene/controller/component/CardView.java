package porprezhas.view.fx.gameScene.controller.component;

import javafx.animation.FadeTransition;
import javafx.scene.control.Label;
import javafx.util.Duration;
import porprezhas.model.cards.Card;
import porprezhas.view.fx.gameScene.controller.GameViewController;
import porprezhas.view.fx.gameScene.controller.dialogBox.ConfirmBox;

import static porprezhas.view.fx.gameScene.GuiSettings.*;

public class CardView extends Label implements SubController {
    private final Card.Effect effect;
    private int price;

    public CardView(Card card) {
        effect = card.effect;
        price = 1;
    }

    public void setup() {
        // add transition animation
        FadeTransition fadeIn = new FadeTransition(Duration.millis(CARD_FADE_IN), this);
        fadeIn.setFromValue(cardOpacity);
        fadeIn.setToValue(1.0f);

        FadeTransition fadeOut = new FadeTransition(Duration.millis(CARD_FADE_OUT), this);
        fadeOut.setFromValue(1.0f);
        fadeOut.setToValue(cardOpacity);

        this.setOnMouseEntered(event -> {
            Label source = ((Label)event.getSource());
            source.toFront();
            fadeIn.play();
        });

        this.setOnMouseExited(event -> {
            Label source = ((Label)event.getSource());
            source.setOpacity(cardOpacity);
            fadeIn.stop();
            fadeOut.play();
        });

        this.setOnMouseClicked(event -> {
            Label source = (Label) event.getSource();
            System.out.println(source.toString());
            if(new ConfirmBox().display("Apply Tool Card N." + effect.ID, "Do you want use this tool card, spending " + price  + " token?\n\n" + effect.description)) {
                parentController.usingToolCard(effect);
            } else {
                parentController.usingToolCard(null);
            }
        });
    }

    public void updatePrice(int price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return effect.toString();
    }


    private GameViewController parentController;

    @Override
    public void setupSubController(GameViewController parentController) {
        this.parentController = parentController;
    }

    @Override
    public void activate() {
        this.setDisable(false);
    }

    @Override
    public void disable() {
        this.setDisable(true);
    }
}

