package common.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import common.xml.ZonedDateTimeXmlAdapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.IOException;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * StudyGroup data class
 */
@XmlRootElement(name="StudyGroup")
@XmlAccessorType(XmlAccessType.FIELD)
public class StudyGroup implements Comparable<StudyGroup>, Serializable {
    private static final long serialVersionUID = 0xDEAD2;

    private Integer id; //Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    @XmlJavaTypeAdapter(ZonedDateTimeXmlAdapter.class)
    private ZonedDateTime creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private Integer studentsCount; //Значение поля должно быть больше 0, Поле не может быть null
    private Long expelledStudents; //Значение поля должно быть больше 0, Поле может быть null
    private Integer shouldBeExpelled; //Значение поля должно быть больше 0, Поле может быть null
    private FormOfEducation formOfEducation; //Поле не может быть null
    private Person groupAdmin; //Поле не может быть null
    private String owner;

    /**
     * @param id - study group ID
     * @param name - study group name
     * @param coordinates - study group coordinates object
     * @param creationDate - ZonedDateTime object of creation date
     * @param studentsCount - students count in the group
     * @param expelledStudents - expelled students count in the group
     * @param shouldBeExpelled - should be expelled students count in the group
     * @param formOfEducation - form of group education
     * @param groupAdmin - admin in the group
     */
    public StudyGroup(Integer id, String name, Coordinates coordinates, ZonedDateTime creationDate, Integer studentsCount, Long expelledStudents, Integer shouldBeExpelled, FormOfEducation formOfEducation, Person groupAdmin) {
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.studentsCount = studentsCount;
        this.expelledStudents = expelledStudents;
        this.shouldBeExpelled = shouldBeExpelled;
        this.formOfEducation = formOfEducation;
        this.groupAdmin = groupAdmin;
    }

    public StudyGroup(Integer id, String name, Coordinates coordinates, ZonedDateTime creationDate, Integer studentsCount, Long expelledStudents, Integer shouldBeExpelled, FormOfEducation formOfEducation, Person groupAdmin, String owner) {
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.studentsCount = studentsCount;
        this.expelledStudents = expelledStudents;
        this.shouldBeExpelled = shouldBeExpelled;
        this.formOfEducation = formOfEducation;
        this.groupAdmin = groupAdmin;
        this.owner = owner;
    }

    public StudyGroup(String name, Coordinates coordinates, ZonedDateTime creationDate, Integer studentsCount, Long expelledStudents, Integer shouldBeExpelled, FormOfEducation formOfEducation, Person groupAdmin) {
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.studentsCount = studentsCount;
        this.expelledStudents = expelledStudents;
        this.shouldBeExpelled = shouldBeExpelled;
        this.formOfEducation = formOfEducation;
        this.groupAdmin = groupAdmin;
    }

    public StudyGroup() {
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public void setCreationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public void setStudentsCount(Integer studentsCount) {
        this.studentsCount = studentsCount;
    }

    public void setExpelledStudents(Long expelledStudents) {
        this.expelledStudents = expelledStudents;
    }

    public void setShouldBeExpelled(Integer shouldBeExpelled) {
        this.shouldBeExpelled = shouldBeExpelled;
    }

    public void setFormOfEducation(FormOfEducation formOfEducation) {
        this.formOfEducation = formOfEducation;
    }

    public void setGroupAdmin(Person groupAdmin) {
        this.groupAdmin = groupAdmin;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public ZonedDateTime getCreationDate() {
        return creationDate;
    }

    public Integer getStudentsCount() {
        return studentsCount;
    }

    public Long getExpelledStudents() {
        return expelledStudents;
    }

    public Integer getShouldBeExpelled() {
        return shouldBeExpelled;
    }

    public FormOfEducation getFormOfEducation() {
        return formOfEducation;
    }

    public Person getGroupAdmin() {
        return groupAdmin;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getOwner() {
        return owner;
    }

    public void update(StudyGroup studyGroup) {
        this.name = studyGroup.name;
        this.coordinates = studyGroup.coordinates;
        this.studentsCount = studyGroup.studentsCount;
        this.expelledStudents = studyGroup.expelledStudents;
        this.shouldBeExpelled = studyGroup.shouldBeExpelled;
        this.formOfEducation = studyGroup.formOfEducation;
        this.groupAdmin = studyGroup.groupAdmin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StudyGroup studyGroup = (StudyGroup) o;
        return Objects.equals(id, studyGroup.id) && similar(studyGroup);
    }


    /**
     * Like equals but id doesn't matter
     * @param studyGroup - StudyGroup object to compare
     * @return true if all fields of objects are equal
     */
    public boolean similar(StudyGroup studyGroup) {
        return Objects.equals(name, studyGroup.name) &&
                Objects.equals(coordinates, studyGroup.coordinates) &&
                Objects.equals(creationDate, studyGroup.creationDate) &&
                Objects.equals(studentsCount, studyGroup.studentsCount) &&
                Objects.equals(expelledStudents, studyGroup.expelledStudents) &&
                Objects.equals(shouldBeExpelled, studyGroup.shouldBeExpelled) &&
                formOfEducation == studyGroup.formOfEducation &&
                Objects.equals(groupAdmin, studyGroup.groupAdmin);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, coordinates, creationDate, studentsCount, expelledStudents, shouldBeExpelled, formOfEducation, groupAdmin);
    }

    @Override
    public String toString() {
        return new GsonBuilder()
                .registerTypeAdapter(ZonedDateTime.class, zonedDateTimeTypeAdapter)
                .enableComplexMapKeySerialization()
                .create().toJson(this);
    }

    private static final TypeAdapter<ZonedDateTime> zonedDateTimeTypeAdapter = new TypeAdapter<ZonedDateTime>() {
        @Override
        public void write(JsonWriter jsonWriter, ZonedDateTime zonedDateTime) throws IOException {
            jsonWriter.value(zonedDateTime.toString());
        }

        @Override
        public ZonedDateTime read(JsonReader jsonReader) throws IOException {
            return ZonedDateTime.parse(jsonReader.nextString());
        }
    };

    @Override
    public int compareTo(StudyGroup o) {
        return name.compareTo(o.name);
    }

    public static StudyGroup fromObjects(Object... objects) {
        return new StudyGroup(
                (String) objects[0],                    // GroupName
                new Coordinates(
                        (int) objects[1],               // X
                        (long) objects[2]               // Y
                ),
                ZonedDateTime.now(),                    // Date
                (int) objects[3],                       // StudentsCount
                (long) objects[4],                      // ExpelledStudents
                (int) objects[5],                       // ShouldBeExpelled
                (FormOfEducation) objects[6],           // FormOfEducation
                new Person(
                        (String) objects[7],            // AdminName
                        (long) objects[8],              // AdminWeight
                        (String) objects[9],            // PassportID
                        new Location(
                                (int) objects[10],      // X
                                (int) objects[11],      // Y
                                (String) objects[12]    // LocationName
                        )
                )
        );
    }

    public Object[] toObjectArguments() {
        return new Object[] {
                getName(),
                getCoordinates().getX(),
                getCoordinates().getY(),
                getStudentsCount(),
                getExpelledStudents(),
                getShouldBeExpelled(),
                getFormOfEducation(),
                getGroupAdmin().getName(),
                getGroupAdmin().getWeight(),
                getGroupAdmin().getPassportID(),
                getGroupAdmin().getLocation().getX(),
                getGroupAdmin().getLocation().getY(),
                getGroupAdmin().getLocation().getName(),
                getId()
        };
    }

    public Object[] toObjectArray() {
        return new Object[] {
                getId(),
                getName(),
                getCoordinates().getX(),
                getCoordinates().getY(),
                getCreationDate(),
                getStudentsCount(),
                getExpelledStudents(),
                getShouldBeExpelled(),
                getFormOfEducation(),
                getGroupAdmin().getName(),
                getGroupAdmin().getWeight(),
                getGroupAdmin().getPassportID(),
                getGroupAdmin().getLocation().getX(),
                getGroupAdmin().getLocation().getY(),
                getGroupAdmin().getLocation().getName()
        };
    }

    public static StudyGroup fromObjectArray(Object[] objects) {
        return new StudyGroup(
                (Integer) objects[0],
                (String) objects[1],
                new Coordinates(
                        (Integer) objects[2],
                        (Long) objects[3]
                ),
                (ZonedDateTime) objects[4],
                (Integer) objects[5],
                (Long) objects[6],
                (Integer) objects[7],
                (FormOfEducation) objects[8],
                new Person(
                        (String) objects[9],
                        (Long) objects[10],
                        (String) objects[11],
                        new Location(
                                (Integer) objects[12],
                                (Integer) objects[13],
                                (String) objects[14]
                        )
                )
        );
    }

    public static StudyGroup fromJson(String json) {
        return new GsonBuilder()
                .registerTypeAdapter(ZonedDateTime.class, zonedDateTimeTypeAdapter)
                .enableComplexMapKeySerialization()
                .create()
                .fromJson(json, StudyGroup.class);
    }

    public static void main(String[] args) {
        StudyGroup studyGroup = new StudyGroup(
                1,
                "sksd",
                new Coordinates(2, 2),
                ZonedDateTime.now(),
                21,
                12L,
                12,
                FormOfEducation.DISTANCE_EDUCATION,
                new Person(
                        "aksd",
                        21,
                        "laslads",
                        new Location(1, 2, "dksad")
                ));
        String json = studyGroup.toString();
        System.out.println(json);
        StudyGroup studyGroup1 = StudyGroup.fromJson(json);
        System.out.println(studyGroup.equals(studyGroup1));
    }
}