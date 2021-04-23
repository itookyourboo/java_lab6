package common.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Objects;

/**
 * Person data class.
 */
@XmlRootElement(name="Person")
@XmlAccessorType(XmlAccessType.FIELD)
public class Person implements Serializable {
    private static final long serialVersionUID = 0xB0BA;
    private String name; //Поле не может быть null, Строка не может быть пустой
    private long weight; //Значение поля должно быть больше 0
    private String passportID; //Строка не может быть пустой, Длина строки должна быть не меньше 4, Поле может быть null
    private Location location; //Поле не может быть null

    /**
     * @param name - name of person
     * @param weight - weight of person
     * @param passportID - passport ID of person
     * @param location - location class object
     */
    public Person(String name, long weight, String passportID, Location location) {
        this.name = name;
        this.weight = weight;
        this.passportID = passportID;
        this.location = location;
    }

    public Person() {
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setWeight(long weight) {
        this.weight = weight;
    }

    public void setPassportID(String passportID) {
        this.passportID = passportID;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public long getWeight() {
        return weight;
    }

    public String getPassportID() {
        return passportID;
    }

    public Location getLocation() {
        return location;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return weight == person.weight &&
                Objects.equals(name, person.name) &&
                Objects.equals(passportID, person.passportID) &&
                Objects.equals(location, person.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, weight, passportID, location);
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", weight=" + weight +
                ", passportID='" + passportID + '\'' +
                ", location=" + location +
                '}';
    }
}