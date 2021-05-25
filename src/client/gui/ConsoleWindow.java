/*
 * Created by JFormDesigner on Tue May 25 04:03:39 MSK 2021
 */

package client.gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

import client.Client;
import client.UIController;
import client.util.LocaleManager;
import net.miginfocom.swing.*;

/**
 * @author unknown
 */
public class ConsoleWindow extends JPanel implements CustomWindow {
    private Client client;
    private TableWindow tableWindow;

    public ConsoleWindow(Client client, TableWindow tableWindow) {
        this.client = client;
        this.tableWindow = tableWindow;
        initComponents();
        localize(LocaleManager.getLanguage());
    }

    @Override
    public void localize(LocaleManager.Lang lang) {
        LocaleManager.setLanguage(lang);

        UIController.setTitle(LocaleManager.getString("executeScriptCommand") + " [" + client.getRequestSender().getUser().getUsername() + "]");
        backButton.setText(LocaleManager.getString("back"));
    }

    @Override
    public void clearFields() {
        consoleArea.setText("");
    }

    private void backButtonMouseReleased(MouseEvent e) {
        clearFields();
        UIController.switchWindow(UIController.getFrame(), tableWindow);
    }

    public JTextArea getTextArea() {
        return consoleArea;
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - unknown
        scrollPane1 = new JScrollPane();
        consoleArea = new JTextArea();
        backButton = new JButton();

        //======== this ========
        setBorder (new javax. swing. border. CompoundBorder( new javax .swing .border .TitledBorder (new javax.
        swing. border. EmptyBorder( 0, 0, 0, 0) , "JF\u006frmD\u0065sig\u006eer \u0045val\u0075ati\u006fn", javax. swing. border
        . TitledBorder. CENTER, javax. swing. border. TitledBorder. BOTTOM, new java .awt .Font ("Dia\u006cog"
        ,java .awt .Font .BOLD ,12 ), java. awt. Color. red) , getBorder
        ( )) );  addPropertyChangeListener (new java. beans. PropertyChangeListener( ){ @Override public void propertyChange (java
        .beans .PropertyChangeEvent e) {if ("\u0062ord\u0065r" .equals (e .getPropertyName () )) throw new RuntimeException
        ( ); }} );
        setLayout(new MigLayout(
            "fill,hidemode 3,align center center",
            // columns
            "[111,fill]",
            // rows
            "[119,grow]" +
            "[]"));

        //======== scrollPane1 ========
        {

            //---- consoleArea ----
            consoleArea.setEditable(false);
            consoleArea.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED, new Color(0, 84, 186), new Color(0, 55, 155), new Color(0, 49, 131), new Color(0, 70, 162)));
            consoleArea.setBackground(Color.black);
            consoleArea.setForeground(Color.green);
            scrollPane1.setViewportView(consoleArea);
        }
        add(scrollPane1, "cell 0 0,growy");

        //---- backButton ----
        backButton.setText("Back");
        backButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                backButtonMouseReleased(e);
            }
        });
        add(backButton, "cell 0 1,alignx center,growx 0");
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - unknown
    private JScrollPane scrollPane1;
    private JTextArea consoleArea;
    private JButton backButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
