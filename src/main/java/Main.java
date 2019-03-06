import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.stage.Stage;
import javafx.scene.media.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.text.SimpleDateFormat;
import java.util.Date;
import javafx.animation.*;

public class Main extends Application{
    private MediaPlayer MOVPlayer;
    private MediaPlayer LADPlayer;
    private MediaPlayer ADDPlayer;
    private MediaPlayer STOPlayer;
    private MediaPlayer JMPPlayer;
    private MediaPlayer ALLPlayer;
    //创建cpu图片展示的面板
    private Pane pane = new Pane();
    //创建各式label
    private Label R0Label = new Label("R0");
    private Label R1Label = new Label("R1");
    private Label R2Label = new Label("R2");
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
                /*HBox ctrlHBox = new HBox();
                ctrlHBox.getChildren().add(new )
                ctrlHBox.alignmentProperty().setValue(Pos.CENTER);*///Hbox中节点分布居中
        GridPane ctrlGridPane = new GridPane();

        ctrlGridPane.setVgap(2);
        ctrlGridPane.setHgap(9);

        selectCommand.getItems().addAll(items);
        selectCommand.setValue("MOV");
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
        /*selectCommand.setOnAction(e -> {
            //outputDescription(items.indexOf(selectCommand.getValue()));
            showCommand();
        });*/
        startShow.setOnAction(e -> {
            showCommand();
        });
        //布局设置
        //borderPane.setTop(new ImageView(cpuPng));
        R0Label.setLayoutX(190);
        R1Label.setLayoutX(190);
        R2Label.setLayoutX(190);
        R0Label.setLayoutY(205);
        R1Label.setLayoutY(227);
        R2Label.setLayoutY(250);
        pane.getChildren().addAll(new ImageView(cpuPng),R0Label,R1Label,R2Label);
        borderPane.setTop(pane);
        borderPane.setCenter(ctrlGridPane);
        borderPane.setBottom(outDescription);

        Scene scene = new Scene(borderPane);
        primaryStage.setTitle("计算机组成原理CPU指令执行演示程序 ");
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();

    }
    private void Addressingtage(String command){
        int x = 630;
        int y = 68;
        Label label = new Label(command);
        label.setLayoutX(x);
        label.setLayoutY(y);
        PathTransition getCommand = new PathTransition();
        Path path = new Path();
        path.getElements().add(new MoveTo(630,16));
        //getCommand.setPath();

    }
    private void MOV(){
        //输出文字描述
        outDescription.setText(flagDescription[0]);
        //MOV 音频播放控件
        Media MOV = new Media(getClass().getResource("audio/MOV.mp3").toString());
        MOVPlayer = new MediaPlayer(MOV);
        MOVPlayer.setOnReady(()->{
            startShow.setDisable(true);
            chkLog.setDisable(true);
            MOVPlayer.play();
        });
        MOVPlayer.setOnEndOfMedia(()->{
            startShow.setDisable(false);
            chkLog.setDisable(false);
        });
    }
    private void LAD(){
        outDescription.setText(flagDescription[1]);
        //LAD 音频播放控件
        Media LAD = new Media(getClass().getResource("audio/LAD.mp3").toString());
        LADPlayer = new MediaPlayer(LAD);
        LADPlayer.setOnReady(()->{
            startShow.setDisable(true);
            chkLog.setDisable(true);
            LADPlayer.play();
        });
        //LADPlayer.play();
        LADPlayer.setOnEndOfMedia(()->{
            startShow.setDisable(false);
            chkLog.setDisable(false);
        });
        /*LADPlayer.setOnStopped(()->{
            startShow.setDisable(false);
            chkLog.setDisable(false);
            //chkLog
        });*/
        //System.out.println( LADPlayer.onStoppedProperty().toString());
    }
    private void ADD(){
        outDescription.setText(flagDescription[2]);
        //ADD 音频播放控件
        Media ADD = new Media(getClass().getResource("audio/ADD.mp3").toString());
        ADDPlayer = new MediaPlayer(ADD);
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
    private void STO(){
        outDescription.setText(flagDescription[3]);
        //STO 音频播放控件
        //String sto = getClass().getResource("audio/STO.mp3").toString();
        Media STO = new Media(getClass().getResource("audio/STO.mp3").toString());
        STOPlayer = new MediaPlayer(STO);
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
    private void JMP(){
        outDescription.setText(flagDescription[4]);
        //JMP 音频播放控件
        //String jmp = getClass().getResource("audio/JMP.mp3").toString();
        Media JMP = new Media(getClass().getResource("audio/JMP.mp3").toString());
        JMPPlayer = new MediaPlayer(JMP);
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
    private void ALL(){
        /*MOV();
        LAD();
        ADD();
        STO();
        JMP();*/
        outDescription.setText(flagDescription[5]);
        Media ALL = new Media(getClass().getResource("audio/ALL.mp3").toString());
        ALLPlayer = new MediaPlayer(ALL);
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
    /*public void outputDescription(int index){
        outDescription.setText(flagDescription[index]);
    }*/
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
        /*startShow.setDisable(true);
         chkLog.setDisable(true);*/
            //检查执行的到底是哪一条命令
            //一开始忘记加break，佛了
            switch (items.indexOf(selectCommand.getValue())) {
                case 0:
                    MOV();
                    break;
                case 1:
                    LAD();
                    break;
                case 2:
                    ADD();
                    break;
                case 3:
                    STO();
                    break;
                case 4:
                    JMP();
                    break;
                case 5:
                    ALL();
                    break;
            }
        /*startShow.setDisable(false);
         hkLog.setDisable(false);*/
            //检查复选框状态
            if (chkLog.selectedProperty().getValue()) {
            /*PrintStream print= new PrintStream("./output.txt");//输出到文件
               System.setOut(print);*/
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
}