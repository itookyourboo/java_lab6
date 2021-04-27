package server.handlers;

import common.net.CommandResult;
import common.net.Request;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ReadRequestRunnable implements Runnable {
    private SocketChannel socketChannel;
    private RequestHandler requestHandler;
    private ExecutorService handleRequestThread = Executors.newFixedThreadPool(10);
    private ExecutorService sendResponseThread = Executors.newFixedThreadPool(5);

    public ReadRequestRunnable(SocketChannel socketChannel, RequestHandler requestHandler) {
        this.socketChannel = socketChannel;
        this.requestHandler = requestHandler;
    }

    @Override
    public void run() {
        Request<?> request;
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(socketChannel.socket().getInputStream());
            request = (Request<?>) objectInputStream.readObject();
            System.out.println(socketChannel.getRemoteAddress() + ": " + request.command);
            CommandResult response = requestHandler.handleRequest(request, handleRequestThread);
            sendResponseThread.submit(new SendResponseRunnable(socketChannel, response));
        } catch (IOException | ClassNotFoundException exception) {
            //System.out.println("Хост принудительно разорвал соединение");
        }
    }
}
