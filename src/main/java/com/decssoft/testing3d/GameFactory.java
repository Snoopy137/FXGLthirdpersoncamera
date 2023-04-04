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
import javafx.animation.SequentialTransition;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;

/**
 *
 * @author mis_p
 */
public class GameFactory implements EntityFactory {

    FileReader reader;
    BufferedReader breader;
    ArrayList<String> lista;

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
}
