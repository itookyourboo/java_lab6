/*
 * Created by JFormDesigner on Thu May 20 12:02:16 MSK 2021
 */

package client.gui;

import java.awt.event.*;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.*;

import client.Client;
import client.UIController;
import client.impl.OnStudyGroupChangeListener;
import client.util.LocaleManager;
import client.util.Validator;
import client.gui.FieldValidator.Field;
import client.gui.FieldValidator.ValidationResult;
import common.exceptions.MustBeNotEmptyException;
import common.exceptions.NotInBoundsException;
import common.model.*;
import net.miginfocom.swing.*;

/**
 * @author unknown
 */
public class StudyGroupWindow extends JPanel implements CustomWindow {

    private Client client;
    private boolean update = false;
    private OnStudyGroupChangeListener onStudyGroupChangeListener;
    private List<StudyGroup> studyGroupList;

    public StudyGroupWindow(Client client, boolean update) {
        this.client = client;
        this.update = update;
        initComponents();
        localize(LocaleManager.getLanguage());
    }

    public void setOnStudyGroupChangeListener(OnStudyGroupChangeListener onStudyGroupChangeListener) {
        this.onStudyGroupChangeListener = onStudyGroupChangeListener;
    }

    public void setData(List<StudyGroup> studyGroupList) {
        this.studyGroupList = studyGroupList;
    }

    private void cancelButtonMouseReleased(MouseEvent e) {
        if (onStudyGroupChangeListener != null)
            onStudyGroupChangeListener.onChanged(null);
    }

    private void okButtonMouseReleased(MouseEvent e) {
        StudyGroup newStudyGroup = validateStudyGroup();

        if (newStudyGroup == null) return;

        if (onStudyGroupChangeListener != null)
            onStudyGroupChangeListener.onChanged(newStudyGroup);
    }

    private StudyGroup validateStudyGroup() {
        StringBuilder stringBuilder = new StringBuilder();
        String groupName = "", adminName = "", adminLocationName = "", adminPassport = "";
        int id = 0, x = 0, studentsCount = 0, shouldBeExpelled = 0, adminX = 0, adminY = 0;
        long y = 0, expelledStudents = 0, adminWeight = 0;
        FormOfEducation formOfEducation = FormOfEducation.valueOf(String.valueOf(formOfEducationBox.getSelectedItem()));
        
        if (update) {
            ValidationResult<Integer> result = FieldValidator.validateID(idField.getText());
            if (appendErrors(stringBuilder, result))
                id = result.result;
        }

        ValidationResult<String> stringResult = FieldValidator.validateGroupName(nameField.getText());
        if (appendErrors(stringBuilder, stringResult))
            groupName = stringResult.result;

        ValidationResult<Integer> intResult = FieldValidator.validateCoordinateX(xField.getText());
        if (appendErrors(stringBuilder, intResult))
            x = intResult.result;

        ValidationResult<Long> longResult = FieldValidator.validateCoordinateY(yField.getText());
        if (appendErrors(stringBuilder, longResult))
            y = longResult.result;

        intResult = FieldValidator.validateStudentsCount(studentsCountField.getText());
        if (appendErrors(stringBuilder, intResult))
            studentsCount = intResult.result;

        longResult = FieldValidator.validateExpelledStudents(expelledStudentsField.getText());
        if (appendErrors(stringBuilder, longResult))
            expelledStudents = longResult.result;

        intResult = FieldValidator.validateShouldBeExpelled(shouldBeExpelledField.getText());
        if (appendErrors(stringBuilder, intResult))
            shouldBeExpelled = intResult.result;

        stringResult = FieldValidator.validateAdminName(nameField.getText());
        if (appendErrors(stringBuilder, stringResult))
            adminName = stringResult.result;

        longResult = FieldValidator.validateAdminWeight(adminWeightField.getText());
        if (appendErrors(stringBuilder, longResult))
            adminWeight = longResult.result;

        stringResult = FieldValidator.validateAdminPassport(adminPassportField.getText());
        if (appendErrors(stringBuilder, stringResult))
            adminPassport = stringResult.result;

        intResult = FieldValidator.validateLocationX(adminXField.getText());
        if (appendErrors(stringBuilder, intResult))
            adminX = intResult.result;

        intResult = FieldValidator.validateLocationY(adminYField.getText());
        if (appendErrors(stringBuilder, intResult))
            adminY = intResult.result;

        stringResult = FieldValidator.validateLocationName(adminLocationField.getText());
        if (appendErrors(stringBuilder, stringResult))
            adminLocationName = stringResult.result;

        if (!stringBuilder.toString().isEmpty()) {
            showDialog(this, stringBuilder.toString());
            return null;
        }

        StudyGroup newStudyGroup = new StudyGroup(
                groupName,
                new Coordinates(
                        x,
                        y
                ),
                ZonedDateTime.now(),
                studentsCount,
                expelledStudents,
                shouldBeExpelled,
                formOfEducation,
                new Person(
                        adminName,
                        adminWeight,
                        adminPassport,
                        new Location(
                                adminX,
                                adminY,
                                adminLocationName
                        )
                )
        );
        if (update) newStudyGroup.setId(id);

        return newStudyGroup;
    }

    private boolean appendErrors(StringBuilder builder, ValidationResult<?> result) {
        if (!result.isCorrect) {
            builder.append(result.message).append("\n");
            return false;
        }
        return true;
    }

    private void idFieldKeyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            try {
                Integer id = Integer.parseInt(idField.getText());
                studyGroupList.stream().filter(studyGroup -> studyGroup.getId().equals(id)).forEach(this::fillFields);
            } catch (Exception exception) {
                exception.printStackTrace();
                showDialog(this, LocaleManager.getString("noId"));
            }
        }
    }

    private void formOfEducationBoxItemStateChanged(ItemEvent e) {
        // TODO add your code here
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - unknown
        idLabel = new JLabel();
        idField = new JTextField();
        nameLabel = new JLabel();
        nameField = new JTextField();
        adminNameLabel = new JLabel();
        adminNameField = new JTextField();
        xLabel = new JLabel();
        xField = new JTextField();
        adminWeightLabel = new JLabel();
        adminWeightField = new JTextField();
        yLabel = new JLabel();
        yField = new JTextField();
        adminPassportLabel = new JLabel();
        adminPassportField = new JTextField();
        studentCountLabel = new JLabel();
        studentsCountField = new JTextField();
        adminXLabel = new JLabel();
        adminXField = new JTextField();
        expelledStudentsLabel = new JLabel();
        expelledStudentsField = new JTextField();
        adminYLabel = new JLabel();
        adminYField = new JTextField();
        shouldBeExpelledLabel = new JLabel();
        shouldBeExpelledField = new JTextField();
        adminLocationLabel = new JLabel();
        adminLocationField = new JTextField();
        formOfEducationLabel = new JLabel();
        formOfEducationBox = new JComboBox();
        cancelButton = new JButton();
        okButton = new JButton();

        //======== this ========
        setBorder ( new javax . swing. border .CompoundBorder ( new javax . swing. border .TitledBorder (
        new javax . swing. border .EmptyBorder ( 0, 0 ,0 , 0) ,  "JF\u006frm\u0044es\u0069gn\u0065r \u0045va\u006cua\u0074io\u006e"
        , javax. swing .border . TitledBorder. CENTER ,javax . swing. border .TitledBorder . BOTTOM
        , new java. awt .Font ( "D\u0069al\u006fg", java .awt . Font. BOLD ,12 )
        ,java . awt. Color .red ) , getBorder () ) );  addPropertyChangeListener(
        new java. beans .PropertyChangeListener ( ){ @Override public void propertyChange (java . beans. PropertyChangeEvent e
        ) { if( "\u0062or\u0064er" .equals ( e. getPropertyName () ) )throw new RuntimeException( )
        ;} } );
        setLayout(new MigLayout(
            "hidemode 3,align center center,gapy 16",
            // columns
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
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
            "[]"));

        //---- idLabel ----
        idLabel.setText("ID:");
        idLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        idLabel.setHorizontalTextPosition(SwingConstants.CENTER);
        add(idLabel, "cell 2 0,alignx center,growx 0");

        //---- idField ----
        idField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                idFieldKeyPressed(e);
            }
        });
        add(idField, "cell 3 0");

        //---- nameLabel ----
        nameLabel.setText("Name:");
        nameLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        add(nameLabel, "cell 0 1");

        //---- nameField ----
        nameField.setColumns(10);
        add(nameField, "cell 1 1");

        //---- adminNameLabel ----
        adminNameLabel.setText("Admin name:");
        adminNameLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        add(adminNameLabel, "cell 4 1");

        //---- adminNameField ----
        adminNameField.setColumns(10);
        add(adminNameField, "cell 5 1");

        //---- xLabel ----
        xLabel.setText("X:");
        xLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        add(xLabel, "cell 0 2");
        add(xField, "cell 1 2");

        //---- adminWeightLabel ----
        adminWeightLabel.setText("Admin weight:");
        adminWeightLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        add(adminWeightLabel, "cell 4 2");
        add(adminWeightField, "cell 5 2");

        //---- yLabel ----
        yLabel.setText("Y:");
        yLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        add(yLabel, "cell 0 3");
        add(yField, "cell 1 3");

        //---- adminPassportLabel ----
        adminPassportLabel.setText("Admin passport:");
        adminPassportLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        add(adminPassportLabel, "cell 4 3");
        add(adminPassportField, "cell 5 3");

        //---- studentCountLabel ----
        studentCountLabel.setText("Students count:");
        studentCountLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        add(studentCountLabel, "cell 0 4");
        add(studentsCountField, "cell 1 4");

        //---- adminXLabel ----
        adminXLabel.setText("Admin X:");
        adminXLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        add(adminXLabel, "cell 4 4");
        add(adminXField, "cell 5 4");

        //---- expelledStudentsLabel ----
        expelledStudentsLabel.setText("Expelled students:");
        expelledStudentsLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        add(expelledStudentsLabel, "cell 0 5");
        add(expelledStudentsField, "cell 1 5");

        //---- adminYLabel ----
        adminYLabel.setText("Admin Y:");
        adminYLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        add(adminYLabel, "cell 4 5");
        add(adminYField, "cell 5 5");

        //---- shouldBeExpelledLabel ----
        shouldBeExpelledLabel.setText("Should be expelled:");
        shouldBeExpelledLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        add(shouldBeExpelledLabel, "cell 0 6");
        add(shouldBeExpelledField, "cell 1 6");

        //---- adminLocationLabel ----
        adminLocationLabel.setText("Admin location:");
        adminLocationLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        add(adminLocationLabel, "cell 4 6");
        add(adminLocationField, "cell 5 6");

        //---- formOfEducationLabel ----
        formOfEducationLabel.setText("Form of education:");
        formOfEducationLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        add(formOfEducationLabel, "cell 0 7");

        //---- formOfEducationBox ----
        formOfEducationBox.addItemListener(e -> formOfEducationBoxItemStateChanged(e));
        add(formOfEducationBox, "cell 1 7");

        //---- cancelButton ----
        cancelButton.setText("Cancel");
        cancelButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                cancelButtonMouseReleased(e);
            }
        });
        add(cancelButton, "cell 2 8");

        //---- okButton ----
        okButton.setText("OK");
        okButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                okButtonMouseReleased(e);
            }
        });
        add(okButton, "cell 3 8");
        // JFormDesigner - End of component initialization  //GEN-END:initComponents

        if (!update) {
            idLabel.setVisible(false);
            idField.setVisible(false);
        }

        formOfEducationBox.setModel(new DefaultComboBoxModel(Arrays.stream(FormOfEducation.values()).toArray()));
    }

    private void fillFields(StudyGroup studyGroup) {
        Map<JTextField, Object> map = new HashMap<>();
        map.put(idField, studyGroup.getId());
        map.put(nameField, studyGroup.getName());
        map.put(xField, studyGroup.getCoordinates().getX());
        map.put(yField, studyGroup.getCoordinates().getY());
        map.put(studentsCountField, studyGroup.getStudentsCount());
        map.put(expelledStudentsField, studyGroup.getExpelledStudents());
        map.put(shouldBeExpelledField, studyGroup.getShouldBeExpelled());
        map.put(adminNameField, studyGroup.getGroupAdmin().getName());
        map.put(adminWeightField, studyGroup.getGroupAdmin().getWeight());
        map.put(adminPassportField, studyGroup.getGroupAdmin().getPassportID());
        map.put(adminXField, studyGroup.getGroupAdmin().getLocation().getX());
        map.put(adminYField, studyGroup.getGroupAdmin().getLocation().getY());
        map.put(adminLocationField, studyGroup.getGroupAdmin().getLocation().getName());

        for (Map.Entry<JTextField, Object> entry : map.entrySet()) {
            entry.getKey().setText(String.valueOf(entry.getValue()));
        }

        //TODO: setComboBoxValue
    }

    @Override
    public void localize(LocaleManager.Lang lang) {
        LocaleManager.setLanguage(lang);

        UIController.setTitle(LocaleManager.getString("studyGroupWindow") + " [" + client.getRequestSender().getUser().getUsername() + "]");

        idLabel.setText(LocaleManager.getString("id"));
        nameLabel.setText(LocaleManager.getString("name"));
        adminNameLabel.setText(LocaleManager.getString("adminName"));
        xLabel.setText(LocaleManager.getString("x"));
        adminWeightLabel.setText(LocaleManager.getString("adminWeight"));
        yLabel.setText(LocaleManager.getString("y"));
        adminPassportLabel.setText(LocaleManager.getString("adminPassport"));
        studentCountLabel.setText(LocaleManager.getString("studentsCount"));
        adminXLabel.setText(LocaleManager.getString("adminX"));
        expelledStudentsLabel.setText(LocaleManager.getString("expelledStudents"));
        adminYLabel.setText(LocaleManager.getString("adminY"));
        shouldBeExpelledLabel.setText(LocaleManager.getString("shouldBeExpelled"));
        adminLocationLabel.setText(LocaleManager.getString("adminLocation"));
        formOfEducationLabel.setText(LocaleManager.getString("formOfEducation"));
        cancelButton.setText(LocaleManager.getString("cancel"));
        okButton.setText(LocaleManager.getString("ok"));
    }

    @Override
    public void clearFields() {
        JTextField[] fields = new JTextField[] {idField, nameField, adminNameField, xField, adminWeightField, yField,
                adminPassportField, studentsCountField, adminXField, expelledStudentsField, adminYField, shouldBeExpelledField,
                adminLocationField
        };
        for (JTextField field: fields) field.setText("");
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - unknown
    private JLabel idLabel;
    private JTextField idField;
    private JLabel nameLabel;
    private JTextField nameField;
    private JLabel adminNameLabel;
    private JTextField adminNameField;
    private JLabel xLabel;
    private JTextField xField;
    private JLabel adminWeightLabel;
    private JTextField adminWeightField;
    private JLabel yLabel;
    private JTextField yField;
    private JLabel adminPassportLabel;
    private JTextField adminPassportField;
    private JLabel studentCountLabel;
    private JTextField studentsCountField;
    private JLabel adminXLabel;
    private JTextField adminXField;
    private JLabel expelledStudentsLabel;
    private JTextField expelledStudentsField;
    private JLabel adminYLabel;
    private JTextField adminYField;
    private JLabel shouldBeExpelledLabel;
    private JTextField shouldBeExpelledField;
    private JLabel adminLocationLabel;
    private JTextField adminLocationField;
    private JLabel formOfEducationLabel;
    private JComboBox formOfEducationBox;
    private JButton cancelButton;
    private JButton okButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
