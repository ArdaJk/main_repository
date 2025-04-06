package fr.ubx.poo.ubgarden.game.view;

import fr.ubx.poo.ubgarden.game.go.GameObject;
import fr.ubx.poo.ubgarden.game.go.bonus.Carrot;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

public class SpriteCarrot extends Sprite {
    public SpriteCarrot(Pane layer, Image image, GameObject gameObject) {
        super(layer, null, gameObject);
        updateImage();
    }
}
