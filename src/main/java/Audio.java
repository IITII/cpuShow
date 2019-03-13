import javafx.scene.media.*;
//√ΩÃÂ¿‡
//2019-3-06
public class Audio implements ActionInterface.play,ActionInterface.pause,ActionInterface.stop{
    private String name = "MOV";
    String nameTmp = "audio/"+name+".mp3";
    private String name1 = getClass().getResource(nameTmp).toString();
    private Media mediaName = new Media(name1);
    private MediaPlayer mediaPlayer = new MediaPlayer(mediaName);
    public void play(){
        mediaPlayer.play();
    }
    public void pause(){
        mediaPlayer.pause();
    }
    public void stop(){
        mediaPlayer.stop();
    }

    public void setName(String name) {
        this.name = name;
    }
}
