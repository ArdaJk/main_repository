package fr.ubx.poo.ubgarden.game;

import fr.ubx.poo.ubgarden.game.go.GameObject;
import fr.ubx.poo.ubgarden.game.go.personage.Gardener;

import java.util.ArrayList;
import java.util.List;


public class Game {

    private final Configuration configuration;
    private final World world;
    private final Gardener gardener;
    private List<GameObject> bugs = new ArrayList<>();
    private int nbBugs = 0;

    private boolean switchLevelRequested = false;
    private int switchLevel;

    public Game(World world, Configuration configuration, Position gardenerPosition) {
        this.configuration = configuration;
        this.world = world;
        gardener = new Gardener(this, gardenerPosition);
    }

    public Configuration configuration() {
        return configuration;
    }

    public Gardener getGardener() {
        return this.gardener;
    }

    public World world() {
        return world;
    }

    public boolean isSwitchLevelRequested() {
        return switchLevelRequested;
    }

    public int getSwitchLevel() {
        return switchLevel;
    }

    public void requestSwitchLevel(int level) {
        this.switchLevel = level;
        switchLevelRequested = true;
    }

    public void clearSwitchLevel() {
        switchLevelRequested = false;
    }

    public void addBug(GameObject bug) {
        bugs.add(bug);
        nbBugs++;
    }

    public void removeBug(GameObject bug) {
        bugs.remove(bug);
        nbBugs--;
    }

    public List<GameObject> getBugs() {
        return bugs;
    }

    public int getNbBugs() {
        return nbBugs;
    }

}
