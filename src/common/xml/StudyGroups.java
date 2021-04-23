package common.xml;

import common.model.StudyGroup;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.TreeSet;

/**
 * Root object of XML file. Contains collection of StudyGroup objects
 */
@XmlRootElement(name = "StudyGroups")
@XmlAccessorType(XmlAccessType.FIELD)
public class StudyGroups {

    @XmlElement(name="StudyGroup")
    private TreeSet<StudyGroup> studyGroups = null;

    public StudyGroups() {
    }

    /**
     * @return collection of StudyGroup objects
     */
    public TreeSet<StudyGroup> getStudyGroups() {
        return studyGroups;
    }

    public void setStudyGroups(TreeSet<StudyGroup> studyGroups) {
        this.studyGroups = studyGroups;
    }
}