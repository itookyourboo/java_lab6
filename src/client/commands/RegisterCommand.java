package client.commands;

import client.RequestSender;

import java.util.Scanner;

public class RegisterCommand extends LoginCommand {

    public RegisterCommand(RequestSender requestSender) {
        super(requestSender);
    }

    public RegisterCommand(RequestSender requestSender, Scanner scanner) {
        super(requestSender, scanner);
    }

    @Override
    public String getName() {
        return "register";
    }

    @Override
    public String getDescription() {
        return "зарегистрировать нового пользователя";
    }

    @Override
    public boolean canExecuteIfNotAuthed() {
        return true;
    }
}
