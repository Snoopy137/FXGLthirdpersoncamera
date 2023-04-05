package com.decssoft.testing3d;

import static com.almasb.fxgl.dsl.FXGLForKtKt.entityBuilder;
import static com.almasb.fxgl.dsl.FXGLForKtKt.getAssetLoader;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.scene3d.Model3D;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import javafx.util.Duration;

/**
 *
 * @author mis_p
 */
public class GameFactory implements EntityFactory {

    FileReader reader;
    BufferedReader breader;
    ArrayList<String> lista;
    TriangleMesh mesh = new TriangleMesh();
    MeshView mv = new MeshView(mesh);

    @Spawns("wallr")
    public Entity newWallr(SpawnData data) {
        var view = new Box(.4, 10, 20);
        return entityBuilder(data)
                .type(EntityType.WALL)
                .bbox(BoundingShape.box3D(.4, 10, 20))
                .view(view)
                .collidable()
                .build();
    }

    @Spawns("walll")
    public Entity newWalll(SpawnData data) {
        var view = new Box(.4, 10, 20);
        view.setMaterial(new PhongMaterial(Color.BROWN));
        return entityBuilder(data)
                .type(EntityType.WALL)
                .bbox(BoundingShape.box3D(.4, 10, 20))
                .view(view)
                .collidable()
                .build();
    }

    @Spawns("roof")
    public Entity newRoof(SpawnData data) {
        var view = new Box(20, .4, 20);
        return entityBuilder(data)
                .type(EntityType.ROOF)
                .bbox(BoundingShape.box3D(20, .4, 20))
                .view(view)
                .collidable()
                .build();
    }

    @Spawns("floor")
    public Entity newFloor(SpawnData data) {
        var view = new Box(20, .4, 20);
        return entityBuilder(data)
                .type(EntityType.FLOOR)
                .bbox(BoundingShape.box3D(20, .4, 20))
                .view(view)
                .collidable()
                .build();
    }

    @Spawns("player")
    public Entity newPlayer(SpawnData data) throws IOException {
        Model3D root = getAssetLoader().loadModel3D("java-duke0.obj");
        SequentialTransition calloutAnimation = new SequentialTransition();
        return entityBuilder(data)
                .type(EntityType.PLAYER)
                .bbox(BoundingShape.box3D(.26, .26, 1.1))
                .view(root)
                .collidable()
                .buildAndAttach();
    }

    @Spawns("pool")
    public Entity newPool(SpawnData data) {
        IntegerProperty keyCycle = new SimpleIntegerProperty();
        PhongMaterial material = new PhongMaterial(new Color(0, 1, 1, 0.5));
        material.setSpecularColor(Color.LIGHTYELLOW);
        material.setSpecularPower(512);
        Image image = new Image("C:\\Users\\mis_p\\Downloads\\Water_002_NORM.jpg");
        material.setBumpMap(image);
        mv.setMaterial(material);
        float[] uvCoords = {3, 0,
            3, 3,
            0, 0,
            0, 3};
        mesh.getPoints().addAll(-10, 0, -10,
                -10, 0, 10,
                10, 0, 10,
                10, 0, -10,
                -10, .4f, -10);
        mesh.getTexCoords().addAll(uvCoords);
        mesh.getFaces().addAll(0, 0, 2, 3, 1, 1,
                0, 0, 3, 2, 2, 3,
                4, 1, 3, 3, 0, 2);
        KeyValue start = new KeyValue(keyCycle, 0, Interpolator.LINEAR);

        KeyValue end = new KeyValue(keyCycle, 24 * 20, Interpolator.LINEAR);
        KeyFrame kf = new KeyFrame(Duration.seconds(20), start, end);

        mv.setOnKeyTyped((t) -> {
            KeyValue start1 = new KeyValue(keyCycle, 0);
            KeyValue end1 = new KeyValue(keyCycle, 21);
            KeyFrame kf1 = new KeyFrame(Duration.seconds(1), start, end);
            Timeline tm = new Timeline(kf);
            tm.setCycleCount(100);
            tm.play();
        });
        Timeline tm = new Timeline(kf);
        tm.setCycleCount(100);
        tm.play();
        Group g = new Group(mv);
        keyCycle.addListener(e -> {

            float add = keyCycle.getValue() / 30000f;

            TriangleMesh mesh = (TriangleMesh) mv.getMesh();
            for (int i = 0; i < uvCoords.length; i++) {
                uvCoords[i] += add;
            }
            mesh.getTexCoords().set(0, uvCoords, 0, uvCoords.length);

        });
        return entityBuilder(data)
                .type(EntityType.POOL)
                .bbox(BoundingShape.box3D(3, 4, 3))
                .view(mv)
                .collidable()
                .buildAndAttach();
    }
}
