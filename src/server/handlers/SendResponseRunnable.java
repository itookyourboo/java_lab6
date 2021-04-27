package server.handlers;

import common.net.CommandResult;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.channels.SocketChannel;

public class SendResponseRunnable implements Runnable {

    private SocketChannel socketChannel;
    private CommandResult response;

    public SendResponseRunnable(SocketChannel socketChannel, CommandResult response) {
        this.socketChannel = socketChannel;
        this.response = response;
    }

    @Override
    public void run() {
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socketChannel.socket().getOutputStream());
            objectOutputStream.writeObject(response);
            objectOutputStream.flush();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
