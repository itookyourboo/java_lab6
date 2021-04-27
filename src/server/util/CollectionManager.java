package server.util;

import common.Config;
import common.DataManager;
import common.exceptions.AccessDeniedException;
import common.exceptions.NoStudyGroupWithSuchId;
import common.model.Person;
import common.model.StudyGroup;
import common.net.CommandResult;
import common.net.Request;
import common.net.ResultStatus;

import java.sql.SQLException;
import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.NavigableSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class CollectionManager extends DataManager {
    private TreeSet<StudyGroup> studyGroupCollection =  new TreeSet<>();
    private ZonedDateTime lastInitTime;
    private ZonedDateTime lastSaveTime;
    private FileManager fileManager;
    private DatabaseManager databaseManager;

    private final Comparator<StudyGroup> sortByName = Comparator.comparing(StudyGroup::getName);

    public CollectionManager(FileManager fileManager) {
        this.lastInitTime = null;
        this.lastSaveTime = null;
        this.fileManager = fileManager;

        loadCollection();
    }

    public CollectionManager(DatabaseManager databaseManager) {
        this.lastInitTime = null;
        this.lastSaveTime = null;
        this.databaseManager = databaseManager;
        this.fileManager = new FileManager(Config.ENV_VAR);

        loadCollectionFromDB();
    }

    public void saveCollection() {
        fileManager.writeCollection(studyGroupCollection);
        lastSaveTime = ZonedDateTime.now();
    }

    /**
     * Loads the collection from file.
     */
    private void loadCollection() {
        studyGroupCollection = fileManager.readCollection();
        lastInitTime = ZonedDateTime.now();
    }

    private void loadCollectionFromDB() {
        studyGroupCollection = databaseManager.readCollection();
        lastInitTime = ZonedDateTime.now();
    }

    public NavigableSet<StudyGroup> getCollection() {
        return studyGroupCollection;
    }

    /**
     * @return Last initialization time or null if there wasn't initialization.
     */
    public ZonedDateTime getLastInitTime() {
        return lastInitTime;
    }

    /**
     * @return Last save time or null if there wasn't saving.
     */
    public ZonedDateTime getLastSaveTime() {
        return lastSaveTime;
    }

    /**
     * @return Name of the collection's type.
     */
    public String collectionType() {
        return studyGroupCollection.getClass().getName();
    }

    /**
     * @return Size of the collection.
     */
    public int collectionSize() {
        return studyGroupCollection.size();
    }

    /**
     * @return The first element of the collection or null if collection is empty.
     */
    public synchronized StudyGroup getFirst() {
        return studyGroupCollection.stream()
                .findFirst()
                .orElse(null);
    }

    /**
     * @return The last element of the collection or null if collection is empty.
     */
    public synchronized StudyGroup getLast() {
        if (studyGroupCollection.isEmpty()) return null;
        return studyGroupCollection.last();
    }

    /**
     * @param id ID of the studyGroup.
     * @return A studyGroup by his ID or null if studyGroup isn't found.
     */
    public synchronized StudyGroup getById(Integer id) {
        return studyGroupCollection.stream()
                .filter(studyGroup -> studyGroup.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    /**
     * @param studyGroupToFind A studyGroup who's value will be found.
     * @return A studyGroup by his value or null if studyGroup isn't found.
     */
    public synchronized StudyGroup getByValue(StudyGroup studyGroupToFind) {
        return studyGroupCollection.stream()
                .filter(studyGroup -> studyGroup.similar(studyGroupToFind))
                .findFirst()
                .orElse(null);
    }

    @Override
    protected synchronized Integer generateNextId() {
        return getLast().getId() + 1;
    }

    @Override
    public synchronized CommandResult add(Request<?> request) {
        try {
            StudyGroup studyGroup = (StudyGroup) request.entity;
            return addStudyGroup(studyGroup, request);
        } catch (SQLException exception) {
            return new CommandResult(ResultStatus.ERROR, "SQL-ошибка на сервере");
        } catch (Exception exception) {
            return new CommandResult(ResultStatus.ERROR, "Передан аргумент другого типа");
        }
    }

    @Override
    public synchronized CommandResult addIfMin(Request<?> request) {
        try {
            StudyGroup studyGroup = (StudyGroup) request.entity;
            String minStudyGroupName = databaseManager.getMinStudyGroupName();
            if (studyGroup.getName().compareTo(minStudyGroupName) < 0)
                return addStudyGroup(studyGroup, request);

            return new CommandResult(ResultStatus.OK, "Элемент не был добавлен, так как не минимальный.");
        } catch (SQLException exception) {
            return new CommandResult(ResultStatus.ERROR, "SQL-ошибка на сервере");
        } catch (Exception exception) {
            return new CommandResult(ResultStatus.ERROR, "Передан аргумент другого типа");
        }
    }

    private synchronized CommandResult addStudyGroup(StudyGroup studyGroup, Request<?> request) throws SQLException {
        boolean ok = databaseManager.addStudyGroup(studyGroup, request.user.getUsername());
        if (ok) {
            studyGroupCollection.add(studyGroup);
            return new CommandResult(ResultStatus.OK, "Новый элемент успешно добавлен");
        }
        return new CommandResult(ResultStatus.ERROR, "Не удалось добавить элемент");
    }

    @Override
    public synchronized CommandResult clear(Request<?> request) {
        studyGroupCollection.clear();
        return new CommandResult(ResultStatus.OK, "Элементы успешно удалены из коллекции");
    }

    @Override
    public synchronized CommandResult countByGroupAdmin(Request<?> request) {
        try {
            Person admin = (Person) request.entity;
            int num = (int) studyGroupCollection.stream()
                    .filter(studyGroup -> studyGroup.getGroupAdmin().equals(admin))
                    .count();
            return new CommandResult(ResultStatus.OK, "Групп с админом: " + num);
        } catch (Exception exception) {
            return new CommandResult(ResultStatus.ERROR, "Передан аргумент другого типа");
        }
    }

    @Override
    public synchronized CommandResult info(Request<?> request) {
        ZonedDateTime lastInitTime = getLastInitTime();
        String lastInitTimeString = (lastInitTime == null) ? "в данной сессии инициализации еще не происходило" :
                lastInitTime.toString();

        ZonedDateTime lastSaveTime = getLastSaveTime();
        String lastSaveTimeString = (lastSaveTime == null) ? "в данной сессии сохранения еще не происходило" :
                lastSaveTime.toString();

        String result = "" +
                " Тип: " + collectionType() + "\n" +
                " Количество элементов: " + collectionSize() + "\n" +
                " Дата последнего сохранения: " + lastSaveTimeString + "\n" +
                " Дата последней инициализации: " + lastInitTimeString;
        return new CommandResult(ResultStatus.OK, result);
    }

    @Override
    public synchronized CommandResult filterGreaterThanExpelledStudents(Request<?> request) {
        try {
            Long expelledStudents = (Long) request.entity;
            String result = studyGroupCollection.stream()
                    .filter(studyGroup -> studyGroup.getExpelledStudents() >= expelledStudents)
                    .sorted(sortByName)
                    .map(StudyGroup::toString)
                    .collect(Collectors.joining("\n"));
            return new CommandResult(ResultStatus.OK, result);
        } catch (Exception exception) {
            return new CommandResult(ResultStatus.ERROR, "Передан аргумент другого типа");
        }
    }

    @Override
    public synchronized CommandResult removeById(Request<?> request) {
        try {
            Integer id = (Integer) request.entity;
            if (studyGroupCollection.stream().filter(studyGroup -> studyGroup.getId().equals(id)).findFirst().orElse(null) == null)
                return new CommandResult(ResultStatus.ERROR, "Группы с таким ID не существует");

            boolean ok = databaseManager.removeById(id, request.user.getUsername());
            if (ok) {
                studyGroupCollection.removeIf(studyGroup -> studyGroup.getId().equals(id));
                return new CommandResult(ResultStatus.OK, "Группа успешно удалена");
            }

            return new CommandResult(ResultStatus.ERROR, "Не удалось удалить группу.");
        } catch (NoStudyGroupWithSuchId exception) {
            return new CommandResult(ResultStatus.ERROR, "Группы с таким ID не существует");
        } catch (SQLException exception) {
            return new CommandResult(ResultStatus.ERROR, "SQL-ошибка на сервере");
        } catch (AccessDeniedException exception) {
            return new CommandResult(ResultStatus.ERROR, "Недостаточно прав для удаления.");
        } catch (Exception exception) {
            return new CommandResult(ResultStatus.ERROR, "Передан аргумент другого типа");
        }
    }

    @Override
    public synchronized CommandResult removeAllByShouldBeExpelled(Request<?> request) {
        try {
            Integer shouldBeExpelled = (Integer) request.entity;
            return deleteElements(databaseManager.removeAllByShouldBeExpelled(shouldBeExpelled));
        } catch (SQLException exception) {
            return new CommandResult(ResultStatus.ERROR, "SQL-ошибка на сервере");
        } catch (Exception exception) {
            return new CommandResult(ResultStatus.ERROR, "Передан аргумент другого типа");
        }
    }

    @Override
    public synchronized CommandResult removeGreater(Request<?> request) {
        try {
            StudyGroup studyGroup = (StudyGroup) request.entity;
            return deleteElements(databaseManager.removeGreater(studyGroup));
        } catch (SQLException exception) {
            return new CommandResult(ResultStatus.ERROR, "SQL-ошибка на сервере");
        }catch (Exception exception) {
            return new CommandResult(ResultStatus.ERROR, "Передан аргумент другого типа");
        }
    }

    @Override
    public synchronized CommandResult removeLower(Request<?> request) {
        try {
            StudyGroup studyGroup = (StudyGroup) request.entity;
            return deleteElements(databaseManager.removeLower(studyGroup));
        } catch (SQLException exception) {
            return new CommandResult(ResultStatus.ERROR, "SQL-ошибка на сервере");
        }catch (Exception exception) {
            return new CommandResult(ResultStatus.ERROR, "Передан аргумент другого типа");
        }
    }

    private synchronized CommandResult deleteElements(List<Integer> deletedIds) {
        int last = studyGroupCollection.size();
        if (deletedIds.size() > 0) {
            deletedIds.forEach(id -> studyGroupCollection.removeIf(studyGroup -> studyGroup.getId().equals(id)));
            return new CommandResult(ResultStatus.OK, "Удалено групп: " + (last - studyGroupCollection.size()));
        }
        return new CommandResult(ResultStatus.OK, "Элементы не были удалены в виду отсутствия прав или отсутсвия таких групп.");
    }

    @Override
    public synchronized CommandResult update(Request<?> request) {
        try {
            StudyGroup studyGroup = (StudyGroup) request.entity;
            int id = studyGroup.getId();
            boolean ok = databaseManager.updateStudyGroup(id, studyGroup, request.user.getUsername());
            if (ok) {
                studyGroupCollection.stream()
                        .filter(studyGroup1 -> studyGroup1.getId().equals(id))
                        .forEach(studyGroup1 -> studyGroup1.update(studyGroup));
                return new CommandResult(ResultStatus.OK, "Группа успешно обновлена");
            }

            return new CommandResult(ResultStatus.ERROR, "Не удалось обновить группу");
        } catch (AccessDeniedException exception) {
            return new CommandResult(ResultStatus.ERROR, "Недостаточно прав для обновления.");
        } catch (NoStudyGroupWithSuchId exception) {
            return new CommandResult(ResultStatus.ERROR, "Группы с таким ID не существует");
        } catch (SQLException exception) {
            return new CommandResult(ResultStatus.ERROR, "SQL-ошибка на сервере");
        } catch (Exception exception) {
            return new CommandResult(ResultStatus.ERROR, "Передан аргумент другого типа");
        }
    }

    @Override
    public void save() {
        saveCollection();
    }

    @Override
    public CommandResult show(Request<?> request) {
        return new CommandResult(ResultStatus.OK, toString());
    }

    @Override
    public synchronized String toString() {
        if (studyGroupCollection.isEmpty()) return "Коллекция пуста!";
        return studyGroupCollection.stream()
                .sorted(sortByName)
                .map(StudyGroup::toString)
                .collect(Collectors.joining("\n"));
    }
}
