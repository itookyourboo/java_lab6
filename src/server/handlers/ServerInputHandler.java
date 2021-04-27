package server.handlers;

import common.DataManager;

import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

public class ServerInputHandler extends Thread {
    private DataManager dataManager;
    private AtomicBoolean exit;

    public ServerInputHandler(DataManager collectionManager, AtomicBoolean exit) {
        this.dataManager = collectionManager;
        this.exit = exit;
    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        while (true){
            if(scanner.hasNextLine()){
                String serverCommand = scanner.nextLine();

                switch (serverCommand){
                    case "save":
                        dataManager.save();
                        break;
                    case "exit":
                        exit.set(true);
                        System.exit(0);
                        return;
                    default:
                        System.out.println("Такой команды нет.");
                        break;
                }
            } else{
                exit.set(true);
                return;
            }
        }
    }
}
