package fr.ubx.poo.ubgarden.game.go.personage;

import fr.ubx.poo.ubgarden.game.Direction;
import fr.ubx.poo.ubgarden.game.Game;
import fr.ubx.poo.ubgarden.game.Position;
import fr.ubx.poo.ubgarden.game.engine.Timer;
import fr.ubx.poo.ubgarden.game.go.GameObject;
import fr.ubx.poo.ubgarden.game.go.Movable;
import fr.ubx.poo.ubgarden.game.go.bonus.InsecticideBomb;
import fr.ubx.poo.ubgarden.game.go.decor.Decor;
import fr.ubx.poo.ubgarden.game.go.decor.Door;
import fr.ubx.poo.ubgarden.game.go.decor.Tree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import static fr.ubx.poo.ubgarden.game.Direction.*;

public class Wasp extends Bugs implements Movable {
    private static final int DAMAGE = 20;
    public Wasp(Game game, Position position) {
        super(game, position,game.configuration().waspMoveFrequency()*1000,1,DAMAGE);
    }

    @Override
    public void createBomb() {
        java.util.Map<Position, Decor> decorsWithoutBonus = new HashMap<>();
        for (Decor decor : game.world().getGrid().values()) {
            if (decor.getBonus() == null && decor.walkableBy(game.getGardener())) {
                decorsWithoutBonus.put(decor.getPosition(), decor);
            }
        }
        if (!decorsWithoutBonus.isEmpty()) {
            List<Decor> eligibleDecors = new ArrayList<>(decorsWithoutBonus.values());
            Random rand = new Random();
            Decor randomDecor1 = eligibleDecors.get(rand.nextInt(eligibleDecors.size()));
            Decor randomDecor2;
            do {
                randomDecor2 = eligibleDecors.get(rand.nextInt(eligibleDecors.size()));
            } while (randomDecor1 == randomDecor2);
            Position pos1 = randomDecor1.getPosition();
            Position pos2 = randomDecor2.getPosition();
            Decor targetDecor1 = game.world().getGrid().get(pos1);
            Decor targetDecor2 = game.world().getGrid().get(pos2);
            targetDecor1.setBonus(new InsecticideBomb(pos1, targetDecor1));
            targetDecor1.getBonus().setModified(true);
            targetDecor2.setBonus(new InsecticideBomb(pos2, targetDecor2));
            targetDecor2.getBonus().setModified(true);
        }
    }
}
