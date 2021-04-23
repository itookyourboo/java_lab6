package common.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Objects;

/**
 * Coordinates data class
 */

@XmlRootElement(name="Coordinates")
@XmlAccessorType(XmlAccessType.FIELD)
public class Coordinates implements Serializable {
    private static final long serialVersionUID = 0xABBA;
    private Integer x; //Значение поля должно быть больше -512, Поле не может быть null
    private long y;

    public Coordinates() {}

    /**
     * @param x - X coordinate
     * @param y - Y coordinate
     */
    public Coordinates(Integer x, long y) {
        this.x = x;
        this.y = y;
    }

    /**
     * @return X coordinate
     */
    public Integer getX() {
        return x;
    }

    /**
     * @return Y coordinate
     */
    public long getY() {
        return y;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public void setY(long y) {
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinates that = (Coordinates) o;
        return y == that.y &&
                Objects.equals(x, that.x);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "Coordinates{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}