package client.gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import client.Client;
import client.UIController;
import client.commands.*;
import client.impl.Updatable;
import client.impl.Validatable;
import client.util.ConsoleOutputStream;
import client.util.LocaleManager;
import common.model.FormOfEducation;
import common.model.StudyGroup;
import common.net.CommandResult;
import common.net.ResultStatus;
import net.miginfocom.swing.*;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.*;
import java.util.List;
import java.util.regex.PatternSyntaxException;
import java.util.stream.Collectors;

/**
 * @author unknown
 */
public class TableWindow extends JPanel implements CustomWindow, Updatable {

    private Client client;
    private volatile List<StudyGroup> list;

    public TableWindow(Client client) {
        this.client = client;
        initComponents();
        loadData();
        localize(LocaleManager.getLanguage());
    }

    @Override
    public synchronized boolean checkForUpdate() {
        CommandResult result = new ShowCommand(client.getRequestSender())
                .executeWithObjectArgument();

        List<StudyGroup> updated;
        if (result.status == ResultStatus.OK && !result.message.trim().isEmpty()) {
            updated = Arrays.stream(result.message.split("\n"))
                    .map(StudyGroup::fromJson)
                    .collect(Collectors.toList());
        } else if (result.status != ResultStatus.ERROR) {
            updated = new ArrayList<>();
        } else {
            updated = list;
        }

        if (!updated.equals(list)) {
            System.out.println("LOAD DATA");
            return true;
        }

        return false;
    }

    @Override
    public synchronized void loadData() {
        CommandResult result = new ShowCommand(client.getRequestSender())
                .executeWithObjectArgument();

        if (result.status == ResultStatus.OK && !result.message.trim().isEmpty()) {
            list = Arrays.stream(result.message.split("\n"))
                    .map(StudyGroup::fromJson)
                    .collect(Collectors.toList());
        } else {
            list = new ArrayList<>();
        }

        ((DefaultTableModel) table.getModel()).setRowCount(0);
        list.forEach(this::addStudyGroupToTable);
    }

    private void addStudyGroupToTable(StudyGroup studyGroup) {
        Object[] row = studyGroup.toObjectArray();
        row[4] = getLocalizedDateTime(studyGroup.getCreationDate());
        ((DefaultTableModel) table.getModel()).addRow(row);
    }

    private String getLocalizedDateTime(ZonedDateTime zonedDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL).withLocale(LocaleManager.getLocale());
        return formatter.format(zonedDateTime);
    }

    @Override
    public void localize(LocaleManager.Lang lang) {
        LocaleManager.setLanguage(lang);

        UIController.setTitle(LocaleManager.getString("tableWindow") + " [" + client.getRequestSender().getUser().getUsername() + "]");
        updateLocaleBox(lang);
        addButton.setText(LocaleManager.getString("addCommand"));
        logOutButton.setText(LocaleManager.getString("logOut"));
        addIfMinButton.setText(LocaleManager.getString("addIfMinCommand"));
        localeLabel.setText(LocaleManager.getString("locale"));
        clearButton.setText(LocaleManager.getString("clearCommand"));
        countButton.setText(LocaleManager.getString("countByGroupAdminCommand"));
        columnLabel.setText(LocaleManager.getString("column"));
        executeScriptButton.setText(LocaleManager.getString("executeScriptCommand"));
        criterionLabel.setText(LocaleManager.getString("criterion"));
        helpButton.setText(LocaleManager.getString("helpCommand"));
        valueLabel.setText(LocaleManager.getString("value"));
        infoButton.setText(LocaleManager.getString("infoCommand"));
        removeByIdButton.setText(LocaleManager.getString("removeByIdCommand"));
        removeGreaterButton.setText(LocaleManager.getString("removeGreaterCommand"));
        removeLowerButton.setText(LocaleManager.getString("removeLowerCommand"));
        updateButton.setText(LocaleManager.getString("updateCommand"));
        visualizeButton.setText(LocaleManager.getString("visualizeCommand"));

        for (int i = 0; i < table.getModel().getRowCount(); i++)
            table.getModel().setValueAt(getLocalizedDateTime(list.get(i).getCreationDate()), i, 4);
    }

    private void updateLocaleBox(LocaleManager.Lang lang) {
        int i = 0;
        for (LocaleManager.Lang l: LocaleManager.Lang.values()) {
            if (l.getName().equals(lang.getName())) {
                localeBox.setSelectedIndex(i);
                break;
            }
            i++;
        }
    }

    @Override
    public void clearFields() {
        JTextField[] fields = new JTextField[] {filterField};
        for (JTextField field: fields) field.setText("");
    }

    private void logOutButtonMouseReleased(MouseEvent e) {
        UIController.switchWindow(UIController.getFrame(), new LoginWindow(client));
    }

    private void localeBoxItemStateChanged(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED) {
            String item = (String) e.getItem();
            LocaleManager.Lang lang = LocaleManager.langMap.get(item);
            localize(lang);
        }
    }

    private void columnBoxItemStateChanged(ItemEvent e) {
        updateFilter();
    }

    private void criterionBoxItemStateChanged(ItemEvent e) {
        updateFilter();
    }

    private void updateFilter() {
        if (sorter == null) return;

        String textToFilter = filterField.getText();
        int index = columnBox.getSelectedIndex();
        String filterOperationValue = (String) criterionBox.getSelectedItem();
        if (textToFilter.isEmpty()) sorter.setRowFilter(null);
        else {
            try {
                // sorter.setRowFilter(RowFilter.regexFilter(textToFilter, index));
                sorter.setRowFilter(chooseFilter(index, filterOperationValue));
            } catch (PatternSyntaxException exception) {
                exception.printStackTrace();
            }
        }
    }

    private void visualizeButtonMouseReleased(MouseEvent e) {
        VisualWindow visualWindow = new VisualWindow(client);
        UIController.switchWindow(UIController.getFrame(), visualWindow);
    }

    private void addButtonMouseReleased(MouseEvent e) {
        executeAddAndRemoveGreaterLowerCommand(new AddCommand(client.getRequestSender()));
    }

    private void addIfMinButtonMouseReleased(MouseEvent e) {
        executeAddAndRemoveGreaterLowerCommand(new AddIfMinCommand(client.getRequestSender()));
    }

    private void clearButtonMouseReleased(MouseEvent e) {
        int input = JOptionPane.showConfirmDialog(this, LocaleManager.getString("sure"),
                LocaleManager.getString("clearCommand"), JOptionPane.YES_NO_OPTION);
        if (input == 0) {
            CommandResult result = new ClearCommand(client.getRequestSender())
                    .executeWithObjectArgument();
            loadData();
            showDialog(this, result.message
                    .replace("removed", LocaleManager.getString("removed"))
                    .replace("notRemoved", LocaleManager.getString("notRemoved")));
        }
    }

    private void countButtonMouseReleased(MouseEvent e) {
        PersonWindow personWindow = new PersonWindow(client);
        UIController.switchWindow(UIController.getFrame(), personWindow);
        personWindow.setOnPersonChangeListener(person -> {
            if (person == null) {
                UIController.switchWindow(UIController.getFrame(), TableWindow.this);
                return;
            }

            CommandResult result = new CountByGroupAdminCommand(client.getRequestSender()).executeWithObjectArgument(
                    person.getName(), person.getWeight(), person.getPassportID(), person.getLocation().getY(),
                    person.getLocation().getY(), person.getLocation().getName()
            );
            UIController.switchWindow(UIController.getFrame(), TableWindow.this);
            String message = result.message
                    .replace("_foundByAdmin", LocaleManager.getString("foundByAdmin"));
            showDialog(TableWindow.this, message);
        });
    }

    private void executeScriptButtonMouseReleased(MouseEvent e) {
        final JFileChooser chooser = new JFileChooser();
        int returnVal = chooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            String path = chooser.getSelectedFile().getAbsolutePath();
            ConsoleWindow consoleWindow = new ConsoleWindow(client, this);
            UIController.switchWindow(UIController.getFrame(), consoleWindow);
            consoleWindow.validate();
            new ExecuteScriptCommand(client.getCommandManager(), client.getRequestSender())
                    .executeWithObjectArgument(path, new ConsoleOutputStream(consoleWindow.getTextArea()));
        }
    }

    private void helpButtonMouseReleased(MouseEvent e) {
        showDialog(this, LocaleManager.getString("helpMessage"));
    }

    private void infoButtonMouseReleased(MouseEvent e) {
        CommandResult result = new InfoCommand(client.getRequestSender()).executeWithObjectArgument();
        String message = result.message
                .replace("_collectionType", LocaleManager.getString("collectionType"))
                .replace("_collectionSize", LocaleManager.getString("collectionSize"))
                .replace("_lastSave", LocaleManager.getString("lastSave"))
                .replace("_lastInit", LocaleManager.getString("lastInit"))
                .replace("_noSave", LocaleManager.getString("noSave"))
                .replace("_noInit", LocaleManager.getString("noInit"));
        showDialog(this, message);
    }

    private void removeByIdButtonMouseReleased(MouseEvent e) {
        String input = JOptionPane.showInputDialog(this, LocaleManager.getString("removeByIdCommand"));
        try {
            int id = Integer.parseInt(input);
            CommandResult result = new RemoveByIdCommand(client.getRequestSender()).executeWithObjectArgument(id);
            showDialog(this, result.message
                .replace("noSuchId", LocaleManager.getString("noSuchId"))
                .replace("removed", LocaleManager.getString("removed"))
                .replace("notRemoved", LocaleManager.getString("notRemoved")
                .replace("noAccess", LocaleManager.getString("noAccess"))));
            loadData();
        } catch (NumberFormatException numberFormatException) {
            showDialog(this, LocaleManager.getString("numberFormatException"));
        }
    }

    private void removeGreaterButtonMouseReleased(MouseEvent e) {
        executeAddAndRemoveGreaterLowerCommand(new RemoveGreaterCommand(client.getRequestSender()));
    }

    private void removeLowerButtonMouseReleased(MouseEvent e) {
        executeAddAndRemoveGreaterLowerCommand(new RemoveLowerCommand(client.getRequestSender()));
    }

    private void updateButtonMouseReleased(MouseEvent e) {
        StudyGroupWindow studyGroupWindow = new StudyGroupWindow(client, true);
        studyGroupWindow.setData(list);
        UIController.switchWindow(UIController.getFrame(), studyGroupWindow);
        studyGroupWindow.setOnStudyGroupChangeListener(studyGroup -> {
            if (studyGroup == null) {
                UIController.switchWindow(UIController.getFrame(), TableWindow.this);
                return;
            }

            CommandResult result = new UpdateCommand(client.getRequestSender()).executeWithObjectArgument(studyGroup.toObjectArguments());
            if (result.status == ResultStatus.OK) {
                UIController.switchWindow(UIController.getFrame(), TableWindow.this);
                loadData();
                showDialog(TableWindow.this, result.message);
            } else {
                showDialog(studyGroupWindow, result.message
                    .replace("noAccess", LocaleManager.getString("noAccess")));
            }
        });
    }

    private void executeAddAndRemoveGreaterLowerCommand(Command command) {
        StudyGroupWindow studyGroupWindow = new StudyGroupWindow(client, false);
        UIController.switchWindow(UIController.getFrame(), studyGroupWindow);
        studyGroupWindow.setOnStudyGroupChangeListener(studyGroup -> {
            if (studyGroup == null) {
                UIController.switchWindow(UIController.getFrame(), TableWindow.this);
                return;
            }

            if (command instanceof AddCommand)
                studyGroup.setOwner(client.getRequestSender().getUser().getUsername());

            CommandResult result = command.executeWithObjectArgument(studyGroup.toObjectArguments());
            UIController.switchWindow(UIController.getFrame(), TableWindow.this);
            loadData();
            showDialog(TableWindow.this, result.message);
        });
    }
    
    private void cancelFilterButtonMouseReleased(MouseEvent e) {
        // TODO add your code here
    }

    private void cancelButtonMouseReleased(MouseEvent e) {
        // TODO add your code here
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - unknown
        addButton = new JButton();
        scrollPane1 = new JScrollPane();
        table = new JTable();
        logOutButton = new JButton();
        addIfMinButton = new JButton();
        localeLabel = new JLabel();
        localeBox = new JComboBox();
        clearButton = new JButton();
        countButton = new JButton();
        columnLabel = new JLabel();
        columnBox = new JComboBox();
        executeScriptButton = new JButton();
        criterionLabel = new JLabel();
        criterionBox = new JComboBox();
        helpButton = new JButton();
        valueLabel = new JLabel();
        filterField = new JTextField();
        infoButton = new JButton();
        removeByIdButton = new JButton();
        removeGreaterButton = new JButton();
        removeLowerButton = new JButton();
        updateButton = new JButton();
        visualizeButton = new JButton();

        //======== this ========
        setBorder (new javax. swing. border. CompoundBorder( new javax .swing .border .TitledBorder (new javax
        . swing. border. EmptyBorder( 0, 0, 0, 0) , "JF\u006frmDes\u0069gner \u0045valua\u0074ion", javax. swing
        . border. TitledBorder. CENTER, javax. swing. border. TitledBorder. BOTTOM, new java .awt .
        Font ("D\u0069alog" ,java .awt .Font .BOLD ,12 ), java. awt. Color. red
        ) , getBorder( )) );  addPropertyChangeListener (new java. beans. PropertyChangeListener( ){ @Override
        public void propertyChange (java .beans .PropertyChangeEvent e) {if ("\u0062order" .equals (e .getPropertyName (
        ) )) throw new RuntimeException( ); }} );
        setLayout(new MigLayout(
            "fill,hidemode 3,align center top",
            // columns
            "[151,fill]" +
            "[700,grow,fill]" +
            "[fill]" +
            "[fill]",
            // rows
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]"));

        //---- addButton ----
        addButton.setText("Add");
        addButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                addButtonMouseReleased(e);
            }
        });
        add(addButton, "cell 0 0");

        //======== scrollPane1 ========
        {
            scrollPane1.setPreferredSize(null);

            //---- table ----
            table.setAutoCreateRowSorter(true);
            table.setFillsViewportHeight(true);
            scrollPane1.setViewportView(table);
        }
        add(scrollPane1, "cell 1 0 1 13,aligny top,growy");

        //---- logOutButton ----
        logOutButton.setText("Log out");
        logOutButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                logOutButtonMouseReleased(e);
            }
        });
        add(logOutButton, "cell 2 0 2 1");

        //---- addIfMinButton ----
        addIfMinButton.setText("Add if min");
        addIfMinButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                addIfMinButtonMouseReleased(e);
            }
        });
        add(addIfMinButton, "cell 0 1");

        //---- localeLabel ----
        localeLabel.setText("Locale");
        localeLabel.setHorizontalTextPosition(SwingConstants.RIGHT);
        add(localeLabel, "cell 2 1");

        //---- localeBox ----
        localeBox.addItemListener(e -> localeBoxItemStateChanged(e));
        add(localeBox, "cell 3 1");

        //---- clearButton ----
        clearButton.setText("Clear");
        clearButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                clearButtonMouseReleased(e);
            }
        });
        add(clearButton, "cell 0 2");

        //---- countButton ----
        countButton.setText("Count by group admin");
        countButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                countButtonMouseReleased(e);
            }
        });
        add(countButton, "cell 0 3");

        //---- columnLabel ----
        columnLabel.setText("Column");
        columnLabel.setHorizontalTextPosition(SwingConstants.RIGHT);
        add(columnLabel, "cell 2 3");

        //---- columnBox ----
        columnBox.addItemListener(e -> columnBoxItemStateChanged(e));
        add(columnBox, "cell 3 3");

        //---- executeScriptButton ----
        executeScriptButton.setText("Execute script");
        executeScriptButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                executeScriptButtonMouseReleased(e);
            }
        });
        add(executeScriptButton, "cell 0 4");

        //---- criterionLabel ----
        criterionLabel.setText("Criterion");
        criterionLabel.setHorizontalTextPosition(SwingConstants.RIGHT);
        add(criterionLabel, "cell 2 4");

        //---- criterionBox ----
        criterionBox.addItemListener(e -> criterionBoxItemStateChanged(e));
        add(criterionBox, "cell 3 4");

        //---- helpButton ----
        helpButton.setText("Help");
        helpButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                helpButtonMouseReleased(e);
            }
        });
        add(helpButton, "cell 0 5");

        //---- valueLabel ----
        valueLabel.setText("Value");
        valueLabel.setHorizontalTextPosition(SwingConstants.RIGHT);
        add(valueLabel, "cell 2 5");
        add(filterField, "cell 3 5");

        //---- infoButton ----
        infoButton.setText("Info");
        infoButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                infoButtonMouseReleased(e);
            }
        });
        add(infoButton, "cell 0 6");

        //---- removeByIdButton ----
        removeByIdButton.setText("Remove by ID");
        removeByIdButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                removeByIdButtonMouseReleased(e);
            }
        });
        add(removeByIdButton, "cell 0 7");

        //---- removeGreaterButton ----
        removeGreaterButton.setText("Remove greater");
        removeGreaterButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                removeGreaterButtonMouseReleased(e);
            }
        });
        add(removeGreaterButton, "cell 0 8");

        //---- removeLowerButton ----
        removeLowerButton.setText("Remove lower");
        removeLowerButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                removeLowerButtonMouseReleased(e);
            }
        });
        add(removeLowerButton, "cell 0 9");

        //---- updateButton ----
        updateButton.setText("Update");
        updateButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                updateButtonMouseReleased(e);
            }
        });
        add(updateButton, "cell 0 10");

        //---- visualizeButton ----
        visualizeButton.setText("Visualization");
        visualizeButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                visualizeButtonMouseReleased(e);
            }
        });
        add(visualizeButton, "cell 1 13");
        // JFormDesigner - End of component initialization  //GEN-END:initComponents

        localeBox.setModel(new DefaultComboBoxModel(LocaleManager.langMap.keySet().toArray(new String[0])));
        columnBox.setModel(new DefaultComboBoxModel(columns));
        criterionBox.setModel(new DefaultComboBoxModel(new String[] {">", "=", "<"}));
        table.setModel(new DefaultTableModel(new Object[][] {}, columns) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnTypes[columnIndex];
            }

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return columnEditable[columnIndex];
            }

            @Override
            public void setValueAt(Object aValue, int row, int column) {
                if (String.valueOf(aValue).isEmpty()) return;
                Validatable validatable = columnValidatable[column];
                if (validatable == null) {
                    super.setValueAt(aValue, row, column);
                    return;
                }

                FieldValidator.ValidationResult<?> validationResult = validatable.validate(String.valueOf(aValue));
                if (!validationResult.isCorrect) {
                    showDialog(TableWindow.this, validationResult.message);
                } else
                    if (sendUpdateCommand(aValue, row, column))
                        super.setValueAt(aValue, row, column);
            }
        });

        initFilterTextListener();
    }

    TableRowSorter<DefaultTableModel> sorter;

    public void initFilterTextListener() {
        DefaultTableModel model = (DefaultTableModel) table.getModel();

        sorter = new TableRowSorter<DefaultTableModel>(model);
        initSortRules(sorter);
        table.setRowSorter(sorter);

        filterField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                changedUpdate(e);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                changedUpdate(e);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateFilter();
            }
        });
    }

    private RowFilter chooseFilter(int index, String filterOperationValue) {
        RowFilter.ComparisonType type = null;
        if (filterOperationValue.equals("=")) type = RowFilter.ComparisonType.EQUAL;
        if (filterOperationValue.equals(">")) type = RowFilter.ComparisonType.AFTER;
        if (filterOperationValue.equals("<")) type = RowFilter.ComparisonType.BEFORE;

        for (int i = 0; i < columnTypes.length; i++) {
            if (i == index && columnTypes[i].equals(Integer.class) || columnTypes[i].equals(Long.class)) {
                try {
                    return RowFilter.numberFilter(type, Long.parseLong(filterField.getText()), index);
                } catch (NumberFormatException e) {
                    return null;
                }
            } else {
                try {
                    if (type == RowFilter.ComparisonType.EQUAL)
                        return RowFilter.regexFilter("^" + filterField.getText() + "$", index);
                    if (type == RowFilter.ComparisonType.AFTER)
                        return RowFilter.regexFilter("[" + filterField.getText().toLowerCase(Locale.ROOT).charAt(0) + "-z]+", index);
                    if (type == RowFilter.ComparisonType.BEFORE)
                        return RowFilter.regexFilter("[a-" + filterField.getText().toLowerCase(Locale.ROOT).charAt(0) + "]+", index);
                } catch (PatternSyntaxException exception) {
                    return null;
                }
            }
        }

        return null;
    }

    public void initSortRules(TableRowSorter<DefaultTableModel> sorter) {
        LongComparator comparator = new LongComparator();
        for (int i = 0; i < columnTypes.length; i++)
            if (columnTypes[i].equals(Integer.class) || columnTypes[i].equals(Long.class))
                sorter.setComparator(i, comparator);
    }

    private boolean sendUpdateCommand(Object aValue, int row, int column) {
        StudyGroup studyGroup = list.stream().filter(studyGroup1 -> studyGroup1.getId().equals(table.getModel().getValueAt(row, 0))).findFirst().get();
        Object[] objects = studyGroup.toObjectArray();
        objects[column] = aValue;
        studyGroup = StudyGroup.fromObjectArray(objects);
        CommandResult result = new UpdateCommand(client.getRequestSender()).executeWithObjectArgument(studyGroup.toObjectArguments());
        if (result.status == ResultStatus.OK) {
            return true;
        }

        showDialog(this, result.message);
        return false;
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - unknown
    private JButton addButton;
    private JScrollPane scrollPane1;
    private JTable table;
    private JButton logOutButton;
    private JButton addIfMinButton;
    private JLabel localeLabel;
    private JComboBox localeBox;
    private JButton clearButton;
    private JButton countButton;
    private JLabel columnLabel;
    private JComboBox columnBox;
    private JButton executeScriptButton;
    private JLabel criterionLabel;
    private JComboBox criterionBox;
    private JButton helpButton;
    private JLabel valueLabel;
    private JTextField filterField;
    private JButton infoButton;
    private JButton removeByIdButton;
    private JButton removeGreaterButton;
    private JButton removeLowerButton;
    private JButton updateButton;
    private JButton visualizeButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables

    private final String[] columns = new String[] {
            "id",
            "name",
            "x",
            "y",
            "creationDate",
            "studentsCount",
            "expelledStudents",
            "shouldBeExpelled",
            "formOfEducation",
            "admin",
            "weight",
            "passport",
            "admin X",
            "admin Y",
            "location"
    };

    private final Class<?>[] columnTypes = new Class<?>[] {
            Integer.class, String.class, Integer.class, Long.class, String.class, Integer.class, Long.class, Integer.class, FormOfEducation.class, String.class, Long.class, String.class, Integer.class, Integer.class, String.class
    };
    private final boolean[] columnEditable = new boolean[] {
            false, true, true, true, false, true, true, true, true, true, true, true, true, true, true
    };
    private final Validatable[] columnValidatable = new Validatable[] {
            null, FieldValidator::validateGroupName, FieldValidator::validateCoordinateX, FieldValidator::validateCoordinateY,
            null, FieldValidator::validateStudentsCount, FieldValidator::validateExpelledStudents, FieldValidator::validateShouldBeExpelled,
            FieldValidator::validateFormOfEducation, FieldValidator::validateAdminName, FieldValidator::validateAdminWeight, FieldValidator::validateAdminPassport,
            FieldValidator::validateLocationX, FieldValidator::validateLocationY, FieldValidator::validateLocationName
    };

    class LongComparator implements Comparator<Number> {

        @Override
        public int compare(Number o1, Number o2) {
            return (int) (o1.doubleValue() - o2.doubleValue());
        }
    }
}
