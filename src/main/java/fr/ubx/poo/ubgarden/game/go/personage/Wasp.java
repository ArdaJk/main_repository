package fr.ubx.poo.ubgarden.game.go.personage;

import fr.ubx.poo.ubgarden.game.Direction;
import fr.ubx.poo.ubgarden.game.Game;
import fr.ubx.poo.ubgarden.game.Position;
import fr.ubx.poo.ubgarden.game.go.GameObject;
import fr.ubx.poo.ubgarden.game.go.Movable;
import fr.ubx.poo.ubgarden.game.go.decor.Decor;
import fr.ubx.poo.ubgarden.game.go.decor.Door;
import fr.ubx.poo.ubgarden.game.go.decor.Tree;

import static fr.ubx.poo.ubgarden.game.Direction.DOWN;

public class Wasp extends GameObject implements Movable {
    private Direction direction;

    //We added this attribut to verify if the corresponding sprite has created
    private Boolean hasSprite;

    public Wasp(Game game, Position position) {
        super(game, position);
        this.direction = DOWN;
        System.out.println(game);
        hasSprite = false;
    }

    public Wasp(Position position) {
        super(position);
    }

    @Override
    public boolean canMove(Direction direction) {
        Position nextPos = direction.nextPosition(getPosition());
        if (!game.world().getGrid().inside(nextPos)) {
            return false;
        }
        Decor nextObj = game.world().getGrid().get(nextPos);
        if (nextObj instanceof Door) {
            return false;
        }
        if (nextObj instanceof Tree) {
            return false;
        }
        return true;
    }

    @Override
    public Position move(Direction direction) {
        Position nextPos = direction.nextPosition(getPosition());
        setPosition(nextPos);
        return nextPos;
    }

    public void chooseDirection() {
        this.direction = DOWN;
        setModified(true);
    }

    public Direction getDirection() {
        return direction;
    }

    @Override
    public void update(long now) {
        if (canMove(direction)) {
            move(direction);
        }
    }

    public void setHasSprite(Boolean hasSprite) {
        this.hasSprite = hasSprite;
    }

    public Boolean getHasSprite() {
        return hasSprite;
    }
}
