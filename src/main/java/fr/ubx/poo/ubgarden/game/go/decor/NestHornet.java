package fr.ubx.poo.ubgarden.game.go.decor;
import fr.ubx.poo.ubgarden.game.Game;
import fr.ubx.poo.ubgarden.game.Position;
import fr.ubx.poo.ubgarden.game.go.personage.Gardener;
import fr.ubx.poo.ubgarden.game.go.personage.Hornet;
import fr.ubx.poo.ubgarden.game.go.personage.Wasp;

public class NestHornet extends Decor {
    private long lastSpawnTime = 0;

    public NestHornet(Game game, Position position) {
        super(game,position);
    }

    //The method we are going to use in order to create a hornet all 10 seconds
    public Hornet createHornet() {
        lastSpawnTime = System.currentTimeMillis();
        return new Hornet(game, super.getPosition());
    }

    @Override
    public boolean walkableBy(Gardener gardener) {
        return gardener.canWalkOn(this);
    }

    @Override
    public void update(long now) {
        if (System.currentTimeMillis() - lastSpawnTime >= 10000) {
            Hornet hornet = createHornet();
            System.out.println("Hornet created");
            game.addBug(hornet);
        }
    }
}
