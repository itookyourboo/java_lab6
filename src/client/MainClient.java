package client;

import client.gui.TableWindow;
import client.impl.Updatable;
import client.util.CommandManager;
import common.Config;

import javax.swing.*;
import java.util.Scanner;

public class MainClient {
    private static int port = Config.PORT;
    private static Client client;
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
        JFrame frame = UIController.initFrame();
        frame.setContentPane(UIController.startWindow(client));
        frame.setVisible(true);

        Thread thread = new Thread(() -> {
            while (true) {
                if (UIController.getPanel() instanceof Updatable) {
                    Updatable window = (Updatable) UIController.getPanel();
                    if (window.checkForUpdate())
                        window.loadData();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException exception) {
                        exception.printStackTrace();
                    }
                }
            }
        });
        thread.start();
    }

    private static void connect(int port) {
        requestSender = new RequestSender(port);
        Scanner scanner = new Scanner(System.in);
        commandManager = new CommandManager(requestSender, scanner);
        System.out.println("Клиент запущен! Порт: " + port);

        client = new Client(requestSender, commandManager);
    }

    public RequestSender getRequestSender() {
        return requestSender;
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }
}
