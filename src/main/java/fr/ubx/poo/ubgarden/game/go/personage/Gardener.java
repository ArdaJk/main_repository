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
import fr.ubx.poo.ubgarden.game.go.bonus.*;
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

    //We create this attribute to test if all the carrots are collected
    private int carrotsCollected = 0;

    private final int maxEnergy = 100;

    private long EnergyRecoveryTime = 1;
    private boolean death = false;

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
            int boost = game.configuration().energyBoost();
            energy += boost;
            if (energy > game.configuration().gardenerEnergy()) {
                energy = game.configuration().gardenerEnergy();
            }
            System.out.println("Energy boost picked up!");
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
        if (bonus instanceof CollectCarrot) {
            carrotsCollected++;
            System.out.println("Carrot collected!");
            bonus.remove();
            if (carrotsCollected == game.world().getGrid().getCarrots()) {
                System.out.println("All Carrots Collected");
                Position doorPos = game.world().getGrid().doorPosition();
                System.out.println(game.world().getGrid().values());
                Door door = (Door) game.world().getGrid().get(doorPos);
                door.setClosed(false);
                door.setModified(true);
            }
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
            if (getEnergy() <= 0) {
                return false;
            }
            return true;
    }

    @Override
    public Position move(Direction direction) {
        Position nextPos = direction.nextPosition(getPosition());
        Decor next = game.world().getGrid().get(nextPos);
        if (next instanceof Door door && !door.isClosed()) {
            int nextLevel = game.world().currentLevel() + 1;
            if (game.hasLevel(nextLevel)) {
                game.requestSwitchLevel(nextLevel);
            } else {
                System.out.println("Next level not found");
            }
            return getPosition();
        }

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
                Decor ground = game.world().getGrid().get(getPosition());
                energy = energy - ground.energyConsumptionWalk()*diseaseLevel;
                move(direction);
            }
        }
        else {
            long time = System.currentTimeMillis();
            if (time - EnergyRecoveryTime >= game.configuration().energyRecoverDuration()) {
                if (energy < maxEnergy) {
                    energy += 1;
                }
                EnergyRecoveryTime = time;
            }
        }
        moveRequested = false;
    }

    public void hurt(int damage) {
        if (damage >= energy) {
            death = true;
        } else {
            this.energy = energy - damage;
        }
    }

    public void hurt() {
        hurt(1);
    }

    public boolean isDeath() {
        return death;
    }

    public Direction getDirection() {
        return direction;
    }

    public void useBomb() {
        insecticideCount--;
    }
}
