package client.commands;

import client.RequestSender;
import client.util.Interactor;
import common.Crypto;
import common.exceptions.WrongAmountOfArgumentsException;
import common.model.User;
import common.net.CommandResult;
import common.net.Request;
import common.net.ResultStatus;

import java.util.Scanner;

public class LoginCommand extends Command {

    public LoginCommand(RequestSender requestSender, Scanner scanner) {
        super(requestSender, scanner);
    }

    @Override
    public String getName() {
        return "login";
    }

    @Override
    public String getDescription() {
        return "произвести аутентификацию пользователя";
    }

    @Override
    public void execute(String stringArgument) {
        try {
            if (!stringArgument.isEmpty()) throw new WrongAmountOfArgumentsException();
            String username = Interactor.askUsername(scanner);
            String password = Interactor.askPassword(scanner);
            password = Crypto.encryptString(password);
            User user = new User(username, password);
            Request<User> request = new Request<>(getName(), user);
            CommandResult result = requestSender.sendRequest(request);
            if (result.status == ResultStatus.OK) {
                Interactor.println(result.message);
                requestSender.setUser(user);
            } else
                Interactor.printError(result.message);
        } catch (WrongAmountOfArgumentsException exception) {
            Interactor.println("Использование: '" + getName() + "'");
        }
    }

    @Override
    public boolean canExecuteIfNotAuthed() {
        return true;
    }
}
