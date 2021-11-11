import javax.swing.*;

// 程序入口
public class Entry extends JFrame {
    public static void main(String[] args){
        new Entry();
    }

    public Entry(){
        setVisible(true);
        setLocation(200, 100);
        setSize(560, 465);
        add(new MessageSwitching());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
}
