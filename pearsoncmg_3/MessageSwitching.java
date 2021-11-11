import java.applet.Applet;
import java.awt.*;
// import java.awt.Event;
import javax.swing.*;
import java.awt.event.*;
import java.awt.image.*;

public class MessageSwitching extends JPanel implements Runnable, ActionListener {
    int data_size_input = 12;

    int packet_size_input = 4;

    boolean prop_delay_L1;

    boolean prop_delay_L2;

    boolean prop_delay_L3;

    Router A;

    Router B;

    Router C;

    Router D;

    Link L1;

    Link L2;

    Link L3;

    double time;

    Button m_start = new Button("Start");

    Button m_pause = new Button("Pause");

    Button m_resume = new Button("Resume");

    Button m_stop = new Button("Stop");

    AnimatedImage canvas_image;

    Panel input = new Panel();

    Panel choices = new Panel();

    Panel pan_choices = new Panel();

    Panel pan_animation = new Panel();

    GridBagLayout gridbag = new GridBagLayout();

    GridBagConstraints c = new GridBagConstraints();

    CheckboxGroup display = new CheckboxGroup();

    Checkbox textual = new Checkbox("Textual Simulation", this.display, false);

    Checkbox graphical = new Checkbox("Graphical Simulation", this.display, true);

    Checkbox chk_prop_delay_L1 = new Checkbox("L1");

    Checkbox chk_prop_delay_L2 = new Checkbox("L2");

    Checkbox chk_prop_delay_L3 = new Checkbox("L3");

    Label l_data_size = new Label("Data Size (bit): ", 1);

    Label l_packet_size = new Label("Packet Size (bit): ", 1);

    Label l_set_ms_in_ps = new Label("* For message switching, set packet size equal to message size.", 0);

    Label l_prop_delay = new Label("Optional Propagation Delay: ");

    Label l_simu_speed = new Label("Simulation Speed: ");

    Label l_simu_fast = new Label("fast", 0);

    Label l_simu_slow = new Label("slow", 2);

    Label l_blank = new Label("", 0);

    Choice cbo_data_size = new Choice();

    Choice cbo_packet_size = new Choice();

    Scrollbar sco_simu_speed = new Scrollbar(0, 10, 1, 1, 20);

    Image offScreenImage;
//    BufferedImage offScreenImage; // 是Image的子类，可以不用改

    Thread m_MessageSwitching;

    public String getAppletInfo() {
        return "Name: MessageSwitching\r\n" + "Author: Albert Huang\r\n"
                + "Created with Microsoft Visual J++ Version 1.01";
    }

    public MessageSwitching() {
        // resize(560, 465);
        setSize(560, 465);
        setVisible(true);
//        this.offScreenImage = createImage(560, 430);  用不了，返回NULL，未知原因
        this.offScreenImage = new BufferedImage(560, 430, BufferedImage.TYPE_4BYTE_ABGR); // 反正用途是双缓冲，这个也一样
        setBackground(Color.white);
        this.pan_choices.setLayout(new GridLayout(0, 4));
        setupChoice(this.cbo_data_size, 16, "12");
        setupChoice(this.cbo_packet_size, 16, "4");
        this.pan_choices.add(new Label("Message Size (kbits): ", 1));
        this.pan_choices.add(this.cbo_data_size);
        this.pan_choices.add(new Label("Packet Size (kbits): ", 1));
        this.pan_choices.add(this.cbo_packet_size);
        this.choices.setLayout(new CardLayout());
        this.choices.setLayout(new CardLayout());
        this.choices.add("DataConfig", this.pan_choices);
        this.c.weightx = 1.0D;
        this.c.anchor = 17;
        this.c.gridwidth = 0;
        this.c.fill = 1;
        this.gridbag.setConstraints(this.textual, this.c);
        this.gridbag.setConstraints(this.graphical, this.c);
        this.gridbag.setConstraints(this.choices, this.c);
        this.c.gridwidth = 0;
        this.gridbag.setConstraints(this.l_set_ms_in_ps, this.c);
        this.c.gridwidth = 1;
        this.gridbag.setConstraints(this.l_prop_delay, this.c);
        this.gridbag.setConstraints(this.chk_prop_delay_L1, this.c);
        this.gridbag.setConstraints(this.chk_prop_delay_L2, this.c);
        this.c.gridwidth = 0;
        this.gridbag.setConstraints(this.chk_prop_delay_L3, this.c);
        this.c.gridwidth = 1;
        this.gridbag.setConstraints(this.l_simu_speed, this.c);
        this.gridbag.setConstraints(this.l_simu_slow, this.c);
        this.gridbag.setConstraints(this.sco_simu_speed, this.c);
        this.c.gridwidth = 0;
        this.gridbag.setConstraints(this.l_simu_fast, this.c);
        this.c.gridwidth = 0;
        this.gridbag.setConstraints(this.l_blank, this.c);
        this.c.gridwidth = 1;
        this.c.fill = 2;
        this.c.weightx = 0.5D;
        this.gridbag.setConstraints(this.m_start, this.c);
        this.gridbag.setConstraints(this.m_pause, this.c);
        this.gridbag.setConstraints(this.m_resume, this.c);
        this.c.gridwidth = 0;
        this.gridbag.setConstraints(this.m_stop, this.c);
        start();
    }

    // public void destroy() {
    // }

    public void paint(Graphics g) {
    }

    public void start() {
        this.input.setLayout(this.gridbag);
        this.input.add(this.choices);
        this.input.add(this.l_set_ms_in_ps);
        this.input.add(this.l_prop_delay);
        this.input.add(this.chk_prop_delay_L1);
        this.input.add(this.chk_prop_delay_L2);
        this.input.add(this.chk_prop_delay_L3);
        this.input.add(this.l_simu_speed);
        this.input.add(this.l_simu_slow);
        this.input.add(this.sco_simu_speed);
        this.input.add(this.l_simu_fast);
        this.input.add(this.l_blank);
        this.input.add(this.m_start);
        this.input.add(this.m_pause);
        this.input.add(this.m_resume);
        this.input.add(this.m_stop);
        this.m_pause.setEnabled(false);
        this.m_resume.setEnabled(false);
        this.m_stop.setEnabled(false);
        // this.pan_animation.resize(500, 430);
        this.pan_animation.setSize(500, 430);
        this.pan_animation.setLayout(new BorderLayout());
        setLayout(new BorderLayout());
        add("North", this.input);
        add("Center", this.pan_animation);
        this.A = null;
        this.B = null;
        this.C = null;
        this.D = null;
        this.L1 = null;
        this.L2 = null;
        this.L3 = null;
        if (this.canvas_image != null)
            this.pan_animation.remove(this.canvas_image);
        this.canvas_image = null;
        this.m_MessageSwitching = null;
        this.A = new Router(this.data_size_input, this.packet_size_input, "Sender");
        this.B = new Router(0, this.packet_size_input, "B");
        this.C = new Router(0, this.packet_size_input, "C");
        this.D = new Router(0, this.packet_size_input, "Receiver");
        this.L1 = new Link(this.A, this.B, this.prop_delay_L1);
        this.L2 = new Link(this.B, this.C, this.prop_delay_L2);
        this.L3 = new Link(this.C, this.D, this.prop_delay_L3);
        this.canvas_image = new AnimatedImage(this.A, this.B, this.C, this.D, this.time, this.L1, this.L2, this.L3, this.offScreenImage, this.textual.getState());
        this.pan_animation.add("Center", this.canvas_image);
        this.canvas_image.repaint();
    }

    public void stop() {
        if (this.m_MessageSwitching != null) {
            // this.m_MessageSwitching.stop();
            this.m_MessageSwitching.interrupt();
            this.m_MessageSwitching = null;
        }
    }

    @Override
    public void run() {
        while (this.D.bit_stream.queue != ((1 << this.data_size_input) - 1)) {
            this.L1.moveBit();
            this.L2.moveBit();
            this.L3.moveBit();
            this.time += 0.25D;
            try {
                this.canvas_image.updateImage(this.A, this.B, this.C, this.D, this.time, this.L1, this.L2, this.L3);
                this.canvas_image.repaint();
                Thread.sleep((5000 - 250 * this.sco_simu_speed.getValue()));
            } catch (InterruptedException interruptedException) {
                stop();
            }
        }
        enableControls();
        this.m_start.setEnabled(true);
        this.m_pause.setEnabled(false);
        this.m_resume.setEnabled(false);
        this.m_stop.setEnabled(false);
    }

    public void setupChoice(Choice ch, int size, String optimal) {
        for (int i = 1; i < size + 1; i++)
            ch.addItem(Integer.toString(i));
        ch.select(optimal);
    }

    @Override
    public void actionPerformed(ActionEvent e){
        action(e, null);
    }

    public boolean action(ActionEvent e, Object arg) {
        if (e.getSource() == this.graphical) {
            this.data_size_input = Integer.parseInt(this.cbo_data_size.getSelectedItem());
            this.packet_size_input = Integer.parseInt(this.cbo_packet_size.getSelectedItem());
            return true;
        }
        if (e.getSource() == this.textual) {
            this.data_size_input = Integer.parseInt(this.cbo_data_size.getSelectedItem());
            this.packet_size_input = Integer.parseInt(this.cbo_packet_size.getSelectedItem());
            return true;
        }
        if (e.getSource() == this.cbo_data_size) {
            this.data_size_input = Integer.parseInt(this.cbo_data_size.getSelectedItem());
            return true;
        }
        if (e.getSource() == this.cbo_packet_size) {
            this.packet_size_input = Integer.parseInt(this.cbo_packet_size.getSelectedItem());
            return true;
        }
        if (e.getSource() == this.chk_prop_delay_L1) {
            this.prop_delay_L1 = this.chk_prop_delay_L1.getState();
            return true;
        }
        if (e.getSource() == this.chk_prop_delay_L2) {
            this.prop_delay_L2 = this.chk_prop_delay_L2.getState();
            return true;
        }
        if (e.getSource() == this.chk_prop_delay_L3) {
            this.prop_delay_L3 = this.chk_prop_delay_L3.getState();
            return true;
        }
        if (e.getSource() == this.m_pause) {
            this.m_start.setEnabled(false);
            this.m_pause.setEnabled(false);
            this.m_resume.setEnabled(true);
            this.m_stop.setEnabled(false);
            // this.m_MessageSwitching.suspend();
            try {
                // synchronized(this.m_MessageSwitching){
                    this.m_MessageSwitching.wait();
                // }
            } catch (Exception error) {
                error.printStackTrace();
            }
            return true;
        }
        if (e.getSource() == this.m_resume) {
            this.m_start.setEnabled(false);
            this.m_pause.setEnabled(true);
            this.m_resume.setEnabled(false);
            this.m_stop.setEnabled(true);
            // this.m_MessageSwitching.resume();
            this.m_MessageSwitching.notify();
            return true;
        }
        if (e.getSource() == this.m_stop) {
            enableControls();
            this.m_start.setEnabled(true);
            this.m_pause.setEnabled(false);
            this.m_resume.setEnabled(false);
            this.m_stop.setEnabled(false);
            // this.m_MessageSwitching.stop();
            this.m_MessageSwitching.interrupt();
            return true;
        }
        if (e.getSource() == this.m_start) {
            disableControls();
            this.m_start.setEnabled(false);
            this.m_pause.setEnabled(true);
            this.m_resume.setEnabled(false);
            this.m_stop.setEnabled(true);
            this.A = null;
            this.B = null;
            this.C = null;
            this.D = null;
            this.L1 = null;
            this.L2 = null;
            this.L3 = null;
            if (this.canvas_image != null)
                this.pan_animation.remove(this.canvas_image);
            this.canvas_image = null;
            this.m_MessageSwitching = null;
            this.A = new Router(this.data_size_input, this.packet_size_input, "Sender");
            this.B = new Router(0, this.packet_size_input, "B");
            this.C = new Router(0, this.packet_size_input, "C");
            this.D = new Router(0, this.packet_size_input, "Receiver");
            this.L1 = new Link(this.A, this.B, this.prop_delay_L1);
            this.L2 = new Link(this.B, this.C, this.prop_delay_L2);
            this.L3 = new Link(this.C, this.D, this.prop_delay_L3);
            this.canvas_image = new AnimatedImage(this.A, this.B, this.C, this.D, this.time, this.L1, this.L2, this.L3,
                    this.offScreenImage, this.textual.getState());
            this.pan_animation.add("Center", this.canvas_image);
            this.time = 0.0D;
            this.A.dequeue();
            if (this.m_MessageSwitching == null) {
                this.m_MessageSwitching = new Thread(this);
                this.m_MessageSwitching.start();
            }
            return true;
        }
        return false;
    }

    public void disableControls() {
        this.textual.setEnabled(false);
        this.graphical.setEnabled(false);
        this.cbo_data_size.setEnabled(false);
        this.cbo_packet_size.setEnabled(false);
        this.chk_prop_delay_L1.setEnabled(false);
        this.chk_prop_delay_L2.setEnabled(false);
        this.chk_prop_delay_L3.setEnabled(false);
        this.sco_simu_speed.setEnabled(false);
    }

    public void enableControls() {
        this.textual.setEnabled(true);
        this.graphical.setEnabled(true);
        this.cbo_data_size.setEnabled(true);
        this.cbo_packet_size.setEnabled(true);
        this.chk_prop_delay_L1.setEnabled(true);
        this.chk_prop_delay_L2.setEnabled(true);
        this.chk_prop_delay_L3.setEnabled(true);
        this.sco_simu_speed.setEnabled(true);
    }
}

class Link {
    Router source;

    Router destination;

    boolean[] bit_pos;

    boolean[] last_bit;

    int[] packet_size;

    int[] packet_number;

    boolean prop_status;

    public Link(Router source, Router destination, boolean prop_status) {
        this.bit_pos = new boolean[4];
        this.last_bit = new boolean[4];
        this.packet_size = new int[4];
        this.packet_number = new int[4];
        this.source = source;
        this.destination = destination;
        this.prop_status = prop_status;
        int i = 0;
        do {
            this.bit_pos[i] = false;
            this.last_bit[i] = false;
            this.packet_size[i] = 0;
            this.packet_number[i] = 0;
        } while (++i < 4);
    }

    public void moveBit() {
        emitBit();
        if (this.prop_status) {
            if (this.bit_pos[3]) {
                this.bit_pos[3] = false;
                this.destination.bit_stream.rec_packet = (this.destination.bit_stream.rec_packet << 1) + 1;
                this.destination.rec_packet_size = this.packet_size[3];
                this.destination.rec_packet_number = this.packet_number[3];
            }
            if (this.bit_pos[2]) {
                this.bit_pos[2] = false;
                this.bit_pos[3] = true;
                this.packet_size[3] = this.packet_size[2];
                this.packet_number[3] = this.packet_number[2];
            }
            if (this.bit_pos[1]) {
                this.bit_pos[1] = false;
                this.bit_pos[2] = true;
                this.packet_size[2] = this.packet_size[1];
                this.packet_number[2] = this.packet_number[1];
            }
            if (this.bit_pos[0]) {
                this.bit_pos[0] = false;
                this.bit_pos[1] = true;
                this.packet_size[1] = this.packet_size[0];
                this.packet_number[1] = this.packet_number[0];
            }
            if (this.source.bit_to_sent == 1) {
                this.source.bit_to_sent = 0;
                this.bit_pos[0] = true;
                this.packet_size[0] = this.source.tran_packet_size;
                this.packet_number[0] = this.source.tran_packet_number;
            }
        }
        if (!this.prop_status && this.source.bit_to_sent == 1) {
            this.source.bit_to_sent = 0;
            this.destination.bit_stream.rec_packet = (this.destination.bit_stream.rec_packet << 1) + 1;
            this.destination.rec_packet_size = this.source.tran_packet_size;
            this.destination.rec_packet_number = this.source.tran_packet_number;
        }
        if (this.destination.rec_packet_size == this.destination.rec_packet_number) {
            this.destination.enqueue();
            this.destination.rec_packet_size = 0;
            this.destination.rec_packet_number = 0;
        }
        if (this.source.bit_stream.cur_packet == 0)
            this.source.dequeue();
    }

    private void emitBit() {
        if (this.source.bit_stream.cur_packet > 0) {
            this.source.bit_to_sent = 1;
            this.source.tran_packet_size = this.source.bit_stream.cur_packet_size;
            this.source.tran_packet_number = this.source.tran_packet_size
                    - this.source.bit_stream.bitNotSentInCurPacket() + 1;
            this.source.bit_stream.cur_packet >>= 1;
        }
    }
}

class Data {
    int rec_packet;

    int packet_size;

    int data_size;

    int cur_packet_temp;

    int std_packet;

    int cur_packet;

    int cur_packet_size;

    long queue;

    public Data(int data_size, int packet_size, String s) {
        this.packet_size = packet_size;
        this.data_size = data_size;
        this.std_packet = (1 << packet_size) - 1;
        if (s.equalsIgnoreCase("Sender")) {
            this.queue = ((1 << data_size) - 1);
        } else {
            this.queue = 0L;
        }
    }

    public void nextPacket() {
        if (isEntirePacketSent())
            if (this.queue > this.std_packet) {
                this.cur_packet = this.std_packet;
                this.cur_packet_size = this.packet_size;
                this.queue >>>= this.packet_size;
            } else {
                this.cur_packet = (int) this.queue;
                this.cur_packet_size = bitNotSentInCurPacket();
                this.queue = 0L;
            }
        this.cur_packet_temp = this.cur_packet;
    }

    public int bitNotSentInCurPacket() {
        int cur_packet_clone = this.cur_packet;
        int bitNotSent = 0;
        while (cur_packet_clone > 0) {
            cur_packet_clone >>= 1;
            bitNotSent++;
        }
        return bitNotSent;
    }

    public int bitReceivedFromCurPacket() {
        int rec_packet_clone = this.rec_packet;
        int bitReceived = 0;
        while (rec_packet_clone > 0) {
            rec_packet_clone >>= 1;
            bitReceived++;
        }
        return bitReceived;
    }

    public int bitNotSentInQueue() {
        long queue_clone = this.queue;
        int bitNotSent = 0;
        while (queue_clone > 0L) {
            queue_clone >>= 1L;
            bitNotSent++;
        }
        return bitNotSent;
    }

    public boolean isEntirePacketSent() {
        if (this.cur_packet == 0)
            return true;
        return false;
    }

    public boolean isEntireQueueConsumed() {
        if (this.queue == 0L)
            return true;
        return false;
    }
}

class Router {
    String name;

    Data bit_stream;

    int tran_packet_size;

    int tran_packet_number;

    int rec_packet_size;

    int rec_packet_number;

    byte bit_to_sent;

    public Router(int data_size, int packet_size, String s) {
        this.name = s;
        this.bit_stream = new Data(data_size, packet_size, s);
    }

    public void dequeue() {
        this.bit_stream.nextPacket();
    }

    public void enqueue() {
        this.bit_stream.queue = (this.bit_stream.rec_packet << this.bit_stream.bitNotSentInQueue())
                | this.bit_stream.queue;
        this.bit_stream.rec_packet = 0;
    }
}
