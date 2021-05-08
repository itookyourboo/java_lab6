package client.gui;

import client.util.LocaleManager;
import common.model.StudyGroup;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.awt.*;
import java.util.List;


public class MainWindow extends JFrame {

    private final List<StudyGroup> studyGroups;

    public MainWindow(List<StudyGroup> studyGroups) {
        super(LocaleManager.getString("loginTitle"));
        this.studyGroups = studyGroups;
        initWindow();
        initComponents();
    }

    private void initWindow() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize().getSize();
        int width = dimension.width, height = dimension.height;
        int WINDOW_WIDTH = width / 2, WINDOW_HEIGHT = height / 2;
        setBounds(width / 2 - WINDOW_WIDTH / 2, height / 2 - WINDOW_HEIGHT / 2, WINDOW_WIDTH, WINDOW_HEIGHT);
//        setResizable(false);
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        Object[][] data = new Object[studyGroups.size()][];
        int i = 0;
        for (StudyGroup studyGroup: studyGroups) {
            data[i++] = studyGroupToObjectArray(studyGroup);
        }

        JTable jTable = new JTable(data, columns);
        jTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        jTable.setAutoCreateRowSorter(true);
        //jTable.setRowSorter(new TableRowSorter());
        add(jTable.getTableHeader(), BorderLayout.NORTH);
        add(jTable, BorderLayout.CENTER);
    }

    String[] columns = new String[] {
            "ID",
            "Name",
            "X",
            "Y",
            "Creation date",
            "Students count",
            "Expelled students",
            "Should be expelled",
            "Form of education",
            "Admin",
            "Admin's weight",
            "Admin's passport",
            "Admin's X",
            "Admin's Y",
            "Admin's location"
    };

    private Object[] studyGroupToObjectArray(StudyGroup studyGroup) {
        return new Object[] {
                studyGroup.getId(),
                studyGroup.getName(),
                studyGroup.getCoordinates().getX(),
                studyGroup.getCoordinates().getY(),
                studyGroup.getCreationDate(),
                studyGroup.getStudentsCount(),
                studyGroup.getExpelledStudents(),
                studyGroup.getShouldBeExpelled(),
                studyGroup.getFormOfEducation(),
                studyGroup.getGroupAdmin().getName(),
                studyGroup.getGroupAdmin().getWeight(),
                studyGroup.getGroupAdmin().getPassportID(),
                studyGroup.getGroupAdmin().getLocation().getX(),
                studyGroup.getGroupAdmin().getLocation().getY(),
                studyGroup.getGroupAdmin().getLocation().getName()
        };
    }

    class TableRowSorter extends RowSorter<TableModel> {

        @Override
        public TableModel getModel() {
            return null;
        }

        @Override
        public void toggleSortOrder(int column) {

        }

        @Override
        public int convertRowIndexToModel(int index) {
            return 0;
        }

        @Override
        public int convertRowIndexToView(int index) {
            return 0;
        }

        @Override
        public void setSortKeys(List<? extends SortKey> keys) {

        }

        @Override
        public List<? extends SortKey> getSortKeys() {
            return null;
        }

        @Override
        public int getViewRowCount() {
            return 0;
        }

        @Override
        public int getModelRowCount() {
            return 0;
        }

        @Override
        public void modelStructureChanged() {

        }

        @Override
        public void allRowsChanged() {

        }

        @Override
        public void rowsInserted(int firstRow, int endRow) {

        }

        @Override
        public void rowsDeleted(int firstRow, int endRow) {

        }

        @Override
        public void rowsUpdated(int firstRow, int endRow) {

        }

        @Override
        public void rowsUpdated(int firstRow, int endRow, int column) {

        }
    }
}