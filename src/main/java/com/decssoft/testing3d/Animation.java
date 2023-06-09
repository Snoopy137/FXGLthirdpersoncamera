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
import javafx.beans.property.Property;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.util.Duration;

/**
 *
 * @author mis_p
 */
public class Animation {

    IntegerProperty keyCycle = new SimpleIntegerProperty();
    KeyValue dukeStart = new KeyValue(keyCycle, 0);
    KeyValue dukeEnd = new KeyValue(keyCycle, 14);
    KeyFrame dukeKey = new KeyFrame(Duration.millis(800), dukeStart, dukeEnd);
    public Timeline dukeLine = new Timeline(dukeKey);
    int cont;
    List<List<CustomShape3D.MeshVertex>> dukesAnimation = new ArrayList<>();
    List<Property<Number>> y = new ArrayList<>();
    List<Property<Number>> x = new ArrayList<>();
    List<Property<Number>> z = new ArrayList<>();

    public void createAnimation(Entity player) {
        getFrames();
        dukeLine.setCycleCount(1);
        Model3D model = (Model3D) player.getViewComponent().getChildren().get(0);
        cont = 0;
        model.getVertices().forEach(v -> {
            v.yProperty().bind(y.get(cont));
            v.xProperty().bind(x.get(cont));
            v.zProperty().bind(z.get(cont));
            cont++;
        });
        cont = 0;
        keyCycle.addListener((o) -> {
            x.clear();
            y.clear();
            z.clear();
            dukesAnimation.get(keyCycle.get())
                    .forEach(v -> {
                        y.add(v.yProperty());
                        x.add(v.xProperty());
                        z.add(v.zProperty());
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
        for (int i = 0; i <= 15; i++) {
            dukesAnimation.add(getAssetLoader().loadModel3D("java-duke" + i + ".obj").getVertices());
            dukesAnimation.get(i).forEach(v -> y.add(v.yProperty()));
            dukesAnimation.get(i).forEach(v -> z.add(v.zProperty()));
            dukesAnimation.get(i).forEach(v -> x.add(v.xProperty()));
        }
    }
}
