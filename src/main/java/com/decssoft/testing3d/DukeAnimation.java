package com.decssoft.testing3d;

import static com.almasb.fxgl.dsl.FXGLForKtKt.getAssetLoader;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.scene3d.CustomShape3D;
import com.almasb.fxgl.scene3d.Model3D;
import java.util.ArrayList;
import java.util.List;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.Group;
import javafx.util.Duration;

/**
 *
 * @author mis_p
 */
public class DukeAnimation {

    IntegerProperty keyCycle = new SimpleIntegerProperty();
    KeyValue dukeStart = new KeyValue(keyCycle, 0);
    KeyValue dukeEnd = new KeyValue(keyCycle, 14);
    KeyFrame dukeKey = new KeyFrame(Duration.millis(300), dukeStart, dukeEnd);
    public Timeline dukeLine = new Timeline(dukeKey);
    int cont;
    List<List<CustomShape3D.MeshVertex>> dukesAnimation = new ArrayList<>();

    public DukeAnimation() {
        getFrames();
    }

    public void createAnimation(Entity player) {
//        getFrames();
        dukeLine.setCycleCount(1);
        keyCycle.addListener((o) -> {
            Group g = (Group) player.getViewComponent().getChildren().get(0);
            Model3D model = (Model3D) g.getChildren().get(0);
            //the below code does not update vertices, left there just as an example
//            model.getVertices().clear();
//            model.getVertices().addAll(dukesAnimation.get(keyCycle.get()).getVertices());
/////////////////////////////////////////////separator////////////////////////////////////////////////////////////////
            cont = 0;
            model.getVertices()
                    .forEach(v -> {
                        v.setX(dukesAnimation.get(keyCycle.get()).get(cont).getX());
                        v.setY(dukesAnimation.get(keyCycle.get()).get(cont).getY());
                        v.setZ(dukesAnimation.get(keyCycle.get()).get(cont).getZ());
                        cont++;
                    });
            if (keyCycle.get() == 14) {
                keyCycle.set(0);
            }
        });
    }

    public Timeline getTimeline() {
        return dukeLine;
    }

    public void getFrames() {
        for (int i = 0; i < 15; i++) {
            dukesAnimation.add(getAssetLoader().loadModel3D("java-duke" + i + ".obj").getVertices());
        }
    }
}
