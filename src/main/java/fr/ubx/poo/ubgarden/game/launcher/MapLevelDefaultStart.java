package fr.ubx.poo.ubgarden.game.launcher;


import static fr.ubx.poo.ubgarden.game.launcher.MapEntity.*;

public class MapLevelDefaultStart extends MapLevel {


    private final static int width = 18;
    private final static int height = 8;
        private final MapEntity[][] level1 = {
                {InsecticideBomb, Grass, Grass, Carrots, Carrots, Carrots, Grass, Flowers, Flowers, Flowers, Grass, Grass, Grass, Grass, Grass, Grass, Grass, DoorNextClosed},
                {Grass, Gardener, Grass, Carrots, Carrots, Carrots, Grass, NestWasp, Grass, Tree, Grass, Grass, Tree, Tree, Grass, Grass, Grass, Grass},
                {InsecticideBomb, Grass, Grass, Carrots, Carrots, Carrots, Grass, Grass, Grass, Grass, Grass, Grass, Grass, Tree, Grass, Grass, Grass, Grass},
                {Grass, Grass, Grass, Carrots, Carrots, Carrots, InsecticideBomb, Grass, Grass, Grass, Grass, Grass, Grass, Tree, Grass, Grass, Grass, Grass},
                {PoisonedApple, Tree, Grass, Grass, Grass, Grass, Grass, Grass, Grass, Grass, Grass, Grass, Grass, Tree, Grass, Grass, Apple, Grass},
                {Grass, Tree, Tree, Tree, Grass, Grass, Grass, Grass, Grass, NestHornet, Grass, Grass, Grass, Grass, Grass, Grass, Grass, Grass},
                {Grass, Grass, Apple, Grass, Grass, Grass, Grass, Grass, Grass, Grass, Grass, Grass, Grass, Grass, Grass, Grass, Grass, Grass},
                {Grass, Tree, Grass, Tree, Grass, Grass, Grass, Grass, Grass, Grass, Grass, Grass, Grass, Grass, Grass, Grass, Grass, Hedgehog}
        };

    public MapLevelDefaultStart() {
        super(width, height);
        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++)
                set(i, j, level1[j][i]);
    }


}
