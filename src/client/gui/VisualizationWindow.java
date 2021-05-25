package client.gui;

import common.model.StudyGroup;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

public class VisualizationWindow extends JPanel {
    private List<StudyGroupCircle> entities;
    private final int tps = 60;
    private Thread tickThread;
    private List<StudyGroup> list;
    private Dimension size;
    //final BufferedImage image;
    private void tick(){
        try {
            while (true) {
                Thread.sleep(1000/tps);
                for (StudyGroupCircle c: entities){
                    c.onTick();
                }
                repaint();
            }
        }
        catch(InterruptedException ex) {Thread.currentThread().interrupt();}
    }
    public void restart(){

        entities.clear();
        long max_x = 0L;
        for(StudyGroup c: list) {
            max_x = Math.max(max_x, c.getCoordinates().getX());
        }

        for(StudyGroup c: list){
            double radius = 20;
            byte[] color_b = c.getOwner().getBytes();
            byte[] color_b_norm = new byte[3];
            for (int i = 0; i < 3; i ++){
                color_b_norm[i] = (byte) Math.abs(color_b[i]);
            }
            Color color = new Color(color_b_norm[0], color_b_norm[1], color_b_norm[2]);
            double velocity_x = -3 + Math.random() * (6);
            double velocity_y = -3 + Math.random() * (6);
            double norm_x = (double)(c.getCoordinates().getX())/(double)(max_x);

            StudyGroupCircle wc = new StudyGroupCircle((double)(size.width)/2, (double)size.height/2, radius,
                    70 + norm_x * (size.width - 140), c.getCoordinates().getY(), velocity_x, velocity_y, "", color);

            entities.add(wc);
        }
        //tickThread.start();
    }
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
//        if (image != null){
//            g2.drawImage(image, 0, 0, size.width, size.height, null);
//        }
        for (StudyGroupCircle c: entities){
            g2.setColor(c.color);
            g2.fill(c);
            g2.draw(c);
            g2.setColor(Color.WHITE);
            g2.drawString(c.key, (int)(c.getX() + c.getWidth()/3), (int)(c.getY() + c.getHeight()/2));
        }

    }

    public VisualizationWindow(List<StudyGroup> list, Dimension size){
        super();
        this.entities = new ArrayList<>();
        this.tickThread = new Thread(this::tick);
        this.size = size;
        setBackground(Color.WHITE);
        tickThread.start();

        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) { }

            @Override
            public void mousePressed(MouseEvent e) {
                int m_x = e.getX();
                int m_y = e.getY();

                for (StudyGroupCircle c: entities){
                    if(c.contains(m_x, m_y)){
                        //new CreateUpdateObjectWindow(UPDATE, c.key);
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) { }

            @Override
            public void mouseEntered(MouseEvent e) { }

            @Override
            public void mouseExited(MouseEvent e) { }
        });
        setPreferredSize(size);
        setVisible(true);
    }
}