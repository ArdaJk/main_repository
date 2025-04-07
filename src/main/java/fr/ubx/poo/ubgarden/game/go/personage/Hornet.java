package fr.ubx.poo.ubgarden.game.go.personage;

import fr.ubx.poo.ubgarden.game.Direction;
import fr.ubx.poo.ubgarden.game.Game;
import fr.ubx.poo.ubgarden.game.Position;
import fr.ubx.poo.ubgarden.game.go.GameObject;
import fr.ubx.poo.ubgarden.game.go.Movable;

public class Hornet extends GameObject implements Movable {
    public Hornet(Game game, Position position) {
        super(game, position);
    }

    public Hornet(Position position) {
        super(position);
    }

    @Override
    public boolean canMove(Direction direction) {
        return false;
    }

    @Override
    public Position move(Direction direction) {
        return null;
    }
}
