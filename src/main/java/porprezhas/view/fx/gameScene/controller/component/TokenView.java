package porprezhas.view.fx.gameScene.controller.component;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class TokenView extends ImageView {
    String resourcePath = "other/circle.png";

    public TokenView() {
        super();
        setImage();
    }

    public TokenView setImage() {
        setImage( new Image(resourcePath) );
        this.setCache(true);
        this.setFitWidth(16.0);
        this.setFitHeight(16.0);
        this.setPreserveRatio(true);
        this.setPickOnBounds(true);
        return this;
    }
}
