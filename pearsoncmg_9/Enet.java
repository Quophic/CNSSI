import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Event;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Label;
import java.awt.Panel;
import java.awt.event.*;
import javax.swing.JPanel;
import java.awt.event.MouseEvent;

import java.awt.Component;
import java.awt.Canvas;

public class Enet extends JPanel implements ActionListener, MouseListener {
    AnimateTimer runner;

    Panel controls;

    Panel choices;

    Panel buttons;

    Panel length_panel;

    Panel size_panel;

    Panel rate_panel;

    Panel length_label_panel;

    Panel size_label_panel;

    Panel rate_label_panel;

    Panel start_panel;

    Panel stop_panel;

    Panel pause_panel;

    Panel resume_panel;

    Panel empty_label1;

    Panel empty_label2;

    Panel empty_label3;

    Panel empty_label4;

    Button Start;

    Button Stop;

    Button Pause;

    Button Resume;

    Choice Length;

    Choice Size;

    Choice Rate;

    NetCanvas network;

    private Image m_kOffScreenImage;

    private Graphics m_kOffScreenGraphics;

    private boolean m_bResized = true;

    public Enet() {
        this.network = new NetCanvas();
        setBackground(Color.white);
        this.Length = new Choice();
        this.Length.addItem("100");
        this.Length.addItem("1000");
        this.Length.addItem("2500");
        this.Length.select("2500");
        this.Size = new Choice();
        this.Size.addItem("500");
        this.Size.addItem("1000");
        this.Size.addItem("5000");
        this.Size.select("500");
        this.Rate = new Choice();
        this.Rate.addItem("10");
        this.Rate.addItem("100");
        this.Rate.select("10");
        this.Pause = new Button("Pause");
        this.Pause.addActionListener(this);
        this.Resume = new Button("Resume");
        this.Resume.addActionListener(this);
        this.Start = new Button("Start");
        this.Start.addActionListener(this);
        this.Stop = new Button("Stop");
        this.Stop.addActionListener(this);
        this.Pause.setEnabled(false);
        this.Resume.setEnabled(false);
        this.Stop.setEnabled(false);
        this.empty_label1 = new Panel();
        this.empty_label1.setLayout(new BorderLayout());
        this.empty_label1.add("North", new Label(" "));
        this.empty_label1.add("South", new Label(" "));
        this.empty_label2 = new Panel();
        this.empty_label2.setLayout(new BorderLayout());
        this.empty_label2.add("North", new Label(" "));
        this.empty_label2.add("South", new Label(" "));
        this.empty_label3 = new Panel();
        this.empty_label3.setLayout(new BorderLayout());
        this.empty_label3.add("North", new Label(" "));
        this.empty_label3.add("South", new Label(" "));
        this.empty_label4 = new Panel();
        this.empty_label4.setLayout(new BorderLayout());
        this.empty_label4.add("North", new Label(" "));
        this.empty_label4.add("South", new Label(" "));
        this.length_label_panel = new Panel();
        this.length_label_panel.setLayout(new BorderLayout());
        this.length_label_panel.add("North", new Label("Length", 1));
        this.length_label_panel.add("South", new Label("(meters)", 1));
        this.length_panel = new Panel();
        this.length_panel.setLayout(new BorderLayout());
        this.length_panel.add("North", this.length_label_panel);
        this.length_panel.add("South", this.Length);
        this.size_label_panel = new Panel();
        this.size_label_panel.setLayout(new BorderLayout());
        this.size_label_panel.add("North", new Label("Size", 1));
        this.size_label_panel.add("South", new Label("(bits)", 1));
        this.size_panel = new Panel();
        this.size_panel.setLayout(new BorderLayout());
        this.size_panel.add("South", this.Size);
        this.size_panel.add("North", this.size_label_panel);
        this.rate_label_panel = new Panel();
        this.rate_label_panel.setLayout(new BorderLayout());
        this.rate_label_panel.add("North", new Label("Rate", 1));
        this.rate_label_panel.add("South", new Label("(Mbps)", 1));
        this.rate_panel = new Panel();
        this.rate_panel.setLayout(new BorderLayout());
        this.rate_panel.add("South", this.Rate);
        this.rate_panel.add("North", this.rate_label_panel);
        this.start_panel = new Panel();
        this.start_panel.setLayout(new BorderLayout());
        this.start_panel.add("South", this.Start);
        this.start_panel.add("North", this.empty_label1);
        this.stop_panel = new Panel();
        this.stop_panel.setLayout(new BorderLayout());
        this.stop_panel.add("South", this.Stop);
        this.stop_panel.add("North", this.empty_label2);
        this.pause_panel = new Panel();
        this.pause_panel.setLayout(new BorderLayout());
        this.pause_panel.add("South", this.Pause);
        this.pause_panel.add("North", this.empty_label3);
        this.resume_panel = new Panel();
        this.resume_panel.setLayout(new BorderLayout());
        this.resume_panel.add("South", this.Resume);
        this.resume_panel.add("North", this.empty_label4);
        this.choices = new Panel();
        this.choices.setLayout(new FlowLayout());
        this.choices.add(this.length_panel);
        this.choices.add(this.size_panel);
        this.choices.add(this.rate_panel);
        this.buttons = new Panel();
        this.buttons.setLayout(new FlowLayout());
        this.buttons.add(this.start_panel);
        this.buttons.add(this.stop_panel);
        this.buttons.add(this.pause_panel);
        this.buttons.add(this.resume_panel);
        this.controls = new Panel();
        this.controls.setLayout(new FlowLayout());
        this.controls.setBackground(Color.white);
        this.controls.add(this.choices);
        this.controls.add(this.buttons);
        setSize(500, 400);
        setLayout(new BorderLayout());
        add("North", this.network);
        add("South", this.controls);
        addMouseListener(this);
    }

    // Applet 类，浏览器调用的函数
    // public void destroy() {
    // if (this.runner != null)
    // this.runner.stop();
    // }

    public void update(Graphics paramGraphics) {
        if (this.m_kOffScreenImage == null || this.m_bResized) {
            this.m_kOffScreenImage = createImage(getSize().width, getSize().height);

            this.m_kOffScreenGraphics = this.m_kOffScreenImage.getGraphics();
            this.m_bResized = false;
        }
        this.m_kOffScreenGraphics.setColor(getBackground());
        this.m_kOffScreenGraphics.fillRect(0, 0, getSize().width, getSize().height);
        this.m_kOffScreenGraphics.setColor(paramGraphics.getColor());
        paint(this.m_kOffScreenGraphics);
        paramGraphics.drawImage(this.m_kOffScreenImage, 0, 0, this);
    }

    public void paint(Graphics paramGraphics) {
        this.network.paint(paramGraphics);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.Start) {
            Constants.CableLength = Integer.parseInt(this.Length.getSelectedItem());
            Constants.TransmissionRate = Integer.parseInt(this.Rate.getSelectedItem());
            Constants.FrameSize = Integer.parseInt(this.Size.getSelectedItem());
            this.Length.setEnabled(false);
            this.Rate.setEnabled(false);
            this.Size.setEnabled(false);
            this.Start.setEnabled(false);
            this.Stop.setEnabled(true);
            this.Pause.setEnabled(true);
            for (byte b1 = 0; b1 < 3; b1++)
                this.network.Stations[b1] = new Station(b1, this.network.Stations);
            this.runner = new AnimateTimer(this, 100);
            this.runner.start();
            // this.runner.run();
        }
        if (e.getSource() == this.Stop) {
            this.Length.setEnabled(true);
            this.Rate.setEnabled(true);
            this.Size.setEnabled(true);
            this.Start.setEnabled(true);
            this.Stop.setEnabled(false);
            this.Pause.setEnabled(false);
            this.Resume.setEnabled(false);
            this.runner.stop();
            for (byte b1 = 0; b1 < 3; b1++)
                this.network.Stations[b1] = null;
            this.network.timer_started = false;
            this.network.timer = 0.0D;
        }
        if (e.getSource() == this.Pause) {
            this.Pause.setEnabled(false);
            this.Resume.setEnabled(true);
            this.runner.Pause();
        }
        if (e.getSource() == this.Resume) {
            this.Pause.setEnabled(true);
            this.Resume.setEnabled(false);
            this.runner.Resume();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    @Override
    public void mouseClicked(MouseEvent e) {
        byte b;
        // 计算点击时鼠标在窗口中的绝对位置
        int x = e.getXOnScreen() - getLocationOnScreen().x;
        int y = e.getYOnScreen() - getLocationOnScreen().y;
        System.out.println(x + " : " + y);
        for (b = 0; b < 3; b++) {
            if (this.network.Stations[b] != null && this.network.Stations[b].onClick(x, y))
                this.network.timer_started = true;
        }
    }

//     public boolean handleEvent(Event paramEvent) {
//         byte b;
//         switch (paramEvent.id) {
//         case 1001:
//             if (paramEvent.target == this.Start) {
//                 Constants.CableLength = Integer.parseInt(this.Length.getSelectedItem());
//                 Constants.TransmissionRate = Integer.parseInt(this.Rate.getSelectedItem());
//                 Constants.FrameSize = Integer.parseInt(this.Size.getSelectedItem());
//                 this.Length.setEnabled(false);
//                 this.Rate.setEnabled(false);
//                 this.Size.setEnabled(false);
//                 this.Start.setEnabled(false);
//                 this.Stop.setEnabled(true);
//                 this.Pause.setEnabled(true);
//                 for (byte b1 = 0; b1 < 3; b1++)
//                     this.network.Stations[b1] = new Station(b1, this.network.Stations);
//                 this.runner = new AnimateTimer(this, 100);
//                 this.runner.start();
//             }
//             if (paramEvent.target == this.Stop) {
//                 this.Length.setEnabled(true);
//                 this.Rate.setEnabled(true);
//                 this.Size.setEnabled(true);
//                 this.Start.setEnabled(true);
//                 this.Stop.setEnabled(false);
//                 this.Pause.setEnabled(false);
//                 this.Resume.setEnabled(false);
//                 this.runner.stop();
//                 for (byte b1 = 0; b1 < 3; b1++)
//                     this.network.Stations[b1] = null;
//                 this.network.timer_started = false;
//                 this.network.timer = 0.0D;
//             }
//             if (paramEvent.target == this.Pause) {
//                 this.Pause.setEnabled(false);
//                 this.Resume.setEnabled(true);
//                 this.runner.Pause();
//             }
//             if (paramEvent.target == this.Resume) {
//                 this.Pause.setEnabled(true);
//                 this.Resume.setEnabled(false);
//                 this.runner.Resume();
//             }
//             return true;
//         case 501:
//             for (b = 0; b < 3; b++) {
//                 if (this.network.Stations[b] != null && this.network.Stations[b].onClick(paramEvent.x, paramEvent.y))
//                     this.network.timer_started = true;
//             }
//             return true;
//         }
//         return false;
//     }
}

class Constants {
    static int CableLength;

    static int FrameSize;

    static int TransmissionRate;
}

class Instance {
    int X;

    int Y;

    int speed;
}

class NetCanvas extends Canvas {
    Station[] Stations;

    double timer;

    boolean timer_started;

    public NetCanvas() {
        this.timer = 0.0D;
        this.timer_started = false;
        setBackground(Color.white);
        this.Stations = new Station[3];
        for (byte b = 0; b < 3; b++)
            this.Stations[b] = null;
    }

    public void drawStations(Graphics paramGraphics) {
        for (byte b = 0; b < 3; b++) {
            Integer integer = b + 1;    // 这样好像没什么问题
            switch (b + 1) {
            case 1:
                paramGraphics.setColor(Color.cyan);
                break;
            case 2:
                paramGraphics.setColor(Color.green);
                break;
            case 3:
                paramGraphics.setColor(Color.magenta);
                break;
            case 4:
                paramGraphics.setColor(Color.orange);
                break;
            }
            paramGraphics.fillRect(305, b * 100 + 20, 40, 40);
            paramGraphics.setColor(Color.black);
            paramGraphics.drawString(integer.toString(), 325, b * 100 + 45);
            paramGraphics.drawLine(345, b * 100 + 40, 350, b * 100 + 40);
            paramGraphics.drawString(integer.toString(), 350 + 10 * b + 3, 30);
        }
        paramGraphics.drawLine(350, 40, 350, 240);
    }

    public void paint(Graphics paramGraphics) {
        paramGraphics.drawString("Time", 10, 140);
        paramGraphics.drawString("(microseconds)", 10, 150);
        paramGraphics.setColor(Color.red);
        paramGraphics.drawString(String.valueOf((int) Math.floor(this.timer)), 10, 160);
        paramGraphics.setColor(Color.black);
        if (this.timer_started)
            this.timer += 0.25D;
        for (byte b = 0; b < 3; b++) {
            if (this.Stations[b] != null) {
                this.Stations[b].Update(paramGraphics);
                this.Stations[b].Draw(paramGraphics);
            }
        }
        drawStations(paramGraphics);
    }
}

class AnimateTimer extends Thread {
    boolean pause = false;

    int interval;

    Component drawArea;

    public AnimateTimer(Component paramComponent, int paramInt) {
        this.drawArea = paramComponent;
        this.interval = paramInt;
    }

    public void run() {
        while (true) {
            if (!this.pause)
                this.drawArea.repaint();
            try {
                Thread.sleep(this.interval);
            } catch (InterruptedException interruptedException) {
            }
        }
    }

    public void ChangePace(int paramInt) {
        this.interval = paramInt;
    }

    public void Pause() {
        this.pause = true;
    }

    public void Resume() {
        this.pause = false;
    }
}

class EnetFrame {
    int length = (int) Math.ceil((Constants.FrameSize * 100) / (Constants.TransmissionRate * 25));

    int ID;

    boolean Active;

    Packet[] packets = new Packet[this.length];

    boolean abort;

    Station[] stations;

    EnetFrame(int paramInt1, int paramInt2, int paramInt3, Station[] paramArrayOfStation) {
        this.ID = paramInt3;
        this.Active = false;
        this.abort = false;
        this.stations = paramArrayOfStation;
        this.packets[0] = new Packet(305, 100 * this.ID + 40, this.ID, this.stations);
        (this.packets[0]).first = true;
        if (this.length == 1)
            (this.packets[0]).last = true;
        for (byte b = 1; b < this.length; b++)
            this.packets[b] = null;
    }

    void Draw(Graphics paramGraphics) {
        for (byte b = 0; b < this.length; b++) {
            if (this.packets[b] != null) {
                switch (this.ID) {
                case 0:
                    if (b % 2 == 0) {
                        paramGraphics.setColor(new Color(0, 255, 255));
                        break;
                    }
                    paramGraphics.setColor(new Color(0, 180, 180));
                    break;
                case 1:
                    if (b % 2 == 0) {
                        paramGraphics.setColor(new Color(0, 255, 0));
                        break;
                    }
                    paramGraphics.setColor(new Color(0, 180, 0));
                    break;
                case 2:
                    if (b % 2 == 0) {
                        paramGraphics.setColor(new Color(255, 0, 255));
                        break;
                    }
                    paramGraphics.setColor(new Color(180, 0, 180));
                    break;
                }
                this.packets[b].Draw(paramGraphics);
                paramGraphics.setColor(Color.black);
            }
        }
    }

    boolean Update() {
        boolean bool = false;
        for (byte b = 0; b < this.length; b++) {
            if (this.packets[b] != null) {
                if (this.packets[b].Update()) {
                    bool = true;
                } else {
                    this.packets[b] = null;
                }
            } else if (b != 0 && this.packets[b - 1] != null) {
                if (!this.abort) {
                    this.packets[b] = new Packet(305, 100 * this.ID + 40, this.ID, this.stations);
                    if (b == this.length - 1)
                        (this.packets[b]).last = true;
                } else {
                    this.length = b + 1;
                    (this.packets[b - 1]).last = true;
                }
                return true;
            }
        }
        return bool;
    }
}

class Station {
    String state_string;

    String collisions_string;

    String K_string;

    int ID;

    EnetFrame e_frame;

    EnetFrame tail;

    boolean active;

    boolean transmitting;

    Station[] stations;

    boolean aborting;

    int passing_frames;

    int collisions;

    boolean waiting;

    int factor;

    int countdown;

    Station(int paramInt, Station[] paramArrayOfStation) {
        this.stations = paramArrayOfStation;
        this.state_string = "";
        this.collisions_string = "";
        this.K_string = "";
        this.ID = paramInt;
        this.active = false;
        this.e_frame = null;
        this.tail = null;
        this.transmitting = false;
        this.aborting = false;
        this.waiting = false;
        this.passing_frames = 0;
        this.collisions = 0;
        this.factor = (int) Math.ceil(51200.0D / (25 * Constants.TransmissionRate));
    }

    public void Draw(Graphics paramGraphics) {
        if (this.e_frame != null)
            this.e_frame.Draw(paramGraphics);
        if (this.tail != null)
            this.tail.Draw(paramGraphics);
        if (this.state_string.equals("Transmission Complete")) {
            paramGraphics.setColor(Color.red);
            paramGraphics.drawString(this.state_string, 150, this.ID * 100 + 50);
            paramGraphics.setColor(Color.black);
        } else {
            paramGraphics.drawString(this.state_string, 150, this.ID * 100 + 50);
        }
        paramGraphics.drawString(this.collisions_string, 150, this.ID * 100 + 60);
        paramGraphics.drawString(this.K_string, 150, this.ID * 100 + 70);
    }

    public int K() {
        return (int) Math.round(Math.random() * (Math.pow(2.0D, Math.min(this.collisions, 10)) - 1.0D));
    }

    public void Update(Graphics paramGraphics) {
        if (this.tail != null && !this.tail.Update())
            this.tail = null;
        if (this.active && this.passing_frames <= 0 && !this.waiting) {
            if (!this.transmitting && this.e_frame == null) {
                this.e_frame = new EnetFrame(305, 100 * this.ID + 40, this.ID, this.stations);
                this.transmitting = true;
                this.state_string = "Transmitting";
                this.K_string = "";
                return;
            }
            if (!this.e_frame.Update()) {
                this.e_frame = null;
                this.active = false;
                this.transmitting = false;
                this.collisions = 0;
                this.state_string = "Transmission Complete";
                this.collisions_string = "";
                this.K_string = "";
            }
        }
        if (this.active && this.passing_frames > 0 && !this.transmitting && !this.waiting) {
            this.state_string = "Busy Channel";
            return;
        }
        if (this.active && this.passing_frames > 0 && this.transmitting) {
            this.transmitting = false;
            this.aborting = true;
            this.e_frame.abort = true;
            this.collisions++;
            this.tail = this.e_frame;
            this.tail.Update();
            this.e_frame = null;
            this.waiting = true;
            int i = K();
            this.countdown = this.factor * i;
            if (i == 0) {
                this.state_string = "Busy Channel";
            } else {
                this.state_string = "Backoff";
            }
            this.collisions_string = "Collisions: " + this.collisions;
            this.K_string = "K = " + i;
            return;
        }
        if (this.aborting)
            this.aborting = false;
        if (this.waiting) {
            if (this.countdown > 0) {
                this.countdown--;
                return;
            }
            this.waiting = false;
        }
    }

    // 这确定所点击的station居然是靠判定鼠标点击坐标在不在station显示坐标范围内的，好家伙
    public boolean onClick(int paramInt1, int paramInt2) {
        if (paramInt1 >= 305 && paramInt1 <= 345 && paramInt2 >= 100 * this.ID + 20
                && paramInt2 <= 100 * this.ID + 60) {
            this.active = true;
            return true;
        }
        return false;
    }
}

class Packet {
    static int Speed;

    static int Size;

    int X;

    int Y;

    int S;

    int F;

    Instance[] Instances;

    boolean first;

    boolean last;

    Station[] stations;

    int which0;

    int which1;

    Packet(int paramInt1, int paramInt2, int paramInt3, Station[] paramArrayOfStation) {
        Speed = (int) Math.ceil(10000.0D / Constants.CableLength);
        Size = (int) Math.ceil(10000.0D / Constants.CableLength);
        this.X = paramInt1;
        this.Y = paramInt2;
        this.S = paramInt3;
        this.first = false;
        this.last = false;
        this.stations = paramArrayOfStation;
        this.which0 = -1;
        this.which1 = -1;
        this.Instances = new Instance[2];
        if (this.S != 0) {
            this.Instances[0] = new Instance();
            (this.Instances[0]).X = this.X + 50 + 10 * this.S;
            (this.Instances[0]).Y = this.Y - Speed;
            (this.Instances[0]).speed = -Speed;
        }
        if (this.S != 2) {
            this.Instances[1] = new Instance();
            (this.Instances[1]).X = this.X + 50 + 10 * this.S;
            (this.Instances[1]).Y = this.Y;
            (this.Instances[1]).speed = Speed;
        }
    }

    boolean Is_Fork(int paramInt) {
        if (paramInt % 100 == 40 && paramInt != this.S * 100 + 40)
            return true;
        return false;
    }

    boolean Update() {
        int i = 0, j = 0;
        if (this.Instances[0] != null && (this.Instances[0]).Y <= 40) {
            i = (this.Instances[0]).Y;
            this.Instances[0] = null;
        }
        if (this.Instances[1] != null && (this.Instances[1]).Y >= 240) {
            j = (this.Instances[1]).Y;
            this.Instances[1] = null;
        }
        if (this.Instances[0] != null)
            if ((this.Instances[0]).Y + (this.Instances[0]).speed > 40) {
                (this.Instances[0]).Y += (this.Instances[0]).speed;
            } else {
                (this.Instances[0]).Y = 40;
            }
        if (this.Instances[1] != null)
            if ((this.Instances[1]).Y + (this.Instances[1]).speed < 240) {
                (this.Instances[1]).Y += (this.Instances[1]).speed;
            } else {
                (this.Instances[1]).Y = 240;
            }
        if (this.first) {
            if (this.Instances[0] != null && Is_Fork((this.Instances[0]).Y))
                (this.stations[((this.Instances[0]).Y - 40) / 100]).passing_frames++;
            if (this.Instances[1] != null && Is_Fork((this.Instances[1]).Y + Speed)) {
                int k = ((this.Instances[1]).Y - 40 + Speed) / 100;
                if (k < 0)
                    k = 0;
                if (k > 2)
                    k = 2;
                (this.stations[k]).passing_frames++;
            }
        }
        if (this.last) {
            if (this.Instances[0] != null && Is_Fork((this.Instances[0]).Y + Speed)) {
                int k = ((this.Instances[0]).Y - 40 + Speed) / 100;
                if (k < 0)
                    k = 0;
                if (k > 2)
                    k = 2;
                if ((this.stations[k]).passing_frames > 0)
                    (this.stations[k]).passing_frames--;
            }
            if (this.Instances[0] == null && Is_Fork(i))
                if ((this.stations[(i - 40) / 100]).passing_frames > 0)
                    (this.stations[(i - 40) / 100]).passing_frames--;
            if (this.Instances[1] == null && Is_Fork(j))
                if ((j - 40) / 100 != 2 && (this.stations[(j - 40) / 100]).passing_frames > 0)
                    (this.stations[(j - 40) / 100]).passing_frames--;
            if (this.Instances[1] != null && Is_Fork((this.Instances[1]).Y)) {
                int k = ((this.Instances[1]).Y - 40 + Speed) / 100;
                if (k < 0)
                    k = 0;
                if (k > 2)
                    k = 2;
                if ((this.stations[k]).passing_frames > 0)
                    (this.stations[k]).passing_frames--;
            }
        }
        if (this.Instances[0] != null || this.Instances[1] != null)
            return true;
        return false;
    }

    void Draw(Graphics paramGraphics) {
        if (this.Instances[0] != null && (this.Instances[0]).Y >= 40)
            paramGraphics.fillRect((this.Instances[0]).X, (this.Instances[0]).Y, 10, Size);
        if (this.Instances[1] != null) {
            if ((this.Instances[1]).Y + Size <= 240) {
                paramGraphics.fillRect((this.Instances[1]).X, (this.Instances[1]).Y, 10, Size);
                return;
            }
            paramGraphics.fillRect((this.Instances[1]).X, (this.Instances[1]).Y, 10, 240 - (this.Instances[1]).X);
        }
    }
}