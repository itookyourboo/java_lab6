package client;

import client.commands.Command;
import client.commands.LoginCommand;
import client.commands.RegisterCommand;
import client.commands.ShowCommand;
import client.gui.LoginWindow;
import client.gui.MainWindow;
import client.impl.LoginContract;
import client.util.CommandManager;
import client.util.Interactor;
import common.Config;
import common.Crypto;
import common.model.StudyGroup;
import common.model.User;
import common.net.CommandResult;
import common.net.Request;
import common.net.ResultStatus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class MainClient {
    private static int port = Config.PORT;
    private static RequestSender requestSender;
    private static CommandManager commandManager;

    public static void main(String[] args) {
        if (args.length == 1) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (Exception exception) {
                System.out.println("Не получается спарсить порт.");
            }
        }
        connect(port);
        showLoginWindow();
    }

    private static void showLoginWindow() {
        LoginWindow loginWindow = new LoginWindow();
        loginWindow.setLoginContract(new LoginContract() {
            @Override
            public void onLoginClicked(String username, String password) {
                loginOrRegister(new LoginCommand(requestSender), username, password);
            }

            @Override
            public void onRegisterClicked(String username, String password) {
                loginOrRegister(new RegisterCommand(requestSender), username, password);
            }

            private void loginOrRegister(Command command, String username, String password) {
                CommandResult result = command.executeWithObjectArgument(username, password);
                if (result.status == ResultStatus.OK) {
                    showMainWindow();
                    loginWindow.setVisible(false);
                } else
                    loginWindow.showError(result.message);
            }
        });
        loginWindow.setVisible(true);
    }

    private static void showMainWindow() {
        List<StudyGroup> list;
        CommandResult result = new ShowCommand(requestSender).executeWithObjectArgument();
        if (result.status == ResultStatus.OK) {
            list = Arrays.stream(result.message.split("\n"))
                    .map(StudyGroup::fromJson)
                    .collect(Collectors.toList());
        } else {
            list = new ArrayList<>();
        }
        MainWindow mainWindow = new MainWindow(list);
        mainWindow.setVisible(true);
    }

    private static void connect(int port) {
        requestSender = new RequestSender(port);
        Scanner scanner = new Scanner(System.in);
        commandManager = new CommandManager(requestSender, scanner);
        System.out.println("Клиент запущен! Порт: " + port);
    }

    public RequestSender getRequestSender() {
        return requestSender;
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }
}
