import com.sun.istack.internal.NotNull;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.*;
import javafx.stage.Stage;
import javafx.scene.media.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import javafx.animation.*;
import javafx.util.Duration;

public class Main extends Application{
    String command1;
    //媒体播放控件
    private MediaPlayer MOVPlayer;
    private MediaPlayer LADPlayer;
    private MediaPlayer ADDPlayer;
    private MediaPlayer STOPlayer;
    private MediaPlayer JMPPlayer;
    private MediaPlayer ALLPlayer;
    private Label PC = new Label("程序计数器");
    private Label PCS = new Label("");
    private Pane PcShow = new Pane();
    //创建cpu图片展示的面板
    private Pane pane = new Pane();
    //创建各式label
    private Label R0Label = new Label("R0");
    private Label R1Label = new Label("R1");
    private Label R2Label = new Label("R2");
    private Label cache30 = new Label("40");
    //创建组合框
    private String[] command = {"MOV", "LAD", "ADD", "STO", "JMP", "汇编演示"};
    //创建R0，R1，R3文本输入框
    private TextField R0 = new TextField();
    private TextField R1 = new TextField();
    private TextField R2 = new TextField();
    //复选框
    private CheckBox chkLog = new CheckBox("记录日志");
    //添加输出文本域
    private TextArea outDescription = new TextArea();
    //创建开始按钮
    private Button startShow = new Button("开始");
    //创建指令文字提示
    private String[] flagDescription = new String[6];
    private ObservableList<String> items =
            FXCollections.observableArrayList(command);
    //创建组合框并设置初始值为  MOV
    private ComboBox<String> selectCommand = new ComboBox<>();
    @Override
    public void start(Stage primaryStage){
        BorderPane borderPane = new BorderPane();//主面板
        //left
        String cpuImagePath = getClass().getResource("images/cpu.png").toString();
        Image cpuPng = new Image(cpuImagePath);
        //bottom
        GridPane ctrlGridPane = new GridPane();

        ctrlGridPane.setVgap(2);
        ctrlGridPane.setHgap(9);

        selectCommand.getItems().addAll(items);
        selectCommand.setValue("MOV");
        //PcShow.setMaxSize(10,2);
        //设置输入框宽度
        R0.setPrefWidth(160);
        R1.setPrefWidth(160);
        R2.setPrefWidth(160);
        //向CtrlGRidPane里面添加元素
        ctrlGridPane.add(new Label("R0:"),0,0);
        ctrlGridPane.add(R0,1,0);
        ctrlGridPane.add(new Label("R1:"),2,0);
        ctrlGridPane.add(R1,3,0);
        ctrlGridPane.add(new Label("R2:"),4,0);
        ctrlGridPane.add(R2,5,0);
        ctrlGridPane.add(selectCommand,6,0);
        ctrlGridPane.add(startShow,7,0);
        //设置默认不勾选
        chkLog.setSelected(false);
        ctrlGridPane.add(chkLog,8,0);


        outDescription.setWrapText(true);
        outDescription.setEditable(false);

        flagDescription[0] = "MOV指令\n" +
                "  取指阶段：\n" +
                "    程序计数器PC装入第一条指令的地址101，PC的内容被放到指令地址总线上，对指令进行译码并启动读命令。从101号地址读出MOV指令，通过指令总线IBus装入指令寄存器IR，程序计数器PC内容加1，变成102，为下一条指令做好准备。指令寄存器IR中的操作码被译码，CPU识别出是MOV指令，至此取指阶段完成。\n" +
                "  执行阶段：\n" +
                //"    操作控制器OC送出控制信号到通用寄存器，选择R1（10）为源寄存器，RO（00）为目标寄存器。OC送出控制信号到ALU，指定ALU做传送操作，打开ALU输出三态门，将ALU输出（10）送的数据总线DBus上，任何时刻DBus上只能有一个数据。将DBus上的数据打入数据缓冲寄存器DR，将DR中的数据打入目标寄存器RO，RO的内容由00变为10至此MOV指令执行完毕。";
                "    操作控制器OC送出控制信号到通用寄存器，选择R1为源寄存器，RO为目标寄存器。OC送出控制信号到ALU，指定ALU做传送操作，打开ALU输出三态门，将ALU输出送的数据总线DBus上，任何时刻DBus上只能有一个数据。将DBus上的数据打入数据缓冲寄存器DR，将DR中的数据打入目标寄存器RO，RO的内容变为R1中的数据至此MOV指令执行完毕。\n";
        flagDescription[1] = "LAD指令\n" +
                "  取指阶段：\n" +
                "    LAD指令的取指阶段和MOV指令完全相同。\n" +
                "  执行阶段：\n" +
                "    OC发出控制命令，打开IR输出三态门，将指令中的直接地址码6放到数据总线DBus上，装入地址寄存器AR，将数存6号单元中的数读出到DBus上，装入缓冲寄存器DR。将DR中的数 装入通用寄存器R1，原来R1中的值被覆盖，至此LAD指令执行完毕。\n";
        flagDescription[2] = "ADD指令\n" +
                "  取指阶段：\n" +
                "    ADD指令的取指阶段和其他指令相同。\n" +
                "  执行阶段：\n" +
                "    操作控制器OC送出控制信号到通用寄存器，选择R1为源寄存器，R2为目标寄存器。ALU做R1和R2的加法运算，打开ALU输出三态门，将运算结果放到数据总线DBus上，然后打入缓冲寄存器DR。ALU产生的进位信号保存在状态字寄存器PSW中，将DR中数值装入R2中，R2原来的数被覆盖。到此ADD指令执行结束。\n";
        flagDescription[3] = "STO指令\n" +
                "  取指阶段：\n" +
                "        STO指令的取指阶段和其他指令相同。\n" +
                "  执行阶段：\n" +
                "        操作控制器OC送出控制信号到通用寄存器，选择R3（30）作为数据存储器的地址。打开通用寄存器输出三态门，将地址30放到DBus上并装入地址寄存器AR，并进行地址译码。操作控制器OC送出控制信号到通用寄存器，选择R2中数据作为数存的写入数据放到DBus上。将此数值写入数存30单元，原先的数据被冲掉。至此STO指令执行结束。\n";
        flagDescription[4] = "JMP指令\n" +
                "    取指阶段：\n" +
                "        JMP指令的取指周期和其他指令相同。\n" +
                "    执行阶段：\n" +
                "        OC发出控制命令，打开IR输出三态门，将IR中的地址码101发送到DBus上，将DBus上的地址码101打入到程序计数器PC中，PC中原先的地址106被更换。于是下一条指令不是从106单元取出，而是转移到101单元取出。至此JMP指令执行周期结束。\n";
        flagDescription[5] = flagDescription[0] + flagDescription[1] + flagDescription[2] + flagDescription[3] + flagDescription[4];
        startShow.setOnAction(e -> {
            showCommand();
        });
        //布局设置
        cache30.setLayoutX(454);
        cache30.setLayoutY(230);

        R0Label.setLayoutX(190);
        R1Label.setLayoutX(190);
        R2Label.setLayoutX(190);
        R0Label.setLayoutY(205);
        R1Label.setLayoutY(227);
        R2Label.setLayoutY(250);
        PC.setLayoutX(585);
        PC.setLayoutY(336);
        PcShow.getChildren().add(PCS);
        pane.getChildren().addAll(new ImageView(cpuPng),R0Label,R1Label,R2Label,PC,PcShow,cache30);
        borderPane.setTop(pane);
        borderPane.setCenter(ctrlGridPane);
        borderPane.setBottom(outDescription);

        Scene scene = new Scene(borderPane);
        primaryStage.setTitle("计算机组成原理CPU指令执行演示程序 ");
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();

    }
    private void Common(String command){
        this.command1 = command;
        Command1();
        Path path = new Path();
        path.getElements().add(new MoveTo(642,16));
        path.getElements().add(new HLineTo(770));
        path.getElements().add(new VLineTo(574));
        path.getElements().add(new HLineTo(642));
        path.getElements().add(new VLineTo(536));
        PathTransition pt=new PathTransition();
        pt.setDuration(Duration.seconds(10));//设置持续时间10秒
        pt.setPath(path);//设置路径
        pt.setNode(PcShow);//设置物体
        //取址阶段
        pt.play();//启动动画
        pt.setOnFinished(e -> {
            PcsSetText("");//当取址阶段完成时，重置PCS标签里面的文字
            //执行阶段
            switch (command) {
                case "MOV":
                    MOV();
                    break;
                case "LAD":
                    LAD();
                    break;
                case "ADD":
                    ADD();
                    break;
                case "STO":
                    STO();
                    break;
                case "JMP":
                    JMP();
                    break;
            }
        });
    }

    private void MOVPlayer() {
        PC.setText("101");
        //输出文字描述
        outDescription.setText(flagDescription[0]);
        //MOV 音频播放控件
        MOVPlayer = new MediaPlayer(new Media(getClass().getResource("audio/MOV.mp3").toString()));
        MOVPlayer.setOnReady(() -> {
            startShow.setDisable(true);
            chkLog.setDisable(true);
            MOVPlayer.play();
        });
        MOVPlayer.setOnEndOfMedia(() -> {
            startShow.setDisable(false);
            chkLog.setDisable(false);
        });
    }
    private void MOV(){
        PcsSetText(R1.getText());
        //PCS.setText(R1.getText());
        Path path = new Path();
        path.getElements().add(new MoveTo(R1Label.getLayoutX(),R1Label.getLayoutY()));
        path.getElements().add(new VLineTo(84));
        path.getElements().add(new VLineTo(16));
        path.getElements().add(new HLineTo(318));
        path.getElements().add(new VLineTo(410));
        path.getElements().add(new HLineTo(180));
        path.getElements().add(new VLineTo(R0Label.getLayoutY()));
        PathTransition pt=new PathTransition();
        pt.setDuration(Duration.seconds(35));//设置持续时间20秒
        pt.setPath(path);//设置路径
        pt.setNode(PcShow);//设置物体
        pt.play();//启动动画
        PcSetText("102");
        pt.setOnFinished(e -> {
            R0LabelText(R1.getText());
            PcsSetText("");
        });
    }

    private void LADPlayer(){
        outDescription.setText(flagDescription[1]);
        //LAD 音频播放控件
        LADPlayer = new MediaPlayer(new Media(getClass().getResource("audio/LAD.mp3").toString()));
        LADPlayer.setOnReady(()->{
            startShow.setDisable(true);
            chkLog.setDisable(true);
            LADPlayer.play();
        });
        LADPlayer.setOnEndOfMedia(()->{
            startShow.setDisable(false);
            chkLog.setDisable(false);
            //chkLog.disableProperty().bind();
        });
    }
    private void LAD(){
        PcsSetText("6");
        Path path = new Path();
        path.getElements().add(new MoveTo(672,512));
        path.getElements().add(new VLineTo(382));
        path.getElements().add(new HLineTo(446));
        path.getElements().add(new VLineTo(135));
        Path path1 = new Path();
        path1.getElements().add(new MoveTo(446,135));
        path1.getElements().add(new VLineTo(16));
        path1.getElements().add(new HLineTo(313));
        path1.getElements().add(new VLineTo(405));
        path1.getElements().add(new HLineTo(198));
        path1.getElements().add(new VLineTo(R1Label.getLayoutY()));
        PathTransition pt=new PathTransition();
        PathTransition pt1=new PathTransition();
        pt.setDuration(Duration.seconds(11));//设置持续时间11秒
        pt.setPath(path);//设置路径
        pt.setNode(PcShow);//设置物体

        pt1.setDuration(Duration.seconds(10));//设置持续时间11秒
        pt1.setPath(path1);//设置路径
        pt1.setNode(PcShow);//设置物体
        pt.play();//启动动画
        PcSetText("103");
        pt.setOnFinished(e -> {
            PcsSetText("100");
            pt1.play();
            pt1.setOnFinished(e1 -> {
                R1LabelText("100");
                //R1Label.setText("100");
                PcsSetText("");
            });
        });
    }

    private void ADDPlayer(){
        outDescription.setText(flagDescription[2]);
        //ADD 音频播放控件
        ADDPlayer = new MediaPlayer(new Media(getClass().getResource("audio/ADD.mp3").toString()));
        ADDPlayer.setOnReady(()->{
            startShow.setDisable(true);
            chkLog.setDisable(true);
            ADDPlayer.play();
        });
        ADDPlayer.setOnEndOfMedia(()->{
            startShow.setDisable(false);
            chkLog.setDisable(false);
        });
    }
    private void ADD(){
        PcsSetText(R0.getText());
        //PCS.setText(R0.getText());
        Path path = new Path();
        //path.getElements().add();190,205,227,250
        path.getElements().add(new MoveTo(190,205));
        path.getElements().add(new VLineTo(91));

        Path path1 = new Path();
        path1.getElements().add(new MoveTo(190,227));
        path1.getElements().add(new VLineTo(91));

        Path path2 = new Path();
        path2.getElements().add(new MoveTo(190,91));
        path2.getElements().add(new VLineTo(16));
        path2.getElements().add(new HLineTo(318));
        path2.getElements().add(new VLineTo(411));
        path2.getElements().add(new HLineTo(180));
        path2.getElements().add(new VLineTo(R2Label.getLayoutY()));

        PathTransition pt=new PathTransition();
        pt.setDuration(Duration.seconds(3));//设置持续时间20秒
        pt.setPath(path);//设置路径
        pt.setNode(PcShow);//设置物体

        PathTransition pt1=new PathTransition();
        pt1.setDuration(Duration.seconds(4));//设置持续时间20秒
        pt1.setPath(path1);//设置路径
        pt1.setNode(PcShow);//设置物体

        PathTransition pt2=new PathTransition();
        pt2.setDuration(Duration.seconds(12));//设置持续时间20秒
        pt2.setPath(path2);//设置路径
        pt2.setNode(PcShow);//设置物体

        pt.play();
        PcSetText("104");
        //PC.setText("104");
        pt.setOnFinished(event -> {
            PCS.setText(R1.getText());
            pt1.play();
            pt1.setOnFinished(event1 -> {
                //PCS.setText((Integer.parseInt(R0Label.getText()) + Integer.parseInt(R1Label.getText()))+"");
                String text = Integer.parseInt(R0Label.getText()) + Integer.parseInt(R1Label.getText())+"";
                PcsSetText(text);
                pt2.play();
                pt2.setOnFinished(event2 -> {
                    R2LabelText(text);
                    PcsSetText("");
                    //R2Label.setText((Integer.parseInt(R0Label.getText()) + Integer.parseInt(R1Label.getText()))+"");
                    //PCS.setText("");
                });
            });
        });
    }
    private void STOPlayer(){
        outDescription.setText(flagDescription[3]);
        //STO 音频播放控件
        STOPlayer = new MediaPlayer(new Media(getClass().getResource("audio/STO.mp3").toString()));
        STOPlayer.setOnReady(()->{
            startShow.setDisable(true);
            chkLog.setDisable(true);
            STOPlayer.play();
        });
        STOPlayer.setOnEndOfMedia(()->{
            startShow.setDisable(false);
            chkLog.setDisable(false);
        });
    }
    private void STO(){
        PcsSetText("30");
        //PCS.setText("30");
        Path path = new Path();
        path.getElements().add(new MoveTo(185,280));
        path.getElements().add(new LineTo(230,160));
        path.getElements().add(new HLineTo(313));
        path.getElements().add(new VLineTo(382));
        path.getElements().add(new HLineTo(446));
        path.getElements().add(new LineTo(390,229));
        PathTransition pt=new PathTransition();
        pt.setDuration(Duration.seconds(9));//设置持续时间11秒
        pt.setPath(path);//设置路径
        pt.setNode(PcShow);//设置物体

        Path path1 = new Path();
        path1.getElements().add(new MoveTo(R2Label.getLayoutX(),R2Label.getLayoutY()));
        path1.getElements().add(new LineTo(230,160));
        path1.getElements().add(new HLineTo(313));
        path1.getElements().add(new VLineTo(16));
        path1.getElements().add(new HLineTo(446));
        path1.getElements().add(new VLineTo(232));
        PathTransition pt1=new PathTransition();
        pt1.setDuration(Duration.millis(10000));//设置持续时间11秒
        pt1.setPath(path1);//设置路径
        pt1.setNode(PcShow);//设置物体

        pt.play();
        PcSetText("105");
        //PC.setText("105");
        pt.setOnFinished(event -> {
            PcsSetText(R2Label.getText());
            //PCS.setText(R2Label.getText());
            pt1.play();
            pt1.setOnFinished(event1 -> {
                Cache(PCS.getText());
                //cache30.setText(PCS.getText());
                //PCS.setText("");
                PcsSetText("");
            });
        });
    }

    private void JMPPlayer(){
        PcSetText("105");
        //PC.setText("105");
        outDescription.setText(flagDescription[4]);
        //JMP 音频播放控件
        JMPPlayer = new MediaPlayer(new Media(getClass().getResource("audio/JMP.mp3").toString()));
        JMPPlayer.setOnReady(()->{
            startShow.setDisable(true);
            chkLog.setDisable(true);
            JMPPlayer.play();
        });
        JMPPlayer.setOnEndOfMedia(()->{
            startShow.setDisable(false);
            chkLog.setDisable(false);
        });
    }
    private void JMP(){
        PcsSetText("101");
            //PCS.setText("101");
            Path path = new Path();
            path.getElements().add(new MoveTo(670,512));
            path.getElements().add(new VLineTo(382));
            path.getElements().add(new HLineTo(PC.getLayoutX()));
            path.getElements().add(new VLineTo(PC.getLayoutY()));
            PathTransition pt=new PathTransition();
            pt.setDuration(Duration.seconds(8));//设置持续时间8秒
            pt.setPath(path);//设置路径
            pt.setNode(PcShow);//设置物体
            pt.play();//启动动画
        pt.setOnFinished(event -> {
            PcSetText("101");
            //PC.setText("101");
            PcsSetText("");
            //PCS.setText("");
        });
    }

    private void ALLPlayer(){
        outDescription.setText(flagDescription[5]);
        ALLPlayer = new MediaPlayer(new Media(getClass().getResource("audio/ALL.mp3").toString()));
        ALLPlayer.setOnReady(()->{
            startShow.setDisable(true);
            chkLog.setDisable(true);
            ALLPlayer.play();
        });
        ALLPlayer.setOnEndOfMedia(()->{
            startShow.setDisable(false);
            chkLog.setDisable(false);
        });
    }
    private void ALL() {
        ALLPlayer();
        Timer timer =new Timer();
        timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    Common("MOV");
                }
            },0);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Common("LAD");
            }
        },75000);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Common("ADD");
            }
        },118000);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Common("STO");
            }
        },154000);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Common("JMP");
            }
        },198000);
};
    private void showCommand(){
        //判断
        try {
            int r0 = Integer.parseInt(R0.getText());
            int r1 = Integer.parseInt(R1.getText());
            int r2 = Integer.parseInt(R2.getText());

            outDescription.setStyle("-fx-text-fill: BLACK");
            R0Label.setText(R0.getText());
            R1Label.setText(R1.getText());
            R2Label.setText(R2.getText());

            //检查执行的到底是哪一条命令
            //一开始忘记加break，佛了
            switch (items.indexOf(selectCommand.getValue())) {
                case 0:
                    MOVPlayer();
                    Common("MOV");
                    break;
                case 1:
                    LADPlayer();
                    Common("LAD");
                    break;
                case 2:
                    ADDPlayer();
                    Common("ADD");
                    break;
                case 3:
                    STOPlayer();
                    Common("STO");
                    break;
                case 4:
                    JMPPlayer();
                    Common("JMP");
                    break;
                case 5:
                    ALLPlayer();
                    ALL();
                    break;
            }
            //检查复选框状态
            if (chkLog.selectedProperty().getValue()) {
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
                String text = "=================================\n"
                        + "当前时间: " + df.format(new Date())
                        + "\nR0: " + R0.getText() + " R1: " + R1.getText() + " R2: " + R2.getText()
                        + "\n当前执行的命令: " + selectCommand.getValue() + "\n复选框状态: " + chkLog.selectedProperty().getValue() + "\n";
                Write write = new Write();
                write.write(text, "./output.txt");
            }
            System.out.println("Happy Ending!!!");
        }catch (NumberFormatException e) {
            outDescription.setStyle("-fx-text-fill: RED");
            outDescription.setText("请在R0,R1,R2中输入有效数字!!!");
            System.out.println("Bad Ending!!!");
        }
    }
    private void PcsSetText(String text) {
        Platform.runLater(() -> {
            PCS.setText(text);
        });
    }
    private void PcSetText(String text){
        Platform.runLater(()->{
            PC.setText(text);
        });
    }
    private void Command1(){
        Platform.runLater(()->{
            PCS.setText(command1);
        });
    }
    private void R0LabelText(String text) {
        Platform.runLater(() -> {
            R0Label.setText(text);
        });
    }
    private void R1LabelText(String text) {
        Platform.runLater(() -> {
            R1Label.setText(text);
        });
    }
    private void R2LabelText(String text) {
        Platform.runLater(() -> {
            R2Label.setText(text);
        });
    }
    private void Cache(String text){
        Platform.runLater(()->{
            cache30.setText(text);
        });
    }
}