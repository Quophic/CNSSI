import javax.swing.*;

public class Entry extends JFrame{
    public static void main(String args[]) {
        new Entry();
    }

    public Entry(){
        setVisible(true);
        setLocation(200, 100);
        setSize(600, 130);
        add(new LineSimApplet());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
}
