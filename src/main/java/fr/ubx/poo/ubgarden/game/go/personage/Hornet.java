package fr.ubx.poo.ubgarden.game.go.personage;

import fr.ubx.poo.ubgarden.game.Direction;
import fr.ubx.poo.ubgarden.game.Game;
import fr.ubx.poo.ubgarden.game.Position;
import fr.ubx.poo.ubgarden.game.engine.Timer;
import fr.ubx.poo.ubgarden.game.go.Movable;
import fr.ubx.poo.ubgarden.game.go.bonus.InsecticideBomb;

public class Hornet extends Bugs implements Movable {
    private static final int DAMAGE = 30;
    private final Timer attackTimer = new Timer(1000);

    public Hornet(Game game, Position position) {
        super(game, position, game.configuration().hornetMoveFrequency() * 1000, 2, DAMAGE);
    }

    @Override
    public void update(long now) {
        attackTimer.update(now);

        if (this.life <= 0) {
            remove();
            return;
        }

        if (hurtByBomb) {
            hurt();
            if (game.world().getGrid().get(getPosition()).getBonus() instanceof InsecticideBomb bomb) {
                bomb.setUsed(true);
                bomb.setModified(true);
            }
            hurtByBomb = false;
            return;
        }

        if (game.world().getGrid().get(getPosition()).getBonus() instanceof InsecticideBomb) {
            hurtByBomb = true;
            return;
        }

        if (getPosition().equals(game.getGardener().getPosition()) && !attackTimer.isRunning()) {
            System.out.println("Gardener has been hit!");
            game.getGardener().hurt(this.damage);
            this.hurt();
            attackTimer.start();
            if (this.life <= 0) {
                remove();
                return;
            }
        }

        moveTimer.update(now);
        if (!moveTimer.isRunning()) {
            chooseDirection();
            if (canMove(direction)) {
                move(direction);
            } else {
                while (!canMove(direction)) {
                    chooseDirection();
                    if (canMove(direction)) {
                        move(direction);
                    }
                }
            }
            moveTimer.start();
        }
    }
}
