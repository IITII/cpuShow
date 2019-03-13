import javax.imageio.ImageIO;
import java.io.*;
import java.awt.image.*;
import java.awt.*;

public class CreImage {
    //String inputNumber = "6";
    /*private Integer intInputNumber = 0; //Ĭ��ͼƬ������
    private String strInputPath = "./test.png";//Ĭ�ϴ��·��*/

    public void getImages(String strInputNumber, String strInputPath) {
        //this.inputNumber = inputNumber; //inputNumber��ͼƬ������
        //this.strInputPath = strInputPath; //strInputPath��Ĭ���ļ����·��

        //String chrInputName = null;
        //chrInputNumber = inputNumber.toString();
        try {
            int width = 81;
            int height = 24;
            // ����BufferedImage����
            Font font = new Font("����", Font.PLAIN, 16);
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            // ��ȡGraphics2D
            Graphics2D g2d = image.createGraphics();
            // ��ͼ
            g2d.setBackground(new Color(255, 255, 255));
            g2d.setPaint(new Color(0, 0, 0));
            g2d.clearRect(0, 0, width, height);
            g2d.drawString(strInputNumber, 36, 10);
            g2d.setFont(font);
            //�ͷŶ���
            g2d.dispose();
            // �����ļ�
            ImageIO.write(image, "png", new File(strInputPath));
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}