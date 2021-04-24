package server.util;

import common.model.User;
import common.net.CommandResult;
import common.net.Request;
import common.net.ResultStatus;

import java.util.ArrayList;

public class DatabaseManager {

    ArrayList<User> users = new ArrayList<>();

    public CommandResult login(Request<?> request) {
        try {
            User user = (User) request.entity;
            if (userExists(user))
                return new CommandResult(ResultStatus.OK,
                        String.format("Добро пожаловать, %s!", user.getUsername()));
            return new CommandResult(ResultStatus.ERROR, "Неверный логин или пароль.");
        } catch (Exception exception) {
            return new CommandResult(ResultStatus.ERROR, "Передан аргумент другого типа");
        }
    }

    public CommandResult register(Request<?> request) {
        try {
            User user = (User) request.entity;
            if (isUsernameFree(user.getUsername())) {
                users.add(user);
                return new CommandResult(ResultStatus.OK,
                        String.format("Добро пожаловать, %s!", user.getUsername()));
            }
            return new CommandResult(ResultStatus.ERROR, "Пользователь с таким именем уже зарегистрирован.");
        } catch (Exception exception) {
            return new CommandResult(ResultStatus.ERROR, "Передан аргумент другого типа");
        }
    }

    private boolean isUsernameFree(String username) {
        return users.stream().noneMatch(user -> user.getUsername().equals(username));
    }

    private boolean userExists(User user) {
        return users.stream().anyMatch(user1 -> user1.equals(user));
    }
}
