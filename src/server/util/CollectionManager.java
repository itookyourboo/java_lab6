package server.util;

import common.DataManager;
import common.model.Person;
import common.model.StudyGroup;
import common.net.CommandResult;
import common.net.Request;
import common.net.ResultStatus;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.NavigableSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class CollectionManager extends DataManager {
    private TreeSet<StudyGroup> studyGroupCollection =  new TreeSet<>();
    private ZonedDateTime lastInitTime;
    private ZonedDateTime lastSaveTime;
    private final FileManager fileManager;

    private final Comparator<StudyGroup> sortByName = Comparator.comparing(StudyGroup::getName);

    public CollectionManager(FileManager fileManager) {
        this.lastInitTime = null;
        this.lastSaveTime = null;
        this.fileManager = fileManager;

        loadCollection();
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
    public StudyGroup getFirst() {
        return studyGroupCollection.stream()
                .findFirst()
                .orElse(null);
    }

    /**
     * @return The last element of the collection or null if collection is empty.
     */
    public StudyGroup getLast() {
        if (studyGroupCollection.isEmpty()) return null;
        return studyGroupCollection.last();
    }

    /**
     * @param id ID of the studyGroup.
     * @return A studyGroup by his ID or null if studyGroup isn't found.
     */
    public StudyGroup getById(Integer id) {
        return studyGroupCollection.stream()
                .filter(studyGroup -> studyGroup.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    /**
     * @param studyGroupToFind A studyGroup who's value will be found.
     * @return A studyGroup by his value or null if studyGroup isn't found.
     */
    public StudyGroup getByValue(StudyGroup studyGroupToFind) {
        return studyGroupCollection.stream()
                .filter(studyGroup -> studyGroup.similar(studyGroupToFind))
                .findFirst()
                .orElse(null);
    }

    @Override
    protected Integer generateNextId() {
        return getLast().getId() + 1;
    }

    @Override
    public CommandResult add(Request<?> request) {
        try {
            StudyGroup studyGroup = (StudyGroup) request.entity;
            studyGroup.setId(generateNextId());
            studyGroupCollection.add(studyGroup);
            return new CommandResult(ResultStatus.OK, "Новый элемент успешно добавлен");
        } catch (Exception exception) {
            return new CommandResult(ResultStatus.ERROR, "Передан аргумент другого типа");
        }
    }

    @Override
    public CommandResult addIfMin(Request<?> request) {
        try {
            StudyGroup studyGroup = (StudyGroup) request.entity;
            studyGroup.setId(generateNextId());
            if (studyGroup.compareTo(getFirst()) < 0) {
                studyGroupCollection.add(studyGroup);
                return new CommandResult(ResultStatus.OK, "Новый элемент успешно добавлен");
            }
            return new CommandResult(ResultStatus.OK, "Элемент не минимальный");
        } catch (Exception exception) {
            return new CommandResult(ResultStatus.ERROR, "Передан аргумент другого типа");
        }
    }

    @Override
    public CommandResult clear(Request<?> request) {
        studyGroupCollection.clear();
        return new CommandResult(ResultStatus.OK, "Элементы успешно удалены из коллекции");
    }

    @Override
    public CommandResult countByGroupAdmin(Request<?> request) {
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
    public CommandResult info(Request<?> request) {
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
    public CommandResult filterGreaterThanExpelledStudents(Request<?> request) {
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
    public CommandResult removeAllByShouldBeExpelled(Request<?> request) {
        try {
            Integer shouldBeExpelled = (Integer) request.entity;
            int last = studyGroupCollection.size();
            studyGroupCollection.removeIf(studyGroup -> studyGroup.getShouldBeExpelled().equals(shouldBeExpelled));
            return new CommandResult(ResultStatus.ERROR, "Удалено " + (last - studyGroupCollection.size()) + " групп");
        } catch (Exception exception) {
            return new CommandResult(ResultStatus.ERROR, "Передан аргумент другого типа");
        }
    }

    @Override
    public CommandResult removeById(Request<?> request) {
        try {
            Integer id = (Integer) request.entity;
            if (studyGroupCollection.stream().noneMatch(studyGroup -> studyGroup.getId().equals(id)))
                return new CommandResult(ResultStatus.ERROR, "Группы с таким ID не существует");
            studyGroupCollection.removeIf(studyGroup -> studyGroup.getId().equals(id));
            return new CommandResult(ResultStatus.OK, "Группа успешно удалена");
        } catch (Exception exception) {
            return new CommandResult(ResultStatus.ERROR, "Передан аргумент другого типа");
        }
    }

    @Override
    public CommandResult removeGreater(Request<?> request) {
        try {
            StudyGroup studyGroup = (StudyGroup) request.entity;
            int last = studyGroupCollection.size();
            studyGroupCollection.removeIf(studyGroup1 -> studyGroup1.compareTo(studyGroup) > 0);
            return new CommandResult(ResultStatus.OK, "Удалено групп: " + (last - studyGroupCollection.size()));
        } catch (Exception exception) {
            return new CommandResult(ResultStatus.ERROR, "Передан аргумент другого типа");
        }
    }

    @Override
    public CommandResult removeLower(Request<?> request) {
        try {
            StudyGroup studyGroup = (StudyGroup) request.entity;
            int last = studyGroupCollection.size();
            studyGroupCollection.removeIf(studyGroup1 -> studyGroup1.compareTo(studyGroup) < 0);
            return new CommandResult(ResultStatus.OK, "Удалено групп: " + (last - studyGroupCollection.size()));
        } catch (Exception exception) {
            return new CommandResult(ResultStatus.ERROR, "Передан аргумент другого типа");
        }
    }

    @Override
    public CommandResult update(Request<?> request) {
        try {
            StudyGroup studyGroup = (StudyGroup) request.entity;
            if (getById(studyGroup.getId()) == null)
                return new CommandResult(ResultStatus.ERROR, "Группы с таким ID не существует");
            studyGroupCollection.stream()
                    .filter(studyGroup1 -> studyGroup1.getId().equals(studyGroup.getId()))
                    .forEach(studyGroup1 -> studyGroup1.update(studyGroup));
            return new CommandResult(ResultStatus.OK, "Группа успешно обновлена");
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
    public String toString() {
        if (studyGroupCollection.isEmpty()) return "Коллекция пуста!";
        return studyGroupCollection.stream()
                .sorted(sortByName)
                .map(StudyGroup::toString)
                .collect(Collectors.joining("\n"));
    }
}
