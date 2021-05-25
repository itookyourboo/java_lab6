/*
 * Created by JFormDesigner on Mon May 24 11:29:23 MSK 2021
 */

package client.gui;

import java.awt.event.*;
import javax.swing.*;

import client.Client;
import client.UIController;
import client.commands.Command;
import client.commands.LoginCommand;
import client.commands.RegisterCommand;
import client.util.LocaleManager;
import client.util.Validator;
import common.net.CommandResult;
import common.net.ResultStatus;
import net.miginfocom.swing.*;

/**
 * @author unknown
 */
public class LoginWindow extends JPanel implements CustomWindow {
    private Client client;

    public LoginWindow(Client client) {
        this.client = client;
        initComponents();
        localize(LocaleManager.getLanguage());
    }

    private void loginButtonMouseReleased(MouseEvent e) {
        loginOrRegister(new LoginCommand(client.getRequestSender()));
    }

    private void regButtonMouseReleased(MouseEvent e) {
        loginOrRegister(new RegisterCommand(client.getRequestSender()));
    }

    private void loginOrRegister(Command command) {
        String username = loginField.getText();
        String password = String.valueOf(passwordField.getPassword());

        if (!Validator.validateUsername(username)) {
            showDialog(this, LocaleManager.getString("invalidUsername"));
            return;
        }

        if (!Validator.validatePassword(password)) {
            showDialog(this, LocaleManager.getString("invalidPassword"));
            return;
        }

        CommandResult result = command.executeWithObjectArgument(username, password);

        if (result.status == ResultStatus.OK) {
            UIController.switchWindow(UIController.getFrame(), new TableWindow(client));
            clearFields();
        } else
            showDialog(this, LocaleManager.getString(result.message));
    }

    private void localeBoxItemStateChanged(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED) {
            String item = (String) e.getItem();
            LocaleManager.Lang lang = LocaleManager.langMap.get(item);
            localize(lang);
        }
    }

    @Override
    public void localize(LocaleManager.Lang lang) {
        LocaleManager.setLanguage(lang);

        updateLocaleBox(lang);
        loginLabel.setText(LocaleManager.getString("login"));
        passwordLabel.setText(LocaleManager.getString("password"));
        loginButton.setText(LocaleManager.getString("enter"));
        regButton.setText(LocaleManager.getString("register"));
        localeLabel.setText(LocaleManager.getString("locale"));
        UIController.setTitle(LocaleManager.getString("loginWindow"));
    }

    private void updateLocaleBox(LocaleManager.Lang lang) {
        int i = 0;
        for (LocaleManager.Lang l: LocaleManager.Lang.values()) {
            if (l.getName().equals(lang.getName())) {
                localeBox.setSelectedIndex(i);
                break;
            }
            i++;
        }
    }

    @Override
    public void clearFields() {
        JTextField[] fields = new JTextField[] {loginField, passwordField};
        for (JTextField field: fields) field.setText("");
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - unknown
        loginLabel = new JLabel();
        loginField = new JTextField();
        passwordLabel = new JLabel();
        passwordField = new JPasswordField();
        loginButton = new JButton();
        regButton = new JButton();
        localeLabel = new JLabel();
        localeBox = new JComboBox();

        //======== this ========
        setBorder(new javax.swing.border.CompoundBorder(new javax.swing.border.TitledBorder(new javax
        .swing.border.EmptyBorder(0,0,0,0), "JF\u006frmD\u0065sig\u006eer \u0045val\u0075ati\u006fn",javax.swing
        .border.TitledBorder.CENTER,javax.swing.border.TitledBorder.BOTTOM,new java.awt.
        Font("Dia\u006cog",java.awt.Font.BOLD,12),java.awt.Color.red
        ), getBorder())); addPropertyChangeListener(new java.beans.PropertyChangeListener(){@Override
        public void propertyChange(java.beans.PropertyChangeEvent e){if("\u0062ord\u0065r".equals(e.getPropertyName(
        )))throw new RuntimeException();}});
        setLayout(new MigLayout(
            "hidemode 3,align center center",
            // columns
            "[fill]para" +
            "[fill]",
            // rows
            "para[]para" +
            "[]para" +
            "[]para" +
            "[]" +
            "[]para" +
            "[]"));

        //---- loginLabel ----
        loginLabel.setText("Login");
        loginLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        add(loginLabel, "cell 0 0");

        //---- loginField ----
        loginField.setColumns(16);
        add(loginField, "cell 1 0");

        //---- passwordLabel ----
        passwordLabel.setText("Password");
        passwordLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        add(passwordLabel, "cell 0 1");
        add(passwordField, "cell 1 1");

        //---- loginButton ----
        loginButton.setText("Log in");
        loginButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                loginButtonMouseReleased(e);
            }
        });
        add(loginButton, "cell 0 2 2 1");

        //---- regButton ----
        regButton.setText("Registration");
        regButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                regButtonMouseReleased(e);
            }
        });
        add(regButton, "cell 0 3 2 1");

        //---- localeLabel ----
        localeLabel.setText("Locale");
        localeLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        add(localeLabel, "cell 0 5");

        //---- localeBox ----
        localeBox.addItemListener(e -> localeBoxItemStateChanged(e));
        add(localeBox, "cell 1 5");
        // JFormDesigner - End of component initialization  //GEN-END:initComponents

        localeBox.setModel(new DefaultComboBoxModel(LocaleManager.langMap.keySet().toArray(new String[0])));
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - unknown
    private JLabel loginLabel;
    private JTextField loginField;
    private JLabel passwordLabel;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton regButton;
    private JLabel localeLabel;
    private JComboBox localeBox;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
