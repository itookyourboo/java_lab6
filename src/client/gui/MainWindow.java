package client.gui;

import client.util.LocaleManager;
import common.model.FormOfEducation;
import common.model.StudyGroup;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.ZonedDateTime;
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
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        Object[][] data = new Object[studyGroups.size()][];
        int i = 0;
        for (StudyGroup studyGroup: studyGroups) {
            data[i++] = studyGroup.toObjectArray();
        }

        JTable jTable = new JTable(data, columns);
        jTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        jTable.setAutoCreateRowSorter(true);
        jTable.setShowHorizontalLines(true);
        jTable.setModel(new DefaultTableModel(data, columns) {
            final Class<?>[] columnTypes = new Class<?>[] {
                    Integer.class, String.class, Integer.class, Long.class, ZonedDateTime.class, Integer.class, Long.class, Integer.class, FormOfEducation.class, String.class, Long.class, String.class, Integer.class, Integer.class, String.class
            };
            final boolean[] columnEditable = new boolean[] {
                    false, true, true, true, false, true, true, true, true, true, true, true, true, true, true
            };
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnTypes[columnIndex];
            }
            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return columnEditable[columnIndex];
            }
        });
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
}