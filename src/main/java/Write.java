/*
* �ļ�д����
* */
import java.io.FileWriter;
import java.io.IOException;
public class Write {
    private String text = null;
    private String filename = null;
    public void write(String write, String fileName){
        this.text = write;
        this.filename = fileName;
        try{
            // ��һ��д�ļ��������캯���еĵڶ�������true��ʾ��׷����ʽд�ļ�
            FileWriter writer = new FileWriter(fileName, true);
            writer.write(text);
            writer.close();
        }catch (IOException e){
            System.err.println("�ļ�д�����!!!");
            //outDescription.setText("�ļ�д�����!!!");

        }
    }
}
