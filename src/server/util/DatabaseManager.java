package server.util;

import common.exceptions.AccessDeniedException;
import common.exceptions.NoStudyGroupWithSuchId;
import common.model.*;
import common.net.CommandResult;
import common.net.Request;
import common.net.ResultStatus;

import java.sql.*;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

public class DatabaseManager {

    private static final String TABLE_USER = "\"user\"";
    private static final String TABLE_STUDY_GROUP = "study_group";

    private static final String USER_ID = "id";
    private static final String USERNAME = "\"username\"";
    private static final String PASSWORD = "\"password\"";

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
    private static final String OWNER_USERNAME = "owner_username";
    private static final String OWNER = "owner";

    private static final String SQL_ADD_USER = String.format("INSERT INTO %s (%s, %s) VALUES (?, ?)",
            TABLE_USER, USERNAME, PASSWORD);

    private static final String SQL_FIND_USERNAME = String.format("SELECT COUNT(*) FROM %s WHERE %s = ?",
            TABLE_USER, USERNAME);
    private static final String SQL_VALIDATE_USER = String.format("SELECT COUNT(*) FROM %s WHERE %s = ? AND %s = ?",
            TABLE_USER, USERNAME, PASSWORD);

    private static final String SQL_ADD_STUDY_GROUP = String.format("INSERT INTO %s (" +
            "%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)" +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING %s",
            TABLE_STUDY_GROUP,
            NAME, COORDINATE_X, COORDINATE_Y, CREATION_DATE, STUDENTS_COUNT, EXPELLED_STUDENTS, SHOULD_BE_EXPELLED, FORM_OF_EDUCATION,
            ADMIN_NAME, ADMIN_WEIGHT, ADMIN_PASSPORT, ADMIN_LOCATION_X, ADMIN_LOCATION_Y, ADMIN_LOCATION_NAME, OWNER_USERNAME,
            STUDY_GROUP_ID);
    private static final String SQL_GET_MIN_STUDY_GROUP_NAME = String.format("SELECT %s FROM %s ORDER BY %s LIMIT 1",
            NAME, TABLE_STUDY_GROUP, NAME);
//    private static final String SQL_COUNT_BY_GROUP_ADMIN = String.format("SELECT COUNT(*) as count FROM %s WHERE %s = ?",
//            TABLE_STUDY_GROUP, ADMIN_NAME);
//    private static final String SQL_FILTER_GREATER_THAN_EXPELLED_STUDENTS = String.format("SELECT * FROM %s WHERE %s > ?",
//            TABLE_STUDY_GROUP, EXPELLED_STUDENTS);
    private static final String SQL_REMOVE_BY_ID = String.format("DELETE FROM %s WHERE %s = ?",
            TABLE_STUDY_GROUP, STUDY_GROUP_ID);
    private static final String SQL_GET_GREATER = String.format("SELECT %s, %s FROM %s WHERE %s > ?",
            STUDY_GROUP_ID, OWNER_USERNAME, TABLE_STUDY_GROUP, NAME);
    private static final String SQL_GET_LOWER = String.format("SELECT %s, %s FROM %s WHERE %s < ?",
            STUDY_GROUP_ID, OWNER_USERNAME, TABLE_STUDY_GROUP, NAME);
    private static final String SQL_GET_ALL_BY_SHOULD_BE_EXPELLED = String.format("SELECT %s, %s FROM %s WHERE %s = ?",
            STUDY_GROUP_ID, OWNER_USERNAME, TABLE_STUDY_GROUP, SHOULD_BE_EXPELLED);
    private static final String SQL_UPDATE_BY_ID = String.format("UPDATE %s SET " +
            "%s = ?, %s = ?, %s = ?, %s = ?, %s = ?, %s = ?, %s = ?, %s = ?, %s = ?, %s = ?, %s = ?, %s = ?, %s = ? " +
            "WHERE %s = ?",
            TABLE_STUDY_GROUP, NAME, COORDINATE_X, COORDINATE_Y, STUDENTS_COUNT, EXPELLED_STUDENTS, SHOULD_BE_EXPELLED, FORM_OF_EDUCATION,
            ADMIN_NAME, ADMIN_WEIGHT, ADMIN_PASSPORT, ADMIN_LOCATION_X, ADMIN_LOCATION_Y, ADMIN_LOCATION_NAME, STUDY_GROUP_ID);

    private static final String SQL_GET_LAST_ID = String.format("SELECT %s FROM %s ORDER BY %s DESC LIMIT 1",
            STUDY_GROUP_ID, TABLE_STUDY_GROUP, STUDY_GROUP_ID);
    private static final String SQL_ID_EXISTENCE = String.format("SELECT COUNT(*) as count FROM %s WHERE %s = ?",
            TABLE_STUDY_GROUP, STUDY_GROUP_ID);
    private static final String SQL_GET_STUDY_GROUPS = String.format("SELECT * FROM %s",
            TABLE_STUDY_GROUP);
    private static final String SQL_BELONGS_TO_USER = String.format("SELECT %s FROM %s WHERE %s = ?",
            OWNER_USERNAME, TABLE_STUDY_GROUP, STUDY_GROUP_ID);

    private final String url;
    private final String username;
    private final String password;
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
            if (validateUser(user))
                return new CommandResult(ResultStatus.OK,
                        String.format("Добро пожаловать, %s!", user.getUsername()));
            return new CommandResult(ResultStatus.ERROR, "Неверный логин или пароль.");
        } catch (SQLException exception) {
            exception.printStackTrace();
            return new CommandResult(ResultStatus.ERROR, "SQL-ошибка на сервере");
        } catch (Exception exception) {
            return new CommandResult(ResultStatus.ERROR, "Передан аргумент другого типа");
        }
    }

    public CommandResult register(Request<?> request) {
        try {
            User user = (User) request.entity;
            if (!userExists(user.getUsername())) {
                registerUser(user);
                return new CommandResult(ResultStatus.OK,
                        String.format("Добро пожаловать, %s!", user.getUsername()));
            }
            return new CommandResult(ResultStatus.ERROR, "Пользователь с таким именем уже зарегистрирован.");
        } catch (SQLException exception) {
            exception.printStackTrace();
            return new CommandResult(ResultStatus.ERROR, "SQL-ошибка на сервере");
        } catch (Exception exception) {
            return new CommandResult(ResultStatus.ERROR, "Передан аргумент другого типа");
        }
    }

    public boolean idExists(int id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(SQL_ID_EXISTENCE);
        statement.setInt(1, id);
        ResultSet resultSet = statement.executeQuery();
        resultSet.next();
        return resultSet.getInt(1) != 0;
    }

    public String getMinStudyGroupName() throws SQLException {
        PreparedStatement statement = connection.prepareStatement(SQL_GET_MIN_STUDY_GROUP_NAME);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next())
            return resultSet.getString(NAME);
        return null;
    }

    public void registerUser(User user) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(SQL_ADD_USER);
        statement.setString(1, user.getUsername());
        statement.setString(2, user.getHashedPassword());
        statement.executeUpdate();
        statement.close();
    }

    public boolean userExists(String username) throws SQLException {
        System.out.println(SQL_FIND_USERNAME);
        PreparedStatement statement = connection.prepareStatement(SQL_FIND_USERNAME);
        statement.setString(1, username);
        ResultSet resultSet = statement.executeQuery();
        resultSet.next();
        int count = resultSet.getInt(1);
        statement.close();
        return count != 0;
    }

    public boolean validateUser(User user) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(SQL_VALIDATE_USER);
        statement.setString(1, user.getUsername());
        statement.setString(2, user.getHashedPassword());
        ResultSet resultSet = statement.executeQuery();
        resultSet.next();
        int count = resultSet.getInt(1);
        statement.close();
        return count != 0;
    }

    public boolean belongsToUser(int id, String username) throws SQLException {
        if (!idExists(id)) return false;

        PreparedStatement statement = connection.prepareStatement(SQL_BELONGS_TO_USER);
        statement.setInt(1, id);
        ResultSet resultSet = statement.executeQuery();
        resultSet.next();
        String owner = resultSet.getString(OWNER_USERNAME);

        return username.equals(owner);
    }

    public boolean removeById(int id, String username) throws SQLException, AccessDeniedException, NoStudyGroupWithSuchId {
        if (!idExists(id)) throw new NoStudyGroupWithSuchId();
        if (!belongsToUser(id, username)) throw new AccessDeniedException();

        PreparedStatement statement = connection.prepareStatement(SQL_REMOVE_BY_ID);
        statement.setInt(1, id);
        statement.executeUpdate();

        return true;
    }

    public List<Map.Entry<Integer, String>> getIdAndOwnerList(String SQL, Object argument) throws SQLException {
        List<Map.Entry<Integer, String>> list = new ArrayList<>();
        PreparedStatement statement = connection.prepareStatement(SQL);
        if (argument instanceof String)
            statement.setString(1, (String) argument);
        else if (argument instanceof Integer)
            statement.setInt(1, (Integer) argument);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            Integer id = resultSet.getInt(STUDY_GROUP_ID);
            String owner = resultSet.getString(OWNER_USERNAME);
            list.add(new AbstractMap.SimpleEntry<Integer, String>(id, owner));
        }
        return list;
    }

    public List<Integer> removeGreater(StudyGroup studyGroup) throws SQLException, NoStudyGroupWithSuchId {
        List<Map.Entry<Integer, String>> list = getIdAndOwnerList(SQL_GET_GREATER, studyGroup.getName());
        return commitAndGetIds(list);
    }

    public List<Integer> removeLower(StudyGroup studyGroup) throws SQLException, NoStudyGroupWithSuchId {
        List<Map.Entry<Integer, String>> list = getIdAndOwnerList(SQL_GET_LOWER, studyGroup.getName());
        return commitAndGetIds(list);
    }

    public List<Integer> removeAllByShouldBeExpelled(Integer shouldBeExpelled) throws SQLException, NoStudyGroupWithSuchId {
        List<Map.Entry<Integer, String>> list = getIdAndOwnerList(SQL_GET_ALL_BY_SHOULD_BE_EXPELLED, shouldBeExpelled);
        return commitAndGetIds(list);
    }

    private List<Integer> commitAndGetIds(List<Map.Entry<Integer, String>> list) throws SQLException, NoStudyGroupWithSuchId {
        connection.setAutoCommit(false);
        List<Integer> deletedIds = removeAndGetIds(list);
        connection.commit();
        connection.setAutoCommit(true);
        return deletedIds;
    }

    private List<Integer> removeAndGetIds(List<Map.Entry<Integer, String>> list) throws SQLException, NoStudyGroupWithSuchId {
        List<Integer> deletedIds = new ArrayList<>();
        for (Map.Entry<Integer, String> element: list) {
            try {
                boolean status = removeById(element.getKey(), element.getValue());
                if (status) deletedIds.add(element.getKey());
            } catch (AccessDeniedException ignored) {}
        }
        return deletedIds;
    }

    public boolean addStudyGroup(StudyGroup studyGroup, String owner) {
        try {
            PreparedStatement statement = connection.prepareStatement(SQL_ADD_STUDY_GROUP,
                    Statement.RETURN_GENERATED_KEYS);
            int i = prepareStatement(statement, studyGroup, true);
            statement.setString(i, owner);
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0)
                throw new SQLException("No rows affected");
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next())
                    studyGroup.setId(generatedKeys.getInt(STUDY_GROUP_ID));
                else
                    throw new SQLException("No ID obtained");
            }
            statement.close();
            return true;
        } catch (SQLException exception) {
            exception.printStackTrace();
            rollback();
        }

        return false;
    }

    public boolean updateStudyGroup(int id, StudyGroup studyGroup, String username) throws SQLException, AccessDeniedException, NoStudyGroupWithSuchId {
        if (!idExists(id)) throw new NoStudyGroupWithSuchId();
        if (!belongsToUser(id, username)) throw new AccessDeniedException();

        try {
            PreparedStatement statement = connection.prepareStatement(SQL_UPDATE_BY_ID);
            int i = prepareStatement(statement, studyGroup, false);
            statement.setInt(i, id);
            statement.executeUpdate();
            statement.close();
            return true;
        } catch (SQLException exception) {
            exception.printStackTrace();
            rollback();
        }

        return false;
    }

    private int prepareStatement(PreparedStatement statement, StudyGroup studyGroup, boolean changeDate) throws SQLException {
        Coordinates coordinates = studyGroup.getCoordinates();
        ZonedDateTime creationDate = studyGroup.getCreationDate();
        FormOfEducation formOfEducation = studyGroup.getFormOfEducation();
        Person admin = studyGroup.getGroupAdmin();
        Location location = admin.getLocation();

        int i = 1;
        statement.setString(i++, studyGroup.getName());
        statement.setInt(i++, coordinates.getX());
        statement.setLong(i++, coordinates.getY());
        if (changeDate)
            statement.setTimestamp(i++, Timestamp.from(creationDate.toInstant()));
        statement.setInt(i++, studyGroup.getStudentsCount());
        statement.setLong(i++, studyGroup.getExpelledStudents());
        statement.setInt(i++, studyGroup.getShouldBeExpelled());
        statement.setString(i++, formOfEducation.name());
        statement.setString(i++, admin.getName());
        statement.setLong(i++, admin.getWeight());
        statement.setString(i++, admin.getPassportID());
        statement.setInt(i++, location.getX());
        statement.setInt(i++, location.getY());
        statement.setString(i++, location.getName());

        return i;
    }

    private StudyGroup getStudyGroupFromResult(ResultSet resultSet) throws SQLException {
        return new StudyGroup(
                resultSet.getInt(STUDY_GROUP_ID),
                resultSet.getString(NAME),
                new Coordinates(
                        resultSet.getInt(COORDINATE_X),
                        resultSet.getLong(COORDINATE_Y)
                ),
                ZonedDateTime.ofInstant(
                        resultSet.getTimestamp(CREATION_DATE).toInstant(), ZoneId.of("UTC")),
                resultSet.getInt(STUDENTS_COUNT),
                resultSet.getLong(EXPELLED_STUDENTS),
                resultSet.getInt(SHOULD_BE_EXPELLED),
                FormOfEducation.valueOf(resultSet.getString(FORM_OF_EDUCATION)),
                new Person(
                        resultSet.getString(ADMIN_NAME),
                        resultSet.getLong(ADMIN_WEIGHT),
                        resultSet.getString(ADMIN_PASSPORT),
                        new Location(
                                resultSet.getInt(ADMIN_LOCATION_X),
                                resultSet.getInt(ADMIN_LOCATION_Y),
                                resultSet.getString(ADMIN_LOCATION_NAME)
                        )
                )
        );
    }

    public TreeSet<StudyGroup> readCollection() {
        TreeSet<StudyGroup> collection = new TreeSet<>();
        try {
            PreparedStatement statement = connection.prepareStatement(SQL_GET_STUDY_GROUPS);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                StudyGroup studyGroup = getStudyGroupFromResult(resultSet);
                collection.add(studyGroup);
            }

            statement.close();
            System.out.println("Коллекция успешно загружена из базы данных. Размер: " + collection.size());
        } catch (SQLException exception) {
            System.out.println("Произошла ошибка при загрузе коллекции из базы данных. Заврешение работы.");
            exception.printStackTrace();
            System.exit(-1);
        }
        return collection;
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
