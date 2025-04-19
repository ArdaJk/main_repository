package fr.ubx.poo.ubgarden.game.go.decor;

import fr.ubx.poo.ubgarden.game.Position;
import fr.ubx.poo.ubgarden.game.go.personage.Gardener;

public class NestWasp extends Decor {
    private long lastSpawnTime = System.currentTimeMillis();

    public NestWasp(Position position) {
        super(position);
    }

    @Override
    public boolean walkableBy(Gardener gardener) {
        return gardener.canWalkOn(this);
    }
    
}
