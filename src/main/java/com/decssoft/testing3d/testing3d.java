package com.decssoft.testing3d;

import com.almasb.fxgl.app.GameApplication;
import static com.almasb.fxgl.app.GameApplication.launch;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.Camera3D;
import static com.almasb.fxgl.dsl.FXGL.getGameScene;
import static com.almasb.fxgl.dsl.FXGL.getGameWorld;
import static com.almasb.fxgl.dsl.FXGL.spawn;
import static com.almasb.fxgl.dsl.FXGLForKtKt.animationBuilder;
import static com.almasb.fxgl.dsl.FXGLForKtKt.getPhysicsWorld;
import static com.almasb.fxgl.dsl.FXGLForKtKt.onKey;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.CollisionHandler;
import javafx.geometry.Point3D;
import javafx.scene.input.KeyCode;
import javafx.util.Duration;

public class testing3d extends GameApplication {

    private Camera3D camera;
    double cameraY = 0;
    Entity player;
    Entity ground;
    double distance = 7;

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
        var roof = spawn("roof", 0, -5, 0);
        player = spawn("player", 0, 0, 0);
        getGameScene().setFPSCamera(true);
    }

    @Override
    protected void initInput() {
        onKey(KeyCode.W, () -> {
            //if you want the player to hold directtion until is moved uncomment following line
            //playerRotate();
            player.getTransformComponent().moveForward(0.5);
            return null;
        });
        onKey(KeyCode.A, () -> {
            //if you want the player to hold directtion until is moved uncomment following line
            //playerRotate();
            player.getTransformComponent().moveLeft(.5);
            return null;
        });
        onKey(KeyCode.S, () -> {
            //if you want the player to hold directtion until is moved uncomment following line
            //playerRotate();
            player.getTransformComponent().moveBack(.5);
            return null;
        });
        onKey(KeyCode.D, () -> {
            //if you want the player to hold directtion until is moved uncomment following line
            //playerRotate();
            player.getTransformComponent().moveRight(.5);
            return null;
        });
        onKey(KeyCode.E, () -> {
            //zoom out from the object
            if (distance < 8) {
                distance += .2;
            }
            return null;
        });
        onKey(KeyCode.Q, () -> {
            //zoom in to the object
            if (distance > 2) {
                distance -= .2;
            }
            return null;
        });
    }

    @Override
    protected void onUpdate(double tpf) {
        //this code finds the object position and sets the camera position and directino to move around the object always looking at it
        double xset = distance * Math.sin(Math.toRadians(camera.getTransform().getRotationY())) * -1 + player.getX();
        double zset = distance * Math.cos(Math.toRadians(camera.getTransform().getRotationY())) * -1 + player.getZ();
        double yset = distance * Math.sin(Math.toRadians(camera.getTransform().getRotationX())) + player.getY() + player.getScaleY() - 2;
        camera.getTransform().setPosition3D(xset, yset, zset);
        //this line makes the object to rotate along with the camera, if you comment this line and do no remove the comments
        //from inputs that move the player the 3d direction will not be updated
        playerRotate();
    }

    private void playerRotate() {
        animationBuilder()
                .duration(Duration.millis(150))
                .autoReverse(false)
                .rotate(player)
                .from(new Point3D(player.getTransformComponent().getRotationX(), player.getTransformComponent().getRotationY(), player.getTransformComponent().getRotationZ()))
                .to(new Point3D(camera.getTransform().getRotationX(), camera.getTransform().getRotationY(), camera.getTransform().getRotationZ()))
                .buildAndPlay();
        Point3D look = camera.getTransform().getDirection3D().add(player.getPosition3D());
        player.getTransformComponent().lookAt(look);
    }

    @Override
    protected void initPhysics() {
        getPhysicsWorld().setGravity(10, 10);
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
