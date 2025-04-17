package fr.ubx.poo.ubgarden.game.launcher;

import fr.ubx.poo.ubgarden.game.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.Properties;

public class GameLauncher {
    //End of file if 'x'
    final static char EOL = 'x';

    private GameLauncher() {
    }

    public static GameLauncher getInstance() {
        return LoadSingleton.INSTANCE;
    }

    private int integerProperty(Properties properties, String name, int defaultValue) {
        return Integer.parseInt(properties.getProperty(name, Integer.toString(defaultValue)));
    }

    private boolean booleanProperty(Properties properties, String name, boolean defaultValue) {
        return Boolean.parseBoolean(properties.getProperty(name, Boolean.toString(defaultValue)));
    }

    private Configuration getConfiguration(Properties properties) {

        // Load parameters
        int waspMoveFrequency = integerProperty(properties, "waspMoveFrequency", 2);
        int hornetMoveFrequency = integerProperty(properties, "hornetMoveFrequency", 1);

        int gardenerEnergy = integerProperty(properties, "gardenerEnergy", 100);
        int energyBoost = integerProperty(properties, "energyBoost", 50);
        long energyRecoverDuration = integerProperty(properties, "energyRecoverDuration", 1_000);
        int diseaseLevel = integerProperty(properties, "diseaseLevel",1);
        long diseaseDuration = integerProperty(properties, "diseaseDuration", 5_000);

        return new Configuration(gardenerEnergy, energyBoost, energyRecoverDuration, diseaseLevel, diseaseDuration, waspMoveFrequency, hornetMoveFrequency);
    }

    public Game load(File file) throws FileNotFoundException {
        int width = 0;
        int height = 0;
        Properties emptyConfig = new Properties();

        //We are going to stock in a string the characters read
        StringBuilder sb = new StringBuilder();
        try {
            Reader r = new FileReader(file);
            int entity = r.read();
            while (entity != -1) {
                sb.append((char) entity);
                entity = r.read();
            }
            System.out.println(sb.toString());

            //Calculate the width of the grid
            for (int i=0; i<sb.length(); i++) {
                //EOL character is 'x'
                if (sb.charAt(i) == EOL) {
                    break;
                } else {
                    width++;
                }
            }

            //Calculate the height of the grid
            for (int i=0; i<sb.length(); i++) {
                //EOL character is 'x'
                if (sb.charAt(i) == EOL) {
                    height++;
                }
            }
            System.out.println("width: " + width + " height: " + height);
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }

        MapLevel mapLevel = new MapLevel(width, height);

        //We create the array read
        MapEntity[][] loadedGrid = new MapEntity[height][width];
        for(int i=0; i<height; i++) {
            for (int j = 0; j<width; j++) {
                loadedGrid[i][j] = MapEntity.fromCode(sb.charAt((i*(width+1))+j));
            }
        }

        //We should set the grid
        for(int i=0; i<height; i++) {
            for (int j=0; j<width; j++) {
                mapLevel.set(j,i,loadedGrid[i][j]);
            }
        }

        Position gardenerPosition = mapLevel.getGardenerPosition();
        if (gardenerPosition == null)
            throw new RuntimeException("Gardener not found");
        Configuration configuration = getConfiguration(emptyConfig);
        World world = new World(1);
        Game game = new Game(world, configuration, gardenerPosition);
        Map level = new Level(game, 1, mapLevel);
        world.put(1, level);
        return game;
    }

    public Game load() {
        Properties emptyConfig = new Properties();
        MapLevel mapLevel = new MapLevelDefaultStart();
        Position gardenerPosition = mapLevel.getGardenerPosition();
        if (gardenerPosition == null)
            throw new RuntimeException("Gardener not found");
        Configuration configuration = getConfiguration(emptyConfig);
        World world = new World(1);
        Game game = new Game(world, configuration, gardenerPosition);
        Map level = new Level(game, 1, mapLevel);
        world.put(1, level);
        return game;
    }

    private static class LoadSingleton {
        static final GameLauncher INSTANCE = new GameLauncher();
    }

}
