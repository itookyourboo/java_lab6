/*
 * Created by JFormDesigner on Thu May 20 12:02:16 MSK 2021
 */

package client.gui;

import javax.swing.*;
import net.miginfocom.swing.*;

/**
 * @author unknown
 */
public class StudyGroupWindow extends JPanel {
    public StudyGroupWindow() {
        initComponents();
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - unknown
        label14 = new JLabel();
        textField20 = new JTextField();
        label1 = new JLabel();
        textField7 = new JTextField();
        label8 = new JLabel();
        textField14 = new JTextField();
        label2 = new JLabel();
        textField8 = new JTextField();
        label9 = new JLabel();
        textField15 = new JTextField();
        label3 = new JLabel();
        textField9 = new JTextField();
        label10 = new JLabel();
        textField16 = new JTextField();
        label4 = new JLabel();
        textField10 = new JTextField();
        label11 = new JLabel();
        textField17 = new JTextField();
        label5 = new JLabel();
        textField11 = new JTextField();
        label12 = new JLabel();
        textField18 = new JTextField();
        label6 = new JLabel();
        textField12 = new JTextField();
        label13 = new JLabel();
        textField19 = new JTextField();
        label7 = new JLabel();
        comboBox1 = new JComboBox();
        button1 = new JButton();

        //======== this ========
        setBorder(new javax.swing.border.CompoundBorder(new javax.swing.border.TitledBorder(new
        javax.swing.border.EmptyBorder(0,0,0,0), "JF\u006frmDes\u0069gner \u0045valua\u0074ion",javax
        .swing.border.TitledBorder.CENTER,javax.swing.border.TitledBorder.BOTTOM,new java
        .awt.Font("D\u0069alog",java.awt.Font.BOLD,12),java.awt
        .Color.red), getBorder())); addPropertyChangeListener(new java.beans.
        PropertyChangeListener(){@Override public void propertyChange(java.beans.PropertyChangeEvent e){if("\u0062order".
        equals(e.getPropertyName()))throw new RuntimeException();}});
        setLayout(new MigLayout(
            "fill,hidemode 3",
            // columns
            "[fill]" +
            "[grow,fill]" +
            "[fill]" +
            "[grow,fill]" +
            "[fill]" +
            "[grow,fill]",
            // rows
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]"));

        //---- label14 ----
        label14.setText("ID:");
        label14.setHorizontalAlignment(SwingConstants.RIGHT);
        add(label14, "cell 2 0");
        add(textField20, "cell 3 0");

        //---- label1 ----
        label1.setText("Name:");
        label1.setHorizontalAlignment(SwingConstants.RIGHT);
        add(label1, "cell 0 1");

        //---- textField7 ----
        textField7.setColumns(10);
        add(textField7, "cell 1 1");

        //---- label8 ----
        label8.setText("Admin name:");
        label8.setHorizontalAlignment(SwingConstants.RIGHT);
        add(label8, "cell 4 1");

        //---- textField14 ----
        textField14.setColumns(10);
        add(textField14, "cell 5 1");

        //---- label2 ----
        label2.setText("X:");
        label2.setHorizontalAlignment(SwingConstants.RIGHT);
        add(label2, "cell 0 2");
        add(textField8, "cell 1 2");

        //---- label9 ----
        label9.setText("Admin weight:");
        label9.setHorizontalAlignment(SwingConstants.RIGHT);
        add(label9, "cell 4 2");
        add(textField15, "cell 5 2");

        //---- label3 ----
        label3.setText("Y:");
        label3.setHorizontalAlignment(SwingConstants.RIGHT);
        add(label3, "cell 0 3");
        add(textField9, "cell 1 3");

        //---- label10 ----
        label10.setText("Admin passport:");
        label10.setHorizontalAlignment(SwingConstants.RIGHT);
        add(label10, "cell 4 3");
        add(textField16, "cell 5 3");

        //---- label4 ----
        label4.setText("Students count:");
        label4.setHorizontalAlignment(SwingConstants.RIGHT);
        add(label4, "cell 0 4");
        add(textField10, "cell 1 4");

        //---- label11 ----
        label11.setText("Admin X:");
        label11.setHorizontalAlignment(SwingConstants.RIGHT);
        add(label11, "cell 4 4");
        add(textField17, "cell 5 4");

        //---- label5 ----
        label5.setText("Expelled students:");
        label5.setHorizontalAlignment(SwingConstants.RIGHT);
        add(label5, "cell 0 5");
        add(textField11, "cell 1 5");

        //---- label12 ----
        label12.setText("Admin Y:");
        label12.setHorizontalAlignment(SwingConstants.RIGHT);
        add(label12, "cell 4 5");
        add(textField18, "cell 5 5");

        //---- label6 ----
        label6.setText("Should be expelled:");
        label6.setHorizontalAlignment(SwingConstants.RIGHT);
        add(label6, "cell 0 6");
        add(textField12, "cell 1 6");

        //---- label13 ----
        label13.setText("Admin location:");
        label13.setHorizontalAlignment(SwingConstants.RIGHT);
        add(label13, "cell 4 6");
        add(textField19, "cell 5 6");

        //---- label7 ----
        label7.setText("Form of education:");
        label7.setHorizontalAlignment(SwingConstants.RIGHT);
        add(label7, "cell 0 7");
        add(comboBox1, "cell 1 7");

        //---- button1 ----
        button1.setText("OK");
        add(button1, "cell 2 8 2 1");
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - unknown
    private JLabel label14;
    private JTextField textField20;
    private JLabel label1;
    private JTextField textField7;
    private JLabel label8;
    private JTextField textField14;
    private JLabel label2;
    private JTextField textField8;
    private JLabel label9;
    private JTextField textField15;
    private JLabel label3;
    private JTextField textField9;
    private JLabel label10;
    private JTextField textField16;
    private JLabel label4;
    private JTextField textField10;
    private JLabel label11;
    private JTextField textField17;
    private JLabel label5;
    private JTextField textField11;
    private JLabel label12;
    private JTextField textField18;
    private JLabel label6;
    private JTextField textField12;
    private JLabel label13;
    private JTextField textField19;
    private JLabel label7;
    private JComboBox comboBox1;
    private JButton button1;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
