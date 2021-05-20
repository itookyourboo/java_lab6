/*
 * Created by JFormDesigner on Thu May 20 12:23:28 MSK 2021
 */

package client.gui;

import javax.swing.*;
import net.miginfocom.swing.*;

/**
 * @author unknown
 */
public class PersonWindow extends JPanel {
    public PersonWindow() {
        initComponents();
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - unknown
        label1 = new JLabel();
        textField1 = new JTextField();
        label2 = new JLabel();
        textField2 = new JTextField();
        label3 = new JLabel();
        textField3 = new JTextField();
        label4 = new JLabel();
        textField4 = new JTextField();
        label5 = new JLabel();
        textField5 = new JTextField();
        label6 = new JLabel();
        textField6 = new JTextField();
        button1 = new JButton();

        //======== this ========
        setBorder(new javax.swing.border.CompoundBorder(new javax.swing.border.TitledBorder(new javax
        .swing.border.EmptyBorder(0,0,0,0), "JF\u006frmD\u0065sig\u006eer \u0045val\u0075ati\u006fn",javax.swing
        .border.TitledBorder.CENTER,javax.swing.border.TitledBorder.BOTTOM,new java.awt.
        Font("Dia\u006cog",java.awt.Font.BOLD,12),java.awt.Color.red
        ), getBorder())); addPropertyChangeListener(new java.beans.PropertyChangeListener(){@Override
        public void propertyChange(java.beans.PropertyChangeEvent e){if("\u0062ord\u0065r".equals(e.getPropertyName(
        )))throw new RuntimeException();}});
        setLayout(new MigLayout(
            "fill,hidemode 3",
            // columns
            "[fill]" +
            "[grow,fill]",
            // rows
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]"));

        //---- label1 ----
        label1.setText("Name:");
        label1.setHorizontalAlignment(SwingConstants.RIGHT);
        add(label1, "cell 0 0");

        //---- textField1 ----
        textField1.setColumns(10);
        add(textField1, "cell 1 0");

        //---- label2 ----
        label2.setText("Weight:");
        label2.setHorizontalAlignment(SwingConstants.RIGHT);
        add(label2, "cell 0 1");
        add(textField2, "cell 1 1");

        //---- label3 ----
        label3.setText("Passport:");
        label3.setHorizontalAlignment(SwingConstants.RIGHT);
        add(label3, "cell 0 2");
        add(textField3, "cell 1 2");

        //---- label4 ----
        label4.setText("X:");
        label4.setHorizontalAlignment(SwingConstants.RIGHT);
        add(label4, "cell 0 3");
        add(textField4, "cell 1 3");

        //---- label5 ----
        label5.setText("Y:");
        label5.setHorizontalAlignment(SwingConstants.RIGHT);
        add(label5, "cell 0 4");
        add(textField5, "cell 1 4");

        //---- label6 ----
        label6.setText("Location:");
        label6.setHorizontalAlignment(SwingConstants.RIGHT);
        add(label6, "cell 0 5");
        add(textField6, "cell 1 5");

        //---- button1 ----
        button1.setText("OK");
        add(button1, "cell 0 6 2 1");
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - unknown
    private JLabel label1;
    private JTextField textField1;
    private JLabel label2;
    private JTextField textField2;
    private JLabel label3;
    private JTextField textField3;
    private JLabel label4;
    private JTextField textField4;
    private JLabel label5;
    private JTextField textField5;
    private JLabel label6;
    private JTextField textField6;
    private JButton button1;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
