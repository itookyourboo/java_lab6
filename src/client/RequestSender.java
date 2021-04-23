package client;

import common.Config;
import common.net.CommandResult;
import common.net.Request;
import common.net.ResultStatus;

import java.io.*;
import java.net.Socket;

public class RequestSender {
    protected final int MAX_ATTEMPTS_COUNT = 5;
    private int port = Config.PORT;

    public RequestSender() {}

    public RequestSender(int port) {
        this.port = port;
    }

    public CommandResult sendRequest(Request<?> request){
        if(request == null){
            throw new IllegalArgumentException("Запрос не может быть null!");
        }

        int attempts = 0; // Сколько было попыток отправить запрос
        while (attempts < MAX_ATTEMPTS_COUNT){
            try{
                Socket socket = new Socket(Config.IP, port);

                OutputStream os = socket.getOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(os);
                oos.writeObject(request);

                InputStream is = socket.getInputStream();
                ObjectInputStream ois = new ObjectInputStream(is);
                CommandResult result = (CommandResult) ois.readObject();
                if(attempts != 0){
                    System.out.println("Вау! Появилось подключение!");
                }
                attempts = MAX_ATTEMPTS_COUNT;
                return result;
            }
            catch (IOException | ClassNotFoundException exc){
                System.out.println("Не удалось подключиться к серверу, но мы подождём, мало ли...");
                attempts++;
                try {
                    Thread.sleep(5 * 1000);
                }
                catch (Exception e) { }
            }
        }
        return new CommandResult(ResultStatus.ERROR, "Не удалось подключиться к серверу, больше пытаться нет смысла.");
    }
}
