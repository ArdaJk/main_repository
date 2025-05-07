package fr.ubx.poo.ubgarden.game;


import fr.ubx.poo.ubgarden.game.go.decor.Decor;
import javafx.geometry.Pos;

import java.util.Collection;

public interface Map {
    int width();

    int height();

    Decor get(Position position);

    void set(Position position, Decor decor);

    Collection<Decor> values();

    boolean inside(Position nextPos);

    //We add this method in order to acces the number of carrots inside the map
    int getCarrots();

    //The method which returns the position of the door
    Position doorPosition();

    Position doorPrevPosition();
}
