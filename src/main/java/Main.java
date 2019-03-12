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
    private String command1;
    //ý�岥�ſؼ�
    private MediaPlayer MOVPlayer;
    private MediaPlayer LADPlayer;
    private MediaPlayer ADDPlayer;
    private MediaPlayer STOPlayer;
    private MediaPlayer JMPPlayer;
    private MediaPlayer ALLPlayer;
    private Label PC = new Label("���������");
    private Label PCS = new Label("");
    private Pane PcShow = new Pane();
    //����cpuͼƬչʾ�����
    private Pane pane = new Pane();
    //������ʽlabel
    private Label R0Label = new Label("R0");
    private Label R1Label = new Label("R1");
    private Label R2Label = new Label("R2");
    private Label cache30 = new Label("40");
    //������Ͽ�
    private String[] command = {"MOV", "LAD", "ADD", "STO", "JMP", "�����ʾ"};
    //����R0��R1��R3�ı������
    private TextField R0 = new TextField();
    private TextField R1 = new TextField();
    private TextField R2 = new TextField();
    //��ѡ��
    private CheckBox chkLog = new CheckBox("��¼��־");
    //�������ı���
    private TextArea outDescription = new TextArea();
    //������ʼ��ť
    private Button startShow = new Button("��ʼ");
    //����ָ��������ʾ
    private String[] flagDescription = new String[6];
    private ObservableList<String> items =
            FXCollections.observableArrayList(command);
    //������Ͽ����ó�ʼֵΪ  MOV
    private ComboBox<String> selectCommand = new ComboBox<>();
    @Override
    public void start(Stage primaryStage){
        BorderPane borderPane = new BorderPane();//�����
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
        //�����������
        R0.setPrefWidth(160);
        R1.setPrefWidth(160);
        R2.setPrefWidth(160);
        //��CtrlGRidPane�������Ԫ��
        ctrlGridPane.add(new Label("R0:"),0,0);
        ctrlGridPane.add(R0,1,0);
        ctrlGridPane.add(new Label("R1:"),2,0);
        ctrlGridPane.add(R1,3,0);
        ctrlGridPane.add(new Label("R2:"),4,0);
        ctrlGridPane.add(R2,5,0);
        ctrlGridPane.add(selectCommand,6,0);
        ctrlGridPane.add(startShow,7,0);
        //����Ĭ�ϲ���ѡ
        chkLog.setSelected(false);
        ctrlGridPane.add(chkLog,8,0);


        outDescription.setWrapText(true);
        outDescription.setEditable(false);

        flagDescription[0] = "MOVָ��\n" +
                "  ȡָ�׶Σ�\n" +
                "    ���������PCװ���һ��ָ��ĵ�ַ101��PC�����ݱ��ŵ�ָ���ַ�����ϣ���ָ��������벢�����������101�ŵ�ַ����MOVָ�ͨ��ָ������IBusװ��ָ��Ĵ���IR�����������PC���ݼ�1�����102��Ϊ��һ��ָ������׼����ָ��Ĵ���IR�еĲ����뱻���룬CPUʶ�����MOVָ�����ȡָ�׶���ɡ�\n" +
                "  ִ�н׶Σ�\n" +
                //"    ����������OC�ͳ������źŵ�ͨ�üĴ�����ѡ��R1��10��ΪԴ�Ĵ�����RO��00��ΪĿ��Ĵ�����OC�ͳ������źŵ�ALU��ָ��ALU�����Ͳ�������ALU�����̬�ţ���ALU�����10���͵���������DBus�ϣ��κ�ʱ��DBus��ֻ����һ�����ݡ���DBus�ϵ����ݴ������ݻ���Ĵ���DR����DR�е����ݴ���Ŀ��Ĵ���RO��RO��������00��Ϊ10����MOVָ��ִ����ϡ�";
                "    ����������OC�ͳ������źŵ�ͨ�üĴ�����ѡ��R1ΪԴ�Ĵ�����ROΪĿ��Ĵ�����OC�ͳ������źŵ�ALU��ָ��ALU�����Ͳ�������ALU�����̬�ţ���ALU����͵���������DBus�ϣ��κ�ʱ��DBus��ֻ����һ�����ݡ���DBus�ϵ����ݴ������ݻ���Ĵ���DR����DR�е����ݴ���Ŀ��Ĵ���RO��RO�����ݱ�ΪR1�е���������MOVָ��ִ����ϡ�\n";
        flagDescription[1] = "LADָ��\n" +
                "  ȡָ�׶Σ�\n" +
                "    LADָ���ȡָ�׶κ�MOVָ����ȫ��ͬ��\n" +
                "  ִ�н׶Σ�\n" +
                "    OC�������������IR�����̬�ţ���ָ���е�ֱ�ӵ�ַ��6�ŵ���������DBus�ϣ�װ���ַ�Ĵ���AR��������6�ŵ�Ԫ�е���������DBus�ϣ�װ�뻺��Ĵ���DR����DR�е��� װ��ͨ�üĴ���R1��ԭ��R1�е�ֵ�����ǣ�����LADָ��ִ����ϡ�\n";
        flagDescription[2] = "ADDָ��\n" +
                "  ȡָ�׶Σ�\n" +
                "    ADDָ���ȡָ�׶κ�����ָ����ͬ��\n" +
                "  ִ�н׶Σ�\n" +
                "    ����������OC�ͳ������źŵ�ͨ�üĴ�����ѡ��R1ΪԴ�Ĵ�����R2ΪĿ��Ĵ�����ALU��R1��R2�ļӷ����㣬��ALU�����̬�ţ����������ŵ���������DBus�ϣ�Ȼ����뻺��Ĵ���DR��ALU�����Ľ�λ�źű�����״̬�ּĴ���PSW�У���DR����ֵװ��R2�У�R2ԭ�����������ǡ�����ADDָ��ִ�н�����\n";
        flagDescription[3] = "STOָ��\n" +
                "  ȡָ�׶Σ�\n" +
                "        STOָ���ȡָ�׶κ�����ָ����ͬ��\n" +
                "  ִ�н׶Σ�\n" +
                "        ����������OC�ͳ������źŵ�ͨ�üĴ�����ѡ��R3��30����Ϊ���ݴ洢���ĵ�ַ����ͨ�üĴ��������̬�ţ�����ַ30�ŵ�DBus�ϲ�װ���ַ�Ĵ���AR�������е�ַ���롣����������OC�ͳ������źŵ�ͨ�üĴ�����ѡ��R2��������Ϊ�����д�����ݷŵ�DBus�ϡ�������ֵд������30��Ԫ��ԭ�ȵ����ݱ����������STOָ��ִ�н�����\n";
        flagDescription[4] = "JMPָ��\n" +
                "    ȡָ�׶Σ�\n" +
                "        JMPָ���ȡָ���ں�����ָ����ͬ��\n" +
                "    ִ�н׶Σ�\n" +
                "        OC�������������IR�����̬�ţ���IR�еĵ�ַ��101���͵�DBus�ϣ���DBus�ϵĵ�ַ��101���뵽���������PC�У�PC��ԭ�ȵĵ�ַ106��������������һ��ָ��Ǵ�106��Ԫȡ��������ת�Ƶ�101��Ԫȡ��������JMPָ��ִ�����ڽ�����\n";
        flagDescription[5] = flagDescription[0] + flagDescription[1] + flagDescription[2] + flagDescription[3] + flagDescription[4];
        startShow.setOnAction(e -> {
            showCommand();
        });
        //��������
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
        primaryStage.setTitle("��������ԭ��CPUָ��ִ����ʾ���� ");
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
        pt.setDuration(Duration.seconds(10));//���ó���ʱ��10��
        pt.setPath(path);//����·��
        pt.setNode(PcShow);//��������
        //ȡַ�׶�
        pt.play();//��������
        pt.setOnFinished(e -> {
            PcsSetText("");//��ȡַ�׶����ʱ������PCS��ǩ���������
            //ִ�н׶�
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
        //�����������
        outDescription.setText(flagDescription[0]);
        //MOV ��Ƶ���ſؼ�
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
        pt.setDuration(Duration.seconds(35));//���ó���ʱ��20��
        pt.setPath(path);//����·��
        pt.setNode(PcShow);//��������
        pt.play();//��������
        PcSetText("102");
        pt.setOnFinished(e -> {
            R0LabelText(R1.getText());
            PcsSetText("");
        });
    }

    private void LADPlayer(){
        outDescription.setText(flagDescription[1]);
        //LAD ��Ƶ���ſؼ�
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
        pt.setDuration(Duration.seconds(11));//���ó���ʱ��11��
        pt.setPath(path);//����·��
        pt.setNode(PcShow);//��������

        pt1.setDuration(Duration.seconds(10));//���ó���ʱ��11��
        pt1.setPath(path1);//����·��
        pt1.setNode(PcShow);//��������
        pt.play();//��������
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
        //ADD ��Ƶ���ſؼ�
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
        pt.setDuration(Duration.seconds(3));//���ó���ʱ��20��
        pt.setPath(path);//����·��
        pt.setNode(PcShow);//��������

        PathTransition pt1=new PathTransition();
        pt1.setDuration(Duration.seconds(4));//���ó���ʱ��20��
        pt1.setPath(path1);//����·��
        pt1.setNode(PcShow);//��������

        PathTransition pt2=new PathTransition();
        pt2.setDuration(Duration.seconds(12));//���ó���ʱ��20��
        pt2.setPath(path2);//����·��
        pt2.setNode(PcShow);//��������

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
        //STO ��Ƶ���ſؼ�
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
        pt.setDuration(Duration.seconds(9));//���ó���ʱ��11��
        pt.setPath(path);//����·��
        pt.setNode(PcShow);//��������

        Path path1 = new Path();
        path1.getElements().add(new MoveTo(R2Label.getLayoutX(),R2Label.getLayoutY()));
        path1.getElements().add(new LineTo(230,160));
        path1.getElements().add(new HLineTo(313));
        path1.getElements().add(new VLineTo(16));
        path1.getElements().add(new HLineTo(446));
        path1.getElements().add(new VLineTo(232));
        PathTransition pt1=new PathTransition();
        pt1.setDuration(Duration.millis(10000));//���ó���ʱ��11��
        pt1.setPath(path1);//����·��
        pt1.setNode(PcShow);//��������

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
        //JMP ��Ƶ���ſؼ�
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
            pt.setDuration(Duration.seconds(8));//���ó���ʱ��8��
            pt.setPath(path);//����·��
            pt.setNode(PcShow);//��������
            pt.play();//��������
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
        //�ж�
        try {
            int r0 = Integer.parseInt(R0.getText());
            int r1 = Integer.parseInt(R1.getText());
            int r2 = Integer.parseInt(R2.getText());

            outDescription.setStyle("-fx-text-fill: BLACK");
            R0Label.setText(R0.getText());
            R1Label.setText(R1.getText());
            R2Label.setText(R2.getText());

            //���ִ�еĵ�������һ������
            //һ��ʼ���Ǽ�break������
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
            //��鸴ѡ��״̬
            if (chkLog.selectedProperty().getValue()) {
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//�������ڸ�ʽ
                String text = "=================================\n"
                        + "��ǰʱ��: " + df.format(new Date())
                        + "\nR0: " + R0.getText() + " R1: " + R1.getText() + " R2: " + R2.getText()
                        + "\n��ǰִ�е�����: " + selectCommand.getValue() + "\n��ѡ��״̬: " + chkLog.selectedProperty().getValue() + "\n";
                Write write = new Write();
                write.write(text, "./log.txt");
            }
            System.out.println("Happy Ending!!!");
        }catch (NumberFormatException e) {
            outDescription.setStyle("-fx-text-fill: RED");
            outDescription.setText("����R0,R1,R2��������Ч����!!!");
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