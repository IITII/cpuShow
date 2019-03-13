/*
* 文件写入类
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
            // 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
            FileWriter writer = new FileWriter(fileName, true);
            writer.write(text);
            writer.close();
        }catch (IOException e){
            System.err.println("文件写入错误!!!");
            //outDescription.setText("文件写入错误!!!");

        }
    }
}
