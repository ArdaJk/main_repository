package fr.ubx.poo.ubgarden.game;

import fr.ubx.poo.ubgarden.game.go.bonus.CollectCarrot;
import fr.ubx.poo.ubgarden.game.go.bonus.PoisonedApple;
import fr.ubx.poo.ubgarden.game.go.bonus.EnergyBoost;
import fr.ubx.poo.ubgarden.game.go.bonus.InsecticideBomb;
import fr.ubx.poo.ubgarden.game.go.decor.*;
import fr.ubx.poo.ubgarden.game.go.decor.ground.Grass;
import fr.ubx.poo.ubgarden.game.go.decor.Hedgehog;
import fr.ubx.poo.ubgarden.game.go.decor.ground.Land;
import fr.ubx.poo.ubgarden.game.launcher.MapEntity;
import fr.ubx.poo.ubgarden.game.launcher.MapLevel;
import javafx.geometry.Pos;

import java.util.Collection;
import java.util.HashMap;



public class Level implements Map {

    private final int level;
    private final int width;
    private final int height;

    private int carrotsToCollect = 0;

    private final java.util.Map<Position, Decor> decors = new HashMap<>();

    public Level(Game game, int level, MapLevel entities) {
        this.level = level;
        this.width = entities.width();
        this.height = entities.height();

        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++) {
                Position position = new Position(level, i, j);
                MapEntity mapEntity = entities.get(i, j);
                switch (mapEntity) {
                    case Grass:
                        decors.put(position, new Grass(position));
                        break;
                    case Tree:
                        decors.put(position, new Tree(position));
                        break;
                    case Apple:
                        Decor grassApple = new Grass(position);
                        grassApple.setBonus(new EnergyBoost(position, grassApple));
                        decors.put(position, grassApple);
                        break;
                    case Carrots:
                        Decor landWithCarrots = new Land(position);
                        landWithCarrots.setBonus(new CollectCarrot(position, landWithCarrots));
                        decors.put(position, landWithCarrots);
                        carrotsToCollect++;
                        break;
                    case PoisonedApple:
                        Decor grassPoisonedApple = new Grass(position);
                        grassPoisonedApple.setBonus(new PoisonedApple(position, grassPoisonedApple));
                        decors.put(position, grassPoisonedApple);
                        break;
                    case InsecticideBomb:
                        Decor grassBomb = new Grass(position);
                        grassBomb.setBonus(new InsecticideBomb(position, grassBomb));
                        decors.put(position, grassBomb);
                        break;
                    case Flowers:
                        decors.put(position, new Flowers(position));
                        break;
                    case NestWasp:
                        decors.put(position, new NestWasp(position));
                        break;
                    case NestHornet:
                        decors.put(position, new NestHornet(position));
                        break;
                    case DoorNextClosed:
                        decors.put(position, new DoorClosed(position));
                        break;
                    case DoorNextOpened:
                        decors.put(position, new DoorOpened(position));
                        break;
                    case Hedgehog:
                        decors.put(position, new Hedgehog(position));
                        break;
                    default:
                        throw new RuntimeException("EntityCode " + mapEntity.name() + " not processed");
                }
            }
    }

    @Override
    public int width() {
        return this.width;
    }

    @Override
    public int height() {
        return this.height;
    }

    public Decor get(Position position) {
        return decors.get(position);
    }

    public void set(Position position, Decor decor) {
        decors.put(position, decor);
    }

    public Collection<Decor> values() {
        return decors.values();
    }


    @Override
    public boolean inside(Position position) {
        return position.x() >= 0 && position.x() < width &&
                position.y() >= 0 && position.y() < height;
    }

    @Override
    public int getCarrots() {
        return carrotsToCollect;
    }

    @Override
    public Position doorPosition() {
        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++) {
                if (this.get(new Position(level, i, j)) instanceof DoorClosed) {
                    return new Position(level,i,j);
            }
        }
        return null;
    }


}
