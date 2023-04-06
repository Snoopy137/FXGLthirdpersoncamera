package com.decssoft.testing3d;

import com.almasb.fxgl.app.GameApplication;
import static com.almasb.fxgl.app.GameApplication.launch;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.Camera3D;
import static com.almasb.fxgl.dsl.FXGL.getGameScene;
import static com.almasb.fxgl.dsl.FXGL.getGameWorld;
import static com.almasb.fxgl.dsl.FXGL.spawn;
import static com.almasb.fxgl.dsl.FXGLForKtKt.animationBuilder;
import static com.almasb.fxgl.dsl.FXGLForKtKt.entityBuilder;
import static com.almasb.fxgl.dsl.FXGLForKtKt.getPhysicsWorld;
import static com.almasb.fxgl.dsl.FXGLForKtKt.onKey;
import com.almasb.fxgl.entity.Entity;
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
//    List<Model3D> dukesAnimation = new ArrayList<>();

    public static void main(String args[]) {
        launch(args);
    }

    @Override
    protected void initSettings(GameSettings settings) {
        settings.set3D(true);
        settings.setWidth(1280);
        settings.setHeight(720);
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
        enemy = spawn("enemy", 0, 4.8, 2);
        //var roof = spawn("roof", 0, -5, 0);
        getGameScene().setFPSCamera(true);
        PointLight spot = new PointLight(Color.WHITE);
        spot.setTranslateX(2);
        spot.setTranslateY(-3);
        spot.setTranslateZ(6);
        Group g = new Group(spot);
        getGameScene().addChild(g);
        ambient = entityBuilder()
                .view(g)
                .buildAndAttach();
    }

    @Override
    protected void initInput() {
        onKey(KeyCode.W, () -> {
            //if you want the player to hold directtion until is moved uncomment following line
            animation.getTimeline().play();
            playerRotate();
            System.out.println("distance " + player.getTransformComponent().distance3D(enemy.getTransformComponent()));
            player.getTransformComponent().moveForward(.05);
            return null;
        });
        onKey(KeyCode.A, () -> {
            //if you want the player to hold directtion until is moved uncomment following line
            playerRotate();
            player.getTransformComponent().moveLeft(.05);
            return null;
        });
        onKey(KeyCode.S, () -> {
            //if you want the player to hold directtion until is moved uncomment following line
            playerRotate();
            player.getTransformComponent().moveBack(.05);
            return null;
        });
        onKey(KeyCode.D, () -> {
            //if you want the player to hold directtion until is moved uncomment following line
            playerRotate();
            player.getTransformComponent().moveRight(.05);
            return null;
        });
        onKey(KeyCode.E, () -> {
            //zoom out from the object
            if (distance < 3) {
                distance += .015;
            }
            return null;
        });
        onKey(KeyCode.Q, () -> {
            //zoom in to the object
            if (distance > .16) {
                distance -= .015;
            }
            return null;
        });
        onKey(KeyCode.T, () -> {
            aim = true;
            return null;
        });

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

    //animate duke to walk using obj files from blender and a timeline to update vertices
//    IntegerProperty keyCycle = new SimpleIntegerProperty();
//    KeyValue dukeStart = new KeyValue(keyCycle, 0);
//    KeyValue dukeEnd = new KeyValue(keyCycle, 14);
//    KeyFrame dukeKey = new KeyFrame(Duration.millis(350), dukeStart, dukeEnd);
//    Timeline dukeLine = new Timeline(dukeKey);
//    int cont;
//    private void createAnimation() {
//        dukeLine.setCycleCount(1);
//        keyCycle.addListener((o) -> {
//            Model3D model = (Model3D) player.getViewComponent().getChildren().get(0);
//            //the below code does not update vertices, left there just as an example
////            model.getVertices().clear();
////            model.getVertices().addAll(dukesAnimation.get(keyCycle.get()).getVertices());
///////////////////////////////////////////////separator////////////////////////////////////////////////////////////////
//            cont = 0;
//            model.getVertices().stream()
//                    .forEach(v -> {
//                        v.setX(dukesAnimation.get(keyCycle.get()).getVertices().get(cont).getX());
//                        v.setY(dukesAnimation.get(keyCycle.get()).getVertices().get(cont).getY());
//                        v.setZ(dukesAnimation.get(keyCycle.get()).getVertices().get(cont).getZ());
//                        cont++;
//                    });
//        });
//    }
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
            //0.0 -3.061616997868383E-17 -90.0
//            System.out.println(yset);
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
                System.out.println(player.getZ() + " " + enemy.getZ());
            }

            @Override
            protected void onCollision(Entity a, Entity b) {
                //player.getTransformComponent().setPosition3D(pos);
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
