package server.util;

import common.model.User;
import common.net.CommandResult;
import common.net.Request;
import common.net.ResultStatus;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

public class DatabaseManager {

    ArrayList<User> users = new ArrayList<>();

    private static final String TABLE_USER = "user";
    private static final String TABLE_STUDY_GROUP = "study_group";

    private static final String USER_ID = "id";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";

    private static final String STUDY_GROUP_ID = "id";
    private static final String NAME = "name";
    private static final String COORDINATE_X = "coordinate_x";
    private static final String COORDINATE_Y = "coordinate_y";
    private static final String CREATION_DATE = "creation_date";
    private static final String STUDENTS_COUNT = "students_count";
    private static final String EXPELLED_STUDENTS = "expelled_students";
    private static final String SHOULD_BE_EXPELLED = "should_be_expelled";
    private static final String FORM_OF_EDUCATION = "form_of_education";
    private static final String ADMIN_NAME = "admin_name";
    private static final String ADMIN_WEIGHT = "admin_weight";
    private static final String ADMIN_PASSPORT = "admin_passport";
    private static final String ADMIN_LOCATION_X = "admin_location_x";
    private static final String ADMIN_LOCATION_Y = "admin_location_y";
    private static final String ADMIN_LOCATION_NAME = "admin_location_name";
    private static final String OWNER_USERNAME = "admin_weight";
    private static final String OWNER = "owner";

    private static final String SQL_ADD_USER = String.format("INSERT INTO %s (%s, %s) VALUES (?, ?)",
            TABLE_USER, USERNAME, PASSWORD);
    private static final String SQL_FIND_USERNAME = String.format("SELECT COUNT(*) AS count FROM %s WHERE %s = ?",
            TABLE_USER, USERNAME);
    private static final String SQL_VALIDATE_USER = String.format("SELECT COUNT(*) AS count FROM %s WHERE %s = ? AND %s = ?",
            TABLE_USER, USERNAME, PASSWORD);
    private static final String SQL_ADD_STUDY_GROUP = String.format("INSERT INTO %s (" +
            "%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)" +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
            TABLE_STUDY_GROUP,
            NAME, COORDINATE_X, COORDINATE_Y, CREATION_DATE, STUDENTS_COUNT, EXPELLED_STUDENTS, SHOULD_BE_EXPELLED, FORM_OF_EDUCATION,
            ADMIN_NAME, ADMIN_WEIGHT, ADMIN_PASSPORT, ADMIN_LOCATION_X, ADMIN_LOCATION_Y, ADMIN_LOCATION_NAME, OWNER_USERNAME);
    private static final String SQL_GET_MIN_STUDY_GROUP_NAME = String.format("SELECT %s FROM %s ORDER BY %s LIMIT 1",
            NAME, TABLE_STUDY_GROUP, NAME);
    private static final String SQL_COUNT_BY_GROUP_ADMIN = String.format("SELECT COUNT(*) as count FROM %s WHERE %s = ?",
            TABLE_STUDY_GROUP, ADMIN_NAME);
    private static final String SQL_FILTER_GREATER_THAN_EXPELLED_STUDENTS = String.format("SELECT * FROM %s WHERE %s > ?",
            TABLE_STUDY_GROUP, EXPELLED_STUDENTS);
    private static final String SQL_REMOVE_ALL_BY_SHOULD_BE_EXPELLED = String.format("DELETE FROM %s WHERE %s = ?",
            TABLE_STUDY_GROUP, SHOULD_BE_EXPELLED);
    private static final String SQL_REMOVE_BY_ID = String.format("DELETE FROM %s WHERE %s = ?",
            TABLE_STUDY_GROUP, STUDY_GROUP_ID);
    private static final String SQL_REMOVE_GREATER = String.format("DELETE FROM %s WHERE %s > ?",
            TABLE_STUDY_GROUP, NAME);
    private static final String SQL_REMOVE_LOWER = String.format("DELETE FROM %s WHERE %s < ?",
            TABLE_STUDY_GROUP, NAME);
    private static final String SQL_UPDATE_BY_ID = String.format("UPDATE %s SET " +
            "%s = ?, %s = ?, %s = ?, %s = ?, %s = ?, %s = ?, %s = ?, %s = ?, %s = ?, %s = ?, %s = ?, %s = ?, %s = ? " +
            "WHERE %s = ?",
            TABLE_STUDY_GROUP, NAME, COORDINATE_X, COORDINATE_Y, STUDENTS_COUNT, EXPELLED_STUDENTS, SHOULD_BE_EXPELLED, FORM_OF_EDUCATION,
            ADMIN_NAME, ADMIN_WEIGHT, ADMIN_PASSPORT, ADMIN_LOCATION_X, ADMIN_LOCATION_Y, ADMIN_LOCATION_NAME, STUDY_GROUP_ID);

    private String url;
    private String username;
    private String password;
    private Connection connection;

    public DatabaseManager(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public void connectToDatabase() {
        try {
            connection = DriverManager.getConnection(url, username, password);
            System.out.println("Подключение к базе данных установлено.");
        } catch (SQLException e) {
            System.out.println("Не удалось выполнить подключение к базе данных.");
            e.printStackTrace();
            System.exit(-1);
        }
    }

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

    public void rollback() {
        try {
            connection.rollback();
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            System.out.println("Не удалось откатить изменения.");
        }
    }
}
