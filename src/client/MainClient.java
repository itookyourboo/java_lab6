package client;

import client.util.CommandManager;
import client.util.Interactor;
import common.Config;

import java.util.Scanner;

public class MainClient {
    private static int port = Config.PORT;

    public static void main(String[] args) {
        if (args.length == 1) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (Exception exception) {
                System.out.println("Не получается спарсить порт.");
            }
        }

        RequestSender requestSender = new RequestSender(port);
        Scanner scanner = new Scanner(System.in);
        CommandManager commandManager = new CommandManager(requestSender, scanner);
        System.out.println("Клиент запущен! Порт: " + port);
        String input;
        do {
            if (!scanner.hasNextLine()) return;
            input = scanner.nextLine();
            try {
                commandManager.parseAndExecute(input);
            } catch (Exception e) {
                Interactor.printError(e.getMessage());
            }
            Interactor.println("");
        } while (!input.equals("exit"));
    }
}
