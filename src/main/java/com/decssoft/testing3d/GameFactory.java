package com.decssoft.testing3d;

import static com.almasb.fxgl.dsl.FXGLForKtKt.entityBuilder;
import static com.almasb.fxgl.dsl.FXGLForKtKt.getAssetLoader;
import static com.almasb.fxgl.dsl.FXGLForKtKt.getPhysicsWorld;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.box2d.dynamics.BodyType;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;

/**
 *
 * @author mis_p
 */
public class GameFactory implements EntityFactory {

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
    public Entity newPlayer(SpawnData data) {
        var view = new Box(3, 4, 3);
        var object = getAssetLoader().loadModel3D("Intergalactic_Spaceship-(Wavefront).obj");
        object.setScaleX(3);
        object.setScaleY(4);
        object.setScaleZ(3);
        return entityBuilder(data)
                .type(EntityType.PLAYER)
                .bbox(BoundingShape.box3D(3, 4, 3))
                .view(object)
                .collidable()
                .buildAndAttach();
    }
}
