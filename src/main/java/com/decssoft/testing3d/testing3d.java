package com.decssoft.testing3d;

import com.almasb.fxgl.app.GameApplication;
import static com.almasb.fxgl.app.GameApplication.launch;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.Camera3D;
import static com.almasb.fxgl.dsl.FXGL.getGameScene;
import static com.almasb.fxgl.dsl.FXGL.getGameWorld;
import static com.almasb.fxgl.dsl.FXGL.spawn;
import static com.almasb.fxgl.dsl.FXGLForKtKt.animationBuilder;
import static com.almasb.fxgl.dsl.FXGLForKtKt.getAssetLoader;
import static com.almasb.fxgl.dsl.FXGLForKtKt.getPhysicsWorld;
import static com.almasb.fxgl.dsl.FXGLForKtKt.onKey;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.fxgl.scene3d.Model3D;
import java.util.ArrayList;
import java.util.List;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Point3D;
import javafx.scene.input.KeyCode;
import javafx.util.Duration;

/**
 *
 * @author mis_p
 */
public class testing3d extends GameApplication {

    private static Camera3D camera;
    Entity player;
    Entity ground;
    double distance = .5;
    List<Model3D> dukesAnimation = new ArrayList<>();

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
        camera = getGameScene().getCamera3D();
        getGameWorld().addEntityFactory(new GameFactory());
        ground = spawn("floor", 0, 5, 0);
        var rightwall = spawn("wallr", 10, 0, .2);
        var leftwall = spawn("walll", -10, 0, 0.2);
        //var roof = spawn("roof", 0, -5, 0);
        player = spawn("player", 0, 4.8, 0);
        getGameScene().setFPSCamera(true);
        for (int i = 0; i <= 15; i++) {
            dukesAnimation.add(getAssetLoader().loadModel3D("java-duke" + i + ".obj"));
        }
        createAnimation();
    }

    @Override
    protected void initInput() {
        onKey(KeyCode.W, () -> {
            if (keyCycle.get() == 14) {
                keyCycle.set(0);
            }
            dukeLine.play();
            //if you want the player to hold directtion until is moved uncomment following line
            playerRotate();
            player.getTransformComponent().moveForward(.1);
            return null;
        });
        onKey(KeyCode.A, () -> {
            //if you want the player to hold directtion until is moved uncomment following line
            playerRotate();
            player.getTransformComponent().moveLeft(.1);
            return null;
        });
        onKey(KeyCode.S, () -> {
            //if you want the player to hold directtion until is moved uncomment following line
            playerRotate();
            player.getTransformComponent().moveBack(.1);
            return null;
        });
        onKey(KeyCode.D, () -> {
            //if you want the player to hold directtion until is moved uncomment following line
            playerRotate();
            player.getTransformComponent().moveRight(.1);
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
    }

    //animate duke to walk using obj files from blender and a timeline to update vertices
    IntegerProperty keyCycle = new SimpleIntegerProperty();
    KeyValue dukeStart = new KeyValue(keyCycle, 0);
    KeyValue dukeEnd = new KeyValue(keyCycle, 14);
    KeyFrame dukeKey = new KeyFrame(Duration.millis(350), dukeStart, dukeEnd);
    Timeline dukeLine = new Timeline(dukeKey);
    int cont;

    private void createAnimation() {
        dukeLine.setCycleCount(1);
        keyCycle.addListener((o) -> {
            Model3D model = (Model3D) player.getViewComponent().getChildren().get(0);
            //the below code does not update vertices, left there just as an example
//            model.getVertices().clear();
//            model.getVertices().addAll(dukesAnimation.get(keyCycle.get()).getVertices());
/////////////////////////////////////////////separator////////////////////////////////////////////////////////////////
            cont = 0;
            model.getVertices().stream()
                    .forEach(v -> {
                        v.setX(dukesAnimation.get(keyCycle.get()).getVertices().get(cont).getX());
                        v.setY(dukesAnimation.get(keyCycle.get()).getVertices().get(cont).getY());
                        v.setZ(dukesAnimation.get(keyCycle.get()).getVertices().get(cont).getZ());
                        cont++;
                    });
        });
    }

    @Override
    protected void onUpdate(double tpf) {
        double yset = distance * Math.sin(Math.toRadians(camera.getTransform().getRotationX())) + player.getY() - .1;
        double dist = distance * Math.cos(Math.toRadians(camera.getTransform().getRotationX()));
        double zset = dist * Math.cos(Math.toRadians(camera.getTransform().getRotationY())) * -1 + player.getZ();
        double xset = dist * Math.sin(Math.toRadians(camera.getTransform().getRotationY())) * -1 + player.getX();
        camera.getTransform().setPosition3D(xset, yset, zset);
        //this line makes the object to rotate along with the camera, if you comment this line and do no remove the comments
        //from inputs that move the player the 3d direction will not be updated
        //calling the rotation here means player will turn along with the camera and can only be look at from behind
        //playerRotate();
    }

    private void playerRotate() {
        //an animation to rotate the player
        animationBuilder()
                .duration(Duration.millis(50))
                .autoReverse(false)
                .rotate(player)
                .from(new Point3D(player.getTransformComponent().getRotationX(), player.getTransformComponent().getRotationY(), player.getTransformComponent().getRotationZ()))
                .to(new Point3D(0, camera.getTransform().getRotationY(), camera.getTransform().getRotationZ()))
                .buildAndPlay();
    }

    @Override
    protected void initPhysics() {
        //testing applying 3d physics since jbox is only for 2d - NOT COMPLETE
        getPhysicsWorld().setGravity(0, 10);
        getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityType.PLAYER, EntityType.WALL) {

            Point3D pos;

            // order of types is the same as passed into the constructor
            @Override
            protected void onCollisionBegin(Entity player, Entity wall) {
                pos = player.getTransformComponent().getPosition3D();
                player.getTransformComponent().setPosition3D(pos);
            }

            @Override
            protected void onCollision(Entity a, Entity b) {
                player.getTransformComponent().setPosition3D(pos);
            }
        });
    }
}
