package fr.ubx.poo.ubgarden.game.go.bonus;

import fr.ubx.poo.ubgarden.game.Position;
import fr.ubx.poo.ubgarden.game.go.Pickupable;
import fr.ubx.poo.ubgarden.game.go.decor.Decor;

public class Carrot extends Bonus implements Pickupable {
    public Carrot(Position position, Decor decor) {
        super(position, decor);
    }
}
