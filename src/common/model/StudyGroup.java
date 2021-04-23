package common.model;

import common.xml.ZonedDateTimeXmlAdapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * StudyGroup data class
 */
@XmlRootElement(name="StudyGroup")
@XmlAccessorType(XmlAccessType.FIELD)
public class StudyGroup implements Comparable<StudyGroup>, Serializable {
    private static final long serialVersionUID = 0xDEAD;

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
        return "StudyGroup{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", coordinates=" + coordinates.toString() +
                ", creationDate=" + creationDate.toString() +
                ", studentsCount=" + studentsCount +
                ", expelledStudents=" + expelledStudents +
                ", shouldBeExpelled=" + shouldBeExpelled +
                ", formOfEducation=" + formOfEducation.toString() +
                ", groupAdmin=" + groupAdmin.toString() +
                '}';
    }

    @Override
    public int compareTo(StudyGroup o) {
        return name.compareTo(o.name);
    }
}