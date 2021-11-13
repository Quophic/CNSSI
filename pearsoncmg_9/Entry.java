import javax.swing.JFrame;

public class Entry extends JFrame {
    public static void main(String[] args){
        new Entry();
    }

    public Entry(){
        setVisible(true);
        setLocation(200, 100);
        setSize(666, 439);
        add(new Enet());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
}
