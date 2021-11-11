import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;


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
