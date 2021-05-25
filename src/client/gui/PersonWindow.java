/*
 * Created by JFormDesigner on Thu May 20 12:23:28 MSK 2021
 */

package client.gui;

import java.awt.event.*;
import javax.swing.*;

import client.Client;
import client.UIController;
import client.impl.OnPersonChangeListener;
import client.util.LocaleManager;
import client.util.Validator;
import client.gui.FieldValidator.ValidationResult;
import common.exceptions.MustBeNotEmptyException;
import common.exceptions.NotInBoundsException;
import common.model.*;
import net.miginfocom.swing.*;

/**
 * @author unknown
 */
public class PersonWindow extends JPanel implements CustomWindow {

    private Client client;
    private OnPersonChangeListener onPersonChangeListener;

    public PersonWindow(Client client) {
        this.client = client;
        initComponents();
        localize(LocaleManager.getLanguage());
    }

    public void setOnPersonChangeListener(OnPersonChangeListener onPersonChangeListener) {
        this.onPersonChangeListener = onPersonChangeListener;
    }

    @Override
    public void localize(LocaleManager.Lang lang) {
        LocaleManager.setLanguage(lang);

        UIController.setTitle(LocaleManager.getString("personWindow") + " [" + client.getRequestSender().getUser().getUsername() + "]");
        nameLabel.setText(LocaleManager.getString("name"));
        weightLabel.setText(LocaleManager.getString("weight"));
        passportLabel.setText(LocaleManager.getString("passport"));
        xLabel.setText(LocaleManager.getString("x"));
        yLabel.setText(LocaleManager.getString("y"));
        locationLabel.setText(LocaleManager.getString("location"));
    }

    @Override
    public void clearFields() {
        JTextField[] fields = new JTextField[] {nameField, weightField, passportField, xField, yField, locationField};
        for (JTextField field: fields) field.setText("");
    }

    private void okButtonMouseReleased(MouseEvent e) {
        Person newPerson = validatePerson();

        if (newPerson == null) return;

        if (onPersonChangeListener != null)
            onPersonChangeListener.onChanged(newPerson);
    }

    private void cancelButtonMouseReleased(MouseEvent e) {
        if (onPersonChangeListener != null) {
            onPersonChangeListener.onChanged(null);
        }
    }

    private Person validatePerson() {
        StringBuilder stringBuilder = new StringBuilder();
        String adminName = "", adminLocationName = "", adminPassport = "";
        int adminX = 0, adminY = 0;
        long adminWeight = 0;

        ValidationResult<String> stringResult = FieldValidator.validateAdminName(nameField.getText());
        if (appendErrors(stringBuilder, stringResult))
            adminName = stringResult.result;

        ValidationResult<Long> longResult = FieldValidator.validateAdminWeight(weightField.getText());
        if (appendErrors(stringBuilder, longResult))
            adminWeight = longResult.result;

        stringResult = FieldValidator.validateAdminPassport(passportField.getText());
        if (appendErrors(stringBuilder, stringResult))
            adminPassport = stringResult.result;

        ValidationResult<Integer> intResult = FieldValidator.validateLocationX(xField.getText());
        if (appendErrors(stringBuilder, intResult))
            adminX = intResult.result;

        intResult = FieldValidator.validateLocationY(yField.getText());
        if (appendErrors(stringBuilder, intResult))
            adminY = intResult.result;

        stringResult = FieldValidator.validateLocationName(locationField.getText());
        if (appendErrors(stringBuilder, stringResult))
            adminLocationName = stringResult.result;

        if (!stringBuilder.toString().isEmpty()) {
            showDialog(this, stringBuilder.toString());
            return null;
        }

        return new Person(adminName, adminWeight, adminPassport, new Location(adminX, adminY, adminLocationName));
    }

    private boolean appendErrors(StringBuilder builder, FieldValidator.ValidationResult<?> result) {
        if (!result.isCorrect) {
            builder.append(result.message).append("\n");
            return false;
        }
        return true;
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - unknown
        nameLabel = new JLabel();
        nameField = new JTextField();
        xLabel = new JLabel();
        xField = new JTextField();
        weightLabel = new JLabel();
        weightField = new JTextField();
        yLabel = new JLabel();
        yField = new JTextField();
        passportLabel = new JLabel();
        passportField = new JTextField();
        locationLabel = new JLabel();
        locationField = new JTextField();
        vSpacer1 = new JPanel(null);
        cancelButton = new JButton();
        okButton = new JButton();

        //======== this ========
        setBorder ( new javax . swing. border .CompoundBorder ( new javax . swing. border .TitledBorder ( new javax . swing. border .EmptyBorder (
        0, 0 ,0 , 0) ,  "JF\u006frm\u0044es\u0069gn\u0065r \u0045va\u006cua\u0074io\u006e" , javax. swing .border . TitledBorder. CENTER ,javax . swing. border .TitledBorder
        . BOTTOM, new java. awt .Font ( "D\u0069al\u006fg", java .awt . Font. BOLD ,12 ) ,java . awt. Color .
        red ) , getBorder () ) );  addPropertyChangeListener( new java. beans .PropertyChangeListener ( ){ @Override public void propertyChange (java .
        beans. PropertyChangeEvent e) { if( "\u0062or\u0064er" .equals ( e. getPropertyName () ) )throw new RuntimeException( ) ;} } );
        setLayout(new MigLayout(
            "hidemode 3,align center center,gapy 16",
            // columns
            "[fill]" +
            "[fill]para" +
            "[fill]" +
            "[fill]",
            // rows
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]"));

        //---- nameLabel ----
        nameLabel.setText("Name:");
        nameLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        add(nameLabel, "cell 0 0");

        //---- nameField ----
        nameField.setColumns(16);
        add(nameField, "cell 1 0");

        //---- xLabel ----
        xLabel.setText("X:");
        xLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        add(xLabel, "cell 2 0");

        //---- xField ----
        xField.setColumns(16);
        add(xField, "cell 3 0");

        //---- weightLabel ----
        weightLabel.setText("Weight:");
        weightLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        add(weightLabel, "cell 0 1");
        add(weightField, "cell 1 1");

        //---- yLabel ----
        yLabel.setText("Y:");
        yLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        add(yLabel, "cell 2 1");
        add(yField, "cell 3 1");

        //---- passportLabel ----
        passportLabel.setText("Passport:");
        passportLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        add(passportLabel, "cell 0 2");
        add(passportField, "cell 1 2");

        //---- locationLabel ----
        locationLabel.setText("Location:");
        locationLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        add(locationLabel, "cell 2 2");
        add(locationField, "cell 3 2");
        add(vSpacer1, "cell 0 3 1 2");

        //---- cancelButton ----
        cancelButton.setText("Cancel");
        cancelButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                cancelButtonMouseReleased(e);
            }
        });
        add(cancelButton, "cell 0 5 2 1");

        //---- okButton ----
        okButton.setText("OK");
        okButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                okButtonMouseReleased(e);
            }
        });
        add(okButton, "cell 2 5 2 1");
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - unknown
    private JLabel nameLabel;
    private JTextField nameField;
    private JLabel xLabel;
    private JTextField xField;
    private JLabel weightLabel;
    private JTextField weightField;
    private JLabel yLabel;
    private JTextField yField;
    private JLabel passportLabel;
    private JTextField passportField;
    private JLabel locationLabel;
    private JTextField locationField;
    private JPanel vSpacer1;
    private JButton cancelButton;
    private JButton okButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
