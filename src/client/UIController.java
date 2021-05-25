package client;

import client.gui.LoginWindow;

import javax.swing.*;
import java.awt.*;

public class UIController {
    private static JFrame frame = null;
    private static volatile JPanel panel = null;

    public static void switchWindow(JFrame frame, JPanel newWindow) {
        panel = newWindow;
        SwingUtilities.invokeLater(() -> {
            frame.setContentPane(newWindow);
            frame.validate();
        });
    }

    public static JFrame initFrame() {
        if (frame == null) {
            frame = new JFrame();
            frame.setSize(800, 700);
            frame.setMinimumSize(new Dimension(800, 700));
            frame.setLocationRelativeTo(null);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        }
        return frame;
    }

    public static void setTitle(String title) {
        frame.setTitle(title);
    }

    public static Dimension getScreenSize() {
        return Toolkit.getDefaultToolkit().getScreenSize().getSize();
    }

    public static JFrame getFrame() {
        if (frame == null) throw new RuntimeException("Frame is not initialized. Use UIControl.initFrame()");
        return frame;
    }

    public static JPanel startWindow(Client client) {
        if (panel == null) {
            panel = new LoginWindow(client);
        }
        return panel;
    }

    public static synchronized JPanel getPanel() {
        return panel;
    }
}
