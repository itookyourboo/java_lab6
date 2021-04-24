package server;

import common.Config;
import common.DataManager;
import common.net.CommandResult;
import common.net.Request;
import common.net.ResultStatus;
import server.execution.ExecutionService;
import server.util.CollectionManager;
import server.util.DatabaseManager;
import server.util.FileManager;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

public class MainServer {

    private static int port = Config.PORT;

    public static void main(String[] args) {
        if (args.length == 1) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (Exception exception) {
                System.out.println("Не получается спарсить порт. Используется " + port);
            }
        }

        DatabaseManager databaseManager;
        try {
            databaseManager = new DatabaseManager();
        } catch (Exception exception) {
            exception.printStackTrace();
            return;
        }

        DataManager dataManager;
        try {
            dataManager = new CollectionManager(new FileManager(Config.ENV_VAR));
        } catch (Exception exception) {
            exception.printStackTrace();
            return;
        }

        ServerSocketChannel serverSocketChannel;
        try {
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.bind(new InetSocketAddress(port));
            serverSocketChannel.configureBlocking(false);
            System.out.println("Сервер запущен. Порт: " + port);
        } catch (IOException exception) {
            System.out.println("Ошибка запуска сервера!");
            System.out.println(exception.getMessage());
            return;
        }

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Выход");
            save(dataManager);
        }));

        ExecutionService executionService = new ExecutionService(dataManager, databaseManager);

        AtomicBoolean exit = new AtomicBoolean(false);
        getUserInputHandler(dataManager, exit).start();

        while (!exit.get()) {
            try (SocketChannel socketChannel = serverSocketChannel.accept()) {
                if (socketChannel == null) continue;

                ObjectInputStream objectInputStream = new ObjectInputStream(socketChannel.socket().getInputStream());
                Request<?> request = (Request<?>) objectInputStream.readObject();
                System.out.println(socketChannel.getRemoteAddress() + ": " + request.command);

                CommandResult result = executionService.executeCommand(request);
                if (result.status == ResultStatus.OK)
                    System.out.println("Команда выполнена успешно");
                else
                    System.out.println("Команда выполнена неуспешно");

                ObjectOutputStream objectOutputStream = new ObjectOutputStream(socketChannel.socket().getOutputStream());
                objectOutputStream.writeObject(result);
            } catch (IOException | ClassNotFoundException exception) {
                exception.printStackTrace();
            }
        }
    }

    private static void save(DataManager dataManager) {
        dataManager.save();
    }

    private static Thread getUserInputHandler(DataManager dataManager, AtomicBoolean exit){
        return new Thread(() -> {
            Scanner scanner = new Scanner(System.in);

            while (true){
                if(scanner.hasNextLine()){
                    String serverCommand = scanner.nextLine();
                    System.out.println("Введено: " + serverCommand);

                    switch (serverCommand){
                        case "save":
                            save(dataManager);
                            break;
                        case "exit":
                            exit.set(true);
                            return;
                        default:
                            System.out.println("Такой команды нет.");
                            break;
                    }
                }
                else{
                    exit.set(true);
                    return;
                }
            }
        });
    }
}
