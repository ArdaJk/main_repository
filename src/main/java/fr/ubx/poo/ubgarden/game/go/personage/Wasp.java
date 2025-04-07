package fr.ubx.poo.ubgarden.game.go.personage;

import fr.ubx.poo.ubgarden.game.Game;
import fr.ubx.poo.ubgarden.game.Position;
import fr.ubx.poo.ubgarden.game.go.GameObject;

public class Wasp extends GameObject {
    public Wasp(Game game, Position position) {
        super(game, position);
    }

    public Wasp(Position position) {
        super(position);
    }
}
