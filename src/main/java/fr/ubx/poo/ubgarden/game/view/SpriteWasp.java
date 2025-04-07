package fr.ubx.poo.ubgarden.game.view;

import fr.ubx.poo.ubgarden.game.go.GameObject;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

import static fr.ubx.poo.ubgarden.game.view.ImageResource.WASP_DOWN;

public class SpriteWasp extends Sprite {
    public SpriteWasp(Pane layer, Image image, GameObject gameObject) {
        super(layer, ImageResourceFactory.getInstance().get(WASP_DOWN), gameObject);
    }
}
