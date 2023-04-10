package com.decssoft.testing3d;

import com.almasb.fxgl.app.GameApplication;
import static com.almasb.fxgl.app.GameApplication.launch;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.Camera3D;
import com.almasb.fxgl.app.scene.LoadingScene;
import com.almasb.fxgl.app.scene.SceneFactory;
import static com.almasb.fxgl.dsl.FXGL.animationBuilder;
import static com.almasb.fxgl.dsl.FXGL.entityBuilder;
import static com.almasb.fxgl.dsl.FXGL.getGameScene;
import static com.almasb.fxgl.dsl.FXGL.getGameWorld;
import static com.almasb.fxgl.dsl.FXGL.getInput;
import static com.almasb.fxgl.dsl.FXGL.getPhysicsWorld;
import static com.almasb.fxgl.dsl.FXGL.onKey;
import static com.almasb.fxgl.dsl.FXGL.runOnce;
import static com.almasb.fxgl.dsl.FXGL.spawn;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.physics.CollisionHandler;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.PointLight;
import javafx.scene.input.KeyCode;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.util.Duration;

/**
 *
 * @author mis_p
 */
public class testing3d extends GameApplication {

    private static Camera3D camera;
    Entity player;
    Entity ground;
    Entity ambient;
    double distance = .5;
    GameFactory factory = new GameFactory();
    DukeAnimation animation;
    boolean aim = false;
    Entity enemy;
    PointLight lightning = new PointLight(Color.RED);
    boolean ahead = false;
    boolean right = false;
    boolean left = false;
    boolean back = false;

    public static void main(String args[]) {
        launch(args);
    }

    @Override
    protected void initSettings(GameSettings settings) {
        settings.set3D(true);
        settings.setWidth(1280);
        settings.setHeight(720);
        settings.setSceneFactory(new SceneFactory() {
            @Override
            public LoadingScene newLoadingScene() {
                return new MyLoadingScene();
            }
        });
        settings.setMainMenuEnabled(true);
//        settings.setIntroEnabled(true);
    }

    @Override
    protected void initGame() {
        animation = new DukeAnimation();
        getGameWorld().addEntityFactory(factory);
        player = spawn("player", 0, 4.8, 0);
        animation.createAnimation(player);
        camera = getGameScene().getCamera3D();
        ground = spawn("floor", 0, 5, 0);
        var rightwall = spawn("wallr", 10, 0, .2);
        var leftwall = spawn("walll", -10, 0, 0.2);
        enemy = spawn("enemy", 0, 4.8, 1);
        //var roof = spawn("roof", 0, -5, 0);
        getGameScene().setFPSCamera(true);
        PointLight spot = new PointLight(Color.WHITE);
        spot.setTranslateX(7);
        spot.setTranslateY(-3);
        spot.setTranslateZ(6);
        spot.setConstantAttenuation(5);
        lightning.setLightOn(false);
        lightning.setTranslateX(-7);
        lightning.setTranslateY(-3);
        lightning.setTranslateZ(6);
        Group g = new Group(lightning, spot);
        getGameScene().addChild(g);
        ambient = entityBuilder()
                .view(g)
                .buildAndAttach();
    }

    @Override
    protected void initInput() {
        getInput().addAction(new UserAction("goahead") {
            @Override
            protected void onActionBegin() {
                ahead = true;
                animation.getTimeline().play();
                playerRotate();
            }

            @Override
            protected void onAction() {
                animation.getTimeline().play();
                playerRotate();
            }

            @Override
            protected void onActionEnd() {
                ahead = false;
            }
        }, KeyCode.W);

        getInput().addAction(new UserAction("goback") {
            @Override
            protected void onActionBegin() {
                back = true;
                animation.getTimeline().play();
                playerTurn();
            }

            @Override
            protected void onAction() {
                animation.getTimeline().play();
            }

            @Override
            protected void onActionEnd() {
                back = false;
            }
        }, KeyCode.S);

        getInput().addAction(new UserAction("goright") {
            double y;

            @Override
            protected void onActionBegin() {
                right = true;
                animation.getTimeline().play();
                playerRotateRight(y);
            }

            @Override
            protected void onAction() {
                double yn = ahead ? y - 45 : y;
                yn = back ? +180 : yn;
                animation.getTimeline().play();
                playerRotateRight(yn);
            }

            @Override
            protected void onActionEnd() {
                right = false;
            }
        }, KeyCode.D);

        getInput().addAction(new UserAction("goleft") {
            @Override
            protected void onActionBegin() {
                left = true;
                animation.getTimeline().play();
                playerRotateLeft();
            }

            @Override
            protected void onAction() {
                playerRotateLeft();
                animation.getTimeline().play();
            }

            @Override
            protected void onActionEnd() {
                left = false;
            }
        }, KeyCode.A);

        onKey(KeyCode.T, () -> {
            aim = true;
        });

        getInput().addAction(new UserAction("jump") {
            @Override
            protected void onActionBegin() {
                player.getComponent(DukeAnimation.class).setheight(4.6);
            }

            @Override
            protected void onActionEnd() {
                player.getComponent(DukeAnimation.class).setheight(4.8);
            }
        }, KeyCode.SPACE);

        getGameScene().getRoot().addEventHandler(ScrollEvent.SCROLL, (s) -> {
            if (s.getDeltaY() < 0) {
                if (distance < 3) {
                    distance += .040;
                }
            }
            if (s.getDeltaY() > 0) {
                if (distance > .26) {
                    distance -= .040;
                }
            }
        });
    }

    @Override
    protected void onUpdate(double tpf) {
        if (!aim) {
            double yset = distance * Math.sin(Math.toRadians(camera.getTransform().getRotationX())) + player.getY() - .1;
            double dist = distance * Math.cos(Math.toRadians(camera.getTransform().getRotationX()));
            double zset = dist * Math.cos(Math.toRadians(camera.getTransform().getRotationY())) * -1 + player.getZ();
            double xset = dist * Math.sin(Math.toRadians(camera.getTransform().getRotationY())) * -1 + player.getX();
            camera.getTransform().setPosition3D(xset, yset, zset);
        } else {
            double zsetp = .15 * Math.cos(Math.toRadians(camera.getTransform().getRotationY() - 90)) * -1 + player.getZ();
            double xsetp = .15 * Math.sin(Math.toRadians(camera.getTransform().getRotationY() - 90)) * -1 + player.getX();
            double yset = .15 * Math.sin(Math.toRadians(camera.getTransform().getRotationX())) + player.getY() - .15;
            double dist = .15 * Math.cos(Math.toRadians(camera.getTransform().getRotationX()));
            double zset = dist * Math.cos(Math.toRadians(camera.getTransform().getRotationY())) * -1 + zsetp;
            double xset = dist * Math.sin(Math.toRadians(camera.getTransform().getRotationY())) * -1 + xsetp;
            camera.getTransform().setPosition3D(xset, player.getY() - .15, zset);
            playerRotate();
        }
        aim = false;
//        playerRotate();
        //this line makes the object to rotate along with the camera, if you comment this line and do no remove the comments
        //from inputs that move the player the 3d direction will not be updated
        //calling the rotation here means player will turn along with the camera and can only be look at from behind
        //playerRotate();
    }

    private void playerRotate() {
        //an animation to rotate the player
        double x = aim ? camera.getTransform().getRotationX() : 0;
        animationBuilder()
                .duration(Duration.millis(100))
                .autoReverse(false)
                .rotate(player)
                .from(new Point3D(player.getTransformComponent().getRotationX(), player.getTransformComponent().getRotationY(), player.getTransformComponent().getRotationZ()))
                .to(new Point3D(x, camera.getTransform().getRotationY(), camera.getTransform().getRotationZ()))
                .buildAndPlay();
    }

    private void playerRotateRight(double direction) {
        var y = ahead ? 45 : 90;
        y = back ? -225 : y;
        y = player.getTransformComponent().getRotationY() <= -224 && !back ? -270 : y;
        System.out.println(y);
        animationBuilder()
                .duration(Duration.millis(100))
                .autoReverse(false)
                .rotate(player)
                .from(new Point3D(player.getTransformComponent().getRotationX(), player.getTransformComponent().getRotationY(), player.getTransformComponent().getRotationZ()))
                .to(new Point3D(0, camera.getTransform().getRotationY() + y, camera.getTransform().getRotationZ()))
                .buildAndPlay();
    }

    private void playerRotateLeft() {
        //an animation to rotate the player
        var y = ahead ? 45 : 90;
        y = back ? 135 : y;
        animationBuilder()
                .duration(Duration.millis(100))
                .autoReverse(false)
                .rotate(player)
                .from(new Point3D(player.getTransformComponent().getRotationX(), player.getTransformComponent().getRotationY(), player.getTransformComponent().getRotationZ()))
                .to(new Point3D(0, camera.getTransform().getRotationY() - y, camera.getTransform().getRotationZ()))
                .buildAndPlay();
    }

    private void playerTurn() {
        //an animation to rotate the player
        animationBuilder()
                .duration(Duration.millis(100))
                .autoReverse(false)
                .rotate(player)
                .from(new Point3D(player.getTransformComponent().getRotationX(), player.getTransformComponent().getRotationY(), player.getTransformComponent().getRotationZ()))
                .to(new Point3D(0, camera.getTransform().getRotationY() - 180, camera.getTransform().getRotationZ()))
                .buildAndPlay();
    }

    @Override
    protected void initPhysics() {
        //testing applying 3d physics since jbox is only for 2d - NOT COMPLETE
        getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityType.PLAYER, EntityType.FLOOR) {

            // order of types is the same as passed into the constructor
            @Override
            protected void onCollisionBegin(Entity player, Entity flor) {
                player.setY(flor.getY() - flor.getHeight() / 2);
            }

            @Override
            protected void onCollision(Entity a, Entity b) {
                //player.getTransformComponent().setPosition3D(pos);
            }
        });

        getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityType.PLAYER, EntityType.ENEMY) {

            // order of types is the same as passed into the constructor
            @Override
            protected void onCollisionBegin(Entity player, Entity enemy) {
                player.getTransformComponent().lookAt(enemy.getPosition3D());
                player.getTransformComponent().moveBack(.15);
            }

            @Override
            protected void onCollision(Entity player, Entity enemy) {
                lightning.setLightOn(true);
            }

            @Override
            protected void onCollisionEnd(Entity a, Entity b) {
                runOnce(() -> {
                    lightning.setLightOn(false);
                }, Duration.millis(200));
            }
        });
        getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityType.PLAYER, EntityType.WALLL) {

            // order of types is the same as passed into the constructor
            @Override
            protected void onCollisionBegin(Entity player, Entity walll) {

            }

            @Override
            protected void onCollision(Entity player, Entity walll) {

            }

            @Override
            protected void onCollisionEnd(Entity player, Entity walll) {
//                player.setX(walll.getX() + walll.getWidth() / 2 + player.getWidth() / 2);
            }
        });
    }

}
