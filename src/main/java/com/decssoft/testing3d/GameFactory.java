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
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.SpotLight;
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
                .type(EntityType.WALLR)
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
                .type(EntityType.WALLL)
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
        view.setMaterial(new PhongMaterial(Color.TURQUOISE));
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
        SpotLight spot = new SpotLight(Color.WHITE);
        spot.setDirection(new Point3D(0, 5, 0));
        spot.setInnerAngle(.9);
        spot.setOuterAngle(1.2);
        spot.setTranslateX(0);
        spot.setTranslateY(-5);
        spot.setTranslateZ(0);
        Group g = new Group(root, spot);
        return entityBuilder(data)
                .type(EntityType.PLAYER)
                .bbox(BoundingShape.box3D(.2, .2, .1))
                .view(g)
                .collidable()
                .build();
    }

    @Spawns("enemy")
    public Entity newEnemy(SpawnData data) throws IOException {
        Model3D root = getAssetLoader().loadModel3D("DukerZombie/java-dukezombie04.obj");
        SpotLight spot = new SpotLight(Color.DARKGREEN);
        spot.setDirection(new Point3D(0, 5, 0));
        spot.setInnerAngle(.5);
        spot.setOuterAngle(.5);
        spot.setTranslateX(0);
        spot.setTranslateY(-5);
        spot.setTranslateZ(0);
//        SpotLight spot1 = new SpotLight(Color.GREEN);
//        spot1.setDirection(new Point3D(0, 0, -1));
//        spot1.setTranslateX(0);
//        spot1.setTranslateY(0);
//        spot1.setTranslateZ(2);
        Group enemy = new Group(root, spot);
        return entityBuilder(data)
                .type(EntityType.ENEMY)
                .bbox(BoundingShape.box3D(.2, .2, .2))
                .view(enemy)
                .collidable()
                .build();
    }
}
