import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.CubicCurveTo;
import javafx.scene.shape.Line;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.awt.*;
import java.security.Key;

public class Test extends Application {
    public void start(Stage arg0) throws Exception {

        BorderPane root = new BorderPane();
        Scene scene = new Scene(root,400,400);

        //创建一个矩形
        /*final Rectangle rect=new Rectangle(0, 0, 40, 40);
        rect.setArcHeight(10);
        rect.setArcWidth(10);
        rect.setFill(Color.RED);*/
        Label label = new Label("ff");
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(4),new KeyValue(label.layoutXProperty(),50),new KeyValue(label.layoutYProperty(),50));
        Timeline timeline = new Timeline();
        //timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.setAutoReverse(false);
        timeline.getKeyFrames().addAll(keyFrame);
        timeline.play();

        Pane rect = new Pane();
        rect.getChildren().addAll(label);
        //将矩形作为一个Node方到Parent中
        root.getChildren().add(rect);

        //创建路径
        //javafx.scene.shape.Path path=new javafx.scene.shape.Path();
        Line path = new Line(30,16,70,16);
        //path.getElements().add(new MoveTo(20, 20));
        //path.getElements().add(new MoveTo(20, 40));
        //path.getElements().add(new MoveTo(60, 40));
        //path.getElements().add(new MoveTo(60, 70));
        //path.getElements().add(new CubicCurveTo(380, 0, 380, 120, 200, 120));
        //path.getElements().add(new CubicCurveTo(0, 120, 0, 240, 380, 240));
        //创建路径转变
        PathTransition pt=new PathTransition();
        pt.setDuration(Duration.millis(4000));//设置持续时间4秒
        pt.setPath(path);//设置路径
        pt.setNode(rect);//设置物体
        pt.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
        pt.setOnFinished(e->{        });
        //设置周期性，无线循环
        pt.setCycleCount(Timeline.INDEFINITE);
        pt.setAutoReverse(true);//自动往复
        pt.play();//启动动画

        root.getChildren().addAll(path);

        arg0.setScene(scene);
        arg0.show();

    }
}
