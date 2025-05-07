    /*
     * Copyright (c) 2020. Laurent Réveillère
     */

    package fr.ubx.poo.ubgarden.game.engine;

    import fr.ubx.poo.ubgarden.game.Direction;
    import fr.ubx.poo.ubgarden.game.Game;
    import fr.ubx.poo.ubgarden.game.go.GameObject;
    import fr.ubx.poo.ubgarden.game.go.bonus.InsecticideBomb;
    import fr.ubx.poo.ubgarden.game.go.decor.Hedgehog;
    import fr.ubx.poo.ubgarden.game.go.personage.Bugs;
    import fr.ubx.poo.ubgarden.game.go.personage.Gardener;
    import fr.ubx.poo.ubgarden.game.go.personage.Hornet;
    import fr.ubx.poo.ubgarden.game.go.personage.Wasp;
    import fr.ubx.poo.ubgarden.game.Position;
    import fr.ubx.poo.ubgarden.game.view.*;
    import javafx.animation.AnimationTimer;
    import javafx.application.Platform;
    import javafx.scene.Group;
    import javafx.scene.Scene;
    import javafx.scene.image.Image;
    import javafx.scene.layout.Pane;
    import javafx.scene.layout.StackPane;
    import javafx.scene.paint.Color;
    import javafx.scene.text.Font;
    import javafx.scene.text.Text;
    import javafx.scene.text.TextAlignment;

    import java.util.*;


    public final class GameEngine {

        private static AnimationTimer gameLoop;
        private final Game game;
        private final Gardener gardener;
        private final List<Sprite> sprites = new LinkedList<>();
        private final Set<Sprite> cleanUpSprites = new HashSet<>();

        private final Scene scene;

        private StatusBar statusBar;

        private final Pane rootPane = new Pane();
        private final Group root = new Group();
        private final Pane layer = new Pane();
        private Input input;

        public GameEngine(Game game, Scene scene) {
            this.game = game;
            this.scene = scene;
            this.gardener = game.getGardener();
            initialize();
            buildAndSetGameLoop();
        }

        public Pane getRoot() {
            return rootPane;
        }

        private void initialize() {
            int height = game.world().getGrid().height();
            int width = game.world().getGrid().width();
            int sceneWidth = width * ImageResource.size;
            int sceneHeight = height * ImageResource.size;
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/application.css")).toExternalForm());
            input = new Input(scene);

            root.getChildren().clear();
            root.getChildren().add(layer);
            statusBar = new StatusBar(root, sceneWidth, sceneHeight);

            rootPane.getChildren().clear();
            rootPane.setPrefSize(sceneWidth, sceneHeight + StatusBar.height);
            rootPane.getChildren().add(root);

            // Create sprites
            int currentLevel = game.world().currentLevel();

            for (var decor : game.world().getGrid().values()) {
                sprites.add(SpriteFactory.create(layer, decor));
                decor.setModified(true);
                var bonus = decor.getBonus();
                if (bonus != null) {
                    sprites.add(SpriteFactory.create(layer, bonus));
                    bonus.setModified(true);
                }
            }

            sprites.add(new SpriteGardener(layer, gardener));
            resizeScene(sceneWidth, sceneHeight);
        }

        void buildAndSetGameLoop() {
            gameLoop = new AnimationTimer() {
                public void handle(long now) {
                    checkLevel();

                    // Check keyboard actionsolur g
                    processInput();

                    // Do actions
                    update(now);
                    checkCollision();

                    // Graphic update
                    cleanupSprites();
                    render();
                    statusBar.update(game);
                }
            };
        }


        private void checkLevel() {
            if (game.isSwitchLevelRequested()) {
                // Find the new level to switch to
                // clear all sprites
                // change the current level
                // Find the position of the door to reach
                // Set the position of the gardener
                // initialize();
                sprites.clear();
                cleanUpSprites.clear();
                game.getBugs().clear();
                game.resetNbBugs();
                int nextLevel = game.getSwitchLevel();
                game.world().setCurrentLevel(nextLevel);
                Position newPos = game.world().getGrid().doorPrevPosition();
                game.getGardener().setPosition(newPos);
                game.clearSwitchLevel();
                initialize();
            }
        }

        private void checkCollision() {
            // Check a collision between the gardener and a wasp or an hornet
            for (GameObject bug : game.getBugs()) {
                Bugs realBug = (Bugs) bug;
                if (realBug.getPosition().equals(game.getGardener().getPosition()) && realBug instanceof Wasp) {
                    if (gardener.getInsecticideCount() == 0) {
                        gardener.hurt(realBug.getDamage());
                        realBug.hurt();
                    } else {
                        realBug.hurt();
                        gardener.useBomb();
                    }
                }
            }
        }

        private void processInput() {
            if (input.isExit()) {
                gameLoop.stop();
                Platform.exit();
                System.exit(0);
            } else if (input.isMoveDown()) {
                gardener.requestMove(Direction.DOWN);
            } else if (input.isMoveLeft()) {
                gardener.requestMove(Direction.LEFT);
            } else if (input.isMoveRight()) {
                gardener.requestMove(Direction.RIGHT);
            } else if (input.isMoveUp()) {
                gardener.requestMove(Direction.UP);
            }
            input.clear();
        }

        private void showMessage(String msg, Color color) {
            Text message = new Text(msg);
            message.setTextAlignment(TextAlignment.CENTER);
            message.setFont(new Font(60));
            message.setFill(color);

            StackPane pane = new StackPane(message);
            pane.setPrefSize(rootPane.getWidth(), rootPane.getHeight());
            rootPane.getChildren().clear();
            rootPane.getChildren().add(pane);

            new AnimationTimer() {
                public void handle(long now) {
                    processInput();
                }
            }.start();
        }

        private void update(long now) {
            game.world().getGrid().values().forEach(decor -> decor.update(now));
            gardener.update(now);

            if (gardener.isDeath()) {
                gameLoop.stop();
                showMessage("Perdu!", Color.RED);
            }

            if (game.world().getGrid().get(gardener.getPosition()) instanceof Hedgehog) {
                gameLoop.stop();
                showMessage("Gagné!",Color.GREEN);
            }

            checkCollision();

            if (game.getNbBugs()>0) {
                for (int i=0; i<game.getNbBugs(); i++) {
                    game.getBugs().get(i).update(now);
                }
            }

            for (var decor : game.world().getGrid().values()) {
                if (decor.getBonus() instanceof InsecticideBomb &&
                        sprites.stream().noneMatch(s -> s.getGameObject() == decor.getBonus())) {
                    sprites.add(SpriteFactory.create(layer, decor.getBonus()));
                    decor.getBonus().setModified(true);
                }
            }

            for (int i=0; i<game.getNbBugs(); i++) {
                if (game.getBugs().get(i) instanceof Wasp && !(((Wasp) game.getBugs().get(i)).getHasSprite())) {
                    sprites.add(new SpriteWasp(layer, (Wasp) game.getBugs().get(i)));
                    //Now we created the sprite
                    ((Wasp) game.getBugs().get(i)).setHasSprite(true);
                    render();
                } else if (game.getBugs().get(i) instanceof Hornet && !(((Hornet) game.getBugs().get(i)).getHasSprite())) {
                    sprites.add(new SpriteHornet(layer, (Hornet) game.getBugs().get(i)));
                    //Now we created the sprite
                    ((Hornet) game.getBugs().get(i)).setHasSprite(true);
                    render();
                }
            }
        }

        public void cleanupSprites() {
            sprites.forEach(sprite -> {
                if (sprite.getGameObject().isDeleted()) {
                    cleanUpSprites.add(sprite);
                }
            });
            cleanUpSprites.forEach(Sprite::remove);
            sprites.removeAll(cleanUpSprites);
            cleanUpSprites.clear();
        }

        private void render() {
            sprites.forEach(Sprite::render);
        }

        public void start() {
            gameLoop.start();
        }

        private void resizeScene(int width, int height) {
            rootPane.setPrefSize(width, height + StatusBar.height);
            layer.setPrefSize(width, height);
            Platform.runLater(() -> scene.getWindow().sizeToScene());
        }
    }