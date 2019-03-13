import javax.imageio.ImageIO;
import java.io.*;
import java.awt.image.*;
import java.awt.*;

public class CreImage {
    //String inputNumber = "6";
    /*private Integer intInputNumber = 0; //默认图片中文字
    private String strInputPath = "./test.png";//默认存放路径*/

    public void getImages(String strInputNumber, String strInputPath) {
        //this.inputNumber = inputNumber; //inputNumber：图片中文字
        //this.strInputPath = strInputPath; //strInputPath：默认文件存放路径

        //String chrInputName = null;
        //chrInputNumber = inputNumber.toString();
        try {
            int width = 81;
            int height = 24;
            // 创建BufferedImage对象
            Font font = new Font("宋体", Font.PLAIN, 16);
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            // 获取Graphics2D
            Graphics2D g2d = image.createGraphics();
            // 画图
            g2d.setBackground(new Color(255, 255, 255));
            g2d.setPaint(new Color(0, 0, 0));
            g2d.clearRect(0, 0, width, height);
            g2d.drawString(strInputNumber, 36, 10);
            g2d.setFont(font);
            //释放对象
            g2d.dispose();
            // 保存文件
            ImageIO.write(image, "png", new File(strInputPath));
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}