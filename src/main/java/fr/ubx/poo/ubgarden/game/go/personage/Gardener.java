/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.ubgarden.game.go.personage;

import fr.ubx.poo.ubgarden.game.Direction;
import fr.ubx.poo.ubgarden.game.Game;
import fr.ubx.poo.ubgarden.game.Position;
import fr.ubx.poo.ubgarden.game.go.GameObject;
import fr.ubx.poo.ubgarden.game.go.Movable;
import fr.ubx.poo.ubgarden.game.go.PickupVisitor;
import fr.ubx.poo.ubgarden.game.go.WalkVisitor;
import fr.ubx.poo.ubgarden.game.go.bonus.Bonus;
import fr.ubx.poo.ubgarden.game.go.bonus.InsecticideBomb;
import fr.ubx.poo.ubgarden.game.go.bonus.PoisonedApple;
import fr.ubx.poo.ubgarden.game.go.bonus.EnergyBoost;
import fr.ubx.poo.ubgarden.game.go.decor.*;
import fr.ubx.poo.ubgarden.game.go.decor.ground.Grass;
import fr.ubx.poo.ubgarden.game.go.decor.ground.Land;
import fr.ubx.poo.ubgarden.game.launcher.GameLauncher;

public class Gardener extends GameObject implements Movable, PickupVisitor, WalkVisitor {

    private int energy;
    //diseaseLevel is variable so we should create an attribute for it in order to change its value
    private int diseaseLevel;
    private Direction direction;
    private boolean moveRequested = false;

    private boolean diseased = false;
    private long diseaseStartTime = 0;

    private int insecticideCount = 0;

    public int getInsecticideCount() {
        return insecticideCount;
    }


    public Gardener(Game game, Position position) {
        super(game, position);
        this.direction = Direction.DOWN;
        this.energy = game.configuration().gardenerEnergy();
        this.diseaseLevel = game.configuration().diseaseLevel();
    }

    //Helper function which we'll use for the poisoned apple
    private void diseaseStarted() {
        diseased = true;
        diseaseStartTime = System.currentTimeMillis();
        diseaseLevel++;
    }
    public int getDiseaseLevel() {
        return diseaseLevel;
    }

    @Override
    public void pickUp(Bonus bonus) {
        if (bonus instanceof EnergyBoost) {
            if (energy + game.configuration().energyBoost() >= 100){
                energy = 101;
            }
            else{
                energy += game.configuration().energyBoost();
            }
            System.out.println("Energy boosted picked up!");
            bonus.remove();
        }
        if (bonus instanceof PoisonedApple) {
            diseaseStarted();
            System.out.println("Poisoned apple eaten!");
            bonus.remove();
        }
        if (bonus instanceof InsecticideBomb) {
            insecticideCount++;
            System.out.println("InsecticideBomb picked up!");
            bonus.remove();
        }
    }

    public int getEnergy() {
        return this.energy;
    }

    public void requestMove(Direction direction) {
        if (direction != this.direction) {
            this.direction = direction;
            setModified(true);
        }
        moveRequested = true;
    }

    @Override
    public final boolean canMove(Direction direction) {
            Position nextPos = direction.nextPosition(getPosition());
            if (!game.world().getGrid().inside(nextPos)) {
                return false;
            }

            Decor nextObj = game.world().getGrid().get(nextPos);
            if (nextObj != null && !nextObj.walkableBy(this)) {
                return false;
            }

            return true;
    }

    @Override
    public Position move(Direction direction) {
        Position nextPos = direction.nextPosition(getPosition());
        Decor next = game.world().getGrid().get(nextPos);
        setPosition(nextPos);
        if (next != null)
            next.pickUpBy(this);
        return nextPos;
    }

    public void update(long now) {
        if (diseased && (System.currentTimeMillis() - diseaseStartTime >= game.configuration().diseaseDuration())) {
            diseased = false;
            diseaseLevel--;
        }
        if (moveRequested) {
            if (canMove(direction)) {
                Position nextPos = direction.nextPosition(getPosition());
                Decor ground = game.world().getGrid().get(nextPos);
                move(direction);
                if (ground instanceof Grass) {
                    energy = energy - game.configuration().diseaseLevel();
                } else if (ground instanceof Land) {
                    energy = energy - 2*game.configuration().diseaseLevel();
                }
            }
        }
        moveRequested = false;
    }

    public void hurt(int damage) {
    }

    public void hurt() {
        hurt(1);
    }

    public Direction getDirection() {
        return direction;
    }
}
