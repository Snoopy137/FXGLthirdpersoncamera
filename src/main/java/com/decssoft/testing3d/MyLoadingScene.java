package com.decssoft.testing3d;

import com.almasb.fxgl.animation.AnimatedStringIncreasing;
import com.almasb.fxgl.animation.Interpolators;
import com.almasb.fxgl.app.scene.LoadingScene;
import com.almasb.fxgl.core.math.FXGLMath;
import static com.almasb.fxgl.dsl.FXGLForKtKt.animationBuilder;
import static com.almasb.fxgl.dsl.FXGLForKtKt.getUIFactoryService;
import com.almasb.fxgl.ui.ProgressBar;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

/**
 *
 * @author mis_p
 */
public class MyLoadingScene extends LoadingScene {

    public MyLoadingScene() {
        getContentRoot().getChildren().addAll(new Rectangle(getAppWidth(), getAppHeight()));

        // circles
        var largeCircle = new Circle(45);
        largeCircle.setTranslateX(getAppWidth() / 2.0);
        largeCircle.setTranslateY(getAppHeight() / 3.0);

        for (int i = 0; i < 3; i++) {
            var circle = new Circle(10, 10, 10, Color.BLUE);
            circle.setTranslateX(getAppWidth() / 2.0);
            circle.setTranslateY(getAppHeight() / 3.0);

            getContentRoot().getChildren().addAll(circle);

            animationBuilder(this)
                    .delay(Duration.seconds(i * 0.4))
                    .duration(Duration.seconds(1.6))
                    .repeatInfinitely()
                    .interpolator(Interpolators.SMOOTH.EASE_IN_OUT())
                    .translate(circle)
                    .alongPath(largeCircle)
                    .buildAndPlay();
        }

        // progress bar
        var bar = new ProgressBar(false);
        bar.setWidth(getAppWidth() - 200);
        bar.setTranslateX(100);
        bar.setTranslateY(getAppHeight() - 100);
        bar.setFill(Color.BLUE.brighter());
        bar.setMaxValue(100);

        animationBuilder(this)
                .interpolator(Interpolators.EXPONENTIAL.EASE_IN())
                .duration(Duration.seconds(2.36))
                .repeatInfinitely()
                .autoReverse(true)
                .animate(bar.currentValueProperty())
                .from(0)
                .to(100)
                .buildAndPlay();

        getContentRoot().getChildren().add(bar);

        // string Loading
        var text = getUIFactoryService().newText("", Color.WHITE, 20.0);
        text.setTranslateX(getAppWidth() - 100);
        text.setTranslateY(getAppHeight() - 20);

        animationBuilder(this)
                .repeatInfinitely()
                .duration(Duration.seconds(3))
                .interpolator(Interpolators.BOUNCE.EASE_OUT())
                .animate(new AnimatedStringIncreasing("Loading..."))
                .onProgress(s -> text.setText(s))
                .buildAndPlay();

        getContentRoot().getChildren().add(text);

        // filled circle
        var pie = new Circle(22, 22, 22, Color.GREENYELLOW);
        pie.setStroke(Color.RED);
        pie.setStrokeWidth(2.5);
        pie.setTranslateX(50);
        pie.setTranslateY(50);

        animationBuilder(this)
                .interpolator(Interpolators.EXPONENTIAL.EASE_IN())
                .duration(Duration.seconds(2.36))
                .repeatInfinitely()
                .autoReverse(true)
                .animate(pie.radiusProperty())
                .from(0)
                .to(22)
                .buildAndPlay();

        getContentRoot().getChildren().add(pie);

        // manual progress bar
        var outerRect = new Rectangle(200, 30);
        outerRect.setStrokeWidth(2.0);
        outerRect.setStroke(Color.BLUE);

        var innerRect = new Rectangle(0, 25, Color.AQUA);

        var stack = new StackPane(outerRect, innerRect);
        stack.setTranslateX(getAppWidth() - 300);
        stack.setTranslateY(50);
        stack.setAlignment(Pos.CENTER_LEFT);

        animationBuilder(this)
                .interpolator(Interpolators.SMOOTH.EASE_IN())
                .duration(Duration.seconds(4.36))
                .repeatInfinitely()
                .autoReverse(true)
                .animate(innerRect.widthProperty())
                .from(0)
                .to(195)
                .buildAndPlay();

        getContentRoot().getChildren().add(stack);

        // hourglass line
        var line = new Rectangle(200, 3, Color.WHITE);
        line.setTranslateX(50);
        line.setTranslateY(250);
        line.setEffect(new DropShadow(15, Color.AQUA));

        animationBuilder(this)
                .interpolator(Interpolators.BACK.EASE_OUT())
                .duration(Duration.seconds(3.36))
                .repeatInfinitely()
                .rotate(line)
                .from(0)
                .to(360)
                .buildAndPlay();

        getContentRoot().getChildren().add(line);

        // dots ...
        for (int i = 0; i < 3; i++) {
            var dot = new Rectangle(20, 20, Color.DARKGREEN);
            dot.setTranslateX(getAppWidth() - 300 + i * 40);
            dot.setTranslateY(300);

            animationBuilder(this)
                    .delay(Duration.seconds(i * 0.5))
                    .interpolator(Interpolators.CUBIC.EASE_OUT())
                    .duration(Duration.seconds(1.36))
                    .repeatInfinitely()
                    .autoReverse(true)
                    .fadeIn(dot)
                    .buildAndPlay();

            getContentRoot().getChildren().add(dot);
        }

        // bezier curves
        for (int i = 0; i < 360; i++) {
            var p = new Rectangle(2, 2, Color.YELLOW);

            var point = FXGLMath.bezier(
                    new Point2D(500, 150),
                    new Point2D(460, 200), new Point2D(200, 550),
                    new Point2D(150, 500),
                    i / 360.0
            );

            p.setTranslateX(point.getX());
            p.setTranslateY(point.getY());

            animationBuilder(this)
                    .delay(Duration.seconds(i * 0.001))
                    .interpolator(Interpolators.CUBIC.EASE_OUT())
                    .duration(Duration.seconds(1.0))
                    .repeatInfinitely()
                    .autoReverse(true)
                    .fadeIn(p)
                    .buildAndPlay();

            getContentRoot().getChildren().add(p);
        }

        for (int i = 0; i < 360; i++) {
            var p = new Rectangle(2, 2, Color.YELLOW);

            var point = FXGLMath.bezier(
                    new Point2D(385, 300),
                    new Point2D(560, 450), new Point2D(550, 500),
                    new Point2D(600, 500),
                    i / 360.0
            );

            p.setTranslateX(point.getX());
            p.setTranslateY(point.getY());

            animationBuilder(this)
                    .delay(Duration.seconds(1.51 + i * 0.001))
                    .interpolator(Interpolators.CUBIC.EASE_IN())
                    .duration(Duration.seconds(1.0))
                    .repeatInfinitely()
                    .autoReverse(true)
                    .fadeIn(p)
                    .buildAndPlay();

            getContentRoot().getChildren().add(p);
        }
    }
}
