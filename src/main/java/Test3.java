import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.awt.*;

public class Test3 extends Application {
    public void start(Stage arg0) throws Exception {

        BorderPane root = new BorderPane();
        Scene scene = new Scene(root);

        //创建一个矩形
        Rectangle rect=new Rectangle(0, 0, 40, 40);
        rect.setArcHeight(10);
        rect.setArcWidth(10);
        rect.setFill(Color.RED);
        //将矩形作为一个Node方到Parent中
        Pane pane = new Pane();
        javafx.scene.control.Label label = new javafx.scene.control.Label("sfdgf");
        pane.getChildren().add(label);
        //root.getChildren().add(pane);
        //root.getChildren().add(rect);
        root.getChildren().addAll(pane);

        //创建路径
        javafx.scene.shape.Path path=new javafx.scene.shape.Path();
        /*
        path.getElements().add(new MoveTo(642,16));
        path.getElements().add(new HLineTo(770));
        path.getElements().add(new VLineTo(574));
        path.getElements().add(new HLineTo(642));
        path.getElements().add(new VLineTo(536));
        //path.getElements().add(new CubicCurveTo(380, 0, 380, 120, 200, 120));
        //path.getElements().add(new CubicCurveTo(0, 120, 0, 240, 380, 240));
        //创建路径转变
        PathTransition pt=new PathTransition();
        pt.setDuration(Duration.millis(4000));//设置持续时间4秒
        pt.setPath(path);//设置路径
        //pt.setNode(rect);//设置物体
        pt.setNode(pane);//设置物体
        pt.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
        //设置周期性，无线循环
        pt.setCycleCount(Timeline.INDEFINITE);
        pt.setAutoReverse(true);//自动往复
        pt.play();//启动动画

        arg0.setScene(scene);
        arg0.show();
    */
    }
}
