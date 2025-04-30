package fr.ubx.poo.ubgarden.game.go.personage;

import fr.ubx.poo.ubgarden.game.Direction;
import fr.ubx.poo.ubgarden.game.Game;
import fr.ubx.poo.ubgarden.game.Position;
import fr.ubx.poo.ubgarden.game.engine.Timer;
import fr.ubx.poo.ubgarden.game.go.GameObject;
import fr.ubx.poo.ubgarden.game.go.Movable;

import static fr.ubx.poo.ubgarden.game.Direction.DOWN;

public class Hornet extends Bugs implements Movable {
    public Hornet(Game game, Position position) {
        super(game, position,game.configuration().hornetMoveFrequency()*1000);
    }
}
