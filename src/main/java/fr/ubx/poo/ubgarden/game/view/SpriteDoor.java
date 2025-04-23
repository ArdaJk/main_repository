package fr.ubx.poo.ubgarden.game.view;

import fr.ubx.poo.ubgarden.game.go.GameObject;
import fr.ubx.poo.ubgarden.game.go.decor.Door;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

import static fr.ubx.poo.ubgarden.game.view.ImageResourceFactory.getInstance;

public class SpriteDoor extends Sprite {
    public SpriteDoor(Pane layer, Image image, GameObject gameObject) {
        super(layer, null, gameObject);
        updateImage();
    }

    @Override
    public void updateImage() {
        Door door = (Door) getGameObject() ;
        if(!door.isClosed()) {
            setImage(getInstance().get(ImageResource.DOOR_OPENED));
        } else {
            setImage(getInstance().get(ImageResource.DOOR_CLOSED));
        }
    }
}