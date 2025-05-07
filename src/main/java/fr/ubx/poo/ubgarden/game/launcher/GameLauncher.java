package fr.ubx.poo.ubgarden.game.launcher;

import fr.ubx.poo.ubgarden.game.*;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    private String decompress(String data) {
        StringBuilder result = new StringBuilder();
        int i = 0;
        while (i < data.length()) {
            char current = data.charAt(i);
            if (!Character.isLetter(current) && current != '+' && current != '-' && current != '>') {
                throw new RuntimeException("Invalid character before count: '" + current + "' at index " + i);
            }
            int count = 0;
            i++;
            while (i < data.length() && Character.isDigit(data.charAt(i))) {
                count = count * 10 + (data.charAt(i++) - '0');
            }
            if (count == 0) count = 1;
            result.append(String.valueOf(current).repeat(count));
        }

        return result.toString();
    }


    public Game load(File file) throws FileNotFoundException {
        int width = 0;
        int height = 0;
        Properties props = new Properties();
        //We are going to stock in a string the characters read
        StringBuilder sb = new StringBuilder();

        try {
            Reader r = new FileReader(file);
            int entity = r.read();
            while (entity != -1) {
                sb.append((char) entity);
                entity = r.read();
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading file", e);
        }

        // Load Properties from the string content
        try {
            props.load(new java.io.StringReader(sb.toString()));
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse properties", e);
        }

        boolean compression = Boolean.parseBoolean(props.getProperty("compression", "false"));
        int levels = Integer.parseInt(props.getProperty("levels", "1"));

        //Read the values from the properties format
        int gardenerEnergy = Integer.parseInt(props.getProperty("gardenerEnergy", "100"));
        int energyBoost = Integer.parseInt(props.getProperty("energyBoost", "50"));
        int energyRecoverDuration = Integer.parseInt(props.getProperty("energyRecoverDuration", "1000"));
        int diseaseLevel = Integer.parseInt(props.getProperty("diseaseLevel", "1"));
        int diseaseDuration = Integer.parseInt(props.getProperty("diseaseDuration", "5000"));
        int waspMoveFrequency = Integer.parseInt(props.getProperty("waspMoveFrequency", "2"));
        int hornetMoveFrequency = Integer.parseInt(props.getProperty("hornetMoveFrequency", "1"));

        List<String> levelStrings = new ArrayList<>();
        for (int i = 1; i <= levels; i++) {
            String levelData = props.getProperty("level" + i);
            if (levelData == null)
                throw new RuntimeException("Missing level" + i);
            if (compression)
                levelData = decompress(levelData);
            levelStrings.add(levelData);
        }

        String level1 = levelStrings.get(0);

        //Calculate the width of the first level
        for (int i=0; i<level1.length(); i++) {
            //EOL character is 'x'
            if (level1.charAt(i) == EOL) {
                break;
            } else {
                width++;
            }
        }

        //Calculate the height of the first level
        for (int i=0; i<level1.length(); i++) {
            //EOL character is 'x'
            if (level1.charAt(i) == EOL) {
                height++;
            }
        }
        MapLevel mapLevel = new MapLevel(width,height);

        //We create the array read
        MapEntity[][] loadedGrid = new MapEntity[height][width];
        String[] lines = level1.split("x");
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                mapLevel.set(j, i, MapEntity.fromCode(lines[i].charAt(j)));
            }
        }

        Position gardenerPosition = mapLevel.getGardenerPosition();
        if (gardenerPosition == null)
            throw new RuntimeException("Gardener not found");
        Configuration configuration = new Configuration(gardenerEnergy, energyBoost, energyRecoverDuration,diseaseLevel,diseaseDuration,waspMoveFrequency,hornetMoveFrequency);
        World world = new World(levels);
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
