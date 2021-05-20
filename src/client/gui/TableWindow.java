/*
 * Created by JFormDesigner on Sun May 16 17:09:22 MSK 2021
 */

package client.gui;

import javax.swing.*;
import net.miginfocom.swing.*;

/**
 * @author unknown
 */
public class TableWindow extends JPanel {
    public TableWindow() {
        initComponents();
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - unknown
        addButton = new JButton();
        scrollPane1 = new JScrollPane();
        table = new JTable();
        logOutButton = new JButton();
        addIfMinButton = new JButton();
        changeLocaleButton = new JButton();
        clearButton = new JButton();
        filterLabel = new JLabel();
        countButton = new JButton();
        columnBox = new JComboBox();
        executeScriptButton = new JButton();
        equalBox = new JComboBox();
        helpButton = new JButton();
        filterField = new JTextField();
        infoButton = new JButton();
        removeByIdButton = new JButton();
        removeGreaterButton = new JButton();
        removeLowerButton = new JButton();
        updateButton = new JButton();
        visualizeButton = new JButton();

        //======== this ========
        setBorder(new javax.swing.border.CompoundBorder(new javax.swing.border.TitledBorder(new javax.swing.border.EmptyBorder(
        0,0,0,0), "JF\u006frmD\u0065sig\u006eer \u0045val\u0075ati\u006fn",javax.swing.border.TitledBorder.CENTER,javax.swing.border.TitledBorder
        .BOTTOM,new java.awt.Font("Dia\u006cog",java.awt.Font.BOLD,12),java.awt.Color.
        red), getBorder())); addPropertyChangeListener(new java.beans.PropertyChangeListener(){@Override public void propertyChange(java.
        beans.PropertyChangeEvent e){if("\u0062ord\u0065r".equals(e.getPropertyName()))throw new RuntimeException();}});
        setLayout(new MigLayout(
            "fill,hidemode 3,align center top",
            // columns
            "[151,fill]" +
            "[700,grow,fill]" +
            "[fill]",
            // rows
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]"));

        //---- addButton ----
        addButton.setText("Add");
        add(addButton, "cell 0 0");

        //======== scrollPane1 ========
        {

            //---- table ----
            table.setAutoCreateRowSorter(true);
            scrollPane1.setViewportView(table);
        }
        add(scrollPane1, "cell 1 0 1 12");

        //---- logOutButton ----
        logOutButton.setText("Log out");
        add(logOutButton, "cell 2 0");

        //---- addIfMinButton ----
        addIfMinButton.setText("Add if min");
        add(addIfMinButton, "cell 0 1");

        //---- changeLocaleButton ----
        changeLocaleButton.setText("Change locale");
        add(changeLocaleButton, "cell 2 1");

        //---- clearButton ----
        clearButton.setText("Clear");
        add(clearButton, "cell 0 2");

        //---- filterLabel ----
        filterLabel.setText("Filter");
        add(filterLabel, "cell 2 2,alignx center,growx 0");

        //---- countButton ----
        countButton.setText("Count by group admin");
        add(countButton, "cell 0 3");
        add(columnBox, "cell 2 3");

        //---- executeScriptButton ----
        executeScriptButton.setText("Execute script");
        add(executeScriptButton, "cell 0 4");
        add(equalBox, "cell 2 4");

        //---- helpButton ----
        helpButton.setText("Help");
        add(helpButton, "cell 0 5");
        add(filterField, "cell 2 5");

        //---- infoButton ----
        infoButton.setText("Info");
        add(infoButton, "cell 0 6");

        //---- removeByIdButton ----
        removeByIdButton.setText("Remove by ID");
        add(removeByIdButton, "cell 0 7");

        //---- removeGreaterButton ----
        removeGreaterButton.setText("Remove greater");
        add(removeGreaterButton, "cell 0 8");

        //---- removeLowerButton ----
        removeLowerButton.setText("Remove lower");
        add(removeLowerButton, "cell 0 9");

        //---- updateButton ----
        updateButton.setText("Update");
        add(updateButton, "cell 0 10");

        //---- visualizeButton ----
        visualizeButton.setText("Visualization");
        add(visualizeButton, "cell 0 11");
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - unknown
    private JButton addButton;
    private JScrollPane scrollPane1;
    private JTable table;
    private JButton logOutButton;
    private JButton addIfMinButton;
    private JButton changeLocaleButton;
    private JButton clearButton;
    private JLabel filterLabel;
    private JButton countButton;
    private JComboBox columnBox;
    private JButton executeScriptButton;
    private JComboBox equalBox;
    private JButton helpButton;
    private JTextField filterField;
    private JButton infoButton;
    private JButton removeByIdButton;
    private JButton removeGreaterButton;
    private JButton removeLowerButton;
    private JButton updateButton;
    private JButton visualizeButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
