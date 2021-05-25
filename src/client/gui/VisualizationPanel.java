package client.gui;

import client.Client;
import client.UIController;
import client.commands.ShowCommand;
import client.impl.Updatable;
import client.util.LocaleManager;
import common.model.Coordinates;
import common.model.StudyGroup;
import common.net.CommandResult;
import common.net.ResultStatus;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class VisualizationPanel extends JPanel implements CustomWindow, Updatable {
    private Client client;
    private volatile List<StudyGroupCircle> entities = Collections.synchronizedList(new ArrayList<>());
    private final int tps = 60;
    private Thread tickThread;
    private volatile List<StudyGroup> list = Collections.synchronizedList(new ArrayList<>());
    private Dimension size = new Dimension(800, 600);

    public VisualizationPanel(Client client){
        this.client = client;
        this.tickThread = new Thread(this::tick);
        setBackground(Color.ORANGE);
        tickThread.start();

        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) { }

            @Override
            public void mousePressed(MouseEvent e) { }

            @Override
            public void mouseReleased(MouseEvent e) {
                int m_x = e.getX();
                int m_y = e.getY();

                for (StudyGroupCircle c: entities){
                    if(c.contains(m_x, m_y)){
                        showDialog(VisualizationPanel.this, c.studyGroup.toString().replaceAll(",", "\n"));
                    }
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) { }

            @Override
            public void mouseExited(MouseEvent e) { }
        });
        setVisible(true);
        loadData();
    }

    public synchronized boolean checkForUpdate() {
        CommandResult result = new ShowCommand(client.getRequestSender())
                .executeWithObjectArgument();

        List<StudyGroup> updated;
        if (result.status == ResultStatus.OK && !result.message.trim().isEmpty()) {
            updated = Arrays.stream(result.message.split("\n"))
                    .map(StudyGroup::fromJson)
                    .collect(Collectors.toList());
        } else if (result.status != ResultStatus.ERROR) {
            updated = new ArrayList<>();
        } else {
            updated = list;
        }

        if (!updated.equals(list)) {
            System.out.println("LOAD DATA");
            return true;
        }

        return false;
    }

    public synchronized void loadData() {
        CommandResult result = new ShowCommand(client.getRequestSender())
                .executeWithObjectArgument();

        if (result.status == ResultStatus.OK && !result.message.trim().isEmpty()) {
            System.out.println(result.message);
            list.addAll(Arrays.stream(result.message.split("\n"))
                    .map(StudyGroup::fromJson)
                    .collect(Collectors.toList()));
        } else {
            list.clear();
        }

        restart();
    }

    private void tick(){
        try {
            while (true) {
                Thread.sleep(1000 / tps);
                for (StudyGroupCircle c: entities){
                    c.onTick();
                }
                repaint();
            }
        }
        catch(InterruptedException ex) {Thread.currentThread().interrupt();}
    }

    public synchronized void restart(){
        entities.clear();
        long max_x = 0, max_y = 0;
        for(StudyGroup c: list) {
            Coordinates coordinates = c.getCoordinates();
            max_x = Math.max(max_x, Math.abs(coordinates.getX()));
            max_y = Math.max(max_y, Math.abs(coordinates.getY()));
        }

        System.out.println(max_x + " " + max_y);

        for(StudyGroup c: list){
            byte[] color_b = c.getOwner().getBytes();
            byte[] color_b_norm = new byte[3];
            for (int i = 0; i < 3; i++){
                color_b_norm[i] = (byte) (0.8 * Math.abs(color_b[i]));
            }
            Color color = new Color(color_b_norm[0], color_b_norm[1], color_b_norm[2]);
            double norm_x = (double)(c.getCoordinates().getX())/(double)(max_x);
            double norm_y = (double)(c.getCoordinates().getY())/(double)(max_y);

            double centerX = (double) size.width / 2 * (1 + norm_x);
            if (centerX <= StudyGroupCircle.MAX_RADIUS) centerX = StudyGroupCircle.MAX_RADIUS;
            if (centerX >= size.width - 2 * StudyGroupCircle.MAX_RADIUS) centerX = size.width - 2 * StudyGroupCircle.MAX_RADIUS;

            double centerY = (double) size.height / 2 * (1 + norm_y);
            if (centerY <= StudyGroupCircle.MAX_RADIUS) centerY = StudyGroupCircle.MAX_RADIUS;
            if (centerY >= size.height - 2 * StudyGroupCircle.MAX_RADIUS) centerY = size.height - 2 * StudyGroupCircle.MAX_RADIUS;

            System.out.println(c.getName() + " " + centerX + " " + centerY + " " + color);

            StudyGroupCircle wc = new StudyGroupCircle(centerX, centerY, c, color);

            entities.add(wc);
        }
    }

    @Override
    public synchronized void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        for (StudyGroupCircle c: entities){
            g2.setColor(c.color);
            g2.fill(c);
            g2.draw(c);
            g2.setColor(Color.WHITE);
            FontMetrics metrics = g.getFontMetrics();
            int textX = (int) (c.getX() + (c.getWidth() - metrics.stringWidth(c.studyGroup.getName())) / 2);
            int textY = (int) (c.getY() + (c.getHeight() - metrics.getHeight()) / 2 + metrics.getAscent());
            g2.drawString(c.studyGroup.getName(), textX, textY);
        }
    }

    @Override
    public synchronized void localize(LocaleManager.Lang lang) {
        LocaleManager.setLanguage(lang);
        UIController.setTitle(LocaleManager.getString("visualizationWindow"));
    }

    @Override
    public void clearFields() {

    }
}