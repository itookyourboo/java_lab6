package client;

import common.Config;
import common.model.User;
import common.net.CommandResult;
import common.net.Request;
import common.net.ResultStatus;

import java.io.*;
import java.net.Socket;

public class RequestSender {
    protected final int MAX_ATTEMPTS_COUNT = 5;
    private int port = Config.PORT;
    private User user;

    public RequestSender() {}

    public RequestSender(int port) {
        this.port = port;
    }

    public CommandResult sendRequest(Request<?> request){
        if(request == null){
            throw new IllegalArgumentException("Запрос не может быть null!");
        }

        request.user = user;
        int attempts = 0;
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
                    System.out.println("Подключение восстановлено.");
                }
                attempts = MAX_ATTEMPTS_COUNT;
                return result;
            }
            catch (IOException | ClassNotFoundException exc){
                System.out.println("Не удалось подключиться к серверу, подождём...");
                attempts++;
                try {
                    Thread.sleep(5 * 1000);
                }
                catch (Exception e) { }
            }
        }
        return new CommandResult(ResultStatus.ERROR, "Прошло 25 секунд, сервер умер.");
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
