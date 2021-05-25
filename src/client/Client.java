package client;

import client.util.CommandManager;
import client.util.LocaleManager;

public class Client {

    public static LocaleManager.Lang language = LocaleManager.Lang.ru_RU;
    private final RequestSender requestSender;
    private final CommandManager commandManager;

    public Client(RequestSender requestSender, CommandManager commandManager) {
        this.requestSender = requestSender;
        this.commandManager = commandManager;
    }

    public RequestSender getRequestSender() {
        return requestSender;
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }
}
