package client.gui;

import client.util.LocaleManager;

import javax.swing.*;

public interface CustomWindow {
    void localize(LocaleManager.Lang lang);
    void clearFields();
    default void showDialog(JPanel parent, String message) {
        JOptionPane.showMessageDialog(parent, message);
    }
}
