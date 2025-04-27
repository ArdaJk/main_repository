package fr.ubx.poo.ubgarden.game.go.decor;

import fr.ubx.poo.ubgarden.game.Game;
import fr.ubx.poo.ubgarden.game.Position;
import fr.ubx.poo.ubgarden.game.go.bonus.InsecticideBomb;
import fr.ubx.poo.ubgarden.game.go.personage.Gardener;
import fr.ubx.poo.ubgarden.game.go.personage.Wasp;

public class NestWasp extends Decor {
    private long lastSpawnTime = 0;

    public NestWasp(Game game, Position position) {
        super(game, position);
    }

    //The method we are going to use in order to create a wasp all 5 seconds
    public Wasp createWasp() {
        lastSpawnTime = System.currentTimeMillis();
        return new Wasp(game, super.getPosition());
    }

    @Override
    public boolean walkableBy(Gardener gardener) {
        return gardener.canWalkOn(this);
    }

    @Override
    public void update(long now) {
        if (System.currentTimeMillis() - lastSpawnTime >= 5000) {
            Wasp wasp = createWasp();
            System.out.println("Wasp created");
            game.addBug(wasp);
        }
    }
}
