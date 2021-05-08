/*
 * Created by JFormDesigner on Sat May 08 14:39:58 MSK 2021
 */

package client.gui;

import client.impl.LoginContract;
import client.util.LocaleManager;

import javax.swing.*;
import java.awt.*;

public class LoginWindow extends JFrame {

    private LoginContract loginContract;

    public LoginWindow() {
        super(LocaleManager.getString("loginTitle"));
        initWindow();
        initComponents();
    }

    private void initWindow() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize().getSize();
        int width = dimension.width, height = dimension.height;
        int WINDOW_WIDTH = width / 5, WINDOW_HEIGHT = height / 5;
        setBounds(width / 2 - WINDOW_WIDTH / 2, height / 2 - WINDOW_HEIGHT / 2, WINDOW_WIDTH, WINDOW_HEIGHT);
        setResizable(false);
    }

    public void setLoginContract(LoginContract loginContract) {
        this.loginContract = loginContract;
    }

    private void initComponents() {
        setLayout(new GridBagLayout());

        JLabel loginLabel = new JLabel(LocaleManager.getString("login"));
        JLabel passwordLabel = new JLabel(LocaleManager.getString("password"));

        JTextField loginTextField = new JTextField(16);
        JPasswordField passwordTextField = new JPasswordField(16);

        JButton loginButton = new JButton(LocaleManager.getString("enter"));
        loginButton.addActionListener(l ->
                loginContract.onLoginClicked(loginTextField.getText(),
                        String.valueOf(passwordTextField.getPassword())));

        JButton registerButton = new JButton(LocaleManager.getString("register"));
        registerButton.addActionListener(l ->
                loginContract.onRegisterClicked(loginTextField.getText(),
                        String.valueOf(passwordTextField.getPassword())));

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = c.gridy = 0;
        c.gridwidth = c.gridheight = 1;
        c.insets = new Insets(5, 5, 5, 5);
        c.anchor = GridBagConstraints.LINE_END;
        add(loginLabel, c);

        c.gridx = 1;
        add(loginTextField, c);

        c.gridy = 1;
        add(passwordTextField, c);

        c.gridx = 0;
        add(passwordLabel, c);

        c.gridy = 2;
        c.gridwidth = 2;
        c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        add(loginButton, c);

        c.gridy = 3;
        add(registerButton, c);

        pack();
    }

    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message);
    }
}
