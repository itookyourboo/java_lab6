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
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
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
            return new CommandResult(ResultStatus.ERROR, "sqlError");
        } catch (Exception exception) {
            return new CommandResult(ResultStatus.ERROR, "invalidArgument");
        }
    }

    @Override
    public synchronized CommandResult addIfMin(Request<?> request) {
        try {
            StudyGroup studyGroup = (StudyGroup) request.entity;
            String minStudyGroupName = databaseManager.getMinStudyGroupName();
            if (minStudyGroupName == null || studyGroup.getName().compareTo(minStudyGroupName) < 0)
                return addStudyGroup(studyGroup, request);

            return new CommandResult(ResultStatus.OK, "notMinElement");
        } catch (SQLException exception) {
            return new CommandResult(ResultStatus.ERROR, "sqlError");
        } catch (Exception exception) {
            return new CommandResult(ResultStatus.ERROR, "invalidArgument");
        }
    }

    private synchronized CommandResult addStudyGroup(StudyGroup studyGroup, Request<?> request) throws SQLException {
        boolean ok = databaseManager.addStudyGroup(studyGroup, request.user.getUsername());
        if (ok) {
            studyGroup.setOwner(request.user.getUsername());
            studyGroupCollection.add(studyGroup);
            return new CommandResult(ResultStatus.OK, "added");
        }
        return new CommandResult(ResultStatus.ERROR, "notAdded");
    }

    @Override
    public synchronized CommandResult clear(Request<?> request) {
        AtomicInteger i = new AtomicInteger();

        studyGroupCollection.forEach(studyGroup -> {
            try {
                if (databaseManager.removeById(studyGroup.getId(), request.user.getUsername()))
                    studyGroupCollection.remove(studyGroup);
                i.getAndIncrement();
            } catch (Exception ignored) {}
        });

        return new CommandResult(ResultStatus.OK, "_removed " + i);
    }

    @Override
    public synchronized CommandResult countByGroupAdmin(Request<?> request) {
        try {
            Person admin = (Person) request.entity;
            int num = (int) studyGroupCollection.stream()
                    .filter(studyGroup -> studyGroup.getGroupAdmin().equals(admin))
                    .count();
            return new CommandResult(ResultStatus.OK, "_foundByAdmin: " + num);
        } catch (Exception exception) {
            return new CommandResult(ResultStatus.ERROR, "invalidArgument");
        }
    }

    @Override
    public synchronized CommandResult info(Request<?> request) {
        ZonedDateTime lastInitTime = getLastInitTime();
        String lastInitTimeString = (lastInitTime == null) ? "_noInit" :
                lastInitTime.toString();

        ZonedDateTime lastSaveTime = getLastSaveTime();
        String lastSaveTimeString = (lastSaveTime == null) ? "_noSave" :
                lastSaveTime.toString();

        String result = "" +
                " _collectionType: " + collectionType() + "\n" +
                " _collectionSize: " + collectionSize() + "\n" +
                " _lastSave: " + lastSaveTimeString + "\n" +
                " _lastInit: " + lastInitTimeString;
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
            return new CommandResult(ResultStatus.ERROR, "invalidArgument");
        }
    }

    @Override
    public synchronized CommandResult removeById(Request<?> request) {
        try {
            Integer id = (Integer) request.entity;
            if (studyGroupCollection.stream().filter(studyGroup -> studyGroup.getId().equals(id)).findFirst().orElse(null) == null)
                return new CommandResult(ResultStatus.ERROR, "noSuchId");

            boolean ok = databaseManager.removeById(id, request.user.getUsername());
            if (ok) {
                studyGroupCollection.removeIf(studyGroup -> studyGroup.getId().equals(id));
                return new CommandResult(ResultStatus.OK, "removed");
            }

            return new CommandResult(ResultStatus.ERROR, "notRemoved");
        } catch (NoStudyGroupWithSuchId exception) {
            return new CommandResult(ResultStatus.ERROR, "noSuchId");
        } catch (SQLException exception) {
            return new CommandResult(ResultStatus.ERROR, "sqlError");
        } catch (AccessDeniedException exception) {
            return new CommandResult(ResultStatus.ERROR, "noAccess");
        } catch (Exception exception) {
            return new CommandResult(ResultStatus.ERROR, "invalidArgument");
        }
    }

    @Override
    public synchronized CommandResult removeAllByShouldBeExpelled(Request<?> request) {
        try {
            Integer shouldBeExpelled = (Integer) request.entity;
            return deleteElements(databaseManager.removeAllByShouldBeExpelled(shouldBeExpelled));
        } catch (SQLException exception) {
            return new CommandResult(ResultStatus.ERROR, "sqlError");
        } catch (Exception exception) {
            return new CommandResult(ResultStatus.ERROR, "invalidArgument");
        }
    }

    @Override
    public synchronized CommandResult removeGreater(Request<?> request) {
        try {
            StudyGroup studyGroup = (StudyGroup) request.entity;
            return deleteElements(databaseManager.removeGreater(studyGroup));
        } catch (SQLException exception) {
            return new CommandResult(ResultStatus.ERROR, "sqlError");
        }catch (Exception exception) {
            return new CommandResult(ResultStatus.ERROR, "invalidArgument");
        }
    }

    @Override
    public synchronized CommandResult removeLower(Request<?> request) {
        try {
            StudyGroup studyGroup = (StudyGroup) request.entity;
            return deleteElements(databaseManager.removeLower(studyGroup));
        } catch (SQLException exception) {
            return new CommandResult(ResultStatus.ERROR, "sqlError");
        }catch (Exception exception) {
            return new CommandResult(ResultStatus.ERROR, "invalidArgument");
        }
    }

    private synchronized CommandResult deleteElements(List<Integer> deletedIds) {
        int last = studyGroupCollection.size();
        if (deletedIds.size() > 0) {
            deletedIds.forEach(id -> studyGroupCollection.removeIf(studyGroup -> studyGroup.getId().equals(id)));
            return new CommandResult(ResultStatus.OK, "_removedNum: " + (last - studyGroupCollection.size()));
        }
        return new CommandResult(ResultStatus.OK, "noAccess");
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
                return new CommandResult(ResultStatus.OK, "updated");
            }

            return new CommandResult(ResultStatus.ERROR, "notUpdated");
        } catch (AccessDeniedException exception) {
            return new CommandResult(ResultStatus.ERROR, "noAccess");
        } catch (NoStudyGroupWithSuchId exception) {
            return new CommandResult(ResultStatus.ERROR, "noSuchId");
        } catch (SQLException exception) {
            return new CommandResult(ResultStatus.ERROR, "sqlError");
        } catch (Exception exception) {
            exception.printStackTrace();
            return new CommandResult(ResultStatus.ERROR, "invalidArgument");
        }
    }

    @Override
    public void save() {
        saveCollection();
    }

    @Override
    public synchronized CommandResult show(Request<?> request) {
        return new CommandResult(ResultStatus.OK, toString());
    }

    @Override
    public synchronized String toString() {
        if (studyGroupCollection.isEmpty()) return "";
        return studyGroupCollection.stream()
                .sorted(sortByName)
                .map(StudyGroup::toString)
                .collect(Collectors.joining("\n"));
    }
}
