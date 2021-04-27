package server;

import common.Config;
import common.DataManager;
import server.execution.ExecutionService;
import server.handlers.ReadRequestRunnable;
import server.handlers.RequestHandler;
import server.handlers.ServerInputHandler;
import server.util.CollectionManager;
import server.util.DatabaseManager;
import server.util.FileManager;
import sun.nio.ch.SocketAdaptor;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public class MainServer {

    private static int port = Config.PORT;
    private static final ExecutorService readRequestThreadPool = Executors.newFixedThreadPool(10);

    public static void main(String[] args) {
        if (args.length == 1) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (Exception exception) {
                System.out.println("Не получается спарсить порт. Используется " + port);
            }
        }

        String[] loginData = FileManager.getLoginData();
        if (loginData == null) {
            return;
        }

        DatabaseManager databaseManager;
        try {
            databaseManager = new DatabaseManager(Config.jdbcLocal, loginData[0], loginData[1]);
            databaseManager.connectToDatabase();
        } catch (Exception exception) {
            exception.printStackTrace();
            return;
        }

        DataManager dataManager;
        try {
            dataManager = new CollectionManager(databaseManager);
            //dataManager = new CollectionManager(new FileManager(Config.ENV_VAR));
        } catch (Exception exception) {
            exception.printStackTrace();
            return;
        }

        ServerSocketChannel serverSocketChannel;
        try {
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.bind(new InetSocketAddress(port));
            System.out.println("Сервер запущен. Порт: " + port);
        } catch (IOException exception) {
            System.out.println("Ошибка запуска сервера!");
            exception.printStackTrace();
            System.out.println(exception.getMessage());
            return;
        }

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Выход");
            dataManager.save();
        }));

        ExecutionService executionService = new ExecutionService(dataManager, databaseManager);

        AtomicBoolean exit = new AtomicBoolean(false);
        new ServerInputHandler(dataManager, exit).start();

        while (!exit.get()) {
            try {
                SocketChannel socketChannel = serverSocketChannel.accept();
                if (socketChannel == null) continue;
                RequestHandler requestHandler = new RequestHandler(executionService);
                readRequestThreadPool.submit(new ReadRequestRunnable(socketChannel, requestHandler));
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
    }
}
