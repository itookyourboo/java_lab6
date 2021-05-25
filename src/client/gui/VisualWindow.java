/*
 * Created by JFormDesigner on Tue May 25 06:54:11 MSK 2021
 */

package client.gui;

import javax.swing.*;

import client.Client;
import client.UIController;
import client.impl.Updatable;
import client.util.LocaleManager;
import net.miginfocom.swing.*;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @author unknown
 */
public class VisualWindow extends JPanel implements CustomWindow, Updatable {
    private Client client;

    public VisualWindow(Client client) {
        this.client = client;
        initComponents();
        panel = new VisualizationPanel(client);
        localize(LocaleManager.getLanguage());
    }

    @Override
    public synchronized boolean checkForUpdate() {
        return panel.checkForUpdate();
    }

    @Override
    public synchronized void loadData() {
        panel.loadData();
    }

    @Override
    public synchronized void localize(LocaleManager.Lang lang) {
        LocaleManager.setLanguage(lang);

        backButton.setText(LocaleManager.getString("back"));
        UIController.setTitle(LocaleManager.getString("visualizeCommand") + " [" + client.getRequestSender().getUser().getUsername() + "]");
    }

    @Override
    public void clearFields() {

    }

    public void backButtonMouseReleased(MouseEvent e) {
        UIController.switchWindow(UIController.getFrame(), new TableWindow(client));
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - unknown
        panel = new VisualizationPanel(client);
        backButton = new JButton();

        //======== this ========
        setBorder (new javax. swing. border. CompoundBorder( new javax .swing .border .TitledBorder (new javax. swing. border. EmptyBorder
        ( 0, 0, 0, 0) , "", javax. swing. border. TitledBorder. CENTER, javax. swing. border
        . TitledBorder. BOTTOM, new java .awt .Font ("D\u0069al\u006fg" ,java .awt .Font .BOLD ,12 ), java. awt
        . Color. red) , getBorder( )) );  addPropertyChangeListener (new java. beans. PropertyChangeListener( ){ @Override public void
        propertyChange (java .beans .PropertyChangeEvent e) {if ("\u0062or\u0064er" .equals (e .getPropertyName () )) throw new RuntimeException( )
        ; }} );
        setLayout(new MigLayout(
            "fill,hidemode 3,align center center",
            // columns
            "[fill]",
            // rows
            "[grow]" +
            "[]"));

        //======== panel ========
        {
            panel.setLayout(new MigLayout(
                "hidemode 3",
                // columns
                "[fill]" +
                "[fill]",
                // rows
                "[]" +
                "[]" +
                "[]"));
        }
        add(panel, "cell 0 0,grow,width 800:800,height 600:600");

        //---- backButton ----
        backButton.setText("Back");
        backButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                backButtonMouseReleased(e);
            }
        });
        add(backButton, "cell 0 1");
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - unknown
    private VisualizationPanel panel;
    private JButton backButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
