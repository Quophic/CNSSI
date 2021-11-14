import java.awt.*;
// import java.awt.Event;
import javax.swing.*;
import java.awt.event.*;
import java.awt.image.*;

public class MessageSwitching extends JPanel implements Runnable, ActionListener, ItemListener {
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
        this.chk_prop_delay_L1.addItemListener(this);
        this.input.add(this.chk_prop_delay_L2);
        this.chk_prop_delay_L2.addItemListener(this);
        this.input.add(this.chk_prop_delay_L3);
        this.chk_prop_delay_L3.addItemListener(this);
        this.input.add(this.l_simu_speed);
        this.input.add(this.l_simu_slow);
        this.input.add(this.sco_simu_speed);
        this.input.add(this.l_simu_fast);
        this.input.add(this.l_blank);
        this.input.add(this.m_start);
        this.m_start.addActionListener(this);
        this.input.add(this.m_pause);
        this.m_pause.addActionListener(this);
        this.input.add(this.m_resume);
        this.m_resume.addActionListener(this);
        this.input.add(this.m_stop);
        this.m_stop.addActionListener(this);
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
        ch.addItemListener(this); // 添加侦听器
    }

    @Override
    public void actionPerformed(ActionEvent e){
        if (e.getSource() == this.m_pause) {
            this.m_start.setEnabled(false);
            this.m_pause.setEnabled(false);
            this.m_resume.setEnabled(true);
            this.m_stop.setEnabled(false);
            // this.m_MessageSwitching.suspend();
            try {
                synchronized(this.m_MessageSwitching){
                    this.m_MessageSwitching.wait();
                }
            } catch (Exception error) {
                error.printStackTrace();
            }
        }
        if (e.getSource() == this.m_resume) {
            this.m_start.setEnabled(false);
            this.m_pause.setEnabled(true);
            this.m_resume.setEnabled(false);
            this.m_stop.setEnabled(true);
            // this.m_MessageSwitching.resume();
            this.m_MessageSwitching.notify();
        }
        if (e.getSource() == this.m_stop) {
            enableControls();
            this.m_start.setEnabled(true);
            this.m_pause.setEnabled(false);
            this.m_resume.setEnabled(false);
            this.m_stop.setEnabled(false);
            // this.m_MessageSwitching.stop();
            this.m_MessageSwitching.interrupt();
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
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e){
        if (e.getItemSelectable() == this.graphical) {
            this.data_size_input = Integer.parseInt(this.cbo_data_size.getSelectedItem());
            this.packet_size_input = Integer.parseInt(this.cbo_packet_size.getSelectedItem());
        }
        if (e.getItemSelectable() == this.textual) {
            this.data_size_input = Integer.parseInt(this.cbo_data_size.getSelectedItem());
            this.packet_size_input = Integer.parseInt(this.cbo_packet_size.getSelectedItem());
        }
        if (e.getItemSelectable() == this.cbo_data_size) {
            this.data_size_input = Integer.parseInt(this.cbo_data_size.getSelectedItem());
        }
        if (e.getItemSelectable() == this.cbo_packet_size) {
            this.packet_size_input = Integer.parseInt(this.cbo_packet_size.getSelectedItem());
        }
        if (e.getItemSelectable() == this.chk_prop_delay_L1) {
            this.prop_delay_L1 = this.chk_prop_delay_L1.getState();
        }
        if (e.getItemSelectable() == this.chk_prop_delay_L2) {
            this.prop_delay_L2 = this.chk_prop_delay_L2.getState();
        }
        if (e.getItemSelectable() == this.chk_prop_delay_L3) {
            this.prop_delay_L3 = this.chk_prop_delay_L3.getState();
        }
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


class AnimatedImage extends Canvas {
    Router A;

    Router B;

    Router C;

    Router D;

    double time;

    Link L1;

    Link L2;

    Link L3;

    Dimension d;

    Image offScreenImage;

    Graphics offScreenGraphics;

    boolean isText;

    public AnimatedImage(Router A, Router B, Router C, Router D, double time, Link L1, Link L2, Link L3,
            Image offScreenImage, boolean isText) {
        // resize(560, 315);
        setSize(560, 315);
        // this.d = size();
        this.d = getSize();
        this.A = A;
        this.B = B;
        this.C = C;
        this.D = D;
        this.time = time;
        this.L1 = L1;
        this.L2 = L2;
        this.L3 = L3;
        this.offScreenImage = offScreenImage;
//        if(offScreenImage == null){
//            System.out.println("offScreen ");
//        }
        this.offScreenGraphics = offScreenImage.getGraphics();
        this.isText = isText;
    }

    public Dimension preferredSize() {
        return new Dimension(560, 315);
    }

    public Dimension minimumSize() {
        return new Dimension(560, 295);
    }

    public void updateImage(Router A, Router B, Router C, Router D, double time, Link L1, Link L2, Link L3) {
        this.A = A;
        this.B = B;
        this.C = C;
        this.D = D;
        this.time = time;
        this.L1 = L1;
        this.L2 = L2;
        this.L3 = L3;
    }

    public void update(Graphics g) {
        paint(g);
    }

    public void paint(Graphics g) {
        if (this.isText) {
            this.offScreenGraphics.clearRect(0, 0, this.d.width, this.d.height);
            this.offScreenGraphics.setColor(Color.blue);
            this.offScreenGraphics.drawString("A:  current queue: ", 150, 30);
            this.offScreenGraphics.setColor(Color.black);
            this.offScreenGraphics.drawString(Integer.toBinaryString((int) this.A.bit_stream.queue), 280, 30);
            this.offScreenGraphics.setColor(Color.blue);
            this.offScreenGraphics.drawString("A:  current packet: ", 150, 45);
            this.offScreenGraphics.setColor(Color.black);
            this.offScreenGraphics.drawString(Integer.toBinaryString(this.A.bit_stream.cur_packet), 280, 45);
            this.offScreenGraphics.setColor(Color.red);
            this.offScreenGraphics.drawString("------------------------------------------------------", 150, 55);
            drawL1(this.offScreenGraphics);
            this.offScreenGraphics.setColor(Color.red);
            this.offScreenGraphics.drawString("------------------------------------------------------", 150, 85);
            this.offScreenGraphics.setColor(Color.blue);
            this.offScreenGraphics.drawString("B: received buffer: ", 150, 95);
            this.offScreenGraphics.setColor(Color.black);
            this.offScreenGraphics.drawString(Integer.toBinaryString(this.B.bit_stream.rec_packet), 280, 95);
            this.offScreenGraphics.setColor(Color.blue);
            this.offScreenGraphics.drawString("B:  current queue: ", 150, 110);
            this.offScreenGraphics.setColor(Color.black);
            this.offScreenGraphics.drawString(Integer.toBinaryString((int) this.B.bit_stream.queue), 280, 110);
            this.offScreenGraphics.setColor(Color.blue);
            this.offScreenGraphics.drawString("B: current packet: ", 150, 125);
            this.offScreenGraphics.setColor(Color.black);
            this.offScreenGraphics.drawString(Integer.toBinaryString(this.B.bit_stream.cur_packet), 280, 125);
            this.offScreenGraphics.setColor(Color.red);
            this.offScreenGraphics.drawString("------------------------------------------------------", 150, 135);
            drawL2(this.offScreenGraphics);
            this.offScreenGraphics.setColor(Color.red);
            this.offScreenGraphics.drawString("------------------------------------------------------", 150, 165);
            this.offScreenGraphics.setColor(Color.blue);
            this.offScreenGraphics.drawString("C: received buffer: ", 150, 175);
            this.offScreenGraphics.setColor(Color.black);
            this.offScreenGraphics.drawString(Integer.toBinaryString(this.C.bit_stream.rec_packet), 280, 175);
            this.offScreenGraphics.setColor(Color.blue);
            this.offScreenGraphics.drawString("C:  current queue: ", 150, 190);
            this.offScreenGraphics.setColor(Color.black);
            this.offScreenGraphics.drawString(Integer.toBinaryString((int) this.C.bit_stream.queue), 280, 190);
            this.offScreenGraphics.setColor(Color.blue);
            this.offScreenGraphics.drawString("C: current packet: ", 150, 205);
            this.offScreenGraphics.setColor(Color.black);
            this.offScreenGraphics.drawString(Integer.toBinaryString(this.C.bit_stream.cur_packet), 280, 205);
            this.offScreenGraphics.setColor(Color.red);
            this.offScreenGraphics.drawString("------------------------------------------------------", 150, 215);
            drawL3(this.offScreenGraphics);
            this.offScreenGraphics.setColor(Color.red);
            this.offScreenGraphics.drawString("------------------------------------------------------", 150, 245);
            this.offScreenGraphics.setColor(Color.blue);
            this.offScreenGraphics.drawString("D: received buffer: ", 150, 255);
            this.offScreenGraphics.setColor(Color.black);
            this.offScreenGraphics.drawString(Integer.toBinaryString(this.D.bit_stream.rec_packet), 280, 255);
            this.offScreenGraphics.setColor(Color.blue);
            this.offScreenGraphics.drawString("D:  current queue: ", 150, 270);
            this.offScreenGraphics.setColor(Color.black);
            this.offScreenGraphics.drawString(Integer.toBinaryString((int) this.D.bit_stream.queue), 280, 270);
            this.offScreenGraphics.setColor(Color.blue);
            this.offScreenGraphics.drawString("D: current packet: ", 150, 285);
            this.offScreenGraphics.setColor(Color.black);
            this.offScreenGraphics.drawString(Integer.toBinaryString(this.D.bit_stream.cur_packet), 280, 285);
            this.offScreenGraphics.setColor(Color.red);
            this.offScreenGraphics.drawString("------------------------------------------------------", 150, 295);
            this.offScreenGraphics.setColor(Color.blue);
            this.offScreenGraphics.drawString("TIME ELAPSED: ", 150, 305);
            this.offScreenGraphics.setColor(Color.red);
            this.offScreenGraphics.drawString(padding(Double.toString(this.time)), 280, 305);
            g.drawImage(this.offScreenImage, 0, 0, this);
        } else {
            this.offScreenGraphics.setColor(Color.pink);
            this.offScreenGraphics.fillRect(0, 0, this.d.width, this.d.height);
            drawRouter(this.offScreenGraphics, "A");
            drawRouter(this.offScreenGraphics, "B");
            drawRouter(this.offScreenGraphics, "C");
            drawRouter(this.offScreenGraphics, "D");
            drawGLine(this.offScreenGraphics, "L1");
            drawGLine(this.offScreenGraphics, "L2");
            drawGLine(this.offScreenGraphics, "L3");
            drawQ(this.offScreenGraphics, "A");
            drawQ(this.offScreenGraphics, "B");
            drawQ(this.offScreenGraphics, "C");
            drawQ(this.offScreenGraphics, "D");
            drawCurPacket(this.offScreenGraphics, "A");
            drawCurPacket(this.offScreenGraphics, "B");
            drawCurPacket(this.offScreenGraphics, "C");
            drawCurPacket(this.offScreenGraphics, "D");
            drawRecPacket(this.offScreenGraphics, "A");
            drawRecPacket(this.offScreenGraphics, "B");
            drawRecPacket(this.offScreenGraphics, "C");
            drawRecPacket(this.offScreenGraphics, "D");
            this.offScreenGraphics.setColor(Color.blue);
            String st_time = Double.toString(this.time);
            this.offScreenGraphics.drawString("TIME ELAPSED: " + padding(st_time), 220, 20);
            drawLegend(this.offScreenGraphics);
            g.drawImage(this.offScreenImage, 0, 0, this);
        }
    }

    public String padding(String s) {
        int num_decimal_digit = 0;
        boolean decimal = false;
        String newString = s;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '.') {
                num_decimal_digit = i;
                decimal = true;
            }
        }
        if (decimal == true)
            num_decimal_digit = s.length() - 1 - num_decimal_digit;
        switch (num_decimal_digit) {
        case 0:
            newString = s + ".00";
            return newString;
        case 1:
            newString = s + "0";
            return newString;
        }
        return newString;
    }

    public void drawL1(Graphics g) {
        g.setColor(Color.blue);
        g.drawString("L1: ", 150, 70);
        g.setColor(Color.darkGray);
        g.drawRect(280, 63, 15, 5);
        g.drawRect(310, 63, 15, 5);
        g.drawRect(340, 63, 15, 5);
        g.setColor(Color.cyan);
        if (this.L1.bit_pos[0]) {
            g.fillRect(281, 64, 14, 4);
        } else {
            g.clearRect(281, 64, 14, 4);
        }
        if (this.L1.bit_pos[1]) {
            g.fillRect(311, 64, 14, 4);
        } else {
            g.clearRect(311, 64, 14, 4);
        }
        if (this.L1.bit_pos[2]) {
            g.fillRect(341, 64, 14, 4);
        } else {
            g.clearRect(341, 64, 14, 4);
        }
    }

    public void drawL2(Graphics g) {
        g.setColor(Color.blue);
        g.drawString("L2: ", 150, 150);
        g.setColor(Color.darkGray);
        g.drawRect(280, 143, 15, 5);
        g.drawRect(310, 143, 15, 5);
        g.drawRect(340, 143, 15, 5);
        g.setColor(Color.cyan);
        if (this.L2.bit_pos[0]) {
            g.fillRect(281, 144, 14, 4);
        } else {
            g.clearRect(281, 144, 14, 4);
        }
        if (this.L2.bit_pos[1]) {
            g.fillRect(311, 144, 14, 4);
        } else {
            g.clearRect(311, 144, 14, 4);
        }
        if (this.L2.bit_pos[2]) {
            g.fillRect(341, 144, 14, 4);
        } else {
            g.clearRect(341, 144, 14, 4);
        }
    }

    public void drawL3(Graphics g) {
        g.setColor(Color.blue);
        g.drawString("L3: ", 150, 230);
        g.setColor(Color.darkGray);
        g.drawRect(280, 223, 15, 5);
        g.drawRect(310, 223, 15, 5);
        g.drawRect(340, 223, 15, 5);
        g.setColor(Color.cyan);
        if (this.L3.bit_pos[0]) {
            g.fillRect(281, 224, 14, 4);
        } else {
            g.clearRect(281, 224, 14, 4);
        }
        if (this.L3.bit_pos[1]) {
            g.fillRect(311, 224, 14, 4);
        } else {
            g.clearRect(311, 224, 14, 4);
        }
        if (this.L3.bit_pos[2]) {
            g.fillRect(341, 224, 14, 4);
        } else {
            g.clearRect(341, 224, 14, 4);
        }
    }

    public void drawRouter(Graphics g, String r) {
        int x = 17;
        int y = 190;
        int width = 60;
        int height = 50;
        if (r.equalsIgnoreCase("A")) {
            x = 17;
        } else if (r.equalsIgnoreCase("B")) {
            x += 155;
        } else if (r.equalsIgnoreCase("C")) {
            x += 310;
        } else if (r.equalsIgnoreCase("D")) {
            x += 465;
        }
        g.setColor(Color.blue);
        g.drawRect(x, y, width, height);
        if (!r.equalsIgnoreCase("A")) {
            g.setColor(Color.red);
            g.fillRect(x + 6, y + 5, 10, 41);
        }
        g.setColor(Color.yellow);
        g.fillRect(x + 26, y + 5, 10, 41);
        if (!r.equalsIgnoreCase("D")) {
            g.setColor(Color.green);
            g.fillRect(x + 46, y + 5, 10, 41);
        }
        g.setColor(Color.blue);
        Font font = new Font(getFont().getName(), 1, getFont().getSize());
        g.setFont(font);
        g.drawString(r, x + 28, y + 65);
        if (r.equalsIgnoreCase("A")) {
            g.drawString("Source", x + 12, y + 77);
        } else if (r.equalsIgnoreCase("D")) {
            g.drawString("Destination", x - 2, y + 77);
        }
    }

    public void drawGLine(Graphics g, String l) {
        int x = 78;
        int y = 212;
        int width = 94;
        int height = 5;
        if (l.equalsIgnoreCase("L1")) {
            x = 78;
            if (!this.L1.prop_status) {
                drawNPDLine(g, x, y, width, height);
            } else {
                drawPDLine(g, x, y, height, this.L1.bit_pos[0], this.L1.bit_pos[1], this.L1.bit_pos[2],
                        this.L1.bit_pos[3], this.L1.packet_number[0], this.L1.packet_number[1],
                        this.L1.packet_number[2], this.L1.packet_number[3]);
            }
            g.setColor(Color.black);
            g.drawString("L1", x + 40, y + 20);
        } else if (l.equalsIgnoreCase("L2")) {
            x += 155;
            if (!this.L2.prop_status) {
                drawNPDLine(g, x, y, width, height);
            } else {
                drawPDLine(g, x, y, height, this.L2.bit_pos[0], this.L2.bit_pos[1], this.L2.bit_pos[2],
                        this.L2.bit_pos[3], this.L2.packet_number[0], this.L2.packet_number[1],
                        this.L2.packet_number[2], this.L2.packet_number[3]);
            }
            g.setColor(Color.black);
            g.drawString("L2", x + 40, y + 20);
        } else if (l.equalsIgnoreCase("L3")) {
            x += 310;
            if (!this.L3.prop_status) {
                drawNPDLine(g, x, y, width, height);
            } else {
                drawPDLine(g, x, y, height, this.L3.bit_pos[0], this.L3.bit_pos[1], this.L3.bit_pos[2],
                        this.L3.bit_pos[3], this.L3.packet_number[0], this.L3.packet_number[1],
                        this.L3.packet_number[2], this.L3.packet_number[3]);
            }
            g.setColor(Color.black);
            g.drawString("L3", x + 40, y + 20);
        }
    }

    public void drawNPDLine(Graphics g, int x, int y, int width, int height) {
        g.setColor(Color.cyan);
        g.fillRect(x, y, width, height);
    }

    public void drawPDLine(Graphics g, int x, int y, int height, boolean p1, boolean p2, boolean p3, boolean p4,
            int p1_num, int p2_num, int p3_num, int p4_num) {
        g.setColor(Color.black);
        int i = 0;
        do {
            g.drawRect(x + 13 + 20 * i, y, 10, height);
        } while (++i < 4);
        g.setColor(Color.cyan);
        if (p1) {
            if (p1_num == 1)
                g.setColor(Color.blue);
            g.fillRect(x + 14, y + 1, 9, height - 1);
        }
        g.setColor(Color.cyan);
        if (p2) {
            if (p2_num == 1)
                g.setColor(Color.blue);
            g.fillRect(x + 34, y + 1, 9, height - 1);
        }
        g.setColor(Color.cyan);
        if (p3) {
            if (p3_num == 1)
                g.setColor(Color.blue);
            g.fillRect(x + 54, y + 1, 9, height - 1);
        }
        g.setColor(Color.cyan);
        if (p4) {
            if (p4_num == 1)
                g.setColor(Color.blue);
            g.fillRect(x + 74, y + 1, 9, height - 1);
        }
    }

    public void drawQ(Graphics g, String r) {
        int x = 42;
        int y = 165;
        int width = 10;
        int height = 5;
        int num_bit = 0;
        if (r.equalsIgnoreCase("A")) {
            x = 42;
            num_bit = this.A.bit_stream.bitNotSentInQueue();
        } else if (r.equalsIgnoreCase("B")) {
            x += 155;
            num_bit = this.B.bit_stream.bitNotSentInQueue();
        } else if (r.equalsIgnoreCase("C")) {
            x += 310;
            num_bit = this.C.bit_stream.bitNotSentInQueue();
        } else if (r.equalsIgnoreCase("D")) {
            x += 465;
            num_bit = this.D.bit_stream.bitNotSentInQueue();
        }
        int i;
        for (i = 0; i < this.A.bit_stream.data_size; i++) {
            g.setColor(Color.black);
            g.drawRect(x, y - i * 9, width, height);
        }
        g.setColor(Color.black);
        g.drawString("B", x + 2, y + 20);
        for (i = 0; i < num_bit; i++) {
            g.setColor(Color.yellow);
            g.fillRect(x + 1, y - i * 9 + 1, width - 1, height - 1);
        }
    }

    public void drawCurPacket(Graphics g, String r) {
        int x = 62;
        int y = 165;
        int width = 10;
        int height = 5;
        int num_bit = 0;
        int cur_packet_size = 0;
        if (r.equalsIgnoreCase("A")) {
            x = 62;
            num_bit = this.A.bit_stream.bitNotSentInCurPacket();
            cur_packet_size = this.A.bit_stream.cur_packet_size;
        } else if (r.equalsIgnoreCase("B")) {
            x += 155;
            num_bit = this.B.bit_stream.bitNotSentInCurPacket();
            cur_packet_size = this.B.bit_stream.cur_packet_size;
        } else if (r.equalsIgnoreCase("C")) {
            x += 310;
            num_bit = this.C.bit_stream.bitNotSentInCurPacket();
            cur_packet_size = this.C.bit_stream.cur_packet_size;
        } else if (r.equalsIgnoreCase("D")) {
            x += 465;
            num_bit = this.D.bit_stream.bitNotSentInCurPacket();
            cur_packet_size = this.D.bit_stream.cur_packet_size;
        }
        int i;
        for (i = 0; i < cur_packet_size; i++) {
            g.setColor(Color.black);
            g.drawRect(x, y - i * 9, width, height);
        }
        if (!r.equalsIgnoreCase("D")) {
            g.setColor(Color.black);
            g.drawString("T", x + 2, y + 20);
        }
        for (i = 0; i < num_bit; i++) {
            g.setColor(Color.green);
            g.fillRect(x + 1, y - i * 9 + 1, width - 1, height - 1);
        }
    }

    public void drawRecPacket(Graphics g, String r) {
        int x = 22;
        int y = 165;
        int width = 10;
        int height = 5;
        int num_bit = 0;
        int rec_packet_size = 0;
        if (r.equalsIgnoreCase("A")) {
            x = 22;
            num_bit = this.A.bit_stream.bitReceivedFromCurPacket();
            rec_packet_size = 0;
        } else if (r.equalsIgnoreCase("B")) {
            x += 155;
            num_bit = this.B.bit_stream.bitReceivedFromCurPacket();
            rec_packet_size = this.B.rec_packet_size;
        } else if (r.equalsIgnoreCase("C")) {
            x += 310;
            num_bit = this.C.bit_stream.bitReceivedFromCurPacket();
            rec_packet_size = this.C.rec_packet_size;
        } else if (r.equalsIgnoreCase("D")) {
            x += 465;
            num_bit = this.D.bit_stream.bitReceivedFromCurPacket();
            rec_packet_size = this.D.rec_packet_size;
        }
        int i;
        for (i = 0; i < rec_packet_size; i++) {
            g.setColor(Color.black);
            g.drawRect(x, y - i * 9, width, height);
        }
        if (!r.equalsIgnoreCase("A")) {
            g.setColor(Color.black);
            g.drawString("R", x + 2, y + 20);
        }
        for (i = 0; i < num_bit; i++) {
            g.setColor(Color.red);
            g.fillRect(x + 1, y - i * 9 + 1, width - 1, height - 1);
        }
    }

    public void drawLegend(Graphics g) {
        g.setColor(Color.black);
        g.drawString("Each rectangle represents 1 kbit of data.", 165, 280);
        g.drawRect(105, 283, 355, 20);
        g.drawString("R: receive buffer" + "    " + "B: internal buffer" + "     " + "T: transmit buffer", 120, 298);
    }
}
